package net.bpelunit.framework.verify;

import java.util.List;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public class PartnersInTestCasesHaveNamesValidator implements
		ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite)
			throws SpecificationException {
		List<XMLTestCase> testCases = suite.getTestSuite().getTestCases().getTestCaseList();
		
		for(XMLTestCase t : testCases) {
			validatePartnersInTestCaseHaveNames(t);
		}
	}

	private void validatePartnersInTestCaseHaveNames(XMLTestCase t) throws SpecificationException {
		validateSOAPPartnersInTestCaseHaveNames(t);
		validateHumanPartnersInTestCaseHaveNames(t);
	}

	private void validateHumanPartnersInTestCaseHaveNames(XMLTestCase t) throws SpecificationException {
		if(t.getHumanPartnerTrackList() != null) {
			for(XMLHumanPartnerTrack pt : t.getHumanPartnerTrackList()) {
				String name = pt.getName();
				if(name == null || "".equals(name)) { 
					throw new SpecificationException("There is a human partner in test case " + t.getName() +" that has no name!");
				}
			}
		}
	}

	private void validateSOAPPartnersInTestCaseHaveNames(XMLTestCase t) throws SpecificationException {
		if(t.getPartnerTrackList() != null) {
			for(XMLPartnerTrack pt : t.getPartnerTrackList()) {
				String name = pt.getName();
				if(name == null || "".equals(name)) { 
					throw new SpecificationException("There is a partner in test case " + t.getName() +" that has no name!");
				}
			}
		}
	}

}
