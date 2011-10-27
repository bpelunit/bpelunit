/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import net.bpelunit.toolsupport.editors.wizards.components.ReceiveComponent;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing a "simple" receive, i.e. only the receive conditions without the operation.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveSimpleWizardPage extends ActivityWizardPage implements IComponentListener {

	private ReceiveComponent fReceiveComponent;
	private XMLReceiveActivity fReceiveActivity;

	public ReceiveSimpleWizardPage(XMLReceiveActivity receiveOp, ActivityEditMode mode, String pageName) {
		super(pageName, mode);
		setDescription("Enter the conditions to be verified.");
		fReceiveActivity= receiveOp;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		fReceiveComponent= new ReceiveComponent(this, getFontMetrics());
		fReceiveComponent.init(fReceiveActivity);
		fReceiveComponent.createControls(composite, nColumns);
		fReceiveComponent.addComponentListener(this);

		valueChanged(null);
	}

	public void valueChanged(DialogField field) {
		setErrorMessage(null);
		setPageComplete(true);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.RECEIVE;
	}

}
