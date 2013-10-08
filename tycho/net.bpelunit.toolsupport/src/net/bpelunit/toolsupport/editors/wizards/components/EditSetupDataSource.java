package net.bpelunit.toolsupport.editors.wizards.components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.client.eclipse.ExtensionControl;
import net.bpelunit.framework.client.model.DataSourceExtension;
import net.bpelunit.framework.control.datasource.VelocityDataSource;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IDataSource.ConfigurationOption;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLDataSource;
import net.bpelunit.framework.xml.suite.XMLProperty;
import net.bpelunit.framework.xml.suite.XMLSetUp;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.wizards.fields.TemplateVelocity;
import net.bpelunit.toolsupport.util.schema.nodes.Element;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
/**
 * Edit the item setup TestSuite and TestCase
 *
 * @author Alejandro Acosta (alex_acos@informaticos.com)
 */
public class EditSetupDataSource extends DataComponent implements MessageChangeListener,
		StringValueListener {

	private XMLSetUp fSendData;
	private XMLTestCase testCase;
	
	
	private TemplateVelocity fieldScript;

	private Text fieldPathToContents;
	private TemplateVelocity fieldContents;
	private Button browserButton;
	private Combo comboBoxType;
	private Button propertyButton;
	
	
	private Text nameField;
	private Text basedOnField;
	private Button abstractField;
	private Button varyField;

	public EditSetupDataSource(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	public void saveData() {
		final String sScript = fieldScript.getText().trim();
		if (sScript.isEmpty()) {
			if (fSendData.isSetScript()) {
				fSendData.unsetScript();
			}
		} else {
			fSendData.setScript(sScript);
		}

		final String sContents = fieldContents.getText().trim();
		final String sDirfile = fieldPathToContents.getText().trim();
		if (!sContents.isEmpty() || !sDirfile.isEmpty()) {
			final XMLDataSource xmlDS;
			if (fSendData.isSetDataSource()) {
				xmlDS = fSendData.getDataSource();
			} else {
				xmlDS = fSendData.addNewDataSource();
			}
			if (!sDirfile.isEmpty()) {
				xmlDS.setSrc(fieldPathToContents.getText());
				saveFile(fieldPathToContents.getText(), sContents);
				xmlDS.setType(comboBoxType.getText());
			} else {
				xmlDS.setContents(sContents);
				xmlDS.setType(comboBoxType.getText());
				VelocityDataSource prueba = new VelocityDataSource();
				prueba.getClass().getAnnotations();

			}
		} else {
			if (fSendData.isSetDataSource()) {
				fSendData.unsetDataSource();
			}
		}
		
		if(testCase!=null)
		{
			testCase.setName(nameField.getText().toString());
			testCase.setBasedOn(basedOnField.getText().toString());
			testCase.setAbstract(abstractField.getSelection());
			testCase.setVary(varyField.getSelection());
		}
		
		
	}


	private void saveFile(String FilePath, String FileContent) {

		FileWriter file;
		BufferedWriter writer;
		if(!FilePath.startsWith("/")){
			try {
				URL url =new URL(fSendData.documentProperties().getSourceName().toString());
				final File fBPTS = new File(url.getFile());
				
				FilePath=fBPTS.getParentFile().toString()+ "/"+FilePath;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				ToolSupportActivator.logErrorMessage(e.getMessage());
			}
			
		}
		try {
			file = new FileWriter(FilePath, false);
			writer = new BufferedWriter(file);
			writer.write(FileContent, 0, FileContent.length());

			writer.close();
			file.close();
		} catch (IOException ex) {
			
			ToolSupportActivator.logErrorMessage(ex.getMessage());
		}
	}
	

	
	
	public void init(XMLSetUp sendData,XMLTestCase ftestCase) {

		fSendData = sendData;
		testCase =ftestCase;

		if (fSendData.isSetDataSource()) {
			if (fSendData.getDataSource().isSetSrc()) {
				File file = new File(fSendData.getDataSource().getSrc().toString());
				if (!file.exists()) {
					
					
					try {
						URL url =new URL(fSendData.documentProperties().getSourceName().toString());
						final File fBPTS = new File(url.getFile());
						file = new File(fBPTS.getParentFile(), fSendData.getDataSource().getSrc()
								.toString());
						fieldPathToContents.setText(file.getAbsolutePath());
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						ToolSupportActivator.logErrorMessage(e.getMessage());
					}
				}else{
					fieldPathToContents.setText(fSendData.getDataSource().getSrc().toString());
				}

				if (!file.exists()) {
					getWizardPage().setErrorMessage("File not found");
				} else {
					loadFile(file.getAbsolutePath());
				}
				comboBoxType.setText(fSendData.getDataSource().getType().toString());
			} else {
				if(fSendData.getDataSource().isSetContents()){
					fieldContents.setText(fSendData.getDataSource().getContents());
					comboBoxType.setText(fSendData.getDataSource().getType().toString());
				}
			}
			
			

		}
		propertyButton.setEnabled(!comboBoxType.getText().isEmpty());
		if (fSendData.isSetScript()) {
			fieldScript.setText(fSendData.getScript());
		}
		
		
		

	}

	public Composite createControls(Composite composite, int nColumns ) {
			
		GridData gd = new GridData();
		gd.minimumHeight = 150;
		gd.widthHint = 500;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = nColumns;

		GridLayout gridLayout = new GridLayout();
		
		composite.setLayout(gridLayout);
		composite.setLayoutData(gd);

		GridLayout grid= new GridLayout();
		
		grid.numColumns = 4;
		final Group[] groupOptions = new Group[2];
		for (int k = 0; k < groupOptions.length; k++) {
			groupOptions[k] = new Group(composite, SWT.BORDER_DOT);
			groupOptions[k].setLayout(grid);
			groupOptions[k].setLayoutData(gd);

		}
		GridData gdField = new GridData();
		gdField.minimumHeight = 160;
		gdField.widthHint = 300;
		gdField.horizontalAlignment = GridData.FILL;
		gdField.verticalAlignment = GridData.FILL;
		gdField.grabExcessHorizontalSpace = true;
		gdField.grabExcessVerticalSpace = true;
		gdField.horizontalSpan = nColumns;

		createScriptEditorGroup(composite, gdField, groupOptions);

		createDataSourceEditorGroup(composite, gdField, groupOptions);

		return composite;
	}

	private void createScriptEditorGroup(Composite group, GridData gd, Group[] groupOptions) {
		groupOptions[0].setText("Script");
		fieldScript = new TemplateVelocity(groupOptions[0], SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.WRAP);
		fieldScript.setLayoutData(gd);
		fieldScript.addListener(SWT.CHANGED, new Listener() {
			public void handleEvent(Event e) {
				EditSetupDataSource.this.fireValueChanged(null);
			}
		});

	}

	private void createDataSourceEditorGroup(final Composite group, GridData gd,
			final Group[] groupOptions) {
		groupOptions[1].setText("Data Source");
		fieldContents = new TemplateVelocity(groupOptions[1], SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.WRAP);
		fieldContents.setLayoutData(gd);
		fieldContents.addListener(SWT.CHANGED, new Listener() {
			public void handleEvent(Event e) {
				EditSetupDataSource.this.fireValueChanged(null);
			}
		});

		fieldPathToContents = new Text(groupOptions[1], SWT.LINE_CUSTOM | SWT.BORDER);
		GridData gdPath = new GridData();
		gdPath.minimumHeight = 100;
		gdPath.widthHint = 0;
		gdPath.horizontalAlignment = GridData.FILL;
		gdPath.verticalAlignment = GridData.FILL;
		gdPath.grabExcessHorizontalSpace = true;
		fieldPathToContents.setLayoutData(gdPath);

		browserButton = new Button(groupOptions[1], SWT.BUTTON1);
		browserButton.setText("Browse...");
		browserButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Shell shell = new Shell(Display.getDefault());
				FileDialog fc = new FileDialog(shell);
				fc.setText("Selection file");
				fc.open();
				fieldPathToContents.setText(fc.getFilterPath() + "/" + fc.getFileName());
				loadFile(fieldPathToContents.getText());
			}
		});

		comboBoxType = new Combo(groupOptions[1], SWT.READ_ONLY | SWT.DROP_DOWN | SWT.BORDER
				| SWT.VERTICAL);

		final Map<String, DataSourceExtension> fDataSources = ExtensionControl.getDataSources();
		for (Map.Entry<String, DataSourceExtension> e : fDataSources.entrySet()) {
			comboBoxType.add(e.getKey());
		}

		propertyButton = new Button(groupOptions[1], SWT.BUTTON1);
		propertyButton.setText("Properties");
		propertyButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (!fSendData.isSetDataSource()) {
					fSendData.addNewDataSource();
				}
				WizardDialog wizard = new WizardDialog(getShell(), new PropertiesWizard(fSendData
						.getDataSource().getPropertyList(), comboBoxType.getText().toString()));
				wizard.open();
			}
		});

		comboBoxType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!comboBoxType.getText().isEmpty()) {
					propertyButton.setEnabled(true);
				} else {
					propertyButton.setEnabled(false);
				}
			}
		});

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
			fieldContents.setText(textfile);
			bf.close();
		} catch (Exception execution) {
			ToolSupportActivator.logErrorMessage("Fail file");
		}
	}

	public String getXmlTextScript() {
		return this.fieldScript.getText();
	}

	public String getXmlTextDataSource() {
		return this.fieldContents.getText();
	}

	@Override
	public void valueChanged(String newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageChanged(Element message) {
		// TODO Auto-generated method stub

	}

	public static class PropertiesWizard extends Wizard {

		public class PageProperties extends WizardPage {
			private Composite container;
			private List<XMLProperty> listProperty;
			private ArrayList<String> nameProperty = new ArrayList<String>();
			private ArrayList<String> defaultValueProperty = new ArrayList<String>();
			private ArrayList<String> descriptionProperty = new ArrayList<String>();
			private String typeProperty;
			private Table table;

			public PageProperties(List<XMLProperty> list, String type) {
				super("DataSource: Properties");
				setTitle("Modify Properties");
				setDescription("Changes the value to store");
				typeProperty = type;
				listProperty = list;

			}

			public void saveData() {
				listProperty.clear();
				Iterator<String> itername = nameProperty.iterator();
				Iterator<String> itervalue = defaultValueProperty.iterator();
				while (itername.hasNext() && itervalue.hasNext()) {
					for (int i = 0; i < nameProperty.toArray().length; i++) {
						String posname = itername.next();
						String posvalue = itervalue.next();
						if (posname.compareTo(table.getItem(i).getText(0)) == 0
								&& posvalue.compareTo(table.getItem(i).getText(1)) != 0) {
							XMLProperty element = XMLProperty.Factory.newInstance();
							element.setName(posname);
							element.setStringValue(table.getItem(i).getText(1));
							listProperty.add(element);
						}
					}

				}
			}

			@Override
			public void createControl(Composite parent) {

				container = new Composite(parent, SWT.NULL);
				FillLayout layout = new FillLayout();

				container.setLayout(layout);

				table = new Table(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
				table.setHeaderVisible(true);
				String[] titles = { "Name", "Value" };

				for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
					TableColumn column = new TableColumn(table, SWT.NULL);
					column.setText(titles[loopIndex]);

				}

				for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
					table.getColumn(loopIndex).pack();
					table.getColumn(loopIndex).setWidth(200);
				}

				final TableEditor editor = new TableEditor(table);

				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;

				final int EDITABLECOLUMN = 1;

				loadPropertiesXML(typeProperty);

				Iterator<String> itername = nameProperty.iterator();
				Iterator<String> itervalue = defaultValueProperty.iterator();
				

				while (itername.hasNext() && itervalue.hasNext()) {
					final TableItem item = new TableItem(table, SWT.WRAP);
					String posname = itername.next();
					item.setText(0, posname.toString());

					String posvalue = itervalue.next();
					item.setText(1, posvalue.toString());

					

				}
				if (!listProperty.isEmpty() && listProperty != null) {

					for (Iterator<XMLProperty> iter = listProperty.iterator(); iter.hasNext();) {
						XMLProperty pos = iter.next();
						for (int i = 0; i < nameProperty.toArray().length; i++) {
							if (pos.getName().compareTo(table.getItem(i).getText(0)) == 0) {
								table.getItem(i).setText(1, pos.getStringValue());
							}
						}
					}
				}
				table.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {

						Control oldEditor = editor.getEditor();
						if (oldEditor != null)
							oldEditor.dispose();

						TableItem item = (TableItem) e.item;
						if (item == null)
							return;

						Text newEditor = new Text(table, SWT.NONE);
						newEditor.setText(item.getText(EDITABLECOLUMN));
						newEditor.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent me) {
								Text text = (Text) editor.getEditor();
								editor.getItem().setText(EDITABLECOLUMN, text.getText());
							}
						});
						newEditor.selectAll();
						newEditor.setFocus();
						editor.setEditor(newEditor, item, EDITABLECOLUMN);
					}
				});
				final Listener labelListener = new Listener() {
					public void handleEvent(Event event) {
						Label label = (Label) event.widget;
						Shell shell = label.getShell();
						switch (event.type) {
						case SWT.MouseDown:
							Event e = new Event();
							e.item = (TableItem) label.getData("_TABLEITEM");
							table.setSelection(new TableItem[] { (TableItem) e.item });
							table.notifyListeners(SWT.Selection, e);
							shell.dispose();
							table.setFocus();
							break;
						case SWT.MouseExit:
							shell.dispose();
							break;
						}
					}
				};
				Listener tableListener = new Listener() {
					Shell tip = null;

					Label label = null;

					public void handleEvent(Event event) {
						switch (event.type) {
						case SWT.MouseMove: {
							if (tip == null)
								break;
							tip.dispose();
							tip = null;
							label = null;
							break;
						}
						case SWT.MouseHover: {
							TableItem item = table.getItem(new Point(event.x, event.y));
							if (item != null) {
								if (tip != null && !tip.isDisposed())
									tip.dispose();
								tip = new Shell(new Shell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
								FillLayout layout = new FillLayout();
								layout.marginWidth = 2;
								tip.setLayout(layout);
								label = new Label(tip, SWT.NONE);
								label.setData("_TABLEITEM", item);
								label.setText(searchDes(item.getText()));
								label.addListener(SWT.MouseExit, labelListener);
								label.addListener(SWT.MouseDown, labelListener);
								Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
								Rectangle rect = item.getBounds(0);
								Point pt = table.toDisplay(rect.x, rect.y);
								tip.setBounds(pt.x, pt.y, size.x, size.y);
								tip.setVisible(true);
							}
						}
						}
					}
				};
				table.addListener(SWT.MouseMove, tableListener);
				table.addListener(SWT.MouseHover, tableListener);
				setControl(container);
				setPageComplete(true);

			}
			
			private String searchDes(String name){
				Iterator<String> itername = nameProperty.iterator();
				Iterator<String> iterdes = descriptionProperty.iterator();
				
				while (itername.hasNext()) {
					for (int i = 0; i < nameProperty.toArray().length; i++) {
						String posname = itername.next();
						String posdes = iterdes.next();
						if (posname.compareTo(name) == 0) {
							return posdes;
						}
					}

				}
				return null;
			}

			private void loadPropertiesXML(String type) {
				try {
					final Map<String, DataSourceExtension> fDataSources = ExtensionControl
							.getDataSources();
					final Class<? extends IDataSource> ds = fDataSources.get(type).createNew()
							.getClass();
					for (Method m : ds.getMethods()) {
						final ConfigurationOption ann = m.getAnnotation(ConfigurationOption.class);
						if (ann != null) {
							nameProperty.add(m.getName().substring(3));
							descriptionProperty.add(ann.description());
							defaultValueProperty.add(ann.defaultValue());
						}
					}
				} catch (SpecificationException e1) {
					// TODO Auto-generated catch block
					ToolSupportActivator.logErrorMessage(e1.getMessage());
				}
			}
		}

		protected PageProperties one;
		private List<XMLProperty> listProperty;
		private String typeProperty;

		public PropertiesWizard(List<XMLProperty> list, String name) {
			super();
			setNeedsProgressMonitor(true);
			listProperty = list;
			typeProperty = name;
		}

		@Override
		public void addPages() {
			one = new PageProperties(listProperty, typeProperty);
			addPage(one);

		}

		@Override
		public boolean performFinish() {
			one.saveData();
			return true;
		}
	}

	public void createControlsTestCase(Composite composite, int nColumns, XMLTestCase testCase) {
		
		final Group group = new Group(composite, SWT.None);
		group.setText("Edit a test case");
		

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		group.setLayout(gridLayout);

		
		
		Label labelName = new Label(group, SWT.NONE);
		labelName.setText("Name: ");
		nameField = new Text(group,SWT.LINE_CUSTOM | SWT.BORDER );
		nameField.setText(testCase.getName());
		
		Label labelBase = new Label(group, SWT.NONE);
		labelBase.setText("Based On: ");
		basedOnField = new Text(group,SWT.LINE_CUSTOM | SWT.BORDER );
		basedOnField.setText(testCase.getBasedOn());
		

		abstractField = new Button(group, SWT.CHECK);
		abstractField.setText("Abstract");
		abstractField.setSelection(testCase.getAbstract());
		
		varyField = new Button(group, SWT.CHECK);
		varyField.setText("Vary send delay times");
		varyField.setSelection(testCase.getVary());
		
		createControls(composite,nColumns);

		
	}
	public TemplateVelocity fieldScript(){
		return fieldScript;
	}

	public TemplateVelocity fieldContents() {
		return fieldContents;
	}

	
}
