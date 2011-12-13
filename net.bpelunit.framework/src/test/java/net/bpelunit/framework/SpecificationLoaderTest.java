package net.bpelunit.framework;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.InputStream;

import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.Test;


public class SpecificationLoaderTest {

	private static final String BASE_DIR = "src/test/resources/xmlFileImport/";
	
	@Test
	public void testInlineAnyElementSrcs() throws Exception {
		SpecificationLoader specificationLoader = new SpecificationLoader(null);
		
		InputStream resourceStream = new FileInputStream(BASE_DIR + "BookSearch-4results-fi.bpts");
		
		assertNotNull(resourceStream);
		XMLTestSuiteDocument tsd = XMLTestSuiteDocument.Factory.parse(resourceStream);
		
		specificationLoader.inlineAnyElementSrcs(tsd, BASE_DIR);
		
		assertNull(tsd.getTestSuite().getTestCases().getTestCaseArray(0).getClientTrack().getSendReceiveArray(0).getSend().getData().getSrc());
		assertNull(tsd.getTestSuite().getTestCases().getTestCaseArray(0).getPartnerTrackArray(0).getReceiveSendArray(0).getSend().getData().getSrc());
	}
	
}
