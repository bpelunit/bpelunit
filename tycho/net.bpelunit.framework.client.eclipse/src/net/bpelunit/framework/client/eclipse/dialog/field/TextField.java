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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A field for the {@link FieldBasedInputDialog}. The TextFields allows the user to enter some
 * text. No strings attached.
 * 
 * The text field allows two modes of operation: SINGLE (a one-line text field) and MULTI (a
 * multiple- line text field with vertical scrollbars).
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TextField extends Field {

	private Text fText;
	private Style fStyle;

	private String fCurrentSelection;
	private Character fEchoChar;

	public enum Style {
		SINGLE, MULTI
	};

	public TextField(FieldBasedInputDialog dialog, String labelText, String initialValue, Style style) {
		super(dialog, labelText, initialValue);
		fStyle= style;
	}

	@Override
	protected void createControl(Composite parent) {

		Label label= new Label(parent, SWT.NONE);
		label.setText(getLabelText());
		GridData labelGD= new GridData();
		labelGD.horizontalAlignment= GridData.HORIZONTAL_ALIGN_BEGINNING;
		labelGD.verticalAlignment= SWT.CENTER;
		label.setLayoutData(labelGD);

		int flags= SWT.BORDER | (fStyle == Style.SINGLE ? SWT.SINGLE : SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);

		fText= new Text(parent, flags);
		GridData gridData= new GridData();
		gridData.horizontalAlignment= SWT.FILL;
		gridData.grabExcessHorizontalSpace= true;

		gridData.widthHint= 200;
		if (fStyle == Style.MULTI) {
			gridData.verticalAlignment= SWT.FILL;
			gridData.heightHint= 50;
		}
		fText.setLayoutData(gridData);

		if(fEchoChar != null) {
			fText.setEchoChar(fEchoChar);
		}
		
		label.setSize(label.getSize().x, fText.getSize().y);

		if (getInitialValue() != null) {
			fCurrentSelection= getInitialValue();
			fText.setText(getInitialValue());
		}

		fCurrentSelection= fText.getText();

		fText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fCurrentSelection= fText.getText();
				getDialog().validateFields();
			}
		});
	}

	@Override
	public String getSelection() {
		return fCurrentSelection;
	}

	public void setText(String text) {
		this.fText.setText(text);
	}
	
	public void setEchoChar(char echoChar) {
		this.fEchoChar = echoChar;
	}
}
