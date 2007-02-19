package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Element;

import coverage.instrumentation.activitytools.ActivityTools;

public class IfActivityHandler implements IStructuredActivity {

	private static final String ELSE_ELEMENT = "else";

	private static final String ELSE_IF_ELEMENT = "elseif";

	public void insertMarkerForBranchCoverage(Element element) {
		if (element.getChild(ELSE_ELEMENT,ActivityTools.NAMESPACE_BPEL_2) == null) {
			insertElseBranch(element);
		}
		insertMarkerForIfBranch(element);
		insertMarkerForELSEIFBranches(element);
		insertMarkerForElseBranch(element);
	}

	private void insertMarkerForElseBranch(Element element) {
		Element branch_activity;
		Element else_el=element.getChild(ELSE_ELEMENT,ActivityTools.NAMESPACE_BPEL_2);
		branch_activity = ActivityTools.getFirstActivityChild(else_el);
		if (branch_activity == null) {
			branch_activity =ActivityTools.createSequence();
			element.addContent(branch_activity);
		}
		BranchMetric.insertMarkerForBranch(branch_activity,"");

	}

	private void insertMarkerForELSEIFBranches(Element element) {
		Element branch_activity;
		List elseif_branches = element.getChildren(ELSE_IF_ELEMENT,ActivityTools.NAMESPACE_BPEL_2);
		for (int i = 0; i < elseif_branches.size(); i++) {
			branch_activity = ActivityTools
					.getFirstActivityChild((Element) elseif_branches.get(i));
			if (branch_activity != null) {
				BranchMetric.insertMarkerForBranch(ActivityTools
						.ensureElementIsInSequence(branch_activity),"");
			}
		}
	}

	private void insertMarkerForIfBranch(Element element) {
		Element branch_activity = ActivityTools.getFirstActivityChild(element);
		if (branch_activity != null) {
			BranchMetric.insertMarkerForBranch(ActivityTools
					.ensureElementIsInSequence(branch_activity),"");
		}
	}

	private void insertElseBranch(Element element) {
		Element elseElement = new Element(ELSE_ELEMENT,ActivityTools.NAMESPACE_BPEL_2);
		element.addContent(element.getContentSize(), elseElement);
	}

}
