/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.bpelunit.framework.xml.suite.XMLAnyElement;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
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
import net.bpelunit.toolsupport.editors.wizards.fields.TemplateVelocity;
import net.bpelunit.toolsupport.editors.wizards.fields.TextDialogField;
import net.bpelunit.toolsupport.editors.wizards.pages.OperationWizardPage;
import net.bpelunit.toolsupport.util.schema.nodes.Element;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The SendComponent allows the user to enter XML data to be sent.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */

public class SendComponent extends DataComponent implements MessageChangeListener,
		StringValueListener {

	protected TextDialogField fSendField;
	protected XMLSendActivity fSendData;
	protected StringDialogField fDelayStringField;
	protected SelectionButtonDialogField fDelaySelectionField;

	protected boolean fDelaySelected;
	protected MessageEditor messageEditor;

	private int option;
	public TemplateVelocity fieldTemplate;
	private Text browserFolder;

	public SendComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	public void saveData() {
		XMLAnyElement xmlAny = XMLAnyElement.Factory.newInstance();
		final XmlCursor xmlCursor = xmlAny.newCursor();

		try {
			if (option == 0) {
				// xmlCursor.setTextValue(messageEditor.getMessageAsXML());
				// fSendData.setData(xmlAny);
				if (fSendData.isSetTemplate()) {
					fSendData.unsetTemplate();
				}
				
				
			} else if (option == 1) {
				// xmlCursor.setTextValue(fSendField.getText());
				// fSendData.setData(xmlAny);
				if (fSendData.isSetTemplate()) {
					fSendData.unsetTemplate();
				}
			} else {

				if (!browserFolder.getText().isEmpty()) {
					fSendData.setTemplate(null);
					fSendData.getTemplate().setSrc(browserFolder.getText());
					saveFile(browserFolder.getText(), fieldTemplate.getText());

				} else {
					xmlCursor.setTextValue(fieldTemplate.getText());

					fSendData.setTemplate(xmlAny);
				}
				if(fSendData.isSetData()){
					fSendData.unsetData();
				}

			}

		} finally {
			xmlCursor.dispose();
		}

	}

	private void saveFile(String FilePath, String FileContent) {

		FileWriter file;
		BufferedWriter writer;

		try {
			file = new FileWriter(FilePath, false);
			writer = new BufferedWriter(file);
			writer.write(FileContent, 0, FileContent.length());

			writer.close();
			file.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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

		this.initValues();
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
		final Group group = new Group(composite, SWT.None);
		group.setText("Data to be sent");

		GridData gd = new GridData();
		gd.minimumHeight = 200;
		gd.widthHint = 300;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = nColumns;

		final StackLayout stackLayout = new StackLayout();
		group.setLayout(stackLayout);
		group.setLayoutData(gd);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		final Group[] groupOptions = new Group[3];
		for (int k = 0; k < groupOptions.length; k++) {
			groupOptions[k] = new Group(group, SWT.BORDER_DOT);
			groupOptions[k].setLayout(gridLayout);
		}
		stackLayout.topControl = groupOptions[0];

		createTreeMessageEditorGroup(groupOptions);
		createLiteralMessageEditorGroup(composite, nColumns, group, groupOptions);
		createTemplateEditorGroup(composite, group, gd, stackLayout, groupOptions);

		return group;
	}

	private void createTreeMessageEditorGroup(final Group[] groupOptions) {
		this.messageEditor = new MessageEditor(groupOptions[0], SWT.NULL, this.getTestSuite());
		if (this.getWizardPage() instanceof OperationWizardPage) {
			OperationWizardPage comp = (OperationWizardPage) this.getWizardPage();
			comp.getOperationDataComponent().addMessageListener(this);
		}
		this.messageEditor.setXML(this.fSendField.getText());
	}

	private void createTemplateEditorGroup(Composite composite, final Group group, GridData gd,
			final StackLayout stackLayout, final Group[] groupOptions) {
		fieldTemplate = new TemplateVelocity(groupOptions[2], SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.WRAP);
		fieldTemplate.setLayoutData(gd);
		fieldTemplate.addListener(SWT.CHANGED, new Listener() {
			public void handleEvent(Event e) {
				SendComponent.this.fireValueChanged(null);
			}
		});

		browserFolder = new Text(groupOptions[2], SWT.LINE_CUSTOM | SWT.BORDER);

		final Button browserButton = new Button(groupOptions[2], SWT.BUTTON1);
		browserButton.setText("Browse...");
		browserButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Shell shell = new Shell(Display.getDefault());
				FileDialog fc = new FileDialog(shell);
				fc.setText("Selection file");
				fc.open();
				browserFolder.setText(fc.getFilterPath() + "/" + fc.getFileName());
				loadFile(browserFolder.getText());
			}
		});

		

		if (fSendData.isSetTemplate()) {
			XMLAnyElement template = fSendData.getTemplate();
			if (template.isSetSrc()) {
				File file = new File(template.getSrc().toString());
				if (!file.exists()) {
					final String docSrc = fSendData.documentProperties().getSourceName();
					final File fBPTS = new File(docSrc);
					file = new File(fBPTS.getParentFile(), template.getSrc().toString());
					browserFolder.setText(file.getAbsolutePath());
				}

				if (!file.exists()) {
					getWizardPage().setErrorMessage("File not found");
				} else {
					browserFolder.setText(template.getSrc().toString());
					loadFile(file.getAbsolutePath());
				}
			} else {
				final XmlCursor templateCursor = template.newCursor();
				final StringBuilder xmlText = new StringBuilder();
				templateCursor.toFirstContentToken();
				do {
					xmlText.append(templateCursor.getChars());
				} while (templateCursor.toNextSibling());

				templateCursor.dispose();

				fieldTemplate.setText(xmlText.toString());
			}
		}

		final Combo comboBox = new Combo(composite, SWT.READ_ONLY | SWT.DROP_DOWN | SWT.BORDER
				| SWT.VERTICAL);
		comboBox.setItems(new String[] { "Document", "Xml Literal", "Template" });
		comboBox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				option = comboBox.getSelectionIndex();
				stackLayout.topControl = groupOptions[option];

				messageEditor.setEditable(option == 0);
				fSendField.getTextControl(null).setEditable(option == 1);

				group.layout();
			}
		});
		comboBox.select(0);
	}

	private void createLiteralMessageEditorGroup(Composite composite, int nColumns,
			final Group group, final Group[] groupOptions) {
		this.fSendField.doFillIntoGrid(groupOptions[1], nColumns);

		Text text = this.fSendField.getTextControl(null);
		text.setEditable(false);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String xml = SendComponent.this.getXmlText();
				SendComponent.this.messageEditor.setXML(xml);
			}
		});
		text.setFocus();

		DialogField.createEmptySpace(composite, 7);

		this.fDelaySelectionField.doFillIntoGrid(composite, 1);
		Button selectionButton = this.fDelaySelectionField.getSelectionButton(null);
		LayoutUtil.setVerticalAlign(selectionButton, GridData.CENTER);

		this.fDelayStringField.doFillIntoGrid(composite, 1);

		Text textControl = this.fDelayStringField.getTextControl(null);
		LayoutUtil.setWidthHint(textControl, this.getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(textControl);

		HyperlinkField namespacePrefixField = new HyperlinkField("Configure Namespace Prefixes...");
		namespacePrefixField.setHyperLinkFieldListener(new IHyperLinkFieldListener() {
			public void hyperLinkActivated() {
				WizardDialog d = new WizardDialog(getShell(), new NamespaceWizard(getTestSuite()));
				if (d.open() == Window.OK) {
					fireValueChanged(fSendField);
					// Message Editor does not take part in fireValueChanged
					// Listeners
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
					// Message Editor does not take part in fireValueChanged
					// Listeners
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
	}

	private void loadFile(String direccion) {
		try {
			final FileReader fr = new FileReader(direccion);
			final BufferedReader bf = new BufferedReader(fr);
			String textfile = "";
			String sCadena = new String();
			while ((sCadena = bf.readLine()) != null) {
				textfile += sCadena + "\n";
			}
			fieldTemplate.setText(textfile);
			bf.close();
		} catch (Exception execution) {
			// FIXME: add logging
			System.out.println("Fail file");
		}
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