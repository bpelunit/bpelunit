/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLSendOnlyActivity;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.pages.HeaderProcessorPage;
import net.bpelunit.toolsupport.editors.wizards.pages.SendCompleteWizardPage;

/**
 * A wizard for editing a send only activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendOnlyWizard extends ActivityWizard {

	private XMLSendOnlyActivity fSendActivity;
	private SendCompleteWizardPage fSendPage;
	private HeaderProcessorPage fHeaderPage;

	public SendOnlyWizard(TestSuitePage page, ActivityEditMode mode, XMLSendOnlyActivity operation) {
		super(page, mode);
		fSendActivity = operation;
	}

	@Override
	public boolean performFinish() {

		transferOperation(fSendPage, fSendActivity);
		transferFault(fSendPage.getSendFault(), fSendActivity);
		transferFaultString(fSendPage.getSendFaultName(), fSendActivity);

		transferLiteralSendData(fSendPage.getSendXML(), fSendActivity);
		transferDelay(fSendPage.getDelaySequence(), fSendActivity);
		fSendPage.getSendComponent().saveData();

		if (!fHeaderPage.hasHeaderProcessorSelected()) {
			// No header was selected. In this case, we have to remove
			// the header processor.
			// If a processor was selected, all data already belongs to the
			// activity.
			fSendActivity.unsetHeaderProcessor();
		}

		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		fSendPage = new SendCompleteWizardPage(fSendActivity, fSendActivity, getMode(),
				getPageName());
		addPage(fSendPage);
		addHeaderProcessorPage();
	}

	protected void addHeaderProcessorPage() {
		fHeaderPage = new HeaderProcessorPage(fSendActivity, getMode(), getPageName());
		addPage(fHeaderPage);
	}

	@Override
	protected String getPageName() {
		return "Send Asynchronous";
	}
}
