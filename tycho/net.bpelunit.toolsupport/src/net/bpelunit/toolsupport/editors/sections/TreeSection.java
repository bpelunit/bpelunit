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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The TreeSection is a structured section which contains a tree viewer.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class TreeSection extends StructuredSection {

	private TreeViewer fTreeViewer;

	public TreeSection(Composite parent, FormToolkit toolkit, TestSuitePage page) {
		super(parent, toolkit, page);
	}

	public TreeSection(Composite parent, FormToolkit toolkit, TestSuitePage page, boolean enableUpDownButtons) {
		super(parent, toolkit, page, enableUpDownButtons);
	}
	
	public TreeSection(Composite parent, FormToolkit toolkit, TestSuitePage page, boolean enableUpDownButtons, boolean enableDuplicateButton, String enableAdd2Button) {
		super(parent, toolkit, page, enableUpDownButtons, enableDuplicateButton, enableAdd2Button);
	}

	@Override
	protected void createStructuredViewer(Composite container) {
		fTreeViewer= new TreeViewer(container, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		GridData gd= new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint= 100;
		gd.widthHint= 100;
		fTreeViewer.getTree().setLayoutData(gd);

		fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection sel= (IStructuredSelection) event.getSelection();
					setEditRemoveDuplicateEnabled(true);
					itemSelected(sel.getFirstElement());
				}
			}
		});

		fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					editPressed();
				}
			}
		});

		hookMenu();
	}

	@Override
	public StructuredViewer getViewer() {
		return fTreeViewer;
	}

	protected TreeViewer getTreeViewer() {
		return (TreeViewer) getViewer();
	}

}
