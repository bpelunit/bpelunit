package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;

import coverage.instrumentation.ActivityTools;

public class WhileActivityHandler implements IStructuredActivity {

	public void insertMarkerForBranchCoverage(Element element) {
		List children = element.getChildren();
		Element activity,child;
		for (int i = 0; i < children.size(); i++) {
			child=(Element)children.get(i);
			if (ActivityTools.isActivity(child)) {
				activity = ActivityTools.encloseActivityInSequence(child);
				insertMarkerForBranch(activity);
				break;
			}
		}
	}

	private void insertMarkerForBranch(Element element) {
		Comment comment1 = new Comment(BranchMetric.getNextLabel());
		Comment comment2 = new Comment(BranchMetric.getNextLabel());
		element.addContent(element.getContentSize(), comment2);
		element.addContent(0, comment1);
	}

}
