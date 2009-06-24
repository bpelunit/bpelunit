package org.bpelunit.framework.coverage;

import java.util.List;
import java.util.Map;

import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IDeployment;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.coverage.exceptions.CoverageMeasurementException;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.model.ProcessUnderTest;

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
	 * Zusatzinformationen enth�lt oder zu null evaluiert. Bei
	 * Aktivit�tsabdeckung sind die Basisaktivit�ten, die ber�cksichtigt werden
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
	 * @param encoder SOAPEncoder f�r die Dekodierung der Nachrichten mit
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
	 * Pr�pariert das Deployment-Archiv f�r die Messung der Abdeckung beim
	 * Testen des BPEL-Prozesses. Die Instrumentierung wird auf einer Kopie
	 * durchgef�hrt.
	 * 
	 * @param pathToArchive
	 * @param archiveFile
	 * @param deployer
	 * @return Name der pr�parierten Archivdatei
	 * @throws CoverageMeasurementException
	 */
	// TODO Instrumentierung?
	/*
	 * public abstract String prepareArchiveForCoverageMeasurement( String
	 * pathToArchive, String archiveFile, IBPELDeployer deployer) throws
	 * CoverageMeasurementException;
	 */

	public abstract String prepareArchiveForCoverageMeasurement(
			String pathToArchive, ProcessUnderTest put, IBPELDeployer deployer)
			throws CoverageMeasurementException;

	public abstract void setErrorStatus(String message);

	/*
	 * Setzt den Testfall, der gerade ausgef�hrt wird. Dadurch ist es m�glich,
	 * die Testabdeckung von jedem Testfalls zu bestimmen.
	 * 
	 * @param testCase Testfall, der gerade ausgef�hrt wird.
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
	 * Empf�ngt SOAP-Nachrichten mit Coverage Marken w�hrend der Testausf�hrung
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
	 * Generiert Statistiken (nach dem Testlauf) f�r alle BPEL-Dateien, die im
	 * Archive sind.
	 * 
	 * @return Statistiken (nach dem Testlauf) f�r alle BPEL-Dateien, die im
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