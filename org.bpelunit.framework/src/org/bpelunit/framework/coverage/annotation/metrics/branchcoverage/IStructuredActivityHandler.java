package org.bpelunit.framework.coverage.annotation.metrics.branchcoverage;


import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Element;




/**
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
	public void insertBranchMarkers(Element structured_activity) throws BpelException;
}
