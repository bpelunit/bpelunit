/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.ext;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
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
	 * @param operation    Identifier of the WSDL operation binding.
	 * @param literalData  Literal data to be used as body for the SOAP message.
	 * @param faultCode    Fault code to be used if a SOAP fault is going to be sent.
	 * @param faultString  Fault string to be used if a SOAP fault is going to be sent.
	 * @return
	 * @throws SOAPEncodingException
	 */
	SOAPMessage construct(SOAPOperationCallIdentifier operation, Element literalData, QName faultCode, String faultString) throws SOAPEncodingException;

	/**
	 * Deconstructs a complete SOAP message into literal elements according to the instruction in
	 * the WSDL binding operation given. The return value must be a dummy parent with literal
	 * children from the SOAP message. If the operation specifies an input or output element, the
	 * children are from the normal SOAP body. If the operation specifies a fault, the children are
	 * from the SOAP Fault Detail. The actual fault code and string must match the expected values.
	 * 
	 * @param operation    Identifier of the WSDL operation binding.
	 * @param message      SOAP message received.
	 * @return
	 * @throws SOAPEncodingException
	 */
	Element deconstruct(SOAPOperationCallIdentifier operation, SOAPMessage message) throws SOAPEncodingException;

}
