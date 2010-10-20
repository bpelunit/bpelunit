/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test.activity;

import java.util.List;

import org.bpelunit.framework.control.ext.IHeaderProcessor;
import org.bpelunit.framework.model.test.PartnerTrack;
import org.bpelunit.framework.model.test.data.DataCopyOperation;
import org.bpelunit.framework.model.test.report.ITestArtefact;

/**
 * Abstract superclass of the two-way asynchronous activites Receive/Send Asynchonrous and
 * Send/Receive Asynchronous.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class TwoWayAsyncActivity extends Activity {

	/**
	 * Send part
	 */
	protected SendAsync fSendAsync;

	/**
	 * Receive part
	 */
	protected ReceiveAsync fReceiveAsync;

	/**
	 * Registered Header Processor, if any (may be null)
	 */
	protected IHeaderProcessor fHeaderProcessor;

	/**
	 * Registered Mappings, if any (may be null). Note that this really only makes sense in the case
	 * of Receive/Send
	 */
	protected List<DataCopyOperation> fMapping;


	// ********************************* Initialization ****************************

	public TwoWayAsyncActivity(PartnerTrack partnerTrack) {
		super(partnerTrack);
	}

	public void initialize(SendAsync sendAct, ReceiveAsync receiveAct, IHeaderProcessor proc, List<DataCopyOperation> mapping) {
		fSendAsync= sendAct;
		fReceiveAsync= receiveAct;
		fHeaderProcessor= proc;
		fMapping= mapping;
	}

	// ***************************** Activity **************************

	@Override
	public int getActivityCount() {
		// The parent itself and the two children
		return 3;
	}

	// ************************** ITestArtefact ************************

	/**
	 * Returns the parent of this activity. This is always the partner track.
	 */
	@Override
	public ITestArtefact getParent() {
		return getPartnerTrack();
	}

}
