package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Element;

import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.exception.BpelException;

public class PickActivityHandler implements IStructuredActivity {

	private static final String ON_MESSAGE = "onMessage";

	private static final String ON_ALARM = "onAlarm";

	public void insertMarkerForBranchCoverage(Element element) throws BpelException {
		identifyBranches(element, ON_MESSAGE);
		identifyBranches(element, ON_ALARM);
	}

	private void identifyBranches(Element element, String name) throws BpelException {
		List children = element.getChildren(name,
				BpelXMLTools.getBpelNamespace());
		Element child;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			child = BpelXMLTools.getFirstActivityChild(child);
			if (child == null) {
				throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
			}
			BranchMetric.insertMarkerBevorAllActivities(child, "!!!!!Pick");

		}
	}
}
