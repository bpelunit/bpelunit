/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;

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
		setStatus(ArtefactStatus.createInitialStatus());
	}

	// ***************************** Activity **************************

	@Override
	public void runInternal(ActivityContext context) {

		context.setHeaderProcessor(getHeaderProcessor());

		getSendAsync().run(context);
		reportProgress(getSendAsync());

		if (getSendAsync().hasProblems()) {
			// The send failed
			// Abort
			setStatus(getSendAsync().getStatus());
			return;
		}

		getReceiveAsync().run(context);
		reportProgress(getReceiveAsync());

		if (getReceiveAsync().hasProblems()) {
			setStatus(getReceiveAsync().getStatus());
			return;
		}

		setStatus(ArtefactStatus.createPassedStatus());
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
		children.add(getSendAsync());
		children.add(getReceiveAsync());
		return children;
	}

}
