/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.exception.SynchronousSendException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.SendDataSpecification;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

/**
 * A send asynchronous activity is intended to send a single outgoing transmission which contains a
 * SOAP body (a normal message, or a SOAP fault).
 * 
 * An asynchronous send will always receive an "OK" back from the server (unless a severe server
 * configuration error occurred). The specification does NOT allow to send back meaningful SOAP
 * Faults, so no such faults will be received as an answer. However, as a severe server config error
 * may indeed result in a return body (though not necessarily a SOAP body), the activity provides
 * the means to gather and display this body as part of the status data. Note that such a body is
 * ALWAYS unexpected.
 * 
 * Send Async can be used inside an asynchronous send/receive, receive/send, or send only block.
 * There are two constructors to accomodate these usages; one for directly initializing the activiy
 * as a child of a parent track, and one for initializing the activity as a child of another
 * (asynchronous two way) activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendAsync extends Activity {

	/**
	 * The parent activity, if there is one.
	 */
	private TwoWayAsyncActivity fParentActivity;

	/**
	 * The send specification, which handles the actual send
	 */
	private SendDataSpecification fSendSpec;

	/**
	 * The send async activity may receive a body, which is illegal, but should be presented to the
	 * user.
	 */
	private String fWrongBody;

	/**
	 * Registered Header Processor, if any (may be null)
	 */
	private IHeaderProcessor fHeaderProcessor;

	// ************************** Initialization ************************

	public SendAsync(PartnerTrack partnerTrack) {
		super(partnerTrack);
		fParentActivity= null;
	}

	public SendAsync(TwoWayAsyncActivity activity) {
		super(activity.getPartnerTrack());
		fParentActivity= activity;
	}

	public void initialize(SendDataSpecification spec, IHeaderProcessor proc) {
		fSendSpec= spec;
		fHeaderProcessor = proc;
		setStatus(ArtefactStatus.createInitialStatus());
	}

	protected IHeaderProcessor getHeaderProcessor() {
		return fHeaderProcessor;
	}
	
	// ***************************** Activity **************************

	@Override
	public void runInternal(ActivityContext context) {

		/** 
		 * Necessary to guard this as a null value would override an existing header processor.
		 * This is damaging if this Send activity is part of a two-way async activity.
		 */
		if(getHeaderProcessor() != null) {
			context.setHeaderProcessor(getHeaderProcessor());
		}
		
		fSendSpec.handle(context);

		if (fSendSpec.hasProblems()) {
			setStatus(fSendSpec.getStatus());
			return;
		}

		OutgoingMessage msg= new OutgoingMessage();
		msg.setSOAPAction(fSendSpec.getSOAPHTTPAction());
		msg.setTargetURL(fSendSpec.getTargetURL());
		msg.setBody(fSendSpec.getSOAPMessage());
		Activity.copyProtocolOptions(fSendSpec, msg);

		IncomingMessage incoming;
		try {
			fSendSpec.delay(context);
			incoming= context.sendMessage(msg);
		} catch (SynchronousSendException e) {
			setStatus(ArtefactStatus.createErrorStatus("HTTP Error while sending out synchronous message!", e));
			return;
		} catch (InterruptedException e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborting due to error in another partner track.", e));
			return;
		} catch (Exception e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborted while computing the delay for the send.", e));
			return;
		}

		if (incoming.getReturnCode() > 200 && incoming.getReturnCode() < 300) {
			setStatus(ArtefactStatus.createPassedStatus());
		} else {
			// This is not possible unless there was a really grave error at the
			// server side.
			// Asynchronous receives may not throw a SOAP error.
			setStatus(ArtefactStatus.createErrorStatus("Asynchronous send got a non-2XX error code: " + incoming.getReturnCode(), null));
			fWrongBody= incoming.getMessageAsString();
		}
	}

	@Override
	public String getActivityCode() {
		return "SendAsync";
	}

	@Override
	public int getActivityCount() {
		return 1;
	}

	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Send Asynchronous";
	}

	@Override
	public ITestArtefact getParent() {
		if (fParentActivity != null) {
			return fParentActivity;
		} else {
			return getPartnerTrack();
		}
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children= new ArrayList<ITestArtefact>();
		children.add(fSendSpec);
		return children;
	}

	@Override
	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(getStatus().getAsStateData());
		if (fWrongBody != null) {
			stateData.add(new StateData("Return Body", fWrongBody));
		}
		return stateData;
	}
}
