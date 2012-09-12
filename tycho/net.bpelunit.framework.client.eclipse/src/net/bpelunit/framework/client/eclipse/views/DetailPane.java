/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.views;

import java.util.List;

import net.bpelunit.framework.model.test.data.XMLData;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.PageBook;

/**
 * A pane that shows details about a test artefact - either in form of a table, or in form of a text
 * pane (for XML artefacts)
 * 
 * @version $Id$
 * @author Philip Mayer
 */
public class DetailPane {

	private PageBook fViewerbook;

	private Composite fTextComposite;
	private Text fXmlText;
	private TableViewer fTable;
	private Color fWhite;

	private ITestArtefact fTestElement;

	class StateDataLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0)
				return ((StateData) element).getKey();
			else
				return ((StateData) element).getValue();
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}

	class StateDataContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			return ((List<?>) inputElement).toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	public DetailPane(Composite parent, ToolBar toolBar) {

		fViewerbook= new PageBook(parent, SWT.NULL);

		fTextComposite= new Composite(fViewerbook, SWT.NONE);
		fTextComposite.setLayout(new GridLayout());

		Label xmlDataLabel= new Label(fTextComposite, SWT.LEFT);
		xmlDataLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		xmlDataLabel.setText("XML Data:");

		// Add the text for XML

		fWhite= new Color(parent.getShell().getDisplay(), 255, 255, 255);
		fXmlText= new Text(fTextComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		fXmlText.setBackground(fWhite);
		fXmlText.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
				| GridData.GRAB_VERTICAL));

		// Add a table for all other elements
		fTable= new TableViewer(fViewerbook, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

		fTable.getTable().setHeaderVisible(true);

		fTable.setLabelProvider(new StateDataLabelProvider());
		fTable.setContentProvider(new StateDataContentProvider());

		TableColumn keyColumn= new TableColumn(fTable.getTable(), SWT.LEFT);
		keyColumn.setText("Key");
		keyColumn.setWidth(100);

		TableColumn valueColumn= new TableColumn(fTable.getTable(), SWT.LEFT);
		valueColumn.setText("Value");
		valueColumn.setWidth(400);

		fViewerbook.showPage(fTextComposite);
	}

	public void dispose() {
		fWhite.dispose();
	}

	public Control getComposite() {
		return fViewerbook;
	}

	public void setArtefact(ITestArtefact testElement) {
		fTestElement= testElement;
		if (fTestElement instanceof XMLData) {
			fXmlText.setText( ((XMLData) fTestElement).getXmlData());
			fViewerbook.showPage(fTextComposite);
		} else {
			fTable.setInput(testElement.getStateData());
			fViewerbook.showPage(fTable.getTable());
		}
	}
}
