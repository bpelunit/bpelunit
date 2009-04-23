/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.toolsupport.editors.wizards.pages;

import java.util.ArrayList;
import java.util.List;

import org.bpelunit.framework.client.eclipse.ExtensionControl;
import org.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.bpelunit.framework.client.eclipse.dialog.field.SelectionField;
import org.bpelunit.framework.client.eclipse.dialog.field.TextField;
import org.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import org.bpelunit.framework.control.util.ActivityUtil;
import org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import org.bpelunit.framework.xml.suite.XMLProperty;
import org.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import org.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing deployment options.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DeploymentOptionWizardPage extends StructuredActivityWizardPage {

	private static class OptionListLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof XMLProperty) {
				XMLProperty property= (XMLProperty) element;
				switch (columnIndex) {
					case 0:
						return property.getName();
					case 1:
						return property.getStringValue();
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

	private ListDialogField fSelectionField;
	private XMLPUTDeploymentInformation fPutInfo;

	public DeploymentOptionWizardPage(String pageName) {
		super(pageName);
		setTitle("Configure Deployment");
		setDescription("Add or remove deployment option for the selected PUT deployer");
	}

	@Override
	public void handleAddPressed() {

		String[] edit= editProperty(null);
		if (edit != null) {
			XMLProperty xmlCondition= fPutInfo.addNewProperty();
			xmlCondition.setName(edit[0]);
			xmlCondition.setStringValue(edit[1]);
			recreateInput();
			enableButtonsForSelection(fSelectionField, false);
		}

	}

	@Override
	public void handleEditPressed() {

		XMLProperty current= getSelectedProperty();
		if (current == null)
			return;

		String[] edit= editProperty(current);
		if (edit != null) {
			current.setName(edit[0]);
			current.setStringValue(edit[1]);
			recreateInput();
			enableButtonsForSelection(fSelectionField, false);
		}

	}

	@Override
	public void handleRemovePressed() {
		XMLProperty prop= getSelectedProperty();
		if (prop != null) {
			int index= ActivityUtil.getIndexFor(fPutInfo.getPropertyArray(), prop);
			if (index != -1) {
				fPutInfo.removeProperty(index);
				recreateInput();
				enableButtonsForSelection(fSelectionField, false);
			}
		}

	}

	private XMLProperty getSelectedProperty() {
		List<Object> selectedElements= fSelectionField.getSelectedElements();
		if (selectedElements.size() > 0)
			return ((XMLProperty) selectedElements.get(0));
		else
			return null;
	}

	public void recreateInput() {
		List<Object> l = new ArrayList<Object>();
		for(Object o : fPutInfo.getPropertyArray()) {
			l.add(o);
		}
		fSelectionField.setElements(l);
	}

	private String[] editProperty(XMLProperty currentProperty) {

		String initialKey= currentProperty != null ? currentProperty.getName() : null;
		String initialValue= currentProperty != null ? currentProperty.getStringValue() : null;

		String title= currentProperty != null ? "Edit an option " : "Add an option";
		FieldBasedInputDialog dialog= new FieldBasedInputDialog(getShell(), title);

		SelectionField field= new SelectionField(dialog, "Key", initialKey, "Keys...", ExtensionControl
				.getSuiteOptionsForDeployer(fPutInfo.getType()));
		field.setValidator(new NotEmptyValidator("Key"));
		dialog.addField(field);

		TextField field2= new TextField(dialog, "Value", initialValue, TextField.Style.SINGLE); // TODO
		field2.setValidator(new NotEmptyValidator("Value"));
		dialog.addField(field2);

		if (dialog.open() != Window.OK)
			return null;

		String[] s= new String[2];
		s[0]= field.getSelection();
		s[1]= field2.getSelection();
		return s;
	}

	public void init(XMLPUTDeploymentInformation putInfo) {
		fPutInfo= putInfo;

		ListFieldListener deploymentConfigListener= createListFieldListener();
		fSelectionField= new ListDialogField(deploymentConfigListener, fButtons, new OptionListLabelProvider());
		fSelectionField.setDialogFieldListener(deploymentConfigListener);
		fSelectionField.setTableColumns(new ListDialogField.ColumnsDescription(new String[] { "Key", "Value" }, true));
		fSelectionField.setLabelText(null);

		List<Object> l = new ArrayList<Object>();
		for(Object o : fPutInfo.getPropertyArray()) {
			l.add(o);
		}
		fSelectionField.setElements(l);
		enableButtonsForSelection(fSelectionField, false);
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		fSelectionField.doFillIntoGrid(composite, nColumns);

		GridData gd= (GridData) fSelectionField.getListControl(null).getLayoutData();
		gd.grabExcessVerticalSpace= true;
		gd.grabExcessHorizontalSpace= true;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.DEPLOYMENTOPTION;
	}
}
