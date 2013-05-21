/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

/**
 * A receive asynchronous activity is intended to receive a single incoming transmission which
 * contains a SOAP body (a normal message, or a SOAP fault).
 * 
 * An asynchronous receive must always signal an "OK" back to the client (unless a severe server
 * configuration error occurred). The specification does NOT allow to send back meaningful SOAP
 * Faults.
 * 
 * Receive Async can be used inside an asynchronous send/receive, receive/send, or receive only
 * block. There are two constructors to accomodate these usages; one for directly initializing the
 * activiy as a child of a parent track, and one for initializing the activity as a child of another
 * (asynchronous two way) activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveAsync extends Activity {

	/**
	 * The parent activity, if there is one.
	 */
	private TwoWayAsyncActivity fParentActivity;

	/**
	 * The receive specification handling the actual receive
	 */
	private ReceiveDataSpecification fReceiveSpec;

	// ************************** Initialization ************************

	public ReceiveAsync(PartnerTrack partnerTrack) {
		super(partnerTrack);
		fParentActivity= null;
	}

	public ReceiveAsync(TwoWayAsyncActivity activity) {
		super(activity.getPartnerTrack());
		fParentActivity= activity;
	}

	public void initialize(ReceiveDataSpecification spec) {
		fReceiveSpec= spec;
		setStatus(ArtefactStatus.createInitialStatus());
	}

	// ***************************** Activity **************************

	@Override
	public void runInternal(ActivityContext context) {

		IncomingMessage incoming;
		try {
			incoming= context.receiveMessage(this.getPartnerTrack());
		} catch (TimeoutException e) {
			setStatus(ArtefactStatus.createErrorStatus("Timeout while waiting for incoming asynchronous message", e));
			return;
		} catch (InterruptedException e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborted while waiting for incoming asynchronous messsage", e));
			return;
		}

		fReceiveSpec.handle(context, incoming);

		/*
		 * 
		 * Note that there is no way of indicating an error during processing of an asynchronous
		 * receive. Sending back fault data is not acceptable:
		 * 
		 * From the WS-I spec:
		 * 
		 * 5.6.10 One-Way Operations
		 * 
		 * There are differing interpretations of how HTTP is to be used when performing one-way
		 * operations.
		 * 
		 * R2714 For one-way operations, an INSTANCE MUST NOT return a HTTP response that contains a
		 * SOAP envelope. Specifically, the HTTP response entity-body must be empty.
		 * 
		 * R2750 A CONSUMER MUST ignore a SOAP response carried in a response from a one-way
		 * operation.
		 * 
		 * R2727 For one-way operations, a CONSUMER MUST NOT interpret a successful HTTP response
		 * status code (i.e., 2xx) to mean the message is valid or that the receiver would process
		 * it.
		 * 
		 * One-way operations do not produce SOAP responses. Therefore, the Profile prohibits
		 * sending a SOAP envelope in response to a one-way operation. This means that transmission
		 * of one-way operations can not result in processing level responses or errors.
		 * 
		 */

		try {
			OutgoingMessage outgoing= new OutgoingMessage();
			// always accept.
			outgoing.setCode(202);
			//outgoing.setBody("");
			context.postAnswer(this.getPartnerTrack(), outgoing);

			if (fReceiveSpec.hasProblems()) {
				setStatus(fReceiveSpec.getStatus());
			} else {
				setStatus(ArtefactStatus.createPassedStatus());
			}
		} catch (TimeoutException e) {
			setStatus(ArtefactStatus.createErrorStatus("Timeout occurred while waiting for ACK for asynchronous receive.", e));
		} catch (InterruptedException e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborted while waiting for ACK for asynchronous receive to be sent.", e));
		}
	}

	@Override
	public String getActivityCode() {
		return "ReceiveAsync";
	}

	@Override
	public int getActivityCount() {
		return 1;
	}

	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Receive Asynchronous";
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
		children.add(fReceiveSpec);
		return children;
	}

}
