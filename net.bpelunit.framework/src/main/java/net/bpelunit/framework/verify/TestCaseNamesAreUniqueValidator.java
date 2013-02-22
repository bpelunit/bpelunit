package net.bpelunit.framework.verify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public class TestCaseNamesAreUniqueValidator implements ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite)
			throws SpecificationException {
		List<XMLTestCase> testCases;
		try {
			testCases = suite.getTestSuite().getTestCases().getTestCaseList();
		} catch(NullPointerException e) {
			// no test cases -> no duplicate names
			return;
		}
		
		if(testCases == null) {
			// again: no test cases -> no duplicate names
			return;
		}
		
		Set<String> testCaseNames = new HashSet<String>();
		for(XMLTestCase tc : testCases) {
			if(testCaseNames.contains(tc.getName())) {
				throw new SpecificationException("Duplicate test name: " + tc.getName());
			} else {
				testCaseNames.add(tc.getName());
			}
		}
	}

}
