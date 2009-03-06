package org.bpelunit.framework.coverage;

import java.util.List;
import java.util.Map;

import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.coverage.exceptions.CoverageMeasurementException;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.exception.ConfigurationException;

/*
 * Die Schnittstelle zum Einbinden der Testabdeckungsmessung.
 * 
 * @author Alex Salnikow
 *
 */
/**
 * Interface for integration of test coverage measurement
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public interface ICoverageMeasurementTool {

	/*
	 * Methode zum Konfigurieren der Metrik, die erhoben werden sollen.
	 * 
	 * @param configMap Map (Metrikname,Liste), wobei die Liste entweder
	 * Zusatzinformationen enthält oder zu null evaluiert. Bei
	 * Aktivitätsabdeckung sind die Basisaktivitäten, die berücksichtigt werden
	 * sollen, in der Liste eingetragen. @throws ConfigurationException
	 */
	// TODO Nochmal checken
	/**
	 * Configuration method for the metric to be covered
	 * 
	 * @param configMap
	 *            Map (metric name,list), where the list contains either
	 *            additional information or evaluates to null.
	 * @throws ConfigurationException
	 */
	public abstract void configureMetrics(Map<String, List<String>> configMap)
			throws ConfigurationException;

	/*
	 * 
	 * @param encoder SOAPEncoder für die Dekodierung der Nachrichten mit
	 * Coverage-Marken
	 */
	/**
	 * Sets the encoder to be used
	 * 
	 * @param encoder
	 *            SOAP Encoder for decoding messages containing coverage
	 *            markings
	 */
	public abstract void setSOAPEncoder(ISOAPEncoder encoder);

	/*
	 * 
	 * @param wsdl WSDL-Beschreibung des Coverage Logging Services
	 */
	/**
	 * Set Path to WSDL
	 * 
	 * @param wsdl
	 *            WSDL-Description of the Coverage Logging Services
	 */
	public abstract void setPathToWSDL(String wsdl);

	/*
	 * 
	 * @return Encoding Style der Coverage-Nachrichten
	 */
	/**
	 * Get Encoding Style
	 * 
	 * @return Coverage Messages' Encoding Style
	 */
	public abstract String getEncodingStyle();

	/**
	 * Präpariert das Deployment-Archiv für die Messung der Abdeckung beim
	 * Testen des BPEL-Prozesses. Die Instrumentierung wird auf einer Kopie
	 * durchgeführt.
	 * 
	 * @param pathToArchive
	 * @param archiveFile
	 * @param deployer
	 * @return Name der präparierten Archivdatei
	 * @throws CoverageMeasurementException
	 */
	// TODO Instrumentierung?
	public abstract String prepareArchiveForCoverageMeasurement(
			String pathToArchive, String archiveFile, IBPELDeployer deployer)
			throws CoverageMeasurementException;

	public abstract void setErrorStatus(String message);

	/*
	 * Setzt den Testfall, der gerade ausgeführt wird. Dadurch ist es möglich,
	 * die Testabdeckung von jedem Testfalls zu bestimmen.
	 * 
	 * @param testCase Testfall, der gerade ausgeführt wird.
	 */
	/**
	 * Sets the current test case.
	 * 
	 * Afterwards it's possible to determine the coverage of each test.
	 * 
	 * @param testCase
	 *            Currently run test case
	 */
	public abstract void setCurrentTestCase(String testCase);

	/*
	 * Empfängt SOAP-Nachrichten mit Coverage Marken während der Testausführung
	 * 
	 * @param body Nachricht mit Coverage-Marken
	 */
	/**
	 * Receives SOAP messages (including coverage markings) while testing.
	 * 
	 * @param message
	 *            message with coverage markings
	 */
	public abstract void putMessage(String message);

	/*
	 * Generiert Statistiken (nach dem Testlauf) für alle BPEL-Dateien, die im
	 * Archive sind.
	 * 
	 * @return Statistiken (nach dem Testlauf) für alle BPEL-Dateien, die im
	 * Archive sind.
	 */
	/**
	 * Generates post-run statistics for all bpel files in the archive
	 * 
	 * @return Statistics (post-run) for all bpel files
	 */
	public abstract List<IFileStatistic> getStatistics();

	/*
	 * 
	 * @return gibt Informationen in einem Fehlerfall
	 */
	/**
	 * Gives feedback about errors
	 * 
	 * @return feedback about errors
	 */
	public abstract String getErrorStatus();

}