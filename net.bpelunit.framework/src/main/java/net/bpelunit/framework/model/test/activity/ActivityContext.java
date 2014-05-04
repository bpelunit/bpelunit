/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.SendPackage;
import net.bpelunit.framework.control.run.TestCaseRunner;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.exception.SynchronousSendException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.data.SendDataSpecification;
import net.bpelunit.framework.model.test.data.extraction.ExtractedDataContainerUtil;
import net.bpelunit.framework.model.test.data.extraction.IExtractedDataContainer;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

import org.w3c.dom.Element;

/**
 * An activity context is a contextual object created for a single enclosing top-level activity
 * (i.e. an activity directly beneath a partnerTrack, not a nested activity).
 * 
 * Through this object, activities and registered extensions like encoders and headers are able to
 * access framework functionality for sending and receiving SOAP calls, and storing/reading metadata
 * like header addressing information or copied values.
 * 
 * @author Philip Mayer
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public class ActivityContext implements VelocityContextProvider {

	/**
	 * Runner for the current test case
	 */
	private TestCaseRunner fRunner;

	public void markActivityAsExecuted(String activityId) {
		fRunner.markActivityAsExecuted(activityId);
	}

	public List<String> getExecutedActivities() {
		return fRunner.getExecutedActivities();
	}

	/**
	 * Current partner track
	 */
	private PartnerTrack fTrack;

	/**
	 * Registered header processor for the top-level activity
	 */
	private IHeaderProcessor fHeaderProcessor;

	/**
	 * The data copy operations associated with the top-level activity
	 */
	private List<DataCopyOperation> fMapping;

	/**
	 * Data attached to this context, may be written or read by registered data copy objects or the
	 * header processor.
	 */
	private Map<String, String> fUserData = new HashMap<String, String>();

	/**
	 * The URL simulated by the current partner
	 */
	private String fSimulatedURL;

	private List<Element> fReceivedMessages = new ArrayList<Element>();

	private List<Element> fSentMessages = new ArrayList<Element>();

	// ****************************** Initialization ****************************

	public ActivityContext(TestCaseRunner runner, PartnerTrack track) {
		fRunner= runner;
		fTrack= track;
		fSimulatedURL= fTrack.getPartner().getSimulatedURL();
	}

	/**
	 * Test Constructor
	 */
	public ActivityContext(String simulatedURL) {
		fRunner= null;
		fTrack= null;
		fSimulatedURL= simulatedURL;
	}

	// *********** Methods for sending and receiving messages *********


	public IncomingMessage receiveMessage(PartnerTrack track) throws TimeoutException, InterruptedException {
		return fRunner.receiveMessage(track);
	}

	public void postAnswer(PartnerTrack track, OutgoingMessage msg) throws TimeoutException, InterruptedException {
		fRunner.sendBackMessage(track, msg);
	}

	public IncomingMessage sendMessage(OutgoingMessage msg) throws SynchronousSendException, InterruptedException {
		return fRunner.sendMessageSynchronous(msg);
	}

	// *********************** Header Processing *************************

	public void setHeaderProcessor(IHeaderProcessor headerProcessor) {
		fHeaderProcessor= headerProcessor;
	}

	public void processHeaders(ReceiveDataSpecification spec) throws HeaderProcessingException {
		if (fHeaderProcessor != null) {
			fHeaderProcessor.processReceive(this, spec.getSOAPMessage());
		}
	}

	public void processHeaders(SendDataSpecification spec) throws HeaderProcessingException {
		if (fHeaderProcessor != null) {
			SendPackage sp= new SendPackage(spec.getTargetURL(), spec.getSOAPMessage());
			fHeaderProcessor.processSend(this, sp);
			spec.setTargetURL(sp.getTargetURL());
		}
	}

	// ************************* Mapping data ******************************

	public void setMapping(List<DataCopyOperation> mapping) {
		fMapping= mapping;
	}

	public List<DataCopyOperation> getMapping() {
		return fMapping;
	}


	// ***************************** User Data ******************************

	public void setUserData(String key, String value) {
		fUserData.put(key, value);
	}

	public String getUserData(String key) {
		String returner= fUserData.get(key);
		return (returner == null) ? "" : returner;
	}

	// **************************** Other ***********************************

	/**
	 * Returns the simulated URL of the partner process
	 */
	public String getPartnerURL() {
		return fSimulatedURL;
	}

	/**
	 * Sets a different simulated URL. Mainly for test purposes.
	 * 
	 * @param simulatedURL new simulated URL
	 */
	public void setSimulatedURL(String simulatedURL) {
		fSimulatedURL= simulatedURL;
	}

	/**
	 * Returns the last received message. Might be null if no message has been
	 * received yet (for instance, in a <sendReceive> activity). The received
	 * message is mostly useful for the VelocityContexts of the <receiveSend>
	 * and <receiveSendAsynchronous> activities.
	 *
	 * @return received message
	 */
	public Element getLastRequest() {
		return fReceivedMessages.size() > 0
			? fReceivedMessages.get(fReceivedMessages.size() - 1)
			: null;
	}

	/**
	 * Returns a list of all messages previously received in this partner track.
	 */
	public List<Element> getReceivedMessages() {
		return fReceivedMessages;
	}

	/**
	 * Saves the message received for later use by Velocity templates,
	 * assumptions and <receive> conditions.
	 *
	 * @param msg Incoming message.
	 */
	public void saveReceivedMessage(Element msg) {
		fReceivedMessages.add(msg);
	}

	/**
	 * Returns a list of all messages previously sent from this partner track.
	 */
	public List<Element> getSentMessages() {
		return fSentMessages;
	}

	/**
	 * Saves the message sent for later use by Velocity templates, assumptions
	 * and <receive> conditions.
	 *
	 * @param msg Outgoing message
	 */
	public void saveSentMessage(Element msg) {
		fSentMessages.add(msg);
	}

	// **************************** Velocity ********************************

	/**
	 * Creates a new Velocity context for this activity.
	 */
	public WrappedContext createVelocityContext(ITestArtefact artefact) throws DataSourceException {
		return fTrack.createVelocityContext(artefact);
	}
}
