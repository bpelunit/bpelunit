/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.test.wsht;

import static org.junit.Assert.assertEquals;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.test.util.TestTestRunner;

import org.junit.Test;

/**
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
public class WSHTTest {

	private static final String BASEPATH= "src/test/resources/wsht/";

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
}
