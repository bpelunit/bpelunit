/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

/**
 * A receive/send synchronous activity is intended to receive a incoming synchronous SOAP message,
 * process it, and return an answer within the same HTTP connection.
 * 
 * A receive/send synchronous activity must return an answer to the caller, even if it is only a
 * SOAP fault indicating something went wrong.
 * 
 * The combined receive/send activity has the optional ability of header processing and copying
 * values from input to output.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveSendSync extends TwoWaySyncActivity {


	private static final int HTTP_OK = 200;
	private static final int HTTP_INTERNAL_ERROR = 500;

	// ********************************* Initialization ****************************


	public ReceiveSendSync(PartnerTrack partnerTrack) {
		super(partnerTrack);
		setStatus(ArtefactStatus.createInitialStatus());
	}


	// ***************************** Activity **************************

	@Override
	public void runInternal(ActivityContext context) {

		context.setHeaderProcessor(getHeaderProcessor());
		context.setMapping(getMapping());

		IncomingMessage incoming;
		try {
			incoming= context.receiveMessage(this.getPartnerTrack());
		} catch (TimeoutException e) {
			setStatus(ArtefactStatus.createErrorStatus("Timeout while waiting for incoming synchronous message", e));
			return;
		} catch (InterruptedException e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborted while waiting for incoming synchronous message", e));
			return;
		}

		getReceiveSpec().handle(context, incoming);

		/*
		 * This is the only place in the testing framework where we can (and should actually return
		 * a result even if the test failed.
		 */

		OutgoingMessage msg= new OutgoingMessage();
		Activity.copyProtocolOptions(getSendSpec(), msg);

		if (!getReceiveSpec().hasProblems()) {
			// Receive was successful

			getSendSpec().handle(context);

			if (!getSendSpec().hasProblems()) {
				if (getSendSpec().isFault()) {
					msg.setCode(HTTP_INTERNAL_ERROR);
				} else {
					msg.setCode(HTTP_OK);
				}
				msg.setBody(getSendSpec().getSOAPMessage());
			} else {
				// Could not successfully generate a return value for
				// whatever reason.
				msg.setCode(HTTP_INTERNAL_ERROR);
				msg.setBody(BPELUnitUtil.generateGenericSOAPFault());
			}
		} else {
			// Receive was not successful
			msg.setCode(HTTP_INTERNAL_ERROR);
			msg.setBody(BPELUnitUtil.generateGenericSOAPFault());
		}

		try {
			getSendSpec().delay(context);
			context.postAnswer(this.getPartnerTrack(), msg);

			if (getReceiveSpec().hasProblems()) {
				setStatus(getReceiveSpec().getStatus());
			} else if (getSendSpec().hasProblems()) {
				setStatus(getSendSpec().getStatus());
			} else {
				setStatus(ArtefactStatus.createPassedStatus());
			}
		} catch (TimeoutException e) {
			setStatus(ArtefactStatus.createErrorStatus("Timeout occurred while waiting for synchronous answer to be sent.", e));
			return;
		} catch (InterruptedException e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborted while waiting for synchronous answer to be sent.", e));
			return;
		} catch (Exception e) {
			setStatus(ArtefactStatus.createAbortedStatus("Aborted while computing the delay for the send.", e));
			return;
		}
	}

	@Override
	public String getActivityCode() {
		return "ReceiveSendSync";
	}


	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Receive/Send Synchronous";
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children= new ArrayList<ITestArtefact>();
		if (getMapping() != null) {
			for (DataCopyOperation copy : getMapping()) {
				children.add(copy);
			}
		}
		children.add(getReceiveSpec());
		children.add(getSendSpec());

		return children;
	}
}
