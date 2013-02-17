/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.DeployerOptionModifyListener;
import net.bpelunit.framework.client.eclipse.dialog.field.SelectionField;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.client.model.ConfigurationOption;
import net.bpelunit.framework.client.model.DeployerExtension;
import net.bpelunit.framework.client.model.ExtensionUtil;
import net.bpelunit.framework.control.ext.ExtensionRegistry;
import net.bpelunit.framework.control.deploy.IBPELDeployer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

/**
 * A table-based editor for adding, changing, and removing options for a
 * deployer.
 * 
 * This is a heavily changed copy of org.eclipse.jface.preference.ListEditor,
 * extended to use a table instead of a list and accomodating methods targeted
 * at editing deployment options.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class OptionTableFieldEditor extends FieldEditor {

	/**
	 * The table widget; <code>null</code> if none (before creation or after
	 * disposal).
	 */
	private TableViewer fTable;

	/**
	 * The button box containing the Add, Edit, andRemove buttons;
	 * <code>null</code> if none (before creation or after disposal).
	 */
	private Composite fButtonBox;

	/**
	 * The Add button.
	 */
	private Button fAddButton;

	/**
	 * The Edit button.
	 */
	private Button fEditButton;

	/**
	 * The Remove button.
	 */
	private Button fRemoveButton;

	/**
	 * The selection listener.
	 */
	private SelectionListener fSelectionListener;

	/**
	 * Deployer which is currently edited
	 */
	private DeployerExtension fCurrentDeployer;

	/**
	 * Options of current deployer
	 */
	private List<ConfigurationOption> fConfigurationOptions;

	/**
	 * Indicates the content has changed. Relevant if the user switches
	 * deployers (not detected by framework).
	 */
	private boolean fChanged;

	/**
	 * The class of the currently selected deployer inv: (fCurrentDeployer !=
	 * null && fCurrentDeployerClass == fCurrentDeployer.getClass()) ||
	 * (fCurrentDeployer == null && fCurrentDeployerClass == null))
	 */
	private Class<? extends IBPELDeployer> fCurrentDeployerClass;

	class OptionLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			String result = null;
			if (element != null) {
				ConfigurationOption var = (ConfigurationOption) element;
				switch (columnIndex) {
				case 0: // key
					result = var.getKey();
					break;
				case 1: // value
					result = var.getValue();
					break;
				}
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	class OptionContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			return ((List<?>) inputElement).toArray();
		}

		public void dispose() {
			// do nothing
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}

	}

	public OptionTableFieldEditor(String name, String labelText,
			Composite parent) {
		init(name, labelText);
		createControl(parent);
		fConfigurationOptions = new ArrayList<ConfigurationOption>();
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		Control control = getLabelControl();
		((GridData) control.getLayoutData()).horizontalSpan = numColumns;
		((GridData) fTable.getTable().getLayoutData()).horizontalSpan = numColumns - 1;
	}

	private void createButtons(Composite box) {
		fAddButton = createPushButton(box, "&Add...");//$NON-NLS-1$
		fEditButton = createPushButton(box, "&Edit...");//$NON-NLS-1$
		fRemoveButton = createPushButton(box, "&Remove");//$NON-NLS-1$
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {

		Composite innerComp = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		innerComp.setLayout(gridLayout);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		innerComp.setLayoutData(gridData);

		Control control = getLabelControl(innerComp);
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns;
		control.setLayoutData(gd);

		fTable = getTableControl(innerComp);
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalSpan = numColumns - 1;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		fTable.getTable().setLayoutData(gd);

		fButtonBox = getButtonBoxControl(innerComp);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		fButtonBox.setLayoutData(gd);

		updateButtons();
		fChanged = false;
	}

	public TableViewer getTableControl(Composite parent) {
		if (fTable == null) {
			fTable = new TableViewer(parent, SWT.BORDER | SWT.SINGLE
					| SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
			fTable.getTable().setFont(parent.getFont());

			fTable.getTable().setHeaderVisible(true);

			fTable.setLabelProvider(new OptionLabelProvider());
			fTable.setContentProvider(new OptionContentProvider());

			TableColumn keyColumn = new TableColumn(fTable.getTable(), SWT.LEFT);
			keyColumn.setText("Option");
			keyColumn.setWidth(150);

			TableColumn valueColumn = new TableColumn(fTable.getTable(),
					SWT.LEFT);
			valueColumn.setText("Value");
			valueColumn.setWidth(200);

			fTable.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					handleTableSelectionChanged(event);
				}
			});

		} else {
			checkParent(fTable.getTable(), parent);
		}
		return fTable;
	}

	public Composite getButtonBoxControl(Composite parent) {
		if (fButtonBox == null) {
			fButtonBox = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			fButtonBox.setLayout(layout);
			createButtons(fButtonBox);
			fButtonBox.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					fAddButton = null;
					fEditButton = null;
					fRemoveButton = null;
					fButtonBox = null;
				}
			});

		} else {
			checkParent(fButtonBox, parent);
		}

		return fButtonBox;
	}

	@Override
	protected void doLoad() {
		fConfigurationOptions.clear();
		String s = getPreferenceStore().getString(getPreferenceName());
		Map<String, String> map = ExtensionUtil.deserializeMap(s);
		for (String key : map.keySet()) {
			fConfigurationOptions
					.add(new ConfigurationOption(key, map.get(key)));
		}
		if (fTable != null)
			fTable.setInput(fConfigurationOptions);
	}

	@Override
	protected void doLoadDefault() {
		fConfigurationOptions.clear();
		if (fTable != null)
			fTable.setInput(fConfigurationOptions);
	}

	@Override
	protected void doStore() {
		Map<String, String> map = new HashMap<String, String>();
		for (ConfigurationOption o : fConfigurationOptions) {
			map.put(o.getKey(), o.getValue());
		}
		String s = ExtensionUtil.serializeMap(map);
		if (s != null) {
			getPreferenceStore().setValue(getPreferenceName(), s);
			fChanged = false;
		}
	}

	@Override
	public int getNumberOfControls() {
		return 2;
	}

	/**
	 * Helper method to create a push button.
	 * 
	 * @param parent
	 *            the parent control
	 * @param key
	 *            the resource name used to supply the button's label text
	 * @return Button
	 */
	private Button createPushButton(Composite parent, String key) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(key);
		button.setFont(parent.getFont());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		int widthHint = convertHorizontalDLUsToPixels(button,
				IDialogConstants.BUTTON_WIDTH);
		data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT,
				SWT.DEFAULT, true).x);
		button.setLayoutData(data);
		button.addSelectionListener(getSelectionListener());
		return button;
	}

	/**
	 * Returns this field editor's selection listener. The listener is created
	 * if nessessary.
	 * 
	 * @return the selection listener
	 */
	private SelectionListener getSelectionListener() {
		if (fSelectionListener == null) {
			createSelectionListener();
		}
		return fSelectionListener;
	}

	/**
	 * Creates a selection listener.
	 */
	public void createSelectionListener() {
		fSelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Widget widget = event.widget;
				if (widget == fAddButton) {
					addPressed();
				} else if (widget == fRemoveButton) {
					removePressed();
				} else if (widget == fEditButton) {
					editPressed();
				}
			}
		};
	}

	protected void removePressed() {
		IStructuredSelection sel = (IStructuredSelection) fTable.getSelection();
		fTable.getControl().setRedraw(false);
		for (Iterator<?> i = sel.iterator(); i.hasNext();) {
			ConfigurationOption var = (ConfigurationOption) i.next();
			fConfigurationOptions.remove(var);
			fChanged = true;
		}
		fTable.getControl().setRedraw(true);
		fTable.refresh();
		updateButtons();
	}

	private void addPressed() {
		String[] edit = editProperty(null);
		if (edit != null) {
			addVariable(new ConfigurationOption(edit[0].trim(), edit[1].trim()));
			updateButtons();
			fChanged = true;
		}
	}

	private String[] editProperty(ConfigurationOption currentProperty) {

		String initialKey = currentProperty != null ? currentProperty.getKey()
				: null;
		String initialValue = currentProperty != null ? currentProperty
				.getValue() : null;

		String title = initialKey == null ? "Add Property" : "Edit Property";

		FieldBasedInputDialog dialog = new FieldBasedInputDialog(getShell(),
				title);

		String[] options = ExtensionRegistry.getPossibleConfigurationOptions(
				fCurrentDeployerClass, false).toArray(new String[0]);

		SelectionField selField = new SelectionField(dialog, "Key", initialKey,
				"Keys...", options);
		selField.setValidator(new NotEmptyValidator("Key"));
		dialog.addField(selField);

		TextField valueField = new TextField(dialog, "Value", initialValue,
				TextField.Style.SINGLE);
		valueField.setValidator(new NotEmptyValidator("Value"));
		dialog.addField(valueField);

		selField.addModifyListener(new DeployerOptionModifyListener(selField, valueField, fCurrentDeployerClass));
		
		if (dialog.open() != Window.OK)
			return null;

		String[] s = new String[2];
		s[0] = selField.getSelection();
		s[1] = valueField.getSelection();
		return s;
	}

	/**
	 * Attempts to add the given variable. Returns whether the variable was
	 * added or not (as when the user answers not to overwrite an existing
	 * variable).
	 * 
	 * @param variable
	 *            the variable to add
	 * @return whether the variable was added
	 */
	protected boolean addVariable(ConfigurationOption variable) {
		String name = variable.getKey();

		for (ConfigurationOption existingVariable : fConfigurationOptions) {
			if (existingVariable.getKey().equals(name)) {
				boolean overWrite = MessageDialog.openQuestion(fTable
						.getTable().getShell(), "Overwrite option?",
						"A variable named " + name
								+ " already exists. Overwrite?"); // 
				if (!overWrite) {
					return false;
				}
				fConfigurationOptions.remove(existingVariable);
				break;
			}
		}

		fConfigurationOptions.add(variable);
		fChanged = true;
		fTable.refresh();
		updateButtons();
		return true;
	}

	/**
	 * Creates an editor for the value of the selected environment variable.
	 */
	private void editPressed() {
		IStructuredSelection sel = (IStructuredSelection) fTable.getSelection();
		ConfigurationOption var = (ConfigurationOption) sel.getFirstElement();
		if (var == null) {
			return;
		}

		String[] edit = editProperty(var);
		if (edit != null) {

			String originalName = var.getKey();
			String name = edit[0];
			String value = edit[1];

			if (!originalName.equals(name)) {
				if (addVariable(new ConfigurationOption(name, value))) {
					fConfigurationOptions.remove(var);
					fTable.refresh();
				}
			} else {
				var.setValue(value);
				fChanged = true;
				fTable.update(var, null);
			}
			updateButtons();
		}
	}

	private Shell getShell() {
		return fTable.getTable().getShell();
	}

	/**
	 * Responds to a selection changed event in the environment table
	 * 
	 * @param event
	 *            the selection change event
	 */
	protected void handleTableSelectionChanged(SelectionChangedEvent event) {
		updateButtons();
	}

	private void updateButtons() {
		int size = ((IStructuredSelection) fTable.getSelection()).size();
		fEditButton.setEnabled(size == 1);
		fRemoveButton.setEnabled(size > 0);
	}

	public void setDeployer(DeployerExtension deployerExtension) {

		if (fChanged) {
			if (MessageDialog
					.openQuestion(fTable.getTable().getShell(),
							"Deployer Options Changed",
							"You have made changes to this deployer. Save the changes?"))
				doStore();
		}

		fCurrentDeployer = deployerExtension;
		try {
			fCurrentDeployerClass = deployerExtension.createNew().getClass();
		} catch (Exception e) {
			e.printStackTrace();
			fCurrentDeployerClass = null;
		}
		fChanged = false;
		if (getPreferenceStore() != null)
			doLoad();
		updateButtons();
	}

	@Override
	public String getPreferenceName() {
		return fCurrentDeployer.getId();
	}
}
