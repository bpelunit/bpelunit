/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.soap;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * <p>
 * This class implements an Encoder for SOAP messages in rpc/literal style. The
 * WSDL specifying the target web service must be structured as specified in the
 * WS-I Basic Profile, which has the following implications for this encoder:
 * </p>
 * <p>
 * <ul>
 * <li>The style attribute must be "rpc", and the use attributes must be
 * "literal".</li>
 * <li>The soap:body elements must have at most one part specified, or none, in
 * which case the corresponding message must only have one part specified (this
 * is not strictly required by this encoder - more parts are allowed).</li>
 * <li>The referenced parts in the messages MUST(!) be specified with the "type"
 * attribute: <i>An rpc-literal binding in a DESCRIPTION MUST refer, in its
 * soapbind:body element(s), only to wsdl:part element(s) that have been defined
 * using the type attribute.</i></li>
 * </ul>
 * </p>
 * 
 * <p>
 * The last point is most relevant. It means that the literal data is already
 * specified with the part names as top-level elements (there is simply no other
 * way of referencing the elements in any other case).
 * </p>
 * <p>
 * A SOAP RPC/Literal message looks like this:
 * </p>
 * <p>
 * <ul>
 * <li>In RPC/Literal SOAP messages, there may only be one child to the SOAP
 * Body element. Its namespace will be equal to the value of the namespace attribute
 * in the soapbind:body element of the WSDL file, and the local part will be equal
 * to the operation name for a request, or to the operation name + "Response" for a
 * response (WS-I BP allows just the operation name for a response as well, but
 * Apache ODE expects the "Response" suffix in all RPC-style responses). For example,
 * this would be the wrapper for a request: <code>&lt;q0:NewOperation xmlns:q0="http://www.example.org/TestWSDL/"&gt;</code>
 * </li>
 * <li>The children of this element are the <em>completely unqualified</em> part
 * names of the WSDL message, containing inside the actual content.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Thus, this encoder generates an RPC parent with a namespace taken from the
 * WSDL definition. The literal children - being already specified in
 * part-format - are then simply added as children to the RPC parent.
 * </p>
 * 
 * @version $Id$
 * @author Philip Mayer, Antonio Garcia-Dominguez
 * 
 */
public class RPCLiteralEncoder implements ISOAPEncoder {

	private static final String RPC_WRAPPER_NAMESPACE_PREFIX = "rpcwrappernsprefix";

