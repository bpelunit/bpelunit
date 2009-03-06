/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.bpelunit.framework.client.eclipse.dialog.field.TextField;
import org.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;
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
				NamespaceDeclaration property= (NamespaceDeclaration) element;
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
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}

	private ListDialogField fNamespaceField;
	private XMLTestSuite fBaseSuite;
	private Map<String, String> fNamespaces;

	public NamespaceWizardPage(String pageName) {
		super(pageName);
		setTitle("Configure Namespaces");
		setDescription("Add or remove namespace declarations");
	}

	@Override
	public void handleAddPressed() {

		String[] edit= editProperty(null);
		if (edit != null && addNamespaceToSuite(edit[0], edit[1])) {
			recreateInput();
			enableButtonsForSelection(fNamespaceField, false);
		}

	}

	@Override
	public void handleEditPressed() {

		NamespaceDeclaration current= getSelectedNamespace();
		if (current == null)
			return;

		String[] edit= editProperty(current);
		if (edit != null) {
			if (editNamespaceInSuite(current, edit[0], edit[1])) {
				recreateInput();
				enableButtonsForSelection(fNamespaceField, false);
			}
		}
	}

	@Override
	public void handleRemovePressed() {
		NamespaceDeclaration prop= getSelectedNamespace();
		if (prop != null) {
			if (removeNamespaceFromSuite(prop)) {
				recreateInput();
				enableButtonsForSelection(fNamespaceField, false);
			}
		}
	}

	private NamespaceDeclaration getSelectedNamespace() {
		List selectedElements= fNamespaceField.getSelectedElements();
		if (selectedElements.size() > 0)
			return ((NamespaceDeclaration) selectedElements.get(0));
		else
			return null;
	}

	public void recreateInput() {
		getNamespacesFromSuite();
		fNamespaceField.setElements(new ArrayList<Object>(getNamespacesAsList()));
	}

	private String[] editProperty(NamespaceDeclaration currentProperty) {

		String initialPrefix= currentProperty != null ? currentProperty.getPrefix() : null;
		String initialUrl= currentProperty != null ? currentProperty.getUrl() : null;

		String title= currentProperty != null ? "Edit a namespace" : "Add a namespace";
		FieldBasedInputDialog dialog= new FieldBasedInputDialog(getShell(), title);

		TextField prefixField= new TextField(dialog, "Prefix", initialPrefix, TextField.Style.SINGLE);
		prefixField.setValidator(new NotEmptyValidator("Prefix"));
		dialog.addField(prefixField);

		TextField urlField= new TextField(dialog, "Url", initialUrl, TextField.Style.SINGLE); // TODO
		urlField.setValidator(new NotEmptyValidator("Url"));
		dialog.addField(urlField);

		if (dialog.open() != Window.OK)
			return null;

		String[] s= new String[2];
		s[0]= prefixField.getSelection();
		s[1]= urlField.getSelection();
		return s;
	}

	public void init(XMLTestSuite suite) {
		fBaseSuite= suite;

		ListFieldListener copyListener= createListFieldListener();
		fNamespaceField= new ListDialogField(copyListener, fButtons, new OptionListLabelProvider());
		fNamespaceField.setDialogFieldListener(copyListener);
		fNamespaceField.setTableColumns(new ListDialogField.ColumnsDescription(new String[] { "Key", "Value" }, true));
		fNamespaceField.setLabelText(null);

		recreateInput();
		enableButtonsForSelection(fNamespaceField, false);
	}

	class NamespaceDeclaration {
		private String fPrefix;
		private String fUrl;

		public NamespaceDeclaration(String prefix, String url) {
			super();
			fPrefix= prefix;
			fUrl= url;
		}

		protected String getPrefix() {
			return fPrefix;
		}

		protected String getUrl() {
			return fUrl;
		}
	}

	private List<NamespaceDeclaration> getNamespacesAsList() {
		List<NamespaceDeclaration> returner= new ArrayList<NamespaceDeclaration>();
		for (String prefix : fNamespaces.keySet()) {
			returner.add(new NamespaceDeclaration(prefix, fNamespaces.get(prefix)));
		}
		return returner;
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		fNamespaceField.doFillIntoGrid(composite, nColumns);

		GridData gd= (GridData) fNamespaceField.getListControl(null).getLayoutData();
		gd.grabExcessVerticalSpace= true;
		// gd.widthHint= Dialog.convertWidthInCharsToPixels(getFontMetrics(),
		// 40);
		gd.grabExcessHorizontalSpace= true;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.DEPLOYMENTOPTION;
	}

	// ***************** Namespace routines

	private boolean addNamespaceToSuite(String prefix, String url) {
		XmlCursor cursor= fBaseSuite.newCursor();
		cursor.toNextToken();
		cursor.insertNamespace(prefix, url);
		return true;
	}

	private boolean editNamespaceInSuite(NamespaceDeclaration current, String prefix, String url) {
		XmlCursor cursor= getPlacedCursor(current);
		if (cursor != null) {
			// These two operations effectively change the prefix for the complete document
			// Upon re-serialization, the new prefix for the given URL is used in all declarations
			cursor.removeXml();
			// cursor.currentTokenType();
			addNamespaceToSuite(prefix, url);
			// cursor.insertNamespace(prefix, url);
			return true;
		}
		return false;
	}

	private boolean removeNamespaceFromSuite(NamespaceDeclaration prop) {
		XmlCursor cursor= getPlacedCursor(prop);
		if (cursor != null) {
			cursor.removeXml();
			return true;
		}
		return false;
	}

	private void getNamespacesFromSuite() {
		fNamespaces= new HashMap<String, String>();
		fBaseSuite.newCursor().getAllNamespaces(fNamespaces);
	}

	private XmlCursor getPlacedCursor(NamespaceDeclaration current) {
		XmlCursor cursor= fBaseSuite.newCursor();

		while (true) {
			// navigate to the next namespace
			TokenType type= cursor.toNextToken();

			// Skip attributes
			while (type.equals(TokenType.ATTR))
				type= cursor.toNextToken();

			// Current token is no attribute and no namespace -> end of suite element
			if (!type.equals(TokenType.NAMESPACE))
				break;

			// Must be namespace element
			String declarationPrefix= cursor.getName().getLocalPart();
			if (current.getPrefix().equals(declarationPrefix)) {
				return cursor;
			}
		}

		return null;
	}


}
