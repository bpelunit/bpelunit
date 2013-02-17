/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.views;

import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A content provider for displaying test artefacts in a table.
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke (filter only executed test cases)
 * 
 */
public class TestSessionTreeContentProvider implements ITreeContentProvider {

	private final Object[] NO_CHILDREN= new Object[0];

	public void dispose() {
	}

	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof TestSuite) {
			return ((TestSuite)inputElement).getTestCasesToExecute().toArray(); 
		} else if (inputElement instanceof ITestArtefact) {
			return ((ITestArtefact) inputElement).getChildren().toArray();
		} else {
			return NO_CHILDREN;
		}
	}

	public Object getParent(Object element) {
		if (element instanceof ITestArtefact) {
			return ((ITestArtefact) element).getParent();
		} else {
			return null;
		}
	}

	public boolean hasChildren(Object element) {
		if (element instanceof ITestArtefact) {
			ITestArtefact artefact= ((ITestArtefact) element);
			return artefact.getChildren().size() > 0;
		} else {
			return false;
		}
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
