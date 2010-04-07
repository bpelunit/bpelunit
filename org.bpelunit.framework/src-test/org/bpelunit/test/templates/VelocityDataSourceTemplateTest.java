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
	 * produces the same results as the equivalent old-style BPTS file.
	 */
	@Test
	public void sameResultsAsOldStyleBPTS() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"Test case data sources "
				+ "should produce the same results as the equivalent old-style "
				+ "BPTS", TC_VDS_TCDS_NOTEMP, TC_VDS_TCDS_TEMP);
	}
}
