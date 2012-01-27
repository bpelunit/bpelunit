package net.bpelunit.test.templates;

import net.bpelunit.test.util.TestUtil;

import org.junit.Test;

/**
 * Tests for checking that the result comparison assertions work as expected.
 *
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class ResultComparisonTest extends AbstractTemplateTest {

	@Test
	public void resultsFromSameBptsAreEqual() throws Exception {
		TestUtil.assertSameAndSuccessfulResults("The same BPTS file should always produce the same results",
				TC_4R_NOTEMP, TC_4R_NOTEMP);
	}

	@Test
	public void resultsFromDifferentBptsAreDifferent() throws Exception {
		TestUtil.assertDifferentResults("BPTS files which are semantically different are reported as such",
				TC_3R_NOTEMP, TC_4R_NOTEMP);
	}

}
