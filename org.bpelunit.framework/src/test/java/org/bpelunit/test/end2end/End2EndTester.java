/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.test.end2end;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.test.util.TestTestRunner;
import org.junit.Before;
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
 * @author Philip Mayer
 * 
 */
public class End2EndTester {

	private String basepath = null;

	@Before
	public void setUp() throws Exception {
		basepath = new File(End2EndTester.class.getResource("/end2end/").toURI()).getAbsolutePath() + File.separator;
	}
	
	/**
	 * This test simulates the WastePaper web service: A "send only" from the client and a "receive
	 * only" on the PUTs side.
	 */
	@Test
	public void testSendOnlyReceiveOnlyWastePaper() throws ConfigurationException, SpecificationException {
		TestTestRunner runner= new TestTestRunner(basepath + "01_SendReceiveOnly/", "WastePaperBasketTestSuite.bpts");
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
	public void testSendReceiveSynchronous() throws ConfigurationException, SpecificationException {
		TestTestRunner runner= new TestTestRunner(basepath + "02_SendReceiveSync/", "CreditRatingServiceTestSuite.bpts");
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
	public void testSendReceiveAsynchronous() throws ConfigurationException, SpecificationException {
		TestTestRunner runner= new TestTestRunner(basepath + "03_SendReceiveAsync/", "LoanServiceTestSuite.bpts");
		runner.testRun();
		assertEquals(3, runner.getPassed());
		assertEquals(0, runner.getProblems());
	}

}
