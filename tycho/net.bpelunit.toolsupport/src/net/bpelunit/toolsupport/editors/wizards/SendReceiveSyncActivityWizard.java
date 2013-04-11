/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.pages.ReceiveSimpleWizardPage;
import net.bpelunit.toolsupport.editors.wizards.pages.SendCompleteWizardPage;

/**
 * A wizard for editing a send/receive synchronous activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendReceiveSyncActivityWizard extends TwoWayActivityWizard {

	private SendCompleteWizardPage fSendPage;
	private ReceiveSimpleWizardPage fReceiveSimpleWizardPage;
	
	

	public SendReceiveSyncActivityWizard(TestSuitePage page, ActivityEditMode mode, XMLTwoWayActivity twoWayActivity) {
		super(page, mode, twoWayActivity);
	}

	@Override
	public boolean performFinish() {

		XMLTwoWayActivity twoWayActivity= getTwoWayActivity();
		
		transferOperation(fSendPage, twoWayActivity);
		transferFault(fSendPage.getSendFault(), twoWayActivity.getSend());
		transferFaultString(fSendPage.getSendFaultName(), twoWayActivity.getSend());
		transferFault(fSendPage.getReceiveFault(), twoWayActivity.getReceive());

		transferLiteralSendData(fSendPage.getSendXML(), twoWayActivity.getSend());
		transferDelay(fSendPage.getDelaySequence(), twoWayActivity.getSend());

		processHeaderPage();
		processDataCopyPage();
		fSendPage.getSendComponent().saveData();
		return true;
	}

	@Override
	protected String getPageName() {
		return "Send/Receive Synchronous";
	}

	@Override
	public void addPages() {
		super.addPages();

		fSendPage= new SendCompleteWizardPage(getTwoWayActivity(), getTwoWayActivity().getSend(), getMode(), getPageName());
		addPage(fSendPage);

		fReceiveSimpleWizardPage= new ReceiveSimpleWizardPage(getTwoWayActivity().getReceive(), getMode(), getPageName());
		addPage(fReceiveSimpleWizardPage);

		addHeaderProcessorPage();
		addDataCopyPage();

	}

}
