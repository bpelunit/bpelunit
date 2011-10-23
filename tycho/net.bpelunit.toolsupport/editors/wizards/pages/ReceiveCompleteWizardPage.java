/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.MessageChangeListener;
import net.bpelunit.toolsupport.editors.wizards.components.ReceiveComponent;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing a "complete" receive, i.e. operation and receive
 * conditions.
 * 
 * @version $Id: ReceiveCompleteWizardPage.java 81 2007-06-03 10:07:37Z
 *          asalnikow $
 * @author Philip Mayer
 * 
 */
public class ReceiveCompleteWizardPage extends OperationWizardPage {

	private ReceiveComponent fReceiveComponent;
	private XMLReceiveActivity fReceiveActivity;

	/**
	 * Creates a new receive wizard page showing an operation and receive
	 * control. The operation information will be initialized from the first
	 * given activity, the receive information will be initialized from the
	 * second given activity.
	 * 
	 * @param operationActivity
	 * @param sendActivity
	 * @param pageName
	 */
	public ReceiveCompleteWizardPage(XMLSoapActivity operationActivity,
			XMLReceiveActivity receiveActivity, ActivityEditMode mode,
			String pageName) {
		super(operationActivity, mode, pageName);
		this.fReceiveActivity = receiveActivity;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {
		super.createFieldControls(composite, nColumns);

		this.fReceiveComponent = new ReceiveComponent(this, this
				.getFontMetrics());
		this.fReceiveComponent.init(this.fReceiveActivity);
		this.fReceiveComponent.createControls(composite, nColumns);
		this.fReceiveComponent.addComponentListener(this);

		this.valueChanged(null);
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.RECEIVE;
	}

	public void addOperationListener(MessageChangeListener listener) {
		this.getOperationDataComponent().addMessageListener(listener);
	}
}
