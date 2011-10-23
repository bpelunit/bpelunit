/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.formwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * Create a text field used for entering values. Fires textDirty() on every change to the text field
 * and textValueChanged() if the user leaves the field (hits Enter or focus lost)
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TextEntry {

	private Control label;
	private Control text;

	private String value= "";
	private boolean dirty;
	boolean ignoreModify= false;
	private IEntryListener listener;

	/**
	 * Adds a new text entry. Note that the parent <strong>must</strong> have a TableWrapLayout.
	 * 
	 * @param parent
	 * @param toolkit
	 * @param labelText
	 * @param style
	 */
	public TextEntry(Composite parent, FormToolkit toolkit, String labelText, int style) {
		createControl(parent, toolkit, labelText, style);
	}

	private void createControl(Composite parent, FormToolkit toolkit, String labelText, int style) {

		label= toolkit.createLabel(parent, labelText);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));

		text= createEntry(parent, toolkit, style);

		addListeners();
		layout(parent);
	}

	protected Control createEntry(Composite parent, FormToolkit toolkit, int style) {
		return toolkit.createText(parent, "", style);
	}

	private void layout(Composite parent) {

		Layout layout= parent.getLayout();
		if (layout instanceof TableWrapLayout) {
			TableWrapData td;
			int span= ((TableWrapLayout) layout).numColumns;
			td= new TableWrapData();
			if ( (text.getStyle() & SWT.MULTI) != 0)
				td.valign= TableWrapData.TOP;
			else
				td.valign= TableWrapData.MIDDLE;

			label.setLayoutData(td);
			int tspan= span - 1;
			td= new TableWrapData(TableWrapData.FILL);
			td.colspan= tspan;
			td.grabHorizontal= (tspan == 1);
			if ( (text.getStyle() & SWT.MULTI) != 0) {
				td.valign= TableWrapData.FILL;
				td.heightHint= 80;
			} else
				td.valign= TableWrapData.MIDDLE;

			text.setLayoutData(td);
		}
	}

	public void setFormEntryListener(IEntryListener listener) {
		this.listener= listener;
	}

	private void addListeners() {

		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				keyReleaseOccured(e);
			}
		});
		addModifyListener(text);

		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				if (dirty)
					commit();
			}
		});
	}

	private void addModifyListener(Control text2) {

		if (text2 instanceof Text)
			((Text) text).addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {

					editOccured(e);
				}
			});
		else if (text2 instanceof Combo)
			((Combo) text).addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {

					editOccured(e);
				}
			});

	}

	/**
	 * If dirty, commits the text in the widget to the value and notifies the listener. This call
	 * clears the 'dirty' flag.
	 * 
	 */
	public void commit() {

		if (dirty) {
			value= getText(text);
			// if (value.length()==0)
			// value = null;
			// notify
			if (listener != null)
				listener.textValueChanged(this);
		}
		dirty= false;
	}

	private String getText(Control text2) {

		if (text2 instanceof Text)
			return ((Text) text).getText();
		else if (text2 instanceof Combo)
			return ((Combo) text).getText();

		return ""; //$NON-NLS-1$
	}

	public void cancelEdit() {
		dirty= false;
	}

	private void editOccured(ModifyEvent e) {

		if (ignoreModify)
			return;
		dirty= true;
		if (listener != null)
			listener.textDirty(this);
	}

	/**
	 * Returns the current entry value. If the entry is dirty and was not commited, the value may be
	 * different from the text in the widget.
	 * 
	 * @return
	 */
	public String getValue() {
		return value.trim();
	}

	/**
	 * Returns true if the text has been modified.
	 * 
	 * @return
	 */
	public boolean isDirty() {

		return dirty;
	}

	private void keyReleaseOccured(KeyEvent e) {

		if (e.character == '\r') {
			// commit value
			if (dirty)
				commit();
		} else if (e.character == '\u001b') { // Escape character
			setText(value != null ? value : ""); // restore old
			dirty= false;
		}
	}

	/**
	 * @param string
	 */
	private void setText(String string) {

		if (text instanceof Text)
			((Text) text).setText(string);
		else if (text instanceof Combo)
			((Combo) text).setText(string);

	}

	/**
	 * Sets the value of this entry.
	 * 
	 * @param value
	 */
	public void setValue(String value) {

		if (text != null)
			setText(value != null ? value : ""); //$NON-NLS-1$
		this.value= (value != null) ? value : ""; //$NON-NLS-1$
	}

	/**
	 * Sets the value of this entry with the possibility to turn the notification off.
	 * 
	 * @param value
	 * @param blockNotification
	 */
	public void setValue(String value, boolean blockNotification) {

		ignoreModify= blockNotification;
		setValue(value);
		ignoreModify= false;
	}
}
