/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.soap;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.util.BPELUnitConstants;
import org.bpelunit.framework.control.util.BPELUnitUtil;
import org.bpelunit.framework.exception.SOAPEncodingException;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * <p>
 * This class implements an Encoder for SOAP messages in rpc/literal style. The WSDL specifying the
 * target web service must be structured as specified in the WS-I Basic Profile, which has the
 * following implications for this encoder:
 * </p>
 * <p>
 * The operation which defines the message must be a document/literal operation, as defined in
 * section 5.3 of WS-I BP.
 * </p>
 * <p>
 * <ul>
 * <li>The style attribute must be "rpc", and the use attributes must be "literal".</li>
 * <li>The soap:body elements must have at most one part specified, or none, in which case the
 * corresponding message must only have one part specified (this is not strictly required by this
 * encoder - more parts are allowed).</li>
 * <li>The referenced parts in the messages MUST(!) be specified with the "type" attribute: <i>An
 * rpc-literal binding in a DESCRIPTION MUST refer, in its soapbind:body element(s), only to
 * wsdl:part element(s) that have been defined using the type attribute.</i></li>
 * </ul>
 * </p>
 * 
 * <p>
 * The last point is most relevant. It means that the literal data is already specified with the
 * part names as top-level elements (there is simply no other way of referencing the elements in any
 * other case).
 * </p>
 * <p>
 * A SOAP RPC/Literal message looks like this:
 * </p>
 * <p>
 * <ul>
 * <li> In RPC/Literal SOAP messages, there may only be one child to the SOAP BODY, and it must be
 * the WSDL-Target-Namespace-Qualified name of the invoked WSDL operation, for example:
 * <q0:NewOperation xmlns:q0="http://www.example.org/TestWSDL/"></li>
 * <li>The children of this element are the completely unqualified part names of the WSDL message,
 * containing inside the actual content.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Thus, this encoder generates an RPC parent with a namespace taken from the WSDL definition. The
 * literal children - being already specified in part-format - are then simply added as children to
 * the RPC parent.
 * </p>
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class RPCLiteralEncoder implements ISOAPEncoder {

	private static final String RPC_WRAPPER_NAMESPACE_PREFIX= "rpcwrappernsprefix";

	public SOAPMessage construct(SOAPOperationCallIdentifier operation, Element literalData) throws SOAPEncodingException {

		try {
		        if (operation.isFault()) {
                            throw new SOAPEncodingException("RPC style cannot be used with SOAP faults: check section 3.6 of the WSDL 1.1 standard");
                        }

			MessageFactory mFactory= BPELUnitUtil.getMessageFactoryInstance();
			SOAPFactory sFactory= SOAPFactory.newInstance();

			SOAPMessage message= mFactory.createMessage();
			SOAPBody body= message.getSOAPBody();

			// The RPC wrapper element name must match the error code if this
			// is a fault, and the operation name otherwise.
			SOAPElement data = body;

			// create the RPC/Literal parent element.
			String targetNameSpace= operation.getTargetNamespace();
			SOAPElement wrapper= sFactory.createElement(operation.getName(), RPC_WRAPPER_NAMESPACE_PREFIX, targetNameSpace);

			NodeList list= literalData.getChildNodes();
			for (int i= 0; i < list.getLength(); i++) {
				Node node= list.item(i);
				if (node instanceof Element) {
					Element actual= (Element) list.item(i);
					wrapper.addChildElement(sFactory.createElement(actual));
				}
			}
			data.addChildElement(wrapper);
			return message;

		} catch (SOAPException e) {
			throw new SOAPEncodingException("A SOAPException occurred in the DocumentLiteralEncoder while encoding to operation " + operation, e);
		}
	}

	public Element deconstruct(SOAPOperationCallIdentifier operation, SOAPMessage message) throws SOAPEncodingException {

		try {
			SOAPBody body= message.getSOAPBody();

                        if (operation.isFault()) {
                            throw new SOAPEncodingException("rpc style cannot be used with SOAP faults: check section 3.6 of the WSDL 1.1 standard");
                        }

			SOAPElement data = body;
			SOAPElement rpcWrapper= null;

			// Find element node child
			for (Iterator childElements= data.getChildElements(); childElements.hasNext();) {
				Object current= childElements.next();
				if (current instanceof SOAPElement) {
					rpcWrapper= (SOAPElement) current;
					break;
				}
			}

			if (rpcWrapper == null)
				throw new SOAPEncodingException("Incoming SOAP message for operation " + operation + " does not have a RPC Wrapper element.");

			// Generate a raw root
			Element rawRoot= BPELUnitUtil.generateDummyElementNode();

			// Iterate through the children and add them
			for (Iterator i= rpcWrapper.getChildElements(); i.hasNext();) {
				Object current= i.next();
				if (current instanceof SOAPElement) {
					SOAPElement element= (SOAPElement) current;
					rawRoot.appendChild(rawRoot.getOwnerDocument().importNode(element, true));
				}
			}

			return rawRoot;
		} catch (SOAPException e) {
			throw new SOAPEncodingException("A SOAPException occurred in the RPCLiteralEncoder while decoding for operation " + operation);
		}
	}



}
