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
	private static CoverageMessageReceiver instance = null;



//	public static String ABSOLUT_CONFIG_PATH="";
	public static String ABSOLUT_CONFIG_PATH="";


	private ISOAPEncoder encoder;

	private SOAPOperationCallIdentifier operation;

	private String testCase;



	private LabelsRegistry markersRegistry;

	public CoverageMessageReceiver(LabelsRegistry markersRegistry) {

		this.markersRegistry=markersRegistry;
	}

//	public static CoverageMessageReceiver getInstance() {
//		if (instance == null)
//			instance = new CoverageMessageReceiver();
//		return instance;
//	}

	public synchronized void putMessage(String body) {
		Logger logger=Logger.getLogger(getClass());
		Element element = null;
//		try {
			logger.info("!!!!!!!!!MESSAGE ANGEKOMMEN");
			if(body==null){
				logger.info("!!!!!!!!BODY==NULL");
			}else{

				logger.info("!!!!!!!BODY!=NULL "+body);
			}
			SOAPMessage fSOAPMessage;
			try {
				fSOAPMessage = BPELUnitUtil.getMessageFactoryInstance()
						.createMessage(null,
								new ByteArrayInputStream(body.getBytes()));


			logger.info("!!!!!!! BYTES Rausgeholt");
				element=encoder.deconstruct(operation, fSOAPMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(e.getLocalizedMessage());
			} catch (SOAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(e.getLocalizedMessage());
			} catch (SOAPEncodingException e) {
				// TODO Auto-generated catch block
				logger.info(e.getLocalizedMessage());
			}
			
			markersRegistry.setCoverageStatusForAllMarker(element.getTextContent(),
					testCase);
//		} 
//		catch (Exception e) {
//			if(element==null){
//				logger.info("!!!!!!!!!!! ELEMENT==NULL "+e.getMessage());
//			}
//			logger.info("Could not create SOAP message from incoming message: "
//					+ e.getMessage());
//			markersRegistry.addInfo("Could not create SOAP message from incoming message: "
//					+ e.getMessage());
//		}
	}

	public void inizialize(BPELUnitRunner runner) throws SpecificationException {
		WSDLReader reader;
		if(ABSOLUT_CONFIG_PATH==null||ABSOLUT_CONFIG_PATH.equals("")){
			throw new SpecificationException(
					"ABSOLUT_CONFIG_DIR=null ");
		}

//		String wsdlPfad = FilenameUtils.concat(	ABSOLUT_CONFIG_PATH,CoverageConstants.COVERAGE_SERVICE_WSDL);
		try {
			reader = WSDLFactory.newInstance().newWSDLReader();

			reader.setFeature(Constants.FEATURE_VERBOSE, false);
//			Definition fWSDLDefinition = reader.readWSDL(wsdlPfad);
			Definition fWSDLDefinition = reader.readWSDL(ABSOLUT_CONFIG_PATH);
			operation = new SOAPOperationCallIdentifier(fWSDLDefinition,
					new QName(
							CoverageConstants.COVERAGETOOL_NAMESPACE.getURI(),
							CoverageConstants.SERVICE_NAME),
					CoverageConstants.PORT_OF_SERVICE,
					CoverageConstants.REPORT_OPERATION,
					SOAPOperationDirectionIdentifier.INPUT);
			String encodingStyle = operation.getEncodingStyle();
			encoder = runner.createNewSOAPEncoder(encodingStyle);
		}catch (WSDLException e) {
			e.printStackTrace();
			
			throw new SpecificationException(
					"Error while reading WSDL for Coverage Tool  from file "
							+ ABSOLUT_CONFIG_PATH 
							+ "." + e.getMessage());
		}
	}


	public void setCurrentTestcase(String testCase) {
		this.testCase=testCase;
	}

	public void initialize() {
		instance=null;	
	}


}
