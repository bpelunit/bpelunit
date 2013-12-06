/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.sections;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.dialog.DialogFieldValidator;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.FileField;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.xml.suite.XMLAbstractDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.util.WSDLFileFilter;
import net.bpelunit.toolsupport.util.WSDLFileValidator;
import net.bpelunit.toolsupport.util.WSDLReadingException;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * The partner section allows the user to add, edit, and remove partners to the
 * test suite.
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke
 * 
 */
public class PartnerSection extends ListSection {

	private static final String LABEL_ADD2BUTTON = "Add &WS-HT Track...";
	private static final String OPTION_WSDL_FILE = "WSDL file";
	private static final String OPTION_PARTNER_WSDL_FILE = "Partner WSDL file";
	private static final String OPTION_NAME = "Name";
	private static final String OPTION_WSHTENDPOINT = "WS-HT Endpoint";
	private static final String OPTION_USERNAME = "User Name";
	private static final String OPTION_PASSWORD = "Password";

	private class PartnerLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			if (element instanceof XMLHumanPartnerDeploymentInformation) {
				return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_HUMANTASK);
			}
			return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_DEPLOYER);
		}

		public String getText(Object element) {
			if (element instanceof XMLAbstractDeploymentInformation)
				return ((XMLAbstractDeploymentInformation) element).getName();
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
				XMLDeploymentSection element = (XMLDeploymentSection) inputElement;

				List<Object> partners = new ArrayList<Object>();
				partners.addAll(element.getPartnerList());
				partners.addAll(element.getHumanPartnerList());

				return partners.toArray();
			} else
				return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class WsdlValidator extends DialogFieldValidator {

		private boolean required;

		public WsdlValidator() {
			this(true);
		}

		public WsdlValidator(boolean required) {
			this.required = required;
		}

		@Override
		public String validate(String value) {
			if (!this.required) {
				return null;
			}

			if ("".equals(value)) {
				return "WSDL file must be specified.";
			}

			try {
				getEditor().getWsdlForFile(value);
			} catch (WSDLReadingException e) {
				String msg = e.getMessage() + ((e.getCause() != null) ? ": " + e.getCause() : "");
				return msg;
			}
			return null;
		}
	}

	public PartnerSection(Composite parent, TestSuitePage page, FormToolkit toolkit) {
		super(parent, toolkit, page, false, false, LABEL_ADD2BUTTON);
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
		XMLTestSuite model = getTestSuite();
		return model.getDeployment();
	}

	private class HumanPartnerPropertiesDialog extends FieldBasedInputDialog {
		private TextField nameField;
		private TextField wsthtEndpointField;
		private TextField usernameField;
		private TextField passwordField;

		public HumanPartnerPropertiesDialog(Shell parent, String title) {
			this(parent, title, "", null, null, null);
		}

		public HumanPartnerPropertiesDialog(Shell shell, String title, String partnerName,
				String wshtEndpoint, String username, String password) {
			super(shell, title);

			nameField = new TextField(this, OPTION_NAME, partnerName, TextField.Style.SINGLE);
			nameField.setValidator(new NotEmptyValidator("Partner Name"));
			this.addField(nameField);

			wsthtEndpointField = new TextField(this, OPTION_WSHTENDPOINT, wshtEndpoint,
					TextField.Style.SINGLE);
			wsthtEndpointField.setValidator(new NotEmptyValidator("WS-HT Endpoint"));
			this.addField(wsthtEndpointField);

			usernameField = new TextField(this, OPTION_USERNAME, username, TextField.Style.SINGLE);
			usernameField.setValidator(new NotEmptyValidator("WS-HT User"));
			this.addField(usernameField);

			passwordField = new TextField(this, OPTION_PASSWORD, password, TextField.Style.SINGLE);
			passwordField.setEchoChar('*');
			this.addField(passwordField);

		}

		public String getPartnerName() {
			return nameField.getSelection();
		}

		public String getWSHTEndpoint() {
			return wsthtEndpointField.getSelection();
		}

		public String getUsername() {
			return usernameField.getSelection();
		}

		public String getPassword() {
			return passwordField.getSelection();
		}

	}

	private class PartnerPropertiesDialog extends FieldBasedInputDialog {
		private FileField wsdlFileField;
		private TextField nameField;
		private FileField partnerWSDLFileField;

		public PartnerPropertiesDialog(Shell parent, String title) {
			this(parent, title, "", null, null);
		}

		public PartnerPropertiesDialog(Shell parent, String title, String partnerName,
				String wsdlFileName, String partnerWSDLFileName) {
			super(parent, title);

			nameField = new TextField(this, OPTION_NAME, partnerName, TextField.Style.SINGLE);
			nameField.setValidator(new NotEmptyValidator("Name"));
			this.addField(nameField);

			wsdlFileField = new FileField(this, OPTION_WSDL_FILE, wsdlFileName,
					new WSDLFileValidator(getEditor()), new WSDLFileFilter(), getEditor()
							.getCurrentProject(), getEditor().getCurrentDirectory());
			wsdlFileField.setValidator(new WsdlValidator());
			this.addField(wsdlFileField);

			partnerWSDLFileField = new FileField(this, OPTION_PARTNER_WSDL_FILE,
					partnerWSDLFileName, new WSDLFileValidator(getEditor(), false),
					new WSDLFileFilter(), getEditor().getCurrentProject(), getEditor()
							.getCurrentDirectory());
			partnerWSDLFileField.setValidator(new WsdlValidator(false));
			this.addField(partnerWSDLFileField);
		}

		public String getPartnerName() {
			return nameField.getSelection();
		}

		public String getWSDLFileName() {
			return wsdlFileField.getSelection();
		}

		public String getPartnerWSDLFileName() {
			return partnerWSDLFileField.getSelection();
		}
	}

	@Override
	public void createClient(Section section, FormToolkit toolkit) {
		super.createClient(section, toolkit);
	}

	@Override
	protected void addPressed() {
		PartnerPropertiesDialog dialog = new PartnerPropertiesDialog(getShell(), "Add a partner");

		if (dialog.open() != Window.OK) {
			return;
		}

		XMLPartnerDeploymentInformation information = getDeploymentXMLPart().addNewPartner();
		String partnerName = dialog.getPartnerName();
		if(partnerName != null) {
			partnerName = partnerName.trim();
		}
		information.setName(partnerName);
		
		addPartnerTrackToAllTestCases(information);
		updateValuesToModel(information, dialog);
	}

	@Override
	protected void add2Pressed() {
		HumanPartnerPropertiesDialog dialog = new HumanPartnerPropertiesDialog(getShell(),
				"Add a WS-HT Partner");

		if (dialog.open() != Window.OK) {
			return;
		}

		XMLHumanPartnerDeploymentInformation information = getDeploymentXMLPart()
				.addNewHumanPartner();
		
		String partnerName = dialog.getPartnerName();
		if(partnerName != null) {
			partnerName = partnerName.trim();
		}
		information.setName(partnerName);
		addPartnerTrackToAllTestCases(information);
		updateValuesToModel(information, dialog);
	}

	private void addPartnerTrackToAllTestCases(XMLAbstractDeploymentInformation partner) {
		List<XMLTestCase> testCases = getEditor().getTestSuite().getTestCases().getTestCaseList();

		if (partner instanceof XMLPartnerDeploymentInformation) {
			for (XMLTestCase testCase : testCases) {
				testCase.addNewPartnerTrack().setName(partner.getName());
			}
		} else {
			for (XMLTestCase testCase : testCases) {
				testCase.addNewHumanPartnerTrack().setName(partner.getName());
			}
		}
	}

	@Override
	protected void editPressed() {
		XMLAbstractDeploymentInformation currentlySelectedItem = getCurrentlySelectedItem();

		if (currentlySelectedItem instanceof XMLPartnerDeploymentInformation) {
			XMLPartnerDeploymentInformation currentPartner = (XMLPartnerDeploymentInformation) currentlySelectedItem;
			PartnerPropertiesDialog dialog = new PartnerPropertiesDialog(getShell(),
					"Edit a partner", currentPartner.getName(), currentPartner.getWsdl(),
					currentPartner.getPartnerWsdl());

			if (dialog.open() != Window.OK) {
				return;
			}

			changePartnerTrackNameInAllTestCases(currentlySelectedItem.getName(),
					dialog.getPartnerName().trim());
			updateValuesToModel(currentPartner, dialog);
		}

		if (currentlySelectedItem instanceof XMLHumanPartnerDeploymentInformation) {
			XMLHumanPartnerDeploymentInformation currentPartner = (XMLHumanPartnerDeploymentInformation) currentlySelectedItem;
			HumanPartnerPropertiesDialog dialog = new HumanPartnerPropertiesDialog(getShell(),
					"Edit a partner", currentPartner.getName(), currentPartner.getWshtEndpoint(),
					currentPartner.getUsername(), currentPartner.getPassword());

			if (dialog.open() != Window.OK) {
				return;
			}

			changePartnerTrackNameInAllTestCases(currentlySelectedItem.getName(),
					dialog.getPartnerName());
			updateValuesToModel(currentPartner, dialog);
		}

	}

	private void changePartnerTrackNameInAllTestCases(String oldName, String newName) {
		if (!oldName.equals(newName)) {
			List<XMLTestCase> testCases = getEditor().getTestSuite().getTestCases()
					.getTestCaseList();

			for (XMLTestCase testCase : testCases) {
				renamePartnerInTestCase(oldName, newName, testCase);
			}
		}
	}

	private void renamePartnerInTestCase(String oldName, String newName, XMLTestCase testCase) {
		List<XMLPartnerTrack> partnerTracks = testCase.getPartnerTrackList();
		for (XMLPartnerTrack pt : partnerTracks) {
			if (oldName.equals(pt.getName())) {
				pt.setName(newName);
				break;
			}
		}
		List<XMLHumanPartnerTrack> humanPartnerTracks = testCase.getHumanPartnerTrackList();
		for (XMLHumanPartnerTrack pt : humanPartnerTracks) {
			if (oldName.equals(pt.getName())) {
				pt.setName(newName);
				break;
			}
		}
	}

	private void updateValuesToModel(XMLPartnerDeploymentInformation modelItem,
			PartnerPropertiesDialog dialog) {
		String name = dialog.getPartnerName();
		String wsdlFileName = dialog.getWSDLFileName();
		String partnerWSDLFileName = dialog.getPartnerWSDLFileName();

		if (name != null && wsdlFileName != null && name.length() > 0 && wsdlFileName.length() > 0) {
			modelItem.setName(name.trim());
			modelItem.setWsdl(wsdlFileName);

			manageTargetNamespace(wsdlFileName);

			if (partnerWSDLFileName != null && !"".equals(partnerWSDLFileName)) {
				modelItem.setPartnerWsdl(partnerWSDLFileName);
				manageTargetNamespace(partnerWSDLFileName);
			} else {
				modelItem.setPartnerWsdl(null);
			}

			getEditor().refresh();
			markDirty();
		}
	}

	private void updateValuesToModel(XMLHumanPartnerDeploymentInformation modelItem,
			HumanPartnerPropertiesDialog dialog) {
		String name = dialog.getPartnerName();
		String wshtEndpoint = dialog.getWSHTEndpoint();
		String username = dialog.getUsername();
		String password = dialog.getPassword();

		if (name != null && wshtEndpoint != null && name.length() > 0 && wshtEndpoint.length() > 0
				&& username != null && username.length() > 0) {
			modelItem.setName(name);
			modelItem.setWshtEndpoint(wshtEndpoint);
			modelItem.setUsername(username);
			modelItem.setPassword(password);

			getEditor().refresh();
			markDirty();
		}
	}

	@Override
	protected void removePressed() {
		XMLAbstractDeploymentInformation currentlySelectedItem = getCurrentlySelectedItem();

		removePartnerFromTestCases(currentlySelectedItem.getName());
		removePartner(currentlySelectedItem);
		getEditor().refresh();
		setEditRemoveDuplicateEnabled(false);
		markDirty();
	}

	private void removePartnerFromTestCases(String name) {
		List<XMLTestCase> testCases = getEditor().getTestSuite().getTestCases().getTestCaseList();

		for (XMLTestCase tc : testCases) {
			List<XMLPartnerTrack> partnerTracks = tc.getPartnerTrackList();

			for (int i = 0; i < partnerTracks.size(); i++) {
				XMLPartnerTrack pt = partnerTracks.get(i);
				if (name.equals(pt.getName())) {
					tc.removePartnerTrack(i);
					break;
				}
			}

			List<XMLHumanPartnerTrack> humanPartnerTracks = tc.getHumanPartnerTrackList();
			for (int i = 0; i < humanPartnerTracks.size(); i++) {
				XMLHumanPartnerTrack pt = humanPartnerTracks.get(i);
				if (name.equals(pt.getName())) {
					tc.removeHumanPartnerTrack(i);
					break;
				}
			}
		}
	}

	private void removePartner(XMLAbstractDeploymentInformation currentlySelectedItem) {
		List<XMLPartnerDeploymentInformation> partnerList = getDeploymentXMLPart().getPartnerList();
		for (int i = 0; i < partnerList.size(); i++) {
			if (partnerList.get(i).equals(currentlySelectedItem)) {
				getDeploymentXMLPart().removePartner(i);
				break;
			}
		}
		List<XMLHumanPartnerDeploymentInformation> humanPartnerList = getDeploymentXMLPart()
				.getHumanPartnerList();
		for (int i = 0; i < humanPartnerList.size(); i++) {
			if (humanPartnerList.get(i).equals(currentlySelectedItem)) {
				getDeploymentXMLPart().removeHumanPartner(i);
				break;
			}
		}
	}

	private XMLAbstractDeploymentInformation getCurrentlySelectedItem() {
		return (XMLAbstractDeploymentInformation) getViewerSelection();
	}

	@Override
	protected void itemSelected(Object item) {
		// do nothing.
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {
		IMenuManager newMenu = new MenuManager("&New");

		createAction(newMenu, "Partner", new Action() {
			@Override
			public void run() {
				addPressed();
			}
		});

		manager.add(newMenu);

		ISelection selection = getViewer().getSelection();
		IStructuredSelection ssel = (IStructuredSelection) selection;
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
