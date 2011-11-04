package net.bpelunit.framework.verify;

import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.xmlbeans.XmlOptions;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public class TestSuiteXMLValidator implements ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument doc)
			throws SpecificationException {
		ArrayList<Object> validationErrors = new ArrayList<Object>();
		XmlOptions options = new XmlOptions();
		options.setErrorListener(validationErrors);

		if (!doc.validate(options)) {
			StringWriter sW = new StringWriter();
			for (Object o : validationErrors) {
				sW.append(o + "\n");
			}
			throw new SpecificationException("BPTS is invalid:\n"
					+ sW.toString());
		}
	}

}
