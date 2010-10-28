/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The abstract ActivityWizardPage is the parent class for all activity wizard pages of the BPELUnit
 * activity wizards.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class ActivityWizardPage extends WizardPage {

	/**
	 * Font metrics
	 */
	private FontMetrics fontMetrics;

	protected ActivityWizardPage(String pageName, ActivityEditMode mode) {
		super(pageName);
		setImageDescriptor(ToolSupportActivator.getImageDescriptor("icons/new_wiz.png"));
		setTitle( (mode == ActivityEditMode.ADD ? "Add" : "Edit") + " a " + pageName + " activity");
	}

	@Override
	protected void initializeDialogUnits(Control testControl) {
		super.initializeDialogUnits(testControl);

		GC gc= new GC(testControl);
		gc.setFont(JFaceResources.getDialogFont());
		fontMetrics= gc.getFontMetrics();
		gc.dispose();
	}

	protected FontMetrics getFontMetrics() {
		return fontMetrics;
	}

	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());

		int nColumns= 4;

		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;
		composite.setLayout(layout);

		createFieldControls(composite, nColumns);

		setControl(composite);

		Dialog.applyDialogFont(composite);
	}

	/**
	 * 
	 * @param composite
	 * @param nColumns
	 */
	protected abstract void createFieldControls(Composite composite, int nColumns);

	/**
	 * Returns the contents code of this wizard page.
	 * 
	 * @return
	 */
	public abstract WizardPageCode getCode();

}
