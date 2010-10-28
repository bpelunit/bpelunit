package net.bpelunit.framework.coverage.annotation.metrics.branchcoverage;

import net.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Element;

/**
 * Die Schnittstelle für die Handler, die die Instrumentierung der
 * Strukturierten Aktivitäten für die Zweigabdeckung übernehmen.
 * 
 * @author Alex Salnikow
 */
public interface IStructuredActivityHandler {
	/**
	 * Fügt Markierungen, die später durch Invoke-Aufrufe protokolliert werden,
	 * um die Ausführung der Zweige zu erfassen.
	 * 
	 * @param structured_activity
	 * @throws BpelException
	 */
	public void insertBranchMarkers(Element structured_activity)
			throws BpelException;
}
