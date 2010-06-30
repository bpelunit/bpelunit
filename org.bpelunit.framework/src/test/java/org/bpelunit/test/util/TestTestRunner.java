/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.test.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
		setHomeDirectory(".");
	}

	public TestTestRunner(String path, String bpts) throws ConfigurationException, SpecificationException {
		super();
		fProblems= 0;
		fPassed= 0;

		Map<String, String> options= new HashMap<String, String>();
		options.put(BPELUnitRunner.SKIP_UNKNOWN_EXTENSIONS, "true");
		initialize(options);

		fSuite= loadTestSuite(new File(path, bpts));
		fSuite.addResultListener(this);
	}

	public void testRun() {

		try {
			fSuite.setUp();
			fSuite.run();
			fSuite.shutDown();
		} catch (DeploymentException e) {
			try {
				fSuite.shutDown();
			} catch (DeploymentException e1) {
			}
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
