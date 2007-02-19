package coverage.instrumentation;

import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;

import coverage.instrumentation.exception.BpelException;
import coverage.instrumentation.exception.BpelVersionException;

/**
 * Dieses Interface wird von dem Handler implementiert, der dafür zuständig ist,
 * die Instrumentierung der BPEL-Datei zu starten und dabei nur die gewünschten
 * Metriken zu berücksichtigen.
 * 
 * @author Alex Salnikow
 * 
 */
public interface IMetricHandler {
	/**
	 * Die übergebene Metrik wird bei der Ausführung der BPEL erhoben:
	 * 
	 * @param metric
	 */
	public void addMetric(IMetric metric);

	/**
	 * Die Metrik wird bei der Ausführung der BPEL nicht erhoben.
	 * 
	 * @param metric
	 */
	public void remove(IMetric metric);

	/**
	 * Startet die Instrumentierung der BPEL-Datei.
	 * 
	 * @param file
	 * @throws JDOMException
	 * @throws IOException
	 * @throws BpelException
	 * @throws BpelVersionException
	 */
	public void startInstrumentation(File file) throws JDOMException,
			IOException, BpelException, BpelVersionException;

}
