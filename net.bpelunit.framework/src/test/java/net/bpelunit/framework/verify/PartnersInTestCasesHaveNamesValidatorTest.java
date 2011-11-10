package net.bpelunit.framework.verify;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PartnersInTestCasesHaveNamesValidatorTest {

	private XMLTestSuiteDocument testSuiteDoc;
	private PartnersInTestCasesHaveNamesValidator validator = new PartnersInTestCasesHaveNamesValidator();
	private XMLTestCasesSection testCases;
	private XMLTestCase testCase1;

	@Before
	public void setUp() {
		testSuiteDoc = XMLTestSuiteDocument.Factory.newInstance();
		XMLTestSuite testSuite = testSuiteDoc.addNewTestSuite();
		testCases = testSuite.addNewTestCases();
		testCase1 = testCases.addNewTestCase();
	}

	@After
	public void tearDown() {
		this.testCases = null;
		this.testSuiteDoc = null;
	}
	
	@Test(expected = SpecificationException.class)
	public void testEmptyWSSOAPPartnerName() throws Exception {
		testCase1.addNewPartnerTrack().setName("A");
		testCase1.addNewPartnerTrack().setName("");

		validator.validate(testSuiteDoc);
	}

	@Test(expected = SpecificationException.class)
	public void testEmptyWSHTName() throws Exception {
		testCase1.addNewHumanPartnerTrack().setName("A");
		testCase1.addNewHumanPartnerTrack().setName("");
		
		validator.validate(testSuiteDoc);
	}

	@Test(expected = SpecificationException.class)
	public void testNullWSSOAPPartnerName() throws Exception {
		testCase1.addNewPartnerTrack().setName("A");
		testCase1.addNewPartnerTrack();

		validator.validate(testSuiteDoc);
	}

	@Test(expected = SpecificationException.class)
	public void testNullWSHTName() throws Exception {
		testCase1.addNewHumanPartnerTrack().setName("A");
		testCase1.addNewHumanPartnerTrack();

		validator.validate(testSuiteDoc);
	}
	
	@Test
	public void testAllHaveNames() throws Exception {
		testCase1.addNewHumanPartnerTrack().setName("A");
		testCase1.addNewHumanPartnerTrack().setName("B");
		testCase1.addNewPartnerTrack().setName("C");
		testCase1.addNewPartnerTrack().setName("D");
		
		validator.validate(testSuiteDoc);
	}
	
	@Test(expected = SpecificationException.class)
	public void testNullWSHTNameInSecondTestCase() throws Exception {
		XMLTestCase testCase2 = testCases.addNewTestCase();
		testCase2.addNewHumanPartnerTrack().setName("A");
		testCase2.addNewHumanPartnerTrack();

		validator.validate(testSuiteDoc);
	}	
}
