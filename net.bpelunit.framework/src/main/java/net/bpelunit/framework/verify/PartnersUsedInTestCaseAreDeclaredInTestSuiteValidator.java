package net.bpelunit.framework.verify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public class PartnersUsedInTestCaseAreDeclaredInTestSuiteValidator implements
		ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite)
			throws SpecificationException {
		XMLTestSuite testSuite = suite.getTestSuite();
		Set<String> partnerNames = gatherPartnerNames(testSuite);

		validateOnlyDeclaredPartnersAreUsedInTestCases(
				testSuite.getTestCases(), partnerNames);
	}

	private void validateOnlyDeclaredPartnersAreUsedInTestCases(
			XMLTestCasesSection testCases, Set<String> partnerNames) throws SpecificationException {
		for (XMLTestCase tc : testCases.getTestCaseList()) {
			validateOnlyDeclaredPartnersAreUsedInTestCase(tc, partnerNames);
		}
	}

	private void validateOnlyDeclaredPartnersAreUsedInTestCase(XMLTestCase tc,
			Set<String> partnerNames) throws SpecificationException {
		if (tc.getPartnerTrackList() != null) {
			for (XMLPartnerTrack pt : tc.getPartnerTrackList()) {
				if(!partnerNames.contains(pt.getName())) {
					throw new SpecificationException(
					"A partnertrack has been specified without a partner name: " + pt.getName() + " in " + tc.getName());
				}
			}
		}
	}

	private Set<String> gatherPartnerNames(XMLTestSuite suite) {
		Set<String> names = new HashSet<String>();

		List<XMLPartnerDeploymentInformation> partners = suite.getDeployment()
				.getPartnerList();

		if (partners != null) {
			for (XMLPartnerDeploymentInformation di : partners) {
				names.add(di.getName());
			}
		}

		return names;
	}

}
