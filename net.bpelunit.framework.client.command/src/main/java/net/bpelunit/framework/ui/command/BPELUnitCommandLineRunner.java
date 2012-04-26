/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.ui.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.base.BPELUnitBaseRunner;
import net.bpelunit.framework.control.result.ITestResultListener;
import net.bpelunit.framework.control.result.XMLResultProducer;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.coverage.ICoverageMeasurementTool;
import net.bpelunit.framework.coverage.result.XMLCoverageResultProducer;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.exception.TestCaseNotFoundException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.data.XMLData;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.util.Console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.NullAppender;

/**
 * The command line runner for BPELUnit.
 * 
 * This class is intended to be run from the command line. Note that you need to have all libraries,
 * including those referenced in the extensions.xml extension file, on the class path. You also need
 * to set the BPELUNIT_HOME environment variable.
 * 
 * Invoke this class from the root directory of your test suite. All relative paths inside the test
 * suite document will be resolved from the current directory.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitCommandLineRunner extends BPELUnitBaseRunner implements ITestResultListener {

	private static final String PARAMETER_DETAILEDCOVERAGEFILE = "d";
	private static final String PARAMETER_COVERAGEFILE = "c";
	private static final String PARAMETER_LOGFILE = "l";
	private static final String PARAMETER_XMLFILE = "x";
	private static final String PARAMETER_VERBOSE = "v";
	private static final int MAX_LINE_LENGTH = 800;
	private final Logger logger = Logger.getLogger(getClass());
	private Console console;
	
	private boolean saveCoverageDetails = false;
	private boolean verbose;
	private String xmlFileName;
	private String logFileName;
	private String covFileName;
	private PrintWriter screen;
	private File testSuiteFile;
	private List<String> testCaseNames;
	private Options options;

	public BPELUnitCommandLineRunner(String[] args) {
		this(new Console(), args);
	}
		
	public BPELUnitCommandLineRunner(Console consoleToUse, String[] args) {
		this.console = consoleToUse;
		this.screen = console.getScreen();
	
		this.createOptions();
		this.parseOptionsFromCommandLine(args);
	}

	@SuppressWarnings("static-access")
	private final void createOptions() {
		options = new Options();
		
		options.addOption(PARAMETER_VERBOSE, false, "adds detailled output");
		options.addOption(OptionBuilder
                .withDescription("write XML output to the given file")
                .hasArg()
                .withArgName("FILE")
                .create(PARAMETER_XMLFILE) );
		options.addOption(OptionBuilder
				.withDescription("write logging messages to the given file")
				.hasArg()
				.withArgName("FILE")
				.create(PARAMETER_LOGFILE) );
		options.addOption(OptionBuilder
				.withDescription("write test coverage to the given file")
				.hasArg()
				.withArgName("FILE")
				.create(PARAMETER_COVERAGEFILE) );
		options.addOption(OptionBuilder
				.withDescription("write detailled test coverage to the given file")
				.hasArg()
				.withArgName("FILE")
				.create(PARAMETER_DETAILEDCOVERAGEFILE) );
	}
	
	@SuppressWarnings("unchecked")
	private final void parseOptionsFromCommandLine(String[] args) {
		saveCoverageDetails = false;

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			
			verifyCommandLineArguments(cmd);
			
			verbose = cmd.hasOption(PARAMETER_VERBOSE);
			xmlFileName = trimEqualsSignFromStart(cmd.getOptionValue(PARAMETER_XMLFILE));
			logFileName = trimEqualsSignFromStart(cmd.getOptionValue(PARAMETER_LOGFILE));
			if(cmd.hasOption(PARAMETER_COVERAGEFILE)) {
				covFileName = trimEqualsSignFromStart(cmd.getOptionValue(PARAMETER_COVERAGEFILE)); 
			}
			if(cmd.hasOption(PARAMETER_DETAILEDCOVERAGEFILE)) {
				covFileName = trimEqualsSignFromStart(cmd.getOptionValue(PARAMETER_DETAILEDCOVERAGEFILE));
				saveCoverageDetails = true;
			}
			
			ArrayList<String> remainingOptions = new ArrayList<String>(cmd.getArgList());
			setAndValidateTestSuiteFileName(remainingOptions.remove(0));
			testCaseNames = remainingOptions;
		} catch (ParseException e) {
			showHelpAndExit();
		}
	}

	private void setAndValidateTestSuiteFileName(String testSuiteFileName) {
		testSuiteFile = new File(testSuiteFileName);
		if (!testSuiteFile.exists()) {
			abort("Cannot find test suite file with path " + testSuiteFile);
		}
	}

	private String trimEqualsSignFromStart(String optionValue) {
		if(optionValue == null) {
			return null;
		}
		
		if(!optionValue.startsWith("=")) {
			return optionValue;
		}
		
		return optionValue.substring(1);
	}

	private void verifyCommandLineArguments(CommandLine cmd) {
		if(cmd.hasOption(PARAMETER_COVERAGEFILE) && cmd.hasOption(PARAMETER_DETAILEDCOVERAGEFILE)) {
			abort(String.format("-%s and -%s cannot be specified at the same time!", PARAMETER_COVERAGEFILE, PARAMETER_DETAILEDCOVERAGEFILE));
		}
		
		if(cmd.getArgList().size() == 0) {
			showHelpAndExit();
		}
	}
	
	/**
	 * Main method, to be started by the user.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new BPELUnitCommandLineRunner(args).run();
	}
	
	// ************************* Implementation *********************

	private void abort(String message) {
		abort(message, null);
	}
	
	private void abort(String message, Exception e) {
		screen.println(message);
		if(e != null) {
			screen.println("Description: " + e.getMessage());
			logger.error("Error while writing coverage information", e);
		}
		console.exit(1);
	}

	private void showHelpAndExit() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(
			"bpelunit [options] testsuite.bpts [testcase...]", 
			options
		);
		console.exit(1);
	}

	void run() {
		screen.println("BPELUnit Command Line Runner");
		screen.println("----------------------------");
		
		try {			
			Map<String, String> options;
			if (covFileName != null) {
				options = new HashMap<String, String>();
				options.put(BPELUnitRunner.MEASURE_COVERAGE, "true");
			} else {
				options = BPELUnitConstants.NULL_OPTIONS;
			}
			
			initialize(options);

			screen.println("Loading Test Suite...");
			TestSuite suite = loadTestSuite(testSuiteFile);

			// Test if all the test names are ok
			for (String testCaseName : testCaseNames) {
				if (!suite.hasTestCase(testCaseName)) {
					abort("Suite does not have test case with name " + testCaseName);
				}
			}

			screen.println("Test Suite sucessfully loaded.");
			screen.println("Deploying Services...");

			try {
				suite.setUp();
			} catch (DeploymentException e) {
				try {
					suite.shutDown();
				} catch (DeploymentException e1) {
					// do nothing
				}
				abort("A deployment error occurred when deploying services for this test suite", e);
			}

			screen.println("Services successfully deployed.");
			screen.println("Running Test Cases...");

			suite.addResultListener(this);
			if (testCaseNames.size() > 0) {
				try {
					suite.setFilter(testCaseNames);
				} catch (TestCaseNotFoundException e1) {
					// tested before, should not happen.
				}
			}

			suite.run();

			suite.removeResultListener(this);
			screen.println("Done running test cases.");

			if (xmlFileName != null) {
				try {
					XMLResultProducer.writeXML(new FileOutputStream(xmlFileName), suite);
				} catch (Exception e) {
					abort("Sorry - could not write XML output to " + xmlFileName, e);
				}
			}
			
			screen.println("Undeploying services...");

			try {
				suite.shutDown();
			} catch (DeploymentException e) {
				abort("An undeployment error occurred when undeploying services for this test suite", e);
			}
			if (covFileName != null) {
				try {
					ICoverageMeasurementTool tool = BPELUnitRunner.getCoverageMeasurmentTool();
					if (tool != null) {
						XMLCoverageResultProducer.writeResult(
								new FileOutputStream(covFileName),
								tool.getStatistics(), tool.getErrorStatus(),
								saveCoverageDetails);
					}
				} catch (IOException e) {
					abort("Sorry - could not write XML coverage output to "
									+ covFileName, e);
				}
			}
			screen.println("Services undeployed.");
			screen.println("Shutting down. Have a nice day!");

		} catch (ConfigurationException e) {
			abort("A configuration error was encountered when initializing BPELUnit", e);
		} catch (SpecificationException e) {
			abort("An error was encountered when reading the test suite specification", e);
		} finally {
			BPELUnitRunner.setCoverageMeasurmentTool(null);
		}
	}

	public void testCaseEnded(TestCase test) {
		String status = "ended";
		String error = null;
		if (test.getStatus().isError()) {
			status = "had an error";
			error = test.getStatus().getMessage();
		}
		if (test.getStatus().isFailure()) {
			status = "failed";
			error = test.getStatus().getMessage();
		}
		if (test.getStatus().isAborted()) {
			status = "was aborted";
		}
		if (test.getStatus().isPassed()) {
			status = "passed";
		}
		screen.println("Test Case " + status + ": " + test.getName() + "."
				+ ((error != null) ? error : ""));
	}

	public void testCaseStarted(TestCase test) {
		if (verbose) {
			screen.println("Test Case started: " + test.getName() + ".\n");
		}
	}

	public void progress(ITestArtefact test) {
		if (verbose && test instanceof PartnerTrack) {
			screen.println(createReadableOutput(test, false));
		}
	}

	private String createReadableOutput(ITestArtefact artefact,
			boolean recursive) {
		return createReadableOutputInternal(artefact, recursive, "");
	}

	private String createReadableOutputInternal(ITestArtefact artefact,
			boolean recursive, String inline) {
		StringBuffer output = new StringBuffer();
		output.append(inline + "<Artefact \"" + artefact.getName() + "\">\n");
		String internalInline = inline + "  ";
		output.append(internalInline + "Status: "
				+ artefact.getStatus().toString() + "\n");
		if (recursive) {
			for (ITestArtefact child : artefact.getChildren()) {
				if (artefact instanceof XMLData) {
					XMLData sd = (XMLData) artefact;
					output.append(internalInline
							+ sd.getName()
							+ " : "
							+ StringUtils.abbreviate(BPELUnitUtil
									.removeSpaceLineBreaks(sd.getXmlData()),
									MAX_LINE_LENGTH) + "\n");
				} else {
					output.append(createReadableOutputInternal(child,
							recursive, internalInline));
				}
			}
		}
		output.append(inline + "</Artefact \"" + artefact.getName() + "\">\n");
		return output.toString();
	}

	public void configureLogging() throws ConfigurationException {
		Logger.getRootLogger().removeAllAppenders();
		if (logFileName != null) {
			try {
				Logger.getRootLogger().addAppender(
						new FileAppender(new PatternLayout(), logFileName));
			} catch (IOException e) {
				screen.println("Error trying to write to log file. Disabling logging...");
			}
			Logger.getRootLogger().setLevel(Level.INFO);
		} else {
			Logger.getRootLogger().addAppender(new NullAppender());
		}
	}

	/**
	 * Only for tests
	 */
	boolean getCoverageDetails() {
		return saveCoverageDetails;
	}

	/**
	 * Only for tests
	 */
	File getTestSuiteFile() {
		return testSuiteFile;
	}

	/**
	 * Only for tests
	 */
	List<String> getTestCaseNames() {
		return new ArrayList<String>(testCaseNames);
	}

	/**
	 * Only for tests
	 */
	boolean isSaveCoverageDetails() {
		return saveCoverageDetails;
	}

	/**
	 * Only for tests
	 */
	boolean isVerbose() {
		return verbose;
	}

	/**
	 * Only for tests
	 */
	String getXmlFileName() {
		return xmlFileName;
	}

	/**
	 * Only for tests
	 */
	String getLogFileName() {
		return logFileName;
	}

	/**
	 * Only for tests
	 */
	String getCovFileName() {
		return covFileName;
	}
}
