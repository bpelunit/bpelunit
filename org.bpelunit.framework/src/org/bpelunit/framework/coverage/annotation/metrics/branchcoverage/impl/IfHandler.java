package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.ELSE_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.ELSE_IF_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createSequence;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.insertElseBranch;

import java.util.List;

import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Element;



/**
 * Die Klasse ist für das Einfügen der Markierungen in der If-Aktivität
 * verantwortlich, die für die Messung der Zweigabdeckung verwendet werden.
 * 
 * @author Alex Salnikow
 */
public class IfHandler implements IStructuredActivityHandler {

	public void insertBranchMarkers(Element element)
			throws BpelException {
		insertMarkerForIfBranch(getFirstEnclosedActivity(element));
		List elseif_branches = element.getChildren(ELSE_IF_ELEMENT,
				getProcessNamespace());
		for (int i = 0; i < elseif_branches.size(); i++) {
			insertMarkerForElseIfBranches(getFirstEnclosedActivity((Element) elseif_branches
					.get(i)));
		}
		Element elseElement = element.getChild(ELSE_ELEMENT, getProcessNamespace());
		if (elseElement == null) {
			elseElement = insertElseBranch(element);
			elseElement.addContent(createSequence());
		}
		insertMarkerForElseBranch(getFirstEnclosedActivity(elseElement));
	}

	/**
	 * 
	 * @param branch_activity
	 *            Aktivität aus dem Else-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForElseBranch(Element branch_activity)
			throws BpelException {
		if (branch_activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetricHandler.insertLabelBevorAllActivities(branch_activity);
//		BranchMetric.insertLabelWithRespectOfTargets(branch_activity);

	}

	/**
	 * 
	 * @param branch_activity
	 *            Aktivität aus dem ElseIf-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForElseIfBranches(Element branch_activity)
			throws BpelException {
		if (branch_activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetricHandler.insertLabelBevorAllActivities(branch_activity);
//		BranchMetric.insertLabelWithRespectOfTargets(branch_activity);

	}

	/**
	 * 
	 * @param branch_activity
	 *            Aktivität aus dem If-Zweig.
	 * @throws BpelException
	 *             Wenn keine Aktivität in dem Zweig vorhanden ist.
	 */
	private void insertMarkerForIfBranch(Element branch_activity)
			throws BpelException {
		if (branch_activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
//		BranchMetric.insertLabelWithRespectOfTargets(branch_activity);
		BranchMetricHandler.insertLabelBevorAllActivities(branch_activity);

	}

}
