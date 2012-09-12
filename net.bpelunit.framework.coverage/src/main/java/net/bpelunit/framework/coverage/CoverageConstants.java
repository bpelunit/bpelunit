package net.bpelunit.framework.coverage;

import javax.xml.namespace.QName;

public final class CoverageConstants {

	private CoverageConstants() {
		
	}
	
	public static final String MARKER_SERVICE_NAMESPACE = "http://www.bpelunit.net/services/marker";
	public static final QName MARKER_SERVICE_MARK_REQUEST_MESSAGE_TYPE = new QName(MARKER_SERVICE_NAMESPACE, "mark");
	public static final String MARKER_SERVICE_MARK_REQUEST_PART = "parameters";
	public static final String MARKER_SERVICE_PARTNERLINK = "__BPELUNIT_MARKERSERVICE_PL";
	public static final String MARKER_SERVICE_PARTNERLINK_PARTNERROLE = "BPELUnit";
	public static final String MARKER_SERVICE_MARK_OPERATION = "mark";
	
}
