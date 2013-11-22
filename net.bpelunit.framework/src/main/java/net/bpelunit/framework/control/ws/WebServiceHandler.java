/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.control.run.TestCaseRunner;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.PartnerNotFoundException;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.ByteArrayISO8859Writer;

/**
 * The handler for incoming HTTP connections. Each incoming request is related
 * to the target PartnerTrack and passed to the framework for analysis.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class WebServiceHandler extends org.eclipse.jetty.server.handler.AbstractHandler {

	private static final int HTTP_INTERNAL_ERROR = 500;

	private Logger wsLogger = Logger.getLogger(this.getClass());

	/**
	 * The test runner handling the requests made to this web service handler
	 */
	private TestCaseRunner fRunner;

	/**
	 * Initializes this handler. It will now receive requests and pass them on
	 * to the runner
	 * 
	 * @param runner
	 *            the runner to pass requests to.
	 */
	public void initialize(TestCaseRunner runner) {
		fRunner = runner;
	}

	/**
	 * Deinitializes the handler. It will now reject requests and not pass them
	 * on to the runner.
	 * 
	 */
	public void deinitialize() {
		fRunner = null;
	}

	/**
	 * 
	 * <p>
	 * Handles one incoming HTTP request. The sequence is as follows:
	 * </p>
	 * <ul>
	 * <li>Check incoming URL for the partner track name. If none, reject.</li>
	 * <li>Find partner for the name. If none or partner has no activities,
	 * reject.</li>
	 * <li>Post incoming message with partner name to the runner. Wait for
	 * answer.</li>
	 * <li>Get incoming message from runner. Return message in HTTP Request.</li>
	 * </ul>
	 * 
	 */
	@Override
	public void handle(String pathInContext, Request rawRequest,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		wsLogger.info("Incoming request for path " + pathInContext);

		if (!request.getMethod().equals(HttpMethods.POST)) {
			wsLogger.error("Got a non-POST request - rejecting message "
					+ pathInContext);
			// no POST method
			// let default 404 handler handle this situation.
			return;
		}

		if (fRunner == null) {
			wsLogger.error("Not initialized - rejecting message for URL "
					+ pathInContext);
			// let default 404 handler handle this situaton.
			return;
		}
		// find target according to path in context

		String partnerName = getPartnerName(pathInContext);
		wsLogger
				.debug("Supposed partner name for this request: " + partnerName);

		PartnerTrack key;
		try {
			key = fRunner.findPartnerTrackForName(partnerName);
			if (key.isDone()) {
				wsLogger.info("Partner track " + partnerName + " has already finished its execution: replying with 404");
				return;
			}
		} catch (PartnerNotFoundException e1) {
			// Let default 404 handler handle this situation
			wsLogger.info(e1.getMessage());
			wsLogger.info("Rejecting message with 404.");
			return;
		}

		wsLogger.debug("A partner was found for the target URL: " + key);

		wsLogger.debug("Request method is: " + request.getMethod());

		IncomingMessage iMessage = new IncomingMessage();
		iMessage.setMessage(request.getInputStream());

		try {
			wsLogger.debug("Posting incoming message to blackboard...");
			fRunner.putWSIncomingMessage(key, iMessage);

			wsLogger.debug("Waiting for framework to supply answer...");
			final OutgoingMessage m2 = fRunner.getWSOutgoingMessage(key);

			wsLogger.debug("Got answer from framework, now sending...");

			int code = m2.getCode();
			String body = m2.getMessageAsString();

			wsLogger.debug("Answer is:\n" + body);
			for(String option : m2.getProtocolOptionNames()) {
				response.addHeader(option, m2.getProtocolOption(option));
			}
			sendResponse(response, code, body);

			wsLogger.debug("Posting \"message sent\" to framework...");
			fRunner.putWSOutgoingMessageSent(m2);

			wsLogger.info("Done handling request, result OK. " + code);

		} catch (TimeoutException e) {
			wsLogger
					.error("Timeout while waiting for framework to supply answer to incoming message");
			wsLogger
					.error("This most likely indicates a bug in the framework.");
			wsLogger.error("Sending fault.");
			sendResponse(response, HTTP_INTERNAL_ERROR, BPELUnitUtil.generateGenericSOAPFault());
		} catch (InterruptedException e) {
			wsLogger
					.error("Interrupted while waiting for framework for incoming message or answer.");
			wsLogger
					.error("This most likely indicates another error occurred.");
			wsLogger.error("Sending fault.");
			sendResponse(response, HTTP_INTERNAL_ERROR, BPELUnitUtil.generateGenericSOAPFault());
		} catch(Exception e) {
			// Debugging only
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Find the partner name in the incoming request URI. Is supposed to be the
	 * last segment of the path URI.
	 * 
	 * @param path
	 * @return
	 */
	private static String getPartnerName(String path) {

		String stringToTest = path;

		// Remove trailing slash, if any.
		stringToTest = StringUtils.removeEnd(stringToTest, "/");
		// Get last part of the URL.
		stringToTest = StringUtils.substringAfterLast(stringToTest, "/");
		return stringToTest;
	}
	
	private void sendResponse(HttpServletResponse response, int code, SOAPMessage body)
	throws IOException {
		// TODO Refactor all message serializations into an own utility method
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			body.writeTo(out);
			sendResponse(response, code, out.toString());
		} catch (SOAPException e) {
			sendResponse(response, code, "");
		}
	}

	private void sendResponse(HttpServletResponse response, int code, String body)
			throws IOException {

		response.setContentType(BPELUnitConstants.TEXT_XML_CONTENT_TYPE);
		response.setStatus(code);

		if(body == null) {
			body = "";
		}
		ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(2048);
		try {
		writer.write(body);
		writer.flush();

		response.setContentLength(writer.size());
		writer.writeTo(response.getOutputStream()); 
		} finally {
			writer.close();
			writer.destroy();
		}
	}

}
