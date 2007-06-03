/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import org.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import org.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.components.DataCopyComponent;
import org.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing data copy operations.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DataCopyPage extends ActivityWizardPage implements IComponentListener {

	private DataCopyComponent fDataCopyComponent;
	private XMLTwoWayActivity fActivity;

	public DataCopyPage(XMLTwoWayActivity twoWayOp, ActivityEditMode mode, String pageName) {
		super(pageName, mode);
		setDescription("Add optional data copy operations");
		fActivity= twoWayOp;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		fDataCopyComponent= new DataCopyComponent(this, getFontMetrics());
		fDataCopyComponent.init(fActivity);
		fDataCopyComponent.createControls(composite, nColumns);
		fDataCopyComponent.addComponentListener(this);

		valueChanged(null);
	}

	public void valueChanged(DialogField field) {
		setErrorMessage(null);
		setPageComplete(true);
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.DATACOPY;
	}

}
