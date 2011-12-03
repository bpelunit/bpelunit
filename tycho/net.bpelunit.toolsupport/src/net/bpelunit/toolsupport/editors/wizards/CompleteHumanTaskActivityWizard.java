package net.bpelunit.toolsupport.editors.wizards;

import net.bpelunit.framework.xml.suite.XMLCompleteHumanTaskActivity;
import net.bpelunit.toolsupport.editors.wizards.pages.CompleteHumanTaskWizardPage;

import org.eclipse.jface.wizard.Wizard;

public class CompleteHumanTaskActivityWizard extends Wizard {

	private XMLCompleteHumanTaskActivity xmlCompleteHumanTaskActivity;
	private CompleteHumanTaskWizardPage completeHumanTaskActivityWizardPage;
	
	public CompleteHumanTaskActivityWizard(XMLCompleteHumanTaskActivity completeHumanTaskActivity) {
		this.xmlCompleteHumanTaskActivity = completeHumanTaskActivity;
	}

	@Override
	public void addPages() {
		super.addPages();
		completeHumanTaskActivityWizardPage = new CompleteHumanTaskWizardPage("Complete WS-HT Task", ActivityEditMode.EDIT, xmlCompleteHumanTaskActivity);
		this.addPage(completeHumanTaskActivityWizardPage);
		
		
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
