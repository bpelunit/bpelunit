/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.sections;

import net.bpelunit.toolsupport.editors.TestSuitePage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The ListSection is a structured section, which contains a Table (used as a List).
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class ListSection extends StructuredSection {

	private TableViewer fTableViewer;

	public ListSection(Composite parent, FormToolkit toolkit, TestSuitePage page) {
		super(parent, toolkit, page, false, false, null);
	}

	public ListSection(Composite parent, FormToolkit toolkit, TestSuitePage page,
			boolean enableUpDownButtons, boolean enableDuplicateButton, String add2ButtonLabel) {
		super(parent, toolkit, page, enableUpDownButtons, enableDuplicateButton, add2ButtonLabel);
	}

	@Override
	public StructuredViewer getViewer() {
		return fTableViewer;
	}

	@Override
	protected void createStructuredViewer(Composite container) {
		fTableViewer= new TableViewer(container, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		GridData gd= new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint= 100;
		gd.widthHint= 100;
		fTableViewer.getTable().setLayoutData(gd);

		fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection sel= (IStructuredSelection) event.getSelection();
					setEditRemoveDuplicateEnabled(true);
					itemSelected(sel.getFirstElement());
				}
			}
		});

		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					editPressed();
				}
			}
		});

		hookMenu();
	}

}
