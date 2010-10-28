/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.field;

import net.bpelunit.framework.client.eclipse.dialog.Field;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * A field for the {@link FieldBasedInputDialog}. The ComboField allows the user to select a value
 * in a drop-down combo box, which is pre-filled with certain values to choose from.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ComboField extends Field {

	private Combo fCombo;

	private String[] fOptions;
	private String fCurrentSelection;

	public ComboField(FieldBasedInputDialog inputDialog, String labelText, String initialValue, String[] options) {
		super(inputDialog, labelText, initialValue);
		fOptions= options;
	}

	@Override
	protected void createControl(Composite parent) {
		Label label= new Label(parent, SWT.NONE);
		label.setText(getLabelText());
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		fCombo= new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		fCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		fCombo.setItems(fOptions);
		// make sure rows are the same height on both panels.
		label.setSize(label.getSize().x, fCombo.getSize().y);

		if (getInitialValue() != null) {
			fCombo.setText(getInitialValue());
		} else if (fOptions.length > 0)
			fCombo.setText(fOptions[0]);
		else
			fCombo.setText("");

		fCurrentSelection= fCombo.getText();

		fCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fCurrentSelection= fCombo.getText();
				getDialog().validateFields();
			}
		});
	}

	@Override
	public String getSelection() {
		return fCurrentSelection;
	}
}
