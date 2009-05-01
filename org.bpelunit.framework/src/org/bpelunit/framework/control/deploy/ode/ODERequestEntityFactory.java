package org.bpelunit.framework.control.deploy.ode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bpelunit.framework.control.util.BPELUnitUtil;

public class ODERequestEntityFactory {
	private static ODERequestEntityFactory factory=null;
	private static final String ODE_ELEMENT_DEPLOY="deploy";
	private static final String ODE_ELEMENT_ZIPNAME="name";
	private static final String ODE_ELEMENT_PACKAGE="package";
	private static final String ODE_ELEMENT_ZIP="zip"; 
	private static final String ODE_ELEMENT_UNDEPLOY="undeploy";
	private static final String ODE_ELEMENT_PACKAGENAME="packageName";
	
	/*private static final String NS_XMLSCHEMA= "http://www.w3.org/2001/XMLSchema";
	private static final String NS_XMLSCHEMA_INSTANCE= "http://www.w3.org/2001/XMLSchema-instance";
	private static final String NS_SOAP_ENCODING= "http://schemas.xmlsoap.org/soap/encoding/";*/
	private static final String NS_DEPLOY_SERVICE="http://www.apache.org/ode/deployapi";
	private static final String NS_XML_MIME="http://www.w3.org/2005/05/xmlmime";
	private static final String NS_PMAPI="http://www.apache.org/ode/pmapi";

	private static final String CONTENT_TYPE_STRING="contentType";
	private static final String ZIP_CONTENT_TYPE="application/zip";
	
	private String fContent;
	
	private ODERequestEntityFactory(){
		
	}
	
	public static synchronized ODERequestEntityFactory newInstance(){
		if(factory == null){
			return (factory=new ODERequestEntityFactory());
		}

		return factory;
	}
	
	public RequestEntity getDeployRequestEntity(File file) throws IOException,SOAPException{
		prepareDeploySOAP(file);
		return new StringRequestEntity(fContent);
	}
	
	public RequestEntity getUndeployRequestEntity(String processId) throws IOException,SOAPException{
		prepareUndeploySOAP(processId);
		return new StringRequestEntity(fContent);
	}
	
	//***** Private helper methods ********
	
	private void prepareDeploySOAP(File file) throws IOException,SOAPException{
		MessageFactory mFactory= BPELUnitUtil.getMessageFactoryInstance();
		SOAPMessage message= mFactory.createMessage();
		SOAPBody body= message.getSOAPBody();
		
		SOAPElement xmlDeploy= body.addChildElement(ODE_ELEMENT_DEPLOY);
		SOAPElement xmlZipFilename= xmlDeploy.addChildElement(ODE_ELEMENT_ZIPNAME);
		xmlZipFilename.setTextContent(FilenameUtils.getName(file.toString()).split("\\.")[0]);
		
		SOAPElement xmlZipContent=xmlDeploy.addChildElement(ODE_ELEMENT_PACKAGE);
		SOAPElement xmlBase64ZipFile=xmlZipContent.addChildElement(ODE_ELEMENT_ZIP,"dep",NS_DEPLOY_SERVICE);
		
		xmlBase64ZipFile.addAttribute(new QName(NS_XML_MIME,CONTENT_TYPE_STRING), ZIP_CONTENT_TYPE);
		
		StringBuilder content= new StringBuilder();
		byte[] arr= FileUtils.readFileToByteArray(file);
		byte[] encoded= Base64.encodeBase64Chunked(arr);
		for (int i= 0; i < encoded.length; i++) {
			content.append((char) encoded[i]);
		}
		
		xmlBase64ZipFile.setTextContent(content.toString());
		
		ByteArrayOutputStream b= new ByteArrayOutputStream();
		message.writeTo(b);
		fContent= b.toString();
	}
	
	private void prepareUndeploySOAP(String packageId) throws SOAPException, IOException{
		MessageFactory mFactory= BPELUnitUtil.getMessageFactoryInstance();
		SOAPMessage message= mFactory.createMessage();
		SOAPBody body= message.getSOAPBody();
		
		SOAPElement xmlUndeploy=body.addChildElement(ODE_ELEMENT_UNDEPLOY);
		SOAPElement xmlPackageName=xmlUndeploy.addChildElement(ODE_ELEMENT_PACKAGENAME);
		xmlPackageName.setTextContent(packageId);
		
		ByteArrayOutputStream b=new ByteArrayOutputStream();
		message.writeTo(b);
		fContent=b.toString();
	}
}