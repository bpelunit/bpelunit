package org.bpelunit.framework.control.result;

import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.xmlbeans.XmlOptions;
import org.bpelunit.framework.xml.result.XMLTestResultDocument;
import org.bpelunit.test.end2end.End2EndTester;
import org.bpelunit.test.util.StringOutputStream;
import org.bpelunit.test.util.TestTestRunner;
import org.junit.Test;

/**
 * Tests for ensuring that the results produced by XMLResultProducer are valid
 * and complete.
 * 
 * @author Antonio García-Domínguez
 */
public class XMLResultProducerTest {

	@Test
	public void testSendOnlyReceiveOnlyResultsAreValid() throws Exception {
		assertValid(getResults(End2EndTester.getSendOnlyReceiveOnlyRunner()));
	}

	@Test
	public void testSendReceiveSyncResultsAreValid() throws Exception {
		assertValid(getResults(End2EndTester.getSendReceiveSyncRunner()));
	}

	@Test
	public void testSendReceiveAsyncResultsAreValid() throws Exception {
		assertValid(getResults(End2EndTester.getSendReceiveAsyncRunner()));
	}

	@Test
	public void testInvalidAssumptionResultsAreValid() throws Exception {
		TestTestRunner runner
			= new TestTestRunner("resources/result/InvalidAssumption/",
					"WastePaperBasketTestSuite.bpts");
		assertValid(getResults(runner));
	}

	private XMLTestResultDocument getResults(TestTestRunner runner) throws Exception {
		runner.testRun();
		StringOutputStream os = new StringOutputStream();
		XMLResultProducer.writeXML(os, runner.getTestSuite());
		return XMLTestResultDocument.Factory.parse(os.getString());
	}

	private void assertValid(XMLTestResultDocument res) {
		ArrayList<Object> errors = new ArrayList<Object>();
		XmlOptions options = new XmlOptions();
		options.setErrorListener(errors);

		if (!res.validate(options)) {
			StringWriter sW = new StringWriter();
			for (Object o : errors) {
				sW.append(o + "\n");
			}
			fail("Results should pass XML Schema validation: " + sW.toString()
					+ "\nXML source was:\n\n" + res.toString());
		}
	}
}
