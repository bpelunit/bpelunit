package org.bpelunit.toolsupport.util.schema;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ErrorReporter implements ErrorHandler {

	public ErrorReporter() {
	}

	public void warning(SAXParseException e) throws SAXException {
	}

	public void error(SAXParseException e) throws SAXException {
	}

	public void fatalError(SAXParseException e) throws SAXException {
	}

}
