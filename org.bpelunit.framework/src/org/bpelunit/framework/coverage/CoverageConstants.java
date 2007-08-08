package org.bpelunit.framework.coverage;

import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.exprlang.ExpressionLanguage;
import org.jdom.Namespace;

public class CoverageConstants {

	/**
	 * Wartezeit nach jedem Testfall um alle Coverage-Nachrichten zu empfangen.
	 */
	public static final int DEFAULT_WAITTIME = 500;

	//***********************WSDL für Coverage Logging Service **************************************
	
	public static final String PARTNERLINK_NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/addressing";

	public static final Namespace COVERAGETOOL_NAMESPACE = Namespace
			.getNamespace("cov",
					"http://www.bpelunit.org/coverage/CoverageReportingService");

	public static final char PREFIX_COPY_OF_ARCHIVEFILE = '_';

	public static final int EXPRESSION_LANGUAGE = ExpressionLanguage.XPATH1_0;

	public static final String REPORT_OPERATION = "reportExecution";

	public static final String PORT_OF_SERVICE = "Soap_service_port";

	public static final String SERVICE_NAME = "_CoverageReportingService_";

	public static final int SERVICE_PORT = 7778;

	public static final String SERVICE_CONTEXT = "/coverage/";

	public static final String ADDRESS_OF_SERVICE = "http://localhost:"
			+ Integer.valueOf(SERVICE_PORT) + "/coverage/" + SERVICE_NAME;

	public static final String COVERAGE_SERVICE_WSDL = "CoverageReportingService.wsdl";

	public static final String PARTNERLINK_NAME = "PL_CoverageReportingService_";

	public static final String REPORTING_SERVICE_PORT = COVERAGETOOL_NAMESPACE
			.getPrefix()
			+ ":" + "_CoverageLabelHandler_";

	public static final String REPORTING_SERVICE_PORTTYPE = COVERAGETOOL_NAMESPACE
			.getPrefix()
			+ ":" + "PLT_CoverageReportingService_";

	public static final String REGISTER_COVERAGE_LABELS_OPERATION = "registerCoverageLabels";

	public static final String PARTNER_ROLE = "ReportListener";

	public static final String PART_OF_REPORTING_MESSAGE = "entries";

	public static final String MESSAGETYPE_OF_REPORTING_MESSAGE = COVERAGETOOL_NAMESPACE
			.getPrefix()
			+ ":" + "CoverageLabels";

	public static final String MESSAGETYPE_OF_REGISTER_MESSAGE = MESSAGETYPE_OF_REPORTING_MESSAGE;

	
	//***********************Konfiguration des Befehlszeilen -Clients
	public static final Namespace NAMESPACE_CONFIGURATION = Namespace
			.getNamespace("http://www.bpelunit.org/schema/coverageMetricsConfiguration");

	public static final String CONFIG_ATTRIBUTE_WAIT_TIME = "waitingTime";

	public static final String CONFIG_METRIC_ELEMENT = "metric";

	public static final String CONFIG_PROPERTY_ELEMENT = "property";

	public static final String CONFIG_ATTRIBUT_NAME = "name";

	public static final String CONFIG_ELEMENT_BASIC_ACTIVITIES = "IncludeBasicActivities";

}
