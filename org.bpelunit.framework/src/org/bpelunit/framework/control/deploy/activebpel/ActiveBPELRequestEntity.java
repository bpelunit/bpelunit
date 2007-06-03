/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.deploy.activebpel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bpelunit.framework.control.util.BPELUnitConstants;
import org.bpelunit.framework.control.util.BPELUnitUtil;

/**
 * An entity which takes care of passing a bpr file as a base64-encoded file in a web service call.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ActiveBPELRequestEntity implements RequestEntity {

	private static final String ACTIVEBPEL_ELEMENT_ABASE64FILE= "aBase64File";
	private static final String ACTIVEBPEL_ELEMENT_ABPRFILENAME= "aBprFilename";
	private static final String ACTIVEBPEL_ELEMENT_DEPLOYBPR= "deployBpr";

	private static final String NS_XMLSCHEMA= "http://www.w3.org/2001/XMLSchema";
	private static final String NS_XMLSCHEMA_INSTANCE= "http://www.w3.org/2001/XMLSchema-instance";
	private static final String NS_SOAP_ENCODING= "http://schemas.xmlsoap.org/soap/encoding/";

	private static final String XSD_STRING= "xsd:string";

	private String fContent;

	public ActiveBPELRequestEntity(File file) throws IOException, SOAPException {

		MessageFactory mFactory= BPELUnitUtil.getMessageFactoryInstance();
		SOAPMessage message= mFactory.createMessage();
		SOAPBody body= message.getSOAPBody();

		body.addNamespaceDeclaration("xsi", NS_XMLSCHEMA_INSTANCE);
		body.addNamespaceDeclaration("xsd", NS_XMLSCHEMA);

		SOAPElement xmlDeployBpr= body.addChildElement(ACTIVEBPEL_ELEMENT_DEPLOYBPR);
		xmlDeployBpr.setEncodingStyle(NS_SOAP_ENCODING);

		// Add filename
		SOAPElement xmlBprFilename= xmlDeployBpr.addChildElement(ACTIVEBPEL_ELEMENT_ABPRFILENAME);
		xmlBprFilename.addAttribute(new QName(NS_XMLSCHEMA_INSTANCE, "type"), XSD_STRING);
		xmlBprFilename.setTextContent(FilenameUtils.getName(file.toString()));

		// Add data
		SOAPElement xmlBase64File= xmlDeployBpr.addChildElement(ACTIVEBPEL_ELEMENT_ABASE64FILE);
		xmlBase64File.addAttribute(new QName(NS_XMLSCHEMA_INSTANCE, "type"), XSD_STRING);

		StringBuilder content= new StringBuilder();
		byte[] arr= FileUtils.readFileToByteArray(file);
		byte[] encoded= Base64.encodeBase64Chunked(arr);
		for (int i= 0; i < encoded.length; i++) {
			content.append((char) encoded[i]);
		}
		xmlBase64File.setTextContent(content.toString());

		ByteArrayOutputStream b= new ByteArrayOutputStream();
		message.writeTo(b);
		fContent= b.toString();
	}

	public long getContentLength() {
		return fContent.length();
	}

	public String getContentType() {
		return BPELUnitConstants.TEXT_XML_CONTENT_TYPE;
	}

	public boolean isRepeatable() {
		return true;
	}

	public void writeRequest(OutputStream out) throws IOException {
		out.write(fContent.toString().getBytes());
		out.flush();
	}

}
