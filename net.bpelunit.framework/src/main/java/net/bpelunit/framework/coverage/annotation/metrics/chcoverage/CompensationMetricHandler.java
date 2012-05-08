package net.bpelunit.framework.coverage.annotation.metrics.chcoverage;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.Iterator;
import java.util.List;

import net.bpelunit.framework.coverage.annotation.Instrumenter;
import net.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Comment;
import org.jdom.Element;

public class CompensationMetricHandler implements IMetricHandler {

	public static final String METRIC_NAME = "CompensationHandlerMetric";

	public static final String COMPENS_HANDLER_LABEL = "compHandler";

	/**
	 * Generiert eine eindeutige Markierung.
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextMarker() {
		return COMPENS_HANDLER_LABEL
				+ Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + BpelXMLTools.incrementCounter();
	}

	private MarkersRegisterForArchive markersRegistry;

	public CompensationMetricHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}


	/**
	 * Fügt die Marker an den richtigen Stellen in
	 * BPEL-Process-Element ein (Instrumentierung). Anhand dieser Marker werden
	 * danach entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_elements
	 * @throws BpelException 
	 */
	public void insertMarkersForMetric(List<Element> process_elements)
			throws BpelException {
		Element handler;
		for (Iterator<Element> iter = process_elements.iterator(); iter.hasNext();) {
			handler = iter.next();
			Element activity = getFirstEnclosedActivity(handler);
			if (!activity.getName().equals(
					StructuredActivities.SEQUENCE_ACTIVITY)) {
				activity = encloseInSequence(activity);
			}
			String marker = getNextMarker();
			markersRegistry.registerMarker(marker);
			Comment comment = new Comment(
					Instrumenter.COVERAGE_LABEL_IDENTIFIER + marker);
			activity.addContent(0, comment);
		}
	}

}
