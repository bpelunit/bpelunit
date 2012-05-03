package net.bpelunit.framework.verify;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public interface ITestSuiteValidator {

	void validate(XMLTestSuiteDocument suite) throws SpecificationException;
	
}
