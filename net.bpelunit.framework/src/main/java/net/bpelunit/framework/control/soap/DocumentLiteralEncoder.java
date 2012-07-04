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
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.Text;

import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * This class implements an Encoder for SOAP messages in document/literal style.
 * The WSDL specifying the target web service must be structured as specified in
 * the WS-I Basic Profile, which has the following implications for this
 * encoder:
 * </p>
 * 
 * <p>
 * The operation which defines the message must be a document/literal operation,
 * as defined in section 5.3 of WS-I BP.
 * </p>
 * <p>
 * <ul>
 * <li>The style attribute must be "document", and the use attributes must be
 * "literal".</li>
 * <li>The soap:body elements must have at most one part specified, or none, in
 * which case the corresponding message must only have one part specified (this
 * is not strictly required by this encoder - more parts are allowed).</li>
 * <li>The referenced parts in the messages MUST(!) be specified with the
 * "element" attribute: <i>R2204 A document-literal binding in a DESCRIPTION
 * MUST refer, in each of its soapbind:body element(s), only to wsdl:part
 * element(s) that have been defined using the element attribute.</i></li>
 * </ul>
 * </p>
 * 
 * <p>
 * The last point is most relevant, as this allows the encoder to just copy the
 * literal elements into the SOAP Body or Fault Detail, no questions asked.
 * </p>
 * 
 * @version $Id$
 * @author Philip Mayer, Antonio Garcia-Dominguez
 * 
 */
public class DocumentLiteralEncoder implements ISOAPEncoder {

	public SOAPMessage construct(SOAPOperationCallIdentifier operation,
			Element literalElement, QName faultCode, String faultString)
			throws SOAPEncodingException {

		try {

			MessageFactory mFactory = MessageFactory.newInstance();
			SOAPFactory sFactory = SOAPFactory.newInstance();

			SOAPMessage message = mFactory.createMessage();
			SOAPBody body = message.getSOAPBody();
			SOAPElement data;
			if (operation.isFault()) {
				SOAPFault fault = body.addFault(faultCode, faultString);
				data = fault.addDetail();
			} else {
				data = body;
			}

			NodeList nodes = literalElement.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				if (node instanceof Element) {
					SOAPElement soapElement = sFactory.createElement((Element) node);
					data.addChildElement(soapElement);
				}
			}

			return message;
		} catch (SOAPException e) {
			throw new SOAPEncodingException(
					"A SOAPException occurred in the DocumentLiteralEncoder while encoding to operation "
							+ operation, e);
		}
	}

	public Element deconstruct(SOAPOperationCallIdentifier operation,
			SOAPMessage message) throws SOAPEncodingException {

		try {
			SOAPElement data = message.getSOAPBody();

			Element rawRoot = BPELUnitUtil.generateDummyElementNode();
			for (Iterator<?> i = data.getChildElements(); i.hasNext();) {
				Object current = i.next();
				if (current instanceof SOAPElement) {
					SOAPElement element = (SOAPElement) current;
					rawRoot.appendChild(rawRoot.getOwnerDocument().importNode(
							element, true));
				}

				if (current instanceof Text) {
					Text element = (Text) current;
					rawRoot.appendChild(rawRoot.getOwnerDocument().importNode(
							element, true));
				}

			}
			return rawRoot;
		} catch (SOAPException e) {
			throw new SOAPEncodingException(
					"A SOAPException occurred in the DocumentLiteralEncoder while decoding for operation "
							+ operation, e);
		}
	}

}
