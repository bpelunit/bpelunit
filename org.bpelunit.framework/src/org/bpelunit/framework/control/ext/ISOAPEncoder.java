/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.ext;

import javax.xml.soap.SOAPMessage;

import org.bpelunit.framework.exception.SOAPEncodingException;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.w3c.dom.Element;

/**
 * The ISOAPEncoder interface represents a SOAP Encoder, and is intended to be implemented by
 * providers of concrete style and encoding targets, like, for example, "document/literal".
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface ISOAPEncoder {

	/**
	 * Construct a complete SOAP message from the given literal elements children (the element
	 * itself is a "dummy header") according to the instructions in the WSDL binding operation
	 * defined in the operation given to either the input, output or fault element.
	 * 
	 * @param operation
	 * @param literalData
	 * @return
	 * @throws SOAPEncodingException
	 */
	public SOAPMessage construct(SOAPOperationCallIdentifier operation, Element literalData) throws SOAPEncodingException;

	/**
	 * Deconstructs a complete SOAP message into literal elements according to the instruction in
	 * the WSDL binding operation given. The return value must be a dummy parent with literal
	 * children from the SOAP message. If the operation specifies an input or output element, the
	 * children are from the normal SOAP body. If the operation specifies a fault, the children are
	 * from the SOAP Fault Detail.
	 * 
	 * @param operation
	 * @param message
	 * @return
	 * @throws SOAPEncodingException
	 */
	public Element deconstruct(SOAPOperationCallIdentifier operation, SOAPMessage message) throws SOAPEncodingException;

}
