package coverage.instrumentation.branchcoverage;

import org.jdom.Element;

import coverage.instrumentation.ActivityTools;

public class WhileActivityHandler implements IStructuredActivity {

	public void insertMarkerForBranchCoverage(Element element) {
		Element activity=ActivityTools.getActivity(element);
		BranchMetric.insertMarkerForBranch(activity, "");
	}

}
