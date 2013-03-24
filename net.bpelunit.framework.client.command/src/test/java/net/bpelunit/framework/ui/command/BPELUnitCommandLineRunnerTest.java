package net.bpelunit.framework.ui.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import net.bpelunit.framework.ui.command.ConsoleMock.ProgramExitException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BPELUnitCommandLineRunnerTest {

	private static final String FILE_COV = "target/file.cov";
	private static final String FILE_LOG = "target/file.log";
	private static final String FILE_XML = "target/file.xml";
	private static final String FILENAME_TESTSUITE = "src/test/resources/testsuite.bpts";
	
	private ConsoleMock commandLineMock;

	private class BPELUnitCommandLineRunnerWithoutRunAndExit extends
			BPELUnitCommandLineRunner {

		public BPELUnitCommandLineRunnerWithoutRunAndExit(ConsoleMock cmd, String[] args) {
			super(cmd, args);
		}

		String getTestSuiteFileName() {
			// normalize just in case we run on Windows. Problem here
			// is that replaceAll takes a RegEx so we cannot just
			// use the pathseparator because in case of \ it needs to
			// be escaped
			return getTestSuiteFile().getPath().replaceAll("\\\\", "/");
		}

		@Override
		void run() {
		}
	}

	@Before
	public void setUp() {
		commandLineMock = new ConsoleMock();
	}
	
	@After
	public void tearDown() {
		new File(FILE_LOG).delete();
		new File(FILE_XML).delete();
		new File(FILE_COV).delete();
	}

	@Test
	public void testParameterParsingNoOptions() throws Exception {
		BPELUnitCommandLineRunnerWithoutRunAndExit runner = null;
		try {
			runner = new BPELUnitCommandLineRunnerWithoutRunAndExit(
					commandLineMock, new String[] { FILENAME_TESTSUITE });
		} catch (ConsoleMock.ProgramExitException e) {
			fail(commandLineMock.getConsoleBuffer());
		}

		assertEquals(false, runner.isVerbose());
		assertNull(runner.getXmlFileName());
		assertNull(runner.getLogFileName());
//		assertNull(runner.getCovFileName());
//		assertFalse(runner.getCoverageDetails());
		assertEquals(FILENAME_TESTSUITE, runner.getTestSuiteFileName());
		assertEquals(0, runner.getTestCaseNames().size());
	}

	@Test
	public void testParameterParsingAllOptionsNormalCoverage() throws Exception {
		BPELUnitCommandLineRunnerWithoutRunAndExit runner = null;
		try {
			runner = new BPELUnitCommandLineRunnerWithoutRunAndExit(
					commandLineMock, new String[] { 
							"-v", 
							"-x=" + FILE_XML,
							"-l=" + FILE_LOG, 
							"-c=" + FILE_COV,
							FILENAME_TESTSUITE, 
							"tc1", 
							"tc2" });
		} catch (ConsoleMock.ProgramExitException e) {
			fail(commandLineMock.getConsoleBuffer());
		}

		assertEquals(true, runner.isVerbose());
		assertEquals(FILE_XML, runner.getXmlFileName());
		assertEquals(FILE_LOG, runner.getLogFileName());
//		assertEquals(FILE_COV, runner.getCovFileName());
//		assertFalse(runner.getCoverageDetails());
		assertEquals(FILENAME_TESTSUITE, runner.getTestSuiteFileName());
		assertEquals(2, runner.getTestCaseNames().size());
		assertEquals("tc1", runner.getTestCaseNames().get(0));
		assertEquals("tc2", runner.getTestCaseNames().get(1));
	}

	@Test
	public void testParameterParsingAllOptionsDetailedCoverage()
			throws Exception {
		BPELUnitCommandLineRunnerWithoutRunAndExit runner = null;
		try {
			runner = new BPELUnitCommandLineRunnerWithoutRunAndExit(
					commandLineMock, new String[] { 
							"-v", 
							"-x=" + FILE_XML,
							"-l=" + FILE_LOG, 
							"-d=" + FILE_COV,
							FILENAME_TESTSUITE, 
							"tc1", 
							"tc2" });
		} catch (ConsoleMock.ProgramExitException e) {
			fail(commandLineMock.getConsoleBuffer());
		}

		assertEquals(true, runner.isVerbose());
		assertEquals(FILE_XML, runner.getXmlFileName());
		assertEquals(FILE_LOG, runner.getLogFileName());
//		assertEquals(FILE_COV, runner.getCovFileName());
//		assertTrue(runner.getCoverageDetails());
		assertEquals(FILENAME_TESTSUITE, runner.getTestSuiteFileName());
		assertEquals(2, runner.getTestCaseNames().size());
		assertEquals("tc1", runner.getTestCaseNames().get(0));
		assertEquals("tc2", runner.getTestCaseNames().get(1));
	}

	@Test(expected = ProgramExitException.class)
	public void testNoParameters() throws Exception {
		new BPELUnitCommandLineRunnerWithoutRunAndExit(commandLineMock,
				new String[0]);
	}
}
