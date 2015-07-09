/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.end2end;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.ITestResultListener;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.test.util.TestTestRunner;
import org.junit.Test;

/**
 * This class tests BPELUnit in an end2end fashion using the built-in test mode.
 * 
 * Activities tested: <table>
 * <tr>
 * <td><b>Activity</b></td>
 * <td><b>Where</b></td>
 * <td><b>Side</b></td>
 * </tr>
 * <tr>
 * <td>Send Only</td>
 * <td>WastePaper</td>
 * <td>Client</td>
 * </tr>
 * <tr>
 * <td>Receive Only</td>
 * <td>WastePaper</td>
 * <td>PUT</td>
 * </tr>
 * <tr>
 * <td>Send/Receive Sync</td>
 * <td>CreditRating</td>
 * <td>Client</td>
 * </tr>
 * <tr>
 * <td>Receive/Send Sync</td>
 * <td>CreditRating</td>
 * <td>PUT</td>
 * </tr>
 * <tr>
 * <td>Send/Receive Async</td>
 * <td>LoanService</td>
 * <td>Client</td>
 * </tr>
 * <tr>
 * <td>Receive/Send Async</td>
 * <td>LoanService</td>
 * <td>PUT</td>
 * </tr>
 * </table>
 * 
 * @version $Id: End2EndTester.java,v 1.5 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer, Daniel Luebkbe
 * 
 */
public class End2EndTester {

	private static final String BASEPATH= "src/test/resources/end2end/";

	public static TestTestRunner getSendOnlyReceiveOnlyRunner() throws ConfigurationException, SpecificationException {
		return new TestTestRunner(BASEPATH + "01_SendReceiveOnly/", "WastePaperBasketTestSuite.bpts");
	}

	public static TestTestRunner getSendReceiveSyncRunner() throws ConfigurationException, SpecificationException {
		return new TestTestRunner(BASEPATH + "02_SendReceiveSync/", "CreditRatingServiceTestSuite.bpts");
	}

	public static TestTestRunner getSendReceiveAsyncRunner() throws ConfigurationException, SpecificationException {
		return new TestTestRunner(BASEPATH + "03_SendReceiveAsync/", "LoanServiceTestSuite.bpts");
	}

	/**
	 * This test simulates the WastePaper web service: A "send only" from the client and a "receive
	 * only" on the PUTs side.
	 */
	@Test
	public void testSendOnlyReceiveOnlyWastePaper() throws ConfigurationException, DeploymentException, SpecificationException {
		TestTestRunner runner = getSendOnlyReceiveOnlyRunner();
		runner.testRun();
		assertEquals(1, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}

	/**
	 * This test simulates the CreditRatingService: A "send/receive sync" from the client, and a
	 * "receive/send sync" on the PUTs side.
	 * 
	 */
	@Test
	public void testSendReceiveSynchronous() throws ConfigurationException, DeploymentException, SpecificationException {
		TestTestRunner runner= getSendReceiveSyncRunner();
		runner.testRun();
		assertEquals(2, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}

	/**
	 * This test simulates the LoanService: A "send/receive async" from the client, and a
	 * "receive/send async" on the PUTs side.
	 * 
	 */
	@Test
	public void testSendReceiveAsynchronous() throws ConfigurationException, DeploymentException, SpecificationException {
		TestTestRunner runner= getSendReceiveAsyncRunner();
		runner.testRun();
		assertEquals(3, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}

	/**
	 * This test checks that invalid BPTS files are properly rejected and that errors are reported.
	 *
	 */
	@Test
	public void testInvalidBPTS() throws ConfigurationException {
		try {
			new TestTestRunner(BASEPATH + "04_Invalid/", "InvalidTestSuite.bpts");
			fail("An exception should have been thrown");
		} catch (SpecificationException ex) {
			assertTrue("Validation errors should be included in the SpecificationException",
					ex.getLocalizedMessage().contains("sendOnlyInvalid"));
		}
	}
	
	@Test
	public void testSendOnlyWSA() throws ConfigurationException, SpecificationException, DeploymentException {
		TestTestRunner runner = new TestTestRunner(BASEPATH + "05_WSASendOnly/", "async-wsa.bpts");
		runner.getTestSuite().addResultListener(new ITestResultListener() {
			
			@Override
			public void testCaseStarted(TestCase testCase) {
			}
			
			@Override
			public void testCaseEnded(TestCase testCase) {
			}
			
			@Override
			public void progress(ITestArtefact testArtefact) {
			}
		});
		
		runner.testRun();
		assertEquals(1, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}
	
	@Test
	public void testAbortWithDependencies() throws ConfigurationException, SpecificationException, DeploymentException {
		TestTestRunner runner = new TestTestRunner(BASEPATH + "06_AbortWithDependencies/", "async-wsa.bpts");
		runner.getTestSuite().addResultListener(new ITestResultListener() {
			
			@Override
			public void testCaseStarted(TestCase testCase) {
			}
			
			@Override
			public void testCaseEnded(TestCase testCase) {
			}
			
			@Override
			public void progress(ITestArtefact testArtefact) {
			}
		});
		
		Map<String, String> options = new HashMap<String, String>();
		options.put(BPELUnitRunner.GLOBAL_TIMEOUT, "2000");
		options.put(BPELUnitRunner.SKIP_UNKNOWN_EXTENSIONS, "true");
		runner.initialize(options);
		runner.testRun();
		assertEquals(0, runner.getPassed());
		assertEquals(1, runner.getProblems());
	}
}
