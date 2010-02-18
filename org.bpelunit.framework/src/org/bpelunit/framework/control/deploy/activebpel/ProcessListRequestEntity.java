package org.bpelunit.framework.control.deploy.activebpel;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.DOMException;

/**
 * Request entity for getting the list of all running WS-BPEL processes from the
 * ActiveBPEL 4.1 administration WS.
 *
 * @author Antonio García Domínguez
 * @version 1.0 2009/05/02
 */
public class ProcessListRequestEntity extends ActiveBPELRequestEntityBase {

    private static final String ACTIVEBPEL_PSTATE_RUNNING      = "1";
    private static final String ACTIVEBPEL_PLIST_FILTER_PSTATE = "processState";
    private static final String ACTIVEBPEL_PLIST_FILTER        = "filter";
    private static final String ACTIVEBPEL_PLISTINPUT          = "getProcessListInput";

    public ProcessListRequestEntity() throws IOException, SOAPException {
        createMessage();
    }

    @Override
    protected void populateMessage(SOAPMessage message)
            throws SOAPException, DOMException {
        SOAPElement rootElement = addRootElement(
            message, new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                               ACTIVEBPEL_PLISTINPUT));
        SOAPElement filterElement = rootElement.addChildElement(
            new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                      ACTIVEBPEL_PLIST_FILTER));
        SOAPElement pstateElement = filterElement.addChildElement(
            new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                      ACTIVEBPEL_PLIST_FILTER_PSTATE));

        pstateElement.setTextContent(ACTIVEBPEL_PSTATE_RUNNING);
    }

}
