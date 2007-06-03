package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.impl;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.isActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetricHandler;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.IStructuredActivityHandler;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;


/**
 * Die Klasse ist für das Einfügen der Markierungen in der Sequence-Aktivität
 * verantwortlich, die für die Messung der Zweigabdeckung verwendet werden.
 * 
 * @author Alex Salnikow
 */
public class SequenceHandler implements IStructuredActivityHandler {

	/**
	 * Fügt Markierungen in Sequence-Elemente ein, die später, um die Ausführung
	 * der Zweige zu erfassen, durch Invoke-Aufrufe protokolliert werden.
	 * 
	 * @param sequence
	 */
	public void insertMarkerForBranchCoverage(Element sequence) {
		List<Element> children = sequence.getContent(new ElementFilter(getProcessNamespace()));
		Element child;
		List<Element> activities=new ArrayList<Element>();
		for (Iterator<Element> iter = children.iterator(); iter.hasNext();) {
			activities.add(iter.next());
		}
		
		Element previousActivity = null;
		for (int i = 0; i < activities.size(); i++) {
			child = activities.get(i);
			if (isActivity(child)) {
				if (previousActivity != null) {
					{
						// BranchMetric.insertLabelAfterActivity(previousActivity);
						System.out.println("!!!!Vorgänger "+previousActivity.getName());
						BranchMetricHandler.insertLabelBevorActivity(child);
					}
				}
				previousActivity = child;
			}
		}
	}
}
