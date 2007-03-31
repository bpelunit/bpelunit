package coverage.instrumentation.metrics.branchcoverage;

import static coverage.instrumentation.bpelxmltools.BpelXMLTools.*;

import org.jdom.Element;

import coverage.CoverageConstants;
import coverage.exception.BpelException;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.bpelxmltools.exprlang.ExpressionLanguage;
import coverage.instrumentation.bpelxmltools.exprlang.impl.XpathLanguage;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.wstools.CMServiceFactory;

public class ForEachActivity implements IStructuredActivity {


	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
		boolean parallel = element.getAttributeValue(FOREACH_PARALLEL_ATTR)
				.equals(FOREACH_PARALLEL_ATTR_VALUE_YES);
		if (parallel) {
			insertMarkerForParallelBranches(element);
		} else {
			insertMarkerForSequenceBranches(element);
		}
	}

	/**
	 * 
	 * @param element
	 * @throws BpelException
	 */
	private void insertMarkerForSequenceBranches(Element element)
			throws BpelException {
		Element activity = element.getChild(StructuredActivity.SCOPE_ACTIVITY,
				getBpelNamespace());
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ELEMENT
					+ "(Scope) in ForEach activity.");
		}
		activity = getFirstEnclosedActivity(activity);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY
					+ " in ForEach activity.");
		}
		BranchMetric.insertLabelsForBranch(activity);

	}

	/**
	 * Fügt Markierung für parallele Zweige ein. Die Markierungen werden mit
	 * Hilfe der Countervariable generiert.
	 * 
	 * @param forEach
	 * @param counterVariable
	 * @throws BpelException
	 */
	private void insertMarkerForParallelBranches(Element forEach)
			throws BpelException {

		Element assign = createBPELElement(ASSIGN_ELEMENT);

		Element startValueVariable = copyStartValue(forEach, assign);
		Element stopValueVariable = copyStopValue(forEach, assign);
		String targetVariable = createVariableName();
		String marker = BranchMetric.getNextLabel();
		assign.addContent(createCopyForInvoke(startValueVariable,
				stopValueVariable, targetVariable, marker));
		insertInvokeForRegistrationOfLabels(forEach, assign, targetVariable);
		insertLabelsForBranches(forEach, marker);
	}

	private Element copyStopValue(Element element, Element assign) {
		Element copy;
		Element stopValue = insertNewIntVariable(null, null);
		copy = extractInfoFromFOREACH(element, stopValue, FOREACH_COUNTER_FINALVALUE_ATTR);
		assign.addContent(copy);
		return stopValue;
	}

	private Element copyStartValue(Element element, Element assign) {
		Element startValue = insertNewIntVariable(null, null);
		Element copy = extractInfoFromFOREACH(element, startValue,
				FOREACH_COUNTER_STARTVALUE_ATTR);
		assign.addContent(copy);
		return startValue;
	}

	private void insertInvokeForRegistrationOfLabels(Element element,
			Element assign, String inputVariable) {
		CMServiceFactory cmFactory = CMServiceFactory.getInstance();
		cmFactory.insertVariableForRegisterMarker(inputVariable);
		Element invoke = cmFactory
				.createInvokeElementForRegisterMarker(inputVariable);
		ensureElementIsInSequence(element);
		Element parent = element.getParentElement();
		int index = parent.indexOf(element);
		parent.addContent(index, assign);
		parent.addContent(index + 1, invoke);
	}

	private void insertLabelsForBranches(Element element, String marker)
			throws BpelException {
		Element activity = getFirstEnclosedActivity(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		String counterVariable = element
				.getAttributeValue(FOREACH_COUNTERNAME_ATTR);
		BranchMetric.insertLabelsForParallelForEach(activity, marker,
				counterVariable);
	}

	private Element createCopyForInvoke(Element stopValue,
			Element variableWithStopValue, String targetVariable, String label) {
		Element from = createBPELElement(FROM_ELEMENT);
		// from.setAttribute(EXPRESSION_LANGUAGE_ATTRIBUTE,
		// XpathLanguage.LANGUAGE_SPEZIFIKATION);
		ExpressionLanguage expLang = ExpressionLanguage
				.getInstance(CoverageConstants.EXPRESSION_LANGUAGE);
		String[] strings = new String[] {
				'\'' + IMetric.DYNAMIC_COVERAGE_LABEL_IDENTIFIER + '\'',
				expLang.valueOf(stopValue.getAttributeValue(NAME_ATTR)),
				'\'' + MetricHandler.SEPARATOR + '\'',
				expLang.valueOf(variableWithStopValue
						.getAttributeValue(NAME_ATTR)),
				'\'' + MetricHandler.SEPARATOR + '\'', '\'' + label + '\'' };
		from.setText(expLang.concat(strings));
		Element to = createBPELElement(TO_ELEMENT);
		to.setAttribute(VARIABLE_ATTR, targetVariable);
		to.setAttribute(PART_ATTR, CoverageConstants.PART_OF_REGISTER_MESSAGE);
		Element copy = createBPELElement(COPY_ELEMENT);
		copy.addContent(from);
		copy.addContent(to);
		return copy;
	}

	private Element extractInfoFromFOREACH(Element forEach,
			Element variableWithStartValue, String childName) {
		Element from = createBPELElement(FROM_ELEMENT);
		Element to = createBPELElement(TO_ELEMENT);
		Element startValue = forEach.getChild(childName, getBpelNamespace());
		// String expressionLang = startValue
		// .getAttributeValue(EXPRESSION_LANGUAGE_ATTRIBUTE);
		// if (expressionLang != null) {
		// from.setAttribute(EXPRESSION_LANGUAGE_ATTRIBUTE,
		// expressionLang);
		// from.setText(startValue.getText());
		// } else {
		Element literal = createBPELElement(LITERAL_ELEMENT);
		literal.setText(startValue.getText());
		from.addContent(literal);
		// }
		to.setAttribute(VARIABLE_ATTR, variableWithStartValue
				.getAttributeValue(NAME_ATTR));
		// startValue.setAttribute(EXPRESSION_LANGUAGE_ATTRIBUTE,
		// XpathLanguage.LANGUAGE_SPEZIFIKATION);
		startValue.setAttribute(EXPRESSION_LANGUAGE_ATTR,
				XpathLanguage.LANGUAGE_SPEZIFIKATION);
		startValue.setText(ExpressionLanguage.getInstance(
				CoverageConstants.EXPRESSION_LANGUAGE).valueOf(
				variableWithStartValue.getAttributeValue(NAME_ATTR)));
		Element copy =createBPELElement(COPY_ELEMENT);
		copy.addContent(from);
		copy.addContent(to);
		return copy;
	}
}
