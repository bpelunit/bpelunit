package net.bpelunit.framework.control.deploy.activebpel;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * Request entity for getting the list of all running WS-BPEL processes from the
 * ActiveBPEL 4.1 administration WS. Optionally, we can select only those from
 * a particular WS-BPEL composition. This will be useful when tearing down the
 * test suite or cleaning up after each test case: we only want the IDs of the
 * processes we want to kill, without affecting other processes which might be
 * running.
 *
 * @author Antonio García Domínguez
 * @version 1.1 2009/11/17
 */
public class ProcessListRequestEntity extends ActiveBPELRequestEntityBase {

    private static final String ACTIVEBPEL_PSTATE_RUNNING      = "1";
    private static final String ACTIVEBPEL_PLIST_FILTER_PSTATE = "processState";
    private static final String ACTIVEBPEL_PLIST_FILTER        = "filter";
    private static final String ACTIVEBPEL_PLISTINPUT          = "getProcessListInput";
    private static final String ACTIVEBPEL_PLIST_FILTER_PNAME  = "processName";

    private String processName;

    /**
     * Convenience constructor which creates a request for the list of all
     * running WS-BPEL processes. It does not take into account which
     * process definition these were instantiated from.
     */
    public ProcessListRequestEntity() throws IOException, SOAPException {
        this(null);
    }

    /**
     * Creates a request for the list of all running WS-BPEL processes which
     * instantiate a particular process definition. The name must be exactly
     * the same in the .bpel and .bpts files.
     */
    public ProcessListRequestEntity(String processName)
      throws IOException, SOAPException {
        this.processName = processName;
        createMessage();
    }

    @Override
    protected void populateMessage(SOAPMessage message)
            throws SOAPException {
        SOAPElement rootElement = addRootElement(
            message, new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                               ACTIVEBPEL_PLISTINPUT));
        SOAPElement filterElement = rootElement.addChildElement(
            new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                      ACTIVEBPEL_PLIST_FILTER));

        // Filter by state (running)
        SOAPElement pstateElement = filterElement.addChildElement(
            new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                      ACTIVEBPEL_PLIST_FILTER_PSTATE));
        pstateElement.setTextContent(ACTIVEBPEL_PSTATE_RUNNING);

        // Filter by name, if processName has been set
        if (processName != null) {
            SOAPElement pnameElement = filterElement.addChildElement(
                new QName(ActiveBPELRequestEntityBase.NS_ACTIVEBPEL_ADMIN,
                          ACTIVEBPEL_PLIST_FILTER_PNAME));
            pnameElement.setTextContent(processName);
        }
    }

}
