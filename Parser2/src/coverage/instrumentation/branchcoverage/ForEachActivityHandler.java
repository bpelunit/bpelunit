package coverage.instrumentation.branchcoverage;

import org.jdom.Element;

import coverage.instrumentation.activitytools.ActivityTools;

public class ForEachActivityHandler implements IStructuredActivity {

	private static final String ATTRIBUTE_PARALLEL = "parallel";

	private static final String PARALLEL_VALUE_YES = "yes";

	private static final String ATTRIBUTE_COUNTERNAME = "counterName";

	public void insertMarkerForBranchCoverage(Element element) {
		boolean parallel = element.getAttributeValue(ATTRIBUTE_PARALLEL)
				.equals(PARALLEL_VALUE_YES);
		String counterVariable = element
				.getAttributeValue(ATTRIBUTE_COUNTERNAME);

		if (parallel) {
			insertMarkerForParallelBranches(element, counterVariable);
		} else {
			insertMarkerForSequenceBranches(element);
		}
	}

	private void insertMarkerForSequenceBranches(Element element) {
		Element activity = ActivityTools.getFirstActivityChild(element);
		BranchMetric.insertMarkerForBranch(activity,"");

	}

	private void insertMarkerForParallelBranches(Element element,
			String counterVariable) {

		Element activity = ActivityTools.getFirstActivityChild(element);
		BranchMetric.insertMarkerForBranch(activity, counterVariable);
	}

}
