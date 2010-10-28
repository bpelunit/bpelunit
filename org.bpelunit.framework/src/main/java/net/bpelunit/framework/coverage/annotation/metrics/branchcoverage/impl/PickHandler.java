package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getFirstEnclosedActivity;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;

import java.util.List;

import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.jdom.Element;


/**
 * Handler, der die Instrumentierung der
 * pick-Aktivitäten für die Zweigabdeckung übernehmen.
 * 
 * @author Alex Salnikow
 */
public class PickHandler implements IStructuredActivityHandler {

	private static final String ON_MESSAGE = "onMessage";

	private static final String ON_ALARM = "onAlarm";

	private MarkersRegisterForArchive markersRegistry;

	public PickHandler(MarkersRegisterForArchive markersRegistry) {
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
		identifyBranches(structured_activity, ON_MESSAGE);
		identifyBranches(structured_activity, ON_ALARM);
	}

	private void identifyBranches(Element element, String name)
			throws BpelException {
		List children = element.getChildren(name, getProcessNamespace());
		Element child;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			child = getFirstEnclosedActivity(child);
			if (child == null) {
				throw new BpelException(BpelException.MISSING_REQUIRED_ACTIVITY);
			}
			markersRegistry.registerMarker(BranchMetricHandler.insertLabelBevorAllActivities(child));
		}
	}
}
