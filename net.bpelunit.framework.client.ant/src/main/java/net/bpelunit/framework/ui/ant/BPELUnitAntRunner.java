/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.ui.ant;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.bpelunit.framework.base.BPELUnitBaseRunner;
import net.bpelunit.framework.control.result.XMLResultProducer;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.ITestResultListener;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.ui.ant.BPELUnit.Logging;
import net.bpelunit.framework.ui.ant.BPELUnit.Output;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.varia.NullAppender;

/**
 * The ant implementation of the BPELUnit runner. Offers support for outputting logging and test
 * results to file or sysout.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class BPELUnitAntRunner extends BPELUnitBaseRunner implements ITestResultListener {

	/**
	 * The home directory of BPELUnit.
	 */
	private String fBPELUnitHomeDirectory;

	/**
	 * List of "loggers"
	 */
	private List<Logging> fLoggers;

	/**
	 * List of "outputters"
	 */
	private List<Output> fOutputs;

	/**
	 * Failure count.
	 */
	private int fFailures;

	/**
	 * Error count.
	 */
	private int fErrors;

	/**
	 * Run count.
	 */
	private int fRuns;

	// ********************* Initialization ********************

	public BPELUnitAntRunner(String homedir, List<Logging> loggers, List<Output> outputList) {
		fBPELUnitHomeDirectory= homedir;
		fLoggers= loggers;
		fOutputs= outputList;
		fFailures= 0;
		fErrors= 0;
		fRuns= 0;
	}

	@Override
	public void configureInit() throws ConfigurationException {
		setHomeDirectory(fBPELUnitHomeDirectory);

		for (Output output : fOutputs) {
			output.initialize();
		}
	}

	@Override
	public void configureLogging() throws ConfigurationException {

		Logger.getRootLogger().removeAllAppenders();
		for (Logging log : fLoggers) {
			log.initialize();
			Logger.getRootLogger().addAppender(new WriterAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN), log.getOutputStream()));
			Logger.getRootLogger().setLevel(Level.toLevel(log.getLevel()));
		}

		if (fLoggers.isEmpty()) {
			Logger.getRootLogger().addAppender(new NullAppender());
		}
	}

	// ************************* Running *************************

	public void run(File testSuite) throws SpecificationException, DeploymentException, ConfigurationException {

		TestSuite suite= loadTestSuite(testSuite);
		suite.addResultListener(this);

		try {
			suite.setUp();
		} catch (DeploymentException e) {
			try {
				suite.shutDown();
			} catch (DeploymentException e2) {}
			throw e;
		}

		outputPlain("START", suite);

		suite.run();
		suite.removeResultListener(this);

		outputPlain("END", suite);

		String result= "Test Run Completed. " + fRuns + " " + getPluralOf(fRuns, "run") + " (" + fFailures + " " + getPluralOf(fFailures, "failure")
				+ ", " + fErrors + " " + getPluralOf(fErrors, "error") + ") \n";
		outputPlain(result);
		System.out.println(result);

		outputXML(suite);

		closeOutputs();

		suite.shutDown();

	}

	// *********************** ITestResultListener *********************

	public void testCaseStarted(TestCase testCase) {
		outputPlain("START", testCase);
	}

	public void testCaseEnded(TestCase testCase) {
		if (testCase.getStatus().isFailure()) {
			fFailures++;
		}
		if (testCase.getStatus().isError()) {
			fErrors++;
		}
		fRuns++;

		outputPlain("END", testCase);
	}

	public void progress(ITestArtefact testArtefact) {
		if (testArtefact instanceof PartnerTrack) {
			outputPlain("PROGRESS", testArtefact);
		}
	}

	private void outputPlain(String head, ITestArtefact testCase) {
		String info= head + ": " + testCase.getName() + ": " + testCase.getStatus().toString() + "\n";
		outputPlain(info);
	}

	// ************************** Helpers *************************

	private void outputPlain(String info) {
		for (Iterator<Output> i= fOutputs.iterator(); i.hasNext();) {
			Output output= i.next();
			try {
				if (output.getStyle().equals(Output.STYLE_PLAIN)) {
					output.write(info);
				}
			} catch (IOException e) {
				System.out.println("I/O Error writing to output stream - canceling output.");
				output.dispose();
				i.remove();
			}
		}
	}

	private void outputXML(TestSuite suite) {
		for (Iterator<Output> i= fOutputs.iterator(); i.hasNext();) {
			Output output= i.next();
			try {
				if (output.getStyle().equals(Output.STYLE_XML)) {
					XMLResultProducer.writeXML(output.getOutput(), suite);
				}
			} catch (IOException e) {
				System.out.println("I/O Error writing to output stream - canceling output.");
				output.dispose();
				i.remove();
			}
		}
	}

	private String getPluralOf(int no, String name) {
		return no == 1 ? name : name + "s";
	}

	private void closeOutputs() {
		for (Logging l : fLoggers) {
			l.dispose();
		}

		for (Output o : fOutputs) {
			o.dispose();
		}
	}

}
