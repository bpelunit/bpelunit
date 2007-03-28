package coverage.instrumentation.metrics.branchcoverage;

import static coverage.instrumentation.bpelxmltools.BpelXMLTools.*;

import org.jdom.Element;

import coverage.exception.BpelException;

public class WhileActivityHandler implements IStructuredActivity {

	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
		Element activity = getFirstEnclosedActivity(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertLabelsForBranch(activity);
	}

}
