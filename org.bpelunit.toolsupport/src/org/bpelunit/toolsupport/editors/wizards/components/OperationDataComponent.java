/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.components;

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
 * The OperationDataComponent allows the user to select a WSDL service, port, and operation for a
 * given partner, and
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class OperationDataComponent extends DataComponent {

	private class ServiceListener implements IStringButtonAdapter, IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			openServiceChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			handleServiceFieldChanged(field);
		}

	}

	private class PortListener implements IStringButtonAdapter, IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			openPortChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			handlePortFieldChanged(field);
		}

	}

	private class OperationListener implements IStringButtonAdapter, IDialogFieldListener {

		public void changeControlPressed(DialogField field) {
			openOperationChooser(field);
		}

		public void dialogFieldChanged(DialogField field) {
			handleOperationFieldChanged(field);
		}
	}

	class SimpleLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_DEPLOYER);
		}

		public String getText(Object element) {
			if (element instanceof QName)
				return ((QName) element).getLocalPart();
			else if (element instanceof String)
				return ((String) element);
			else
				return "Unknown Type";

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

	private static final String SEND_NAME= "send";
	private static final String RECEIVE_NAME= "receive";

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

	public OperationDataComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	public void handleServiceFieldChanged(DialogField field) {

		Definition def= getDefinition();
		if (def != null)
			fService= new QName(def.getTargetNamespace(), fServiceDialogField.getText());

		validateOperation(Verify.ALL);
	}

	public void handlePortFieldChanged(DialogField field) {
		fPort= fPortDialogField.getText();
		validateOperation(Verify.ALL);
	}

	public void handleOperationFieldChanged(DialogField field) {
		fOperation= fOperationDialogField.getText();
		validateOperation(Verify.ALL);
	}

	/**
	 * Validates the whole operation block "until" the given verification code.
	 * 
	 * @param v
	 * @return
	 */
	private boolean validateOperation(Verify v) {

		XMLTrack track= ActivityUtil.getEnclosingTrack(fActivity);

		if (track == null) {
			setProblem("Partner Track not found.");
			return false;
		}

		Definition def;
		try {
			def= getEditor().getWsdlForPartner(track);
		} catch (WSDLReadingException e) {
			String msg= e.getMessage() + e.getCause() != null ? e.getCause().getMessage() : "";
			setProblem(msg);
			return false;
		}

		if (def == null) {
			setProblem("WSDL definition was not found for this partner track.");
			return false;
		}

		if (v.equals(Verify.DEF)) {
			setNoProblem();
			return true;
		}

		if (isEmpty(fService)) {
			setProblem("Enter a service name.");
			return false;
		}

		Service service= def.getService(fService);
		if (service == null) {
			setProblem("Could not locate Service with name " + fService.toString());
			return false;
		}

		if (v.equals(Verify.SERVICE)) {
			setNoProblem();
			return true;
		}

		if (isEmpty(fPort)) {
			setProblem("Enter a port name.");
			return false;
		}

		Port port= getDefinition().getService(fService).getPort(fPort);
		if (port == null) {
			setProblem("Could not locate port with name " + fPort);
			return false;
		}

		if (v.equals(Verify.PORT)) {
			setNoProblem();
			return true;
		}

		if (isEmpty(fOperation)) {
			setProblem("Enter an operation name");
			return false;
		}

		Binding binding= getDefinition().getService(fService).getPort(fPort).getBinding();
		if ( (binding == null) || (binding.getBindingOperation(fOperation, null, null)) == null) {
			setProblem("Could not locate operation with name " + fOperation);
			return false;
		}

		setNoProblem();
		return true;
	}

	public void openServiceChooser(DialogField field) {

		Definition def= getDefinition();
		if (def == null) {
			MessageDialog.openError(getShell(), "Error", "Partner Definition is incorrect; Problem loading WSDL.");
			return;
		}
		/*
		 * The map is Map<QName, javax.wsdl.Service>.
		 */
		Map services= def.getServices();

		if (services != null) {
			ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), new SimpleLabelProvider());
			dialog.setElements(services.keySet().toArray());
			dialog.setTitle("Services for this partner");
			dialog.setMessage("Select one of the services to set it.");
			dialog.setMultipleSelection(false);
			int code= dialog.open();
			if (code == IDialogConstants.OK_ID) {
				Object[] res= dialog.getResult();
				if (res != null && res.length > 0) {
					QName serviceName= (QName) res[0];
					fService= serviceName;
					updateFields();
					validateOperation(Verify.ALL);
				}
			}
		}
	}

	private void openPortChooser(DialogField field) {

		if (validateOperation(Verify.SERVICE)) {
			Map ports= getDefinition().getService(fService).getPorts();
			String str= openStringChooser(ports.keySet().toArray());
			if (str != null) {
				fPort= str;
			}
			updateFields();
			validateOperation(Verify.ALL);
		} else
			MessageDialog.openError(getShell(), "Error", "Please select a valid service first.");
	}

	public void openOperationChooser(DialogField field) {

		if (validateOperation(Verify.PORT)) {
			List operations= getDefinition().getService(fService).getPort(fPort).getBinding().getBindingOperations();

			String[] options= new String[operations.size()];
			int i= 0;
			for (Object object : operations) {
				options[i]= ((BindingOperation) object).getName();
				i++;
			}

			String str= openStringChooser(options);
			if (str != null) {
				fOperation= str;
			}
			updateFields();
			validateOperation(Verify.ALL);
		} else
			MessageDialog.openError(getShell(), "Error", "Please select a valid service and port first.");
	}

	private boolean isEmpty(String something) {
		return (something == null || "".equals(something));
	}

	private boolean isEmpty(QName something) {
		return (something == null || "".equals(something.getLocalPart()));
	}

	private String openStringChooser(Object[] values) {
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), new SimpleLabelProvider());
		dialog.setElements(values);
		dialog.setTitle("Services for this partner");
		dialog.setMessage("Select one of the services to set it.");
		dialog.setMultipleSelection(false);
		int code= dialog.open();
		if (code == IDialogConstants.OK_ID) {
			Object[] res= dialog.getResult();
			if (res != null && res.length > 0) {
				return (String) res[0];
			}
		}
		return null;
	}

	private void updateFields() {
		if (fService != null)
			fServiceDialogField.setText(fService.getLocalPart());
		if (fPort != null)
			fPortDialogField.setText(fPort);
		if (fOperation != null)
			fOperationDialogField.setText(fOperation);
	}

	private Definition getDefinition() {
		XMLTrack track= ActivityUtil.getEnclosingTrack(fActivity);

		if (track == null) {
			setProblem("Partner Track not found - cannot continue.");
			return null;
		}

		Definition wsdlForPartner;
		try {
			wsdlForPartner= getEditor().getWsdlForPartner(track);
			return wsdlForPartner;
		} catch (WSDLReadingException e) {
			String msg= e.getMessage() + e.getCause() != null ? e.getCause().getMessage() : "";
			setProblem(msg);
			return null;
		}
	}

	public void init(XMLActivity activity) {

		fActivity= activity;

		try {
			fService= activity.getService();
			fPort= activity.getPort();
			fOperation= activity.getOperation();
		} catch (Exception e) {
			// nay happen in getService(), if the prefix is not bound
		}

		if (fService == null) {
			// an exception occurred
			fPort= null;
			fOperation= null;
		}

		ServiceListener serviceAdapter= new ServiceListener();
		fServiceDialogField= new StringButtonDialogField(serviceAdapter);
		fServiceDialogField.setDialogFieldListener(serviceAdapter);
		fServiceDialogField.setLabelText("Service");
		fServiceDialogField.setButtonLabel("Choose...");

		PortListener portListener= new PortListener();
		fPortDialogField= new StringButtonDialogField(portListener);
		fPortDialogField.setDialogFieldListener(portListener);
		fPortDialogField.setLabelText("Port");
		fPortDialogField.setButtonLabel("Choose...");

		OperationListener operationListener= new OperationListener();
		fOperationDialogField= new StringButtonDialogField(operationListener);
		fOperationDialogField.setDialogFieldListener(operationListener);
		fOperationDialogField.setLabelText("Operation");
		fOperationDialogField.setButtonLabel("Choose...");

		// Faults:

		fSendFaultField= new SelectionButtonDialogField(SWT.CHECK);
		fSendFaultField.setLabelText("Use fault element for " + SEND_NAME + " operation");
		fReceiveFaultField= new SelectionButtonDialogField(SWT.CHECK);
		fReceiveFaultField.setLabelText("Use fault element for " + RECEIVE_NAME + " operation");

		fSendFaultField.setSelection(ActivityUtil.getSendFault(fActivity));
		fReceiveFaultField.setSelection(ActivityUtil.getReceiveFault(fActivity));

		updateFields();
		validateOperation(Verify.ALL);
	}

	@Override
	public Composite createControls(Composite composite, int nColumns) {

		String niceName= ActivityUtil.getNiceName(fActivity) + " operation";
		Group operationGroup= createGroup(composite, niceName, nColumns, new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		fServiceDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text0= fServiceDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text0, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text0);

		fPortDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text1= fPortDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text1, Dialog.convertWidthInCharsToPixels(getFontMetrics(), 30));

		fOperationDialogField.doFillIntoGrid(operationGroup, nColumns);
		Text text2= fOperationDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text2, Dialog.convertWidthInCharsToPixels(getFontMetrics(), 30));

		if (ActivityUtil.isReceiveFirstActivity(fActivity))
			fReceiveFaultField.doFillIntoGrid(operationGroup, nColumns);
		else
			fSendFaultField.doFillIntoGrid(operationGroup, nColumns);

		if (ActivityUtil.isTwoWayActivity(fActivity)) {
			if (ActivityUtil.isReceiveFirstActivity(fActivity))
				fSendFaultField.doFillIntoGrid(operationGroup, nColumns);
			else
				fReceiveFaultField.doFillIntoGrid(operationGroup, nColumns);
		}

		return operationGroup;
	}

	public QName getService() {
		return fService;
	}

	public String getPort() {
		return fPort;
	}

	public String getOperation() {
		return fOperation;
	}

	public boolean getSendFault() {
		return fSendFaultField.isSelected();
	}

	public boolean getReceiveFault() {
		return fReceiveFaultField.isSelected();
	}

	private void setProblem(String string) {
		setErrorMessage(string);
		setPageComplete(false);
		fireValueChanged(null);
	}

	private void setNoProblem() {
		setErrorMessage(null);
		setPageComplete(true);
		fireValueChanged(null);
	}

	public String getErrorMessage() {
		return fErrorMessage;
	}

	public boolean isPageComplete() {
		return fPageComplete;
	}

	private void setPageComplete(boolean isComplete) {
		fPageComplete= isComplete;
	}

	private void setErrorMessage(String string) {
		fErrorMessage= string;
	}

}
