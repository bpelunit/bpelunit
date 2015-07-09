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

import net.bpelunit.framework.base.BPELUnitBaseRunner;
import net.bpelunit.framework.control.result.XMLResultProducer;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.exception.TestCaseNotFoundException;
import net.bpelunit.framework.model.test.ITestResultListener;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.data.XMLData;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.util.Console;

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
 * This class is intended to be run from the command line. Note that you need to
 * have all libraries, including those referenced in the extensions.xml
 * extension file, on the class path. You also need to set the BPELUNIT_HOME
 * environment variable.
 * 
 * Invoke this class from the root directory of your test suite. All relative
 * paths inside the test suite document will be resolved from the current
 * directory.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitCommandLineRunner extends BPELUnitBaseRunner implements
		ITestResultListener {

	private static final String PARAMETER_DETAILEDCOVERAGEFILE = "d"; //$NON-NLS-1$
	private static final String PARAMETER_COVERAGEFILE = "c"; //$NON-NLS-1$
	private static final String PARAMETER_LOGFILE = "l"; //$NON-NLS-1$
	private static final String PARAMETER_XMLFILE = "x"; //$NON-NLS-1$
	private static final String PARAMETER_VERBOSE = "v"; //$NON-NLS-1$
	private static final String PARAMETER_TIMEOUT = "t"; //$NON-NLS-1$
	private static final int MAX_LINE_LENGTH = 800;
	private final Logger logger = Logger.getLogger(getClass());
	private Console console;

	private boolean saveCoverageDetails = false;
	private boolean verbose;
	private String xmlFileName;
	private String logFileName;
	private PrintWriter screen;
	private File testSuiteFile;
	private List<String> testCaseNames;
	private Options options;
	private long timeout;

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

		options.addOption(
				PARAMETER_VERBOSE,
				false,
				Messages.getString("BPELUnitCommandLineRunner.PARAMTER_DESCRIPTION_VERBOSE")); //$NON-NLS-1$
		options.addOption(OptionBuilder
				.withDescription(
						Messages.getString("BPELUnitCommandLineRunner.PARAMETER_DESCRIPTION_XMLFILE")) //$NON-NLS-1$
				.hasArg().withArgName("FILE").create(PARAMETER_XMLFILE));
		options.addOption(OptionBuilder
				.withDescription(
						Messages.getString("BPELUnitCommandLineRunner.PARAMETER_DESCRIPTION_LOGFILE")) //$NON-NLS-1$
				.hasArg().withArgName("FILE").create(PARAMETER_LOGFILE));
		options.addOption(OptionBuilder
				.withDescription(
						Messages.getString("BPELUnitCommandLineRunner.PARAMETER_DESCRIPTION_COVERAGEFILE")) //$NON-NLS-1$
				.hasArg().withArgName("FILE").create(PARAMETER_COVERAGEFILE));
		options.addOption(OptionBuilder
				.withDescription(
						Messages.getString("BPELUnitCommandLineRunner.PARAMTER_DESCRIPTION_DETAILEDCOVERAGEFILE")) //$NON-NLS-1$
				.hasArg().withArgName("FILE")
				.create(PARAMETER_DETAILEDCOVERAGEFILE));
		options.addOption(OptionBuilder
				.withDescription(
						Messages.getString("BPELUnitCommandLineRunner.PARAMTER_DESCRIPTION_TIMEOUT")) //$NON-NLS-1$
				.hasArg().withArgName("TIMEOUT").create(PARAMETER_TIMEOUT));
	}

	@SuppressWarnings("unchecked")
	private final void parseOptionsFromCommandLine(String[] args) {
		saveCoverageDetails = false;

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			verifyCommandLineArguments(cmd);

			verbose = cmd.hasOption(PARAMETER_VERBOSE);
			xmlFileName = trimEqualsSignFromStart(cmd
					.getOptionValue(PARAMETER_XMLFILE));
			logFileName = trimEqualsSignFromStart(cmd
					.getOptionValue(PARAMETER_LOGFILE));

			if (cmd.hasOption(PARAMETER_TIMEOUT)) {
				timeout = Long.parseLong(cmd.getOptionValue(PARAMETER_TIMEOUT));
			}

			ArrayList<String> remainingOptions = new ArrayList<String>(
					cmd.getArgList());
			setAndValidateTestSuiteFileName(remainingOptions.remove(0));
			testCaseNames = remainingOptions;
		} catch (ParseException e) {
			showHelpAndExit();
		}
	}

	private void setAndValidateTestSuiteFileName(String testSuiteFileName) {
		testSuiteFile = new File(testSuiteFileName);
		if (!testSuiteFile.exists()) {
			abort(String
					.format(Messages
							.getString("BPELUnitCommandLineRunner.MSG_ERR_TESTSUITE_FILE_NOT_EXISTING"),
							testSuiteFile)); //$NON-NLS-1$
		}
	}

	private String trimEqualsSignFromStart(String optionValue) {
		if (optionValue == null) {
			return null;
		}

		if (!optionValue.startsWith("=")) { //$NON-NLS-1$
			return optionValue;
		}

		return optionValue.substring(1);
	}

	private void verifyCommandLineArguments(CommandLine cmd) {
		if (cmd.hasOption(PARAMETER_COVERAGEFILE)
				&& cmd.hasOption(PARAMETER_DETAILEDCOVERAGEFILE)) {
			abort(String
					.format(Messages
							.getString("BPELUnitCommandLineRunner.MSG_ERR_PARAMETER_COVERAGE_DETAILEDCOVERAGE_ARE_EXCLUSIVE"), PARAMETER_COVERAGEFILE, PARAMETER_DETAILEDCOVERAGEFILE)); //$NON-NLS-1$
		}

		if (cmd.getArgList().size() == 0) {
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
		if (e != null) {
			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MESSAGE_DESCRIPTION") + e.getMessage()); //$NON-NLS-1$
			logger.error(
					Messages.getString("BPELUnitCommandLineRunner.MSG_ERROR_CANNOT_WRITE_COVERAGE_INFORMATION"), e); //$NON-NLS-1$
		}
		console.exit(1);
	}

	private void showHelpAndExit() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(
				Messages.getString("BPELUnitCommandLineRunner.MSG_HELP_USAGE"), //$NON-NLS-1$
				options);
		console.exit(1);
	}

	void run() {
		String bpelUnitRunner = Messages
				.getString("BPELUnitCommandLineRunner.MSG_TITLE_BPELUNIT_COMMANDLINE_RUNNER");
		screen.println(bpelUnitRunner); //$NON-NLS-1$
		screen.println(StringUtils.repeat("-", bpelUnitRunner.length())); //$NON-NLS-1$

		try {
			Map<String, String> options;
			if (timeout == 0) {
				options = BPELUnitConstants.NULL_OPTIONS;
			} else {
				options = new HashMap<String, String>();
				options.put(GLOBAL_TIMEOUT, "" + timeout);
			}

			initialize(options);

			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_LOADING_TESTSUITE")); //$NON-NLS-1$
			TestSuite suite = loadTestSuite(testSuiteFile);

			// Test if all the test names are ok
			for (String testCaseName : testCaseNames) {
				if (!suite.hasTestCase(testCaseName)) {
					abort(String
							.format(Messages
									.getString("BPELUnitCommandLineRunner.MSG_ERROR_UNKNOWN_TESTCASE"), testCaseName)); //$NON-NLS-1$
				}
			}

			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_TESTSUITE_LOADED")); //$NON-NLS-1$
			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_DEPLOYING_SERVICES")); //$NON-NLS-1$

			try {
				suite.setUp();
			} catch (DeploymentException e) {
				try {
					suite.shutDown();
				} catch (DeploymentException e1) {
					// do nothing
				}
				abort(Messages
						.getString("BPELUnitCommandLineRunner.MSG_ERROR_DEPLOYMENT_ERROR"), e); //$NON-NLS-1$
			}

			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_DEPLOYMENT_DONE")); //$NON-NLS-1$
			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_RUNNING_TEST_CASES")); //$NON-NLS-1$

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
			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_TESTCASES_FINISHED")); //$NON-NLS-1$

			if (xmlFileName != null) {
				try {
					XMLResultProducer.writeXML(
							new FileOutputStream(xmlFileName), suite);
				} catch (Exception e) {
					abort(String
							.format(Messages
									.getString("BPELUnitCommandLineRunner.MSG_ERROR_ERROR_WRITING_XML_FILE"), xmlFileName), e); //$NON-NLS-1$
				}
			}

			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_UNDEPLOY")); //$NON-NLS-1$

			try {
				suite.shutDown();
			} catch (DeploymentException e) {
				abort(Messages
						.getString("BPELUnitCommandLineRunner.MSG_ERROR_UNDEPLOY"), e); //$NON-NLS-1$
			}
			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_UNDEPLOYED")); //$NON-NLS-1$
			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_BYE")); //$NON-NLS-1$
		} catch (ConfigurationException e) {
			abort(Messages
					.getString("BPELUnitCommandLineRunner.MSG_ERROR_COFIGURATION_ERROR"), e); //$NON-NLS-1$
		} catch (SpecificationException e) {
			abort(Messages
					.getString("BPELUnitCommandLineRunner.MSG_ERROR_BPTS_ERROR"), e); //$NON-NLS-1$
		}
	}

	public void testCaseEnded(TestCase test) {
		String status = "ended"; //$NON-NLS-1$
		String error = null;
		if (test.getStatus().isError()) {
			status = "had an error"; //$NON-NLS-1$
			error = test.getStatus().getMessage();
		}
		if (test.getStatus().isFailure()) {
			status = "failed"; //$NON-NLS-1$
			error = test.getStatus().getMessage();
		}
		if (test.getStatus().isAborted()) {
			status = "was aborted"; //$NON-NLS-1$
		}
		if (test.getStatus().isPassed()) {
			status = "passed"; //$NON-NLS-1$
		}
		screen.println("Test Case " + status + ": " + test.getName() + "." //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ((error != null) ? error : "")); //$NON-NLS-1$
	}

	public void testCaseStarted(TestCase test) {
		if (verbose) {
			screen.println(Messages
					.getString("BPELUnitCommandLineRunner.MSG_PROGRESS_TESTCASE_STARTED") + test.getName() + ".\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void progress(ITestArtefact test) {
		if (verbose && test instanceof PartnerTrack) {
			screen.println(createReadableOutput(test, false));
		}
	}

	private String createReadableOutput(ITestArtefact artefact,
			boolean recursive) {
		return createReadableOutputInternal(artefact, recursive, ""); //$NON-NLS-1$
	}

	private String createReadableOutputInternal(ITestArtefact artefact,
			boolean recursive, String inline) {
		StringBuffer output = new StringBuffer();
		output.append(inline + "<Artefact \"" + artefact.getName() + "\">\n"); //$NON-NLS-1$ //$NON-NLS-2$
		String internalInline = inline + "  "; //$NON-NLS-1$
		output.append(internalInline + "Status: " //$NON-NLS-1$
				+ artefact.getStatus().toString() + "\n"); //$NON-NLS-1$
		if (recursive) {
			for (ITestArtefact child : artefact.getChildren()) {
				if (artefact instanceof XMLData) {
					XMLData sd = (XMLData) artefact;
					output.append(internalInline
							+ sd.getName()
							+ " : " //$NON-NLS-1$
							+ StringUtils.abbreviate(BPELUnitUtil
									.removeSpaceLineBreaks(sd.getXmlData()),
									MAX_LINE_LENGTH) + "\n"); //$NON-NLS-1$
				} else {
					output.append(createReadableOutputInternal(child,
							recursive, internalInline));
				}
			}
		}
		output.append(inline + "</Artefact \"" + artefact.getName() + "\">\n"); //$NON-NLS-1$ //$NON-NLS-2$
		return output.toString();
	}

	public void configureLogging() throws ConfigurationException {
		Logger.getRootLogger().removeAllAppenders();
		if (logFileName != null) {
			try {
				Logger.getRootLogger().addAppender(
						new FileAppender(new PatternLayout(), logFileName));
			} catch (IOException e) {
				screen.println(Messages
						.getString("BPELUnitCommandLineRunner.MSG_ERROR_LOGFILE")); //$NON-NLS-1$
			}
			Logger.getRootLogger().setLevel(Level.INFO);
		} else {
			Logger.getRootLogger().addAppender(new NullAppender());
		}
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
}
