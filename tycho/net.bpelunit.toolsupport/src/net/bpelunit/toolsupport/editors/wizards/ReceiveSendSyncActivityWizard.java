/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.pages.ReceiveCompleteWizardPage;
import net.bpelunit.toolsupport.editors.wizards.pages.SendSimpleWizardPage;

import org.eclipse.swt.widgets.Composite;

/**
 * A wizard for editing a receive/send syncronous activity.
 * 
 * @version $Id: ReceiveSendSyncActivityWizard.java 81 2007-06-03 10:07:37Z
 *          asalnikow $
 * @author Philip Mayer
 * 
 */
public class ReceiveSendSyncActivityWizard extends TwoWayActivityWizard {

	private SendSimpleWizardPage fSendPage;
	private ReceiveCompleteWizardPage fReceivePage;

	
	
	public ReceiveSendSyncActivityWizard(TestSuitePage page,
			ActivityEditMode mode, XMLTwoWayActivity twoWayActivity) {
		super(page, mode, twoWayActivity);
	}

	@Override
	public boolean performFinish() {

		XMLTwoWayActivity twoWayActivity = this.getTwoWayActivity();
		
		
		
		this.transferOperation(this.fReceivePage, twoWayActivity);
		this.transferFault(this.fReceivePage.getSendFault(), twoWayActivity
				.getSend());
		this.transferFaultString(this.fReceivePage.getSendFaultName(),
				twoWayActivity.getSend());
		this.transferFault(this.fReceivePage.getReceiveFault(), twoWayActivity
				.getReceive());

		this.transferLiteralSendData(this.fSendPage.getSendXML(),
				twoWayActivity.getSend());
		this.transferDelay(this.fSendPage.getDelaySequence(), twoWayActivity
				.getSend());

		this.processHeaderPage();
		this.processDataCopyPage();
		
		fSendPage.getSendComponent().saveData();
		
		return true;
	}

	@Override
	protected String getPageName() {
		return "Receive/Send Synchronous";
	}

	@Override
	public void addPages() {
		super.addPages();

		this.fReceivePage = new ReceiveCompleteWizardPage(this
				.getTwoWayActivity(), this.getTwoWayActivity().getReceive(),
				this.getMode(), this.getPageName());
		this.addPage(this.fReceivePage);

		this.fSendPage = new SendSimpleWizardPage(this.getTwoWayActivity()
				.getSend(), this.getMode(), this.getPageName());
		this.addPage(this.fSendPage);

		this.addHeaderProcessorPage();
		this.addDataCopyPage();
	}

	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		this.fReceivePage.addOperationListener(this.fSendPage
				.getOperationChangeListener());
	}

}
