/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.views;

import net.bpelunit.framework.model.test.report.ITestArtefact;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A label provider for displaying test artefacts in a table.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TestSessionLabelProvider extends LabelProvider {

	private final BPELUnitView fTestRunnerPart;

	public TestSessionLabelProvider(BPELUnitView testRunnerPart) {
		fTestRunnerPart= testRunnerPart;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ITestArtefact) {
			ITestArtefact testCaseElement= (ITestArtefact) element;
			return testCaseElement.getName();
		} else {
			throw new IllegalArgumentException(String.valueOf(element));
		}
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ITestArtefact)
			return fTestRunnerPart.getImage((ITestArtefact) element);
		else
			throw new IllegalStateException(element.toString());
	}
}
