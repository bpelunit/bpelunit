package net.bpelunit.framework.coverage;

import javax.xml.namespace.QName;

public final class CoverageConstants {

	public static final String MARKER_SERVICE_NAMESPACE = "http://www.bpelunit.net/services/marker";
	public static final String VARIABLE_MARK_REQUEST = "__BPELUNIT_MARK_REQUEST__";
	public static final QName COVERAGE_PARTNERLINK_TYPE = new QName(
			MARKER_SERVICE_NAMESPACE, "CoveragePLT");
	public static final String COVERAGE_MSG_ELEMENT = "mark";
	public static final String COVERAGE_MSG_MARKER_ELEMENT = "Marker";
	public static final String COVERAGE_SERVICE_MARK_OPERATION = "mark";
	public static final QName COVERAGE_SERVICE_SERVICE = new QName(MARKER_SERVICE_NAMESPACE, "MarkerService");
	public static final String COVERAGE_SERVICE_PORT = "MarkerServiceSOAP";

	public static final String INSTRUMENTATION_SCOPE_NAME_PREFIX = "INSTRUMENTATION_";
	
	public static final QName MARKER_SERVICE_MARK_REQUEST_MESSAGE_TYPE = new QName(MARKER_SERVICE_NAMESPACE, "mark");
	public static final String MARKER_SERVICE_MARK_REQUEST_PART = "parameters";
	public static final String MARKER_SERVICE_PARTNERLINK = "__BPELUNIT_MARKERSERVICE_PL";
	public static final String MARKER_SERVICE_PARTNERLINK_PARTNERROLE = "BPELUnit";
	/** Name of the "partner track" that BPELUnit uses for collecting coverage information */ 
	public static final String COVERAGE_SERVICE_BPELUNIT_NAME = "__BPELUNIT_COVERAGE__";
	
	public static final String WSDL_NAME = "MarkerService.wsdl"; 
	public static final String XSD_NAME = "MarkerService.xsd"; 
	
	private CoverageConstants() {
		
	}
	
}
