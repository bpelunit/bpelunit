package net.bpelunit.toolsupport.util.schema;

import net.bpelunit.toolsupport.ToolSupportActivator;
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
 * Antonio: implemented all three methods so the user would see the problem at
 * least in the Problems view.
 *
 * @author cvolhard, Antonio Garcia-Dominguez
 * 
 */
public class ErrorAdapter implements ErrorHandler {

	/**
	 * Receive notification of a warning. Throws no Exception.
	 */
	public void warning(SAXParseException e) throws SAXException {
		ToolSupportActivator.log(e);
	}

	/**
	 * Receive notification of a recoverable error. Throws no Exception.
	 */
	public void error(SAXParseException e) throws SAXException {
		ToolSupportActivator.log(e);
	}

	/**
	 * Receive notification of a non-recoverable error. Throws no Exception.
	 */
	public void fatalError(SAXParseException e) throws SAXException {
		ToolSupportActivator.log(e);
	}

}
