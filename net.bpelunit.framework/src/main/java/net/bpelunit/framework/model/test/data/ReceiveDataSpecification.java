/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.framework.model.test.activity.VelocityContextProvider;
import net.bpelunit.framework.model.test.data.extraction.DataExtraction;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.model.test.wire.IncomingMessage;

import org.apache.velocity.context.Context;
import org.w3c.dom.Element;

/**
 * The receive data specification is a data package which contains all necessary information to
 * decode a message from SOAP to literal data, handling style, decoding, possible header processing,
 * and data replacement through mapping.
 * 
 * A receive data specification may be used in an answering SOAP message (i.e., as a response in a
 * request/response transaction or a response-only transaction) or to process an initiating SOAP
 * message (i.e., a request in a request/response ) - both synchronously and asynchronously.
 * 
 * 
 * @version $Id$
 * @author Philip Mayer, Antonio Garcia-Dominguez
 * 
 */
public class ReceiveDataSpecification extends DataSpecification {

	/**
	 * The actual SOAP operation used for the receive.
	 */
	private SOAPOperationCallIdentifier fOperation;

	/**
	 * Style and encoding of the operation.
	 */
	private String fEncodingStyle;

	/**
	 * The decoder to be used for decoding the plain message.
	 */
	private ISOAPEncoder fDecoder;

	/**
	 * The plain incoming message.
	 */
	private String fPlainMessage;

	/**
	 * The SOAP-Encoded message.
	 */
	private SOAPMessage fSOAPMessage;

	/**
	 * The literal XML data received.
	 */
	private Element fLiteralData;

	/**
	 * A list of conditions which must be checked against the received data.
	 */
	private List<ReceiveCondition> fConditions;

	/**
	 * A list of data extraction requests that have to be performed on the incoming messages.
	 */
	private List<DataExtraction> fDataExtractions;
	
	/**
	 * Expected SOAP fault code (if this is a fault)
	 */
	private QName fFaultCode;

	/**
	 * Expected SOAP fault string (if this is a fault)
	 */
	private String fFaultString;

	// ********************** Initialization ***************************

	public ReceiveDataSpecification(Activity parent, NamespaceContext nsContext) throws SpecificationException {
		super(parent, nsContext);
	}

	public void initialize(SOAPOperationCallIdentifier op, String encodingStyle, ISOAPEncoder encoder, List<ReceiveCondition> conditions,
			List<DataExtraction> deList, QName faultCode, String faultString) throws SpecificationException {
		fOperation= op;
		fConditions= conditions;
		fDataExtractions= deList;
		fDecoder= encoder;
		fEncodingStyle= encodingStyle;

		fFaultCode= faultCode;
		fFaultString= faultString;
	}

	// ******************** Implementation ***************************

	/**
	 * 
	 * Handles the given incoming message.
	 * 
	 * @param context
	 * @param incomingMessage
	 */
	public void handle(ActivityContext context, IncomingMessage msg) {

		// Check content
		setInWireFormat(msg);

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

		decodeMessage();

		if (hasProblems()) {
			return;
		}

		extractMappingData(context);

		if (hasProblems()) {
			return;
		}

		context.saveReceivedMessage(fLiteralData);
		validateConditions(context);
		if (hasProblems()) {
			return;
		}

		extractData(context, fDataExtractions, fLiteralData);
		if (hasProblems()) {
			return;
		}

		// Receive completed.
		setStatus(ArtefactStatus.createPassedStatus());
	}

	public SOAPMessage getSOAPMessage() {
		return fSOAPMessage;
	}

	// ************************* Inner Stuff ***********************

	private void setInWireFormat(IncomingMessage msg) {
		try {
			fSOAPMessage= msg.getMessage();
			fPlainMessage= msg.getMessageAsString();
		} catch (Exception e) {
			setStatus(ArtefactStatus.createErrorStatus("Could not create SOAP message from incoming message: " + e.getMessage(), e));
		}
	}

	private void decodeMessage() {
		try {
			fLiteralData= fDecoder.deconstruct(fOperation, fSOAPMessage);
		} catch (SOAPEncodingException e) {
			setStatus(ArtefactStatus.createErrorStatus("Not able to deconstruct incoming message into SOAP Message: " + e.getMessage(), e));
		}
	}

