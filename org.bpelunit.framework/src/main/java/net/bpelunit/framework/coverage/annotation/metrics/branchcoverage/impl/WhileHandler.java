package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;

import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Element;


/**
 * Handler, der die Instrumentierung der
 * while-Aktivitäten für die Zweigabdeckung übernehmen.
 * 
 * @author Alex Salnikow
 */

public class WhileHandler implements IStructuredActivityHandler {

	private MarkersRegisterForArchive markersRegistry;

	public WhileHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}

	/**
	 * Fügt Markierungen, die später durch Invoke-Aufrufe protokolliert werden,
	 * um die Ausführung der Zweige zu erfassen.
	 * 
	 * @param structured_activity
	 * @throws BpelException
	 */
	public void insertBranchMarkers(Element structured_activity)
			throws BpelException {
		Element activity = getFirstEnclosedActivity(structured_activity);
		if (activity == null)
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		
		markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(activity));
		markersRegistry.registerMarker(BranchMetricHandler.insertLabelAfterAllActivities(activity));
	}

}
