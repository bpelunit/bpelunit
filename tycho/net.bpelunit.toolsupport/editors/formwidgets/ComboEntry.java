/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.formwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Create a combo box used for selecting values. Fires textDirty() on every change to the selection.
 * 
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ComboEntry extends TextEntry {

	private Combo text;
	private String[][] fItems;

	public ComboEntry(Composite parent, FormToolkit toolkit, String labelText) {
		super(parent, toolkit, labelText, SWT.SINGLE);
	}

	@Override
	protected Control createEntry(Composite parent, FormToolkit toolkit, int style) {

		text= new Combo(parent, toolkit.getBorderStyle() | SWT.READ_ONLY | SWT.DROP_DOWN | toolkit.getOrientation());
		toolkit.adapt(text);
		return text;
	}

	/**
	 * Sets the items for selection. This is a two-dimensional array; [i][0] holds the
	 * human-readable name for displaying purpose, and [i][1] holds the ID of the option (which is
	 * returned by getValue())
	 * 
	 * @param items
	 */
	public void setItems(String[][] items) {
		fItems= items;
		String[] texts= new String[items.length];
		for (int i= 0; i < items.length; i++) {
			texts[i]= fItems[i][0];
		}
		text.setItems(texts);
	}

	/**
	 * Returns the id of the selected value, or null if nothing was selected.
	 */
	@Override
	public String getValue() {
		String selected= super.getValue();
		for (String[] element : fItems) {
			if (selected.equals(element[0].trim()))
				return element[1];
		}
		return null;
	}

	/**
	 * Sets the value of this entry (id).
	 * 
	 * @param id
	 */
	@Override
	public void setValue(String id) {

		String toSet= id;
		if (toSet != null)
			for (String[] element : fItems) {
				if (id.equals(element[1]))
					toSet= element[0];
			}
		super.setValue(toSet);
	}

	public void addFocusListener(FocusListener listener) {
		text.addFocusListener(listener);
	}
}
