package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.Element;

import coverage.instrumentation.BasisActivity;
import coverage.instrumentation.StructuredActivity;

public class SequenceActivityHandler implements IStructuredActivity {

	public void insertMarkerForBranchCoverage(Element element) {
		List children = element.getChildren();
		Element child;
		Element previousActivity = null;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			if (BasisActivity.isBasisActivity(child)
					|| StructuredActivity.isStructuredActivity(child))
				if (previousActivity != null) {
					BranchMetric.insertMarkerAfterActivity(previousActivity);
				}
			previousActivity = child;
		}
	}
}
