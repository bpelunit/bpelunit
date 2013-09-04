package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLSetUp;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.EditSetupDataSource;
import net.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.TemplateVelocity;

import org.eclipse.swt.widgets.Composite;
/**
 * Page created for the wizard to edit the item Setup from TestCase and TestSuite
 *
 * @author Alejandro Acosta (alex_acos@informaticos.com)
 */
public class SetupWizardPage extends ActivityWizardPage implements IComponentListener {
	private EditSetupDataSource fFormSetUpDataSource;
	private XMLSetUp fXMLSetUp;
	private XMLTestCase testCase;

	public SetupWizardPage(ActivityEditMode mode, String pageName, XMLSetUp xmlSetup, XMLTestCase ftestCase) {
		super(pageName, mode);
		this.setDescription("Enter the data to be sent.");
		this.fXMLSetUp = xmlSetup;
		testCase=ftestCase;
		
		
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		this.fFormSetUpDataSource = new EditSetupDataSource(this, this.getFontMetrics());
		
		if(testCase==null){
			this.fFormSetUpDataSource.createControls(composite, nColumns);	
		}else{
			this.fFormSetUpDataSource.createControlsTestCase(composite,nColumns,testCase);
		}
		
		this.fFormSetUpDataSource.init(this.fXMLSetUp,testCase);

		this.fFormSetUpDataSource.addComponentListener(this);
		this.valueChanged(null);
	}

	public void valueChanged(DialogField field) {
		TemplateVelocity fieldContents =  this.fFormSetUpDataSource.fieldContents();
		TemplateVelocity fieldScript =  this.fFormSetUpDataSource.fieldScript();
		if (this.fFormSetUpDataSource != null && fieldScript != null) {
			if (fieldScript.getError()) {
				this.fail("Script: " + fieldScript.getMsgError());
				return;
			}
		}
		if (this.fFormSetUpDataSource != null && fieldContents != null) {
			if (fieldContents.getError()) {
				this.fail("Data Source: " + fieldContents.getMsgError());
				return;
			}
		}

		this.setErrorMessage(null);
		this.setPageComplete(true);
	}

	private void fail(String string) {
		this.setErrorMessage(string);
		this.setPageComplete(false);
	}

	public String getSendXMLScript() {
		return this.fFormSetUpDataSource.getXmlTextScript();
	}

	public String getSendXMLDataSource() {
		return this.fFormSetUpDataSource.getXmlTextDataSource();
	}

	public EditSetupDataSource getFormSetUpDataSource() {
		return fFormSetUpDataSource;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.SEND;
	}

}
