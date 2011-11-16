package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLCompleteHumanTaskActivity;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CompleteHumanTaskWizardPage extends WizardPage {

	private XMLCompleteHumanTaskActivity xmlCompleteHumanTaskActivity;

	public CompleteHumanTaskWizardPage(String pageName, XMLCompleteHumanTaskActivity completeHumanTaskActivity) {
		super(pageName, "Edit a Wait Activity", null);
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
		textLabel.setText("Wait Duration (ms): ");
		
		setControl(topLevel);
	}

}
