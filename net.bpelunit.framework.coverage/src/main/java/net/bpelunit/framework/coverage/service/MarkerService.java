package net.bpelunit.framework.coverage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.instrumentation.AbstractInstrumenter;
import net.bpelunit.util.XMLUtil;

import org.apache.log4j.Logger;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class MarkerService extends AbstractHttpHandler {

	private List<AbstractInstrumenter> instrumenters = new ArrayList<AbstractInstrumenter>();

	private Logger logger = Logger.getLogger(getClass());
	
	public MarkerService(List<?extends AbstractInstrumenter> coverageInstrumenters) {
		instrumenters.addAll(coverageInstrumenters);
	}
	
	// TODO Fix restriction warning
	@SuppressWarnings("restriction")
	public void handle(String pathInContext, String pathParams,
			HttpRequest request, HttpResponse response) throws IOException {

		if (pathInContext
				.endsWith(CoverageConstants.COVERAGE_SERVICE_BPELUNIT_NAME)
				&& request.getMethod().equals("POST")) {
			try {
				MessageFactory factory = MessageFactory.newInstance();
				// TODO FIX CHARSET
				SOAPMessage message = factory.createMessage(null,
						request.getInputStream());
				Element msgElement = XMLUtil.getChildElementsByName(message.getSOAPBody(), CoverageConstants.COVERAGE_MSG_ELEMENT).get(0);
				for(Element markerElement : XMLUtil.getChildElementsByName(msgElement, CoverageConstants.COVERAGE_MSG_MARKER_ELEMENT)) {
					for(AbstractInstrumenter ai : instrumenters) {
						ai.pushMarker(XMLUtil.getContentsOfTextOnlyNode(markerElement));
					}
				}
				response.setStatus(202);
				response.setContentLength(0);
			} catch (Exception e) {
				logger.warn("Exception while processing marker message: " , e);
			}
		}
	}
}
