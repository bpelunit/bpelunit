package org.bpelunit.framework.coverage.annotation.metrics.activitycoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.SOURCES_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.SOURCE_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.TARGETS_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.TARGET_ELEMENT;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.count;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.encloseInSequence;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.ensureElementIsInSequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Klasse instrumentiert einen BPEL-Prozess, um die Abdeckung der
 * BasicActivities bei der Ausf�hrung zu messen. Die einzelnen Aktivit�ten, die
 * bei der Messung ber�cksichtigt werden m�ssen, m�ssen explizit angegeben
 * werden.
 * 
 * @author Alex Salnikow
 */
public class ActivityMetricHandler implements IMetricHandler {

	public static int targetscount;

	private Logger logger = Logger.getLogger(getClass());

	private MarkersRegisterForArchive markersRegistry;

	public ActivityMetricHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
		logger = Logger.getLogger(getClass());
	}

	/**
	 * Diese Methode f�gt die Markierungen in BPEL-Process-Element ein
	 * (Instrumentierung). Anhand dieser Markierungen werden danach
	 * entsprechende Invoke aufrufe generiert und dadurch die Ausf�hrung
	 * bestimmter Aktivit�ten geloggt.
	 * 
	 * @param activities
	 *            Prozess-Element der BPEL
	 * 
	 */
	// FIXME Possible bug, need tests for: sequence is not used but
	// seems like it is supposed to be an out parameter which it is
	// NOT
	public void insertMarkersForMetric(List<Element> activities) {
		for (Element activity : activities) {
			Element sequence = null;
			respectTargetActivities(activity, sequence);
			// respectSourceActivities(element, sequence);
			ensureElementIsInSequence(activity);
			insertMarkerForActivity(activity);
		}
	}

	private void respectSourceActivities(Element element, Element sequence) {
		List<Element> sourceElements = getSourceElements(element);
		if (sourceElements.size() > 0) {
			if (sequence == null)
				sequence = encloseInSequence(element);
			Element sourceElement;
			for (Iterator<Element> iter = sourceElements.iterator(); iter
					.hasNext();) {
				sourceElement = iter.next();
				sequence.addContent(0, sourceElement.detach());
			}

		}
	}

	/**
	 * Schliesst die Aktivit�t mit der Coverage Marke mit einer Sequence um,
	 * wenn die Aktivit�t Ziel eines Synchronisationslinks ist.
	 * 
	 * @param element
	 * @param sequence
	 */
	private void respectTargetActivities(Element element, Element sequence) {
		List<Element> targetElements = getTargetElements(element);
		if (targetElements.size() > 0) {
			sequence = encloseInSequence(element);
			Element targetElement;
			for (Iterator<Element> iter = targetElements.iterator(); iter
					.hasNext();) {
				targetElement = iter.next();
				sequence.addContent(0, targetElement.detach());
			}
		}
	}

	private List<Element> getSourceElements(Element element) {
		List<Element> sourceElements = new ArrayList<Element>();
		Namespace bpelVersion = BpelXMLTools.getProcessNamespace();
		if (bpelVersion.equals(NAMESPACE_BPEL_2_0)) {
			Element sourceElement = element.getChild(SOURCES_ELEMENT,
					bpelVersion);
			if (sourceElement != null)
				sourceElements.add(sourceElement);
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
		List<Element> targetElements = new ArrayList<Element>();
		Namespace bpelVersion = BpelXMLTools.getProcessNamespace();
		if (bpelVersion.equals(NAMESPACE_BPEL_2_0)) {
			Element targetElement = element.getChild(TARGETS_ELEMENT,
					bpelVersion);
			if (targetElement != null)
				targetElements.add(targetElement);
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
	 * F�gt eine Markierung f�r die Aktivit�t ein. Die Markierung wird entweder
	 * direkt vor oder nach der Aktivit�t eingef�gt. Die Aktivit�ten, die in
	 * eingetragen sind, m�ssen vor der Ausf�hrung geloggt werden.
	 * 
	 * @param element
	 *            Aktivit�t
	 */
	private void insertMarkerForActivity(Element element) {
		Element parent = element.getParentElement();
		String element_name = element.getName();
		String marker = element_name
				+ Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + (count++);
		markersRegistry.registerMarker(marker);
		Comment comment = new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER
				+ marker);
		int index = parent.indexOf(element);
		if (element_name.equals(BasicActivities.RECEIVE_ACTIVITY))
			parent.addContent(index + 1, comment);
		else
			parent.addContent(index, comment);

	}

}
