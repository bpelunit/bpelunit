package org.bpelunit.framework.coverage.annotation.metrics.fhcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Comment;
import org.jdom.Element;

public class FaultMetricHandler implements IMetricHandler {

	public static final String METRIC_NAME = "FaultHandlerMetric";

	public static final String FAULT_HANDLER_LABEL = "catchBlock";

	/**
	 * Generiert eine eindeutige Markierung.
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextMarker() {
		return FAULT_HANDLER_LABEL
				+ Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + (count++);
	}

	private MarkersRegisterForArchive markersRegistry;

	public FaultMetricHandler(MarkersRegisterForArchive markersRegistry) {

		this.markersRegistry = markersRegistry;
	}


	/**
	 * Fügt die Marker an den richtigen Stellen in
	 * BPEL-Process-Element ein (Instrumentierung). Anhand dieser Marker werden
	 * danach entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_element
	 * @throws BpelException 
	 */
	public void insertMarkersForMetric(List<Element> activities)
			throws BpelException {

		Iterator<Element> iter = activities.iterator();
		while (iter.hasNext()) {
			insertCoverageLabelsForCatchBlock(iter.next());
		}

	}

	private void insertCoverageLabelsForCatchBlock(Element catchBlock) {
		Element child = getFirstEnclosedActivity(catchBlock);
		if (!isSequence(child))
			child = ensureElementIsInSequence(child);

		String marker = getNextMarker();
		markersRegistry.registerMarker(marker);
		Comment comment = new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER
				+ marker);
		child.addContent(0, comment);
	}

}
