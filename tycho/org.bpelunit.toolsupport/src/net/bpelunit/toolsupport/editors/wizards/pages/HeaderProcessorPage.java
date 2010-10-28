/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.HeaderProcessorComponent;
import net.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing a header processor.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class HeaderProcessorPage extends ActivityWizardPage implements IComponentListener {

	private HeaderProcessorComponent fHeaderComponent;
	private XMLTwoWayActivity fTwoWayActivity;

	public HeaderProcessorPage(XMLTwoWayActivity activity, ActivityEditMode mode, String pageName) {
		super(pageName, mode);
		fTwoWayActivity= activity;
		setDescription("Add an optional header processor");
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		fHeaderComponent= new HeaderProcessorComponent(this, getFontMetrics());

		fHeaderComponent.init(fTwoWayActivity);
		fHeaderComponent.createControls(composite, nColumns);
		fHeaderComponent.addComponentListener(this);

		valueChanged(null);
	}

	public void valueChanged(DialogField field) {
		setErrorMessage(null);
		setPageComplete(true);
	}

	public boolean hasHeaderProcessorSelected() {
		return fHeaderComponent.hasHeaderProcessorSelected();
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.HEADERPROCESSOR;
	}
}
