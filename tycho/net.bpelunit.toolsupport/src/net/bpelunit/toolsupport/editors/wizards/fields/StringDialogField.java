/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * Dialog field containing a label and a text control.
 */
public class StringDialogField extends DialogField {

	private String fText;
	private Text fTextControl;
	private ModifyListener fModifyListener;
	private boolean fSkipLabel;

	public StringDialogField() {
		super();
		fText= ""; //$NON-NLS-1$
	}

	public StringDialogField(boolean skipLabel) {
		super();
		fText= ""; //$NON-NLS-1$
		fSkipLabel= skipLabel;
	}

	// ------- layout helpers

	/*
	 * @see DialogField#doFillIntoGrid
	 */
	@Override
	public Control[] doFillIntoGrid(Composite parent, int nColumns) {
		assertEnoughColumns(nColumns);

		Label label= null;
		if (!fSkipLabel) {
			label= getLabelControl(parent);
			label.setLayoutData(gridDataForLabel(1));
		}
		Text text= getTextControl(parent);
		text.setLayoutData(gridDataForText(nColumns - (fSkipLabel ? 0 : 1)));

		return fSkipLabel ? new Control[] { text } : new Control[] { label, text };
	}

	/*
	 * @see DialogField#getNumberOfControls
	 */
	@Override
	public int getNumberOfControls() {
		return fSkipLabel ? 1 : 2;
	}

	protected static GridData gridDataForText(int span) {
		GridData gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= span;
		return gd;
	}

	// ------- focus methods

	/*
	 * @see DialogField#setFocus
	 */
	@Override
	public boolean setFocus() {
		if (isOkToUse(fTextControl)) {
			fTextControl.setFocus();
			fTextControl.setSelection(0, fTextControl.getText().length());
		}
		return true;
	}

	// ------- ui creation

	/**
	 * Creates or returns the created text control.
	 * 
	 * @param parent The parent composite or <code>null</code> when the widget has already been
	 *            created.
	 */
	public Text getTextControl(Composite parent) {
		if (fTextControl == null) {
			assertCompositeNotNull(parent);
			fModifyListener= new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					doModifyText(e);
				}
			};

			fTextControl= new Text(parent, SWT.SINGLE | SWT.BORDER);
			// moved up due to 1GEUNW2
			fTextControl.setText(fText);
			fTextControl.setFont(parent.getFont());
			fTextControl.addModifyListener(fModifyListener);

			fTextControl.setEnabled(isEnabled());
		}
		return fTextControl;
	}

	private void doModifyText(ModifyEvent e) {
		if (isOkToUse(fTextControl)) {
			fText= fTextControl.getText();
		}
		dialogFieldChanged();
	}

	// ------ enable / disable management

	/*
	 * @see DialogField#updateEnableState
	 */
	@Override
	protected void updateEnableState() {
		super.updateEnableState();
		if (isOkToUse(fTextControl)) {
			fTextControl.setEnabled(isEnabled());
		}
	}

	// ------ text access

	/**
	 * Gets the text. Can not be <code>null</code>
	 */
	public String getText() {
		return fText;
	}

	/**
	 * Sets the text. Triggers a dialog-changed event.
	 */
	public void setText(String text) {
		fText= text;
		if (isOkToUse(fTextControl)) {
			fTextControl.setText(text);
		} else {
			dialogFieldChanged();
		}
	}

	/**
	 * Sets the text without triggering a dialog-changed event.
	 */
	public void setTextWithoutUpdate(String text) {
		fText= text;
		if (isOkToUse(fTextControl)) {
			fTextControl.removeModifyListener(fModifyListener);
			fTextControl.setText(text);
			fTextControl.addModifyListener(fModifyListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		if (isOkToUse(fTextControl)) {
			setTextWithoutUpdate(fText);
		}
	}

}
