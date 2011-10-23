/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.pages.ReceiveCompleteWizardPage;
import net.bpelunit.toolsupport.editors.wizards.pages.SendCompleteWizardPage;

/**
 * Abstract superclass for the two asynchronous activities.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class TwoWayAsyncActivityWizard extends TwoWayActivityWizard {

	private ReceiveCompleteWizardPage fReceivePage;
	private SendCompleteWizardPage fSendPage;

	public TwoWayAsyncActivityWizard(TestSuitePage page, ActivityEditMode mode, XMLTwoWayActivity twoWayActivity) {
		super(page, mode, twoWayActivity);
	}

	@Override
	public boolean performFinish() {

		XMLTwoWayActivity twoWayActivity= getTwoWayActivity();
		transferOperation(fSendPage, twoWayActivity.getSend());
		transferOperation(fReceivePage, twoWayActivity.getReceive());

		transferFault(fSendPage.getSendFault(), twoWayActivity.getSend());
		transferFaultString(fSendPage.getSendFaultName(), twoWayActivity.getSend());
		transferFault(fReceivePage.getReceiveFault(), twoWayActivity.getReceive());

		transferLiteralSendData(fSendPage.getSendXML(), twoWayActivity.getSend());
		transferDelay(fSendPage.getDelaySequence(), twoWayActivity.getSend());

		processHeaderPage();
		processDataCopyPage();

		return true;
	}

	protected ReceiveCompleteWizardPage createReceivePage() {
		fReceivePage= new ReceiveCompleteWizardPage(getTwoWayActivity().getReceive(), getTwoWayActivity().getReceive(), getMode(), getPageName());
		return fReceivePage;
	}

	protected SendCompleteWizardPage createSendPage() {
		fSendPage= new SendCompleteWizardPage(getTwoWayActivity().getSend(), getTwoWayActivity().getSend(), getMode(), getPageName());
		return fSendPage;
	}
}
