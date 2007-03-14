package coverage.bpelunitext;

/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.bpelunit.framework.model.test.PartnerTrack;
import org.bpelunit.framework.model.test.TestCase;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;
import org.bpelunit.framework.model.test.wire.IncomingMessage;
import org.bpelunit.framework.model.test.wire.OutgoingMessage;

/**
 * A receive asynchronous activity is intended to receive a single incoming
 * transmission which contains a SOAP body (a normal message, or a SOAP fault).
 * 
 * An asynchronous receive must always signal an "OK" back to the client (unless
 * a severe server configuration error occurred). The specification does NOT
 * allow to send back meaningful SOAP Faults.
 * 
 * Receive Async can be used inside an asynchronous send/receive, receive/send,
 * or receive only block. There are two constructors to accomodate these usages;
 * one for directly initializing the activiy as a child of a parent track, and
 * one for initializing the activity as a child of another (asynchronous two
 * way) activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceivePermanent extends Activity {

	/**
	 * The receive specification handling the actual receive
	 */
	private ReceiveDataForLogSpecification fReceiveSpec;

	private boolean interrupt;

	private Logger fLogger;

	// ************************** Initialization ************************

	public ReceivePermanent(PartnerTrack partnerTrack) {
		super(partnerTrack);
		interrupt = false;
		fLogger = Logger.getLogger(getClass());
	}

	public void initialize(ReceiveDataForLogSpecification spec) {
		fReceiveSpec = spec;
		fStatus = ArtefactStatus.createInitialStatus();
	}

	// ***************************** Activity **************************

	@Override
	public void run(ActivityContext context) {

		while (!interrupt) {
			IncomingMessage incoming;
			try {
				fLogger
						.info("--------LogService wartet auf Message ----------");
				incoming = context.receiveMessage(this.getPartnerTrack());
				fReceiveSpec.setTestCase(((TestCase)getPartnerTrack().getParent()));
				fReceiveSpec.handle(context, incoming.getBody());
				if (fReceiveSpec.isLastMessageReceived()) {
					interrupt = true;
					fLogger.info("--------Last Message received");
				}
			} catch (TimeoutException e) {
				fLogger.info("--------TimeoutException");
				fStatus = ArtefactStatus
						.createErrorStatus(
								"Timeout while waiting for incoming asynchronous message",
								e);
				interrupt = true;
				// fStatus= ArtefactStatus.createErrorStatus("Timeout occurred
				// while waiting for ACK for asynchronous receive.", e);
			} catch (InterruptedException e) {

				fLogger.info("--------InterruptedException");
				fStatus = ArtefactStatus
						.createAbortedStatus(
								"Aborted while waiting for incoming asynchronous messsage",
								e);
				interrupt = true;
				// interrupt=true;
				// fStatus= ArtefactStatus.createAbortedStatus("Aborted while
				// waiting for ACK for asynchronous receive to be sent.", e);
			}
			try{
				OutgoingMessage outgoing = new OutgoingMessage();
				// always accept.
				outgoing.setCode(202);
				outgoing.setBody("");
				context.postAnswer(this.getPartnerTrack(), outgoing);
				// if (fReceiveSpec.hasProblems())
				// fStatus= fReceiveSpec.getStatus();
				// else
				// fStatus= ArtefactStatus.createPassedStatus();
			} catch (TimeoutException e) {
				fStatus= ArtefactStatus.createErrorStatus("Timeout occurred while waiting for ACK for asynchronous receive.", e);
				interrupt = true;
			} catch (InterruptedException e) {
				fStatus= ArtefactStatus.createAbortedStatus("Aborted while waiting for ACK for asynchronous receive to be sent.", e);
				interrupt = true;
			}


		}
	}

	@Override
	public String getActivityCode() {
		return "ReceivePermanent";
	}

	@Override
	public int getActivityCount() {
		return 1;
	}

	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Receive Permanent";
	}

	@Override
	public ITestArtefact getParent() {
		return getPartnerTrack();
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children = new ArrayList<ITestArtefact>();
		children.add(fReceiveSpec);
		return children;
	}

	public void interruptActivity() {
		interrupt = true;
	}


}
