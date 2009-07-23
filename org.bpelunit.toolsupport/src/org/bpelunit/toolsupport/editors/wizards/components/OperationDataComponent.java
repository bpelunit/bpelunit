/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.xml.namespace.QName;

import org.bpelunit.framework.control.util.ActivityUtil;
import org.bpelunit.framework.xml.suite.XMLActivity;
import org.bpelunit.framework.xml.suite.XMLTrack;
import org.bpelunit.toolsupport.ToolSupportActivator;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import org.bpelunit.toolsupport.editors.wizards.fields.IStringButtonAdapter;
import org.bpelunit.toolsupport.editors.wizards.fields.LayoutUtil;
import org.bpelunit.toolsupport.editors.wizards.fields.SelectionButtonDialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.StringButtonDialogField;
import org.bpelunit.toolsupport.util.WSDLReadingException;
import org.bpelunit.toolsupport.util.schema.WSDLParser;
import org.bpelunit.toolsupport.util.schema.nodes.Element;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * The OperationDataComponent allows the user to select a WSDL service, port,
 * and operation for a given partner, and
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class OperationDataComponent extends DataComponent {

	private class ServiceListener implements IStringButtonAdapter,
			IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			OperationDataComponent.this.openServiceChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			OperationDataComponent.this.handleServiceFieldChanged(field);
		}

	}

	private class PortListener implements IStringButtonAdapter,
			IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			OperationDataComponent.this.openPortChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			OperationDataComponent.this.handlePortFieldChanged(field);
		}

	}

	private class OperationListener implements IStringButtonAdapter,
			IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			OperationDataComponent.this.openOperationChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			OperationDataComponent.this.handleOperationFieldChanged(field);
		}
	}

	class SimpleLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return ToolSupportActivator
					.getImage(ToolSupportActivator.IMAGE_DEPLOYER);
		}

		public String getText(Object element) {
			if (element instanceof QName) {
				return ((QName) element).getLocalPart();
			} else if (element instanceof String) {
				return ((String) element);
			} else {
				return "Unknown Type";
			}

		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}

	private enum Verify {
		DEF, SERVICE, PORT, ALL
	}

	private static final String SEND_NAME = "send";
	private static final String RECEIVE_NAME = "receive";

	private StringButtonDialogField fServiceDialogField;
	private StringButtonDialogField fPortDialogField;
	private StringButtonDialogField fOperationDialogField;
	private XMLActivity fActivity;

	private QName fService;
	private String fPort;
	private String fOperation;
	private String fErrorMessage;

	private boolean fPageComplete;
	private SelectionButtonDialogField fSendFaultField;
	private SelectionButtonDialogField fReceiveFaultField;

	private List<InputElementChangeListener> inputElementChangeListeners = new ArrayList<InputElementChangeListener>();

	public OperationDataComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	public void handleServiceFieldChanged(DialogField field) {

		Definition def = this.getDefinition();
		if (def != null) {
			this.fService = new QName(def.getTargetNamespace(),
					this.fServiceDialogField.getText());
		}

		this.validateOperation(Verify.ALL);
	}

	public void handlePortFieldChanged(DialogField field) {
		this.fPort = this.fPortDialogField.getText();
		this.validateOperation(Verify.ALL);
	}

	public void handleOperationFieldChanged(DialogField field) {
		this.fOperation = this.fOperationDialogField.getText();
		if (this.validateOperation(Verify.ALL)) {
			Element inputElement = this.getWSDLParser()
					.getInputElementForOperation(this.getService(),
							this.getPort(), this.getOperation());
			for (InputElementChangeListener listener : this.inputElementChangeListeners) {
				listener.inputElementChanged(inputElement);
			}
		}
	}

	public void addOperationListener(InputElementChangeListener listener) {
		for (InputElementChangeListener ol : this.inputElementChangeListeners) {
			if (ol == listener) {
				return;
			}
		}
		this.inputElementChangeListeners.add(listener);
		if (this.validateOperation(Verify.ALL)
				&& listener instanceof SendComponent) {
			Element inputElement = this.getWSDLParser()
					.getInputElementForOperation(this.getService(),
							this.getPort(), this.getOperation());
			((SendComponent) listener).setInputElement(inputElement, false);
		}
	}

	public void removeOperationListener(InputElementChangeListener listener) {
		this.inputElementChangeListeners.remove(listener);
	}

	/**
	 * Validates the whole operation block "until" the given verification code.
	 * 
	 * @param v
	 * @return
	 */
	private boolean validateOperation(Verify v) {

		XMLTrack track = ActivityUtil.getEnclosingTrack(this.fActivity);

		if (track == null) {
			this.setProblem("Partner Track not found.");
			return false;
		}

		Definition def;
		try {
			def = this.getEditor().getWsdlForPartner(track);
		} catch (WSDLReadingException e) {
			String msg = e.getMessage() + e.getCause() != null ? e.getCause()
					.getMessage() : "";
			this.setProblem(msg);
			return false;
		}

		if (def == null) {
			this
					.setProblem("WSDL definition was not found for this partner track.");
			return false;
		}

		if (v.equals(Verify.DEF)) {
			this.setNoProblem();
			return true;
		}

		if (this.isEmpty(this.fService)) {
			this.setProblem("Enter a service name.");
			return false;
		}

		Service service = def.getService(this.fService);
		if (service == null) {
			this.setProblem("Could not locate Service with name "
					+ this.fService.toString());
			return false;
		}

		if (v.equals(Verify.SERVICE)) {
			this.setNoProblem();
			return true;
		}

		if (this.isEmpty(this.fPort)) {
			this.setProblem("Enter a port name.");
			return false;
		}

		Port port = this.getDefinition().getService(this.fService).getPort(
				this.fPort);
		if (port == null) {
			this.setProblem("Could not locate port with name " + this.fPort);
			return false;
		}

		if (v.equals(Verify.PORT)) {
			this.setNoProblem();
			return true;
		}

		if (this.isEmpty(this.fOperation)) {
			this.setProblem("Enter an operation name");
			return false;
		}

		Binding binding = this.getDefinition().getService(this.fService)
				.getPort(this.fPort).getBinding();
		if ((binding == null)
				|| (binding.getBindingOperation(this.fOperation, null, null)) == null) {
			this.setProblem("Could not locate operation with name "
					+ this.fOperation);
			return false;
		}

		this.setNoProblem();
		return true;
	}

	public void openServiceChooser(DialogField field) {

		Definition def = this.getDefinition();
		if (def == null) {
			MessageDialog.openError(this.getShell(), "Error",
					"Partner Definition is incorrect; Problem loading WSDL.");
			return;
		}
		/*
		 * The map is Map<QName, javax.wsdl.Service>.
		 */
		Map services = def.getServices();

		if (services != null) {
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(
					this.getShell(), new SimpleLabelProvider());
			dialog.setElements(services.keySet().toArray());
			dialog.setTitle("Services for this partner");
			dialog.setMessage("Select one of the services to set it.");
			dialog.setMultipleSelection(false);
			int code = dialog.open();
			if (code == IDialogConstants.OK_ID) {
				Object[] res = dialog.getResult();
				if (res != null && res.length > 0) {
					QName serviceName = (QName) res[0];
					this.fService = serviceName;
					this.updateFields();
					this.validateOperation(Verify.ALL);
				}
			}
		}
	}

	private void openPortChooser(DialogField field) {

		if (this.validateOperation(Verify.SERVICE)) {
			Map ports = this.getDefinition().getService(this.fService)
					.getPorts();
			String str = this.openStringChooser(ports.keySet().toArray());
			if (str != null) {
				this.fPort = str;
			}
			this.updateFields();
			this.validateOperation(Verify.ALL);
		} else {
			MessageDialog.openError(this.getShell(), "Error",
					"Please select a valid service first.");
		}
	}

	public void openOperationChooser(DialogField field) {

		if (this.validateOperation(Verify.PORT)) {
			List operations = this.getDefinition().getService(this.fService)
					.getPort(this.fPort).getBinding().getBindingOperations();

			String[] options = new String[operations.size()];
			int i = 0;
			for (Object object : operations) {
				options[i] = ((BindingOperation) object).getName();
				i++;
			}

			String str = this.openStringChooser(options);
			if (str != null) {
				this.fOperation = str;
			}
			this.updateFields();
			this.validateOperation(Verify.ALL);
		} else {
			MessageDialog.openError(this.getShell(), "Error",
					"Please select a valid service and port first.");
		}
	}

	private boolean isEmpty(String something) {
		return (something == null || "".equals(something));
	}

	private boolean isEmpty(QName something) {
		return (something == null || "".equals(something.getLocalPart()));
	}

	private String openStringChooser(Object[] values) {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(this
				.getShell(), new SimpleLabelProvider());
		dialog.setElements(values);
		dialog.setTitle("Services for this partner");
		dialog.setMessage("Select one of the services to set it.");
		dialog.setMultipleSelection(false);
		int code = dialog.open();
		if (code == IDialogConstants.OK_ID) {
			Object[] res = dialog.getResult();
			if (res != null && res.length > 0) {
				return (String) res[0];
			}
		}
		return null;
	}

	private void updateFields() {
		if (this.fService != null) {
			this.fServiceDialogField.setText(this.fService.getLocalPart());
		}
		if (this.fPort != null) {
			this.fPortDialogField.setText(this.fPort);
		}
		if (this.fOperation != null) {
			this.fOperationDialogField.setText(this.fOperation);
		}
	}

	public Definition getDefinition() {
		XMLTrack track = ActivityUtil.getEnclosingTrack(this.fActivity);

		if (track == null) {
			this.setProblem("Partner Track not found - cannot continue.");
			return null;
		}

		Definition wsdlForPartner;
		try {
			wsdlForPartner = this.getEditor().getWsdlForPartner(track);
			return wsdlForPartner;
		} catch (WSDLReadingException e) {
			String msg = e.getMessage() + e.getCause() != null ? e.getCause()
					.getMessage() : "";
			this.setProblem(msg);
			return null;
		}
	}

	public WSDLParser getWSDLParser() {
		return this.getEditor()
				.getWSDLParserForDefinition(this.getDefinition());
	}

	public void init(XMLActivity activity) {

		this.fActivity = activity;

		try {
			this.fService = activity.getService();
			this.fPort = activity.getPort();
			this.fOperation = activity.getOperation();
		} catch (Exception e) {
			// nay happen in getService(), if the prefix is not bound
		}

		if (this.fService == null) {
			// an exception occurred
			this.fPort = null;
			this.fOperation = null;
		}

		ServiceListener serviceAdapter = new ServiceListener();
		this.fServiceDialogField = new StringButtonDialogField(serviceAdapter);
		this.fServiceDialogField.setDialogFieldListener(serviceAdapter);
		this.fServiceDialogField.setLabelText("Service");
		this.fServiceDialogField.setButtonLabel("Choose...");

		PortListener portListener = new PortListener();
		this.fPortDialogField = new StringButtonDialogField(portListener);
		this.fPortDialogField.setDialogFieldListener(portListener);
		this.fPortDialogField.setLabelText("Port");
		this.fPortDialogField.setButtonLabel("Choose...");

		OperationListener operationListener = new OperationListener();
		this.fOperationDialogField = new StringButtonDialogField(
				operationListener);
		this.fOperationDialogField.setDialogFieldListener(operationListener);
		this.fOperationDialogField.setLabelText("Operation");
		this.fOperationDialogField.setButtonLabel("Choose...");

		// Faults:

		this.fSendFaultField = new SelectionButtonDialogField(SWT.CHECK);
		this.fSendFaultField.setLabelText("Use fault element for " + SEND_NAME
				+ " operation");
		this.fReceiveFaultField = new SelectionButtonDialogField(SWT.CHECK);
		this.fReceiveFaultField.setLabelText("Use fault element for "
				+ RECEIVE_NAME + " operation");

		this.fSendFaultField.setSelection(ActivityUtil
				.getSendFault(this.fActivity));
		this.fReceiveFaultField.setSelection(ActivityUtil
				.getReceiveFault(this.fActivity));

		this.updateFields();
		this.validateOperation(Verify.ALL);
	}

	@Override
	public Composite createControls(Composite composite, int nColumns) {

		String niceName = ActivityUtil.getNiceName(this.fActivity)
				+ " operation";
		Group operationGroup = this.createGroup(composite, niceName, nColumns,
				new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		this.fServiceDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text0 = this.fServiceDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text0, this.getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text0);

		this.fPortDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text1 = this.fPortDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text1, Dialog.convertWidthInCharsToPixels(this
				.getFontMetrics(), 30));

		this.fOperationDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text2 = this.fOperationDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text2, Dialog.convertWidthInCharsToPixels(this
				.getFontMetrics(), 30));

		if (ActivityUtil.isReceiveFirstActivity(this.fActivity)) {
			this.fReceiveFaultField.doFillIntoGrid(operationGroup, nColumns);
		} else {
			this.fSendFaultField.doFillIntoGrid(operationGroup, nColumns);
		}

		if (ActivityUtil.isTwoWayActivity(this.fActivity)) {
			if (ActivityUtil.isReceiveFirstActivity(this.fActivity)) {
				this.fSendFaultField.doFillIntoGrid(operationGroup, nColumns);
			} else {
				this.fReceiveFaultField
						.doFillIntoGrid(operationGroup, nColumns);
			}
		}

		return operationGroup;
	}

	public QName getService() {
		return this.fService;
	}

	public String getPort() {
		return this.fPort;
	}

	public String getOperation() {
		return this.fOperation;
	}

	public boolean getSendFault() {
		return this.fSendFaultField.isSelected();
	}

	public boolean getReceiveFault() {
		return this.fReceiveFaultField.isSelected();
	}

	private void setProblem(String string) {
		this.setErrorMessage(string);
		this.setPageComplete(false);
		this.fireValueChanged(null);
	}

	private void setNoProblem() {
		this.setErrorMessage(null);
		this.setPageComplete(true);
		this.fireValueChanged(null);
	}

	public String getErrorMessage() {
		return this.fErrorMessage;
	}

	public boolean isPageComplete() {
		return this.fPageComplete;
	}

	private void setPageComplete(boolean isComplete) {
		this.fPageComplete = isComplete;
	}

	private void setErrorMessage(String string) {
		this.fErrorMessage = string;
	}

}
