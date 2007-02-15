package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;

import coverage.instrumentation.ActivityTools;

public class PickActivityHandler implements IStructuredActivity {
	
	private static final String ON_MESSAGE="onMessage";
	private static final String ON_ALARM="onAlaram";

	public void insertMarkerForBranchCoverage(Element element) {
		identifyBranches(element,ON_MESSAGE);
		identifyBranches(element,ON_ALARM);
	}

	private void identifyBranches(Element element, String name) {
		List children=element.getChildren(name);
		Element child;
		for(int i=0;i<children.size();i++){
			child=(Element) children.get(i);
			child=ActivityTools.getActivity(child);
			if(child!=null){
				insertMarker(child);
			}
		}
		
	}

	private void insertMarker(Element child) {
		Element sequence=ActivityTools.encloseActivityInSequence(child);
		sequence.addContent(0, new Comment(BranchMetric.getNextLabel()));
		sequence.addContent(new Comment(BranchMetric.getNextLabel()));	
	}

}
