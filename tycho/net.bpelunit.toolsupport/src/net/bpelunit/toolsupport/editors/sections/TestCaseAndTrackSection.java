/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.sections;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.CheckBoxField;
import net.bpelunit.framework.client.eclipse.dialog.field.ComboField;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.client.eclipse.dialog.validate.NullValidator;
import net.bpelunit.framework.control.util.ActivityUtil;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLSetUp;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.SetupWizard;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.Node;

/**
 * The TestCaseAndTrack section offers a tree view of test cases and their
 * associated partner tracks, allowing the user to add, edit, remove, and move
 * test cases and partner tracks.
 * 
 * @version $Id: TestCaseAndTrackSection.java 81 2007-06-03 10:07:37Z asalnikow
 *          $
 * @author Philip Mayer
 * 
 */
public class TestCaseAndTrackSection extends TreeSection {

	private XMLTestSuite testSuite;

	private class TestCaseLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_TESTCASE);
		}

		public String getText(Object element) {
			if (element instanceof XMLTestCase)
				return ((XMLTestCase) element).getName();
			else if (element instanceof XMLPartnerTrack)
				return ((XMLPartnerTrack) element).getName();
			else if (element instanceof XMLHumanPartnerTrack) {
				return ((XMLHumanPartnerTrack) element).getName();
			} else
				return BPELUnitConstants.CLIENT_NAME;
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

	private class TestCaseContentProvider implements ITreeContentProvider {

		private final Object[] EMPTY_LIST = new Object[0];

		private XMLTestCasesSection fSection;

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof XMLTestCasesSection) {
				XMLTestCasesSection element = (XMLTestCasesSection) inputElement;
				return element.getTestCaseList().toArray();
			} else
				return EMPTY_LIST;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof XMLTestCasesSection)
				fSection = (XMLTestCasesSection) newInput;

		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof XMLTestCase) {
				XMLTestCase testCase = (XMLTestCase) parentElement;
				List<Object> tracks = new ArrayList<Object>();
				tracks.addAll(testCase.getPartnerTrackList());
				tracks.addAll(testCase.getHumanPartnerTrackList());
				if (testCase.getClientTrack() != null)
					tracks.add(testCase.getClientTrack());
				return tracks.toArray();
			} else
				return EMPTY_LIST;
		}

		public Object getParent(Object element) {
			if (element instanceof XMLTrack) {
				XMLTrack track = (XMLTrack) element;
				List<XMLTestCase> testCaseList = fSection.getTestCaseList();
				for (XMLTestCase case1 : testCaseList) {
					List<XMLPartnerTrack> partnerTrackList = case1.getPartnerTrackList();
					for (XMLPartnerTrack track2 : partnerTrackList) {
						if (track2.equals(track))
							return case1;
					}
					if (element.equals(case1.getClientTrack()))
						return case1;
				}
			}

			if (element instanceof XMLHumanPartnerTrack) {
				XMLHumanPartnerTrack track = (XMLHumanPartnerTrack) element;
				List<XMLTestCase> testCaseList = fSection.getTestCaseList();
				for (XMLTestCase case1 : testCaseList) {
					List<XMLHumanPartnerTrack> partnerTrackList = case1.getHumanPartnerTrackList();
					for (XMLHumanPartnerTrack track2 : partnerTrackList) {
						if (track2.equals(track))
							return case1;
					}
				}
			}

			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof XMLTestCase) {
				XMLTestCase testCase = (XMLTestCase) element;
				return testCase.getPartnerTrackList().size() > 0
						|| testCase.getHumanPartnerTrackList().size() > 0
						|| testCase.getClientTrack() != null;
			} else
				return false;
		}

	}

	public TestCaseAndTrackSection(Composite parent, TestSuitePage page, FormToolkit toolkit) {
		super(parent, toolkit, page, true, true, null);
		testSuite = page.getSuiteEditor().getTestSuite();

		init();
	}

	private void init() {
		getViewer().setLabelProvider(new TestCaseLabelProvider());
		getViewer().setContentProvider(new TestCaseContentProvider());
	}

	@Override
	protected String getDescription() {
		return "Manage test cases and partner tracks.";
	}

	@Override
	protected String getName() {
		return "Test Cases and Tracks";
	}

	@Override
	public void refresh() {
		setViewerInput(getTestCasesXMLPart());
		getTreeViewer().expandAll();
		super.refresh();
	}

	private XMLTestCasesSection getTestCasesXMLPart() {
		XMLTestSuite model = getEditor().getTestSuite();
		return model.getTestCases();
	}

	@Override
	protected void addPressed() {
		addTestCase();
	}

	protected void addPartnerTrack(XMLTestCase to) {
		String name = editPartnerTrack("Add a new partner track", null);

		if (name != null && name.length() > 0) {
			XMLPartnerTrack track = to.addNewPartnerTrack();
			track.setName(name);
			adjust();
		}
	}

	protected void addHumanPartnerTrack(XMLTestCase to) {
		String name = editHumanPartnerTrack("Add a new WS-HT Track", null);

		if (name != null && name.length() > 0) {
			XMLHumanPartnerTrack track = to.addNewHumanPartnerTrack();
			track.setName(name);
			adjust();
		}
	}

	protected void addClientTrack(XMLTestCase to) {
		to.addNewClientTrack();
		adjust();
	}

	private void addTestCase() {
		String[] results = editTestCase("Add a new test case", null, null, false, false, null);

		if (results != null) {
			XMLTestCase testCase = getTestCasesXMLPart().addNewTestCase();

			// initialize the test case
			testCase.setName(results[0]);
			testCase.setBasedOn(results[1]);
			testCase.setAbstract(Boolean.parseBoolean(results[2]));
			testCase.setVary(Boolean.parseBoolean(results[3]));

			testCase.addNewClientTrack();
			List<XMLPartnerDeploymentInformation> partnerTrackDeploymentInformations = getAllPartnerTracks();
			for (XMLPartnerDeploymentInformation information : partnerTrackDeploymentInformations) {
				XMLPartnerTrack track = testCase.addNewPartnerTrack();
				track.setName(information.getName());
			}
			List<XMLHumanPartnerDeploymentInformation> humanPartnerDeploymentInformations = getAllHumanPartnerTracks();
			for (XMLHumanPartnerDeploymentInformation information : humanPartnerDeploymentInformations) {
				XMLHumanPartnerTrack track = testCase.addNewHumanPartnerTrack();
				track.setName(information.getName());
			}
			
			adjust();
			getTreeViewer().expandToLevel(testCase, AbstractTreeViewer.ALL_LEVELS);
			getTreeViewer().setSelection(new StructuredSelection(testCase));
		}
	}

	private List<XMLPartnerDeploymentInformation> getAllPartnerTracks() {
		XMLTestSuite model = getEditor().getTestSuite();
		List<XMLPartnerDeploymentInformation> partnerList = model.getDeployment().getPartnerList();

		return partnerList;
	}
	
	private List<XMLHumanPartnerDeploymentInformation> getAllHumanPartnerTracks() {
		XMLTestSuite model = getEditor().getTestSuite();
		List<XMLHumanPartnerDeploymentInformation> partnerList = model.getDeployment().getHumanPartnerList();
		
		return partnerList;
	}

	@Override
	protected void editPressed() {

		Object current = getViewerSelection();
		if (current instanceof XMLTestCase) {

			XMLTestCase testCase = (XMLTestCase) current;

			editTestCase("Edit a test case", testCase.getName(),
					testCase.getBasedOn(), testCase.getAbstract(), testCase.getVary(), testCase);
			

		} else if (current instanceof XMLPartnerTrack) {
			// TODO use a combo
			editPartnerTrack((XMLPartnerTrack) current);
		} else if (current instanceof XMLPartnerTrack) {
			editHumanPartnerTrack((XMLHumanPartnerTrack) current);
		}
	}

	private void editPartnerTrack(XMLPartnerTrack track) {
		String name = editPartnerTrack("Edit a partner track", track.getName());
		if (name != null) {
			track.setName(name);
			setEditRemoveDuplicateEnabled(true);
			adjust();
		}
	}

	private void editHumanPartnerTrack(XMLHumanPartnerTrack track) {
		String name = editPartnerTrack("Edit a WS-HT Track", track.getName());
		if (name != null) {
			track.setName(name);
			setEditRemoveDuplicateEnabled(true);
			adjust();
		}
	}

	@Override
	protected void removePressed() {
		Object current = getViewerSelection();

		if (current instanceof XMLTestCase) {
			removeTestCase(current);
		} else if (current instanceof XMLTrack) {
			removeTrack((XMLTrack) current);
		} else if (current instanceof XMLHumanPartnerTrack) {
			removeTrack((XMLHumanPartnerTrack) current);
		}
	}

	private void removeTrack(XMLHumanPartnerTrack track) {
		XMLTestCase current = getTestCase(track);
		List<XMLHumanPartnerTrack> partnerTrackList = current.getHumanPartnerTrackList();
		int index = partnerTrackList.indexOf(track);
		if (index >= 0) {
			current.removeHumanPartnerTrack(index);
			getPage().postTrackSelected((XMLHumanPartnerTrack) null);
		}

		getViewer().refresh();
		setEditRemoveDuplicateEnabled(false);
		markDirty();
	}

	private void removeTestCase(Object current) {
		XMLTestCasesSection testCaseSection = getTestCasesXMLPart();
		List<XMLTestCase> testCaseList = testCaseSection.getTestCaseList();
		int i = 0;
		for (XMLTestCase testCase : testCaseList) {
			if (testCase.equals(current)) {
				testCaseSection.removeTestCase(i);
				break;
			}
			i++;
		}
		getViewer().refresh();
		setEditRemoveDuplicateEnabled(false);
		markDirty();
	}

	private XMLTestCase getTestCase(XmlObject o) {
		XmlCursor c = o.newCursor();
		if (!c.toParent()) {
			return null;
		}

		XmlObject object = c.getObject();
		if (object instanceof XMLTestCase) {
			return (XMLTestCase) object;
		} else {
			return null;
		}
	}

	private void removeTrack(XMLTrack track) {
		XMLTestCase current = getTestCase(track);
		List<XMLPartnerTrack> partnerTrackList = current.getPartnerTrackList();
		int index = partnerTrackList.indexOf(track);
		if (index >= 0) {
			current.removePartnerTrack(index);
			getPage().postTrackSelected((XMLTrack) null);
		}

		getViewer().refresh();
		setEditRemoveDuplicateEnabled(false);
		markDirty();
	}

	@Override
	protected void upPressed() {
		Object viewerSelection = getViewerSelection();
		if (viewerSelection instanceof XMLTestCase) {
			// move the current activity to the one before
			XMLTestCase xmlTestCase = (XMLTestCase) viewerSelection;
			int currentPosition = testSuite.getTestCases().getTestCaseList().indexOf(xmlTestCase);
			XmlCursor currentCursor = xmlTestCase.newCursor();
			// Does this activity even have a previous sibling?
			if (currentCursor.toPrevSibling()) {
				// move from the current activity to the position of the
				// previous sibling, i.e. one up.
				xmlTestCase.newCursor().moveXml(currentCursor);
				adjust();
				setSelection(testSuite.getTestCases().getTestCaseList().get(currentPosition - 1));
			}
		}
	}

	@Override
	protected void downPressed() {
		Object viewerSelection = getViewerSelection();
		if (viewerSelection instanceof XMLTestCase) {
			// move the current activity to the one before
			XMLTestCase xmlTestCase = (XMLTestCase) viewerSelection;
			int currentPosition = testSuite.getTestCases().getTestCaseList().indexOf(xmlTestCase);
			XmlCursor currentCursor = xmlTestCase.newCursor();
			// Does this activity even have a next sibling?
			if (currentCursor.toNextSibling()) {
				// Yes, it does -> move that sibling up to the current activity
				// position
				currentCursor.moveXml(xmlTestCase.newCursor());
				adjust();
				setSelection(testSuite.getTestCases().getTestCaseList().get(currentPosition + 1));
			}
		}
	}

	@Override
	protected void duplicatePressed() {
		Object viewerSelection = getViewerSelection();
		if (viewerSelection instanceof XMLTestCase) {
			// move the current activity to the one before
			XMLTestCase testCase = (XMLTestCase) viewerSelection;

			Node node = testCase.getDomNode();
			node.getParentNode().appendChild(node.cloneNode(true));

			adjust();
			List<XMLTestCase> testCases = testSuite.getTestCases().getTestCaseList();
			setSelection(testCases.get(testCases.size() - 1));
		}
	}

	private void adjust() {
		// Don't call this.refresh(); changes dirty/stale states
		getViewer().refresh();
		markDirty();
	}

	private String editPartnerTrack(String title, String current) {

		List<XMLPartnerDeploymentInformation> partnerList = getEditor().getTestSuite()
				.getDeployment().getPartnerList();
		String[] partnerNames = new String[partnerList.size()];

		int i = 0;
		boolean found = false;
		for (XMLPartnerDeploymentInformation information : partnerList) {
			partnerNames[i] = information.getName();
			if (partnerNames[i].equals(current))
				found = true;
			i++;
		}
		if (!found) {
			// The partner track seems to have been deleted. Cannot display
			// it...
			current = null;
		}

		FieldBasedInputDialog dialog = new FieldBasedInputDialog(getShell(), title);
		ComboField combo = new ComboField(dialog, "Name:", current, partnerNames);
		combo.setValidator(new NotEmptyValidator("Name"));
		dialog.addField(combo);

		if (dialog.open() != Window.OK)
			return null;

		return combo.getSelection();
	}

	private String editHumanPartnerTrack(String title, String current) {

		List<XMLHumanPartnerDeploymentInformation> partnerList = getEditor().getTestSuite()
				.getDeployment().getHumanPartnerList();
		String[] partnerNames = new String[partnerList.size()];

		int i = 0;
		boolean found = false;
		for (XMLHumanPartnerDeploymentInformation information : partnerList) {
			partnerNames[i] = information.getName();
			if (partnerNames[i].equals(current))
				found = true;
			i++;
		}
		if (!found) {
			// The partner track seems to have been deleted. Cannot display
			// it...
			current = null;
		}

		FieldBasedInputDialog dialog = new FieldBasedInputDialog(getShell(), title);
		ComboField combo = new ComboField(dialog, "Name:", current, partnerNames);
		combo.setValidator(new NotEmptyValidator("Name"));
		dialog.addField(combo);

		if (dialog.open() != Window.OK)
			return null;

		return combo.getSelection();
	}

	private boolean openWizard(IWizard wizard) {

		WizardDialog dialog = new WizardDialog(getShell(), wizard);
		return (dialog.open() == Window.OK);
	}

	private String[] editTestCase(String title, String currentName, String currentBasedOn,
			boolean currentAbstractSetting, boolean currentVarySetting, XMLTestCase testCase) {

		FieldBasedInputDialog dialog = new FieldBasedInputDialog(getShell(), title);

		TextField nameField = new TextField(dialog, "Name:", currentName, TextField.Style.SINGLE);
		nameField.setValidator(new NotEmptyValidator("Name"));

		TextField basedOnField = new TextField(dialog, "Based On:", currentBasedOn,
				TextField.Style.SINGLE);
		basedOnField.setValidator(new NullValidator());

		CheckBoxField abstractField = new CheckBoxField(dialog, "Abstract", currentAbstractSetting);
		abstractField.setValidator(new NotEmptyValidator("Abstract"));

		CheckBoxField varyField = new CheckBoxField(dialog, "Vary send delay times",
				currentVarySetting);
		varyField.setValidator(new NotEmptyValidator("Vary"));

		if (testCase != null) {
			XMLSetUp setup = testCase.getSetUp();
			if (setup == null) {
				setup = testCase.addNewSetUp();
			}
			Wizard wizard = new SetupWizard(getPage(), ActivityEditMode.ADD, setup, testCase);

			if (openWizard(wizard)) {
				markDirty();
			}
			if (!setup.isSetDataSource() && !setup.isSetScript()) {
				testCase.unsetSetUp();
			}
			setEditRemoveDuplicateEnabled(true);
			adjust();

		} else {

			dialog.addField(nameField);

			dialog.addField(basedOnField);

			dialog.addField(abstractField);

			dialog.addField(varyField);

			if (dialog.open() != Window.OK)
				return null;
		}
		return new String[] { nameField.getSelection(), basedOnField.getSelection(),
				abstractField.getSelection(), varyField.getSelection() };

	}

	@Override
	protected void itemSelected(Object firstElement) {
		if (firstElement instanceof XMLTrack) {
			XMLTrack selection = (XMLTrack) firstElement;
			getPage().postTrackSelected(selection);
		} else if (firstElement instanceof XMLHumanPartnerTrack) {
			XMLHumanPartnerTrack selection = (XMLHumanPartnerTrack) firstElement;
			getPage().postTrackSelected(selection);
		}
		setEnabled(BUTTON_REMOVE, getIsDeleteEnabled(firstElement));
		setEnabled(BUTTON_EDIT, getIsEditEnabled(firstElement));
		setEnabled(BUTTON_DUPLICATE, getIsDuplicateEnabled(firstElement));
		setEnabled(
				BUTTON_UP,
				getIsMoveEnabled(firstElement)
						&& ActivityUtil.hasPrevious((XmlObject) firstElement));
		setEnabled(BUTTON_DOWN,
				getIsMoveEnabled(firstElement) && ActivityUtil.hasNext((XmlObject) firstElement));
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {
		ISelection selection = getViewer().getSelection();
		IStructuredSelection ssel = (IStructuredSelection) selection;

		IMenuManager newMenu = new MenuManager("&New");

		// Enable adding test cases if no selection, or current selection is a
		// test case
		if (ssel.size() == 0 || (ssel.size() == 1 && ssel.getFirstElement() instanceof XMLTestCase)) {

			createAction(newMenu, "Test Case", new Action() {
				@Override
				public void run() {
					addPressed();
				}
			});
		}

		if (ssel.size() == 1) {

			final Object object = ssel.getFirstElement();
			if (object instanceof XMLTestCase) {

				// Enable adding partner tracks/client tracks if a test case is
				// selected
				final XMLTestCase testCase = (XMLTestCase) object;

				createAction(newMenu, "Partner Track", new Action() {
					@Override
					public void run() {
						addPartnerTrack(testCase);
					}
				});
				createAction(newMenu, "WS-HT Track", new Action() {
					@Override
					public void run() {
						addHumanPartnerTrack(testCase);
					}
				});

				Action newClientTrackAction = createAction(newMenu, "Client Track", new Action() {
					@Override
					public void run() {
						addClientTrack(testCase);
					}
				});
				newClientTrackAction.setEnabled(testCase.getClientTrack() == null);
			}

			manager.add(newMenu);
			manager.add(new Separator());

			Action editAction = createAction(manager, "&Edit", new Action() {
				@Override
				public void run() {
					editPressed();
				}
			});
			editAction.setEnabled(getIsEditEnabled(object));

			manager.add(new Separator());

			Action removeAction = createAction(manager, "&Delete", new Action() {
				@Override
				public void run() {
					removePressed();
				}
			});
			removeAction.setEnabled(getIsDeleteEnabled(object));

			manager.add(new Separator());

			Action duplicateAction = createAction(manager, "D&uplicate", new Action() {
				@Override
				public void run() {
					duplicatePressed();
				}
			});
			duplicateAction.setEnabled(getIsDeleteEnabled(object));
		}
	}

	private boolean getIsMoveEnabled(Object object) {
		return (object instanceof XMLTestCase);
	}

	private boolean getIsEditEnabled(Object object) {
		return (object instanceof XMLPartnerTrack || object instanceof XMLTestCase || object instanceof XMLHumanPartnerTrack);
	}

	private boolean getIsDuplicateEnabled(Object object) {
		return (object instanceof XMLTestCase);
	}

	private boolean getIsDeleteEnabled(Object object) {
		return object != null;
	}

}
