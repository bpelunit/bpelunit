/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.field;

import net.bpelunit.framework.client.eclipse.dialog.Field;
import net.bpelunit.framework.client.eclipse.dialog.FieldBasedInputDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * A field for the {@link FieldBasedInputDialog}. The ListField is a sophisticated input field,
 * displaying a complete list of values and allowing the user to select one of the pre-defined
 * values.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ListField extends Field {

	class ListContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			return ((String[]) inputElement);
		}

		public void dispose() {
			// noop
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// noop
		}
	}

	private TableViewer fViewer;
	private ILabelProvider fLabelProvider;

	private String[] fValues;
	private String fSelection;

	public ListField(FieldBasedInputDialog inputDialog, String labelText, String initialValue, String[] values) {
		super(inputDialog, labelText, initialValue);
		fValues= values;
		fSelection= "";
	}

	public void setLabelProvider(ILabelProvider provider) {
		fLabelProvider= provider;
	}

	@Override
	protected void createControl(Composite parent) {

		fViewer= new TableViewer(parent);
		GridData gd= new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gd.widthHint= 200;
		gd.horizontalSpan= 2;

		fViewer.getTable().setLayoutData(gd);
		fViewer.setContentProvider(new ListContentProvider());
		fViewer.setLabelProvider(fLabelProvider);
		fViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				String selection= extractSelection();
				if (selection != null) {
					fSelection= selection;
					getDialog().validateFields();
				}
			}
		});
		fViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				String selection= extractSelection();
				if (selection != null) {
					fSelection= selection;
					getDialog().validateFields();
					getDialog().doPressOK();
				}
			}

		});

		fViewer.setInput(fValues);
	}

	@Override
	public String getSelection() {
		return fSelection;
	}

	private String extractSelection() {
		if (fViewer.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection struct= (IStructuredSelection) fViewer.getSelection();
			Object first= struct.getFirstElement();
			if (first != null && first instanceof String)
				return (String) first;
		}
		return null;
	}

}
