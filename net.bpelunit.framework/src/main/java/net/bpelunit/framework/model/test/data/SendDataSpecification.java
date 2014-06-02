/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;

import org.apache.log4j.Logger;
import org.apache.velocity.context.Context;
import org.w3c.dom.Element;


/**
 * The send data specification is a data package which contains all necessary information to encode
 * a message from literal data to SOAP, handling style, encoding, possible header processing, and
 * data replacement through mapping.
 * 
 * A send data specification may be used in an initiating SOAP message (i.e., as a request in a
 * request/response or request only transaction) or in an answering SOAP message (i.e., as a
 * response in a request/response transaction) - both synchronously and asynchronously.
 * 
 * In all cases except as a response in a synchronous request/response message, the send spec will
 * know about its Target URL and SOAP Action. As the response in a synchronous request/response is
 * handled inside an alreay open HTTP request, no url and soap action are required.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SendDataSpecification extends DataSpecification {

	/**
	 * The actual SOAP operation used for the send.
	 */
	private SOAPOperationCallIdentifier fOperation;

	/**
	 * Style and encoding of the operation.
	 */
	private String fEncodingStyle;

	/**
	 * The encoder to be used for encoding the literal data
	 */
	private ISOAPEncoder fEncoder;

	/**
	 * The plain outgoing message.
	 */
	private String fPlainMessage;

	/**
	 * The SOAP-Encoded message.
	 */
	private SOAPMessage fSOAPMessage;

	/**
	 * The literal XML data to be sent.
	 */
	private Element fLiteralData;

	/**
	 * SOAP Action. Null in case of receive part of synchronous request/response.
	 */
	private String fSOAPHTTPAction;

	/**
	 * Target URL. Null in case of receive part of synchronous request/response.
	 */
	private String fTargetURL;

	/**
	 * Constant delay for this send specification (if any).
	 */
	private double fDelay;

	/**
	 * Expression to be used to initialize the constant delay in fDelay, when equal to zero.
	 * @see #getDelay()
	 */
	private String fDelayExpression;

        /**
         * Fault code to be reported, if this is a fault.
         */
        private QName fFaultCode;

        /**
         * Fault string to be reported, if this is a fault.
         */
        private String fFaultString;

        /**
         * If no literal data is available, this Velocity template will be used to produce the data to be sent.
         */
		private String fDataTemplate;

		/**
		 * Options to be set for the underlaying transport protocol (ATM HTTP only)
		 */
		private Map<String, String> protocolOptions = new HashMap<String, String>();
		
	// ******************** Initialization ************************

	public SendDataSpecification(Activity parent, NamespaceContext nsContext) throws SpecificationException {
		super(parent, nsContext);
	}

	public void initialize(SOAPOperationCallIdentifier operation, double delay, String delayExpression, String targetURL, String soapAction, String encodingStyle,
			ISOAPEncoder encoder, Element rawDataRoot, String dataTemplate, QName faultCode, String faultString) {
		fOperation= operation;
		fLiteralData= rawDataRoot;
		fDataTemplate= dataTemplate;

		fSOAPHTTPAction= soapAction;
		fTargetURL= targetURL;
		fEncodingStyle= encodingStyle;
		fEncoder= encoder;

		setDelay(delay);
		setDelayExpression(delayExpression);
		fFaultCode= faultCode;
		fFaultString= faultString;
	}

	// ******************** Implementation ***************************

	public void handle(ActivityContext context) {

		// Expand template into literal data if there is one
		if (fDataTemplate != null) {
			fLiteralData = generateLiteralDataFromTemplate(context, fDataTemplate);
		}
		if (hasProblems()) {
			return;
		}

		// Insert mapping data from context (if any)
		insertMappingData(context);

		// Set up the send call
		encodeMessage();

		if (hasProblems()) {
			return;
		}

		try {
			context.processHeaders(this);
		} catch (HeaderProcessingException e) {
			setStatus(ArtefactStatus.createErrorStatus("Header Processing Fault.", e));
			return;
		}

		if (hasProblems()) {
			return;
		}

		createWireFormat();

		if (hasProblems()) {
			return;
		}

		setStatus(ArtefactStatus.createPassedStatus());
	}

	/**
	 * Delays execution for a specified delay. Should be executed inside a block with other
	 * interruptable methods
	 * @param context Activity context for the running specification.
	 * @throws InterruptedException 
	 * @throws XPathExpressionException 
	 * @throws DataSourceException 
	 * @throws Exception Could not compute the delay from the XPath expression inside the delay attribute.
	 */
	public void delay(ActivityContext context) throws DataSourceException, XPathExpressionException, InterruptedException {
		if (getDelay(context) > 0) {
			Logger.getLogger(getClass()).info("Delaying send for " + getDelay(context) + " seconds...");
			Thread.sleep((int)(getDelay(context) * 1000));
		}
	}

	public String getTargetURL() {
		return fTargetURL;
	}

	public void setTargetURL(String targetURL) {
		fTargetURL= targetURL;
	}

	public String getSOAPHTTPAction() {
		return fSOAPHTTPAction;
	}

	public SOAPMessage getSOAPMessage() {
		return fSOAPMessage;
	}

	public String getInWireFormat() {
		return fPlainMessage;
	}

	public boolean isFault() {
		return fOperation.isFault();
	}

	public void setDelay(double fDelay) {
		this.fDelay = fDelay;
	}

	public double getDelay(ActivityContext activityContext) throws DataSourceException, XPathExpressionException {
		if (getDelayExpression() != null) {
			final Context vtlContext = activityContext.createVelocityContext(this);
			final ContextXPathVariableResolver xpathResolver = new ContextXPathVariableResolver(vtlContext);

			final XPath xpath = XPathFactory.newInstance().newXPath();
			xpath.setNamespaceContext(getNamespaceContext());
			xpath.setXPathVariableResolver(xpathResolver);

			// We should only evaluate these expressions once per row and round
			fDelay = (Double)xpath.evaluate(getDelayExpression(), fLiteralData, XPathConstants.NUMBER);
			setDelayExpression(null);
		}
		return fDelay;
	}

	public void setDelayExpression(String fDelayExpression) {
		this.fDelayExpression = fDelayExpression;
	}

	public String getDelayExpression() {
		return fDelayExpression;
	}

	// ************************* Inner Stuff ***********************

	private void insertMappingData(ActivityContext context) {

		List<DataCopyOperation> mapping= context.getMapping();
		if (mapping != null) {
			for (DataCopyOperation copy : mapping) {
				copy.setTextNodes(fLiteralData, getNamespaceContext());
				if (copy.isError()) {
					setStatus(ArtefactStatus.createErrorStatus("An error occurred while evaluating Copy-To-XPath expression."));
					return;
				}
			}
		}
	}

	private void encodeMessage() {
		try {
			fSOAPMessage= fEncoder.construct(fOperation, fLiteralData, fFaultCode, fFaultString);
		} catch (SOAPEncodingException e) {
			setStatus(ArtefactStatus.createErrorStatus("Encoding the message failed: " + e.getMessage(), e));
		}
	}

	private void createWireFormat() {
		try {
			ByteArrayOutputStream b= new ByteArrayOutputStream();
			fSOAPMessage.writeTo(b);
			fPlainMessage= b.toString();
		} catch (Exception e) {
			setStatus(ArtefactStatus.createErrorStatus("Error serializing SOAP message: " + e.getMessage(), e));
		}
	}

	private String getWireFormatAsString() {
		if (fPlainMessage != null) {
			return fPlainMessage;
		} else {
			return "(no data)";
		}
	}

	private String getLiteralDataAsString() {
		if (fLiteralData != null) {
			return BPELUnitUtil.toFormattedString(fLiteralData.getOwnerDocument());
		}
		return "(no data)";
	}

	private String getSOAPMessageDataAsString() {
		if (fSOAPMessage != null) {
			return BPELUnitUtil.toFormattedString(fSOAPMessage.getSOAPPart());
		}
		return "(no message)";
	}


	// ************************** ITestArtefact ************************

	public String getName() {
		return "Send Data Package";
	}

	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> returner= new ArrayList<ITestArtefact>();
		returner.add(new XMLData(this, "Literal XML data", getLiteralDataAsString()));
		returner.add(new XMLData(this, "SOAP Message data", getSOAPMessageDataAsString()));
		returner.add(new XMLData(this, "Plain outgoing message", getWireFormatAsString()));
		return returner;
	}

	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(getStatus().getAsStateData());
		if (fTargetURL != null) {
			stateData.add(new StateData("Target URL", fTargetURL));
			stateData.add(new StateData("HTTP Action", fSOAPHTTPAction));
		}
		stateData.add(new StateData("Style/Encoding", fEncodingStyle));
		stateData.add(new StateData("Direction", fOperation.getDirection().name()));
		return stateData;
	}

	public void putProtocolOption(String name, String value) {
		this.protocolOptions.put(name, value);
	}
	
	public String getProtocolOption(String name) {
		return protocolOptions.get(name);
	}
	
	public String[] getProtocolOptionNames() {
		return protocolOptions.keySet().toArray(new String[protocolOptions.size()]);
	}
}
