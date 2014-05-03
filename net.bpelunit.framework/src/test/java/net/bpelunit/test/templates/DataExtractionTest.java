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
	public void basicTest() throws Exception {
		// getResults(...) also checks that the .bpts passes all tests
		TestUtil.getResults(TC_DATAEXTRACTION);
	}
}
