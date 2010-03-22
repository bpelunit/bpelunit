/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.velocity.VelocityContext;
import org.bpelunit.framework.control.ext.IHeaderProcessor;
import org.bpelunit.framework.control.ext.SendPackage;
import org.bpelunit.framework.control.run.TestCaseRunner;
import org.bpelunit.framework.exception.HeaderProcessingException;
import org.bpelunit.framework.exception.SynchronousSendException;
import org.bpelunit.framework.model.test.PartnerTrack;
import org.bpelunit.framework.model.test.data.DataCopyOperation;
import org.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import org.bpelunit.framework.model.test.data.SendDataSpecification;
import org.bpelunit.framework.model.test.wire.IncomingMessage;
import org.bpelunit.framework.model.test.wire.OutgoingMessage;
import org.w3c.dom.Element;

/**
 * An activity context is a contextual object created for a single enclosing top-level activity
 * (i.e. an activity directly beneath a partnerTrack, not a nested activity).
 * 
 * Through this object, activities and registered extensions like encoders and headers are able to
 * access framework functionality for sending and receiving SOAP calls, and storing/reading metadata
 * like header adressing information or copied values.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ActivityContext {

	/**
	 * Runner for the current test case
	 */
	private TestCaseRunner fRunner;

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
	private Map<String, String> fUserData;

	/**
	 * The URL simulated by the current partner
	 */
	private String fSimulatedURL;

	private Element fIncomingMessage;

	private VelocityContext fVelocityContext;



	// ****************************** Initialization ****************************

	public ActivityContext(TestCaseRunner runner, PartnerTrack track) {
		fRunner= runner;
		fTrack= track;
		fUserData= new HashMap<String, String>();
		fSimulatedURL= fTrack.getPartner().getSimulatedURL();
		fVelocityContext = setupVelocityContext();
	}

	/**
	 * Test Constructor
	 */
	public ActivityContext(String simulatedURL) {
		fRunner= null;
		fTrack= null;
		fUserData= new HashMap<String, String>();
		fSimulatedURL= simulatedURL;
		fVelocityContext = new VelocityContext();
	}

	private VelocityContext setupVelocityContext() {
		VelocityContext ctx = new VelocityContext(fRunner.getVelocityContext());
		ctx.put("partnerTrackName", fTrack.getRawName());
		ctx.put("partnerTrackURL", fTrack.getPartner().getSimulatedURL());
		return ctx;
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
		if (fHeaderProcessor != null)
			fHeaderProcessor.processReceive(this, spec.getSOAPMessage());
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
	 * Returns the received message. Might be null if no message has been
	 * received yet (for instance, in a <sendReceive> activity). The received
	 * message is mostly useful for the VelocityContexts of the <receiveSend>
	 * and <receiveSendAsynchronous> activities.
	 *
	 * @return received message
	 */
	public Element getIncomingMessage() {
		return fIncomingMessage;
	}
	
	/**
	 * Saves the message received for later use by the VelocityContext of the
	 * <send> activity. Useful for <receiveSend> and <receiveSendAsynchronous>
	 * activities.
	 *
	 * @param incomingMessage Incoming message.
	 */
	public void setIncomingMessage(Element incomingMessage) {
		fIncomingMessage = incomingMessage;
	}

	// **************************** Velocity ********************************

	/**
	 * Returns the partner track-wide Velocity context. It should be wrapped into
	 * another VelocityContext for every activity, to avoid modifying it by
	 * mistake.
	 * @return Base VelocityContext for the partner track, which wraps the
	 * test case VelocityContext, which wraps the test suite VelocityContext in
	 * turn.
	 */
	public VelocityContext getVelocityContext() {
		return fVelocityContext;
	}
}
