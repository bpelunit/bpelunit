/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Abstract superclass of all fields for the {@link FieldBasedInputDialog}.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class Field {

	private FieldBasedInputDialog fDialog;
	private String fLabelText;
	private String fInitialValue;

	private DialogFieldValidator fValidator;

	public Field(FieldBasedInputDialog inputDialog, String labelText, String initialValue) {
		fDialog= inputDialog;
		fLabelText= labelText;
		fInitialValue= initialValue;
	}

	protected abstract void createControl(Composite parent);

	public abstract String getSelection();

	protected FieldBasedInputDialog getDialog() {
		return fDialog;
	}

	protected String getInitialValue() {
		return fInitialValue;
	}

	protected String getLabelText() {
		return fLabelText;
	}

	protected DialogFieldValidator getValidator() {
		return fValidator;
	}

	public void setValidator(DialogFieldValidator validator) {
		fValidator= validator;
	}

	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button= new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));

		setButtonLayoutData(button);
		return button;
	}

	protected void setButtonLayoutData(Button button) {
		GridData data= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int widthHint= getDialog().convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		Point minSize= button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint= Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}

}
