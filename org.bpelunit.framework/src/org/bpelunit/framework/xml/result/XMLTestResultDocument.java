/*
 * An XML document type.
 * Localname: testResult
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLTestResultDocument
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result;


/**
 * A document containing one testResult(@http://www.bpelunit.org/schema/testResult) element.
 *
 * This is a complex type.
 */
public interface XMLTestResultDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLTestResultDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("testresult956edoctype");
    
    /**
     * Gets the "testResult" element
     */
    org.bpelunit.framework.xml.result.XMLTestResult getTestResult();
    
    /**
     * Sets the "testResult" element
     */
    void setTestResult(org.bpelunit.framework.xml.result.XMLTestResult testResult);
    
    /**
     * Appends and returns a new empty "testResult" element
     */
    org.bpelunit.framework.xml.result.XMLTestResult addNewTestResult();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument newInstance() {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLTestResultDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLTestResultDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
