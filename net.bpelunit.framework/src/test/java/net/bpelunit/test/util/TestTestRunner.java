/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.base.BPELUnitBaseRunner;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.ITestResultListener;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.report.ITestArtefact;

import org.apache.commons.io.FileUtils;

/**
 * 
 * BPELUnit Runner specialized for tests
 * 
 * @version $Id: TestTestRunner.java,v 1.6 2006/07/15 14:52:05 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestTestRunner extends BPELUnitBaseRunner implements ITestResultListener {

	private int problems;

	private int passed;

	private TestSuite suite;
	
	private List<String> errorMessages = new ArrayList<String>();

	@Override
	public void configureInit() throws ConfigurationException {
		setHomeDirectory(FileUtils.toFile(BPELUnitBaseRunner.class.getClass().getResource("/")).getAbsolutePath());
	}

	public TestTestRunner(File fBPTS) throws ConfigurationException, SpecificationException {
		super();
		problems = 0;
		passed = 0;

		Map<String, String> options= new HashMap<String, String>();
		options.put(BPELUnitRunner.SKIP_UNKNOWN_EXTENSIONS, "true");
		options.put(BPELUnitRunner.GLOBAL_TIMEOUT, "10000");
		initialize(options);

		suite = loadTestSuite(fBPTS);
		suite.addResultListener(this);
		
		// TODO DL: Fix structure:
		// Option A: Pass option flags to TestSuite and remove dep TestSuite->Runner (easy change)
		// Option B: Move execution logic to runner (seems more natural)
	}

	public TestTestRunner(String path, String bpts) throws ConfigurationException, SpecificationException {
		this(new File(path, bpts));
	}

	public void testRun() throws DeploymentException {

		try {
			suite.setUp();
			suite.run();
		} finally {
				suite.shutDown();
		}
	}

	public int getProblems() {
		return problems;
	}

	public int getPassed() {
		return passed;
	}

	public void progress(ITestArtefact test) {

	}

	public void testCaseEnded(TestCase test) {
		if (test.hasProblems()) {
			problems++;
			errorMessages.add(test.getStatus().getMessage());
		} else {
			passed++;
		}
	}

	public void testCaseStarted(TestCase test) {

	}

	@Override
	public void configureLogging() throws ConfigurationException {
		// do nothing.
	}

	public TestSuite getTestSuite() {
		return suite;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
}

