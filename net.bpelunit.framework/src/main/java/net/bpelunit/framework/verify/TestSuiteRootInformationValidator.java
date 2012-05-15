package net.bpelunit.framework.verify;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.framework.xml.suite.XMLTrack;

public class TestSuiteRootInformationValidator implements ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite)
			throws SpecificationException {
		XMLTestSuite xmlTestSuite = suite.getTestSuite();

		validateRootElementSet(xmlTestSuite);
		validateName(xmlTestSuite);
		validateBaseURL(xmlTestSuite);

		XMLDeploymentSection deployment = xmlTestSuite.getDeployment();
		validateDeploymentSection(deployment);

		XMLTestCasesSection xmlTestCases = xmlTestSuite.getTestCases();
		validateTestCasesSection(xmlTestCases);
	}

	private void validateTestCasesSection(XMLTestCasesSection xmlTestCases)
			throws SpecificationException {
		if (xmlTestCases == null) {
			throw new SpecificationException(
					"No test case section found in test suite document.");
		}

		List<XMLTestCase> xmlTestCaseList = xmlTestCases.getTestCaseList();
		if (xmlTestCaseList == null || xmlTestCaseList.size() == 0) {
			throw new SpecificationException("No test cases found.");
		}

		for (XMLTestCase tc : xmlTestCaseList) {
			validateTestCase(tc);
		}
	}

	private void validateTestCase(XMLTestCase tc) throws SpecificationException {
		validateTestCaseHasName(tc);
		validateTestCasePropertiesAreValid(tc);
		validateTestCaseHasClientTrack(tc);
		validateTestCaseTracksHaveNames(tc);
	}

	private void validateTestCaseTracksHaveNames(XMLTestCase tc) throws SpecificationException {
		if(tc.getPartnerTrackList() != null) {
			for(XMLPartnerTrack pt : tc.getPartnerTrackList()) {
				if(pt.getName() == null || pt.getName().equals("")) {
					throw new SpecificationException("There is an unnamed partner track in test case " + tc.getName());
				}
			}
		}
	}

	private void validateTestCaseHasName(XMLTestCase tc)
			throws SpecificationException {
		if (tc.getName() == null || tc.getName().equals("")) {
			throw new SpecificationException("A test case has no name");
		}
	}

	private void validateTestCasePropertiesAreValid(XMLTestCase tc)
			throws SpecificationException {
		if (tc.getPropertyList() != null) {
			for (net.bpelunit.framework.xml.suite.XMLProperty data : tc
					.getPropertyList()) {
				String xmlPropertyName = data.getName();
				String xmlPropertyData = data.getStringValue();
				if ((xmlPropertyName == null) || (xmlPropertyData == null)) {
					throw new SpecificationException("Metadata in Test Case "
							+ tc.getName()
							+ " must have both property and value.");
				}
			}
		}
	}

	private void validateTestCaseHasClientTrack(XMLTestCase tc)
			throws SpecificationException {
		XMLTrack xmlClientTrack = tc.getClientTrack();
		if (xmlClientTrack == null) {
			throw new SpecificationException(
					"Could not find clientTrack in test case "
							+ tc.getName());
		}
	}

	private void validateDeploymentSection(XMLDeploymentSection deployment)
			throws SpecificationException {
		validateDeploymentSet(deployment);

		validatePUTSet(deployment);
		validateRequiredPUTFieldsSet(deployment);

		List<XMLPartnerDeploymentInformation> partners = deployment
				.getPartnerList();
		validateAllPartnerDeploymentInformation(partners);
	}

	private void validateAllPartnerDeploymentInformation(
			List<XMLPartnerDeploymentInformation> partners)
			throws SpecificationException {
		for (XMLPartnerDeploymentInformation xmlPDI : partners) {
			validatePartnerDeploymentInformation(xmlPDI);
		}
	}

	private void validatePartnerDeploymentInformation(
			XMLPartnerDeploymentInformation xmlPDI)
			throws SpecificationException {
		String name = xmlPDI.getName();
		String wsdl = xmlPDI.getWsdl();
		if ((name == null) || (wsdl == null)) {
			throw new SpecificationException(
					"Name and WSDL attributes of a partner must not be empty.");
		}
	}

	private void validateRequiredPUTFieldsSet(XMLDeploymentSection deployment)
			throws SpecificationException {
		XMLPUTDeploymentInformation xmlPut = deployment.getPut();
		String xmlPutName = xmlPut.getName();
		String xmlPutWSDL = xmlPut.getWsdl();
		String xmlPutType = xmlPut.getType();
		if ((xmlPutName == null) || (xmlPutWSDL == null)
				|| (xmlPutType == null)) {
			throw new SpecificationException(
					"Process Under Test must have attributes name, type, wsdl, and a deployment section specified.");
		}
	}

	private void validatePUTSet(XMLDeploymentSection deployment)
			throws SpecificationException {
		if (deployment.getPut() == null) {
			throw new SpecificationException(
					"Expected a Process Under Test (PUT) in the test suite.");
		}
	}

	private void validateDeploymentSet(XMLDeploymentSection deployment)
			throws SpecificationException {
		if (deployment == null) {
			throw new SpecificationException(
					"Could not find deployment section inside test suite document.");
		}
	}

	private void validateRootElementSet(XMLTestSuite xmlTestSuite)
			throws SpecificationException {
		if (xmlTestSuite == null) {
			throw new SpecificationException(
					"Could not find testSuite root element in the test suite XML file.");
		}
	}

	private void validateName(XMLTestSuite xmlTestSuite)
			throws SpecificationException {
		if (xmlTestSuite.getName() == null || xmlTestSuite.getName().equals("")) {
			throw new SpecificationException(
					"No name found for the test suite.");
		}
	}

	private void validateBaseURL(XMLTestSuite xmlTestSuite)
			throws SpecificationException {
		String xmlUrl = xmlTestSuite.getBaseURL();
		if (xmlUrl == null) {
			xmlUrl = BPELUnitConstants.DEFAULT_BASE_URL;
		}
		try {
			new URL(xmlUrl);
		} catch (MalformedURLException e) {
			throw new SpecificationException("Base URL is not a valid URL: "
					+ xmlUrl, e);
		}
	}
}
