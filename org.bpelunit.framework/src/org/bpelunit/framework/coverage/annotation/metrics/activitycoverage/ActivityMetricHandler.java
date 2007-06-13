package org.bpelunit.framework.coverage.annotation.metrics.activitycoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.SOURCES_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.SOURCE_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.TARGETS_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.TARGET_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.encloseInSequence;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.ensureElementIsInSequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Klasse instrumentiert ein BPEL-Prozess, um die Abdeckung der BasicActivities
 * bei der Ausführung zu messen. Die einzelnen Aktivitäten, die bei der Messung
 * berücksichtigt werden müssen, müssen explizit angegeben werden ({@link #addBasicActivity(String)}).
 * 
 * @author Alex Salnikow
 */
public class ActivityMetricHandler implements IMetricHandler {

	private static int count = 0;

	private static HashMap<String, String> logging_before_activity;

	private HashMap<String, String> activities_to_respekt;

	private Logger logger;
	static {

		logging_before_activity = new HashMap<String, String>();
		logging_before_activity.put(BasicActivities.THROW_ACTIVITY,
				BasicActivities.THROW_ACTIVITY);
		logging_before_activity.put(BasicActivities.RETHROW_ACTIVITY,
				BasicActivities.RETHROW_ACTIVITY);
		logging_before_activity.put(BasicActivities.COMPENSATE_ACTIVITY,
				BasicActivities.COMPENSATE_ACTIVITY);
		logging_before_activity.put(BasicActivities.COMPENSATESCOPE_ACTIVITY,
				BasicActivities.COMPENSATESCOPE_ACTIVITY);
		logging_before_activity.put(BasicActivities.EXIT_ACTIVITY,
				BasicActivities.EXIT_ACTIVITY);
		logging_before_activity.put(BasicActivities.TERMINATE_ACTIVITY,BasicActivities.TERMINATE_ACTIVITY);
		logging_before_activity.put(BasicActivities.REPLY_ACTIVITY,BasicActivities.REPLY_ACTIVITY);
	}

	public ActivityMetricHandler() {
		activities_to_respekt = new HashMap<String, String>();
		logger = Logger.getLogger(getClass());
	}

	/**
	 * Diese Methode fügt die Markierungen in BPEL-Process-Element ein
	 * (Instrumentierung). Anhand dieser Markierungen werden danach
	 * entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_element
	 *            Prozess-Element der BPEL
	 */
	public void insertMarkersForMetric(List<Element> activities) {
		Element element;
		for (int i = 0; i < activities.size(); i++) {
			element = activities.get(i);
			Element sequence = null;
			respectTargetActivities(element, sequence);
//			respectSourceActivities(element, sequence);
			ensureElementIsInSequence(element);
			insertMarkerForActivity(element);
		}
	}



	private void respectSourceActivities(Element element, Element sequence) {
		List<Element> sourceElements = getSourceElements(element);
		if (sourceElements.size() > 0) {
			if (sequence == null) {
				sequence = encloseInSequence(element);
			}
			Element sourceElement;
			for (Iterator<Element> iter = sourceElements.iterator(); iter
					.hasNext();) {
				sourceElement = iter.next();
				sequence.addContent(0, sourceElement.detach());
			}

		}
	}

	private Element respectTargetActivities(Element element, Element sequence) {
		List<Element> targetElements = getTargetElements(element);
		if (targetElements.size() > 0) {
			sequence = encloseInSequence(element);
			Element targetElement;
			for (Iterator<Element> iter = targetElements.iterator(); iter
					.hasNext();) {
				targetElement = iter.next();
				sequence.addContent(0, targetElement.detach());
				logger.info("CoverageTool: Statementmetric. Link replaced: "
						+ targetElement.getName());
			}
		}
		return sequence;
	}

	private List<Element> getSourceElements(Element element) {
		// TODO nicht gleich eine neue Liste erzeugen
		List<Element> sourceElements = new ArrayList<Element>();
		Namespace bpelVersion = BpelXMLTools.getProcessNamespace();
		if (bpelVersion.equals(NAMESPACE_BPEL_2_0)) {
			Element sourceElement = element.getChild(SOURCES_ELEMENT,
					bpelVersion);
			if (sourceElement != null) {
				sourceElements.add(sourceElement);
			}
		} else if (bpelVersion.equals(NAMESPACE_BPEL_1_1)) {
			List<Element> list = element.getChildren(SOURCE_ELEMENT,
					bpelVersion);
			for (Iterator<Element> iter = list.iterator(); iter.hasNext();) {
				sourceElements.add(iter.next());
			}
		}
		return sourceElements;
	}

	private List<Element> getTargetElements(Element element) {
		// TODO nicht gleich eine neue Liste erzeugen
		List<Element> targetElements = new ArrayList<Element>();
		Namespace bpelVersion =BpelXMLTools.getProcessNamespace();
		if (bpelVersion.equals(NAMESPACE_BPEL_2_0)) {
			Element targetElement = element.getChild(TARGETS_ELEMENT,
					bpelVersion);
			if (targetElement != null) {
				targetElements.add(targetElement);
			}
		} else if (bpelVersion.equals(NAMESPACE_BPEL_1_1)) {
			List<Element> list = element.getChildren(TARGET_ELEMENT,
					bpelVersion);
			for (Iterator<Element> iter = list.iterator(); iter.hasNext();) {
				targetElements.add(iter.next());
			}
		}
		return targetElements;
	}

	/**
	 * Fügt eine Markierung für die Aktivität ein. Die Markierung wird entweder
	 * direkt vor oder nach der Aktivität eingefügt. Die Aktivitäten, die in
	 * {@link #logging_before_activity} eingetragen sind, müssen vor der
	 * Ausführung geloggt werden.
	 * 
	 * @param element
	 *            Aktivität
	 */
	private void insertMarkerForActivity(Element element) {
		Element parent = element.getParentElement();
		String element_name = element.getName();
		String marker = element_name + Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR
				+ (count++);
		LabelsRegistry.getInstance().addMarker(marker);
		Comment comment = new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER
				+ marker);
		int index = parent.indexOf(element);
		if (logging_before_activity.containsKey(element_name)) {
			parent.addContent(index, comment);
		} else {
			parent.addContent(index + 1, comment);
		}

	}



	public List<String> getPrefix4CovLabeles() {
		Set<String> activities = activities_to_respekt.keySet();
		List<String> labelsForSubMetrics = new ArrayList<String>(activities);
		return labelsForSubMetrics;
	}

}
