/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.test.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.base.BPELUnitBaseRunner;
import org.bpelunit.framework.control.result.ITestResultListener;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.TestCase;
import org.bpelunit.framework.model.test.TestSuite;
import org.bpelunit.framework.model.test.report.ITestArtefact;

/**
 * 
 * BPELUnit Runner specialized for tests
 * 
 * @version $Id: TestTestRunner.java,v 1.6 2006/07/15 14:52:05 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestTestRunner extends BPELUnitBaseRunner implements ITestResultListener {

	private int fProblems;

	private int fPassed;

	private TestSuite fSuite;

	@Override
	public void configureInit() throws ConfigurationException {
		setHomeDirectory(FileUtils.toFile(BPELUnitBaseRunner.class.getClass().getResource("/")).getAbsolutePath());
	}

	public TestTestRunner(File fBPTS) throws ConfigurationException, SpecificationException {
		super();
		fProblems= 0;
		fPassed= 0;

		Map<String, String> options= new HashMap<String, String>();
		options.put(BPELUnitRunner.SKIP_UNKNOWN_EXTENSIONS, "true");
		options.put(BPELUnitRunner.GLOBAL_TIMEOUT, "10000");
		initialize(options);

		fSuite = loadTestSuite(fBPTS);
		fSuite.addResultListener(this);
	}

	public TestTestRunner(String path, String bpts) throws ConfigurationException, SpecificationException {
		this(new File(path, bpts));
	}

	public void testRun() throws DeploymentException {

		try {
			fSuite.setUp();
			fSuite.run();
		} finally {
				fSuite.shutDown();
		}
	}

	public int getProblems() {
		return fProblems;
	}

	public int getPassed() {
		return fPassed;
	}

	public void progress(ITestArtefact test) {

	}

	public void testCaseEnded(TestCase test) {
		if (test.hasProblems())
			fProblems++;
		else
			fPassed++;
	}

	public void testCaseStarted(TestCase test) {

	}

	@Override
	public void configureLogging() throws ConfigurationException {
		// do nothing.
	}

	public TestSuite getTestSuite() {
		return fSuite;
	}

}

