package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLSetUp;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.EditSetupDataSource;
import net.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;

import org.eclipse.swt.widgets.Composite;

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

		if (this.fFormSetUpDataSource != null && fFormSetUpDataSource.fieldScript != null) {
			if (fFormSetUpDataSource.fieldScript.getError()) {
				this.fail("Script: " + fFormSetUpDataSource.fieldScript.getMsgError());
				return;
			}
		}
		if (this.fFormSetUpDataSource != null && fFormSetUpDataSource.fieldContents != null) {
			if (fFormSetUpDataSource.fieldContents.getError()) {
				this.fail("Data Source: " + fFormSetUpDataSource.fieldContents.getMsgError());
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
