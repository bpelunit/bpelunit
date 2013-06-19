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
		setStatus(ArtefactStatus.createInitialStatus());
	}


	// ***************************** Activity **************************

	@Override
	public void runInternal(ActivityContext context) {

		context.setHeaderProcessor(getHeaderProcessor());
		context.setMapping(getMapping());

		getReceiveAsync().run(context);
		reportProgress(getReceiveAsync());

		/*
		 * Note that there is no way of indicating an error during processing of an asynchronous
		 * receive. Sending back fault data is not acceptable.
		 * 
		 * See ReceiveAsync class for more information.
		 * 
		 */

		if (getReceiveAsync().hasProblems()) {
			// The receive failed (either never received anything, or wrong
			// message)
			// Abort
			setStatus(getReceiveAsync().getStatus());
			return;
		}

		getSendAsync().run(context);
		reportProgress(getSendAsync());

		if (getSendAsync().hasProblems()) {
			setStatus(getSendAsync().getStatus());
			return;
		}

		setStatus(ArtefactStatus.createPassedStatus());
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
		if (getMapping() != null) {
			for (DataCopyOperation copy : getMapping()) {
				children.add(copy);
			}
		}
		// Add activities
		children.add(getReceiveAsync());
		children.add(getSendAsync());
		return children;
	}

}
