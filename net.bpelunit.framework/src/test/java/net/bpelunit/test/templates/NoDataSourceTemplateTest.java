package net.bpelunit.test.templates;

import net.bpelunit.test.util.TestUtil;
import org.junit.Test;

/**
 * Tests for checking that templates with no data sources work correctly.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class NoDataSourceTemplateTest extends AbstractTemplateTest {

	@Test
	public void noContextTemplatesAreCorrectlyEvaluated() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"The expanded template should also produce 4 results",
				TC_4R_NOTEMP, TC_4R_TEMP);
	}

	@Test
	public void requestVariableCanBeUsedInTemplate() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"The $request variable should be an XmlObject with the SOAP " +
			"body of the incoming message", TC_K2R_NOTEMP, TC_K2R_TEMP);
	}

	@Test
	public void testInfoCanBeUsedInTemplate() throws Exception {
		TestUtil.assertSameAndSuccessfulResults("Information about the PUT, test suite and " +
				"current test case should be made available to the template",
				TC_TI_NOTEMP, TC_TI_TEMP);
	}

	@Test
	public void testSuiteAndTestCaseSetUpBlocksWorkCorrectly() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"setUp Velocity scripts are executed using 2-levels with proper" +
				" isolation, inheritance and redefinition", TC_SUP_NOTEMP, TC_SUP_TEMP);
	}

	@Test
	public void testPreviousSentAndReceivedMessagesVarsWorkCorrectly() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"partnerTrackReceived and partnerTrackSent contain all the " +
				"messages previously received and sent from the current partner " +
				"track", TC_PTRACKHIST_ONLYREQ, TC_PTRACKHIST_ALLVARS);
	}

	@Test
	public void velocityToolsIsWellIntegrated() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"partnerTrackReceived and partnerTrackSent contain all the " +
				"messages previously received and sent from the current partner " +
				"track", TC_PTRACKHIST_ONLYREQ, TC_PTRACKHIST_VELOCITYTOOLS);
	}

	@Test
	public void printerUtilityWorks() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
				"XMLPrinterTool#print prints back the right XML fragment",
				TC_PRINTER_NOTEMP, TC_PRINTER_TEMP);
	}
}