	private void validateConditions(VelocityContextProvider templateContext) {
		// Check implicit fault assertions
		SOAPBody body;
		try {
			body = fSOAPMessage.getSOAPBody();
		} catch (SOAPException e) {
			setStatus(ArtefactStatus.createErrorStatus(
				"Exception during condition validation", e));
			return;
		}
		if (fOperation.isFault()) {
			SOAPFault fault = body.getFault();
			if (fault == null) {
				setStatus(ArtefactStatus.createFailedStatus(
					"A fault was expected in operation "
					+ this
					+ ", but none was found in input data."));
				return;
			}
			if (fFaultCode != null && !fFaultCode.equals(fault.getFaultCodeAsQName())) {
				setStatus(ArtefactStatus.createFailedStatus(String.format(
					"Expected the fault code %s, got %s instead",
					fFaultCode, fault.getFaultCodeAsQName())));
				return;
			}
			if (fFaultString != null && !fFaultString.equals(fault.getFaultString())) {
				setStatus(ArtefactStatus.createFailedStatus(String.format(
					"Expected the fault string %s, got %s instead",
					fFaultString, fault.getFaultString())));
				return;
			}
		}
		else if (body.getFault() != null) {
			setStatus(ArtefactStatus.createFailedStatus(
				"The operation "
				+ this
				+ " was expected to succeed, but replied with a SOAP fault."));
			return;
		}

		// Create Velocity context for the conditions
		Context conditionContext;
		try {
			conditionContext = templateContext.createVelocityContext(this);
		} catch (Exception e) {
			setStatus(ArtefactStatus.createErrorStatus(String.format(
				"Could not create the Velocity context for this condition: %s",
				e.getLocalizedMessage()), e));
			return;
		}
		ContextXPathVariableResolver variableResolver = new ContextXPathVariableResolver(conditionContext);

		for (ReceiveCondition c : fConditions) {
			c.evaluate(templateContext, fLiteralData, getNamespaceContext(), variableResolver);

			if (c.isFailure()) {
				if(! getStatus().isError()) {
				setStatus(ArtefactStatus.createFailedStatus(String.format(
						"Condition '%s=%s' did not hold: %s",
						c.getExpression(), c.getExpectedValue(), c.getStatus()
								.getMessage())));
				}
			} else if (c.isError()) {
				setStatus(ArtefactStatus.createErrorStatus(String.format(
						"Condition '%s=%s' had an error: %s.", c
								.getExpression(), c.getExpectedValue(), c
								.getStatus().getMessage())));
			}
		}
	}

	/**
	 * Extract data from the received message according to the copy/mapping
	 * instructions of the context on the <code>mapping</code> element. This
	 * enables BPELUnit to copy a string of text from a source element of the
	 * incoming message of a two-way activity to the target element of the outgoing
	 * message in the same activity.
	 *
	 * For more advanced scenarios involving different activities in different tracks
	 * or that require copying something more complex than a string, use the
	 * <code>dataExtraction</code> elements, which are interpreted from
	 * {@link #extractData(ActivityContext).
	 */
	private void extractMappingData(ActivityContext context) {
		List<DataCopyOperation> mapping= context.getMapping();
		if (mapping != null) {
			for (DataCopyOperation copy : mapping) {
				copy.retrieveTextNodes(fLiteralData, getNamespaceContext());
				if (copy.isError()) {
					setStatus(ArtefactStatus.createErrorStatus("An error occurred while evaluating Copy-From-XPath expression."));
					return;
				}
			}
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
		return "Receive Data Package";
	}

	public List<ITestArtefact> getChildren() {
		List<ITestArtefact> returner = new ArrayList<ITestArtefact>();
		for (ReceiveCondition c : fConditions) {
			returner.add(c);
		}
		for (DataExtraction de : fDataExtractions) {
			returner.add(de);
		}
		returner.add(new XMLData(this, "Plain incoming message", getWireFormatAsString()));
		returner.add(new XMLData(this, "SOAP Message data", getSOAPMessageDataAsString()));
		returner.add(new XMLData(this, "Literal XML data", getLiteralDataAsString()));

		return returner;
	}

	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(getStatus().getAsStateData());
		stateData.add(new StateData("Style/Encoding", fEncodingStyle));
		stateData.add(new StateData("Direction", fOperation.getDirection().name()));
		return stateData;
	}

}
