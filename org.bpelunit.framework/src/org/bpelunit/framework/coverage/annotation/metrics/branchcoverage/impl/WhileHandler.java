package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;

import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Element;



public class WhileHandler implements IStructuredActivityHandler {

	private MarkersRegisterForArchive markersRegistry;

	public WhileHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}

	public void insertBranchMarkers(Element element)
			throws BpelException {
		Element activity = getFirstEnclosedActivity(element);
		if (activity == null) {
			throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
		}
		markersRegistry.addMarker(BranchMetricHandler.insertLabelBevorAllActivities(activity));
		markersRegistry.addMarker(BranchMetricHandler.insertLabelAfterAllActivities(activity));
	}

}
