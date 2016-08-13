package net.bpelunit.model.bpel;

/**
 * Class with constants used throughout the BPEL process instrumenter.
 *
 * @author Antonio Garcia-Dominguez
 */
public final class BPELConstants {

	private BPELConstants() {}

	public static final String BPEL2_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	public static final String BPEL2_NS_PREFIX = "bpel";

	/* WSDL namespace  */
	public static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
	public static final String WSDL_NS_PREFIX = "wsdl";

	/* SOAP namespace */
	public static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap/";
	public static final String SOAP_NS_PREFIX = "soap";
	public static final String SOAPENV_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String SOAPENV_NS_PREFIX = "soapenv";

	/* Partner links namespaces */
	public static final String PLINK_NAMESPACE_WSBPEL2 = "http://docs.oasis-open.org/wsbpel/2.0/plnktype";
	public static final String PLINK_NAMESPACE_BPEL4WS = "http://schemas.xmlsoap.org/ws/2003/05/partner-link/";
	public static final String PLINK_NS_PREFIX_WSBPEL2 = "plnkWSBPEL2";
	public static final String PLINK_NS_PREFIX_BPEL4WS = "plnkBPEL4WS";

	/* UCA-specific extensions */
	public static final String UCA_NAMESPACE = "http://www.uca.es/xpath/2007/11";
	public static final String UCA_NS_PREFIX = "uca";

	/* WS-Addressing 200303 */
	public static final String WSA200303_NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/addressing";
	public static final String WSA200303_PREFIX = "wsa";

	/* WS-Addressing 200403 */
	public static final String WSA200403_NAMESPACE = "http://schemas.xmlsoap.org/ws/2004/03/addressing";
	public static final String WSA200403_PREFIX = "wsa2";

	/* WS-Addressing 200408 */
	public static final String WSA200408_NAMESPACE = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
	public static final String WSA200408_PREFIX = "wsa3";

	/* WS-Addressing 200508 */
	public static final String WSA200508_NAMESPACE = "http://www.w3.org/2005/08/addressing";
	public static final String WSA200508_PREFIX = "wsa4";

	/* Variable properties */
	public static final String VARIABLE_PROPERTIES_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/varprop";
	public static final String VARIABLE_PROPERTIES_PREFIX = "vprop";

	/* XML Schema */
	public static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	public static final String XSD_PREFIX = "xsd";

	/* Valid values for soap:binding/@style */
	public static final String SOAP_DOCUMENT_STYLE = "document";
	public static final String SOAP_RPC_STYLE = "rpc";

	/* XSLT */
	public static final String XSLT_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";
	public static final String XSLT_NS_PREFIX = "xsl";
}
