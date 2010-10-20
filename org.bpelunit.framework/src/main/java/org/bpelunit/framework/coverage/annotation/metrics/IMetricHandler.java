package org.bpelunit.framework.coverage.annotation.metrics;

import java.util.List;

import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Element;

/*
 * Die Schnittstelle ist für Handler vorgesehen, die die Instrumentierung für die Metriken übernehmen.
 * @author Alex
 *
 */
/**
 * This interface is for handlers instrumenting the metrics
 * 
 * @author Alex, Ronald Becher
 */
public interface IMetricHandler {

	/*
	 * Fügt die Marker an den richtigen Stellen in BPEL-Process-Element ein
	 * (Instrumentierung). Anhand dieser Marker werden danach entsprechende
	 * Invoke aufrufe generiert und dadurch die Ausführung bestimmter
	 * Aktivitäten geloggt.
	 * 
	 * @param process_elements
	 * 
	 * @throws BpelException
	 */
	/**
	 * Inserts the markers at the correct place in a bpel process element.
	 * 
	 * <br />According to those markers the associated invoke calls will be
	 * generated, logging the processing of certain activities
	 * 
	 * @param processElements
	 * @throws BpelException
	 */
	public void insertMarkersForMetric(List<Element> processElements)
			throws BpelException;

}
