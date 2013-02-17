/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A field editor for a combo box that allows the drop-down selection of one of a list of items.
 * 
 * Taken from org.eclipse.jdt.internal.debug.ui.launcher.ComboFieldEditor and extended to allow
 * external reactions to selections.
 * 
 * @version $Id$
 * @author Philip Mayer
 */
public class ComboFieldEditor extends FieldEditor {

	/**
	 * The Combo widget.
	 */
	private Combo fCombo;

	/**
	 * The value (not the name) of the currently selected item in the Combo widget.
	 */
	private String fValue;

	/**
	 * The names (labels) and underlying values to populate the combo widget. These should be
	 * arranged as: { {name1, value1}, {name2, value2}, ...}
	 */
	private String[][] fEntryNamesAndValues;

	public ComboFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
		init(name, labelText);
		fEntryNamesAndValues= entryNamesAndValues;
		createControl(parent);
	}

	/**
	 * @see FieldEditor#adjustForNumColumns(int)
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
		if (numColumns > 1) {
			Control control= getLabelControl();
			int left= numColumns;
			if (control != null) {
				((GridData) control.getLayoutData()).horizontalSpan= 1;
				left= left - 1;
			}
			((GridData) fCombo.getLayoutData()).horizontalSpan= left;
		} else {
			Control control= getLabelControl();
			if (control != null) {
				((GridData) control.getLayoutData()).horizontalSpan= 1;
			}
			((GridData) fCombo.getLayoutData()).horizontalSpan= 1;
		}
	}

	/**
	 * @see FieldEditor#doFillIntoGrid(Composite, int)
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		int comboC= 1;
		if (numColumns > 1) {
			comboC= numColumns - 1;
		}
		Control control= getLabelControl(parent);
		GridData gd= new GridData();
		gd.horizontalSpan= 1;
		control.setLayoutData(gd);
		control= getComboBoxControl(parent);
		gd= new GridData();
		gd.horizontalSpan= comboC;
		gd.horizontalAlignment= GridData.FILL;
		control.setLayoutData(gd);
		control.setFont(parent.getFont());
	}

	/**
	 * @see FieldEditor#doLoad()
	 */
	@Override
	protected void doLoad() {
		updateComboForValue(getPreferenceStore().getString(getPreferenceName()));
	}

	/**
	 * @see FieldEditor#doLoadDefault()
	 */
	@Override
	protected void doLoadDefault() {
		updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()));
	}

	/**
	 * @see FieldEditor#doStore()
	 */
	@Override
	protected void doStore() {
		if (fValue == null) {
			getPreferenceStore().setToDefault(getPreferenceName());
			return;
		}

		getPreferenceStore().setValue(getPreferenceName(), fValue);
	}

	/**
	 * @see FieldEditor#getNumberOfControls()
	 */
	@Override
	public int getNumberOfControls() {
		return 1;
	}

	/**
	 * Lazily create and return the Combo control.
	 */
	public Combo getComboBoxControl(Composite parent) {
		if (fCombo == null) {
			fCombo= new Combo(parent, SWT.READ_ONLY);
			fCombo.setFont(parent.getFont());
			for (int i= 0; i < fEntryNamesAndValues.length; i++) {
				fCombo.add(fEntryNamesAndValues[i][0], i);
			}

			fCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					String oldValue= fValue;
					String name= fCombo.getText();
					fValue= getValueForName(name);
					setPresentsDefaultValue(false);
					fireValueChanged(VALUE, oldValue, fValue);
				}
			});
		}
		return fCombo;
	}

	/**
	 * Given the name (label) of an entry, return the corresponding value.
	 */
	protected String getValueForName(String name) {
		for (String[] entry : fEntryNamesAndValues) {
			if (name.equals(entry[0])) {
				return entry[1];
			}
		}
		return fEntryNamesAndValues[0][0];
	}

	/**
	 * Set the name in the combo widget to match the specified value.
	 */
	protected void updateComboForValue(String value) {
		fValue= value;
		for (String[] element : fEntryNamesAndValues) {
			if (value.equals(element[1])) {
				fCombo.setText(element[0]);
				return;
			}
		}
		if (fEntryNamesAndValues.length > 0) {
			fValue= fEntryNamesAndValues[0][1];
		}
	}

	public String getCurrentSelectionString() {
		return fValue;
	}
}
