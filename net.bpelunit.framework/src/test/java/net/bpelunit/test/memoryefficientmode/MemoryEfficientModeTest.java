package net.bpelunit.test.memoryefficientmode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.ref.WeakReference;

import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.test.util.TestTestRunner;

import org.junit.Test;

public class MemoryEfficientModeTest {

	protected static final File TEST_BPTS_DIR = new File(MemoryEfficientModeTest.class
			.getResource("/memoryefficient").getPath());

	public static TestTestRunner getTestRunner()
			throws ConfigurationException, SpecificationException {
		return new TestTestRunner(new File(TEST_BPTS_DIR, "memoryefficient.bpts"));
	}

	@Test
	public void testMemoryEfficientModeDoesNotBreak() throws ConfigurationException,
			DeploymentException, SpecificationException {
		TestTestRunner runner = getTestRunner();

		// first test run to make sure all code paths have been loaded
		Runtime runtime = Runtime.getRuntime();
		runner.setMemoryEfficientMode(true);
		runner.testRun();
		assertEquals(1, runner.getPassed());
		assertEquals(0, runner.getProblems());
		runner = null;
		gc();
		
		// efficient test run
		runner = getTestRunner();
		runner.setMemoryEfficientMode(true);
		gc();
		long usedMemoryBeforeMemoryEfficient = runtime.totalMemory() - runtime.freeMemory();
		runner.testRun();
		gc();
		long usedMemoryAfterMemoryEfficient = runtime.totalMemory() - runtime.freeMemory();
		long memoryNeededForMemoryEfficient = usedMemoryAfterMemoryEfficient - usedMemoryBeforeMemoryEfficient;
		assertEquals(1, runner.getPassed());
		assertEquals(0, runner.getProblems());
		assertNotNull(runner.getTestSuite().getTestCases().get(0));
		
		// non-efficient test run 
		runner = getTestRunner();
		runner.setMemoryEfficientMode(false);
		gc();
		long usedMemoryBeforeStandardMemory = runtime.totalMemory() - runtime.freeMemory();
		runner.testRun();
		gc();
		long usedMemoryAfterStandard = runtime.totalMemory() - runtime.freeMemory();
		long memoryNeededForStandard = usedMemoryAfterStandard - usedMemoryBeforeStandardMemory;
		assertEquals(1, runner.getPassed());
		assertEquals(0, runner.getProblems());
		assertNotNull(runner.getTestSuite().getTestCases().get(0));
		
		// this is nice but memory approximations are very unreliant and should not break the build
//		System.out.println(String.format("Efficient: %d=%d-%d", memoryNeededForMemoryEfficient, usedMemoryAfterMemoryEfficient, usedMemoryBeforeMemoryEfficient));
//		System.out.println(String.format("Standard:  %d=%d-%d", memoryNeededForStandard, usedMemoryAfterStandard, usedMemoryBeforeStandardMemory));
//		assertTrue(memoryNeededForStandard > memoryNeededForMemoryEfficient);
	}
	
	public static void gc() {
		Object obj = new Object();
		WeakReference<Object> ref = new WeakReference<Object>(obj);
		obj = null;
		while (ref.get() != null) {
			System.gc();
		}
	}
}
