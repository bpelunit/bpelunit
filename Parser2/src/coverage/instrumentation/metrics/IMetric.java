package coverage.instrumentation.metrics;

import org.jdom.Element;

import coverage.exception.BpelException;


/**
 * Dieses Interface wird von den Metriken implementiert.
 * 
 * @author Alex Salnikow
 * 
 */
public interface IMetric {
	


	public static final String COVERAGE_LABEL_IDENTIFIER = "@coverageLabel";
	public static final String DYNAMIC_COVERAGE_LABEL_IDENTIFIER = "@ForEachCoverageLabel";

	/**
	 * Diese Methode fügt die Marker an den richtigen Stellen in
	 * BPEL-Process-Element ein (Instrumentierung). Anhand dieser Marker werden
	 * danach entsprechende Invoke aufrufe generiert und dadurch die Ausführung
	 * bestimmter Aktivitäten geloggt.
	 * 
	 * @param process_element
	 * @throws BpelException 
	 */
	public void insertCoverageLabels(Element process_element) throws BpelException;
	
	public String getName();

}
