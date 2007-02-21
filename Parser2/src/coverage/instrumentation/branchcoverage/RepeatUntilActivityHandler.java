package coverage.instrumentation.branchcoverage;

import org.jdom.Element;

import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.exception.BpelException;

public class RepeatUntilActivityHandler implements IStructuredActivity {


	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
		branchFromConditionToActivity(element);
		branchFromActivityToCondition(element);
	}

	private void branchFromActivityToCondition(Element element)
			throws BpelException {
		Element activity = BpelXMLTools.getFirstActivityChild(element);
		if (element == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		BranchMetric.insertMarkerAfterAllActivities(activity, "");
	}

	private void branchFromConditionToActivity(Element element)
			throws BpelException {
		Element countVariable = BpelXMLTools.createVariable(element
				.getDocument());
		BpelXMLTools.insertVariable(countVariable, element.getDocument()
				.getRootElement());
		Element initializeAssign = BpelXMLTools
				.createInitializeAssign(countVariable);
		insert(initializeAssign, element);
		Element increesAssign = BpelXMLTools
				.createIncreesAssign(countVariable);
		insertIncreesAssign(increesAssign, element);
		insertIfConstruct(element, countVariable);
	}

	private void insertIfConstruct(Element element, Element countVariable) throws BpelException {
		Element activity = BpelXMLTools.getFirstActivityChild(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		if (!BpelXMLTools.isSequence(activity)) {
			activity = BpelXMLTools.ensureElementIsInSequence(activity);
		}
		Element if_element=BpelXMLTools.createIfActivity("$" + countVariable.getName() + "=1");

		Element sequence = BpelXMLTools.createSequence();
		if_element.addContent(sequence);
		activity.addContent(0, if_element);
		BranchMetric.insertMarkerBevorAllActivities(sequence, "");

	}

	private void insertIncreesAssign(Element increesAssign, Element element)
			throws BpelException {
		Element activity = BpelXMLTools.getFirstActivityChild(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		if (!BpelXMLTools.isSequence(activity)) {
			activity = BpelXMLTools.ensureElementIsInSequence(activity);
		}
		activity.addContent(increesAssign);

	}

	private void insert(Element initializeAssign, Element element) {
		Element sequence = BpelXMLTools.ensureElementIsInSequence(element);
		sequence.addContent(sequence.indexOf(element), initializeAssign);
	}

}
