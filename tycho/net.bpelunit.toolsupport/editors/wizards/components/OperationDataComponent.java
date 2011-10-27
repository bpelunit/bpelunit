/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.xml.namespace.QName;

import net.bpelunit.framework.control.util.ActivityUtil;
import net.bpelunit.framework.xml.suite.XMLActivity;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import net.bpelunit.toolsupport.editors.wizards.fields.IStringButtonAdapter;
import net.bpelunit.toolsupport.editors.wizards.fields.LayoutUtil;
import net.bpelunit.toolsupport.editors.wizards.fields.SelectionButtonDialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.StringButtonDialogField;
import net.bpelunit.toolsupport.editors.wizards.pages.OperationWizardPage;
import net.bpelunit.toolsupport.util.AggregatedWSDLDefinitionFacade;
import net.bpelunit.toolsupport.util.WSDLReadingException;
import net.bpelunit.toolsupport.util.schema.WSDLParser;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
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

	private static final String REGLAR_MESSAGE_SELITEM = "regular message";
	private static final String CUSTOM_FAULT_SELITEM = "custom fault";

	private class ServiceListener implements IStringButtonAdapter, IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			OperationDataComponent.this.openServiceChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			OperationDataComponent.this.handleServiceFieldChanged(field);
		}

	}

	private class OutputListener implements IStringButtonAdapter, IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			OperationDataComponent.this.openOutputChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			OperationDataComponent.this.handleOutputFieldChanged(field);
		}

	}

	private class PortListener implements IStringButtonAdapter, IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			OperationDataComponent.this.openPortChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			OperationDataComponent.this.handlePortFieldChanged(field);
		}

	}

	private class OperationListener implements IStringButtonAdapter, IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			OperationDataComponent.this.openOperationChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			OperationDataComponent.this.handleOperationFieldChanged(field);
		}
	}

	class SimpleLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_DEPLOYER);
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
		DEF, SERVICE, PORT, OPERATION, ALL
	}

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
	private SelectionButtonDialogField fReceiveFaultField;

	private List<MessageChangeListener> messageChangeListener = new ArrayList<MessageChangeListener>();
	private StringButtonDialogField fOutputDialogField;

	private boolean fSendFault;
	private String fSendFaultName;

	public OperationDataComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	public void handleOutputFieldChanged(DialogField field) {
		this.setFaultByMenuSelection(this.fOutputDialogField.getText());
		fireMessageChanged();
	}

	public void handleServiceFieldChanged(DialogField field) {

		Definition def = this.getDefinition();

		// We shouldn't assume the service mentioned in the text field belongs
		// to the target namespace. The user only gets to write the local part:
		// the namespace URI might be from one of the imported WSDL definitions.
		// It's better to look at all the services available in the definition
		// and get the first service whose name has a matching local part.

		// There might be more than one candidate: in that case, the user should
		// use the 'Choose...' dialog. We need to make sure we don't clobber the
		// user's selection back to the first candidate. We'll only update the
		// fService field here when there's no service selected or the local
		// name of the current service doesn't match the contents of the text
		// field.

		final String fieldText = this.fServiceDialogField.getText();
		if (def != null
				&& (this.fService == null || !this.fService.getLocalPart().equals(fieldText))) {
			@SuppressWarnings("unchecked")
			Collection<Service> services = def.getServices().values();
			for (Service s : services) {
				final QName sName = s.getQName();
				if (sName.getLocalPart().equals(fieldText)) {
					this.fService = sName;
				}
			}
		}

		this.validateOperation(Verify.ALL);
	}

	public void handlePortFieldChanged(DialogField field) {
		this.fPort = this.fPortDialogField.getText();
		this.validateOperation(Verify.ALL);
	}

	public void handleOperationFieldChanged(DialogField field) {
		this.fOperation = this.fOperationDialogField.getText();
		fireMessageChanged();
	}

	public void addMessageListener(MessageChangeListener listener) {
		for (MessageChangeListener ol : this.messageChangeListener) {
			if (ol == listener) {
				return;
			}
		}
		this.messageChangeListener.add(listener);
		if (this.getWizardPage() instanceof OperationWizardPage) {
			Element element;
			try {
				element = ((OperationWizardPage) this.getWizardPage()).getElementForOperation();
				if (this.validateOperation(Verify.ALL) && listener instanceof SendComponent
						&& element != null) {
					((SendComponent) listener).setOperationMessage(element, false);
				}
			} catch (Exception e1) {
				// Error will (sometimes) be shown elsewhere as well
				ToolSupportActivator.log(e1);
			}
		}
	}

	public void removeOperationListener(MessageChangeListener listener) {
		this.messageChangeListener.remove(listener);
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
			def = this.getDefinition();
		} catch (Exception e) {
			String msg = e.getMessage() + e.getCause() != null ? e.getCause().getMessage() : "";
			this.setProblem(msg);
			return false;
		}

		if (def == null) {
			this.setProblem("WSDL definition was not found for this partner track.");
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
			this.setProblem("Could not locate Service with name " + this.fService.toString());
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

		Port port = this.getDefinition().getService(this.fService).getPort(this.fPort);
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

		Binding binding = this.getDefinition().getService(this.fService).getPort(this.fPort)
				.getBinding();
		if ((binding == null) || (binding.getBindingOperation(this.fOperation, null, null)) == null) {
			this.setProblem("Could not locate operation with name " + this.fOperation);
			return false;
		}

		if (v.equals(Verify.OPERATION)) {
			this.setNoProblem();
			return true;
		}

		// If we're sending a specific fault, check that the fault exists
		if ((!ActivityUtil.isReceiveFirstActivity(fActivity) || ActivityUtil
				.isTwoWayActivity(fActivity)) && !isEmpty(this.fSendFaultName)) {
			Operation op = getOperationByName(this.fOperation);
			if (op != null && op.getFault(fSendFaultName) == null) {
				this.setProblem("Could not locate fault with name " + this.fSendFaultName);
				return false;
			}
		}

		this.setNoProblem();
		return true;
	}

	@SuppressWarnings("unchecked")
	public void openServiceChooser(DialogField field) {

		Definition def = this.getDefinition();
		if (def == null) {
			showErrorDialog("Partner Definition is incorrect; Problem loading WSDL.");
			return;
		}

		Map<QName, javax.wsdl.Service> services = def.getServices();

		if (services != null) {
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(this.getShell(),
					new SimpleLabelProvider());
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
			Map<?, ?> ports = this.getDefinition().getService(this.fService).getPorts();
			String str = this.openStringChooser(ports.keySet().toArray());
			if (str != null) {
				this.fPort = str;
			}
			this.updateFields();
			this.validateOperation(Verify.ALL);
		} else {
			showErrorDialog("Please select a valid service first.");
		}
	}

	@SuppressWarnings("unchecked")
	public void openOperationChooser(DialogField field) {

		if (this.validateOperation(Verify.PORT)) {
			List<BindingOperation> operations = this.getDefinition().getService(this.fService)
					.getPort(this.fPort).getBinding().getBindingOperations();

			String[] options = new String[operations.size()];
			int i = 0;
			for (BindingOperation operation : operations) {
				options[i] = operation.getName();
				i++;
			}

			String str = this.openStringChooser(options);
			if (str != null) {
				this.fOperation = str;
			}
			this.updateFields();
			this.validateOperation(Verify.ALL);
		} else {
			showErrorDialog("Please select a valid service and port first.");
		}
	}

	private void showErrorDialog(final String msg) {
		MessageDialog.openError(this.getShell(), "Error", msg);
	}

	@SuppressWarnings("unchecked")
	private void openOutputChooser(DialogField field) {
		if (this.validateOperation(Verify.OPERATION)) {
			Operation op = getOperationByName(this.fOperation);
			if (op == null) {
				showErrorDialog("Couldn't find information about the selected Operation "
						+ fOperation);
				return;
			}

			int iOption = 0;
			Collection<Fault> faults = op.getFaults().values();
			String[] options = new String[2 + faults.size()];
			options[iOption++] = REGLAR_MESSAGE_SELITEM;
			options[iOption++] = CUSTOM_FAULT_SELITEM;
			for (Fault fault : faults) {
				options[iOption++] = fault.getName();
			}

			setFaultByMenuSelection(this.openStringChooser(options));
			this.updateFields();
			this.validateOperation(Verify.ALL);
		} else {
			showErrorDialog("Please select a valid service, port and operation first.");
		}
	}

	private void setFaultByMenuSelection(String str) {
		if (isEmpty(str) || REGLAR_MESSAGE_SELITEM.equals(str)) {
			this.fSendFault = false;
			this.fSendFaultName = null;
		} else if (CUSTOM_FAULT_SELITEM.equals(str)) {
			this.fSendFault = true;
			this.fSendFaultName = null;
		} else {
			this.fSendFault = true;
			this.fSendFaultName = str;
		}
	}

	private boolean isEmpty(String something) {
		return (something == null || "".equals(something));
	}

	private boolean isEmpty(QName something) {
		return (something == null || "".equals(something.getLocalPart()));
	}

	private String openStringChooser(Object[] values) {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(this.getShell(),
				new SimpleLabelProvider());
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
		if (this.fSendFault) {
			this.fOutputDialogField.setText(!isEmpty(this.fSendFaultName) ? this.fSendFaultName
					: CUSTOM_FAULT_SELITEM);
		} else {
			this.fOutputDialogField.setText(REGLAR_MESSAGE_SELITEM);
		}
	}

	public AggregatedWSDLDefinitionFacade getDefinition() {
		XMLTrack track = ActivityUtil.getEnclosingTrack(this.fActivity);

		if (track == null) {
			this.setProblem("Partner Track not found - cannot continue.");
			return null;
		}

		Definition wsdlForPartner;
		try {
			wsdlForPartner = this.getEditor().getWsdlForPartner(track);
		} catch (WSDLReadingException e) {
			String msg = e.getMessage() + e.getCause() != null ? e.getCause().getMessage() : "";
			this.setProblem(msg);
			return null;
		}

		Definition partnerWSDLForPartner = null;
		try {
			partnerWSDLForPartner = this.getEditor().getPartnerWsdlForPartner(track);
		} catch (WSDLReadingException e) {
		}

		if (partnerWSDLForPartner == null) {
			return new AggregatedWSDLDefinitionFacade(wsdlForPartner);
		} else {
			return new AggregatedWSDLDefinitionFacade(wsdlForPartner, partnerWSDLForPartner);
		}
	}

	public WSDLParser getWSDLParser() {
		AggregatedWSDLDefinitionFacade facade = this.getDefinition();

		try {
			Definition definition = facade.getDefinition(fService.getNamespaceURI());
			return this.getEditor().getWSDLParserForDefinition(definition);
		} catch (Exception e) {
			return null;
		}
	}

	public void init(XMLSoapActivity activity) {

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
		this.fOperationDialogField = new StringButtonDialogField(operationListener);
		this.fOperationDialogField.setDialogFieldListener(operationListener);
		this.fOperationDialogField.setLabelText("Operation");
		this.fOperationDialogField.setButtonLabel("Choose...");

		OutputListener outputListener = new OutputListener();
		this.fOutputDialogField = new StringButtonDialogField(outputListener);
		this.fOutputDialogField.setDialogFieldListener(outputListener);
		this.fOutputDialogField.setLabelText("Send");
		this.fOutputDialogField.setButtonLabel("Choose...");

		// Faults:
		this.fSendFault = ActivityUtil.getSendFault(fActivity);
		this.fSendFaultName = ActivityUtil.getSendFaultString(fActivity);

		this.fReceiveFaultField = new SelectionButtonDialogField(SWT.CHECK);
		this.fReceiveFaultField
				.setLabelText("Use fault element for " + RECEIVE_NAME + " operation");

		this.fReceiveFaultField.setSelection(ActivityUtil.getReceiveFault(this.fActivity));

		this.updateFields();
		this.validateOperation(Verify.ALL);
	}

	@Override
	public Composite createControls(Composite composite, int nColumns) {

		String niceName = ActivityUtil.getNiceName(this.fActivity) + " operation";
		Group operationGroup = this.createGroup(composite, niceName, nColumns, new GridData(
				SWT.FILL, SWT.BEGINNING, true, false));

		this.fServiceDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text0 = this.fServiceDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text0, this.getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text0);

		this.fPortDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text1 = this.fPortDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text1,
				Dialog.convertWidthInCharsToPixels(this.getFontMetrics(), 30));

		this.fOperationDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text2 = this.fOperationDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text2,
				Dialog.convertWidthInCharsToPixels(this.getFontMetrics(), 30));

		if (ActivityUtil.isReceiveFirstActivity(this.fActivity)) {
			this.fReceiveFaultField.doFillIntoGrid(operationGroup, nColumns);
		} else {
			this.fOutputDialogField.doFillIntoGrid(operationGroup, nColumns);
		}

		if (ActivityUtil.isTwoWayActivity(this.fActivity)) {
			if (ActivityUtil.isReceiveFirstActivity(this.fActivity)) {
				this.fOutputDialogField.doFillIntoGrid(operationGroup, nColumns);
			} else {
				this.fReceiveFaultField.doFillIntoGrid(operationGroup, nColumns);
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
		return this.fSendFault;
	}

	public String getSendFaultName() {
		return this.fSendFaultName;
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

	@SuppressWarnings("unchecked")
	private Operation getOperationByName(String opName) {
		List<BindingOperation> bindingOps = this.getDefinition().getService(this.fService)
				.getPort(this.fPort).getBinding().getBindingOperations();
		for (BindingOperation bindingOp : bindingOps) {
			if (opName.equals(bindingOp.getOperation().getName())) {
				return bindingOp.getOperation();
			}
		}
		return null;
	}

	private void fireMessageChanged() {
		if (this.validateOperation(Verify.ALL)) {
			if (this.getWizardPage() instanceof OperationWizardPage) {
				Element element;
				try {
					element = ((OperationWizardPage) this.getWizardPage()).getElementForOperation();
					for (MessageChangeListener listener : this.messageChangeListener) {
						listener.messageChanged(element);
					}
				} catch (Exception e1) {
					// Error will (sometimes) be shown elsewhere as well
					ToolSupportActivator.log(e1);
				}
			}
		}
	}
}
