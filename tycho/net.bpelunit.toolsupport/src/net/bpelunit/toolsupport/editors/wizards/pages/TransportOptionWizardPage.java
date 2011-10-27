/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.pages;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.SelectionField;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.xml.suite.XMLProperty;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
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
public class TransportOptionWizardPage extends StructuredActivityWizardPage {

	private ListDialogField fSelectionField;
	private XMLSendActivity sendActivity;

	public TransportOptionWizardPage(String pageName) {
		super(pageName);
		setTitle("Configure Deployment");
		setDescription("Add or remove deployment option for the selected PUT deployer");
	}

	@Override
	public void handleAddPressed() {

		String[] edit = editProperty(null);
		if (edit != null) {
			XMLProperty xmlProperty = sendActivity.addNewTransportOption();
			xmlProperty.setName(edit[0]);
			xmlProperty.setStringValue(edit[1]);
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
			int index = sendActivity.getTransportOptionList().indexOf(prop);
			if (index != -1) {
				sendActivity.removeTransportOption(index);
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
		for (Object o : sendActivity.getTransportOptionList()) {
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
				"Keys...", new String[0]);
		keyField.setValidator(new NotEmptyValidator("Key"));
		dialog.addField(keyField);

		TextField valueField = new TextField(dialog, "Value", initialValue,
				TextField.Style.SINGLE);
		valueField.setValidator(new NotEmptyValidator("Value"));
		dialog.addField(valueField);

		if (dialog.open() != Window.OK)
			return null;

		String[] s = new String[2];
		s[0] = keyField.getSelection();
		s[1] = valueField.getSelection();
		return s;
	}

	public void init(XMLSendActivity sendActivity) {
		this.sendActivity = sendActivity;

		ListFieldListener deploymentConfigListener = createListFieldListener();
		fSelectionField = new ListDialogField(deploymentConfigListener,
				fButtons, new XMLPropertyLabelProvider());
		fSelectionField.setDialogFieldListener(deploymentConfigListener);
		fSelectionField.setTableColumns(new ListDialogField.ColumnsDescription(
				new String[] { "Key", "Value" }, true));
		fSelectionField.setLabelText(null);

		List<Object> l = new ArrayList<Object>();
		for (Object o : sendActivity.getTransportOptionList()) {
			l.add(o);
		}
		fSelectionField.setElements(l);
		enableButtonsForSelection(fSelectionField, false);
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
