package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLSetUp;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.pages.SetupWizardPage;

/**
 * Creation of wizard to edit the item setup from TestCase and TestSuite
 *
 * @author Alejandro Acosta (alex_acos@informaticos.com)
 */

public class SetupWizard extends TwoWayActivityWizard {

	private SetupWizardPage fSendPage;
	private XMLSetUp xmlSetup;
	private XMLTestCase ftestCase;

	public SetupWizard(TestSuitePage page, ActivityEditMode mode, XMLSetUp xmlSetUp, XMLTestCase testCase) {
		super(page, mode, null);
		xmlSetup = xmlSetUp;
		ftestCase=testCase;
		
		
	}

	@Override
	public boolean performFinish() {
		fSendPage.getFormSetUpDataSource().saveData();
		return true;
	}

	@Override
	protected String getPageName() {
		return "Test Suite: SetUp/DataSource";
	}

	public void addPages() {
		super.addPages();

		fSendPage = new SetupWizardPage(this.getMode(), this.getPageName(), xmlSetup,ftestCase);
		addPage(fSendPage);
	}

}
