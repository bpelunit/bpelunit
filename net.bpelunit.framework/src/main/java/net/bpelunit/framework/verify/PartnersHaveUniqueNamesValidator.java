package net.bpelunit.framework.verify;

import java.util.HashSet;
import java.util.Set;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public class PartnersHaveUniqueNamesValidator implements ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite)
			throws SpecificationException {

		Set<String> partnerNames = new HashSet<String>();
		XMLDeploymentSection deployment = suite.getTestSuite().getDeployment();
		
		for(XMLPartnerDeploymentInformation p : deployment.getPartnerList()) {
			String name = p.getName();
			if(partnerNames.contains(name)) {
				throw new SpecificationException("There are at least two partners with the name <" + name + ">. There must be only one.");
			}
			partnerNames.add(name);
		}
		
		for(XMLHumanPartnerDeploymentInformation p :deployment.getHumanPartnerList()) {
			String name = p.getName();
			if(partnerNames.contains(name)) {
				throw new SpecificationException("There are at least two partners with the name <" + name + ">. There must be only one.");
			}
			partnerNames.add(name);
		}

	}

}
