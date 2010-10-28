package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;

import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.StructuredActivities;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Element;

/**
 * Handler, der die Instrumentierung der
 * forEach-Aktivitäten für die Zweigabdeckung übernehmen.
 * 
 * @author Alex Salnikow
 */

public class ForEachHandler implements IStructuredActivityHandler {


	private MarkersRegisterForArchive markersRegistry;

	public ForEachHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}

	/**
	 * Fügt Markierungen, die später durch Invoke-Aufrufe protokolliert werden,
	 * um die Ausführung der Zweige zu erfassen.
	 * 
	 * @param forEach_activity
	 * @throws BpelException
	 */
	public void insertBranchMarkers(Element forEach_activity)
			throws BpelException {
			insertMarkerForSequenceBranches(forEach_activity);
	}

	/**
	 * 
	 * @param element
	 * @throws BpelException
	 */
	private void insertMarkerForSequenceBranches(Element element)
			throws BpelException {
		Element activity = element.getChild(StructuredActivities.SCOPE_ACTIVITY,
				BpelXMLTools.getProcessNamespace());
		if (activity == null)
			throw new BpelException(BpelException.MISSING_REQUIRED_ELEMENT
					+ "(Scope) in ForEach activity.");
		
		activity = getFirstEnclosedActivity(activity);
		
		if (activity == null)
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY
					+ " in ForEach activity.");

		markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(activity));
		markersRegistry.registerMarker(BranchMetricHandler.insertLabelAfterAllActivities(activity));

	}

}
