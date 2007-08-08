package org.bpelunit.framework.coverage.annotation.metrics;

import java.util.List;

import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Element;

/**
 * Die Schnittstelle ist für Handler vorgesehen, die die Instrumentierung für die Metriken übernehmen.
 * @author Alex
 *
 */
public interface IMetricHandler {

	/**
	 * Fügt die Marker an den richtigen Stellen in
	 * BPEL-Process-Element ein (Instrumentierung). Anhand dieser Marker werden
	 * danach entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_elements
	 * @throws BpelException 
	 */
	public void insertMarkersForMetric(List<Element> process_elements) throws BpelException;
	
}
