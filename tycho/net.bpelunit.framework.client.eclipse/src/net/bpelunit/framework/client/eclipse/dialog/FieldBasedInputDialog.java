/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog for editing multiple, string based values.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class FieldBasedInputDialog extends Dialog {

	protected Composite fPanel;
	private String fTitle;
	private MessageBox fMessageBox;

	private List<Field> fFields = new ArrayList<Field>();

	public FieldBasedInputDialog(Shell shell, String title) {
		super(shell);
		this.fTitle = title;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	public void addField(Field field) {
		fFields.add(field);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (fTitle != null) {
			shell.setText(fTitle);
		}
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control bar = super.createButtonBar(parent);
		validateFields();
		return bar;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		fPanel = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		fPanel.setLayout(layout);
		fPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		for (Field field : fFields) {
			field.createControl(fPanel);
		}

		fMessageBox = new MessageBox(container, SWT.NONE);
		fMessageBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Dialog.applyDialogFont(container);
		return container;
	}

	@Override
	public int open() {
		applyDialogFont(fPanel);
		return super.open();
	}

	@Override
	public int convertHorizontalDLUsToPixels(int dlus) {
		return super.convertHorizontalDLUsToPixels(dlus);
	}

	public void validateFields() {
		for (Field field : fFields) {
			if (field.getValidator() != null) {
				String validation = field.getValidator().validate(
						field.getSelection());
				if (validation != null) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setErrorMessage(validation);
					return;
				}
			}
		}
		setErrorMessage(null);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
	}

	private void setErrorMessage(String msg) {
		fMessageBox.setMessage(msg);
	}

	public void doPressOK() {
		if (getButton(IDialogConstants.OK_ID).isEnabled())
			super.okPressed();
	}

	private static class MessageBox extends Composite {
		private Label fImage;
		private Text fText;

		public MessageBox(Composite parent, int style) {
			super(parent, style);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			setLayout(layout);
			fImage = new Label(this, SWT.NONE);
			fImage.setImage(BPELUnitActivator
					.getImage(BPELUnitActivator.IMAGE_INFO));
			Point size = fImage.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			GridData gd = new GridData();
			gd.verticalAlignment = SWT.TOP;
			gd.widthHint = size.x;
			gd.heightHint = size.y;
			fImage.setLayoutData(gd);
			fImage.setImage(null);

			fText = new Text(this, SWT.WRAP | SWT.READ_ONLY);
			fText.setText(" \n "); //$NON-NLS-1$
			gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.widthHint = 350;
			fText.setLayoutData(gd);
		}

		public void setMessage(String msg) {
			if (msg == null || msg.length() == 0) {
				msg = "";
			}
			fText.setText(escapeAmpersands(msg));
			if ("".equals(msg))
				fImage.setImage(null);
			else {
				fImage.setImage(BPELUnitActivator
						.getImage(BPELUnitActivator.IMAGE_ERROR));
				/*
				 * The setMessage() call can occur before the layouting of this
				 * dialog takes place. In that case, if the message is rather
				 * long, the dialog will get too large, so we need to restrain
				 * the text field. Note that if the message is empty, the text
				 * field should not be restrained, as it would take up too much
				 * space looking awkward.
				 */
				if (msg.length() > 200) {
					GridData d = (GridData) fText.getLayoutData();
					d.heightHint = 40;
				}
			}
		}

		private String escapeAmpersands(String message) {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < message.length(); i++) {
				char ch = message.charAt(i);
				if (ch == '&') {
					result.append('&');
				}
				result.append(ch);
			}
			return result.toString();
		}
	}

}
