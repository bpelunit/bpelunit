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
import org.bpelunit.toolsupport.ToolSupportActivator;
import org.bpelunit.toolsupport.editors.formwidgets.HyperlinkField;
import org.bpelunit.toolsupport.editors.formwidgets.HyperlinkField.IHyperLinkFieldListener;
import org.bpelunit.toolsupport.editors.wizards.NamespaceWizard;
import org.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import org.bpelunit.toolsupport.editors.wizards.fields.LayoutUtil;
import org.bpelunit.toolsupport.editors.wizards.fields.MessageEditor;
import org.bpelunit.toolsupport.editors.wizards.fields.SelectionButtonDialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.StringDialogField;
import org.bpelunit.toolsupport.editors.wizards.fields.TextDialogField;
import org.bpelunit.toolsupport.util.schema.nodes.Element;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * The SendComponent allows the user to enter XML data to be sent.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendComponent extends DataComponent implements IHyperLinkFieldListener,
		InputElementChangeListener, StringValueListener {

	private TextDialogField fSendField;
	private XMLSendActivity fSendData;
	private StringDialogField fDelayStringField;
	private SelectionButtonDialogField fDelaySelectionField;
	private SelectionButtonDialogField enterLiteralXMLCheckBox;

	private boolean fDelaySelected;
	protected MessageEditor messageEditor;
	private TabItem literalXMLTab;
	private TabItem messageEditorTab;

	public SendComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	public void init(XMLSendActivity sendData) {

		this.fSendData = sendData;

		//
		// fSendField
		this.fSendField = new TextDialogField();
		this.fSendField.setLabelText(null);
		this.fSendField.setDialogFieldListener(new IDialogFieldListener() {

			public void dialogFieldChanged(DialogField field) {
				SendComponent.this.fireValueChanged(field);
			}
		});

		//
		// fDelayStringField
		this.fDelayStringField = new StringDialogField(true);
		this.fDelayStringField.setDialogFieldListener(new IDialogFieldListener() {

			public void dialogFieldChanged(DialogField field) {
				if (SendComponent.this.fDelaySelected) {
					SendComponent.this.fireValueChanged(field);
				}
			}
		});

		//
		// fDelaySelectionField
		this.fDelaySelectionField = new SelectionButtonDialogField(SWT.CHECK);
		this.fDelaySelectionField.attachDialogField(this.fDelayStringField);
		this.fDelaySelectionField.setLabelText("Vary send delay");
		this.fDelaySelectionField.setDialogFieldListener(new IDialogFieldListener() {

			public void dialogFieldChanged(DialogField field) {
				SendComponent.this.fDelaySelected = SendComponent.this.fDelaySelectionField
						.isEnabled();
				SendComponent.this.fDelayStringField.setText("");
				SendComponent.this.fireValueChanged(field);
			}
		});

		//
		// enterLiteralXMLCheckBox
		this.enterLiteralXMLCheckBox = new SelectionButtonDialogField(SWT.CHECK);
		this.enterLiteralXMLCheckBox.setLabelText("Enter XML literal");
		this.enterLiteralXMLCheckBox.setSelection(false);
		this.enterLiteralXMLCheckBox.setDialogFieldListener(new IDialogFieldListener() {

			@Override
			public void dialogFieldChanged(DialogField field) {
				SendComponent.this.setInputType();

			}
		});

		this.initValues();
	}

	protected void setInputType() {
		boolean selected = this.enterLiteralXMLCheckBox.isSelected();
		Image image = ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_LOCK);
		if (selected) {
			this.messageEditorTab.setImage(image);
			this.literalXMLTab.setImage(null);
		} else {
			this.messageEditorTab.setImage(null);
			this.literalXMLTab.setImage(image);
		}
		this.fSendField.getTextControl(null).setEditable(selected);
		this.messageEditor.setEditable(!selected);
	}

	private void initValues() {

		XMLAnyElement data = this.fSendData.getData();
		if (data == null) {
			this.fSendData.addNewData();
		}

		if (!data.newCursor().toFirstChild()) {
			// no children!
			this.fSendField.setText("");
		} else {
			XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);

			Map ns = new HashMap();
			this.getTestSuite().newCursor().getAllNamespaces(ns);
			opts.setSaveImplicitNamespaces(ns);

			this.fSendField.setText(this.fSendData.getData().xmlText(opts));
		}

		String delaySequence = this.fSendData.getDelaySequence();
		if (delaySequence == null || "".equals(delaySequence)) {
			delaySequence = "";
			this.fDelaySelected = false;
		} else {
			this.fDelaySelected = true;
		}

		this.fDelaySelectionField.setSelection(this.fDelaySelected);
		this.fDelayStringField.setText(delaySequence);

	}

	@Override
	public Composite createControls(Composite composite, int nColumns) {
		Group group = this.createGroup(composite, "Data to be sent", nColumns, new GridData(
				SWT.FILL, SWT.FILL, true, true));

		this.enterLiteralXMLCheckBox.doFillIntoGrid(group, 1);

		Label label = new Label(group, SWT.RIGHT);
		label.setText("Message Editor");

		Button button = new Button(group, SWT.NULL);
		button.setImage(ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_ARROW_LEFT));
		button.setToolTipText("Reset the \"XML to be sent\" with the XML from the Message Editor");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SendComponent.this.valueChanged(SendComponent.this.messageEditor.getMessageAsXML());
			}
		});

		label = new Label(group, SWT.LEFT);
		label.setText("XML to be sent");

		TabFolder tabFolder = new TabFolder(group, SWT.TOP);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = nColumns;
		tabFolder.setLayoutData(gd);

		this.messageEditor = new MessageEditor(tabFolder, SWT.NULL, this.getTestSuite());
		this.messageEditor.addStringValueListener(this);
		this.messageEditorTab = new TabItem(tabFolder, SWT.NULL);
		this.messageEditorTab.setControl(this.messageEditor);
		this.messageEditorTab.setText("Message Editor");

		this.fSendField.doFillIntoGrid(tabFolder, nColumns);
		this.literalXMLTab = new TabItem(tabFolder, SWT.NULL);
		Text text = this.fSendField.getTextControl(null);
		text.setEditable(false);
		this.literalXMLTab.setControl(text);
		this.literalXMLTab.setText("XML to be sent");

		LayoutUtil.setHeightHint(text, Dialog
				.convertHeightInCharsToPixels(this.getFontMetrics(), 6));
		LayoutUtil.setWidthHint(text, this.getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);

		Composite inner = new Composite(group, SWT.NULL);
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		inner.setLayoutData(gridData);
		inner.setLayout(new GridLayout(3, false));

		this.fDelaySelectionField.doFillIntoGrid(inner, 1);
		Button selectionButton = this.fDelaySelectionField.getSelectionButton(null);
		LayoutUtil.setVerticalAlign(selectionButton, GridData.CENTER);

		this.fDelayStringField.doFillIntoGrid(inner, 2);
		Text textControl = this.fDelayStringField.getTextControl(null);
		LayoutUtil.setWidthHint(textControl, this.getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(textControl);

		HyperlinkField field = new HyperlinkField("Configure Namespace Prefixes...");
		field.setHyperLinkFieldListener(this);
		field.createControl(group, nColumns, GridData.BEGINNING);

		this.setInputType();
		return group;
	}

	public String getXmlText() {
		return this.fSendField.getText();
	}

	public void setXmlText(String xml) {
		this.fSendField.setTextWithoutUpdate(xml);
	}

	public String getDelaySequence() {
		return this.fDelayStringField.getText();
	}

	public void hyperLinkActivated() {
		WizardDialog d = new WizardDialog(this.getShell(), new NamespaceWizard(this.getTestSuite()));
		if (d.open() == Window.OK) {
			this.fireValueChanged(this.fSendField);
		}
		this.messageEditor.updateItems();
	}

	@Override
	public void inputElementChanged(Element input) {
		this.setInputElement(input);
	}

	private void setInputElement(Element inputElement) {
		this.messageEditor.displayElement(inputElement);

	}

	@Override
	public void valueChanged(String newValue) {
		this.fSendField.setText(newValue);

	}

}
