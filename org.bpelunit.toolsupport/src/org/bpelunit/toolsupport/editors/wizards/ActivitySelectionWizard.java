/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards;

import org.bpelunit.framework.control.util.ActivityUtil.ActivityConstant;
import org.bpelunit.toolsupport.editors.wizards.pages.ActivitySelectionWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * Wizard for selecting an activity type.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ActivitySelectionWizard extends Wizard {

	private ActivitySelectionWizardPage fPage;

	public ActivitySelectionWizard() {
		super();
		setWindowTitle("Select an activity type");
		setHelpAvailable(false);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void addPages() {
		fPage= new ActivitySelectionWizardPage("Activity Selection Page");
		addPage(fPage);
	}

	public ActivityConstant getSelectedActivity() {
		return fPage.getSelectedActivity();
	}

}
