/*
 * XML Type:  Artefact
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLArtefact
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result;


/**
 * An XML Artefact(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public interface XMLArtefact extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLArtefact.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s40325DB26B1B206C7D793E5B0986465B").resolveHandle("artefact777dtype");
    
    /**
     * Gets array of all "state" elements
     */
    org.bpelunit.framework.xml.result.XMLInfo[] getStateArray();
    
    /**
     * Gets ith "state" element
     */
    org.bpelunit.framework.xml.result.XMLInfo getStateArray(int i);
    
    /**
     * Returns number of "state" element
     */
    int sizeOfStateArray();
    
    /**
     * Sets array of all "state" element
     */
    void setStateArray(org.bpelunit.framework.xml.result.XMLInfo[] stateArray);
    
    /**
     * Sets ith "state" element
     */
    void setStateArray(int i, org.bpelunit.framework.xml.result.XMLInfo state);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "state" element
     */
    org.bpelunit.framework.xml.result.XMLInfo insertNewState(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "state" element
     */
    org.bpelunit.framework.xml.result.XMLInfo addNewState();
    
    /**
     * Removes the ith "state" element
     */
    void removeState(int i);
    
    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();
    
    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();
    
    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);
    
    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
    
    /**
     * Gets the "result" attribute
     */
    java.lang.String getResult();
    
    /**
     * Gets (as xml) the "result" attribute
     */
    org.apache.xmlbeans.XmlString xgetResult();
    
    /**
     * Sets the "result" attribute
     */
    void setResult(java.lang.String result);
    
    /**
     * Sets (as xml) the "result" attribute
     */
    void xsetResult(org.apache.xmlbeans.XmlString result);
    
    /**
     * Gets the "message" attribute
     */
    java.lang.String getMessage();
    
    /**
     * Gets (as xml) the "message" attribute
     */
    org.apache.xmlbeans.XmlString xgetMessage();
    
    /**
     * Sets the "message" attribute
     */
    void setMessage(java.lang.String message);
    
    /**
     * Sets (as xml) the "message" attribute
     */
    void xsetMessage(org.apache.xmlbeans.XmlString message);
    
    /**
     * Gets the "exception" attribute
     */
    java.lang.String getException();
    
    /**
     * Gets (as xml) the "exception" attribute
     */
    org.apache.xmlbeans.XmlString xgetException();
    
    /**
     * True if has "exception" attribute
     */
    boolean isSetException();
    
    /**
     * Sets the "exception" attribute
     */
    void setException(java.lang.String exception);
    
    /**
     * Sets (as xml) the "exception" attribute
     */
    void xsetException(org.apache.xmlbeans.XmlString exception);
    
    /**
     * Unsets the "exception" attribute
     */
    void unsetException();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.result.XMLArtefact newInstance() {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLArtefact parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLArtefact) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
