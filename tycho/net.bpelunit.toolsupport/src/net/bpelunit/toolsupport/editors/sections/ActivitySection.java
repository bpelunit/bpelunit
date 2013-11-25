/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.xml.namespace.QName;

import net.bpelunit.framework.client.eclipse.dialog.DialogFieldValidator;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.ListField;
import net.bpelunit.framework.control.util.ActivityUtil;
import net.bpelunit.framework.control.util.ActivityUtil.ActivityConstant;
import net.bpelunit.framework.xml.suite.XMLActivity;
import net.bpelunit.framework.xml.suite.XMLCompleteHumanTaskActivity;
import net.bpelunit.framework.xml.suite.XMLCondition;
import net.bpelunit.framework.xml.suite.XMLHeaderProcessor;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLMapping;
import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLSendOnlyActivity;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.framework.xml.suite.XMLWaitActivity;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.TestSuitePage;
import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.ActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.CompleteHumanTaskActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.ReceiveOnlyWizard;
import net.bpelunit.toolsupport.editors.wizards.ReceiveSendAsyncActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.ReceiveSendSyncActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.SendOnlyWizard;
import net.bpelunit.toolsupport.editors.wizards.SendReceiveAsyncActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.SendReceiveSyncActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.WaitActivityWizard;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.util.WSDLReadingException;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The ActivitySection allows the user to add, edit, remove, and move activites
 * of a selected partner track.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ActivitySection extends TreeSection {

	final static class ActivityLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_ACTIVITY);
		}

		public String getText(Object element) {
			return ActivityUtil.getUIName(element);
		}

		public void addListener(ILabelProviderListener listener) {
			// noop
		}

		public void dispose() {
			// noop
		}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {
			// noop
		}
	}

	private class ActivityContentProvider implements ITreeContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof XMLTrack) {
				return ActivityUtil.getActivities((XMLTrack) inputElement).toArray();
			} else if(inputElement instanceof XMLHumanPartnerTrack) {
				return ((XMLHumanPartnerTrack) inputElement).getCompleteHumanTaskList().toArray();
			} else {
				return new Object[0];
			}
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof XMLActivity) {
				XMLActivity activity = (XMLActivity) parentElement;

				if (activity instanceof XMLReceiveActivity) {
					XMLReceiveActivity rcvOp = (XMLReceiveActivity) activity;
					return rcvOp.getConditionList().toArray();
				}
				if (ActivityUtil.isTwoWayActivity(activity)) {
					XMLTwoWayActivity twoWayActivity = (XMLTwoWayActivity) activity;
					List<XmlObject> moreActivities = new ArrayList<XmlObject>();

					if (ActivityUtil.isReceiveFirstActivity(activity)) {
						moreActivities.add(twoWayActivity.getReceive());
						moreActivities.add(twoWayActivity.getSend());
					} else {
						moreActivities.add(twoWayActivity.getSend());
						moreActivities.add(twoWayActivity.getReceive());
					}
					if (twoWayActivity.getMapping() != null)
						moreActivities.add(twoWayActivity.getMapping());
					if (twoWayActivity.getHeaderProcessor() != null)
						moreActivities.add(twoWayActivity.getHeaderProcessor());
					return moreActivities.toArray();
				}
				if(activity instanceof XMLSendOnlyActivity) {
					XMLSendOnlyActivity xmlSendOnlyActivity = (XMLSendOnlyActivity)activity;
					if(xmlSendOnlyActivity.getHeaderProcessor() != null) {
						return new Object[] { xmlSendOnlyActivity.getHeaderProcessor() };
					} else {
						return new Object[0];
					}
				}
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			// TODO why no parent?
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof XMLTwoWayActivity)
				return true;

			if (element instanceof XMLReceiveActivity)
				return ((XMLReceiveActivity) element).getConditionList().size() > 0;

			if(element instanceof XMLSendOnlyActivity) {
				return ((XMLSendOnlyActivity)element).getHeaderProcessor() != null;
			}
				
			return false;
		}

	}

	private XMLTrack fCurrentPartnerTrack;
	private boolean fCtrlDown;
	private XMLHumanPartnerTrack fCurrentHumanPartnerTrack;

	public ActivitySection(Composite parent, TestSuitePage page, FormToolkit toolkit) {
		super(parent, toolkit, page, true);
		init();
		fCtrlDown = false;
	}

	private void init() {
		getViewer().setLabelProvider(new ActivityLabelProvider());
		getViewer().setContentProvider(new ActivityContentProvider());
	}

	@Override
	protected String getDescription() {
		return "Manage the activities of the selected partner track.";
	}

	@Override
	protected String getName() {
		return "Activities";
	}

	@Override
	protected void editPressed() {
		Object viewerSelection = getViewerSelection();

		XMLActivity currentActivity = null;
		WizardPageCode code = null;

		if (viewerSelection instanceof XMLMapping) {
			currentActivity = ActivityUtil.getParentActivityFor(viewerSelection);
			code = WizardPageCode.DATACOPY;
		}
		if (viewerSelection instanceof XMLCondition) {
			currentActivity = ActivityUtil.getParentActivityFor(viewerSelection);
			code = WizardPageCode.RECEIVE;
		}
		if (viewerSelection instanceof XMLHeaderProcessor) {
			currentActivity = ActivityUtil.getParentActivityFor(viewerSelection);
			code = WizardPageCode.HEADERPROCESSOR;
		}
		if (ActivityUtil.isActivity(viewerSelection)) {
			currentActivity = (XMLActivity) viewerSelection;
		}

		/*
		 * Check for child activities (result of either user selecting such an
		 * activity, or a child element of a child activity).
		 */
		if (ActivityUtil.isChildActivity(currentActivity)) {
			// User selected a child activity. Get parent activity
			currentActivity = ActivityUtil.getParentActivityFor(currentActivity);
			if (code == null) {
				if (ActivityUtil.isActivity(viewerSelection, ActivityConstant.SEND))
					code = WizardPageCode.SEND;
				if (ActivityUtil.isActivity(viewerSelection, ActivityConstant.RECEIVE))
					code = WizardPageCode.RECEIVE;
			}
		}

		if (currentActivity != null) {
			ActivityConstant activityConstant = ActivityUtil.getActivityConstant(currentActivity);
			/*
			 * The wizard pages set their data directly in the XML activities,
			 * it is therefore necessary to copy the activity beforehand, in
			 * case the user cancels the wizard. Note that the wizards need to
			 * operate on the original activity, as they are dependent on
			 * navigating the tree structure (for example, for finding the
			 * parent track of an activity).
			 */
			int currentActivityIndex = getActivityIndex(currentActivity);

			// Clone:
			XMLActivity copiedActivity = (XMLActivity) currentActivity.copy();
			Wizard wizard = null;
			switch (activityConstant) {
			case SEND_ONLY:
				wizard = new SendOnlyWizard(getPage(), ActivityEditMode.EDIT,
						(XMLSendOnlyActivity) currentActivity);
				break;
			case RECEIVE_ONLY:
				wizard = new ReceiveOnlyWizard(getPage(), ActivityEditMode.EDIT,
						(XMLReceiveActivity) currentActivity);
				break;
			case SEND_RECEIVE_SYNC:
				wizard = new SendReceiveSyncActivityWizard(getPage(), ActivityEditMode.EDIT,
						(XMLTwoWayActivity) currentActivity);
				break;
			case RECEIVE_SEND_SYNC:
				wizard = new ReceiveSendSyncActivityWizard(getPage(), ActivityEditMode.EDIT,
						(XMLTwoWayActivity) currentActivity);
				break;
			case SEND_RECEIVE_ASYNC:
				wizard = new SendReceiveAsyncActivityWizard(getPage(), ActivityEditMode.EDIT,
						(XMLTwoWayActivity) currentActivity);
				break;
			case RECEIVE_SEND_ASYNC:
				wizard = new ReceiveSendAsyncActivityWizard(getPage(), ActivityEditMode.EDIT,
						(XMLTwoWayActivity) currentActivity);
				break;
			case WAIT:
				wizard = new WaitActivityWizard((XMLWaitActivity) currentActivity);
				break;
			case COMPLETEHUMANTASK:
				wizard = new CompleteHumanTaskActivityWizard((XMLCompleteHumanTaskActivity)currentActivity);
				break;
			default:
				ToolSupportActivator.logErrorMessage("Unknown activity type " + activityConstant);
			}
			if (wizard != null) {
				if (code != null)
					((ActivityWizard) wizard).setStart(code);
				if (openWizard(wizard)) {
					adjust(true);
				} else {
					replaceActivity(fCurrentPartnerTrack, currentActivityIndex, activityConstant,
							copiedActivity);
					// Refresh viewer without marking stale
					// Nodes have been replaced; the viewer still has the old
					// ones in cache.
					getViewer().refresh();
					getTreeViewer().expandAll();
				}

			}
		}
	}

	private int getActivityIndex(XMLActivity currentActivity) {
		if(fCurrentPartnerTrack != null) {
			return getActivityIndex(fCurrentPartnerTrack, currentActivity);
		} else if (fCurrentHumanPartnerTrack != null) {
			return getActivityIndex(fCurrentHumanPartnerTrack, currentActivity);
		}
		return -1;
	}

	private int getActivityIndex(XMLHumanPartnerTrack humanPartnerTrack,
			XMLActivity currentActivity) {
		return humanPartnerTrack.getCompleteHumanTaskList().indexOf(currentActivity);
	}

	@Override
	protected void removePressed() {

		Object viewerSelection = getViewerSelection();
		if (ActivityUtil.isActivity(viewerSelection)) {
			if(fCurrentPartnerTrack != null) {
				removeActivity(fCurrentPartnerTrack, viewerSelection);
			} else {
				removeActivity(fCurrentHumanPartnerTrack, viewerSelection);
			}
		} // endif XMLActivity
		else if (viewerSelection instanceof XMLMapping) {
			XMLActivity activity = ActivityUtil.getParentActivityFor(viewerSelection);
			if (ActivityUtil.isTwoWayActivity(activity)) {
				XMLTwoWayActivity op = (XMLTwoWayActivity) activity;
				op.unsetMapping();
			}
		} // endif XMLMapping
		else if (viewerSelection instanceof XMLHeaderProcessor) {
			XMLActivity activity = ActivityUtil.getParentActivityFor(viewerSelection);
			if (ActivityUtil.isTwoWayActivity(activity)) {
				XMLTwoWayActivity op = (XMLTwoWayActivity) activity;
				op.unsetHeaderProcessor();
			}
		} // endif XMLHeaderProcessor
		else if (viewerSelection instanceof XMLCondition) {
			XMLCondition cond = (XMLCondition) viewerSelection;
			XmlCursor cursor = cond.newCursor();
			try {
			if (cursor.toParent()) {
				XmlObject parent = cursor.getObject();
				if (parent instanceof XMLReceiveActivity) {
					XMLReceiveActivity rcvOp = (XMLReceiveActivity) parent;
					int index = rcvOp.getConditionList().indexOf(viewerSelection);
					if (index != -1)
						rcvOp.removeCondition(index);
				}
			} 
			} finally { 
				cursor.dispose();
			}
		} // endif XMLCondition
		adjust(false);
	}

	private void removeActivity(XMLHumanPartnerTrack currentPartnerTrack,
			Object activityToRemove) {
		int index = currentPartnerTrack.getCompleteHumanTaskList().indexOf(activityToRemove);
		
		if(index >= 0) {
			ToolSupportActivator.log(new Status(
					IStatus.INFO, ToolSupportActivator.PLUGIN_ID,
					"Removing CompleteHumanTask[" + index + "]"));
			currentPartnerTrack.removeCompleteHumanTask(index);
		}
	}

	@Override
	protected void upPressed() {
		Object viewerSelection = getViewerSelection();
		if (ActivityUtil.isActivity(viewerSelection)) {
			// move the current activity to the one before
			XMLActivity activity = (XMLActivity) viewerSelection;
			XmlCursor currentCursor = activity.newCursor();
			// Does this activity even have a previous sibling?
			if (currentCursor.toPrevSibling()) {
				// move from the current activity to the position of the
				// previous sibling, i.e. one up.
				activity.newCursor().moveXml(currentCursor);
				adjust(true);
			}
			currentCursor.dispose();
		}
	}

	@Override
	protected void downPressed() {
		Object viewerSelection = getViewerSelection();
		if (ActivityUtil.isActivity(viewerSelection)) {
			// move the current activity to the one before
			XMLActivity activity = (XMLActivity) viewerSelection;
			XmlCursor currentCursor = activity.newCursor();
			// Does this activity even have a next sibling?
			if (currentCursor.toNextSibling()) {
				// Yes, it does -> move that sibling up to the current activity
				// position
				currentCursor.moveXml(activity.newCursor());
				adjust(true);
			}
			currentCursor.dispose();
		}
	}

	private void removeActivity(XMLTrack track, Object activity) {
		int index = getActivityIndex(track, activity);
		if (index != -1) {
			final ActivityConstant activityType = ActivityUtil.getActivityConstant(activity);
			switch (activityType) {
			case SEND_ONLY:
				fCurrentPartnerTrack.removeSendOnly(index);
				break;
			case RECEIVE_ONLY:
				fCurrentPartnerTrack.removeReceiveOnly(index);
				break;
			case SEND_RECEIVE_SYNC:
				fCurrentPartnerTrack.removeSendReceive(index);
				break;
			case RECEIVE_SEND_SYNC:
				fCurrentPartnerTrack.removeReceiveSend(index);
				break;
			case SEND_RECEIVE_ASYNC:
				fCurrentPartnerTrack.removeSendReceiveAsynchronous(index);
				break;
			case RECEIVE_SEND_ASYNC:
				fCurrentPartnerTrack.removeReceiveSendAsynchronous(index);
				break;
			case WAIT:
				ToolSupportActivator.log(new Status(
					IStatus.INFO, ToolSupportActivator.PLUGIN_ID,
					"Removing WAIT[" + index + "]"));
				fCurrentPartnerTrack.removeWait(index);
				break;
			default:
				ToolSupportActivator.logErrorMessage("Unknown activity type " + activityType);
			}
		}
	}

	private void replaceActivity(XMLTrack track, int index, ActivityConstant type,
			XMLActivity theReplacement) {
		if (index != -1) {
			switch (type) {
			case SEND_ONLY:
				fCurrentPartnerTrack.setSendOnlyArray(index, (XMLSendOnlyActivity) theReplacement);
				break;
			case RECEIVE_ONLY:
				fCurrentPartnerTrack
						.setReceiveOnlyArray(index, (XMLReceiveActivity) theReplacement);
				break;
			case SEND_RECEIVE_SYNC:
				fCurrentPartnerTrack.setSendReceiveArray(index, (XMLTwoWayActivity) theReplacement);
				break;
			case RECEIVE_SEND_SYNC:
				fCurrentPartnerTrack.setReceiveSendArray(index, (XMLTwoWayActivity) theReplacement);
				break;
			case SEND_RECEIVE_ASYNC:
				fCurrentPartnerTrack.setSendReceiveAsynchronousArray(index,
						(XMLTwoWayActivity) theReplacement);
				break;
			case RECEIVE_SEND_ASYNC:
				fCurrentPartnerTrack.setReceiveSendAsynchronousArray(index,
						(XMLTwoWayActivity) theReplacement);
				break;
			default:
				ToolSupportActivator.logErrorMessage("Unknown activity type " + type);
			}
		}
	}

	private int getActivityIndex(XMLTrack track, Object activity) {
		List<? extends Object> activities = null;
		switch (ActivityUtil.getActivityConstant(activity)) {
		case SEND_ONLY:
			activities = track.getSendOnlyList();
			break;
		case RECEIVE_ONLY:
			activities = fCurrentPartnerTrack.getReceiveOnlyList();
			break;
		case SEND_RECEIVE_SYNC:
			activities = fCurrentPartnerTrack.getSendReceiveList();
			break;
		case RECEIVE_SEND_SYNC:
			activities = fCurrentPartnerTrack.getReceiveSendList();
			break;
		case SEND_RECEIVE_ASYNC:
			activities = fCurrentPartnerTrack.getSendReceiveAsynchronousList();
			break;
		case RECEIVE_SEND_ASYNC:
			activities = fCurrentPartnerTrack.getReceiveSendAsynchronousList();
			break;
		case WAIT:
			activities = fCurrentPartnerTrack.getWaitList();
			break;
		default:
			return -1;
		}

		return activities.indexOf(activity);
	}

	@Override
	protected void addPressed() {

		FieldBasedInputDialog dialog = new FieldBasedInputDialog(getShell(),
				"Create a new activity");

		if(fCurrentPartnerTrack != null) {
		List<ActivityConstant> topLevelActivities = ActivityUtil.getTopLevelSoapActivities();
		String[] names = new String[topLevelActivities.size()];
		int i = 0;
		for (ActivityConstant constant : topLevelActivities) {
			names[i] = constant.getNiceName();
			i++;
		}
		ListField nameField = new ListField(dialog, "Activity", null, names);
		nameField.setValidator(new DialogFieldValidator() {

			@Override
			public String validate(String value) {
				if ("".equals(value))
					return "Select an activity.";
				return null;
			}

		});
		nameField.setLabelProvider(new ILabelProvider() {

			public Image getImage(Object element) {
				return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_ACTIVITY);
			}

			public String getText(Object element) {
				return (String) element;
			}

			public void addListener(ILabelProviderListener listener) {

			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return true;
			}

			public void removeListener(ILabelProviderListener listener) {
			}

		});
		dialog.addField(nameField);

		if (dialog.open() != Window.OK) {
			return;
		}

		String name = nameField.getSelection();

		if (name != null)
			addActivity(ActivityConstant.getForNiceName(name));
		} else {
			addActivity(ActivityConstant.COMPLETEHUMANTASK);
		}
	}

	public void handleTrackSelection(XMLTrack selection) {
		fCurrentHumanPartnerTrack = null;
		fCurrentPartnerTrack = selection;
		setEnabled(BUTTON_ADD, true);
		setEnabled(BUTTON_EDIT, false);
		setEnabled(BUTTON_REMOVE, false);
		setEnabled(BUTTON_UP, false);
		setEnabled(BUTTON_DOWN, false);
		getViewer().setInput(selection);
		getTreeViewer().expandToLevel(AbstractTreeViewer.ALL_LEVELS);
	}
	
	public void handleTrackSelection(XMLHumanPartnerTrack selection) {
		fCurrentPartnerTrack = null;
		fCurrentHumanPartnerTrack = selection;
		setEnabled(BUTTON_ADD, true);
		setEnabled(BUTTON_EDIT, false);
		setEnabled(BUTTON_REMOVE, false);
		setEnabled(BUTTON_UP, false);
		setEnabled(BUTTON_DOWN, false);
		getViewer().setInput(selection);
		getTreeViewer().expandToLevel(AbstractTreeViewer.ALL_LEVELS);
	}

	@Override
	protected void itemSelected(Object item) {
		if (item != null) {
			setEnabled(BUTTON_ADD, true);
			setEnabled(BUTTON_EDIT, true);
			setEnabled(BUTTON_REMOVE, getIsRemoveEnabled(item));
			if (ActivityUtil.isActivity(item) && !ActivityUtil.isChildActivity(item)) {
				setEnabled(BUTTON_UP, ActivityUtil.hasPrevious((XMLActivity) item));
				setEnabled(BUTTON_DOWN, ActivityUtil.hasNext((XMLActivity) item));
			} else {
				setEnabled(BUTTON_UP, false);
				setEnabled(BUTTON_DOWN, false);
			}
		}
	}

	@Override
	public void refresh() {
		fCurrentPartnerTrack = null;
		getViewer().setInput(null);
		setEnabled(BUTTON_ADD, false);
		setEnabled(BUTTON_EDIT, false);
		setEnabled(BUTTON_REMOVE, false);
		setEnabled(BUTTON_UP, false);
		setEnabled(BUTTON_DOWN, false);
		super.refresh();
	}

	private void copyPressed() {

		Object viewerSelection = getViewerSelection();
		if (ActivityUtil.isActivity(viewerSelection)
				&& !ActivityUtil.isChildActivity(viewerSelection)) {
			XMLActivity activity = (XMLActivity) viewerSelection;

			Clipboard clipboard = new Clipboard(getShell().getDisplay());

			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setSaveOuter();

			Object[] o = new Object[] { activity.xmlText(options) };
			Transfer[] t = new Transfer[] { TextTransfer.getInstance() };
			clipboard.setContents(o, t);
			clipboard.dispose();
		}

	}

	private void pastePressed() {

		Clipboard clipboard = new Clipboard(getShell().getDisplay());
		TextTransfer textTransfer = TextTransfer.getInstance();
		String textData = (String) clipboard.getContents(textTransfer);
		if (textData != null) {
			// Try and parse it.
			try {
				XmlObject activity = XmlObject.Factory.parse(textData);
				activity.validate();
				XmlCursor cursor = activity.newCursor();
				if (cursor.toFirstChild()) {
					XmlObject obj = cursor.getObject();
					ActivityConstant type = ActivityUtil.getActivityConstant(obj);

					XMLActivity newOne = ActivityUtil.createNewTopLevelActivity(
							fCurrentPartnerTrack, type);
					newOne.set(obj);

					adjust(true);
				}
				cursor.dispose();

			} catch (XmlException e) {
				// simply do not paste.
				MessageDialog.openError(getShell(), "Error", "No activity in clipboard.");
			}
		}
		clipboard.dispose();
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {

		// No partner track? Nothing to add.
		if (fCurrentPartnerTrack == null)
			return;

		ISelection selection = getViewer().getSelection();
		IStructuredSelection ssel = (IStructuredSelection) selection;
		boolean hasPaste = false;

		IMenuManager newMenu = new MenuManager("&New Activity");

		// Add the new activity menu even if nothing is selected
		if ((ssel.size() == 0)
				|| ((ssel.size() == 1) && ActivityUtil.isActivity(ssel.getFirstElement()) && !ActivityUtil
						.isChildActivity(ssel.getFirstElement()))) {

			List<ActivityConstant> topLevelActivities = ActivityUtil.getTopLevelSoapActivities();
			for (final ActivityConstant constant : topLevelActivities) {
				createAction(newMenu, constant.getNiceName(), new Action() {
					@Override
					public void run() {
						addActivity(constant);
					}
				});
			}
		}

		manager.add(newMenu);

		if (ssel.size() == 1) {
			final Object object = ssel.getFirstElement();

			manager.add(new Separator());

			if (ActivityUtil.isActivity(ssel.getFirstElement())
					&& !ActivityUtil.isChildActivity(ssel.getFirstElement())) {
				Action copyAction = new Action() {
					@Override
					public void run() {
						copyPressed();
					}
				};

				copyAction.setText("&Copy");
				manager.add(copyAction);

				Action pasteAction = new Action() {
					@Override
					public void run() {
						pastePressed();
					}
				};

				pasteAction.setText("&Paste");
				manager.add(pasteAction);
				hasPaste = true;

				manager.add(new Separator());
			}

			Action editAction = new Action() {
				@Override
				public void run() {
					editPressed();
				}
			};

			editAction.setText("&Edit");
			manager.add(editAction);

			editAction.setEnabled(true);

			manager.add(new Separator());
			Action delAction = new Action() {
				@Override
				public void run() {
					removePressed();
				}
			};
			delAction.setText("&Delete");
			manager.add(delAction);
			manager.add(new Separator());

			delAction.setEnabled(getIsRemoveEnabled(object));
		}

		if (!hasPaste) {
			Action pasteAction = new Action() {
				@Override
				public void run() {
					pastePressed();
				}
			};

			pasteAction.setText("&Paste");
			manager.add(pasteAction);
		}
	}

	private boolean getIsRemoveEnabled(Object object) {
		// cannot delete subactivities
		return !ActivityUtil.isChildActivity(object);
	}

	private boolean openWizard(IWizard wizard) {
		WizardDialog dialog = new WizardDialog(getShell(), wizard);
		return (dialog.open() == Window.OK);
	}

	protected void addActivity(ActivityConstant constant) {

		XMLActivity added = null;

		switch (constant) {
		case SEND_ONLY: {
			XMLSendOnlyActivity sendOp = fCurrentPartnerTrack.addNewSendOnly();

			// Initialize the activity:
			sendOp.setService(new QName(""));
			sendOp.setPort("");
			sendOp.setOperation("");
			sendOp.addNewData();

			this.prefillDataIfOnlyOneChoiceExists(sendOp);

			// Open the wizard
			SendOnlyWizard wiz = new SendOnlyWizard(getPage(), ActivityEditMode.ADD, sendOp);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, sendOp);
			} else
				added = sendOp;
			break;
		}
		case RECEIVE_ONLY: {
			XMLReceiveActivity receiveOp = fCurrentPartnerTrack.addNewReceiveOnly();
			// Initialize the activity:
			receiveOp.setService(new QName(""));
			receiveOp.setPort("");
			receiveOp.setOperation("");
			this.prefillDataIfOnlyOneChoiceExists(receiveOp);

			// Open the wizard
			ReceiveOnlyWizard wiz = new ReceiveOnlyWizard(getPage(), ActivityEditMode.ADD,
					receiveOp);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, receiveOp);
			} else
				added = receiveOp;
			break;
		}
		case SEND_RECEIVE_SYNC: {
			XMLTwoWayActivity sendRcvOp = fCurrentPartnerTrack.addNewSendReceive();
			initializeTwoWay(sendRcvOp);

			// Open the wizard
			SendReceiveSyncActivityWizard wiz = new SendReceiveSyncActivityWizard(getPage(),
					ActivityEditMode.ADD, sendRcvOp);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, sendRcvOp);
			} else
				added = sendRcvOp;
			break;
		}
		case RECEIVE_SEND_SYNC: {
			XMLTwoWayActivity rcvSendOp = fCurrentPartnerTrack.addNewReceiveSend();
			initializeTwoWay(rcvSendOp);

			// Open the wizard
			ReceiveSendSyncActivityWizard wiz = new ReceiveSendSyncActivityWizard(getPage(),
					ActivityEditMode.ADD, rcvSendOp);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, rcvSendOp);
			} else
				added = rcvSendOp;
			break;
		}
		case SEND_RECEIVE_ASYNC: {
			XMLTwoWayActivity sendRcvOp = fCurrentPartnerTrack.addNewSendReceiveAsynchronous();
			initializeTwoWay(sendRcvOp);

			// Open the wizard
			SendReceiveAsyncActivityWizard wiz = new SendReceiveAsyncActivityWizard(getPage(),
					ActivityEditMode.ADD, sendRcvOp);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, sendRcvOp);
			} else
				added = sendRcvOp;
			break;
		}
		case RECEIVE_SEND_ASYNC: {
			XMLTwoWayActivity sendRcvOp = fCurrentPartnerTrack.addNewReceiveSendAsynchronous();
			initializeTwoWay(sendRcvOp);

			// Open the wizard
			ReceiveSendAsyncActivityWizard wiz = new ReceiveSendAsyncActivityWizard(getPage(),
					ActivityEditMode.ADD, sendRcvOp);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, sendRcvOp);
			} else
				added = sendRcvOp;
			break;
		}
		case WAIT: {
			XMLWaitActivity waitAct = fCurrentPartnerTrack.addNewWait();
			initializeWait(waitAct);
			WaitActivityWizard wiz = new WaitActivityWizard(waitAct);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, waitAct);
			} else {
				added = waitAct;
			}
			break;
		}
		case COMPLETEHUMANTASK: {
			XMLCompleteHumanTaskActivity completeHumanTaskActivity = fCurrentHumanPartnerTrack.addNewCompleteHumanTask();
			CompleteHumanTaskActivityWizard wiz = new CompleteHumanTaskActivityWizard(completeHumanTaskActivity);
			if (!openWizard(wiz)) {
				removeActivity(fCurrentPartnerTrack, completeHumanTaskActivity);
			} else {
				added = completeHumanTaskActivity;
			}
			break;
		}
		default:
			ToolSupportActivator.logErrorMessage("Unknown activity constant " + constant);
		}
		if (added != null) {
			adjust(false);
			getTreeViewer().expandToLevel(added, AbstractTreeViewer.ALL_LEVELS);
			getTreeViewer().setSelection(new StructuredSelection(added));
		}
	}

	private void initializeWait(XMLWaitActivity waitAct) {
		waitAct.setWaitForMilliseconds(1000);
	}

	/**
	 * Functionality for choosing a service etc if only one service is available
	 * in the WSDL -> saves much effort
	 * 
	 * Because this WSDL4J version does not use generics, we suppress warnings
	 * here
	 */
	@SuppressWarnings("unchecked")
	private void prefillDataIfOnlyOneChoiceExists(XMLSoapActivity operation) {
		XMLTrack track = ActivityUtil.getEnclosingTrack(operation);
		if (track == null) {
			return;
		}

		try {
			Definition wsdlForPartner = getEditor().getWsdlForPartner(track);
			if (wsdlForPartner == null) {
				return;
			}

			Map<?, Service> services = wsdlForPartner.getServices();
			if (services.size() == 1) {
				Service service = services.values().iterator().next();
				operation.setService(service.getQName());

				Map<?,Port> ports = service.getPorts();
				if (ports.size() == 1) {
					Port port = ports.values().iterator().next();
					operation.setPort(port.getName());

					List<Operation> operations = port.getBinding().getPortType().getOperations();
					if (operations.size() == 1) {
						operation.setOperation(operations.get(0).getName());
					}
				}

			}
		} catch (WSDLReadingException e) {
			// if we cannot determine a service, we leave it as is
		}
	}

	private void initializeTwoWay(XMLTwoWayActivity sendRcvOp) {
		if (sendRcvOp == null) {
			return;
		}

		sendRcvOp.setService(new QName(""));
		sendRcvOp.setPort("");
		sendRcvOp.setOperation("");

		// Initialize the activity:
		XMLSendActivity sendOp = sendRcvOp.addNewSend();
		sendOp.addNewData();
		XMLReceiveActivity recvOp = sendRcvOp.addNewReceive();

		// a bit ugly but we have to fill in for every block
		// because depending on the type of the operation,
		// one or the other is read
		this.prefillDataIfOnlyOneChoiceExists(sendRcvOp);
		this.prefillDataIfOnlyOneChoiceExists(sendOp);
		this.prefillDataIfOnlyOneChoiceExists(recvOp);
	}

	@Override
	protected void handleKeyPressed(KeyEvent event) {
		super.handleKeyPressed(event);

		if (event.keyCode == SWT.CTRL)
			fCtrlDown = true;

		if (event.keyCode == 'c' && fCtrlDown)
			copyPressed();

		if (event.keyCode == 'v' && fCtrlDown)
			pastePressed();
	}

	@Override
	protected void handleKeyReleased(KeyEvent event) {
		super.handleKeyReleased(event);
		if (event.keyCode == SWT.CTRL)
			fCtrlDown = false;
	}

	private void adjust(boolean expand) {
		// Don't call this.refresh(); changes dirty/stale states
		getViewer().refresh();
		if (expand)
			getTreeViewer().expandAll();
		markDirty();
	}
}
