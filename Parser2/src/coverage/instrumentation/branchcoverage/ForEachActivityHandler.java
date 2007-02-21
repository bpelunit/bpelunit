package coverage.instrumentation.branchcoverage;

import org.jdom.Element;

import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.exception.BpelException;

public class ForEachActivityHandler implements IStructuredActivity {

	private static final String ATTRIBUTE_PARALLEL = "parallel";

	private static final String PARALLEL_VALUE_YES = "yes";

	private static final String ATTRIBUTE_COUNTERNAME = "counterName";

	public void insertMarkerForBranchCoverage(Element element) throws BpelException {
		boolean parallel = element.getAttributeValue(ATTRIBUTE_PARALLEL)
				.equals(PARALLEL_VALUE_YES);
		if (parallel) {
			insertMarkerForParallelBranches(element,element
					.getAttributeValue(ATTRIBUTE_COUNTERNAME));
		} else {
			insertMarkerForSequenceBranches(element);
		}
	}

	private void insertMarkerForSequenceBranches(Element element) throws BpelException {
		Element activity=element.getChild(StructuredActivity.SCOPE_ACTIVITY, BpelXMLTools.getBpelNamespace());
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		activity = BpelXMLTools.getFirstActivityChild(activity);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertMarkerForBranch(activity, "");

	}

	private void insertMarkerForParallelBranches(Element element,
			String counterVariable) throws BpelException {
		Element activity = BpelXMLTools.getFirstActivityChild(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertMarkerForBranch(activity, counterVariable);
	}

}
