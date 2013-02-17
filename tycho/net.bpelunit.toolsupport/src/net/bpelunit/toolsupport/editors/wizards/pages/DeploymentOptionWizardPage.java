/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.ExtensionControl;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.DeployerOptionModifyListener;
import net.bpelunit.framework.client.eclipse.dialog.field.SelectionField;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.control.ext.ExtensionRegistry;
import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLProperty;
import net.bpelunit.toolsupport.editors.wizards.WizardPageCode;
import net.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * A page for editing deployment options.
 * 
 * @version $Id: DeploymentOptionWizardPage.java 199 2009-04-23 17:19:04Z
 *          dluebke $
 * @author Philip Mayer
 * 
 */
public class DeploymentOptionWizardPage extends StructuredActivityWizardPage {

	private ListDialogField fSelectionField;
	private XMLPUTDeploymentInformation fPutInfo;
	private String[] fPossibleParameterNames = null;
	private Class<? extends IBPELDeployer> fDeployerClass;

	public DeploymentOptionWizardPage(String pageName) {
		super(pageName);
		setTitle("Configure Deployment");
		setDescription("Add or remove deployment option for the selected PUT deployer");
	}

	@Override
	public void handleAddPressed() {

		String[] edit = editProperty(null);
		if (edit != null) {
			XMLProperty xmlCondition = fPutInfo.addNewProperty();
			xmlCondition.setName(edit[0]);
			xmlCondition.setStringValue(edit[1]);
			recreateInput();
			enableButtonsForSelection(fSelectionField, false);
		}

	}

	@Override
	public void handleEditPressed() {

		XMLProperty current = getSelectedProperty();
		if (current == null)
			return;

		String[] edit = editProperty(current);
		if (edit != null) {
			current.setName(edit[0]);
			current.setStringValue(edit[1]);
			recreateInput();
			enableButtonsForSelection(fSelectionField, false);
		}

	}

	@Override
	public void handleRemovePressed() {
		XMLProperty prop = getSelectedProperty();
		if (prop != null) {
			int index = fPutInfo.getPropertyList().indexOf(prop);
			if (index != -1) {
				fPutInfo.removeProperty(index);
				recreateInput();
				enableButtonsForSelection(fSelectionField, false);
			}
		}
	}

	private XMLProperty getSelectedProperty() {
		List<Object> selectedElements = fSelectionField.getSelectedElements();
		if (selectedElements.size() > 0)
			return ((XMLProperty) selectedElements.get(0));
		else
			return null;
	}

	public void recreateInput() {
		List<Object> l = new ArrayList<Object>();
		for (Object o : fPutInfo.getPropertyList()) {
			l.add(o);
		}
		fSelectionField.setElements(l);
	}

	private String[] editProperty(XMLProperty currentProperty) {

		String initialKey = currentProperty != null ? currentProperty.getName()
				: null;
		String initialValue = currentProperty != null ? currentProperty
				.getStringValue() : null;

		String title = currentProperty != null ? "Edit an option "
				: "Add an option";
		FieldBasedInputDialog dialog = new FieldBasedInputDialog(getShell(),
				title);

		SelectionField keyField = new SelectionField(dialog, "Key", initialKey,
				"Keys...", this.fPossibleParameterNames);
		keyField.setValidator(new NotEmptyValidator("Key"));
		dialog.addField(keyField);

		TextField valueField = new TextField(dialog, "Value", initialValue,
				TextField.Style.SINGLE); // TODO
		valueField.setValidator(new NotEmptyValidator("Value"));
		dialog.addField(valueField);

		keyField.addModifyListener(new DeployerOptionModifyListener(keyField, valueField,
				fDeployerClass));
		
		if (dialog.open() != Window.OK)
			return null;

		String[] s = new String[2];
		s[0] = keyField.getSelection();
		s[1] = valueField.getSelection();
		return s;
	}

	public void init(XMLPUTDeploymentInformation putInfo) {
		fPutInfo = putInfo;

		ListFieldListener deploymentConfigListener = createListFieldListener();
		fSelectionField = new ListDialogField(deploymentConfigListener,
				fButtons, new XMLPropertyLabelProvider());
		fSelectionField.setDialogFieldListener(deploymentConfigListener);
		fSelectionField.setTableColumns(new ListDialogField.ColumnsDescription(
				new String[] { "Key", "Value" }, true));
		fSelectionField.setLabelText(null);

		List<Object> l = new ArrayList<Object>();
		for (Object o : fPutInfo.getPropertyList()) {
			l.add(o);
		}
		fSelectionField.setElements(l);
		enableButtonsForSelection(fSelectionField, false);

		try {
			String deployerName = fPutInfo.getType();
			IBPELDeployer deployer = ExtensionControl.findDeployerExtension(
					deployerName).createNew();
			fDeployerClass = deployer.getClass();
			this.fPossibleParameterNames = ExtensionRegistry
					.getPossibleConfigurationOptions(deployer.getClass(), true)
					.toArray(new String[0]);
		} catch (Throwable t) {
			this.fPossibleParameterNames = new String[0];
		}
	}

	@Override
	protected void createFieldControls(Composite composite, int nColumns) {

		fSelectionField.doFillIntoGrid(composite, nColumns);

		GridData gd = (GridData) fSelectionField.getListControl(null)
				.getLayoutData();
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
	}

	@Override
	public WizardPageCode getCode() {
		return WizardPageCode.DEPLOYMENTOPTION;
	}
}
