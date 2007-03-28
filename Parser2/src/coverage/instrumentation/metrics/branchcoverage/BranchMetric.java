package coverage.instrumentation.metrics.branchcoverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import coverage.exception.BpelException;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;
import coverage.instrumentation.bpelxmltools.StructuredActivity;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.wstools.CoverageRegistry;

/**
 * Klasse instrumentiert ein BPEL-Prozess, um die Zweigabdeckung bei der
 * Ausführung zu messen.
 * 
 * @author Alex Salnikow
 */
public class BranchMetric implements IMetric {

	public static final String METRIC_NAME = "Branchmetric";

	public static final String BRANCH_LABEL = "branch";

	public static final String NEGATIV_LINK_LABEL = "negativlink";

	public static final String POSITIV_LINK_LABEL = "positivlink";

	private static int count = 0;

	/**
	 * Generiert eine eindeutige Markierung.
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextLabelAndRegister() {
		String label = BRANCH_LABEL + "_" + (count++);
		registerCoverageLabel(label);
		return IMetric.COVERAGE_LABEL_IDENTIFIER + label;
	}

	public static String getNextLabel() {

		return BRANCH_LABEL + "_" + (count++);
	}

	private static void registerCoverageLabel(String label) {
		CoverageRegistry.getInstance().addMarker(label);
	}

	/**
	 * Generiert eindeutige Merkierung für die Links (in der Flow-Umgebung)
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextPositivLinkLabel() {
		String marker = POSITIV_LINK_LABEL + "_" + (count++);
		CoverageRegistry.getInstance().addMarker(marker);
		return IMetric.COVERAGE_LABEL_IDENTIFIER + marker;
	}

	/**
	 * Generiert eindeutige Merkierung für die Links (in der Flow-Umgebung)
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextNegativLinkLabel() {
		String marker = NEGATIV_LINK_LABEL + "_" + (count++);
		CoverageRegistry.getInstance().addMarker(marker);
		return IMetric.COVERAGE_LABEL_IDENTIFIER + marker;
	}

	/**
	 * Fügt Markierung für einen Zweig, der durch eine Aktivität repräsentiert
	 * ist. Es wird eine Markierung davor und eine danach eingefügt.
	 * 
	 * @param activity
	 *            Aktivität, die einen Zweig repräsentiert
	 * @param additionalInfo
	 */
	public static void insertLabelsForBranch(Element activity) {
		insertLabelBevorAllActivities(activity);
		insertLabelAfterAllActivities(activity);
	}

	/**
	 * Fügt eine Markierung vor allen Aktivitäten ein. Wenn es notwendig ist,
	 * dann wird die Markierung ind die Aktivitäten in ein Sequence-Element
	 * eingeschlossen.
	 * 
	 * @param activity
	 * @param additionalInfo
	 */
	public static void insertLabelBevorAllActivities(Element activity) {
		if (!BpelXMLTools.isSequence(activity)) {
			activity = BpelXMLTools.ensureElementIsInSequence(activity);
		}
		activity.addContent(0, new Comment(BranchMetric
				.getNextLabelAndRegister()));
	}

	public static void insertLabelsForParallelForEach(Element activity,
			String marker, String counterVariable) {
		if (!BpelXMLTools.isSequence(activity)) {
			activity = BpelXMLTools.ensureElementIsInSequence(activity);
		}

		Comment comment = new Comment(IMetric.DYNAMIC_COVERAGE_LABEL_IDENTIFIER + marker
				+ MetricHandler.SEPARATOR + counterVariable);

		activity.addContent(0, comment);
	}

	/**
	 * Fügt eine Markierung nach allen Aktivitäten ein. Wenn es notwendig ist,
	 * dann wird die Markierung ind die Aktivitäten in ein Sequence-Element
	 * eingeschlossen.
	 * 
	 * @param activity
	 * @param additionalInfo
	 */
	public static void insertLabelAfterAllActivities(Element activity) {
		if (!BpelXMLTools.isSequence(activity)) {
			activity = BpelXMLTools.ensureElementIsInSequence(activity);
		}
		activity.addContent(new Comment(BranchMetric
				.getNextLabelAndRegister()));
	}

	/**
	 * 
	 * @param activity
	 *            muss innerhalb Sequence sein
	 */
	public static void insertLabelAfterActivity(Element activity) {
		Element parent = activity.getParentElement();
		parent.addContent(parent.indexOf(activity) + 1, new Comment(
				getNextLabelAndRegister()));
	}

	private HashMap<String, IStructuredActivity> structured_activity_handler = new HashMap<String, IStructuredActivity>();

	public BranchMetric() {

		structured_activity_handler.put(StructuredActivity.FLOW_ACTIVITY,
				new FlowActivityHandler());
		structured_activity_handler.put(StructuredActivity.SEQUENCE_ACTIVITY,
				new SequenceActivityHandler());
		structured_activity_handler.put(StructuredActivity.IF_ACTIVITY,
				new IfActivityHandler());
		structured_activity_handler.put(StructuredActivity.WHILE_ACTIVITY,
				new WhileActivityHandler());
		structured_activity_handler.put(
				StructuredActivity.REPEATUNTIL_ACTIVITY,
				new RepeatUntilActivityHandler());
		structured_activity_handler.put(StructuredActivity.FOREACH_ACTIVITY,
				new ForEachActivityHandler());
		structured_activity_handler.put(StructuredActivity.PICK_ACTIVITY,
				new PickActivityHandler());
	}

	public void insertCoverageLabels(Element element) throws BpelException {
		Element next_element;
		Iterator iterator2 = element.getDescendants(new ElementFilter());
		List<Element> elements_to_log = new ArrayList<Element>();
		while (iterator2.hasNext()) {
			next_element = (Element) iterator2.next();
			if (BpelXMLTools.isStructuredActivity(next_element)) {
				elements_to_log.add(next_element);
			}
		}
		for (Iterator<Element> iter = elements_to_log.iterator(); iter
				.hasNext();) {
			next_element = iter.next();
			String next_element_name = next_element.getName();
			if (structured_activity_handler.containsKey(next_element_name)) {
				structured_activity_handler.get(next_element_name)
						.insertMarkerForBranchCoverage(next_element);
			}
		}
	}

	@Override
	public String toString() {
		return METRIC_NAME;
	}

	public String getName() {
		return METRIC_NAME;
	}

}
