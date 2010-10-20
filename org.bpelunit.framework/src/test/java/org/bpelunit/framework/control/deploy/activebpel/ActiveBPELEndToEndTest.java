package org.bpelunit.framework.control.deploy.activebpel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.test.util.TestTestRunner;
import org.junit.Test;

/**
 * End-to-end test case using the ActiveBPEL engine. Requires an installed and
 * <em>running</em> instance installed in an application server set up under the
 * path specified in the CATALINA_HOME environment variable. The server should
 * use the default configuration values used in
 * {@link org.bpelunit.framework.control.deploy.activebpel.ActiveBPELDeployer}.
 * 
 * @author Antonio García-Domínguez
 */
public class ActiveBPELEndToEndTest {
	private static final String TEST_SUITE_DIR = "resources/engines";
	private static final String TEST_SUITE_FNAME = "tacService-activebpel.bpts";
	private static final String TEST_SUITE_ENDLESS_FNAME = "tacService-activebpel-endless.bpts";
	private static final String TEST_SUITE_INVALID = "tacService-activebpel-invalid.bpts";

	@Test
	public void allTestCasesPass() throws ConfigurationException,
			DeploymentException, SpecificationException {
		checkAssumptions();

		TestTestRunner runner = new TestTestRunner(TEST_SUITE_DIR,
				TEST_SUITE_FNAME);
		runner.testRun();
		assertEquals("All test cases should pass", 4, runner.getPassed());
	}

	/**
	 * Ensures that the deployer works when the .bpts is in the current directory.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void allTestCasesPassSameDir() throws Exception {
		checkAssumptions();

		// Copy the test suite files to the current directory
		final File testSuiteDir = new File(TEST_SUITE_DIR);
		final File cwd = new File(".");
		// List the test resources, so we can clean them up later
		final Collection<File> testFiles = FileUtils.listFiles(testSuiteDir, null, false);

		try {
			FileUtils.copyDirectory(testSuiteDir, cwd);
			TestTestRunner runner = new TestTestRunner(new File(TEST_SUITE_FNAME));
			runner.testRun();
			assertEquals("All test cases should pass", 4, runner.getPassed());
		} finally {
			for (File f : testFiles) {
				new File(f.getName()).delete();
			}
		}
	}

	@Test
	public void endlessLoopIsTerminated() throws ConfigurationException,
			DeploymentException, SpecificationException {
		checkAssumptions();

		TestTestRunner runner = new TestTestRunner(TEST_SUITE_DIR, TEST_SUITE_ENDLESS_FNAME);
		runner.testRun();
		assertEquals("Only the case with empty input passed",
				1, runner.getPassed());
		assertTrue("Some processes were terminated",
				ActiveBPELDeployer._terminatedProcessCount >= 1);
	}

	@Test
	public void invalidProcessesAreRejected() throws Exception {
		checkAssumptions();

		try {
			TestTestRunner runner = new TestTestRunner(TEST_SUITE_DIR, TEST_SUITE_INVALID);
			runner.testRun();
			fail("A DeploymentException was expected.");
		} catch (DeploymentException ex) {}
	}

	private void checkAssumptions() {
		assumeNotNull(System
				.getenv(ActiveBPELDeployer.DEFAULT_APPSERVER_DIR_ENVVAR));
		assumeTrue(new File(System
				.getenv(ActiveBPELDeployer.DEFAULT_APPSERVER_DIR_ENVVAR), "bpr")
				.canRead());
	}
}
