package net.bpelunit.framework.coverage.annotation.metrics.fhcoverage;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.Iterator;
import java.util.List;

import net.bpelunit.framework.coverage.annotation.Instrumenter;
import net.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Comment;
import org.jdom.Element;

public class FaultMetricHandler implements IMetricHandler {

	public static final String METRIC_NAME = "FaultHandlerMetric";

	public static final String FAULT_HANDLER_LABEL = "catchBlock";

	/**
	 * Generates unique marker
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getNextMarker() {
		return FAULT_HANDLER_LABEL
				+ Instrumenter.COVERAGE_LABEL_INNER_SEPARATOR + BpelXMLTools.incrementCounter();
	}

	private MarkersRegisterForArchive markersRegistry;

	public FaultMetricHandler(MarkersRegisterForArchive markersRegistry) {

		this.markersRegistry = markersRegistry;
	}


	/**
	 * @see net.bpelunit.framework.coverage.annotation.metrics.IMetricHandler#insertMarkersForMetric(java.util.List)
	 */
	public void insertMarkersForMetric(List<Element> processElements)
			throws BpelException {

		Iterator<Element> iter = processElements.iterator();
		while (iter.hasNext()) {
			insertCoverageLabelsForCatchBlock(iter.next());
		}

	}

	private void insertCoverageLabelsForCatchBlock(Element catchBlock) {
		Element child = getFirstEnclosedActivity(catchBlock);
		if (!isSequence(child)) {
			child = ensureElementIsInSequence(child);
		}

		String marker = getNextMarker();
		markersRegistry.registerMarker(marker);
		Comment comment = new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER
				+ marker);
		child.addContent(0, comment);
	}

}
