package org.bpelunit.framework.control.deploy.activebpel;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.DOMException;

/**
 * Request entity for terminating a WS-BPEL process using the ActiveBPEL 4.1
 * administration WS.
 *
 * @author Antonio García Domínguez
 * @version 1.0 2009/05/02
 */
public class TerminateProcessRequestEntity extends ActiveBPELRequestEntityBase {
    private int pid;

    public TerminateProcessRequestEntity(int pid) throws IOException,
            SOAPException {
        this.pid = pid;
        createMessage();
    }

    @Override
    protected void populateMessage(SOAPMessage message)
            throws SOAPException, DOMException {
        SOAPElement rootElement = addRootElement(
            message, new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                               "terminateProcessInput"));
        SOAPElement pidElement = rootElement.addChildElement(
            new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN, "pid"));
        pidElement.setTextContent(Integer.toString(pid));
    }

}
