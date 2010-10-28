/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.BPELUnitEditor;
import net.bpelunit.toolsupport.editors.wizards.ActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.editors.wizards.pages.ActivityWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

/**
 * A data component is a graphical widget used to display and edit data. It may be added to wizard
 * pages to be displayed in wizards.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class DataComponent {

	private List<IComponentListener> fListeners;
	private FontMetrics fFontMetrics;
	private IWizardPage fWizardPage;

	public DataComponent(IWizardPage page, FontMetrics metrics) {
		fWizardPage= page;
		fFontMetrics= metrics;
		fListeners= new ArrayList<IComponentListener>();
	}

	public void addComponentListener(IComponentListener listener) {
		fListeners.add(listener);
	}

	public void removeComponentListener(IComponentListener listener) {
		fListeners.remove(listener);
	}

	protected void fireValueChanged(DialogField field) {
		for (IComponentListener listener : fListeners) {
			listener.valueChanged(field);
		}
	}

	protected int getMaxFieldWidth() {
		return Dialog.convertWidthInCharsToPixels(fFontMetrics, 40);
	}

	protected Group createGroup(Composite composite, String title, int nColumns, GridData gd) {
		Group operationGroup= new Group(composite, SWT.NULL);
		operationGroup.setText(title);

		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;
		operationGroup.setLayout(layout);

		gd.horizontalSpan= 4;
		operationGroup.setLayoutData(gd);
		return operationGroup;
	}

	public abstract Composite createControls(Composite composite, int nColumns);

	protected FontMetrics getFontMetrics() {
		return fFontMetrics;
	}

	protected BPELUnitEditor getEditor() {
		return getWizard().getEditor();
	}

	protected XMLTestSuite getTestSuite() {
		return getEditor().getTestSuite();
	}

	protected ActivityWizardPage getWizardPage() {
		return (ActivityWizardPage) fWizardPage;
	}

	protected ActivityWizard getWizard() {
		return (ActivityWizard) fWizardPage.getWizard();
	}

	protected Shell getShell() {
		return fWizardPage.getControl().getShell();
	}
}
