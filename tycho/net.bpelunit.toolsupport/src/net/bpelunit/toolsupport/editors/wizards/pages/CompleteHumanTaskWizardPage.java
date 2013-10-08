package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLCompleteHumanTaskActivity;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.SendComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CompleteHumanTaskWizardPage extends ActivityWizardPage {

	private XMLCompleteHumanTaskActivity xmlCompleteHumanTaskActivity;

	@SuppressWarnings("unused")
	private SendComponent sendComponent;

	private Text taskNameText;

	public CompleteHumanTaskWizardPage(String pageName, ActivityEditMode mode, XMLCompleteHumanTaskActivity completeHumanTaskActivity) {
		super(pageName, mode);
		this.setDescription("Configure properties for completing a WS-HT Task");
		this.xmlCompleteHumanTaskActivity = completeHumanTaskActivity;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite topLevel = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		
		layout.marginWidth = layout.marginHeight = 5;
		topLevel.setLayout(layout);
		
		Label textLabel = new Label(topLevel, SWT.NONE);
		textLabel.setText("Task Name: ");
		taskNameText = new Text(topLevel, SWT.SINGLE | SWT.BORDER);
		taskNameText.setText("" + xmlCompleteHumanTaskActivity.getTaskName());
		
		this.sendComponent = new SendComponent(this, getFontMetrics());
		
		this.setControl(topLevel);
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.COMPLETEHUMANTASK;
	}

}
