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
import net.bpelunit.framework.xml.suite.XMLCopy;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
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
 * The DataCopyComponent contains controls for displaying and editing data copy operations, i.e.
 * XPath expressions describing source and destination of a copy operation.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DataCopyComponent extends StructuredDataComponent implements IHyperLinkFieldListener {

	private static class CopyListLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof XMLCopy) {
				XMLCopy property= (XMLCopy) element;
				switch (columnIndex) {
					case 0:
						return property.getFrom();
					case 1:
						return property.getTo();
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

	private ListDialogField fDataCopyField;
	private XMLTwoWayActivity fParentActivity;

	public DataCopyComponent(IWizardPage wizard, FontMetrics metrics) {
		super(wizard, metrics);
	}

	@Override
	public void handleAddPressed() {

		String[] edit= editCopy(null);
		if (edit != null) {
			XMLCopy xmlCopy= fParentActivity.getMapping().addNewCopy();
			xmlCopy.setFrom(edit[0]);
			xmlCopy.setTo(edit[1]);
			recreateInput();
			enableButtonsForSelection(fDataCopyField, false);
		}
	}

	@Override
	public void handleEditPressed() {

		XMLCopy current= getSelectedCopy();
		if (current == null)
			return;

		String[] edit= editCopy(current);
		if (edit != null) {
			current.setFrom(edit[0]);
			current.setTo(edit[1]);
			recreateInput();
			enableButtonsForSelection(fDataCopyField, false);
		}

	}

	@Override
	public void handleRemovePressed() {
		XMLCopy copy= getSelectedCopy();
		if (copy != null) {
			int index= fParentActivity.getMapping().getCopyList().indexOf(copy);
			if (index != -1) {
				fParentActivity.getMapping().removeCopy(index);
				recreateInput();
				enableButtonsForSelection(fDataCopyField, false);
			}
		}
	}

	private XMLCopy getSelectedCopy() {
		List<Object> selectedElements= fDataCopyField.getSelectedElements();
		if (selectedElements.size() > 0)
			return ((XMLCopy) selectedElements.get(0));
		else
			return null;
	}

	public void recreateInput() {
		List<Object> l = new ArrayList<Object>();
		for(Object o : fParentActivity.getMapping().getCopyList()) {
			l.add(o);
		}
		fDataCopyField.setElements(l);
	}

	private String[] editCopy(XMLCopy currentProperty) {

		FieldBasedInputDialog dialog= new FieldBasedInputDialog(getWizard().getShell(), "Add a copy operation");

		String initialFrom= currentProperty != null ? currentProperty.getFrom() : null;
		String initialTo= currentProperty != null ? currentProperty.getTo() : null;

		TextField fromField= new TextField(dialog, "Source", initialFrom, TextField.Style.MULTI);
		fromField.setValidator(new XPathValidator("source"));
		dialog.addField(fromField);

		TextField toField= new TextField(dialog, "Target", initialTo, TextField.Style.MULTI);
		toField.setValidator(new XPathValidator("target"));
		dialog.addField(toField);

		if (dialog.open() != Window.OK)
			return null;

		String[] s= new String[2];
		s[0]= fromField.getSelection();
		s[1]= toField.getSelection();
		return s;
	}

	// convertHeightInCharsToPixels(6);
	public void init(XMLTwoWayActivity activity) {

		ListFieldListener copyListener= new ListFieldListener();
		fDataCopyField= new ListDialogField(copyListener, fButtons, new CopyListLabelProvider());
		fDataCopyField.setDialogFieldListener(copyListener);
		fDataCopyField.setTableColumns(new ListDialogField.ColumnsDescription(new String[] { "From", "To" }, true));
		fDataCopyField.setLabelText(null);

		if (activity.getMapping() == null)
			activity.addNewMapping();

		fParentActivity= activity;
		List<Object> l = new ArrayList<Object>();
		for(Object o : fParentActivity.getMapping().getCopyList()) {
			l.add(o);
		}
		fDataCopyField.setElements(l);
		enableButtonsForSelection(fDataCopyField, false);
	}

	@Override
	public Composite createControls(Composite composite, int nColumns) {

		Group group= createGroup(composite, "Data Copy Operations", nColumns, new GridData(SWT.FILL, SWT.FILL, true, true));
		fDataCopyField.doFillIntoGrid(group, nColumns);

		final TableViewer tableViewer= fDataCopyField.getTableViewer();
		tableViewer.getTable().setHeaderVisible(true);

		GridData gd= (GridData) fDataCopyField.getListControl(null).getLayoutData();
		gd.heightHint= Dialog.convertHeightInCharsToPixels(getFontMetrics(), 6);

		gd.grabExcessVerticalSpace= false;
		gd.widthHint= getMaxFieldWidth();
		gd.grabExcessHorizontalSpace= true;

		HyperlinkField field= new HyperlinkField("Configure Namespace Prefixes...");
		field.setHyperLinkFieldListener(this);
		field.createControl(group, nColumns, GridData.BEGINNING);

		return group;
	}

	public void hyperLinkActivated() {
		WizardDialog d= new WizardDialog(getShell(), new NamespaceWizard(getTestSuite()));
		d.open();
	}

}
