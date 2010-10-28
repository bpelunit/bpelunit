/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;

/**
 * A receive/send asynchronous activity is a combination of an asynchronous receive and an
 * asynchronous send chained together.
 * 
 * The combined activities have the optional ability of header processing and copying values from
 * input to output.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveSendAsync extends TwoWayAsyncActivity {


	// ************************** Initialization ************************

	public ReceiveSendAsync(PartnerTrack partnerTrack) {
		super(partnerTrack);
		fStatus= ArtefactStatus.createInitialStatus();
	}


	// ***************************** Activity **************************

	@Override
	public void run(ActivityContext context) {

		context.setHeaderProcessor(fHeaderProcessor);
		context.setMapping(fMapping);

		fReceiveAsync.run(context);
		reportProgress(fReceiveAsync);

		/*
		 * Note that there is no way of indicating an error during processing of an asynchronous
		 * receive. Sending back fault data is not acceptable.
		 * 
		 * See ReceiveAsync class for more information.
		 * 
		 */

		if (fReceiveAsync.hasProblems()) {
			// The receive failed (either never received anything, or wrong
			// message)
			// Abort
			fStatus= fReceiveAsync.getStatus();
			return;
		}

		fSendAsync.run(context);
		reportProgress(fSendAsync);

		if (fSendAsync.hasProblems()) {
			fStatus= fSendAsync.getStatus();
			return;
		}

		fStatus= ArtefactStatus.createPassedStatus();
	}

	@Override
	public String getActivityCode() {
		return "ReceiveSendAsync";
	}

	// ************************** ITestArtefact ************************

	@Override
	public String getName() {
		return "Receive/Send Asynchronous";
	}

	@Override
	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> children= new ArrayList<ITestArtefact>();
		// Add copy information
		if (fMapping != null)
			for (DataCopyOperation copy : fMapping)
				children.add(copy);
		// Add activities
		children.add(fReceiveAsync);
		children.add(fSendAsync);
		return children;
	}

}
