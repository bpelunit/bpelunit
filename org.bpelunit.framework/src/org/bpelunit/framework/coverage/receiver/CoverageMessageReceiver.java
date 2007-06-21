package org.bpelunit.framework.coverage.receiver;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.util.BPELUnitUtil;
import org.bpelunit.framework.coverage.CoverageConstants;
import org.bpelunit.framework.exception.SOAPEncodingException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import org.w3c.dom.Element;

import com.ibm.wsdl.Constants;

public class CoverageMessageReceiver {


	private ISOAPEncoder encoder = null;
	
	private Logger logger = Logger.getLogger(getClass());

	private SOAPOperationCallIdentifier operation = null;

	private String testCase=null;

	private MarkersRegisterForArchive markersRegistry;

	public CoverageMessageReceiver(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}


	public synchronized void putMessage(String body) {
		if (encoder != null && operation != null) {
			
			Element element = null;
			// try {
			logger.info("!!!!!!!!!MESSAGE ANGEKOMMEN");
			if (body == null) {
				logger.info("!!!!!!!!BODY==NULL");
			} else {

				logger.info("!!!!!!!BODY!=NULL " + body);
			}
			SOAPMessage fSOAPMessage;
			try {
				fSOAPMessage = BPELUnitUtil.getMessageFactoryInstance()
						.createMessage(null,
								new ByteArrayInputStream(body.getBytes()));

				logger.info("!!!!!!! BYTES Rausgeholt");
				element = encoder.deconstruct(operation, fSOAPMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(e.getLocalizedMessage());
			} catch (SOAPException e) {
				logger.info("Could not create SOAP message from incoming message: "
						 + e.getMessage());
			} catch (SOAPEncodingException e) {
				logger.info("Could not create SOAP message from incoming message: "
						 + e.getMessage());
			}

			markersRegistry.setCoverageStatusForAllMarker(element
					.getTextContent(), testCase);
		}else{
			logger.info("SOAPEncoder is not initialized.");
		}

	}


	public void setSOAPEncoder(ISOAPEncoder encoder) {
		this.encoder = encoder;
	}

	public void setPathToWSDL(String wsdl) {
		try {
			WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();

			reader.setFeature(Constants.FEATURE_VERBOSE, false);
			Definition fWSDLDefinition = reader.readWSDL(wsdl);

			operation = new SOAPOperationCallIdentifier(fWSDLDefinition,
					new QName(
							CoverageConstants.COVERAGETOOL_NAMESPACE.getURI(),
							CoverageConstants.SERVICE_NAME),
					CoverageConstants.PORT_OF_SERVICE,
					CoverageConstants.REPORT_OPERATION,
					SOAPOperationDirectionIdentifier.INPUT);

			// String encodingStyle = operation.getEncodingStyle();
		} catch (SpecificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WSDLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getEncodingStyle() {
		String style = null;
		if (operation != null) {
			try {
				style = operation.getEncodingStyle();
			} catch (SpecificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				markersRegistry.addInfo(e.getMessage());
			}
		}
		return style;
	}

	public void setCurrentTestcase(String testCase) {
		this.testCase = testCase;
	}

}
