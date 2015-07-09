package net.bpelunit.test.templates;

import net.bpelunit.test.util.TestUtil;

import org.junit.Test;

/**
 * Tests for the data extraction feature, which assigns certain data
 * from an incoming message to a variable that persists within a certain
 * scope.
 *
 * @author Antonio García-Domínguez
 */
public class DataExtractionTest extends AbstractTemplateTest {

	@Test
	public void stringExtractionIsDefault() throws Exception {
		// getResults(...) also checks that the .bpts passes all tests
		TestUtil.getResults(TC_DATAEXTRACTION_DEFAULT);
	}

	@Test
	public void stringExtraction() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"The default extraction type and the string extraction type should produce the same results",
			TC_DATAEXTRACTION_DEFAULT,
			TC_DATAEXTRACTION_STRING);
	}

	@Test
	public void nodeExtraction() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"The default extraction type and the node extraction type should produce the same results",
			TC_DATAEXTRACTION_DEFAULT,
			TC_DATAEXTRACTION_NODE);
	}

	@Test
	public void nodesetExtraction() throws Exception {
		TestUtil.assertSameAndSuccessfulResults(
			"The default extraction type and the nodeset extraction type should produce the same results",
			TC_DATAEXTRACTION_DEFAULT,
			TC_DATAEXTRACTION_NODESET);
	}

}
