package org.bpelunit.test.templates;

import org.bpelunit.test.util.TestUtil;
import org.junit.Test;

/**
 * Tests for checking that templates with no data source nor context
 * work correctly.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class NoContextTemplateTest extends AbstractTemplateTest {

	@Test
	public void noContextTemplatesAreCorrectlyEvaluated() throws Exception {
		TestUtil.assertSameResults(
				"The expanded template should also produce 4 results",
				TC_4R_NOTEMP, TC_4R_TEMP);
	}

}
