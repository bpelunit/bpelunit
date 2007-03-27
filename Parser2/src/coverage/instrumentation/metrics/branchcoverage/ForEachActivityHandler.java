package coverage.instrumentation.metrics.branchcoverage;

import org.jdom.Element;

import coverage.exception.BpelException;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
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
		BranchMetric.insertMarkerForBranch(activity);

	}

	/**
	 * Fügt Markierung für parallele Zweige ein. Die Markierungen werden mit
	 * Hilfe der Countervariable generiert.
	 * 
	 * @param element
	 * @param counterVariable
	 * @throws BpelException
	 */
	private void insertMarkerForParallelBranches(Element element)
			throws BpelException {
		Element from = new Element(BpelXMLTools.FROM_TAG, BpelXMLTools
				.getBpelNamespace());

		Element variableWithStartValue = BpelXMLTools.createIntVariable();
		BpelXMLTools.insertVariable(variableWithStartValue);
		Element variableWithStopValue = BpelXMLTools.createIntVariable();
		BpelXMLTools.insertVariable(variableWithStopValue);
		Element to = new Element(BpelXMLTools.TO_TAG, BpelXMLTools
				.getBpelNamespace());
		Element startValue = element.getChild(ATTRIBUTE_START_VALUE,
				BpelXMLTools.getBpelNamespace());
		// String expressionLang = startValue
		// .getAttributeValue(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE);
		// if (expressionLang != null) {
		// from.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// expressionLang);
		// from.setText(startValue.getText());
		// } else {
		Element literal = new Element(BpelXMLTools.LITERAL_TAG, BpelXMLTools
				.getBpelNamespace());
		literal.setText(startValue.getText());
		from.addContent(literal);
		// }
		to.setAttribute(BpelXMLTools.ATTRIBUTE_VARIABLE, variableWithStartValue
				.getAttributeValue(BpelXMLTools.ATTRIBUTE_NAME));
		// startValue.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// XpathLanguage.LANGUAGE_SPEZIFIKATION);
		startValue.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
				XpathLanguage.LANGUAGE_SPEZIFIKATION);
		startValue.setText(XpathLanguage.valueOf(variableWithStartValue
				.getAttributeValue(BpelXMLTools.ATTRIBUTE_NAME)));

		Element assign = BpelXMLTools.createAssign(from, to);

		from = new Element(BpelXMLTools.FROM_TAG, BpelXMLTools
				.getBpelNamespace());
		to = new Element(BpelXMLTools.TO_TAG, BpelXMLTools.getBpelNamespace());
		Element stopValue = element.getChild(ATTRIBUTE_FINAL_VALUE,
				BpelXMLTools.getBpelNamespace());
		// expressionLang = stopValue
		// .getAttributeValue(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE);
		// if (expressionLang != null) {
		// from.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// expressionLang);
		// from.setText(stopValue.getText());
		// } else {
		literal = new Element(BpelXMLTools.LITERAL_TAG, BpelXMLTools
				.getBpelNamespace());
		literal.setText(stopValue.getText());
		from.addContent(literal);
		// }
		to.setAttribute(BpelXMLTools.ATTRIBUTE_VARIABLE, variableWithStopValue
				.getAttributeValue(BpelXMLTools.ATTRIBUTE_NAME));

		// stopValue.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// XpathLanguage.LANGUAGE_SPEZIFIKATION);

		stopValue.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
				XpathLanguage.LANGUAGE_SPEZIFIKATION);
		stopValue.setText(XpathLanguage.valueOf(variableWithStopValue
				.getAttributeValue(BpelXMLTools.ATTRIBUTE_NAME)));
		BpelXMLTools.addCopyElement(assign, from, to);

		from = new Element(BpelXMLTools.FROM_TAG, BpelXMLTools
				.getBpelNamespace());
		// from.setAttribute(BpelXMLTools.EXPRESSION_LANGUAGE_ATTRIBUTE,
		// XpathLanguage.LANGUAGE_SPEZIFIKATION);
		String marker = BranchMetric.getNextLabel();
		String[] strings = new String[] {
				'\'' + IMetric.DYNAMIC_COVERAGE_LABEL_IDENTIFIER + '\'',
				XpathLanguage.valueOf(variableWithStartValue
						.getAttributeValue(BpelXMLTools.ATTRIBUTE_NAME)),
				'\'' + MetricHandler.SEPARATOR + '\'',
				XpathLanguage.valueOf(variableWithStopValue
						.getAttributeValue(BpelXMLTools.ATTRIBUTE_NAME)),
				'\'' + MetricHandler.SEPARATOR + '\'',
				'\'' + marker + '\'' };
		from.setText(XpathLanguage.concat(strings));
		CMServiceFactory cmFactory = CMServiceFactory.getInstance();
		String stringVariable = BpelXMLTools.createVariableName();
		cmFactory.insertVariableForRegisterMarker(stringVariable);
		to = new Element(BpelXMLTools.TO_TAG, BpelXMLTools.getBpelNamespace());
		to.setAttribute(BpelXMLTools.ATTRIBUTE_VARIABLE, stringVariable);
		to.setAttribute("part", "registerMarker");
		BpelXMLTools.addCopyElement(assign, from, to);
		Element invoke = cmFactory
				.createInvokeElementForRegisterMarker(stringVariable);

		Element sequence = BpelXMLTools.ensureElementIsInSequence(element);

		int index = sequence.indexOf(element);
		sequence.addContent(index, assign);
		sequence.addContent(index + 1, invoke);

		Element activity = BpelXMLTools.getFirstEnclosedActivity(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		String counterVariable = element
				.getAttributeValue(ATTRIBUTE_COUNTERNAME);
		BranchMetric.insertMarkerForParallelForEach(activity, marker,
				counterVariable);

	}

}
