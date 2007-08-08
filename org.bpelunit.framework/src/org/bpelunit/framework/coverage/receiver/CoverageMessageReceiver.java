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

import org.apache.log4j.Logger;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.util.BPELUnitUtil;
import org.bpelunit.framework.coverage.CoverageConstants;
import org.bpelunit.framework.exception.SOAPEncodingException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import org.w3c.dom.Element;

import com.ibm.wsdl.Constants;

/**
 * Die Klasse ist für die Verarbeitung von SOAP-Nachrichten an Coverage Logging
 * Service zuständig.
 * 
 * @author Alex Salnikow
 * 
 */
public class CoverageMessageReceiver {

	private ISOAPEncoder encoder = null;

	private Logger logger = Logger.getLogger(getClass());

	private SOAPOperationCallIdentifier operation = null;

	private String testCase = null;

	private MarkersRegisterForArchive markersRegistry;

	public CoverageMessageReceiver(MarkersRegisterForArchive markersRegistry) {
		this.markersRegistry = markersRegistry;
	}

	/**
	 * Empfängt SOAP-Nachrichten mit Coverage Marken während der Testausführung
	 * 
	 * @param body
	 *            Nachricht mit Coverage-Marken
	 */
	public synchronized void putMessage(String body) {
		if (encoder != null && operation != null) {
			Element element = null;
			SOAPMessage fSOAPMessage;
			try {
				fSOAPMessage = BPELUnitUtil.getMessageFactoryInstance()
						.createMessage(null,
								new ByteArrayInputStream(body.getBytes()));

				element = encoder.deconstruct(operation, fSOAPMessage);
			} catch (IOException e) {
				logger.debug(e.getLocalizedMessage());
			} catch (SOAPException e) {
				logger
						.debug("Could not create SOAP message from incoming message: "
								+ e.getMessage());
			} catch (SOAPEncodingException e) {
				logger
						.debug("Could not create SOAP message from incoming message: "
								+ e.getMessage());
			}

			markersRegistry.setCoverageStatusForAllMarker(element
					.getTextContent(), testCase);
		} else {
			logger.info("SOAPEncoder is not initialized.");
		}

	}

	/**
	 * 
	 * @param encoder
	 *            sSOAPEncoder für die Dekodierung der Nachrichten mit
	 *            Coverage-Marken
	 */
	public void setSOAPEncoder(ISOAPEncoder encoder) {
		this.encoder = encoder;
	}

	/**
	 * 
	 * @param wsdl WSDL-Beschreibung des Coverage Logging Services
	 */
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

		} catch (SpecificationException e) {
			logger
			.debug("WSDL of Coverage Logging Service is not valid:"
					+ e.getMessage());
		} catch (WSDLException e) {
			logger
			.debug("WSDL of Coverage Logging Service is not valid:"
					+ e.getMessage());
		}
	}

	/**
	 * 
	 * @return Encoding Style der Coverage-Nachrichten
	 */
	public String getEncodingStyle() {
		String style = null;
		if (operation != null) {
			try {
				style = operation.getEncodingStyle();
			} catch (SpecificationException e) {
				logger
				.debug("Encoding style problem:"
						+ e.getMessage());
//				markersRegistry.addInfo(e.getMessage());
				
			}
		}
		return style;
	}

	/**
	 * Setzt den Testfall, der gerade ausgeführt wird. Dadurch ist es möglich,
	 * die Testabdeckung von jedem Testfalls zu bestimmen.
	 * 
	 * @param testCase
	 *            Testfall, der gerade ausgeführt wird.
	 */
	public void setCurrentTestcase(String testCase) {
		this.testCase = testCase;
	}

}
