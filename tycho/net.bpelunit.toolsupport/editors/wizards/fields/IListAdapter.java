/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.fields;

/**
 * Change listener used by <code>ListDialogField</code> and <code>CheckedListDialogField</code>
 */
public interface IListAdapter {

	/**
	 * A button from the button bar has been pressed.
	 */
	void customButtonPressed(ListDialogField field, int index);

	/**
	 * The selection of the list has changed.
	 */
	void selectionChanged(ListDialogField field);

	/**
	 * En entry in the list has been double clicked
	 */
	void doubleClicked(ListDialogField field);

}
