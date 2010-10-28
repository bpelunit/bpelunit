/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.field;

import net.bpelunit.framework.client.eclipse.dialog.Field;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * A field for the {@link FieldBasedInputDialog}. The CheckBoxField allows the user to select a
 * boolean value, represented by a checkbox with a title describing the value.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class CheckBoxField extends Field {

	private Button fCheckBox;

	private boolean fCurrentSelection;

	public CheckBoxField(FieldBasedInputDialog inputDialog, String labelText, boolean initialValue) {
		super(inputDialog, labelText, Boolean.toString(initialValue));
	}

	@Override
	protected void createControl(Composite parent) {

		fCheckBox= new Button(parent, SWT.CHECK);
		fCheckBox.setText(getLabelText());
		GridData gridData= new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan= 2;
		fCheckBox.setLayoutData(gridData);

		if (getInitialValue() != null) {
			fCheckBox.setSelection(Boolean.parseBoolean(getInitialValue()));
		} else
			fCheckBox.setSelection(false);

		fCurrentSelection= fCheckBox.getSelection();

		fCheckBox.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				fCurrentSelection= fCheckBox.getSelection();
				getDialog().validateFields();
			}
		});

	}

	@Override
	public String getSelection() {
		return Boolean.toString(fCurrentSelection);
	}
}
