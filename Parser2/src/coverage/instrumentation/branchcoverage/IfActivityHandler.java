package coverage.instrumentation.branchcoverage;

import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;

import sun.security.krb5.internal.br;

import coverage.instrumentation.ActivityTools;
import coverage.instrumentation.BasisActivity;
import coverage.instrumentation.StructuredActivity;

public class IfActivityHandler implements IStructuredActivity {

	private static final String ELSE_ELEMENT = "else";

	private static final String ELSE_IF_ELEMENT = "elseif";

	public void insertMarkerForBranchCoverage(Element element) {
		if (element.getChild(ELSE_ELEMENT) == null) {
			insertElseBranch(element);
		}
		insertMarkerForIfBranch(element);
		insertMarkerForELSEIFBranches(element);
		insertMarkerForElseBranch(element);
	}

	private void insertMarkerForElseBranch(Element element) {
		Element branch_activity;
		branch_activity=ActivityTools.getActivity(element.getChild(ELSE_ELEMENT));
		if(branch_activity!=null){
			insertMarkerForBranch(ActivityTools.ensureActivityInSequence(branch_activity));
		}
	}

	private void insertMarkerForELSEIFBranches(Element element) {
		Element branch_activity;
		List elseif_branches=element.getChildren(ELSE_IF_ELEMENT);
		for(int i=0;i<elseif_branches.size();i++){
			branch_activity=ActivityTools.getActivity((Element)elseif_branches.get(i));
			if(branch_activity!=null){
				insertMarkerForBranch(ActivityTools.ensureActivityInSequence(branch_activity));
			}
		}
	}

	private void insertMarkerForIfBranch(Element element) {
		Element branch_activity=ActivityTools.getActivity(element);
		if(branch_activity!=null){
			insertMarkerForBranch(ActivityTools.ensureActivityInSequence(branch_activity));
		}
	}
	




	private void insertMarkerForBranch(Element element) {
		Comment comment1 = new Comment(BranchMetric.getNextLabel());
		Comment comment2 = new Comment(BranchMetric.getNextLabel());		
		element.addContent(element.getContentSize(), comment2);
		element.addContent(0, comment1);
	}



	private void insertElseBranch(Element element) {
		Element elseElement = new Element(ELSE_ELEMENT, element.getNamespace());
		element.addContent(element.getContentSize(), elseElement);
	}

}
