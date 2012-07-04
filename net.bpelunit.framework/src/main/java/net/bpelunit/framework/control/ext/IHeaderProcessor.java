/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.ext;

import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.exception.HeaderProcessingException;
import net.bpelunit.framework.model.test.activity.ActivityContext;

/**
 * 
 * The IHeaderProcessor interface represents a SOAP Header Processor and is intended to be
 * implemented by concrete processors which, in turn, implement header protocols like WSA Adressing.
 * 
 * For every activity (like, for example, send/receive asynchronous), a new instance of a header
 * processor will be created by the framework.
 * 
 * A header processor may be specified at any given activity. The order in which the
 * {@link #processSend(ActivityContext, SendPackage)} and
 * {@link #processReceive(ActivityContext, SOAPMessage)} methods are called depends on that
 * activity.
 * 
 * Properties defined by the tester in the test specifications are set via
 * {@link #setProperty(String, String)}. All properties are guaranteed to be set before any of the
 * other methods are called.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface IHeaderProcessor {

	/**
	 * Name of the context value for the ID of the receive message.
	 * This context value name is independent from the specific technology:
	 * basically, we should reject asynchronous incoming messages if
	 * {@link #MSG_RECEIVED_ID} does not match {@link #MSG_SENT_ID}.
	 */
	String MSG_RECEIVED_ID = "Message-ID-Receive";

	/** See {@link #MSG_RECEIVED_ID}. */
	String MSG_SENT_ID = "Message-ID-Sent";

	/**
	 * Sets a property given by the tester in the test specification.
	 * 
	 * @param name name of the property
	 * @param value value of the property
	 */
	void setProperty(String name, String value);

	/**
	 * Process a SOAP Message which is intended to be sent to a web service.
	 * 
	 * @param context activity context
	 * @param sendPackage the data to be sent
	 * @throws HeaderProcessingException
	 */
	void processSend(ActivityContext context, SendPackage sendPackage) throws HeaderProcessingException;

	/**
	 * Process a SOAP Message which has been received from a web service.
	 * 
	 * @param context activity context
	 * @param receivedPackage the received data
	 * @throws HeaderProcessingException
	 */
	void processReceive(ActivityContext context, SOAPMessage receivedPackage) throws HeaderProcessingException;

}
