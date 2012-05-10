package net.bpelunit.framework.coverage;

import java.util.List;
import java.util.Map;

import net.bpelunit.framework.control.ext.IBPELDeployer;
import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.coverage.exceptions.CoverageMeasurementException;
import net.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.model.ProcessUnderTest;

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
	void configureMetrics(Map<String, List<String>> configMap)
			throws ConfigurationException;

	/**
	 * Sets the encoder to be used
	 * 
	 * @param encoder
	 *            SOAP Encoder for decoding messages containing coverage
	 *            markings
	 */
	void setSOAPEncoder(ISOAPEncoder encoder);

	/**
	 * Set Path to WSDL
	 * 
	 * @param wsdl
	 *            WSDL-Description of the Coverage Logging Services
	 */
	void setPathToWSDL(String wsdl);

	/**
	 * Get Encoding Style
	 * 
	 * @return Coverage Messages' Encoding Style
	 */
	String getEncodingStyle();

	String prepareArchiveForCoverageMeasurement(
			String pathToArchive, ProcessUnderTest put, IBPELDeployer deployer)
			throws CoverageMeasurementException;

	void setErrorStatus(String message);

	/**
	 * Sets the current test case.
	 * 
	 * Afterwards it's possible to determine the coverage of each test.
	 * 
	 * @param testCase
	 *            Currently run test case
	 */
	void setCurrentTestCase(String testCase);

	/**
	 * Receives SOAP messages (including coverage markings) while testing.
	 * 
	 * @param message
	 *            message with coverage markings
	 */
	void putMessage(String message);

	/**
	 * Generates post-run statistics for all bpel files in the archive
	 * 
	 * @return Statistics (post-run) for all bpel files
	 */
	List<IFileStatistic> getStatistics();

	/**
	 * Gives feedback about errors
	 * 
	 * @return feedback about errors
	 */
	String getErrorStatus();

}