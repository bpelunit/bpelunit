package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;

import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivity;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Element;



public class ForEachHandler implements IStructuredActivityHandler {


	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
			insertMarkerForSequenceBranches(element);
	}

	/**
	 * 
	 * @param element
	 * @throws BpelException
	 */
	private void insertMarkerForSequenceBranches(Element element)
			throws BpelException {
		Element activity = element.getChild(StructuredActivity.SCOPE_ACTIVITY,
				BpelXMLTools.getProcessNamespace());
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ELEMENT
					+ "(Scope) in ForEach activity.");
		}
		activity = getFirstEnclosedActivity(activity);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY
					+ " in ForEach activity.");
		}
		BranchMetricHandler.insertLabelsForBranch(activity);

	}

}
