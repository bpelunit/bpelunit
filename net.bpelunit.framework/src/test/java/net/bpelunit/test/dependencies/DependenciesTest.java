package net.bpelunit.test.dependencies;

import static org.junit.Assert.assertEquals;

import java.io.File;

import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.test.util.TestTestRunner;

import org.junit.Test;

public class DependenciesTest {

	protected static final File TEST_BPTS_DIR = new File(DependenciesTest.class
			.getResource("/dependencies").getPath());

	public static TestTestRunner getDependenciesTestRunner()
			throws ConfigurationException, SpecificationException {
		return new TestTestRunner(new File(TEST_BPTS_DIR, "dependencies.bpts"));
	}

	@Test
	public void testSendReceiveSynchronous() throws ConfigurationException,
			DeploymentException, SpecificationException {
		TestTestRunner runner = getDependenciesTestRunner();
		runner.testRun();
		assertEquals(1, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}
}
