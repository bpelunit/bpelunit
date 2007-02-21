package coverage.instrumentation.branchcoverage;

import org.jdom.Element;

import coverage.instrumentation.bpelxmltools.ActivityTools;
import coverage.instrumentation.exception.BpelException;

public class RepeatUntilActivityHandler implements IStructuredActivity {


	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
		branchFromConditionToActivity(element);
		branchFromActivityToCondition(element);
	}

	private void branchFromActivityToCondition(Element element)
			throws BpelException {
		Element activity = ActivityTools.getFirstActivityChild(element);
		if (element == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertMarkerAfterAllActivities(activity, "");
	}

	private void branchFromConditionToActivity(Element element)
			throws BpelException {
		Element countVariable = ActivityTools.createVariable(element
				.getDocument());
		ActivityTools.insertVariable(countVariable, element.getDocument()
				.getRootElement());
		Element initializeAssign = ActivityTools
				.createInitializeAssign(countVariable);
		insert(initializeAssign, element);
		Element increesAssign = ActivityTools
				.createIncreesAssign(countVariable);
		insertIncreesAssign(increesAssign, element);
		insertIfConstruct(element, countVariable);
	}

	private void insertIfConstruct(Element element, Element countVariable) throws BpelException {
		Element activity = ActivityTools.getFirstActivityChild(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		if (!ActivityTools.isSequence(activity)) {
			activity = ActivityTools.ensureElementIsInSequence(activity);
		}
		Element if_element=ActivityTools.createIfActivity("$" + countVariable.getName() + "=1");

		Element sequence = ActivityTools.createSequence();
		if_element.addContent(sequence);
		activity.addContent(0, if_element);
		BranchMetric.insertMarkerBevorAllActivities(sequence, "");

	}

	private void insertIncreesAssign(Element increesAssign, Element element)
			throws BpelException {
		Element activity = ActivityTools.getFirstActivityChild(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		if (!ActivityTools.isSequence(activity)) {
			activity = ActivityTools.ensureElementIsInSequence(activity);
		}
		activity.addContent(increesAssign);

	}

	private void insert(Element initializeAssign, Element element) {
		Element sequence = ActivityTools.ensureElementIsInSequence(element);
		sequence.addContent(sequence.indexOf(element), initializeAssign);
	}

}
