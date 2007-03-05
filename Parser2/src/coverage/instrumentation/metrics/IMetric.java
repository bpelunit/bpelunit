package coverage.instrumentation.metrics;

import org.jdom.Element;

import exception.BpelException;

/**
 * Dieses Interface wird von den Metriken implementiert.
 * 
 * @author Alex Salnikow
 * 
 */
public interface IMetric {

	/**
	 * Diese Methode fügt die Marker an den richtigen Stellen in
	 * BPEL-Process-Element ein (Instrumentierung). Anhand dieser Marker werden
	 * danach entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_element
	 * @throws BpelException 
	 */
	public void insertMarker(Element process_element) throws BpelException;

}
