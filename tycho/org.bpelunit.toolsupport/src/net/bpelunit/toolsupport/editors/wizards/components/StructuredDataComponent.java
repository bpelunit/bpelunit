/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import net.bpelunit.toolsupport.editors.wizards.fields.IListAdapter;
import net.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.FontMetrics;

/**
 * 
 * The abstract class StructuredDataComponent provides generic methods for use in components which
 * feature adding, removing, and editing of artefacts.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class StructuredDataComponent extends DataComponent {

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
			fireValueChanged(field);
		}
	}

	private final int BUTTON_ADD= 0;
	private final int BUTTON_EDIT= 1;
	private final int BUTTON_REMOVE= 2;

	protected static final String[] fButtons= new String[] { "&Add", "&Edit", "&Remove" };

	public StructuredDataComponent(IWizardPage page, FontMetrics metrics) {
		super(page, metrics);
	}

	protected void enableButtonsForSelection(ListDialogField field, boolean enable) {
		field.enableButton(BUTTON_EDIT, enable);
		field.enableButton(BUTTON_REMOVE, enable);
	}

	protected abstract void handleRemovePressed();

	protected abstract void handleEditPressed();

	protected abstract void handleAddPressed();

	protected ListFieldListener createListFieldListener() {
		return new ListFieldListener();
	}

}
