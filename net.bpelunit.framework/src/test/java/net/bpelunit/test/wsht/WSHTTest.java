/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.test.wsht;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.test.util.TestTestRunner;
import net.bpelunit.test.util.TestUtil;

import org.junit.Test;

/**
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
public class WSHTTest {

	private static final String BASEPATH = "src/test/resources/wsht/";

	public static TestTestRunner getWSHTTestSuite() throws ConfigurationException, SpecificationException {
		return new TestTestRunner(BASEPATH, "WSHTTestSuite.bpts");
	}

	@Test
	public void testWSHTTestSuite() throws ConfigurationException, DeploymentException, SpecificationException {
		TestTestRunner runner = getWSHTTestSuite();
		runner.testRun();
		assertEquals(1, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}
	
	@Test
	public void wshtCompleteTaskDataSrcWorks() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"Using external files or embedding the data in the BPTS should always produce the same results",
				new File(BASEPATH, "WSHTTestSuite.bpts"),
				new File(BASEPATH, "WSHTTestSuite-fi.bpts"));
	}

	@Test
	public void wshtDataExtractionWorks() throws Exception {
		TestSuite results = TestUtil.getResults(new File(BASEPATH, "WSHTTestSuite-velocitydatacopy.bpts"));
		assertTrue("Tests should have passed", results.getStatus().isPassed());
	}

}
