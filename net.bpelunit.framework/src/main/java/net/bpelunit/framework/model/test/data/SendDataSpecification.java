/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.exception.SOAPEncodingException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ActivityContext;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.rits.cloning.Cloner;

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
	 * Delay for this send specification (if any)
	 */
	private int fDelay;

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

	// For deep cloning contexts so they are isolated from each other
	private static final Cloner fCloner = new Cloner();

	// ******************** Initialization ************************

	public SendDataSpecification(Activity parent, NamespaceContext nsContext) throws SpecificationException {
		super(parent, nsContext);
	}

	public void initialize(SOAPOperationCallIdentifier operation, int delay, String targetURL, String soapAction, String encodingStyle,
			ISOAPEncoder encoder, Element rawDataRoot, String dataTemplate, QName faultCode, String faultString) {
		fOperation= operation;
		fLiteralData= rawDataRoot;
		fDataTemplate= dataTemplate;

		fSOAPHTTPAction= soapAction;
		fTargetURL= targetURL;
		fEncodingStyle= encodingStyle;
		fEncoder= encoder;

		fDelay= delay;
		fFaultCode= faultCode;
		fFaultString= faultString;
	}

	// ******************** Implementation ***************************

	public void handle(ActivityContext context) {

		// Expand template into literal data if there is one
		if (fDataTemplate != null) {
			expandTemplate(context);
		}
		if (hasProblems()) return;

		// Insert mapping data from context (if any)
		insertMappingData(context);

		// Set up the send call
		encodeMessage();

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

		createWireFormat();

		if (hasProblems())
			return;

		fStatus= ArtefactStatus.createPassedStatus();
	}

	private void expandTemplate(ActivityContext context) {
		try {
			VelocityContext velocityCtx = fCloner.deepClone(context.createVelocityContext());
			velocityCtx.put("xpath", new XPathTool(this.fNamespaceContext));

			// Expand the template as a regular string
			StringWriter writer = new StringWriter();
			Velocity.evaluate(velocityCtx, writer, "expandTemplate", fDataTemplate);
			String expandedTemplate = writer.toString();

			// Parse back to a DOM XML element
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document docExpanded = dbf.newDocumentBuilder().parse(
					new InputSource(new StringReader(expandedTemplate)));
			fLiteralData = docExpanded.getDocumentElement();
			context.saveSentMessage(fLiteralData);
		} catch (Exception ex) {
			fStatus = ArtefactStatus.createErrorStatus("Template expansion fault: "
					+ ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * Delays execution for a specified delay. Should be executed inside a block with other
	 * interruptable methods
	 * 
	 * @throws InterruptedException
	 */
	public void delay() throws InterruptedException {
		if (fDelay > 0) {
			Logger.getLogger(getClass()).info("Delaying send for " + fDelay + " seconds...");
			Thread.sleep(fDelay * 1000);
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


	// ************************* Inner Stuff ***********************

	private void insertMappingData(ActivityContext context) {

		List<DataCopyOperation> mapping= context.getMapping();
		if (mapping != null) {
			for (DataCopyOperation copy : mapping) {
				copy.setTextNodes(fLiteralData, fNamespaceContext);
				if (copy.isError()) {
					fStatus= ArtefactStatus.createErrorStatus("An error occurred while evaluating Copy-To-XPath expression.");
					return;
				}
			}
		}
	}

	private void encodeMessage() {
		try {
			fSOAPMessage= fEncoder.construct(fOperation, fLiteralData, fFaultCode, fFaultString);
		} catch (SOAPEncodingException e) {
			fStatus= ArtefactStatus.createErrorStatus("Encoding the message failed: " + e.getMessage(), e);
		}
	}

	private void createWireFormat() {
		try {
			ByteArrayOutputStream b= new ByteArrayOutputStream();
			fSOAPMessage.writeTo(b);
			fPlainMessage= b.toString();
		} catch (Exception e) {
			fStatus= ArtefactStatus.createErrorStatus("Error serializing SOAP message: " + e.getMessage(), e);
		}
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
		stateData.addAll(fStatus.getAsStateData());
		if (fTargetURL != null) {
			stateData.add(new StateData("Target URL", fTargetURL));
			stateData.add(new StateData("HTTP Action", fSOAPHTTPAction));
		}
		stateData.add(new StateData("Style/Encoding", fEncodingStyle));
		stateData.add(new StateData("Direction", fOperation.getDirection().name()));
		return stateData;
	}

}
