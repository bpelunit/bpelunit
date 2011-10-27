/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import net.bpelunit.toolsupport.editors.wizards.ActivityEditMode;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import net.bpelunit.toolsupport.editors.wizards.fields.IListAdapter;
import net.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;

/**
 * Abstract superclass for wizard pages which allow adding, editing, and removing of elements.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class StructuredActivityWizardPage extends ActivityWizardPage {

	protected class ListFieldListener implements IListAdapter, IDialogFieldListener {

		public void customButtonPressed(ListDialogField field, int index) {
			switch (index) {
				case BUTTON_ADD:
					handleAddPressed();
					break;
				case BUTTON_EDIT:
					handleEditPressed();
					break;
				case BUTTON_REMOVE:
					handleRemovePressed();
					break;
			}
		}

		public void doubleClicked(ListDialogField field) {
			handleEditPressed();
		}

		public void selectionChanged(ListDialogField field) {
			enableButtonsForSelection(field, true);
		}

		public void dialogFieldChanged(DialogField field) {
		}
	}

	private final int BUTTON_ADD= 0;
	private final int BUTTON_EDIT= 1;
	private final int BUTTON_REMOVE= 2;

	protected String[] fButtons= new String[] { "&Add", "&Edit", "&Remove" };

	protected StructuredActivityWizardPage(String pageName) {
		super(pageName, ActivityEditMode.ADD);
	}

	protected abstract void handleAddPressed();

	protected abstract void handleRemovePressed();

	protected abstract void handleEditPressed();

	protected void enableButtonsForSelection(ListDialogField listField, boolean enable) {
		listField.enableButton(BUTTON_EDIT, enable);
		listField.enableButton(BUTTON_REMOVE, enable);
	}

	protected ListFieldListener createListFieldListener() {
		return new ListFieldListener();
	}
}
