package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.FlowHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.ForEachHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.IfHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.PickHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.RepeatUntilHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.SequenceHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.SwitchHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.WhileHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Klasse instrumentiert ein BPEL-Prozess, um die Zweigabdeckung bei der
 * Ausführung zu messen.
 * 
 * @author Alex Salnikow
 */
public class BranchMetricHandler implements IMetricHandler {

	public static final String METRIC_NAME = "Branchmetric";

	public static final String BRANCH_LABEL = "branch";

//	private static int count = 0;

	/**
	 * Generiert eine eindeutige Markierung.
	 * 
	 * @return eindeutige Markierung
	 */
	private static String getAndRegisterNextLabel() {
		String label = BRANCH_LABEL
				+ Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + (count++);
		// registerCoverageLabel(label);
		return label;
	}

	// public static String getNextLabel() {
	// return BRANCH_LABEL + Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR
	// + (count++);
	// }

	// private static void registerCoverageLabel(String label) {
	// LabelsRegistry.getInstance().addMarker(label);
	// }

	/**
	 * Fügt Markierung für einen Zweig, der durch eine Aktivität repräsentiert
	 * ist. Es wird eine Markierung davor und eine danach eingefügt.
	 * 
	 * @param activity
	 *            Aktivität, die einen Zweig repräsentiert
	 * @param additionalInfo
	 */
//	public static void insertLabelsForBranch(Element activity) {
//		insertLabelBevorAllActivities(activity);
//		insertLabelAfterAllActivities(activity);
//	}

	/**
	 * Fügt eine Markierung vor allen Aktivitäten ein. Wenn es notwendig ist,
	 * dann wird die Markierung ind die Aktivitäten in ein Sequence-Element
	 * eingeschlossen.
	 * 
	 * @param activity
	 * @param additionalInfo
	 */
	public static String insertLabelBevorAllActivities(Element activity) {
		activity = respectTargetsOfLinks(activity);
		if (!isSequence(activity)) {
			activity = ensureElementIsInSequence(activity);
		}
		String label=getAndRegisterNextLabel();
		activity.addContent(0, new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER + label));
		return label;
	}

	private static Element respectTargetsOfLinks(Element activity) {
		List<Element> targets = getTargets(activity);
		if (targets.size() > 0) {
			Element sequence = createSequence();
			for (Iterator<Element> iter = targets.iterator(); iter.hasNext();) {
				sequence.addContent(iter.next().detach());
			}
			Element parent = activity.getParentElement();
			int index = parent.indexOf(activity);
			parent.addContent(index, sequence);
			sequence.addContent(activity.detach());
			activity = sequence;
		}
		return activity;
	}

	/**
	 * Fügt eine Markierung nach allen Aktivitäten ein. Wenn es notwendig ist,
	 * dann wird die Markierung ind die Aktivitäten in ein Sequence-Element
	 * eingeschlossen.
	 * 
	 * @param activity
	 * @param additionalInfo
	 */
	public static String insertLabelAfterAllActivities(Element activity) {
		if (!isSequence(activity)) {
			activity = ensureElementIsInSequence(activity);
		}
		String label = getAndRegisterNextLabel();
		activity.addContent(new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER
				+ label));
		return label;
	}

	/**
	 * 
	 * @param activity
	 *            muss innerhalb Sequence sein
	 */
	public static String  insertLabelAfterActivity(Element activity) {
		// TODO anderer Name
		Element parent = activity.getParentElement();
		String label = getAndRegisterNextLabel();
		parent.addContent(parent.indexOf(activity) + 1, new Comment(
				Instrumenter.COVERAGE_LABEL_IDENTIFIER + label));
		return label;
	}

	/**
	 * 
	 * @param activity
	 *            muss innerhalb Sequence sein
	 */
	public static String insertLabelBevorActivity(Element activity) {
		activity = respectTargetsOfLinks(activity);
		Element parent = activity.getParentElement();
		String label = getAndRegisterNextLabel();
		parent.addContent(parent.indexOf(activity), new Comment(
				Instrumenter.COVERAGE_LABEL_IDENTIFIER + label));
		return label;
	}

	private HashMap<String, IStructuredActivityHandler> structured_activity_handler = new HashMap<String, IStructuredActivityHandler>();

//	private LabelsRegistry markersRegistry;

	public BranchMetricHandler(MarkersRegisterForArchive markersRegistry) {
//		this.markersRegistry = markersRegistry;
		structured_activity_handler.put(StructuredActivities.FLOW_ACTIVITY,
				new FlowHandler(markersRegistry));
		structured_activity_handler.put(StructuredActivities.SEQUENCE_ACTIVITY,
				new SequenceHandler(markersRegistry));
		structured_activity_handler.put(StructuredActivities.IF_ACTIVITY,
				new IfHandler(markersRegistry));
		structured_activity_handler.put(StructuredActivities.WHILE_ACTIVITY,
				new WhileHandler(markersRegistry));
		structured_activity_handler.put(
				StructuredActivities.REPEATUNTIL_ACTIVITY,
				new RepeatUntilHandler(markersRegistry));
		structured_activity_handler.put(StructuredActivities.FOREACH_ACTIVITY,
				new ForEachHandler(markersRegistry));
		structured_activity_handler.put(StructuredActivities.PICK_ACTIVITY,
				new PickHandler(markersRegistry));
		structured_activity_handler.put(StructuredActivities.SWITCH_ACTIVITY,
				new SwitchHandler(markersRegistry));
	}

	public void insertMarkersForMetric(List<Element> activities)
			throws BpelException {
		Element next_element;
		for (Iterator<Element> iter = activities.iterator(); iter.hasNext();) {
			next_element = iter.next();
			String next_element_name = next_element.getName();
			if (structured_activity_handler.containsKey(next_element_name)) {
				structured_activity_handler.get(next_element_name)
						.insertBranchMarkers(next_element);
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

	// public List<String> getPrefix4CovLabeles() {
	// List<String> list = new ArrayList<String>();
	// list.add(BranchMetricHandler.BRANCH_LABEL);
	// return list;
	// }

	// /**
	// * Fügt label vor allen Aktivitäten und verschiebt Targets, wenn
	// vorhanden.
	// *
	// * @param child
	// */
	// public static void insertLabelWithRespectOfTargets(Element child) {
	// child = respectTargetsOfLinks(child);
	// insertLabelBevorAllActivities(child);
	// }

	public static List<Element> getTargets(Element child) {
		Namespace ns = getProcessNamespace();
		List<Element> targets = new ArrayList<Element>();
		if (ns.equals(NAMESPACE_BPEL_1_1)) {
			targets.addAll(child.getChildren(TARGET_ELEMENT, ns));
		} else if (ns.equals(NAMESPACE_BPEL_2_0)) {
			Element target = child.getChild(TARGETS_ELEMENT, ns);
			if (target != null)
				targets.add(target);
		}
		return targets;
	}

}
