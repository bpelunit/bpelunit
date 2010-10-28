/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.pages.ReceiveCompleteWizardPage;

/**
 * A wizard for editing a receive only activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveOnlyWizard extends ActivityWizard {

	private XMLReceiveActivity fReceiveActivity;
	private ReceiveCompleteWizardPage fReceiveWizardPage;

	public ReceiveOnlyWizard(TestSuitePage page, ActivityEditMode mode, XMLReceiveActivity operation) {
		super(page, mode);
		fReceiveActivity= operation;
	}

	@Override
	public boolean performFinish() {
		transferOperation(fReceiveWizardPage, fReceiveActivity);
		transferFault(fReceiveWizardPage.getReceiveFault(), fReceiveActivity);
		return true;
	}

	@Override
	public void addPages() {
		super.addPages();
		fReceiveWizardPage= new ReceiveCompleteWizardPage(fReceiveActivity, fReceiveActivity, getMode(), getPageName());
		addPage(fReceiveWizardPage);
	}

	@Override
	protected String getPageName() {
		return "Receive Asynchronous";
	}

}
