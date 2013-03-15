package net.bpelunit.test.templates;

import static org.junit.Assert.assertTrue;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.test.util.TestTestRunner;
import net.bpelunit.test.util.TestUtil;

import org.junit.Test;

/**
 * End to end tests for BPTS files which use Velocity data sources.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class VelocityDataSourceTemplateTest extends AbstractTemplateTest {

	/**
	 * Checks that a template-based BPTS with a test-case level data source
	 * with inline contents produces the same results as the equivalent old
	 * style BPTS file.
	 */
	@Test
	public void testCaseSourceSameResultsAsOldStyleBPTS() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"Test case data sources with inline contents "
				+ "should produce the same results as the equivalent old-style "
				+ "BPTS", TC_VDS_TCDS_NOTEMP, TC_VDS_TCDS_TEMP);
	}

	/**
	 * Checks that a template-based BPTS with a test-suite level data source
	 * with an external reference to a .vm file produces the same results as
	 * the equivalent old style BPTS file.
	 */
	@Test
	public void testSuiteSourceSameResultsAsOldStyleBPTS() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"Test suite data sources with external references to files "
				+ "should produce the same results as the equivalent old-style "
				+ "BPTS", TC_VDS_TCDS_NOTEMP, TC_VDS_TSDS_TEMP);
	}

	/**
	 * Checks that a template-based BPTS with a test-suite level data source
	 * with an external reference to a .vm file produces the same results as
	 * the equivalent old style BPTS file.
	 */
	@Test
	public void testSuiteSourceWithCDATASameResultsAsOldStyleBPTS() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"Test suite data sources with external references to files "
				+ "should produce the same results as the equivalent old-style "
				+ "BPTS", TC_VDS_TCDS_NOTEMP, TC_VDS_TSDS_TEMP_CDATA);
	}
	
	/**
	 * Checks that a template-based BPTS with a test-suite level data source
	 * with an external reference to a .vm file and external references to .vm
	 * templates produces the same results as the equivalent old style BPTS file.
	 */
	@Test
	public void testSuiteSourceAndExternalTemplatesSameResultsAsOldStyleBPTS() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"Test suite data sources with external references to files "
				+ "should produce the same results as the equivalent old-style "
				+ "BPTS", TC_VDS_TCDS_NOTEMP, TC_VDS_TSDS_TEMP_SRCATTRIB);
	}

	/**
	 * Checks that variables inside the activity context are available in
	 * the XPath receive conditions (both expression and value).
	 */
	@Test
	public void testActivityContextVariablesAvailableInConditions() throws Exception {
		TestUtil.getResults(TC_COND);
	}

	/**
	 * Checks that partner tracks are be skipped if and only if the XPath
	 * expression in their assume attributes evaluate to false. These XPath
	 * expressions have access to the partner track template variables.
	 */
	@Test
	public void testPartnerTracksAreSkippedWhenAssumptionsAreFalse() throws Exception {
		TestUtil.getResults(TC_ASSUME_PTRACK);
	}

	/**
	 * Checks that activities are skipped if and only if the XPath expression
	 * in their assume attributes evaluate to false. These XPath expressions have
	 * access to the partner track template variables.
	 */
	@Test
	public void testActivitiesAreSkippedIffAssumptionsAreFalse() throws Exception {
		TestUtil.getResults(TC_ASSUME_ACTIVITY);
	}

	/**
	 * Checks that activities in the client track are skipped if their assumptions
	 * do not hold.
	 */
	@Test
	public void testClientTrackActivitiesAreSkippedIffAssumptionsAreFalse() throws Exception {
		TestUtil.getResults(TC_ASSUME_CTRACK);
	}

	/**
	 * Checks that receive condition templates work.
	 */
	@Test
	public void receiveConditionTemplatesShouldWork() throws Exception {
		TestUtil.getResults(TC_VDS_TSDS_TEMP_RECVCOND);
	}

	/**
	 * Checks that trying to redefine predefined variables is detected and reported correctly. 
	 */
	@Test
	public void duplicateVariablesAreReportedAndRejected() throws Exception {
		final TestTestRunner runner = new TestTestRunner(TC_SUP_DUPVARS.getParent(), TC_SUP_DUPVARS.getName());
		runner.testRun();

		assertTrue(runner.getTestSuite().getStatus().hasProblems());

		final TestCase firstTestCase = (TestCase)runner.getTestSuite().getChildren().get(0);
		final ArtefactStatus status = firstTestCase.getStatus();
		assertTrue(status.hasProblems());
		assertTrue(status.getExceptionMessage().contains("overwrite"));
	}
}
