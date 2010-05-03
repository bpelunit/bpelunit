package org.bpelunit.framework.coverage;

import java.util.List;
import java.util.Map;

import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.coverage.exceptions.CoverageMeasurementException;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.model.ProcessUnderTest;

/**
 * Interface for integration of test coverage measurement
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public interface ICoverageMeasurementTool {

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

	/**
	 * Sets the encoder to be used
	 * 
	 * @param encoder
	 *            SOAP Encoder for decoding messages containing coverage
	 *            markings
	 */
	public abstract void setSOAPEncoder(ISOAPEncoder encoder);

	/**
	 * Set Path to WSDL
	 * 
	 * @param wsdl
	 *            WSDL-Description of the Coverage Logging Services
	 */
	public abstract void setPathToWSDL(String wsdl);

	/**
	 * Get Encoding Style
	 * 
	 * @return Coverage Messages' Encoding Style
	 */
	public abstract String getEncodingStyle();

	public abstract String prepareArchiveForCoverageMeasurement(
			String pathToArchive, ProcessUnderTest put, IBPELDeployer deployer)
			throws CoverageMeasurementException;

	public abstract void setErrorStatus(String message);

	/**
	 * Sets the current test case.
	 * 
	 * Afterwards it's possible to determine the coverage of each test.
	 * 
	 * @param testCase
	 *            Currently run test case
	 */
	public abstract void setCurrentTestCase(String testCase);

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