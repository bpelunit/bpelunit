/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import org.bpelunit.framework.model.test.PartnerTrack;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;

/**
 * A send/receive asynchronous activity is a combination of an asynchronous send and an asynchronous
 * receive chained together.
 * 
 * The combined activites has the option ability of header processing.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendReceiveAsync extends TwoWayAsyncActivity {


	// ************************** Initialization ************************

	public SendReceiveAsync(PartnerTrack partnerTrack) {
		super(partnerTrack);
		fStatus= ArtefactStatus.createInitialStatus();
	}

	// ***************************** Activity **************************

	@Override
	public void run(ActivityContext context) {

		context.setHeaderProcessor(fHeaderProcessor);

		fSendAsync.run(context);
		reportProgress(fSendAsync);

		if (fSendAsync.hasProblems()) {
			// The send failed
			// Abort
			fStatus= fSendAsync.getStatus();
			return;
		}

		fReceiveAsync.run(context);
		reportProgress(fReceiveAsync);

		if (fReceiveAsync.hasProblems()) {
			fStatus= fReceiveAsync.getStatus();
			return;
		}

		fStatus= ArtefactStatus.createPassedStatus();
	}

	@Override
	public String getActivityCode() {
		return "SendReceiveAsync";
	}

	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Send/Receive Asynchronous";
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children= new ArrayList<ITestArtefact>();
		children.add(fSendAsync);
		children.add(fReceiveAsync);
		return children;
	}

}
