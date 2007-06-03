/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import org.bpelunit.toolsupport.editors.TestSuitePage;
import org.bpelunit.toolsupport.editors.wizards.pages.ReceiveCompleteWizardPage;
import org.bpelunit.toolsupport.editors.wizards.pages.SendSimpleWizardPage;

/**
 * A wizard for editing a receive/send syncronous activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveSendSyncActivityWizard extends TwoWayActivityWizard {

	private SendSimpleWizardPage fSendPage;
	private ReceiveCompleteWizardPage fReceivePage;

	public ReceiveSendSyncActivityWizard(TestSuitePage page, ActivityEditMode mode, XMLTwoWayActivity twoWayActivity) {
		super(page, mode, twoWayActivity);
	}

	@Override
	public boolean performFinish() {

		XMLTwoWayActivity twoWayActivity= getTwoWayActivity();

		transferOperation(fReceivePage, twoWayActivity);
		transferFault(fReceivePage.getSendFault(), twoWayActivity.getSend());
		transferFault(fReceivePage.getReceiveFault(), twoWayActivity.getReceive());

		transferLiteralSendData(fSendPage.getSendXML(), twoWayActivity.getSend());
		transferDelay(fSendPage.getDelaySequence(), twoWayActivity.getSend());

		processHeaderPage();
		processDataCopyPage();

		return true;
	}

	@Override
	protected String getPageName() {
		return "Receive/Send Synchronous";
	}

	@Override
	public void addPages() {
		super.addPages();

		fReceivePage= new ReceiveCompleteWizardPage(getTwoWayActivity(), getTwoWayActivity().getReceive(), getMode(), getPageName());
		addPage(fReceivePage);

		fSendPage= new SendSimpleWizardPage(getTwoWayActivity().getSend(), getMode(), getPageName());
		addPage(fSendPage);

		addHeaderProcessorPage();
		addDataCopyPage();
	}

}
