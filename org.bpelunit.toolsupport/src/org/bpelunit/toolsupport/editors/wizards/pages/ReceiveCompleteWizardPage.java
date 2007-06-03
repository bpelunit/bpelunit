/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import org.bpelunit.framework.xml.suite.XMLActivity;
import org.bpelunit.framework.xml.suite.XMLReceiveActivity;
import org.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.components.ReceiveComponent;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing a "complete" receive, i.e. operation and receive conditions.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveCompleteWizardPage extends OperationWizardPage {

	private ReceiveComponent fReceiveComponent;
	private XMLReceiveActivity fReceiveActivity;

	/**
	 * Creates a new receive wizard page showing an operation and receive control. The operation
	 * information will be initialized from the first given activity, the receive information will
	 * be initialized from the second given activity.
	 * 
	 * @param operationActivity
	 * @param sendActivity
	 * @param pageName
	 */
	public ReceiveCompleteWizardPage(XMLActivity operationActivity, XMLReceiveActivity receiveActivity, ActivityEditMode mode, String pageName) {
		super(operationActivity, mode, pageName);
		fReceiveActivity= receiveActivity;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		super.createFieldControls(composite, nColumns);

		fReceiveComponent= new ReceiveComponent(this, getFontMetrics());
		fReceiveComponent.init(fReceiveActivity);
		fReceiveComponent.createControls(composite, nColumns);
		fReceiveComponent.addComponentListener(this);

		valueChanged(null);
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.RECEIVE;
	}

}
