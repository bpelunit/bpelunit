package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;

import coverage.instrumentation.BasisActivity;
import coverage.instrumentation.StructuredActivity;

public class SequenceActivityHandler implements IStructuredActivity {

	public void insertMarkerForBranchCoverage(Element element) {
		List children = element.getChildren();
		Element child;
		Element lastElement = null;
		int index;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			if (BasisActivity.isBasisActivity(child)
					|| StructuredActivity.isStructuredActivity(child)) {
				if (lastElement != null) { // TODO Einschränken abhängig von
											// letzten Aktivität z.B. kein Zweig
											// zählen nach pick,flow,if..
					index = element.indexOf(child);
					element.addContent(index, new Comment(BranchMetric
							.getNextLabel()+" sequence"));
				}
				lastElement = child;

			}
		}
	}

}
