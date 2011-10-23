/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import java.util.HashMap;
import java.util.Map;

import net.bpelunit.framework.xml.suite.XMLAnyElement;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.formwidgets.HyperlinkField;
import net.bpelunit.toolsupport.editors.formwidgets.HyperlinkField.IHyperLinkFieldListener;
import net.bpelunit.toolsupport.editors.wizards.NamespaceWizard;
import net.bpelunit.toolsupport.editors.wizards.TransportOptionWizard;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import net.bpelunit.toolsupport.editors.wizards.fields.LayoutUtil;
import net.bpelunit.toolsupport.editors.wizards.fields.MessageEditor;
import net.bpelunit.toolsupport.editors.wizards.fields.SelectionButtonDialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.StringDialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.TextDialogField;
import net.bpelunit.toolsupport.editors.wizards.pages.OperationWizardPage;
import net.bpelunit.toolsupport.util.schema.nodes.Element;

import org.apache.xmlbeans.XmlOptions;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
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
public class SendComponent extends DataComponent implements 
		MessageChangeListener, StringValueListener {

	protected TextDialogField fSendField;
	protected XMLSendActivity fSendData;
	protected StringDialogField fDelayStringField;
	protected SelectionButtonDialogField fDelaySelectionField;
	protected SelectionButtonDialogField enterLiteralXMLCheckBox;

	protected boolean fDelaySelected;
	protected MessageEditor messageEditor;
	protected TabItem literalXMLTab;
	private TabItem messageEditorTab;
	private TabFolder tabFolder;

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
			if (!this.messageEditor.isXMLValid()) {
				boolean reset = MessageDialog
						.openQuestion(this.getShell(), "Reset XML?",
								"Continuing will reset the XML to the default message of the selected Operation. Continue anyway?");
				if (!reset) {
					this.enterLiteralXMLCheckBox.setSelection(true);
					return;
				}
			}
			this.messageEditorTab.setImage(null);
			this.literalXMLTab.setImage(image);
		}
		this.fSendField.getTextControl(null).setEditable(selected);
		this.messageEditor.setEditable(!selected);
	}

	private void initValues() {

		XMLAnyElement data = this.fSendData.getData();
		if (data == null) {
			data = this.fSendData.addNewData();
		}

		if (!data.newCursor().toFirstChild()) {
			// no children!
			this.fSendField.setText("");
		} else {
			XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);

			Map<?, ?> ns = new HashMap<Object, Object>();
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

		this.tabFolder = new TabFolder(group, SWT.TOP);
		this.tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item == SendComponent.this.literalXMLTab) {
					SendComponent.this.fSendField.getTextControl(null).setFocus();
				}
			}
		});
		GridData gd = new GridData();
		gd.minimumHeight = 200;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = nColumns;
		this.tabFolder.setLayoutData(gd);

		this.messageEditor = new MessageEditor(this.tabFolder, SWT.NULL, this.getTestSuite());
		if (this.getWizardPage() instanceof OperationWizardPage) {
			OperationWizardPage comp = (OperationWizardPage) this.getWizardPage();
			comp.getOperationDataComponent().addMessageListener(this);
		}
		this.messageEditor.setXML(this.fSendField.getText());
		this.messageEditor.addStringValueListener(this);
		this.messageEditorTab = new TabItem(this.tabFolder, SWT.NULL);
		this.messageEditorTab.setControl(this.messageEditor);
		this.messageEditorTab.setText("Message Editor");

		this.fSendField.doFillIntoGrid(this.tabFolder, nColumns);
		this.literalXMLTab = new TabItem(this.tabFolder, SWT.NULL);
		this.literalXMLTab.setImage(ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_LOCK));
		Text text = this.fSendField.getTextControl(null);
		text.setEditable(false);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (SendComponent.this.enterLiteralXMLCheckBox.isSelected()) {
					String xml = SendComponent.this.getXmlText();
					SendComponent.this.messageEditor.setXML(xml);
				}
			}
		});
		text.setFocus();
		this.literalXMLTab.setControl(text);
		this.literalXMLTab.setText("XML to be sent");

		LayoutUtil.setHeightHint(text, Dialog
				.convertHeightInCharsToPixels(this.getFontMetrics(), 6));
		LayoutUtil.setWidthHint(text, this.getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);

		Composite inner = new Composite(group, SWT.NULL);
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		inner.setLayoutData(gridData);
		inner.setLayout(new GridLayout(10, false));

		this.enterLiteralXMLCheckBox.doFillIntoGrid(inner, 1);
		if (!this.messageEditor.isXMLValid() && !this.fSendField.getText().isEmpty()) {
			this.enterLiteralXMLCheckBox.setSelection(true);
			this.setInputType();
			this.tabFolder.setSelection(1);
		}
		DialogField.createEmptySpace(inner, 7);

		this.fDelaySelectionField.doFillIntoGrid(inner, 1);
		Button selectionButton = this.fDelaySelectionField.getSelectionButton(null);
		LayoutUtil.setVerticalAlign(selectionButton, GridData.CENTER);

		this.fDelayStringField.doFillIntoGrid(inner, 1);

		Text textControl = this.fDelayStringField.getTextControl(null);
		LayoutUtil.setWidthHint(textControl, this.getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(textControl);

		HyperlinkField namespacePrefixField = new HyperlinkField("Configure Namespace Prefixes...");
		namespacePrefixField.setHyperLinkFieldListener(new IHyperLinkFieldListener() {
			public void hyperLinkActivated() {
				WizardDialog d = new WizardDialog(getShell(), new NamespaceWizard(getTestSuite()));
				if (d.open() == Window.OK) {
					fireValueChanged(fSendField);
					// Message Editor does not take part in fireValueChanged Listeners
					// as those update far too often
					messageEditor.updateItems();
				}
			}
		});
		namespacePrefixField.createControl(group, nColumns, GridData.BEGINNING);

		HyperlinkField transportOptionField = new HyperlinkField("Add HTTP Header...");
		transportOptionField.setHyperLinkFieldListener(new IHyperLinkFieldListener() {
			public void hyperLinkActivated() {
				WizardDialog d = new WizardDialog(getShell(), new TransportOptionWizard(fSendData));
				if (d.open() == Window.OK) {
					fireValueChanged(fSendField);
					// Message Editor does not take part in fireValueChanged Listeners
					// as those update far too often
					messageEditor.updateItems();
				}
			}
		});
		transportOptionField.createControl(group, nColumns, GridData.BEGINNING);
		
		// If the WSDL contains only one service with one port and one
		// operation, theses values are preselected. If this is the case, the
		// InputElement of the Operation must be displayed from the
		// MessageEditor
		if (this.getWizardPage() instanceof OperationWizardPage) {
			// This can only happen, if WizardPage is a OperationWizardPage. In
			// the other cases, the operation is already saved and displayed
			// automatically.
			OperationWizardPage wizardPage = (OperationWizardPage) this.getWizardPage();
			Element element;
			try {
				element = wizardPage.getElementForOperation();
				if (this.fSendField.getText().isEmpty()) {
					this.messageEditor.displayElement(element, true);
				}
			} catch (Exception e1) {
				// no (existing) operation selected. Error is shown elsewhere.
			}
		}

		return group;
	}

	public String getXmlText() {
		return this.fSendField.getText();
	}

	public void setXmlText(String xml) {
		this.fSendField.setTextWithoutUpdate(xml);
	}

	public String getDelaySequence() {
		return fDelaySelected ? this.fDelayStringField.getText() : "";
	}

	@Override
	public void messageChanged(Element input) {
		this.setOperationMessage(input, true);
	}

	public void setOperationMessage(Element inputElement, boolean notifyListener) {
		this.messageEditor.displayElement(inputElement, notifyListener);
	}

	@Override
	public void valueChanged(String newValue) {
		this.fSendField.setText(newValue);
	}

}
