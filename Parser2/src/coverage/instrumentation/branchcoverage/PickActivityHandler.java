package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Element;

import coverage.instrumentation.ActivityTools;

public class PickActivityHandler implements IStructuredActivity {
	
	private static final String ON_MESSAGE="onMessage";
	private static final String ON_ALARM="onAlarm";

	public void insertMarkerForBranchCoverage(Element element) {
		identifyBranches(element,ON_MESSAGE);
		identifyBranches(element,ON_ALARM);
	}

	private void identifyBranches(Element element, String name) {
		List children=element.getChildren(name,ActivityTools.NAMESPACE_BPEL_2);
		Element child;
		for(int i=0;i<children.size();i++){
			child=(Element) children.get(i);
			child=ActivityTools.getActivity(child);
			if(child!=null){
				BranchMetric.insertMarkerForBranch(child,"");
			}
		}
	}
}
