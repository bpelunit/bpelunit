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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.NullAppender;
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

	private static final int MAX_LINE_LENGTH = 800;
	private static final PrintWriter CONSOLE = System.console().writer();
	
	private static boolean fCoverageDetails = false;
	private boolean fVerbose;
	private String fXmlFileName;
	private String fLogFileName;
	private String fCovFileName;

	public BPELUnitCommandLineRunner(boolean verbose, String xmlFileName, String logFileName, String covFileName) {
		fVerbose= verbose;
		fXmlFileName= xmlFileName;
		fLogFileName= logFileName;
		fCovFileName = covFileName;
	}

	/**
	 * Main method, to be started by the user.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CONSOLE.println("BPELUnit Command Line Runner");
		CONSOLE.println("---------------------------------------------");

		int index = 0;
		boolean verbose = false;
		String logFileName = null;
		String xmlFileName = null;
		String coverageFileName = null;
		fCoverageDetails = false;

		if (args.length == 0) {
			abort();
		}

		// ********** Check for extended output flag ***********

		if ("-v".equals(args[0])) {
			index++;
			verbose= true;
		}

		if (args.length < index + 1) {
			abort();
		}

		// ************* Check for XML output flag **************

		if (args[index].startsWith("-x")) {
			xmlFileName= findFileName(args[index], "xml");
			index++;
		}

		if (args.length < index + 1) {
			abort();
		}

		// ************* Check for LOG output flag **************

		if (args[index].startsWith("-l")) {
			logFileName= findFileName(args[index], "log");
			index++;
		}
		
		// ************* Check for coverage output flag **************

		if (args[index].startsWith("-c")) {
			coverageFileName = findFileName(args[index], "xml");
			index++;
		} else if(args[index].startsWith("-d")){
			coverageFileName = findFileName(args[index], "xml");
			fCoverageDetails = true;
			index++;
		}

		if (args.length < index + 1) {
			abort();
		}


		// ************** Check actual test suite ***************

		File testSuiteFile = new File(args[index]);
		if (!testSuiteFile.exists()) {
			abort("Cannot find test suite file with path " + testSuiteFile);
		}

		// ************** Check test case names ***************

		List<String> testCaseNames = new ArrayList<String>();
		for (int i= index + 1; i < args.length; i++) {
			testCaseNames.add(args[i]);
		}

		new BPELUnitCommandLineRunner(verbose, xmlFileName, logFileName,
				coverageFileName).run(testSuiteFile, testCaseNames);
	}

	// ************************* Implementation *********************

	private static void abort(String string) {
		CONSOLE.println(string);
		System.exit(1);
	}

	private static void abort() {
		CONSOLE.println("Usage: bpelunit [-v] [-x=filename] [-l=filename] [-c=filename]|[-d=filename] testsuite.bpts [testcase1] [testcase2]");
		CONSOLE.println("		-v adds detailed output");
		CONSOLE.println("		-x=filename send XML output into the given file");
		CONSOLE.println("		-l=filename send logging messages into the given file");
		CONSOLE.println("		-c=filename send test coverage into the given file");
		CONSOLE.println("		-d=filename send detailed test coverage into the given file");
		System.exit(1);
	}

	private void run(File testSuiteFile, List<String> testCaseNames) {

		try {			
			Map<String, String> options;
			if (fCovFileName != null) {
				options = new HashMap<String, String>();
				options.put(BPELUnitRunner.MEASURE_COVERAGE, "true");
			} else {
				options = BPELUnitConstants.NULL_OPTIONS;
			}
			
			initialize(options);

			CONSOLE.println("Loading Test Suite...");
			TestSuite suite= loadTestSuite(testSuiteFile);

			// Test if all the test names are ok
			for (String string : testCaseNames) {
				if (!suite.hasTestCase(string))
					abort("Suite does not have test case with name " + string);
			}

			CONSOLE.println("Test Suite sucessfully loaded.");
			CONSOLE.println("Deploying Services...");

			try {
				suite.setUp();
			} catch (DeploymentException e) {
				CONSOLE.println("A deployment error occurred when deploying services for this test suite:");
				CONSOLE.println("  " + e.getMessage());
				try {
					suite.shutDown();
				} catch (DeploymentException e1) {
					// do nothing
				}
				System.exit(1);
			}

			CONSOLE.println("Services successfully deployed.");
			CONSOLE.println("Running Test Cases...");

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
			CONSOLE.println("Done running test cases.");

			if (fXmlFileName != null) {
				try {
					XMLResultProducer.writeXML(new FileOutputStream(fXmlFileName), suite);
				} catch (Exception e) {
					CONSOLE.println("Sorry - could not write XML output to " + fXmlFileName + ": " + e.getMessage());
				}
			}
			
			
			CONSOLE.println("Undeploying services...");

			try {
				suite.shutDown();
			} catch (DeploymentException e) {
				CONSOLE.println("An undeployment error occurred when undeploying services for this test suite:");
				CONSOLE.println("  " + e.getMessage());
				System.exit(1);
			}
			if (fCovFileName != null) {
				try {
					ICoverageMeasurementTool tool = BPELUnitRunner.getCoverageMeasurmentTool();
					if(tool!=null)
					XMLCoverageResultProducer.writeResult(new FileOutputStream(
							fCovFileName),tool.getStatistics(),tool.getErrorStatus(),fCoverageDetails);
				} catch (IOException e) {

					System.out
							.println("Sorry - could not write XML coverage output to "
									+ fCovFileName + ": " + e.getMessage());
					e.printStackTrace();
				}
			}
			CONSOLE.println("Services undeployed.");
			CONSOLE.println("Shutting down. Have a nice day!");

		} catch (ConfigurationException e) {
			CONSOLE.println("A configuration error was encountered when initializing BPELUnit:");
			CONSOLE.println(" " + e.getMessage());
			System.exit(1);
		} catch (SpecificationException e) {
			CONSOLE.println("An error was encountered when reading the test suite specification:");
			CONSOLE.println(" " + e.getMessage());
			System.exit(1);
		} finally{
			BPELUnitRunner.setCoverageMeasurmentTool(null);
		}

	}

	public void testCaseEnded(TestCase test) {
		String status= "ended";
		String error= null;
		if (test.getStatus().isError()) {
			status= "had an error";
			error= test.getStatus().getMessage();
		}
		if (test.getStatus().isFailure()) {
			status= "failed";
			error= test.getStatus().getMessage();
		}
		if (test.getStatus().isAborted())
			status= "was aborted";
		if (test.getStatus().isPassed())
			status= "passed";
		CONSOLE.println("Test Case " + status + ": " + test.getName() + "." + ( (error != null) ? error : ""));

	}

	public void testCaseStarted(TestCase test) {
		if (fVerbose) {
			CONSOLE.println("Test Case started: " + test.getName() + ".\n");
		}
	}

	public void progress(ITestArtefact test) {
		if (fVerbose && test instanceof PartnerTrack) {
			CONSOLE.println(createReadableOutput(test, false));
		}
	}

	private static String createReadableOutput(ITestArtefact artefact, boolean recursive) {
		return createReadableOutputInternal(artefact, recursive, "");
	}

	private static String createReadableOutputInternal(ITestArtefact artefact, boolean recursive, String inline) {
		StringBuffer output= new StringBuffer();
		output.append(inline + "<Artefact \"" + artefact.getName() + "\">\n");
		String internalInline= inline + "  ";
		output.append(internalInline + "Status: " + artefact.getStatus().toString() + "\n");
		if (recursive) {
			for (ITestArtefact child : artefact.getChildren()) {
				if (artefact instanceof XMLData) {
					XMLData sd= (XMLData) artefact;
					output.append(internalInline + sd.getName() + " : "
							+ StringUtils.abbreviate(BPELUnitUtil.removeSpaceLineBreaks(sd.getXmlData()), MAX_LINE_LENGTH) + "\n");
				} else
					output.append(createReadableOutputInternal(child, recursive, internalInline));
			}
		}
		output.append(inline + "</Artefact \"" + artefact.getName() + "\">\n");
		return output.toString();
	}

	public void configureLogging() throws ConfigurationException {
		Logger.getRootLogger().removeAllAppenders();
		if (fLogFileName != null) {
			try {
				Logger.getRootLogger().addAppender(new FileAppender(new PatternLayout(), fLogFileName));
			} catch (IOException e) {
				CONSOLE.println("Error trying to write to log file. Disabling logging...");
			}
			Logger.getRootLogger().setLevel(Level.INFO);
		} else {
			Logger.getRootLogger().addAppender(new NullAppender());
		}
	}

	private static String findFileName(String string, String type) {
		String logFileName= null;
		int i = string.indexOf('=');
		if (i != -1) {
			logFileName = string.substring(i + 1);
			try {
				new File(logFileName).createNewFile();
			} catch (IOException e) {
				abort("Problem creating " + type + " file: " + e.getMessage());
			}
		} else {
			abort(type + " file not specified!");
		}

		return logFileName;
	}
}
