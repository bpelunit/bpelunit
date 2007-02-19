package coverage.instrumentation.branchcoverage;

import org.jdom.Comment;
import org.jdom.Element;

import coverage.instrumentation.activitytools.ActivityTools;
import coverage.instrumentation.activitytools.StructuredActivity;

public class RepeatUntilActivityHandler implements IStructuredActivity {
	

	
	private static final String IF_TAG="if";
	private static final String CONDITION_TAG="condition";

	public void insertMarkerForBranchCoverage(Element element) {
		branchFromConditionToActivity(element);
		branchFromActivityToCondition(element);
	}

	private void branchFromActivityToCondition(Element element) {
		Element activity=ActivityTools.getFirstActivityChild(element);
		BranchMetric.insertMarkerAfterAllActivities(activity, "");
	}

	private void branchFromConditionToActivity(Element element) {
		Element countVariable = ActivityTools.createVariable(element
				.getDocument());
		ActivityTools.insertVariable(countVariable, element.getDocument()
				.getRootElement());
		Element initializeAssign = ActivityTools
				.createInitializeAssign(countVariable);
		insert(initializeAssign, element);
		Element increesAssign = ActivityTools.createIncreesAssign(countVariable);
		insertIncreesAssign(increesAssign, element);
		insertIfConstruct(element,countVariable);
	}

	private void insertIfConstruct(Element element,Element countVariable) {
		Element sequence=ActivityTools.getFirstActivityChild(element);
		if(sequence!=null){
			Element if_element=new Element(IF_TAG,element.getNamespace());
			Element condition=new Element(CONDITION_TAG,ActivityTools.NAMESPACE_BPEL_2);
			Element sequence2=new Element(StructuredActivity.SEQUENCE_ACTIVITY,element.getNamespace());
			condition.setText("$"+countVariable.getName()+"=1");
			if_element.addContent(condition);
			if_element.addContent(sequence2);
			sequence.addContent(0,if_element);
			BranchMetric.insertMarkerBevorAllActivities(sequence2, "");
		}
		
	}

	private void insertIncreesAssign(Element increesAssign, Element element) {
		Element sequence=ActivityTools.getFirstActivityChild(element);
		if(sequence!=null){
			sequence.addContent(increesAssign);
		}
	}

	private void insert(Element initializeAssign, Element element) {
		Element sequence = ActivityTools.ensureElementIsInSequence(element);
		sequence.addContent(sequence.indexOf(element), initializeAssign);
	}

}
