/*
 * XML Type:  SoapActivity
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLSoapActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite;


/**
 * An XML SoapActivity(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public interface XMLSoapActivity extends org.bpelunit.framework.xml.suite.XMLActivity
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLSoapActivity.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2DE349CDB64FC5F159C18554BFE3264F").resolveHandle("soapactivity09actype");
    
    /**
     * Gets the "service" attribute
     */
    javax.xml.namespace.QName getService();
    
    /**
     * Gets (as xml) the "service" attribute
     */
    org.apache.xmlbeans.XmlQName xgetService();
    
    /**
     * True if has "service" attribute
     */
    boolean isSetService();
    
    /**
     * Sets the "service" attribute
     */
    void setService(javax.xml.namespace.QName service);
    
    /**
     * Sets (as xml) the "service" attribute
     */
    void xsetService(org.apache.xmlbeans.XmlQName service);
    
    /**
     * Unsets the "service" attribute
     */
    void unsetService();
    
    /**
     * Gets the "port" attribute
     */
    java.lang.String getPort();
    
    /**
     * Gets (as xml) the "port" attribute
     */
    org.apache.xmlbeans.XmlString xgetPort();
    
    /**
     * True if has "port" attribute
     */
    boolean isSetPort();
    
    /**
     * Sets the "port" attribute
     */
    void setPort(java.lang.String port);
    
    /**
     * Sets (as xml) the "port" attribute
     */
    void xsetPort(org.apache.xmlbeans.XmlString port);
    
    /**
     * Unsets the "port" attribute
     */
    void unsetPort();
    
    /**
     * Gets the "operation" attribute
     */
    java.lang.String getOperation();
    
    /**
     * Gets (as xml) the "operation" attribute
     */
    org.apache.xmlbeans.XmlString xgetOperation();
    
    /**
     * True if has "operation" attribute
     */
    boolean isSetOperation();
    
    /**
     * Sets the "operation" attribute
     */
    void setOperation(java.lang.String operation);
    
    /**
     * Sets (as xml) the "operation" attribute
     */
    void xsetOperation(org.apache.xmlbeans.XmlString operation);
    
    /**
     * Unsets the "operation" attribute
     */
    void unsetOperation();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity newInstance() {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLSoapActivity parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLSoapActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
