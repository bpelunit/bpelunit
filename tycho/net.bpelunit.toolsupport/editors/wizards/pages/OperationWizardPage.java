/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import javax.wsdl.Definition;
import javax.xml.namespace.QName;

import net.bpelunit.framework.xml.suite.XMLActivity;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.ReceiveSendSyncActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.components.IComponentListener;
import net.bpelunit.toolsupport.editors.wizards.components.OperationDataComponent;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.util.schema.InvalidInputException;
import net.bpelunit.toolsupport.util.schema.NoElementDefinitionExistsException;
import net.bpelunit.toolsupport.util.schema.NoSuchOperationException;
import net.bpelunit.toolsupport.util.schema.WSDLParser;
import net.bpelunit.toolsupport.util.schema.nodes.Element;

import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing SOAP operations.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class OperationWizardPage extends ActivityWizardPage implements IComponentListener {

	private XMLSoapActivity fOperationActivity;
	private OperationDataComponent fOperationsComponent;
	private String elementError;
	private String elementWarning;

	public OperationWizardPage(XMLSoapActivity operationActivity, ActivityEditMode mode,
			String pageName) {
		super(pageName, mode);
		this.fOperationActivity = operationActivity;
		this.setDescription("Enter an operation and the data to send.");
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		this.fOperationsComponent = new OperationDataComponent(this, this.getFontMetrics());
		this.fOperationsComponent.init(this.fOperationActivity);
		this.fOperationsComponent.createControls(composite, nColumns);
		this.fOperationsComponent.addComponentListener(this);

		this.valueChanged(null);
	}

	public void valueChanged(DialogField field) {
		final String operationError = this.fOperationsComponent.getErrorMessage();

		this.setErrorMessage(operationError != null ? operationError : elementError);
		this.setMessage(elementWarning, WARNING);
		this.setPageComplete(elementError == null && this.fOperationsComponent.isPageComplete());
	}

	public Definition getDefinition() {
		return this.fOperationsComponent.getDefinition();
	}

	public QName getService() {
		return this.fOperationsComponent.getService();
	}

	public String getPort() {
		return this.fOperationsComponent.getPort();
	}

	public String getOperation() {
		return this.fOperationsComponent.getOperation();
	}

	public boolean getSendFault() {
		return this.fOperationsComponent.getSendFault();
	}

	public String getSendFaultName() {
		return this.fOperationsComponent.getSendFaultName();
	}

	public boolean getReceiveFault() {
		return this.fOperationsComponent.getReceiveFault();
	}

	private WSDLParser getWSDLParser() {
		return this.fOperationsComponent.getWSDLParser();
	}

	protected XMLActivity getActivity() {
		return this.fOperationActivity;
	}

	public OperationDataComponent getOperationDataComponent() {
		return this.fOperationsComponent;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.OPERATION;
	}

	public Element getElementForOperation() throws InvalidInputException, NoSuchOperationException {
		Element element = null;
		WSDLParser parser = this.getWSDLParser();
		if (parser == null) {
			return null;
		}

		try {
			/*
			 * Normally I'd expect faults only to be sent instead of an
			 * <output>, but it turns out they can be sent instead of an
			 * <input>, for solicit-response operations (see WSDL 1.1, section
			 * 2.4.3).
			 */
			if (this.getSendFault()) {
				element = parser.getFaultElementForOperation(getService(), getPort(),
						getOperation(), getSendFaultName());
			} else if (this.getWizard() instanceof ReceiveSendSyncActivityWizard) {
				// Sync receive/send: send the output from the WS
				element = parser.getOutputElementForOperation(getService(), getPort(),
						getOperation());
			} else {
				// Sync send/receive, async send/receive or async receive/send:
				// send the input for some WS
				element = parser.getInputElementForOperation(getService(), getPort(),
						getOperation());
			}

			elementError = null;
			elementWarning = null;
		} catch (InvalidInputException ex) {
			elementError = "Invalid input: " + ex.getLocalizedMessage();
			return null;
		} catch (NoElementDefinitionExistsException ex) {
			elementWarning = "Cannot generate sample element: " + ex.getLocalizedMessage();
			return null;
		}

		return element;
	}
}
