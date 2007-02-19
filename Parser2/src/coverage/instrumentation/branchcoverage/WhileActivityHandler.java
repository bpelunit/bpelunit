package coverage.instrumentation.branchcoverage;

import org.jdom.Element;

import coverage.instrumentation.activitytools.ActivityTools;

public class WhileActivityHandler implements IStructuredActivity {

	public void insertMarkerForBranchCoverage(Element element) {
		Element activity=ActivityTools.getFirstActivityChild(element);
		BranchMetric.insertMarkerForBranch(activity, "");
	}

}
