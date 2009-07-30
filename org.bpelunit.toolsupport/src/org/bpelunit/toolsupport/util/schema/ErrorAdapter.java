package org.bpelunit.toolsupport.util.schema;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An adapter class for receiving Exceptions during XML parsing. The methods in
 * this class are empty, so no Exceptions are thrown.
 * 
 * Extend this class to create a ErrorHandler and override the methods of
 * interest.
 * 
 * @author cvolhard
 * 
 */
public class ErrorAdapter implements ErrorHandler {

	/**
	 * Receive notification of a warning. Throws no Exception.
	 */
	public void warning(SAXParseException e) throws SAXException {
		// not implemented
	}

	/**
	 * Receive notification of a recoverable error. Throws no Exception.
	 */
	public void error(SAXParseException e) throws SAXException {
		// not implemented
	}

	/**
	 * Receive notification of a non-recoverable error. Throws no Exception.
	 */
	public void fatalError(SAXParseException e) throws SAXException {
		// not implemented
	}

}