	public SOAPMessage construct(SOAPOperationCallIdentifier operation,
			Element literalData, QName faultCode, String faultString)
			throws SOAPEncodingException {
		try {
			if (operation.isFault()) {
				throw new SOAPEncodingException(
						"RPC style cannot be used with SOAP faults: check section 3.6 of the WSDL 1.1 standard");
			}

			final MessageFactory mFactory = MessageFactory.newInstance();
			final SOAPFactory sFactory = SOAPFactory.newInstance();
			final SOAPMessage message = mFactory.createMessage();
			final SOAPBody body = message.getSOAPBody();

			// The RPC wrapper element name must match the error code if this
			// is a fault, and the operation name otherwise.
			final SOAPElement data = body;

			// Create the RPC/Literal wrapper if it doesn't exist.
			// It would be easier (and less error prone in corner cases) to
			// just assume all literal data for rpc/lit operations were
			// properly wrapped, but that'd break backwards compatibility.
			//
			// See WS-I Basic Profile 1.1, section 4.7.10 "Namespaces for soapbind Elements" for details on
			// how to handle the element that wraps the parts in the RPC messsage and how it should be treated
			// in the WSDL <binding> element.
			//
			// http://www.ws-i.org/profiles/basicprofile-1.1-2004-08-24.html#Namespaces_for_soapbind_Elements
			final String bodyNamespace = operation.getBodyNamespace();
			final String operationName = operation.getName();
			final Element firstElement = getFirstElementChild(literalData);
			final String wrapperLocalPart = operation.getDirection() == SOAPOperationDirectionIdentifier.INPUT ? operationName : operationName + "Response";
			final SOAPElement newWrapper = bodyNamespace == null ? sFactory.createElement(wrapperLocalPart) : sFactory.createElement(wrapperLocalPart, RPC_WRAPPER_NAMESPACE_PREFIX, bodyNamespace);

			NodeList partNodes;
			final String firstElementName = firstElement != null ? firstElement.getLocalName() : null;
			final String firstElementNS   = firstElement != null ? firstElement.getNamespaceURI() : null;
			if (equals(bodyNamespace, firstElementNS) && (operationName.equals(firstElementName) || (operationName + "Response").equals(firstElementName))) {
				// already wrapped according to WS-I BP 1.1 S4.7.10: the parts are the children of the wrapper
				partNodes = firstElement.getChildNodes();
			} else {
				// not wrapped yet: we can grab the parts straight from the literal data
				partNodes = literalData.getChildNodes();
			}

			for (int i = 0; i < partNodes.getLength(); i++) {
				final Node part = partNodes.item(i);
				if (part instanceof Element) {
					final Element actual = (Element) partNodes.item(i);
					
					SOAPElement copy = sFactory.createElement(actual);
					if (copy.getNamespaceURI() != null) {
						// While we copy the element into the actual message, ensure
						// that the part meets WS-I BP 1.1 S4.7.20 R2735: part
						// accessors should not belong to any namespace.
						//
						// http://www.ws-i.org/profiles/basicprofile-1.1-2004-08-24.html#Part_Accessors
						copy = copy.setElementQName(new QName(null, copy.getLocalName()));
					}
					
					newWrapper.addChildElement(copy);
				}
			}
			data.addChildElement(newWrapper);
			return message;
		} catch (SOAPException e) {
			throw new SOAPEncodingException(
					"A SOAPException occurred in the DocumentLiteralEncoder while encoding to operation "
							+ operation, e);
		}
	}

	public Element deconstruct(SOAPOperationCallIdentifier operation, SOAPMessage message) throws SOAPEncodingException {
		try {
			SOAPBody body = message.getSOAPBody();

			if (operation.isFault()) {
				throw new SOAPEncodingException(
						"rpc style cannot be used with SOAP faults: check section 3.6 of the WSDL 1.1 standard");
			}

			SOAPElement data = body;
			SOAPElement rpcWrapper = null;

			// Find element node child
			for (Iterator<?> childElements = data.getChildElements(); childElements
					.hasNext();) {
				Object current = childElements.next();
				if (current instanceof SOAPElement) {
					rpcWrapper = (SOAPElement) current;
					break;
				}
			}

			if (rpcWrapper == null) {
				throw new SOAPEncodingException(
						"Incoming SOAP message for operation " + operation
								+ " does not have a RPC Wrapper element.");
			}

			// Generate a raw root
			Element rawRoot = BPELUnitUtil.generateDummyElementNode();

			// Iterate through the children and add them
			for (Iterator<?> i = rpcWrapper.getChildElements(); i.hasNext();) {
				Object current = i.next();
				if (current instanceof SOAPElement) {
					SOAPElement element = (SOAPElement) current;
					rawRoot.appendChild(rawRoot.getOwnerDocument().importNode(
							element, true));
				}
			}

			return rawRoot;
		} catch (SOAPException e) {
			throw new SOAPEncodingException(
					"A SOAPException occurred in the RPCLiteralEncoder while decoding for operation "
							+ operation, e);
		}
	}

	/**
	 * Compares two values for equality. Unlike {@link Object#equals(Object)}, it
	 * can handle the case in which the left one is <code>null</code>.
	 */
	private boolean equals(final String a, final String b) {
		return a == null && b == null || a != null && a.equals(b);
	}

	private Element getFirstElementChild(Node literalData) {
		Node firstElement = literalData.getFirstChild();
		while (firstElement != null) {
			if (firstElement instanceof Element) {
				return (Element) firstElement;
			}
			firstElement = firstElement.getNextSibling();
		}
		return null;
	}
}
