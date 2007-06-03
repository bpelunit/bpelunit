/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IHeaderProcessor;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.util.BPELUnitConstants;
import org.bpelunit.framework.control.util.BPELUnitUtil;
import org.bpelunit.framework.coverage.CoverageMeasurementTool;
import org.bpelunit.framework.coverage.receiver.CoverageMessageReceiver;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.TestSuite;

/**
 * <p>
 * The BPELUnit Runner lies at the heart of BPELUnit. To execute BPELUnit tests,
 * the runner must be subclasses, then instantiated and run.
 * </p>
 * <p>
 * BPELUnit Core options: <table>
 * <tr>
 * <td><b>Option</b></td>
 * <td><b>Values</b></td>
 * </tr>
 * <tr>
 * <td>SKIP_UNKNOWN_EXTENSIONS</td>
 * <td>If set to "true", this option causes BPELUnit to ignore extensions with
 * classes it cannot find in the current CLASSPATH. If set to "false" (default),
 * BPELUnit fails when encountering extensions with unknown classes.</td>
 * </tr>
 * <tr>
 * <td>GLOBAL_TIMEOUT</td>
 * <td>The timeout in milliseconds (ms) for every send/receive operation. For
 * example, a synchronous send will wait for this time before it fails when not
 * receiving an answer. The default is 25000 (25 seconds)</td>
 * </tr>
 * <tr>
 * <td>HALT_ON_ERROR</td>
 * <td>If set to "true", this option causes BPELUnit to stop after the first
 * test case error. The default is false.</td>
 * </tr>
 * <tr>
 * <td>HALT_ON_FAILURE</td>
 * <td>If set to "true", this option causes BPELUnit to stop after the first
 * test case failure. The default is false.</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class BPELUnitRunner {

	/*
	 * Options:
	 */
	public static final String SKIP_UNKNOWN_EXTENSIONS = "SKIP_UNKNOWN_EXTENSIONS";

	public static final String GLOBAL_TIMEOUT = "GLOBAL_TIMEOUT";

	public static final String HALT_ON_ERROR = "HALT_ON_ERROR";

	public static final String HALT_ON_FAILURE = "HALT_ON_FAILURE";

	public static final String MEASURE_COVERAGE = "MEASURE_COVERAGE";

	/*
	 * Default values of options:
	 */
	private static boolean fSkipUnknownExtensions = false;

	private static int fGlobalTimeout = BPELUnitConstants.TIMEOUT;

	private static boolean fHaltOnError = false;

	private static boolean fHaltOnFailure = false;

	private static boolean fMeasureCoverage = false;

	// HIER
	public static CoverageMeasurementTool coverageMeasurmentTool = null;

	/**
	 * Indicates whether the runner has been properly initialized
	 */
	private boolean fInitialized;

	public BPELUnitRunner() {
		fInitialized = false;
	}

	/**
	 * Initializes BPELUnit. This is a template method, calling various other
	 * methods which are to be or may be implemented by subclasses.
	 * 
	 * @param options
	 *            options (see class javadoc)
	 * 
	 * @throws ConfigurationException
	 *             home directory not found, problem while reading extensions or
	 *             finding classes linked in extensions, or no xml parser found.
	 */
	public void initialize(Map<String, String> options)
			throws ConfigurationException {

		// Revert state
		fInitialized = false;

		fSkipUnknownExtensions = false;
		String skipUnknown = options.get(SKIP_UNKNOWN_EXTENSIONS);
		if ((skipUnknown != null) && (skipUnknown.equalsIgnoreCase("true")))
			fSkipUnknownExtensions = true;

		fHaltOnError = false;
		String haltOnErr = options.get(HALT_ON_ERROR);
		if ((haltOnErr != null) && (haltOnErr.equalsIgnoreCase("true")))
			fHaltOnError = true;

		fHaltOnFailure = false;
		String haltOnFail = options.get(HALT_ON_FAILURE);
		if ((haltOnFail != null) && (haltOnFail.equalsIgnoreCase("true")))
			fHaltOnFailure = true;

		// HIER
		CoverageMessageReceiver.getInstance().initialize();
		coverageMeasurmentTool = null;
		fMeasureCoverage = false;
		String measureCoverage = options.get(MEASURE_COVERAGE);
		if ((measureCoverage != null)
				&& (measureCoverage.equalsIgnoreCase("true"))) {
			fMeasureCoverage = true;
			System.out.println("MEASURECOVERAGE=TRUE");
		}else{
			System.out.println("MEASURECOVERAGE=FALSE");
		}

		fGlobalTimeout = BPELUnitConstants.TIMEOUT;
		String timeout = options.get(GLOBAL_TIMEOUT);
		if (timeout != null)
			try {
				fGlobalTimeout = Integer.parseInt(timeout);
			} catch (NumberFormatException e) {
				throw new ConfigurationException(
						"Global timeout value in options is not an integer: "
								+ timeout);
			}

		configureInit();

		configureLogging();
		initializeXMLParser();

		configureExtensions();
		configureDeployers();

		// HIER

		if (fMeasureCoverage) {
			try {
				configureCoverageTool();
			} catch (ConfigurationException e) {
				LabelsRegistry.getInstance().addInfo(e.getMessage());
			}
		}

		// Okay
		fInitialized = true;
	}

	/**
	 * Called by initialize() before all other configuration methods.
	 * 
	 * @throws ConfigurationException
	 */
	public abstract void configureInit() throws ConfigurationException;

	/**
	 * Called by initialize() to configure the logging system.
	 * 
	 * @throws ConfigurationException
	 */
	public abstract void configureLogging() throws ConfigurationException;

	/**
	 * Called by initialize() to configure the extensions
	 * 
	 * @throws ConfigurationException
	 */
	public abstract void configureExtensions() throws ConfigurationException;

	/**
	 * Called by initialize() to configure the deployers
	 * 
	 * @throws ConfigurationException
	 */
	public abstract void configureDeployers() throws ConfigurationException;

	/**
	 * Called by initialize() to configure the measurement of coverage.
	 * 
	 */
	// HIER
	public abstract void configureCoverageTool() throws ConfigurationException;

	/**
	 * 
	 * Main entry point into the BPELUnit testing functionality. This method
	 * loads a complete TestSuite specification from a .bpts file, which is then
	 * ready to be run by a client.
	 * 
	 * @param suiteFile
	 *            absolute or relative (to the **current** working directory)
	 *            path to the .bpts file.
	 * @return the test suite
	 * 
	 * @throws SpecificationException
	 *             A problem with the test suite file or xml
	 */
	public TestSuite loadTestSuite(File suiteFile)
			throws SpecificationException {

		// Check setup
		if (!fInitialized)
			throw new SpecificationException(
					"BPELUnitCore was not properly initialized. Please call initialize() first.");

		return new SpecificationLoader(this).loadTestSuite(suiteFile);
	}

	/**
	 * Creates a new deployer instance for the given deployer type
	 * 
	 * @param type
	 *            name of the deployer
	 * @return the deployer
	 * @throws SpecificationException
	 */
	public abstract IBPELDeployer createNewDeployer(String type)
			throws SpecificationException;

	/**
	 * Creates a new header processor instance for the given name
	 * 
	 * @param name
	 *            name of the header processor
	 * @return the header processor
	 * @throws SpecificationException
	 */
	public abstract IHeaderProcessor createNewHeaderProcessor(String name)
			throws SpecificationException;

	/**
	 * Creates a new SOAP encoder for the given style and encoding
	 * 
	 * @param styleEncoding
	 *            style and encoding
	 * @return the soap encoder
	 * @throws SpecificationException
	 */
	public abstract ISOAPEncoder createNewSOAPEncoder(String styleEncoding)
			throws SpecificationException;

	/**
	 * Returns the configured timeout
	 * 
	 * @return timeout in milliseconds
	 */
	public static int getTimeout() {
		return fGlobalTimeout;
	}

	/**
	 * Returns whether unknown extensions should be skipped
	 * 
	 * @return true if skip
	 */
	public static boolean isSkipUnknownExtensions() {
		return fSkipUnknownExtensions;
	}

	/**
	 * Returns whether BPELUnit halts on error
	 * 
	 * @return
	 */
	public static boolean isHaltOnError() {
		return fHaltOnError;
	}

	/**
	 * Returns whether BPELUnit halts on failure
	 * 
	 * @return
	 */
	public static boolean isHaltOnFailure() {
		return fHaltOnFailure;
	}

	public static boolean isMeasureTestCoverage() {
		return fMeasureCoverage;
	}

	// ******************** internals ******************

	private void initializeXMLParser() throws ConfigurationException {

		try {
			BPELUnitUtil.initializeParsing();
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(
					"Could not initialize XML Parser Component.", e);
		}
	}

}
