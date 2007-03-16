package coverage.instrumentation.metrics;

import java.io.IOException;
import java.util.List;

import org.jdom.JDOMException;

import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
import de.schlichtherle.io.File;
import exception.BpelException;
import exception.BpelVersionException;

/**
 * Dieses Interface wird von dem Handler implementiert, der dafür zuständig ist,
 * die Instrumentierung der BPEL-Datei zu starten und dabei nur die gewünschten
 * Metriken zu berücksichtigen.
 * 
 * @author Alex Salnikow
 * 
 */
public interface IMetricHandler {

	public static final String STATEMENT_METRIC = Statementmetric.METRIC_NAME;

	public static final String BRANCH_METRIC = BranchMetric.METRIC_NAME;
	
	public static final String MARKER_IDENTIFIRE = "@marker";

	/**
	 * Die übergebene Metrik wird bei der Ausführung der BPEL erhoben:
	 * 
	 * @param metricName
	 */
	public void addMetric(IMetric metric);

	/**
	 * Die Metrik wird bei der Ausführung der BPEL nicht erhoben.
	 * 
	 * @param metricName
	 */
	public void remove(String metricName);

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

	/**
	 * 
	 * @param metricName
	 * @return Metrik
	 */
	public IMetric getMetric(String metricName);

}
