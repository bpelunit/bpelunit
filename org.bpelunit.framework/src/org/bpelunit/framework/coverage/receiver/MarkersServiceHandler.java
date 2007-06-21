package org.bpelunit.framework.coverage.receiver;

/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.util.BPELUnitConstants;
import org.bpelunit.framework.coverage.CoverageConstants;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.util.ByteArrayISO8859Writer;

/**
 * The handler for incoming HTTP connections. Each incoming request is related
 * to the target PartnerTrack and passed to the framework for analysis.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class MarkersServiceHandler extends AbstractHttpHandler {

	private Logger wsLogger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = -2402788148972993151L;




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
	public void handle(String pathInContext, String pathParams,
			HttpRequest request, HttpResponse response) throws HttpException,
			IOException {

		wsLogger.info("!!!!!!!!!!!!!Incoming request for path " + pathInContext);

		if (!request.getMethod().equals(HttpRequest.__POST)) {
			wsLogger.error("Got a non-POST request - rejecting message "
					+ pathInContext);
			// no POST method
			// let default 404 handler handle this situation.
			return;
		}

		String partnerName = getPartnerName(pathInContext);

		if (partnerName.equals(CoverageConstants.SERVICE_NAME)) {
			sendResponse(response, 200, "");
				StringBuffer buf = readRequest(request);
				BPELUnitRunner.getCoverageMeasurmentTool().putMessage(buf.toString());
	 
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

	private static StringBuffer readRequest(HttpRequest request)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String line;
		StringBuffer theRequest = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			theRequest.append(line);
		}
		return theRequest;
	}

	private void sendResponse(HttpResponse response, int code, String body)
			throws IOException {

		response.setContentType(BPELUnitConstants.TEXT_XML_CONTENT_TYPE);
		response.setStatus(code);

		ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(2048);
		writer.write(body);
		writer.flush();

		response.setContentLength(writer.size());
		writer.writeTo(response.getOutputStream());
		writer.destroy();
	}

}