/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import javax.xml.namespace.QName;

import org.bpelunit.framework.xml.suite.XMLActivity;
import org.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import org.bpelunit.toolsupport.editors.wizards.components.OperationDataComponent;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing SOAP operations.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class OperationWizardPage extends ActivityWizardPage implements IComponentListener {

	private XMLActivity fOperationActivity;
	private OperationDataComponent fOperationsComponent;


	public OperationWizardPage(XMLActivity operationActivity, ActivityEditMode mode, String pageName) {
		super(pageName, mode);
		fOperationActivity= operationActivity;
		setDescription("Enter an operation and the data to send.");
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		fOperationsComponent= new OperationDataComponent(this, getFontMetrics());
		fOperationsComponent.init(fOperationActivity);
		fOperationsComponent.createControls(composite, nColumns);
		fOperationsComponent.addComponentListener(this);

		valueChanged(null);
	}

	public void valueChanged(DialogField field) {
		setErrorMessage(fOperationsComponent.getErrorMessage());
		setPageComplete(fOperationsComponent.isPageComplete());
	}


	public QName getService() {
		return fOperationsComponent.getService();
	}

	public String getPort() {
		return fOperationsComponent.getPort();
	}

	public String getOperation() {
		return fOperationsComponent.getOperation();
	}

	public boolean getSendFault() {
		return fOperationsComponent.getSendFault();
	}

	public boolean getReceiveFault() {
		return fOperationsComponent.getReceiveFault();
	}

	protected XMLActivity getActivity() {
		return fOperationActivity;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.OPERATION;
	}

}
