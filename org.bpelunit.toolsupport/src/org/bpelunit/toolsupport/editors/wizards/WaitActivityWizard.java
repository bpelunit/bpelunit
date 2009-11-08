package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.xml.suite.XMLWaitActivity;
import org.bpelunit.toolsupport.editors.wizards.pages.WaitWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class WaitActivityWizard extends Wizard {

	private XMLWaitActivity xmlWait;
	private WaitWizardPage waitWizardPage;
	
	public WaitActivityWizard(XMLWaitActivity activity) {
		this.xmlWait = activity;
	}

	@Override
	public void addPages() {
		super.addPages();
		waitWizardPage = new WaitWizardPage("Wait", xmlWait);
		this.addPage(waitWizardPage);
	}
	
	@Override
	public boolean performFinish() {
		xmlWait.setWaitForMilliseconds(waitWizardPage.getDuration());
		return true;
	}
}
