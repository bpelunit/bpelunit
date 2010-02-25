/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.xml.suite.XMLSendActivity;
import org.bpelunit.toolsupport.editors.TestSuitePage;
import org.bpelunit.toolsupport.editors.wizards.pages.SendCompleteWizardPage;

/**
 * A wizard for editing a send only activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendOnlyWizard extends ActivityWizard {

	private XMLSendActivity fSendActivity;
	private SendCompleteWizardPage fSendPage;

	public SendOnlyWizard(TestSuitePage page, ActivityEditMode mode, XMLSendActivity operation) {
		super(page, mode);
		fSendActivity= operation;
	}

	@Override
	public boolean performFinish() {

		transferOperation(fSendPage, fSendActivity);
		transferFault(fSendPage.getSendFault(), fSendActivity);
		transferFaultString(fSendPage.getSendFaultName(), fSendActivity);

		transferLiteralSendData(fSendPage.getSendXML(), fSendActivity);
		transferDelay(fSendPage.getDelaySequence(), fSendActivity);

		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		fSendPage= new SendCompleteWizardPage(fSendActivity, fSendActivity, getMode(), getPageName());
		addPage(fSendPage);
	}

	@Override
	protected String getPageName() {
		return "Send Synchronous";
	}
}
