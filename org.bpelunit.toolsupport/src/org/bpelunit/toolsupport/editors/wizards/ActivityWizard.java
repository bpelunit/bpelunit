/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.bpelunit.framework.xml.suite.XMLActivity;
import org.bpelunit.framework.xml.suite.XMLReceiveActivity;
import org.bpelunit.framework.xml.suite.XMLSendActivity;
import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.toolsupport.editors.BPELUnitEditor;
import org.bpelunit.toolsupport.editors.TestSuitePage;
import org.bpelunit.toolsupport.editors.wizards.pages.ActivityWizardPage;
import org.bpelunit.toolsupport.editors.wizards.pages.OperationWizardPage;
import org.bpelunit.toolsupport.util.ToolUtil;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * Abstract base class of all activity-related wizards.
 */
public abstract class ActivityWizard extends Wizard {

	/**
	 * The test suite page
	 */
	private TestSuitePage fPage;

	/**
	 * The code of the startup page
	 */
	private WizardPageCode fStartupPageCode;

	/**
	 * Activity edit mode
	 */
	private ActivityEditMode fMode;

	public ActivityWizard(TestSuitePage page, ActivityEditMode mode) {
		super();
		fMode= mode;
		fPage= page;
		setWindowTitle(getPageName());
	}

	public BPELUnitEditor getEditor() {
		return fPage.getSuiteEditor();
	}

	public XMLTestSuite getTestSuite() {
		return getEditor().getTestSuite();
	}

	protected abstract String getPageName();

	public void setStart(WizardPageCode code) {
		fStartupPageCode= code;
	}

	@Override
	public IWizardPage getStartingPage() {
		if (fStartupPageCode != null) {
			IWizardPage[] pages= getPages();
			for (IWizardPage element : pages) {
				ActivityWizardPage page= (ActivityWizardPage) element;
				if (page.getCode().equals(fStartupPageCode)) {
					return page;
				}
			}
		}
		return super.getStartingPage();
	}

	protected void transferOperation(OperationWizardPage page, XMLActivity activity) {
		QName service= page.getService();
		String port= page.getPort();
		String operation= page.getOperation();

		activity.setService(service);
		activity.setPort(port);
		activity.setOperation(operation);

	}

	protected void transferFault(boolean fault, XMLReceiveActivity activity) {
		activity.setFault(fault);
	}

	protected void transferFault(boolean fault, XMLSendActivity activity) {
		activity.setFault(fault);
	}

	protected void transferDelay(String delaySequence, XMLSendActivity activity) {
		if (delaySequence.equals("") && activity.getDelaySequence() != null)
			activity.unsetDelaySequence();
		else
			activity.setDelaySequence(delaySequence);
	}

	protected void transferLiteralSendData(String sendXML, XMLSendActivity sendActivity) {
		XmlObject any= ToolUtil.parseSendBlock(getTestSuite(), sendXML);
		if (any != null) {
			sendActivity.getData().set(any);
		}
	}

	protected ActivityEditMode getMode() {
		return fMode;
	}

}
