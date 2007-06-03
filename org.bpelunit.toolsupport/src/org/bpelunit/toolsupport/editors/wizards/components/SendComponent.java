/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlOptions;
import org.bpelunit.framework.xml.suite.XMLAnyElement;
import org.bpelunit.framework.xml.suite.XMLSendActivity;
import org.bpelunit.toolsupport.editors.formwidgets.HyperlinkField;
import org.bpelunit.toolsupport.editors.formwidgets.HyperlinkField.IHyperLinkFieldListener;
import org.bpelunit.toolsupport.editors.wizards.NamespaceWizard;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import org.bpelunit.toolsupport.editors.wizards.fields.LayoutUtil;
import org.bpelunit.toolsupport.editors.wizards.fields.SelectionButtonDialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.StringDialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.TextDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * The SendComponent allows the user to enter XML data to be sent.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendComponent extends DataComponent implements IHyperLinkFieldListener {

	private class SendListener implements IDialogFieldListener {

		public void dialogFieldChanged(DialogField field) {
			fireValueChanged(field);
		}
	}

	private class DelayTextListener implements IDialogFieldListener {

		public void dialogFieldChanged(DialogField field) {
			if (fDelaySelected)
				fireValueChanged(field);
		}
	}

	private class DelaySelectionListener implements IDialogFieldListener {

		public void dialogFieldChanged(DialogField field) {
			fDelaySelected= fDelaySelectionField.isEnabled();
			fDelayStringField.setText("");
			fireValueChanged(field);
		}
	}

	private TextDialogField fSendField;
	private XMLSendActivity fSendData;
	private StringDialogField fDelayStringField;
	private SelectionButtonDialogField fDelaySelectionField;

	private boolean fDelaySelected;

	public SendComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	public void init(XMLSendActivity sendData) {

		SendListener sendFieldListener= new SendListener();
		fSendField= new TextDialogField();
		fSendField.setDialogFieldListener(sendFieldListener);
		fSendField.setLabelText(null);
		fSendData= sendData;

		DelayTextListener delayTextListener= new DelayTextListener();
		fDelayStringField= new StringDialogField(true);
		fDelayStringField.setDialogFieldListener(delayTextListener);

		DelaySelectionListener delaySelectionListener= new DelaySelectionListener();
		fDelaySelectionField= new SelectionButtonDialogField(SWT.CHECK);
		fDelaySelectionField.attachDialogField(fDelayStringField);
		fDelaySelectionField.setLabelText("Vary send delay");
		fDelaySelectionField.setDialogFieldListener(delaySelectionListener);

		initValues();
	}

	private void initValues() {

		XMLAnyElement data= fSendData.getData();
		if (data == null)
			fSendData.addNewData();

		if (!data.newCursor().toFirstChild()) {
			// no children!
			fSendField.setText("");
		} else {
			XmlOptions opts= new XmlOptions();
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);

			Map ns= new HashMap();
			getTestSuite().newCursor().getAllNamespaces(ns);
			opts.setSaveImplicitNamespaces(ns);

			fSendField.setText(fSendData.getData().xmlText(opts));
		}

		String delaySequence= fSendData.getDelaySequence();
		if (delaySequence == null || "".equals(delaySequence)) {
			delaySequence= "";
			fDelaySelected= false;
		} else {
			fDelaySelected= true;
		}

		fDelaySelectionField.setSelection(fDelaySelected);
		fDelayStringField.setText(delaySequence);

	}

	@Override
	public Composite createControls(Composite composite, int nColumns) {

		Group group= createGroup(composite, "Data to be sent (literal XML)", nColumns, new GridData(SWT.FILL, SWT.FILL, true, true));

		fSendField.doFillIntoGrid(group, nColumns);
		Text text= fSendField.getTextControl(null);
		LayoutUtil.setHeightHint(text, Dialog.convertHeightInCharsToPixels(getFontMetrics(), 6));
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);

		Composite inner= new Composite(group, SWT.NULL);
		GridData gridData= new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		inner.setLayoutData(gridData);
		inner.setLayout(new GridLayout(3, false));

		fDelaySelectionField.doFillIntoGrid(inner, 1);
		Button selectionButton= fDelaySelectionField.getSelectionButton(null);
		LayoutUtil.setVerticalAlign(selectionButton, GridData.CENTER);

		fDelayStringField.doFillIntoGrid(inner, 2);
		Text textControl= fDelayStringField.getTextControl(null);
		LayoutUtil.setWidthHint(textControl, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(textControl);

		HyperlinkField field= new HyperlinkField("Configure Namespace Prefixes...");
		field.setHyperLinkFieldListener(this);
		field.createControl(group, nColumns, GridData.BEGINNING);

		return group;
	}

	public String getXmlText() {
		return fSendField.getText();
	}

	public String getDelaySequence() {
		return fDelayStringField.getText();
	}

	public void hyperLinkActivated() {
		WizardDialog d= new WizardDialog(getShell(), new NamespaceWizard(getTestSuite()));
		if (d.open() == Window.OK) {
			fireValueChanged(fSendField);
		}
	}
}
