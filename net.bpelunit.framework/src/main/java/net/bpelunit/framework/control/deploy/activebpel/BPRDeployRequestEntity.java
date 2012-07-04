/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See
 * enclosed license file for more information.
 */
package net.bpelunit.framework.control.deploy.activebpel;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * An entity which takes care of passing a BPR file as a base64-encoded file in
 * a web service call. Refactored to reuse code between all ActiveBPEL request
 * entities.
 *
 * @version 2.0 2009/05/02
 * @author Philip Mayer, Antonio García Domínguez
 */
public class BPRDeployRequestEntity extends ActiveBPELRequestEntityBase {

    private static final String ACTIVEBPEL_ELEMENT_ABASE64FILE  = "aBase64File";
    private static final String ACTIVEBPEL_ELEMENT_ABPRFILENAME = "aBprFilename";
    private static final String ACTIVEBPEL_ELEMENT_DEPLOYBPR    = "deployBpr";

    private static final String XSD_STRING                      = "xsd:string";
    private File                file;

    public BPRDeployRequestEntity(File file) throws IOException, SOAPException {
        this.file = file;
        createMessage();
    }

    @Override
    protected void populateMessage(SOAPMessage message)
            throws SOAPException, IOException {
        SOAPElement xmlDeployBpr = addRootElement(
            message, new QName(ACTIVEBPEL_ELEMENT_DEPLOYBPR));

        // Add filename
        SOAPElement xmlBprFilename = xmlDeployBpr
            .addChildElement(ACTIVEBPEL_ELEMENT_ABPRFILENAME);
        xmlBprFilename.addAttribute(
            new QName(ActiveBPELRequestEntityBase.NS_XMLSCHEMA_INSTANCE,
                      "type"),
            XSD_STRING);
        xmlBprFilename.setTextContent(FilenameUtils.getName(file.toString()));

        // Add data
        SOAPElement xmlBase64File = xmlDeployBpr
                .addChildElement(ACTIVEBPEL_ELEMENT_ABASE64FILE);
        xmlBase64File.addAttribute(
            new QName(ActiveBPELRequestEntityBase.NS_XMLSCHEMA_INSTANCE,
                      "type"),
            XSD_STRING);

        StringBuilder content = new StringBuilder();
        byte[] arr = FileUtils.readFileToByteArray(file);
        byte[] encoded = Base64.encodeBase64Chunked(arr);
        for (int i = 0; i < encoded.length; i++) {
            content.append((char) encoded[i]);
        }
        xmlBase64File.setTextContent(content.toString());
    }

}
