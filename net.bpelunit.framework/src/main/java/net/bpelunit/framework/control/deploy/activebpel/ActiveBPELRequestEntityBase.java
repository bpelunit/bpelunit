package net.bpelunit.framework.control.deploy.activebpel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.util.BPELUnitConstants;

import org.apache.commons.httpclient.methods.RequestEntity;

public abstract class ActiveBPELRequestEntityBase implements RequestEntity {

    public static final String NS_XMLSCHEMA
        = "http://www.w3.org/2001/XMLSchema";
    public static final String NS_XMLSCHEMA_INSTANCE
        = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String NS_ACTIVEBPEL_ADMIN
        = "http://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd";
    public static final String NS_SOAP_ENCODING
        = "http://schemas.xmlsoap.org/soap/encoding/";

    private byte[]              bytesMessage;

    public long getContentLength() {
        return bytesMessage.length;
    }

    public String getContentType() {
        return BPELUnitConstants.TEXT_XML_CONTENT_TYPE;
    }

    public boolean isRepeatable() {
        return true;
    }

    public void writeRequest(OutputStream out)
            throws IOException {
        out.write(bytesMessage);
    }

    private SOAPMessage createEmptyMessage()
            throws SOAPException {
        MessageFactory mFactory = MessageFactory.newInstance();
        return mFactory.createMessage();
    }

    /**
     * Utility method for creating the root element for the body of a SOAP
     * message.
     */
    protected SOAPElement addRootElement(SOAPMessage message, final QName name)
            throws SOAPException {
        SOAPBody body = message.getSOAPBody();
        SOAPElement rootElement = body.addChildElement(name);
        rootElement.setEncodingStyle(NS_SOAP_ENCODING);
        rootElement.addNamespaceDeclaration("xsi", NS_XMLSCHEMA_INSTANCE);
        rootElement.addNamespaceDeclaration("xsd", NS_XMLSCHEMA);
        rootElement.addNamespaceDeclaration("act", NS_ACTIVEBPEL_ADMIN);

        return rootElement;
    }

    /**
     * This method must be called somewhere at the end of the constructor of
     * every subclass: it is a template method for creating the complete SOAP
     * message. This allows us to initialize the fields in the subclass _before_
     * the message is created.
     */
    protected void createMessage()
            throws IOException, SOAPException {
        SOAPMessage message = createEmptyMessage();
        populateMessage(message);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        message.writeTo(b);
        bytesMessage = b.toByteArray();
    }

    /**
     * Populate the empty SOAP message in <code>message</code>.
     */
    protected abstract void populateMessage(SOAPMessage message)
            throws SOAPException, IOException;

}
