/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.model.test.data;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.util.BPELUnitUtil;
import org.bpelunit.framework.exception.HeaderProcessingException;
import org.bpelunit.framework.exception.SOAPEncodingException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.report.ArtefactStatus;
import org.bpelunit.framework.model.test.report.ITestArtefact;
import org.bpelunit.framework.model.test.report.StateData;
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
	 * Namespace Context
	 */
	private NamespaceContext fNamespaceContext;

	/**
	 * Expected SOAP fault code (if this is a fault)
	 */
	private QName fFaultCode;

	/**
	 * Expected SOAP fault string (if this is a fault)
	 */
	private String fFaultString;

	// ********************** Initialization ***************************

	public ReceiveDataSpecification(Activity parent) throws SpecificationException {
		super(parent);
	}

	public void initialize(SOAPOperationCallIdentifier op, String encodingStyle, ISOAPEncoder encoder, List<ReceiveCondition> conditions,
			NamespaceContext context, QName faultCode, String faultString) throws SpecificationException {
		fOperation= op;
		fConditions= conditions;
		fNamespaceContext= context;
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
	public void handle(ActivityContext context, String incomingMessage) {

		// Check content
		setInWireFormat(incomingMessage);

		if (hasProblems())
			return;

		try {
			context.processHeaders(this);
		} catch (HeaderProcessingException e) {
			fStatus= ArtefactStatus.createErrorStatus("Header Processing Fault.", e);
			return;
		}

		if (hasProblems())
			return;

		decodeMessage();

		if (hasProblems())
			return;

		extractMappingData(context);

		if (hasProblems())
			return;

		// If the message is a SOAP fault, but this receive did not expect a fault,
		// log as an error
		if (hasMessageFault() && !fOperation.isFault()) {
			fStatus= ArtefactStatus.createErrorStatus("Received a SOAP fault although none was expected.");
			return;
		}

		validateConditions();
		if (hasProblems())
			return;

		// Receive completed.
		fStatus= ArtefactStatus.createPassedStatus();
	}

	public SOAPMessage getSOAPMessage() {
		return fSOAPMessage;
	}

	// ************************* Inner Stuff ***********************

	private void setInWireFormat(String body) {
		try {
			fPlainMessage= body;
			MessageFactory factory= BPELUnitUtil.getMessageFactoryInstance();
			fSOAPMessage= factory.createMessage(null, new ByteArrayInputStream(body.getBytes()));
		} catch (Exception e) {
			fStatus= ArtefactStatus.createErrorStatus("Could not create SOAP message from incoming message: " + e.getMessage(), e);
		}
	}

	private void decodeMessage() {
		try {
			fLiteralData= fDecoder.deconstruct(fOperation, fSOAPMessage);
		} catch (SOAPEncodingException e) {
			fStatus= ArtefactStatus.createErrorStatus("Not able to deconstruct incoming message into SOAP Message: " + e.getMessage(), e);
		}
	}

	private void validateConditions() {

		for (ReceiveCondition c : fConditions) {
			c.evaluate(fLiteralData, fNamespaceContext);

			if (c.isFailure()) {
				fStatus = ArtefactStatus.createFailedStatus(String.format(
						"Condition '%s=%s' did not hold: %s",
						c.getExpression(), c.getExpectedValue(), c.getStatus()
								.getMessage()));
				break;
			} else if (c.isError()) {
				fStatus = ArtefactStatus.createErrorStatus(String.format(
						"Condition '%s=%s' had an error: %s.", c
								.getExpression(), c.getExpectedValue(), c
								.getStatus().getMessage()));
				break;
			}
		}
	}

	/**
	 * Extract data from the received message according to the copy/mapping instructions of the
	 * context
	 * 
	 */
	private void extractMappingData(ActivityContext context) {
		List<DataCopyOperation> mapping= context.getMapping();
		if (mapping != null) {
			for (DataCopyOperation copy : mapping) {
				copy.retrieveTextNodes(fLiteralData, fNamespaceContext);
				if (copy.isError()) {
					fStatus= ArtefactStatus.createErrorStatus("An error occurred while evaluating Copy-From-XPath expression.");
					return;
				}
			}
		}
	}

	private boolean hasMessageFault() {
		boolean fault= false;
		try {
			fault= fSOAPMessage.getSOAPBody().hasFault();
		} catch (SOAPException e) {
			// should not happen
		}
		return fault;
	}


	private String getWireFormatAsString() {
		if (fPlainMessage != null) {
			return fPlainMessage;
		} else
			return "(no data)";
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
		List<ITestArtefact> returner= new ArrayList<ITestArtefact>();
		for (ReceiveCondition c : fConditions)
			returner.add(c);
		returner.add(new XMLData(this, "Plain incoming message", getWireFormatAsString()));
		returner.add(new XMLData(this, "SOAP Message data", getSOAPMessageDataAsString()));
		returner.add(new XMLData(this, "Literal XML data", getLiteralDataAsString()));

		return returner;
	}

	public List<StateData> getStateData() {
		List<StateData> stateData= new ArrayList<StateData>();
		stateData.addAll(fStatus.getAsStateData());
		stateData.add(new StateData("Style/Encoding", fEncodingStyle));
		stateData.add(new StateData("Direction", fOperation.getDirection().name()));
		return stateData;
	}



}
