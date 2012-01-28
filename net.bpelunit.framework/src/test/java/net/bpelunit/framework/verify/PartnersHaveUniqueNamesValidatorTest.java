package net.bpelunit.framework.verify;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PartnersHaveUniqueNamesValidatorTest {

	private XMLDeploymentSection deployment;
	private XMLTestSuiteDocument testSuiteDoc;
	private PartnersHaveUniqueNamesValidator validator = new PartnersHaveUniqueNamesValidator();

	@Before
	public void setUp() {
		testSuiteDoc = XMLTestSuiteDocument.Factory.newInstance();
		XMLTestSuite testSuite = testSuiteDoc.addNewTestSuite();
		deployment = testSuite.addNewDeployment();
	}

	@Test
	public void testAllPartnerNamesUnique() throws Exception {
		deployment.addNewPartner().setName("A");
		deployment.addNewPartner().setName("B");
		deployment.addNewHumanPartner().setName("C");
		deployment.addNewHumanPartner().setName("D");

		validator.validate(testSuiteDoc);
	}

	@Test(expected = SpecificationException.class)
	public void testTwoSOAPPartnersWithSameName() throws Exception {
		deployment.addNewPartner().setName("A");
		deployment.addNewPartner().setName("B");
		deployment.addNewPartner().setName("A");
		deployment.addNewHumanPartner().setName("C");
		deployment.addNewHumanPartner().setName("D");

		validator.validate(testSuiteDoc);
	}

	@Test(expected = SpecificationException.class)
	public void testTwoHumanPartnersWithSameName() throws Exception {
		deployment.addNewPartner().setName("A");
		deployment.addNewPartner().setName("B");
		deployment.addNewHumanPartner().setName("C");
		deployment.addNewHumanPartner().setName("D");
		deployment.addNewHumanPartner().setName("C");

		validator.validate(testSuiteDoc);
	}

	@Test(expected = SpecificationException.class)
	public void testOneSOAPPartnerAndOneHumanPartnerWithSameName()
			throws Exception {
		deployment.addNewPartner().setName("A");
		deployment.addNewPartner().setName("B");
		deployment.addNewHumanPartner().setName("C");
		deployment.addNewHumanPartner().setName("A");

		validator.validate(testSuiteDoc);
	}

	@After
	public void tearDown() {
		this.deployment = null;
		this.testSuiteDoc = null;
	}
}
