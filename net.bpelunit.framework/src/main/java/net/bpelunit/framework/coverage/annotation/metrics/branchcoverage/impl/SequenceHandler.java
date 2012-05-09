package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.isActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import net.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import net.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

/**
 * Handler, der die Instrumentierung der
 * sequence-Aktivitäten für die Zweigabdeckung übernehmen.
 * 
 * @author Alex Salnikow
 */
public class SequenceHandler implements IStructuredActivityHandler {

	private MarkersRegisterForArchive markersRegistry;

	public SequenceHandler(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}

	/**
	 * Fügt Markierungen, die später durch Invoke-Aufrufe protokolliert werden,
	 * um die Ausführung der Zweige zu erfassen.
	 * 
	 * @param structuredActivity
	 */
	public void insertBranchMarkers(Element structuredActivity) {
		List<Element> children = structuredActivity.getContent(new ElementFilter(
				getProcessNamespace()));
		Element child;
		List<Element> activities = new ArrayList<Element>();
		for (Iterator<Element> iter = children.iterator(); iter.hasNext();) {
			activities.add(iter.next());
		}

		Element previousActivity = null;
		for (int i = 0; i < activities.size(); i++) {
			child = activities.get(i);
			if (isActivity(child)) {
				if (previousActivity != null) {
					markersRegistry.registerMarker(BranchMetricHandler
							.insertLabelBevorActivity(child));
				}
					
				previousActivity = child;
			}
		}
	}
}
