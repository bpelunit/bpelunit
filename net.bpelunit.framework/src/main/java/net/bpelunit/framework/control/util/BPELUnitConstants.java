/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Some Constants.
 * 
 * @author Philip Mayer
 * 
 */
public final class BPELUnitConstants {

	private BPELUnitConstants() {
	}
	
	/**
	 * The default base URL of the HTTP Server included in BPELUnit. BPELUnit uses this server to
	 * receive calls for partners and/or callbacks to the client. The name of each partner must be
	 * appended to the base URL in the definition of partners; the framework will then re-extract
	 * the name from the target URL.
	 */
	public static final String DEFAULT_BASE_URL= "http://localhost:7777/ws/";

	/**
	 * The default base port for BPELUnit. This port is only used if the user provided an URL in his
	 * test suite, but did not specify a port - this usually indicates port 80.
	 */
	public static final int DEFAULT_BASE_PORT= 80;

	/**
	 * The namespace of the BPELUnit test suite XML document
	 */
	public static final String BPELUNIT_TESTSUITE_NAMESPACE= "http://www.bpelunit.org/schema/testSuite";

	/**
	 * The SOAP 1.1 namespace
	 */
	public static final String SOAP_1_1_NAMESPACE= "http://schemas.xmlsoap.org/soap/envelope/";

	/**
	 * The name for the "client" partner track
	 */
	public static final String CLIENT_NAME= "client";

	/**
	 * Default timeout value for all send and receive activities (50 seconds)
	 */
	public static final int TIMEOUT= 50000;

	/**
	 * Default sleep time for threads waiting for something (200 ms).
	 */
	public static final int TIMEOUT_SLEEP_TIME= 150;

	/**
	 * Default charset for HTTP requests
	 */
	public static final String DEFAULT_HTTP_CHARSET= "ISO-8859-1";

	/**
	 * Content type for sending XML over HTTP.
	 */
	public static final String TEXT_XML_CONTENT_TYPE= "text/xml";

	/**
	 * Null (no) options
	 */
	public static final Map<String, String> NULL_OPTIONS= new HashMap<String, String>();

	/**
	 * SOAP Fault Code for BPELUnit framework generated faults
	 */
	public static final QName SOAP_FAULT_CODE_CLIENT= new QName(SOAP_1_1_NAMESPACE, "Client");

	/**
	 * SOAP Fault Description for BPELUnit framework generazted faults
	 */
	public static final String SOAP_FAULT_DESCRIPTION= "BPELUnit Framework Generated Fault";
}
