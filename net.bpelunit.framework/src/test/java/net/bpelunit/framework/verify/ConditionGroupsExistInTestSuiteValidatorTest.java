package net.bpelunit.framework.verify;

import static org.junit.Assert.*;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConditionGroupsExistInTestSuiteValidatorTest {

	private final ConditionGroupsExistInTestSuiteValidator validator = new ConditionGroupsExistInTestSuiteValidator();
	private XMLTestSuiteDocument testSuiteDocument;
	private XMLTestSuite testSuite;

	@Before
	public void setUp() {
		testSuiteDocument = XMLTestSuiteDocument.Factory.newInstance();
		testSuite = testSuiteDocument.addNewTestSuite();
	}

	@After
	public void tearDown() {
		testSuiteDocument = null;
	}

	@Test
	public void throws_No_Error_With_Empty_Test_Suite()
			throws SpecificationException {
		validator.validate(testSuiteDocument);
	}

	@Test(expected = SpecificationException.class)
	public void detects_Missing_ConditionGroup_In_ReceiveOnly()
			throws SpecificationException {
		testSuite.addNewTestCases().addNewTestCase().addNewPartnerTrack()
				.addNewReceiveOnly()
				.addConditionGroup("MISSING_CONDITION_GROUP");
		validator.validate(testSuiteDocument);
	}

	@Test(expected = SpecificationException.class)
	public void detects_Missing_ConditionGroup_In_ReceiveSend()
			throws SpecificationException {
		testSuite.addNewTestCases().addNewTestCase().addNewPartnerTrack()
				.addNewReceiveSend().addNewReceive()
				.addConditionGroup("MISSING_CONDITION_GROUP");
		validator.validate(testSuiteDocument);
	}
	
	@Test(expected = SpecificationException.class)
	public void detects_Missing_ConditionGroup_In_SendReceive()
			throws SpecificationException {
		testSuite.addNewTestCases().addNewTestCase().addNewPartnerTrack()
		.addNewSendReceive().addNewReceive()
		.addConditionGroup("MISSING_CONDITION_GROUP");
		validator.validate(testSuiteDocument);
	}
	
	@Test(expected = SpecificationException.class)
	public void detects_Missing_ConditionGroup_In_ReceiveSendAsync()
			throws SpecificationException {
		testSuite.addNewTestCases().addNewTestCase().addNewPartnerTrack()
		.addNewReceiveSendAsynchronous().addNewReceive()
		.addConditionGroup("MISSING_CONDITION_GROUP");
		validator.validate(testSuiteDocument);
	}
	
	@Test(expected = SpecificationException.class)
	public void detects_Missing_ConditionGroup_In_SendReceiveAsync()
			throws SpecificationException {
		testSuite.addNewTestCases().addNewTestCase().addNewPartnerTrack()
		.addNewSendReceiveAsynchronous().addNewReceive()
		.addConditionGroup("MISSING_CONDITION_GROUP");
		validator.validate(testSuiteDocument);
	}
	
	@Test(expected = SpecificationException.class)
	public void detects_Missing_ConditionGroup_In_CompleteHumanTask()
			throws SpecificationException {
		testSuite.addNewTestCases().addNewTestCase().addNewHumanPartnerTrack().addNewCompleteHumanTask()
		.addConditionGroup("MISSING_CONDITION_GROUP");
		validator.validate(testSuiteDocument);
	}
	
	@Test(expected = SpecificationException.class)
	public void detects_Missing_ConditionGroup_In_ConditionGroup_Inheritance()
			throws SpecificationException {
		testSuite.addNewConditionGroups().addNewConditionGroup().setInheritFrom("MISSING_CONDITION_GROUP");
		validator.validate(testSuiteDocument);
	}

}
