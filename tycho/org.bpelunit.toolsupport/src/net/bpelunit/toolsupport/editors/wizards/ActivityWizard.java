/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.BPELUnitEditor;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.pages.ActivityWizardPage;
import net.bpelunit.toolsupport.editors.wizards.pages.OperationWizardPage;
import net.bpelunit.toolsupport.util.ToolUtil;
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
	WizardPageCode fStartupPageCode;

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



	protected abstract String getPageName();

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

	protected void transferOperation(OperationWizardPage page, XMLSoapActivity activity) {
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

	protected void transferFaultString(String faultString, XMLReceiveActivity activity) {
		if (faultString == null || "".equals(faultString)) {
			if (activity.isSetFaultstring())
				activity.unsetFaultstring();
		} else
			activity.setFaultstring(faultString);
	}

	protected void transferFaultString(String faultString, XMLSendActivity activity) {
		if (faultString == null || "".equals(faultString)) {
			if (activity.isSetFaultstring())
				activity.unsetFaultstring();
		} else
			activity.setFaultstring(faultString);
	}

	protected void transferDelay(String delaySequence, XMLSendActivity activity) {
		if (delaySequence == null || "".equals(delaySequence)) {
			if (activity.isSetDelaySequence())
				activity.unsetDelaySequence();
		} else
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

	public BPELUnitEditor getEditor() {
		return fPage.getSuiteEditor();
	}

	public XMLTestSuite getTestSuite() {
		return getEditor().getTestSuite();
	}

	public void setStart(WizardPageCode code) {
		fStartupPageCode = code;
	}
	
}
