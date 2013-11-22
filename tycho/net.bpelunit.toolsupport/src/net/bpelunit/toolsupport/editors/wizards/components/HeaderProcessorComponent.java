/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.client.eclipse.ExtensionControl;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import net.bpelunit.framework.client.eclipse.dialog.field.SelectionField;
import net.bpelunit.framework.client.eclipse.dialog.field.TextField;
import net.bpelunit.framework.client.eclipse.dialog.validate.NotEmptyValidator;
import net.bpelunit.framework.xml.suite.XMLHeaderProcessor;
import net.bpelunit.framework.xml.suite.XMLProperty;
import net.bpelunit.framework.xml.suite.XMLSendOnlyActivity;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.toolsupport.editors.wizards.fields.ComboDialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;
import net.bpelunit.toolsupport.editors.wizards.fields.IDialogFieldListener;
import net.bpelunit.toolsupport.editors.wizards.fields.ListDialogField;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * The HeaderProcessorComponent contains controls for selecting a header processor from a list of
 * all registered processors, and editing properties for this header processor.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class HeaderProcessorComponent extends StructuredDataComponent {

	private static final String HEADER_PROCESSOR_NULL_NAME= "(none)";
	private static final int HEADER_PROCESSOR_NULL_INDEX= 0;

	private class HeaderListener implements IDialogFieldListener {

		public void dialogFieldChanged(DialogField field) {
			headerProcessorChanged();
		}
	}

	private static class ConditionsListLabelProvider implements ITableLabelProvider {

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

	private ComboDialogField fHeaderProcessorField;
	private XMLTwoWayActivity fParentTwoWayActivity;
	private ListDialogField fPropertiesField;

	private String fCurrentlySelectedHeaderProcessor;
	private String[][] fDeployerMetaInformations;
	private XMLSendOnlyActivity fSendOnlyActivity;


	public HeaderProcessorComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	@Override
	public void handleAddPressed() {

		String[] edit= editProperty(null);
		if (edit != null) {
			XMLProperty xmlCondition= getHeaderProcessor().addNewProperty();
			xmlCondition.setName(edit[0]);
			xmlCondition.setStringValue(edit[1]);
			recreateInput();
			enableButtonsForSelection(fPropertiesField, false);
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
			enableButtonsForSelection(fPropertiesField, false);
		}

	}

	@Override
	public void handleRemovePressed() {
		XMLProperty prop= getSelectedProperty();
		if (prop != null) {
			int index= getHeaderProcessor().getPropertyList().indexOf(prop);
			if (index != -1) {
				getHeaderProcessor().removeProperty(index);
				recreateInput();
				enableButtonsForSelection(fPropertiesField, false);
			}
		}

	}

	private XMLProperty getSelectedProperty() {
		List<Object> selectedElements= fPropertiesField.getSelectedElements();
		if (selectedElements.size() > 0)
			return ((XMLProperty) selectedElements.get(0));
		else
			return null;
	}

	public void recreateInput() {
		List<Object> l = new ArrayList<Object>();
		for(Object o : getHeaderProcessor().getPropertyList()) {
			l.add(o);
		}
		fPropertiesField.setElements(l);
	}


	private String[] editProperty(XMLProperty currentProperty) {

		FieldBasedInputDialog dialog= new FieldBasedInputDialog(getWizard().getShell(), "Add a property");

		String initialKey= currentProperty != null ? currentProperty.getName() : null;
		String initialValue= currentProperty != null ? currentProperty.getStringValue() : null;

		SelectionField field= new SelectionField(dialog, "Key", initialKey, "Keys...", new String[0]);
		field.setValidator(new NotEmptyValidator("Key"));
		dialog.addField(field);

		TextField field2= new TextField(dialog, "Value", initialValue, TextField.Style.SINGLE);
		field2.setValidator(new NotEmptyValidator("Value"));
		dialog.addField(field2);

		if (dialog.open() != Window.OK)
			return null;

		String[] s= new String[2];
		s[0]= field.getSelection();
		s[1]= field2.getSelection();
		return s;
	}

	public void headerProcessorChanged() {
		if ( (fPropertiesField != null) && fHeaderProcessorField != null) {

			int selectionIndex= fHeaderProcessorField.getSelectionIndex();
			if (selectionIndex > HEADER_PROCESSOR_NULL_INDEX) {
				fPropertiesField.setEnabled(true);
				String currentName= fHeaderProcessorField.getItems()[fHeaderProcessorField.getSelectionIndex()];
				fCurrentlySelectedHeaderProcessor= name2code(currentName);
				getHeaderProcessor().setName(fCurrentlySelectedHeaderProcessor);
				enableButtonsForSelection(fPropertiesField, false);
			} else {
				fPropertiesField.setEnabled(false);
				// will be removed later
				getHeaderProcessor().setName("removeme");
				fCurrentlySelectedHeaderProcessor= null;
			}
		}

	}

	public void init(XMLSendOnlyActivity sendOnlyActivity) {
		fSendOnlyActivity= sendOnlyActivity;
		init();
	}

	public void init(XMLTwoWayActivity twoWayActivity) {
		fParentTwoWayActivity= twoWayActivity;
		init();
	}
	
	private void init() {
		HeaderListener headerListener= new HeaderListener();
		fHeaderProcessorField= new ComboDialogField(SWT.READ_ONLY);
		fHeaderProcessorField.setDialogFieldListener(headerListener);
		fHeaderProcessorField.setLabelText("Processor");

		fDeployerMetaInformations= ExtensionControl.getHeaderProcessorMetaInformation();
		String[] items= new String[fDeployerMetaInformations.length + 1];
		items[0]= HEADER_PROCESSOR_NULL_NAME;
		for (int i= 0; i < fDeployerMetaInformations.length; i++) {
			items[i + 1]= fDeployerMetaInformations[i][0];
		}

		fHeaderProcessorField.setItems(items);
		ListFieldListener conditionListener= createListFieldListener();
		fPropertiesField= new ListDialogField(conditionListener, fButtons, new ConditionsListLabelProvider());
		fPropertiesField.setDialogFieldListener(conditionListener);
		fPropertiesField.setTableColumns(new ListDialogField.ColumnsDescription(new String[] { "Key", "Value" }, true));
		fPropertiesField.setLabelText("Properties");

		/*
		 * The header processor may still be null...
		 */
		boolean initialized= false;
		fCurrentlySelectedHeaderProcessor= null;
		if (hasHeaderProcessor()) {
			// Check the name
			String code= getHeaderProcessor().getName();
			String readableName= code2name(code);
			int index= getItemIndex(items, readableName);
			if (readableName != null && index != -1) {
				fPropertiesField.setEnabled(true);
				List<Object> l = new ArrayList<Object>();
				for(Object o : getHeaderProcessor().getPropertyList()) {
					l.add(o);
				}
				fPropertiesField.setElements(l);

				fHeaderProcessorField.selectItem(index);
				fCurrentlySelectedHeaderProcessor= code;
				initialized= true;
			}
		} else {
			addNewHeaderProcessor();
		}
		if (!initialized) {
			fPropertiesField.setEnabled(false);
			fHeaderProcessorField.selectItem(0);
		}

		enableButtonsForSelection(fPropertiesField, false);
	}

	private void addNewHeaderProcessor() {
		if(fParentTwoWayActivity != null) {
			fParentTwoWayActivity.addNewHeaderProcessor();
		} else {
			fSendOnlyActivity.addNewHeaderProcessor();
		}
	}

	private XMLHeaderProcessor getHeaderProcessor() {
		if(fParentTwoWayActivity != null) {
			return fParentTwoWayActivity.getHeaderProcessor();
		} else {
			return fSendOnlyActivity.getHeaderProcessor();
		}
	}

	private String code2name(String code) {

		if (code == null)
			return null;

		for (int i= 0; i < fDeployerMetaInformations.length; i++) {
			if (fDeployerMetaInformations[i][1].equals(code))
				return fDeployerMetaInformations[i][0];
		}
		return null;
	}

	private String name2code(String name) {

		if (name == null)
			return null;

		for (int i= 0; i < fDeployerMetaInformations.length; i++) {
			if (fDeployerMetaInformations[i][0].equals(name))
				return fDeployerMetaInformations[i][1];
		}
		return null;
	}

	private int getItemIndex(String[] items, String name) {
		for (int i= 0; i < items.length; i++) {
			if (items[i].equals(name))
				return i;
		}
		return -1;
	}

	private boolean hasHeaderProcessor() {
		return getHeaderProcessor() != null;
	}

	@Override
	public Composite createControls(Composite composite, int nColumns) {

		Group group= createGroup(composite, "Header Processor", nColumns, new GridData(SWT.FILL, SWT.FILL, true, true));
		fHeaderProcessorField.doFillIntoGrid(group, nColumns);

		// header combo
		GridData gd= (GridData) fHeaderProcessorField.getComboControl(null).getLayoutData();
		gd.heightHint= Dialog.convertHeightInCharsToPixels(getFontMetrics(), 6);

		gd.grabExcessVerticalSpace= false;
		gd.widthHint= getMaxFieldWidth();
		gd.grabExcessHorizontalSpace= true;

		// Properties
		fPropertiesField.doFillIntoGrid(group, nColumns);

		final TableViewer tableViewer= fPropertiesField.getTableViewer();
		tableViewer.getTable().setHeaderVisible(true);

		gd= (GridData) fPropertiesField.getListControl(null).getLayoutData();
		gd.heightHint= Dialog.convertHeightInCharsToPixels(getFontMetrics(), 6);

		gd.grabExcessVerticalSpace= false;
		gd.widthHint= getMaxFieldWidth();
		gd.grabExcessHorizontalSpace= true;

		headerProcessorChanged();

		return group;
	}

	public boolean hasHeaderProcessorSelected() {
		return fCurrentlySelectedHeaderProcessor != null;
	}

}
