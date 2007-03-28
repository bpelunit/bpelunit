package coverage.instrumentation.metrics.branchcoverage;

import org.jdom.Element;

import coverage.CoverageConstants;
import coverage.exception.BpelException;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.bpelxmltools.exprlang.ExpressionLanguage;
import coverage.instrumentation.bpelxmltools.exprlang.impl.XpathLanguage;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.wstools.CMServiceFactory;

public class ForEachActivityHandler implements IStructuredActivity {

	private static final String ATTRIBUTE_PARALLEL = "parallel";

	private static final String PARALLEL_VALUE_YES = "yes";

	private static final String ATTRIBUTE_COUNTERNAME = "counterName";

	private static final String ATTRIBUTE_START_VALUE = "startCounterValue";

	private static final String ATTRIBUTE_FINAL_VALUE = "finalCounterValue";

	public void insertMarkerForBranchCoverage(Element element)
			throws BpelException {
		boolean parallel = element.getAttributeValue(ATTRIBUTE_PARALLEL)
				.equals(PARALLEL_VALUE_YES);
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
				BpelXMLTools.getBpelNamespace());
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ELEMENT
					+ "(Scope) in ForEach activity.");
		}
		activity = BpelXMLTools.getFirstEnclosedActivity(activity);
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

		Element assign = BpelXMLTools
				.createBPELElement(BpelXMLTools.ASSIGN_ELEMENT);

		Element startValueVariable = copyStartValue(forEach, assign);
		Element stopValueVariable = copyStopValue(forEach, assign);
		String targetVariable = BpelXMLTools.createVariableName();
		String marker = BranchMetric.getNextLabel();
		assign.addContent(createCopyForInvoke(startValueVariable,
				stopValueVariable, targetVariable, marker));
		insertInvokeForRegistrationOfLabels(forEach, assign, targetVariable);
		insertLabelsForBranches(forEach, marker);
	}

	private Element copyStopValue(Element element, Element assign) {
		Element copy;
		Element stopValue = BpelXMLTools.insertNewIntVariable(null, null);
		copy = extractInfoFromFOREACH(element, stopValue, ATTRIBUTE_FINAL_VALUE);
		assign.addContent(copy);
		return stopValue;
	}

	private Element copyStartValue(Element element, Element assign) {
		Element startValue = BpelXMLTools.insertNewIntVariable(null, null);
		Element copy = extractInfoFromFOREACH(element, startValue,
				ATTRIBUTE_START_VALUE);
		assign.addContent(copy);
		return startValue;
	}

	private void insertInvokeForRegistrationOfLabels(Element element,
			Element assign, String inputVariable) {
		CMServiceFactory cmFactory = CMServiceFactory.getInstance();
		cmFactory.insertVariableForRegisterMarker(inputVariable);
		Element invoke = cmFactory
				.createInvokeElementForRegisterMarker(inputVariable);
		BpelXMLTools.ensureElementIsInSequence(element);
		Element parent = element.getParentElement();
		int index = parent.indexOf(element);
		parent.addContent(index, assign);
		parent.addContent(index + 1, invoke);
	}

	private void insertLabelsForBranches(Element element, String marker)
			throws BpelException {
		Element activity = BpelXMLTools.getFirstEnclosedActivity(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		String counterVariable = element
				.getAttributeValue(ATTRIBUTE_COUNTERNAME);
		BranchMetric.insertLabelsForParallelForEach(activity, marker,
				counterVariable);
	}

	private Element createCopyForInvoke(Element stopValue,
			Element variableWithStopValue, String targetVariable, String label) {
		Element from = BpelXMLTools
				.createBPELElement(BpelXMLTools.FROM_ELEMENT);
		// from.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// XpathLanguage.LANGUAGE_SPEZIFIKATION);
		ExpressionLanguage expLang = ExpressionLanguage
				.getInstance(CoverageConstants.EXPRESSION_LANGUAGE);
		String[] strings = new String[] {
				'\'' + IMetric.DYNAMIC_COVERAGE_LABEL_IDENTIFIER + '\'',
				expLang.valueOf(stopValue
						.getAttributeValue(BpelXMLTools.NAME_ATTRIBUTE)),
				'\'' + MetricHandler.SEPARATOR + '\'',
				expLang.valueOf(variableWithStopValue
						.getAttributeValue(BpelXMLTools.NAME_ATTRIBUTE)),
				'\'' + MetricHandler.SEPARATOR + '\'', '\'' + label + '\'' };
		from.setText(expLang.concat(strings));
		Element to = BpelXMLTools.createBPELElement(BpelXMLTools.TO_ELEMENT);
		to.setAttribute(BpelXMLTools.VARIABLE_ATTRIBUTE, targetVariable);
		to.setAttribute("part", "registerEntries");
		Element copy = BpelXMLTools
				.createBPELElement(BpelXMLTools.COPY_ELEMENT);
		copy.addContent(from);
		copy.addContent(to);
		return copy;
	}

	private Element extractInfoFromFOREACH(Element forEach,
			Element variableWithStartValue, String childName) {
		Element from = BpelXMLTools
				.createBPELElement(BpelXMLTools.FROM_ELEMENT);
		Element to = BpelXMLTools.createBPELElement(BpelXMLTools.TO_ELEMENT);
		Element startValue = forEach.getChild(childName, BpelXMLTools
				.getBpelNamespace());
		// String expressionLang = startValue
		// .getAttributeValue(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE);
		// if (expressionLang != null) {
		// from.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// expressionLang);
		// from.setText(startValue.getText());
		// } else {
		Element literal = BpelXMLTools
				.createBPELElement(BpelXMLTools.LITERAL_ELEMENT);
		literal.setText(startValue.getText());
		from.addContent(literal);
		// }
		to.setAttribute(BpelXMLTools.VARIABLE_ATTRIBUTE, variableWithStartValue
				.getAttributeValue(BpelXMLTools.NAME_ATTRIBUTE));
		// startValue.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// XpathLanguage.LANGUAGE_SPEZIFIKATION);
		startValue.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
				XpathLanguage.LANGUAGE_SPEZIFIKATION);
		startValue.setText(ExpressionLanguage.getInstance(
				CoverageConstants.EXPRESSION_LANGUAGE).valueOf(
				variableWithStartValue
						.getAttributeValue(BpelXMLTools.NAME_ATTRIBUTE)));
		Element copy = new Element("copy", BpelXMLTools.getBpelNamespace());
		copy.addContent(from);
		copy.addContent(to);
		return copy;
	}
}
