/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.exception.SynchronousSendException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

/**
 * A send/receive synchronous activity is intended to send a outgoing synchronous SOAP message,
 * expect an answer within the same HTTP connection, and process the answer.
 * 
 * A send/receive synchronous activity will always get an answer to the service, even if it is only
 * a SOAP fault indicating something went wrong.
 * 
 * The combined send/receive activity has the optional ability of header processing.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendReceiveSync extends TwoWaySyncActivity {

	/**
	 * The send async activity may receive a body with a non-2XX and non-5XX result code, which
	 * should be presented to the user.
	 */
	private String fWrongReturnBody;


	// ********************** Initialization ***************************

	public SendReceiveSync(PartnerTrack partnerTrack) {
		super(partnerTrack);
		fStatus= ArtefactStatus.createInitialStatus();
	}


	// ***************************** Activity **************************

	@Override
	public void run(ActivityContext context) {

		context.setHeaderProcessor(fHeaderProcessor);

		fSendSpec.handle(context);

		if (fSendSpec.hasProblems()) {
			fStatus= fSendSpec.getStatus();
			return;
		}

		OutgoingMessage msg= new OutgoingMessage();
		msg.setTargetURL(fSendSpec.getTargetURL());
		msg.setSOAPAction(fSendSpec.getSOAPHTTPAction());
		msg.setBody(fSendSpec.getInWireFormat());

		IncomingMessage returnMsg;
		try {
			fSendSpec.delay();
			returnMsg= context.sendMessage(msg);
		} catch (SynchronousSendException e) {
			fStatus= ArtefactStatus.createErrorStatus("HTTP Error while sending out synchronous message: " + e.getMessage(), e);
			return;
		} catch (InterruptedException e) {
			// Interruption by another thread. Abort.
			fStatus= ArtefactStatus.createAbortedStatus("Aborted due to error in other partner track", e);
			return;
		}

		if (returnMsg.getReturnCode() == 200) {
			// Send is ok
			fReceiveSpec.handle(context, returnMsg.getBody());
			fStatus= fReceiveSpec.getStatus();
		} else if ( (returnMsg.getReturnCode() >= 500 && returnMsg.getReturnCode() < 600)) {
			// could be SOAP fault. Let receive handle it.
			fReceiveSpec.handle(context, returnMsg.getBody());
			fStatus= fReceiveSpec.getStatus();
		} else {
			// something went really wrong
			fStatus= ArtefactStatus
					.createErrorStatus("Error: Answer from synchronous call had non-expected return code " + returnMsg.getReturnCode());

			fWrongReturnBody= returnMsg.getBody();
			if ("".equals(fWrongReturnBody))
				fWrongReturnBody= "(empty)";
		}
	}

	@Override
	public String getActivityCode() {
		return "SendReceiveSync";
	}


	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Send/Receive Synchronous";
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children= new ArrayList<ITestArtefact>();
		children.add(fSendSpec);
		children.add(fReceiveSpec);
		return children;
	}

	@Override
	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(fStatus.getAsStateData());
		if (fWrongReturnBody != null)
			stateData.add(new StateData("Return Body", fWrongReturnBody));
		return stateData;
	}

}
