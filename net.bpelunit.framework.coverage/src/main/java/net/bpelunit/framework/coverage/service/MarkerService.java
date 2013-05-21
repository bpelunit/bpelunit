package net.bpelunit.framework.coverage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.instrumentation.AbstractInstrumenter;
import net.bpelunit.util.XMLUtil;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.soap.MessageFactory;
import org.apache.xmlbeans.impl.soap.SOAPMessage;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.w3c.dom.Element;

public class MarkerService extends AbstractHandler {

	private List<AbstractInstrumenter> instrumenters = new ArrayList<AbstractInstrumenter>();

	private Logger logger = Logger.getLogger(getClass());

	public MarkerService(
			List<? extends AbstractInstrumenter> coverageInstrumenters) {
		instrumenters.addAll(coverageInstrumenters);
	}

	public void handle(String pathInContext, Request pathParams,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (pathInContext
				.endsWith(CoverageConstants.COVERAGE_SERVICE_BPELUNIT_NAME)
				&& request.getMethod().equals("POST")) {
			try {
				MessageFactory factory = MessageFactory.newInstance();
				// TODO FIX CHARSET
				SOAPMessage message = factory.createMessage(null,
						request.getInputStream());
				Element msgElement = XMLUtil.getChildElementsByName(
						message.getSOAPBody(),
						CoverageConstants.COVERAGE_MSG_ELEMENT).get(0);
				for (Element markerElement : XMLUtil.getChildElementsByName(
						msgElement,
						CoverageConstants.COVERAGE_MSG_MARKER_ELEMENT)) {
					for (AbstractInstrumenter ai : instrumenters) {
						ai.pushMarker(XMLUtil
								.getContentsOfTextOnlyNode(markerElement));
					}
				}
				response.setStatus(202);
				response.setContentLength(0);
			} catch (Exception e) {
				logger.warn("Exception while processing marker message: ", e);
			}
		}
	}
}
