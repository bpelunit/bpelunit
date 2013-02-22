package net.bpelunit.framework.verify;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.Test;


public class TestCaseNamesAreUniqueValidatorTest {

	private TestCaseNamesAreUniqueValidator validator = new TestCaseNamesAreUniqueValidator();
	
	@Test
	public void testSuiteWithoutTestCase() throws Exception {
		XMLTestSuiteDocument suiteDoc = XMLTestSuiteDocument.Factory.newInstance();
		
		// calls without exception are expected, thus no asserts
		validator.validate(suiteDoc);
		
		suiteDoc.addNewTestSuite();
		validator.validate(suiteDoc);
		
		suiteDoc.getTestSuite().addNewTestCases();
		validator.validate(suiteDoc);
	}

	@Test
	public void testSuiteWithOneTestCase() throws Exception {
		// calls without exception are expected, thus no asserts
		
		XMLTestSuiteDocument suiteDoc = XMLTestSuiteDocument.Factory.newInstance();
		XMLTestCasesSection testCases = suiteDoc.addNewTestSuite().addNewTestCases();
		testCases.addNewTestCase().setName("TC1");
		
		validator.validate(suiteDoc);
	}
	
	@Test
	public void testSuiteWithTwoTestCasesUniqueNames() throws Exception {
		// calls without exception are expected, thus no asserts
		
		XMLTestSuiteDocument suiteDoc = XMLTestSuiteDocument.Factory.newInstance();
		XMLTestCasesSection testCases = suiteDoc.addNewTestSuite().addNewTestCases();
		testCases.addNewTestCase().setName("TC1");
		testCases.addNewTestCase().setName("TC2");
		
		validator.validate(suiteDoc);
	}
	
	@Test(expected=SpecificationException.class)
	public void testSuiteWithTwoTestCasesNotUniqueNames() throws Exception {
		
		XMLTestSuiteDocument suiteDoc = XMLTestSuiteDocument.Factory.newInstance();
		XMLTestCasesSection testCases = suiteDoc.addNewTestSuite().addNewTestCases();
		testCases.addNewTestCase().setName("TC1");
		testCases.addNewTestCase().setName("TC1");
		
		// exception expected here
		validator.validate(suiteDoc);
	}
	
}
