/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;
import net.bpelunit.toolsupport.util.NamespaceDeclaration;
import net.bpelunit.toolsupport.util.NamespaceEditor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing namespaces.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class NamespaceWizardPage extends StructuredActivityWizardPage {

	private static class OptionListLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof NamespaceDeclaration) {
				NamespaceDeclaration property = (NamespaceDeclaration) element;
				switch (columnIndex) {
				case 0:
					return property.getPrefix();
				case 1:
					return property.getUrl();
				}
			}
			return "";
		}

		public void addListener(ILabelProviderListener listener) {
			// not needed
		}

		public void dispose() {
			// not needed
		}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {
			// not needed
		}
	}

	private ListDialogField namespaceField;
	private Map<String, String> namespaces;
	private NamespaceEditor namespaceEditor;

	public NamespaceWizardPage(String pageName) {
		super(pageName);
		this.setTitle("Configure Namespaces");
		this.setDescription("Add or remove namespace declarations");
	}

	@Override
	public void handleAddPressed() {

		String[] edit = this.editProperty(null);
		if (edit != null && this.namespaceEditor.addNamespaceToSuite(edit[0], edit[1])) {
			this.recreateInput();
			this.enableButtonsForSelection(this.namespaceField, false);
		}

	}

	@Override
	public void handleEditPressed() {

		NamespaceDeclaration current = this.getSelectedNamespace();
		if (current == null) {
			return;
		}

		String[] edit = this.editProperty(current);
		if (edit != null) {
			if (this.namespaceEditor.editNamespaceInSuite(current, edit[0], edit[1])) {
				this.recreateInput();
				this.enableButtonsForSelection(this.namespaceField, false);
			}
		}
	}

	@Override
	public void handleRemovePressed() {
		NamespaceDeclaration prop = this.getSelectedNamespace();
		if (prop != null) {
			if (this.namespaceEditor.removeNamespaceFromSuite(prop)) {
				this.recreateInput();
				this.enableButtonsForSelection(this.namespaceField, false);
			}
		}
	}

	private NamespaceDeclaration getSelectedNamespace() {
		List<Object> selectedElements = this.namespaceField.getSelectedElements();
		if (selectedElements.size() > 0) {
			return ((NamespaceDeclaration) selectedElements.get(0));
		}
		return null;
	}

	public void recreateInput() {
		this.namespaces = new HashMap<String, String>();
		this.namespaceEditor.getNamespacesFromSuite(this.namespaces);
		this.namespaceField.setElements(new ArrayList<Object>(this.getNamespacesAsList()));
	}

	private String[] editProperty(NamespaceDeclaration currentProperty) {

		String initialPrefix = currentProperty != null ? currentProperty.getPrefix() : null;
		String initialUrl = currentProperty != null ? currentProperty.getUrl() : null;

		String title = currentProperty != null ? "Edit a namespace" : "Add a namespace";
		FieldBasedInputDialog dialog = new FieldBasedInputDialog(this.getShell(), title);

		TextField prefixField = new TextField(dialog, "Prefix", initialPrefix,
				TextField.Style.SINGLE);
		prefixField.setValidator(new NotEmptyValidator("Prefix"));
		dialog.addField(prefixField);

		TextField urlField = new TextField(dialog, "Url", initialUrl, TextField.Style.SINGLE); // TODO
		urlField.setValidator(new NotEmptyValidator("Url"));
		dialog.addField(urlField);

		if (dialog.open() != Window.OK) {
			return null;
		}

		String[] s = new String[2];
		s[0] = prefixField.getSelection();
		s[1] = urlField.getSelection();
		return s;
	}

	public void init(XMLTestSuite suite) {
		this.namespaceEditor = new NamespaceEditor(suite);

		ListFieldListener copyListener = this.createListFieldListener();
		this.namespaceField = new ListDialogField(copyListener, this.fButtons,
				new OptionListLabelProvider());
		this.namespaceField.setDialogFieldListener(copyListener);
		this.namespaceField.setTableColumns(new ListDialogField.ColumnsDescription(new String[] {
				"Key", "Value" }, true));
		this.namespaceField.setLabelText(null);

		this.recreateInput();
		this.enableButtonsForSelection(this.namespaceField, false);
	}

	private List<NamespaceDeclaration> getNamespacesAsList() {
		List<NamespaceDeclaration> returner = new ArrayList<NamespaceDeclaration>();
		for (String prefix : this.namespaces.keySet()) {
			returner.add(new NamespaceDeclaration(prefix, this.namespaces.get(prefix)));
		}
		return returner;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		this.namespaceField.doFillIntoGrid(composite, nColumns);

		GridData gd = (GridData) this.namespaceField.getListControl(null).getLayoutData();
		gd.grabExcessVerticalSpace = true;
		// gd.widthHint= Dialog.convertWidthInCharsToPixels(getFontMetrics(),
		// 40);
		gd.grabExcessHorizontalSpace = true;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.DEPLOYMENTOPTION;
	}
}
