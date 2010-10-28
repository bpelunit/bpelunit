/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.sections;

import org.bpelunit.framework.client.eclipse.dialog.DialogFieldValidator;
import org.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.bpelunit.framework.client.eclipse.dialog.field.FileField;
import org.bpelunit.framework.client.eclipse.dialog.field.TextField;
import org.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import org.bpelunit.framework.xml.suite.XMLDeploymentSection;
import org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.toolsupport.ToolSupportActivator;
import org.bpelunit.toolsupport.editors.TestSuitePage;
import org.bpelunit.toolsupport.util.WSDLFileFilter;
import org.bpelunit.toolsupport.util.WSDLFileValidator;
import org.bpelunit.toolsupport.util.WSDLReadingException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The partner section allows the user to add, edit, and remove partners to the test suite.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class PartnerSection extends ListSection {

	private static final String OPTION_WSDL_FILE= "WSDL file";
	private static final String OPTION_NAME= "Name";

	private class PartnerLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_DEPLOYER);
		}

		public String getText(Object element) {
			if (element instanceof XMLPartnerDeploymentInformation)
				return ((XMLPartnerDeploymentInformation) element).getName();
			else
				return "";
		}

		public void addListener(ILabelProviderListener listener) {
			// noop
		}

		public void dispose() {
			// noop
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
			// noop
		}
	}

	private class PartnerContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof XMLDeploymentSection) {
				XMLDeploymentSection element= (XMLDeploymentSection) inputElement;
				return element.getPartnerArray();
			} else
				return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class WsdlValidator extends DialogFieldValidator {
		@Override
		public String validate(String value) {
			if ("".equals(value))
				return "WSDL file must be specified.";

			try {
				getEditor().getWsdlForFile(value);
			} catch (WSDLReadingException e) {
				String msg= e.getMessage() + ( (e.getCause() != null) ? ": " + e.getCause() : "");
				return msg;
			}
			return null;
		}
	}

	public PartnerSection(Composite parent, TestSuitePage page, FormToolkit toolkit) {
		super(parent, toolkit, page);
		init();
	}

	private void init() {
		getViewer().setLabelProvider(new PartnerLabelProvider());
		getViewer().setContentProvider(new PartnerContentProvider());
	}

	@Override
	protected String getDescription() {
		return "Manage the partner processes.";
	}

	@Override
	protected String getName() {
		return "Partners";
	}

	@Override
	public void refresh() {
		setViewerInput(getDeploymentXMLPart());
		super.refresh();
	}

	private XMLDeploymentSection getDeploymentXMLPart() {
		XMLTestSuite model= getTestSuite();
		return model.getDeployment();
	}

	@Override
	protected void addPressed() {

		FieldBasedInputDialog dialog= new FieldBasedInputDialog(getShell(), "Add a partner");

		TextField nameField= new TextField(dialog, OPTION_NAME, null, TextField.Style.SINGLE);
		nameField.setValidator(new NotEmptyValidator("Name"));
		dialog.addField(nameField);

		FileField fileField= new FileField(dialog, OPTION_WSDL_FILE, null, new WSDLFileValidator(getEditor()), new WSDLFileFilter(), getEditor()
				.getCurrentProject(), getEditor().getCurrentDirectory());
		fileField.setValidator(new WsdlValidator());
		dialog.addField(fileField);

		if (dialog.open() != Window.OK) {
			return;
		}

		String name= nameField.getSelection();
		String file= fileField.getSelection();

		if (name != null && file != null && name.length() > 0 && file.length() > 0) {
			XMLPartnerDeploymentInformation information= getDeploymentXMLPart().addNewPartner();
			information.setName(name);
			information.setWsdl(file);

			manageTargetNamespace(file);

			// Don't call this.refresh(); changes dirty/stale states
			getViewer().refresh();
			markDirty();
		}
	}

	@Override
	protected void editPressed() {

		XMLPartnerDeploymentInformation currentlySelectedItem= getCurrentlySelectedItem();

		FieldBasedInputDialog dialog= new FieldBasedInputDialog(getShell(), "Edit a partner");

		TextField nameField= new TextField(dialog, OPTION_NAME, currentlySelectedItem.getName(), TextField.Style.SINGLE);
		nameField.setValidator(new NotEmptyValidator("Name"));
		dialog.addField(nameField);

		FileField fileField= new FileField(dialog, OPTION_WSDL_FILE, currentlySelectedItem.getWsdl(), new WSDLFileValidator(getEditor()),
				new WSDLFileFilter(), getEditor().getCurrentProject(), getEditor().getCurrentDirectory());
		fileField.setValidator(new WsdlValidator());
		dialog.addField(fileField);

		if (dialog.open() != Window.OK) {
			return;
		}

		String name= nameField.getSelection();
		String file= fileField.getSelection();

		if (name != null && file != null && name.length() > 0 && file.length() > 0) {
			currentlySelectedItem.setName(name);
			currentlySelectedItem.setWsdl(file);

			manageTargetNamespace(file);

			getViewer().refresh();
			markDirty();
		}

	}

	@Override
	protected void removePressed() {
		XMLPartnerDeploymentInformation currentlySelectedItem= getCurrentlySelectedItem();
		XMLPartnerDeploymentInformation[] partnerList= getDeploymentXMLPart().getPartnerArray();
		int i= 0;
		for (XMLPartnerDeploymentInformation information : partnerList) {
			if (information.equals(currentlySelectedItem)) {
				getDeploymentXMLPart().removePartner(i);
				break;
			}
			i++;
		}
		getViewer().refresh();
		setEditRemoveDuplicateEnabled(false);
		markDirty();
	}

	private XMLPartnerDeploymentInformation getCurrentlySelectedItem() {
		return (XMLPartnerDeploymentInformation) getViewerSelection();
	}

	@Override
	protected void itemSelected(Object item) {
		// do nothing.
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {

		IMenuManager newMenu= new MenuManager("&New");

		createAction(newMenu, "Partner", new Action() {
			@Override
			public void run() {
				addPressed();
			}
		});

		manager.add(newMenu);

		ISelection selection= getViewer().getSelection();
		IStructuredSelection ssel= (IStructuredSelection) selection;
		if (ssel.size() == 1) {
			manager.add(new Separator());

			createAction(manager, "&Edit", new Action() {
				@Override
				public void run() {
					editPressed();
				}
			});

			manager.add(new Separator());

			createAction(manager, "&Remove", new Action() {
				@Override
				public void run() {
					removePressed();
				}
			});
		}
	}

}
