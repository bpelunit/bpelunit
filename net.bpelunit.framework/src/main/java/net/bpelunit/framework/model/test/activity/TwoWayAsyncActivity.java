/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.List;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.report.ITestArtefact;

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
	private SendAsync fSendAsync;

	/**
	 * Receive part
	 */
	private ReceiveAsync fReceiveAsync;

	/**
	 * Registered Header Processor, if any (may be null)
	 */
	private IHeaderProcessor fHeaderProcessor;

	/**
	 * Registered Mappings, if any (may be null). Note that this really only makes sense in the case
	 * of Receive/Send
	 */
	private List<DataCopyOperation> fMapping;


	// ********************************* Initialization ****************************

	protected SendAsync getSendAsync() {
		return fSendAsync;
	}

	protected ReceiveAsync getReceiveAsync() {
		return fReceiveAsync;
	}

	protected IHeaderProcessor getHeaderProcessor() {
		return fHeaderProcessor;
	}

	protected List<DataCopyOperation> getMapping() {
		return fMapping;
	}

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
