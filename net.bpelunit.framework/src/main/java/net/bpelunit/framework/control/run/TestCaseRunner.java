/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.run;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.control.ws.LocalHTTPServer;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.PartnerNotFoundException;
import net.bpelunit.framework.exception.SynchronousSendException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * 
 * The TestCaseRunner handles kicking off the partnerTrack threads; it handles
 * communication between threads as well as network communication and aborting
 * in case of an error, failure, or user interaction.
 * 
 * @author Philip Mayer
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public class TestCaseRunner implements VelocityContextProvider {

	private enum PartnerTrackResult {
		RUNNING, COMPLETED
	};
	
	// Data
	private TestCase fTestCase;

	private Map<PartnerTrack, PartnerTrackResult> fPartnerTracks;

	private List<String> executedActivities;
	
	private boolean fProblemOccurred;

	// Thread communication
	private BlackBoard<PartnerTrack, IncomingMessage> fIncomingBlackboard;

	private BlackBoard<PartnerTrack, OutgoingMessage> fOutgoingBlackboard;

	private BlackBoard<OutgoingMessage, Boolean> fSentBlackBoard;

	// Connection stuff
	private LocalHTTPServer fServer;

	// Other stuff
	private Logger fLogger;

	private boolean fAbortedByUser;

	private HttpClient fClient;

	public TestCaseRunner(LocalHTTPServer localServer, TestCase caseToRun) {
		fTestCase = caseToRun;
		fServer = localServer;

		fProblemOccurred = false;
		fAbortedByUser = false;

		initializePartnerTracks(caseToRun);

		fIncomingBlackboard = new BlackBoard<PartnerTrack, IncomingMessage>();
		fOutgoingBlackboard = new BlackBoard<PartnerTrack, OutgoingMessage>();
		fSentBlackBoard = new BlackBoard<OutgoingMessage, Boolean>();

		executedActivities = new ArrayList<String>();
		
		fLogger = Logger.getLogger(getClass());
	}

	private void initializePartnerTracks(TestCase caseToRun) {
		List<PartnerTrack> partnerTracks = caseToRun.getPartnerTracks();
		for (PartnerTrack partnerTrack : partnerTracks) {
			partnerTrack.initialize(this);
		}
		fPartnerTracks = new HashMap<PartnerTrack, PartnerTrackResult>();
		for (PartnerTrack head : partnerTracks) {
			fPartnerTracks.put(head, PartnerTrackResult.RUNNING);
		}
	}

	public void run() {
		// Pool connections to avoid socket leaks
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		// Increase maximum per host, as most will use the same host (localhost)
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(10);

		fClient = new HttpClient(connectionManager);

		try {
			fLogger.info("Initiating testCase " + fTestCase.getName());
			fServer.startTest(this);

			final List<Thread> threads = new ArrayList<Thread>();
			startPartnerTracks(threads);
			waitForPartnerTracksOrError();

			if (fProblemOccurred || fAbortedByUser) {
				checkPartnerTracksForProblems();
				interruptAllThreads(threads);
				waitForPartnerTracks();
			} else {
				fLogger.info("Test case passed.");
			}
			fLogger.debug("All threads returned.");

			fLogger.info("Stopping testCase " + fTestCase.getName());
			fServer.stopTest(this);
		}
		finally {
			connectionManager.shutdown();
		}
	}

	private void startPartnerTracks(final List<Thread> threads) {
		for (PartnerTrack partnerTrack : fPartnerTracks.keySet()) {
			Thread trackThread = new Thread(partnerTrack,
					partnerTrack.getPartnerName());
			fLogger.debug("Now starting thread for partner "
					+ partnerTrack.getPartnerName());
			trackThread.start();
			threads.add(trackThread);
		}
		fLogger.info("TestCase was started.");
	}

	private void checkPartnerTracksForProblems() {
		for (PartnerTrack partnerTrack : fPartnerTracks.keySet()) {
			if (partnerTrack.getStatus().isError()
					|| partnerTrack.getStatus().isFailure()) {
				fLogger.info(String.format(
						"A test failure or error occurred on %s: %s",
						partnerTrack.getName(), partnerTrack
								.getStatus().getMessage()));
			}
		}
	}

	private void interruptAllThreads(final List<Thread> threads) {
		fLogger.debug("Trying to interrupt all threads...");
		for (Thread t : threads) {
			if (t.isAlive()) {
				t.interrupt();
			}
		}
		fLogger.debug("All threads interrupted. Waiting for threads...");
	}

	public synchronized PartnerTrack findPartnerTrackForName(String name)
			throws PartnerNotFoundException {
		for (PartnerTrack partnerTrack : fPartnerTracks.keySet()) {
			if (partnerTrack.getPartnerName().equalsIgnoreCase(name)) {
				// found the partner track - but does she have activities?
				if (partnerTrack.getActivityCount() != 0) {
					return partnerTrack;
				} else {
					throw new PartnerNotFoundException(
							"Partner found, but zero activities for name "
									+ name);
				}
			}
		}
		throw new PartnerNotFoundException("No partner found for name " + name);
	}

	// ********************* Messaging ******************************

	/**
	 * Wait for an incoming message at the port and address registered to the
	 * given partner track.
	 * 
	 * This call will not return until either a message as been received, a
	 * timeout has occurred, or the thread was interrupted.
	 * 
	 * Note that the Web Service Handler expects an answer to every such
	 * incoming message. Use
	 * {@link #sendBackMessage(PartnerTrack, OutgoingMessage)} to send back a
	 * message.
	 * 
	 * @param partnerTrack
	 * @return
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public IncomingMessage receiveMessage(PartnerTrack partnerTrack)
			throws TimeoutException, InterruptedException {
		return fIncomingBlackboard.getObject(partnerTrack);
	}

	/**
	 * 
	 * Sends back a message as an answer to a previously-received incoming
	 * message via {@link #receiveMessage(PartnerTrack)}. The message will be
	 * sent back using the still-open HTTP Connection.
	 * 
	 * This call will not return until either the message has been delivered, or
	 * a timeout has occurred, or the thread was interrupted.
	 * 
	 * @param partnerTrack
	 * @param message
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public void sendBackMessage(PartnerTrack partnerTrack,
			OutgoingMessage message) throws TimeoutException,
			InterruptedException {
		fOutgoingBlackboard.putObject(partnerTrack, message);
		fSentBlackBoard.getObject(message);
	}

	/**
	 * 
	 * Sends a synchronous message, waits for the result, and returns the
	 * result. This method blocks until either a result has been retrieved, a
	 * send error has occurred, or the thread was interrupted.
	 * 
	 * @param message
	 *            the message to be sent
	 * @return the resulting incoming message
	 * @throws SynchronousSendException
	 * @throws InterruptedException
	 */
	public IncomingMessage sendMessageSynchronous(OutgoingMessage message)
			throws SynchronousSendException, InterruptedException {
		PostMethod method = new PostMethod(message.getTargetURL());

		// Set parameters:
		// -> Do not retry
		// -> Socket timeout to default timeout value.
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,
				Integer.valueOf(BPELUnitRunner.getTimeout()));

		method.addRequestHeader("SOAPAction", "\""
				+ message.getSOAPHTTPAction() + "\"");
		RequestEntity entity;
		try {
			entity = new StringRequestEntity(message.getMessageAsString(),
					BPELUnitConstants.TEXT_XML_CONTENT_TYPE,
					BPELUnitConstants.DEFAULT_HTTP_CHARSET);
		} catch (UnsupportedEncodingException e) {
			// cannot happen since we use the default HTTP Encoding.
			throw new SynchronousSendException(
					"Unsupported encoding when trying to post message to web service.",
					e);
		}
		
		for(String option : message.getProtocolOptionNames()) {
			method.addRequestHeader(option, message.getProtocolOption(option));
		}
		method.setRequestEntity(entity);

		try {
			// Execute the method.

			int statusCode = fClient.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();

			IncomingMessage returnMsg = new IncomingMessage();
			returnMsg.setStatusCode(statusCode);
			returnMsg.setMessage(in);

			return returnMsg;

		} catch (Exception e) {
			if (isAborting()) {
				throw new InterruptedException();
			} else {
				throw new SynchronousSendException(e);
			}
		} finally {
			// Release the connection.
			method.releaseConnection();
		}

	}

	// ************* Accessor functions for partner tracks

	/**
	 * This method signals that the given partner track has completed its
	 * actions without any fault. The partner track thread should terminate
	 * directly after calling this method.
	 * 
	 * @param track
	 */
	public synchronized void done(PartnerTrack track) {
		fPartnerTracks.put(track, PartnerTrackResult.COMPLETED);
		notifyAll();
	}

	/**
	 * This method signals that the given partner track has completed its
	 * actions with an error or fault. The partner track thread should terminate
	 * directly after calling this method.
	 * 
	 * @param track
	 */
	public synchronized void doneWithFault(PartnerTrack track) {
		fProblemOccurred = true;
		fPartnerTracks.put(track, PartnerTrackResult.COMPLETED);
		notifyAll();
	}

	// ********** Accessor functions for the web service handler *************

	public OutgoingMessage getWSOutgoingMessage(PartnerTrack head)
			throws TimeoutException, InterruptedException {
		return fOutgoingBlackboard.getObject(head);
	}

	public void putWSIncomingMessage(PartnerTrack head, IncomingMessage message)
			throws InterruptedException {
		fIncomingBlackboard.putObject(head, message);
	}

	public void putWSOutgoingMessageSent(OutgoingMessage message)
			throws InterruptedException {
		fSentBlackBoard.putObject(message, true);
	}

	// ********************* Waiting *******************************

	private synchronized void waitForPartnerTracksOrError() {
		while (!allPartnerTracksCompleted() && (!fProblemOccurred)
				&& (!fAbortedByUser)) {
			try {
				wait(BPELUnitConstants.TIMEOUT_SLEEP_TIME);
			} catch (InterruptedException e) {
			}
		}
	}

	private synchronized void waitForPartnerTracks() {
		while (!allPartnerTracksCompleted()) {
			try {
				wait(BPELUnitConstants.TIMEOUT_SLEEP_TIME);
			} catch (InterruptedException e) {
			}
		}
	}

	private synchronized boolean allPartnerTracksCompleted() {
		for (PartnerTrack head : fPartnerTracks.keySet()) {
			if (fPartnerTracks.get(head).equals(PartnerTrackResult.RUNNING)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isAborting() {
		return fProblemOccurred || fAbortedByUser;
	}

	public void abortTest() {
		/*
		 * Set aborted flag. Will get picked up by the runner in the right
		 * thread.
		 */
		fAbortedByUser = true;
	}

	public void markActivityAsExecuted(String activityId) {
		if(activityId != null) {
			executedActivities.add(activityId);
		}
	}
	
	public List<String> getExecutedActivities() {
		return new ArrayList<String>(this.executedActivities);
	}
	
	// ********************* Velocity contexts *********************

	public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException  {
		return fTestCase.createVelocityContext(artefact);
	}
}
