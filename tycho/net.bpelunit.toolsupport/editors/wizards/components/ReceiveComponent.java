/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.xml.suite.XMLCondition;
import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.toolsupport.editors.formwidgets.HyperlinkField;
import net.bpelunit.toolsupport.editors.formwidgets.HyperlinkField.IHyperLinkFieldListener;
import net.bpelunit.toolsupport.editors.wizards.NamespaceWizard;
import net.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;
import net.bpelunit.toolsupport.util.XPathValidator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * The ReceiveComponent allows the user to add, edit, and remove receive conditions.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ReceiveComponent extends StructuredDataComponent implements IHyperLinkFieldListener {

	private static class ConditionsListLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof XMLCondition) {
				XMLCondition property= (XMLCondition) element;
				switch (columnIndex) {
					case 0:
						return property.getExpression();
					case 1:
						return property.getValue();
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

	private ListDialogField fConditionsField;
	private XMLReceiveActivity fReceiveOperation;

	public ReceiveComponent(IWizardPage page, FontMetrics metrics) {
		super(page, metrics);
	}

	@Override
	public void handleAddPressed() {

		String[] edit= editCondition(null);
		if (edit != null) {
			XMLCondition xmlCondition= fReceiveOperation.addNewCondition();
			xmlCondition.setExpression(edit[0]);
			xmlCondition.setValue(edit[1]);
			recreateInput();
			enableButtonsForSelection(fConditionsField, false);
		}
	}

	@Override
	public void handleEditPressed() {
		XMLCondition xmlCondition= getSelectedCondition();
		if (xmlCondition != null) {
			openConditionEditor(xmlCondition);
		}
	}

	@Override
	public void handleRemovePressed() {
		XMLCondition cond= getSelectedCondition();
		if (cond != null) {
			int index= fReceiveOperation.getConditionList().indexOf(cond);
			if (index != -1) {
				fReceiveOperation.removeCondition(index);
				recreateInput();
				enableButtonsForSelection(fConditionsField, false);
			}
		}
	}

	private XMLCondition getSelectedCondition() {
		List<Object> selectedElements= fConditionsField.getSelectedElements();
		if (selectedElements.size() > 0)
			return ((XMLCondition) selectedElements.get(0));
		else
			return null;
	}

	private String[] editCondition(XMLCondition currentProperty) {

		FieldBasedInputDialog dialog= new FieldBasedInputDialog(getWizard().getShell(), "Add a condition");

		String initialExpression= currentProperty != null ? currentProperty.getExpression() : null;
		String initialValue= currentProperty != null ? currentProperty.getValue() : null;

		TextField expressionField= new TextField(dialog, "Expression", initialExpression, TextField.Style.MULTI);
		expressionField.setValidator(new XPathValidator("expression"));
		dialog.addField(expressionField);

		TextField valueField= new TextField(dialog, "Value", initialValue, TextField.Style.MULTI);
		valueField.setValidator(new NotEmptyValidator("value"));
		dialog.addField(valueField);

		if (dialog.open() != Window.OK)
			return null;

		String[] s= new String[2];
		s[0]= expressionField.getSelection();
		s[1]= valueField.getSelection();
		return s;
	}

	public void init(XMLReceiveActivity receiveOperation) {

		ListFieldListener conditionListener= createListFieldListener();
		fConditionsField= new ListDialogField(conditionListener, fButtons, new ConditionsListLabelProvider());
		fConditionsField.setDialogFieldListener(conditionListener);
		fConditionsField.setTableColumns(new ListDialogField.ColumnsDescription(new String[] { "Condition", "Value" }, true));
		fConditionsField.setLabelText(null);

		fReceiveOperation= receiveOperation;
		recreateInput();
	}


	@Override
	public Composite createControls(Composite composite, int nColumns) {

		Group group= createGroup(composite, "Conditions to be verified", nColumns, new GridData(SWT.FILL, SWT.FILL, true, true));
		fConditionsField.doFillIntoGrid(group, nColumns);

		final TableViewer tableViewer= fConditionsField.getTableViewer();
		tableViewer.getTable().setHeaderVisible(true);

		GridData gd= (GridData) fConditionsField.getListControl(null).getLayoutData();
		gd.heightHint= Dialog.convertHeightInCharsToPixels(getFontMetrics(), 6);

		gd.grabExcessVerticalSpace= false;
		gd.widthHint= getMaxFieldWidth();
		gd.grabExcessHorizontalSpace= true;

		HyperlinkField field= new HyperlinkField("Configure Namespace Prefixes...");
		field.setHyperLinkFieldListener(this);
		field.createControl(group, nColumns, GridData.BEGINNING);

		return group;
	}

	public void recreateInput() {
		List<Object> l = new ArrayList<Object>();
		for(Object o : fReceiveOperation.getConditionList()) {
			l.add(o);
		}
		fConditionsField.setElements(l);
		enableButtonsForSelection(fConditionsField, false);
	}

	public void openConditionEditor(XMLCondition xmlCondition) {
		String[] edit= editCondition(xmlCondition);
		if (edit != null) {
			xmlCondition.setExpression(edit[0]);
			xmlCondition.setValue(edit[1]);
			recreateInput();
			enableButtonsForSelection(fConditionsField, false);
		}
	}

	public void hyperLinkActivated() {
		WizardDialog d= new WizardDialog(getShell(), new NamespaceWizard(getTestSuite()));
		d.open();
	}

}
