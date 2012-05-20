package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.coverage.annotation.Instrumenter;
import net.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.FlowHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.ForEachHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.IfHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.PickHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.RepeatUntilHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.SequenceHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.SwitchHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl.WhileHandler;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;

/*
 * Klasse instrumentiert ein BPEL-Prozess, um die Zweigabdeckung bei der
 * Ausführung zu messen.
 * 
 * @author Alex Salnikow
 */
/**
 * Instrumentats a bpel process for branch coverage measuring
 * 
 * @author Alex Salnikow, Ronald Becher
 */
public class BranchMetricHandler implements IMetricHandler {

	public static final String METRIC_NAME = "Branchmetric";

	public static final String BRANCH_LABEL = "branch";

	/*
	 * Generiert eine eindeutige Markierung.
	 * 
	 * @return eindeutige Markierung
	 */
	/**
	 * Generates an unique marker 
	 * 
	 * @return eindeutige Markierung
	 */
	private static String getNextMarker() {
		String marker = BRANCH_LABEL
				+ Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + BpelXMLTools.incrementCounter();
		return marker;
	}

	/**
	 * Fügt eine Markierung vor allen Aktivitäten ein. Wenn es notwendig ist,
	 * dann wird die Markierung ind die Aktivitäten in ein Sequence-Element
	 * eingeschlossen.
	 * 
	 * @param activity
	 */
	public static String insertLabelBevorAllActivities(Element activity) {
		Element realTarget = respectTargetsOfLinks(activity);
		if (!isSequence(realTarget)) {
			realTarget = ensureElementIsInSequence(realTarget);
		}
		String marker = getNextMarker();
		realTarget.addContent(0, new Comment(
				Instrumenter.COVERAGE_LABEL_IDENTIFIER + marker));
		return marker;
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
			return sequence;
		}
		
		return activity;
	}

	/**
	 * Fügt eine Markierung nach allen Aktivitäten ein. Wenn es notwendig ist,
	 * dann wird die Markierung ind die Aktivitäten in ein Sequence-Element
	 * eingeschlossen.
	 * 
	 * @param activity
	 */
	public static String insertLabelAfterAllActivities(Element activity) {
		Element realActivity = activity;
		
		if (!isSequence(realActivity)) {
			realActivity = ensureElementIsInSequence(realActivity);
		}
		String marker = getNextMarker();
		realActivity.addContent(new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER
				+ marker));
		return marker;
	}

	/**
	 * 
	 * @param activity
	 *            muss innerhalb Sequence sein
	 */
	public static String insertLabelAfterActivity(Element activity) {
		Element parent = activity.getParentElement();
		String marker = getNextMarker();
		parent.addContent(parent.indexOf(activity) + 1, new Comment(
				Instrumenter.COVERAGE_LABEL_IDENTIFIER + marker));
		return marker;
	}

	/**
	 * 
	 * @param activity
	 *            muss innerhalb Sequence sein
	 */
	public static String insertLabelBevorActivity(Element activity) {
		Element realTarget = respectTargetsOfLinks(activity);
		Element parent = realTarget.getParentElement();
		String marker = getNextMarker();
		parent.addContent(parent.indexOf(realTarget), new Comment(
				Instrumenter.COVERAGE_LABEL_IDENTIFIER + marker));
		return marker;
	}
	
	public static List<Element> getTargets(Element child) {
		Namespace ns = getProcessNamespace();
		List<Element> targets = new ArrayList<Element>();
		if (ns.equals(NAMESPACE_BPEL_1_1)) {
			targets.addAll(child.getChildren(TARGET_ELEMENT, ns));
		} else if (ns.equals(NAMESPACE_BPEL_2_0)) {
			Element target = child.getChild(TARGETS_ELEMENT, ns);
			if (target != null) {
				targets.add(target);
			}
		}
		return targets;
	}

	private Map<String, IStructuredActivityHandler> structuredActivityHandler = new HashMap<String, IStructuredActivityHandler>();

	public BranchMetricHandler(MarkersRegisterForArchive markersRegistry) {
		structuredActivityHandler.put(StructuredActivities.FLOW_ACTIVITY,
				new FlowHandler(markersRegistry));
		structuredActivityHandler.put(StructuredActivities.SEQUENCE_ACTIVITY,
				new SequenceHandler(markersRegistry));
		structuredActivityHandler.put(StructuredActivities.IF_ACTIVITY,
				new IfHandler(markersRegistry));
		structuredActivityHandler.put(StructuredActivities.WHILE_ACTIVITY,
				new WhileHandler(markersRegistry));
		structuredActivityHandler.put(
				StructuredActivities.REPEATUNTIL_ACTIVITY,
				new RepeatUntilHandler(markersRegistry));
		structuredActivityHandler.put(StructuredActivities.FOREACH_ACTIVITY,
				new ForEachHandler(markersRegistry));
		structuredActivityHandler.put(StructuredActivities.PICK_ACTIVITY,
				new PickHandler(markersRegistry));
		structuredActivityHandler.put(StructuredActivities.SWITCH_ACTIVITY,
				new SwitchHandler(markersRegistry));
	}

	/**
	 * * Fügt die Marker an den richtigen Stellen in BPEL-Process-Element ein
	 * (Instrumentierung). Anhand dieser Marker werden danach entsprechende
	 * Invoke aufrufe generiert und dadurch die Aktivierung der Zweige geloggt.
	 * 
	 * @param activities alle Strukturierten Aktivitäten des Prozesses
	 * @throws BpelException
	 */
	public void insertMarkersForMetric(List<Element> activities)
			throws BpelException {
		Element nextElement;
		for (Iterator<Element> iter = activities.iterator(); iter.hasNext();) {
			nextElement = iter.next();
			String nextElementName = nextElement.getName();
			if (structuredActivityHandler.containsKey(nextElementName)) {
				// Delegiert die Instrumentierung
				structuredActivityHandler.get(nextElementName)
						.insertBranchMarkers(nextElement);
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
