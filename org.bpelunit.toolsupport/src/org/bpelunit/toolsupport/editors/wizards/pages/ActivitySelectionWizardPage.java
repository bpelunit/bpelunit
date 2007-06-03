/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import org.bpelunit.framework.control.util.ActivityUtil;
import org.bpelunit.framework.control.util.ActivityUtil.ActivityConstant;
import org.bpelunit.toolsupport.ToolSupportActivator;
import org.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import org.bpelunit.toolsupport.editors.wizards.fields.IListAdapter;
import org.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for selecting an activity.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ActivitySelectionWizardPage extends ActivityWizardPage {

	private class CopyListener implements IListAdapter, IDialogFieldListener {

		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void doubleClicked(ListDialogField field) {
		}

		public void selectionChanged(ListDialogField field) {
			if (field.getSelectedElements().size() == 1) {
				setPageComplete(true);
				fCurrentSelection= (ActivityConstant) field.getSelectedElements().get(0);
			} else {
				setPageComplete(false);
				fCurrentSelection= null;
			}
		}

		public void dialogFieldChanged(DialogField field) {
		}
	}

	private static class CopyListLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			return ((ActivityConstant) element).getNiceName();
		}

		@Override
		public Image getImage(Object element) {
			return ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_ACTIVITY);
		}
	}

	private ListDialogField fSelectionField;
	private ActivityConstant fCurrentSelection;

	public ActivitySelectionWizardPage(String pageName) {
		super(pageName, ActivityEditMode.ADD);
		setTitle("Add a new activity");
		setDescription("Select an activity");
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		CopyListener copyListener= new CopyListener();
		String[] addButtons= new String[] {};
		fSelectionField= new ListDialogField(copyListener, addButtons, new CopyListLabelProvider());
		fSelectionField.setDialogFieldListener(copyListener);
		fSelectionField.setTableColumns(new ListDialogField.ColumnsDescription(1, false));
		fSelectionField.setLabelText(null);

		fSelectionField.setElements(ActivityUtil.getTopLevelActivities());

		fSelectionField.doFillIntoGrid(composite, nColumns);

		final TableViewer tableViewer= fSelectionField.getTableViewer();
		tableViewer.setColumnProperties(new String[] { "Activity" });

		GridData gd= (GridData) fSelectionField.getListControl(null).getLayoutData();

		gd.grabExcessVerticalSpace= true;
		gd.grabExcessHorizontalSpace= true;

		fCurrentSelection= null;
		setPageComplete(false);
	}

	public ActivityConstant getSelectedActivity() {
		return fCurrentSelection;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.SEND;
	}

}
