package net.bpelunit.framework.verify;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public interface TestSuiteValidator {

	void validate(XMLTestSuiteDocument suite) throws SpecificationException;
	
}
