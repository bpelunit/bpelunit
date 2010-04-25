package org.bpelunit.test.templates;

import org.bpelunit.test.util.TestUtil;
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
	 * Checks that variables inside the activity context are available in
	 * the XPath receive conditions (both expression and value).
	 */
	@Test
	public void testActivityContextVariablesAvailableInConditions() throws Exception {
		TestUtil.getResults(TC_COND);
	}
}
