package net.bpelunit.framework.coverage.receiver;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.ibm.wsdl.Constants;

/**
 * This class holds accountable for processing SOAP messages in the coverage
 * logging service
 * 
 * @author Alex Salnikow, Ronald Becher
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
	 * Receives SOAP messages with coverage markers while testing
	 * 
	 * @param message
	 *            with markers
	 */
	public synchronized void putMessage(String message) {
		if (encoder != null && operation != null) {
			Element element = null;
			SOAPMessage fSOAPMessage;
			try {
				fSOAPMessage = MessageFactory.newInstance()
						.createMessage(null,
								new ByteArrayInputStream(message.getBytes()));

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
	 * Sets SOAP encoder for decoding messages with markers
	 * 
	 * @param encoder
	 */
	public void setSOAPEncoder(ISOAPEncoder encoder) {
		this.encoder = encoder;
	}

	/**
	 * Sets path to WDL
	 * 
	 * @param wsdl
	 *            WSDL-Beschreibung des Coverage Logging Services
	 */
	// TODO Path oder beschreibung?
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
			logger.debug("WSDL of Coverage Logging Service is not valid:"
					+ e.getMessage());
		} catch (WSDLException e) {
			logger.debug("WSDL of Coverage Logging Service is not valid:"
					+ e.getMessage());
		}
	}

	/**
	 * Gets coverage messages encoding style
	 * 
	 * @return encoding style
	 */
	public String getEncodingStyle() {
		String style = null;
		if (operation != null) {
			try {
				style = operation.getEncodingStyle();
			} catch (SpecificationException e) {
				logger.debug("Encoding style problem:" + e.getMessage());
			}
		}
		return style;
	}

	/**
	 * Sets the currently processed test case.
	 * 
	 * <br />This holds accountable for determining coverage of test cases
	 * 
	 * @param currently
	 *            processed test case
	 */
	public void setCurrentTestcase(String testCase) {
		this.testCase = testCase;
	}

}
