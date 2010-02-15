/*
 * XML Type:  SendActivity
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLSendActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite;


/**
 * An XML SendActivity(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public interface XMLSendActivity extends org.bpelunit.framework.xml.suite.XMLSoapActivity
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLSendActivity.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2DE349CDB64FC5F159C18554BFE3264F").resolveHandle("sendactivitybb4ftype");
    
    /**
     * Gets the "data" element
     */
    org.bpelunit.framework.xml.suite.XMLAnyElement getData();
    
    /**
     * Sets the "data" element
     */
    void setData(org.bpelunit.framework.xml.suite.XMLAnyElement data);
    
    /**
     * Appends and returns a new empty "data" element
     */
    org.bpelunit.framework.xml.suite.XMLAnyElement addNewData();
    
    /**
     * Gets the "fault" attribute
     */
    boolean getFault();
    
    /**
     * Gets (as xml) the "fault" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFault();
    
    /**
     * True if has "fault" attribute
     */
    boolean isSetFault();
    
    /**
     * Sets the "fault" attribute
     */
    void setFault(boolean fault);
    
    /**
     * Sets (as xml) the "fault" attribute
     */
    void xsetFault(org.apache.xmlbeans.XmlBoolean fault);
    
    /**
     * Unsets the "fault" attribute
     */
    void unsetFault();
    
    /**
     * Gets the "delaySequence" attribute
     */
    java.lang.String getDelaySequence();
    
    /**
     * Gets (as xml) the "delaySequence" attribute
     */
    org.apache.xmlbeans.XmlString xgetDelaySequence();
    
    /**
     * True if has "delaySequence" attribute
     */
    boolean isSetDelaySequence();
    
    /**
     * Sets the "delaySequence" attribute
     */
    void setDelaySequence(java.lang.String delaySequence);
    
    /**
     * Sets (as xml) the "delaySequence" attribute
     */
    void xsetDelaySequence(org.apache.xmlbeans.XmlString delaySequence);
    
    /**
     * Unsets the "delaySequence" attribute
     */
    void unsetDelaySequence();
    
    /**
     * Gets the "faultcode" attribute
     */
    javax.xml.namespace.QName getFaultcode();
    
    /**
     * Gets (as xml) the "faultcode" attribute
     */
    org.apache.xmlbeans.XmlQName xgetFaultcode();
    
    /**
     * True if has "faultcode" attribute
     */
    boolean isSetFaultcode();
    
    /**
     * Sets the "faultcode" attribute
     */
    void setFaultcode(javax.xml.namespace.QName faultcode);
    
    /**
     * Sets (as xml) the "faultcode" attribute
     */
    void xsetFaultcode(org.apache.xmlbeans.XmlQName faultcode);
    
    /**
     * Unsets the "faultcode" attribute
     */
    void unsetFaultcode();
    
    /**
     * Gets the "faultstring" attribute
     */
    java.lang.String getFaultstring();
    
    /**
     * Gets (as xml) the "faultstring" attribute
     */
    org.apache.xmlbeans.XmlString xgetFaultstring();
    
    /**
     * True if has "faultstring" attribute
     */
    boolean isSetFaultstring();
    
    /**
     * Sets the "faultstring" attribute
     */
    void setFaultstring(java.lang.String faultstring);
    
    /**
     * Sets (as xml) the "faultstring" attribute
     */
    void xsetFaultstring(org.apache.xmlbeans.XmlString faultstring);
    
    /**
     * Unsets the "faultstring" attribute
     */
    void unsetFaultstring();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.suite.XMLSendActivity newInstance() {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLSendActivity parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLSendActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
