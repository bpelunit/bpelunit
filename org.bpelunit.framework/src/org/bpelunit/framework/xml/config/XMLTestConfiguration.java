/*
 * XML Type:  TestConfiguration
 * Namespace: http://www.bpelunit.org/schema/testConfiguration
 * Java type: org.bpelunit.framework.xml.config.XMLTestConfiguration
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.config;


/**
 * An XML TestConfiguration(@http://www.bpelunit.org/schema/testConfiguration).
 *
 * This is a complex type.
 */
public interface XMLTestConfiguration extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLTestConfiguration.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("testconfigurationf552type");
    
    /**
     * Gets a List of "configuration" elements
     */
    java.util.List<org.bpelunit.framework.xml.config.XMLConfiguration> getConfigurationList();
    
    /**
     * Gets array of all "configuration" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.config.XMLConfiguration[] getConfigurationArray();
    
    /**
     * Gets ith "configuration" element
     */
    org.bpelunit.framework.xml.config.XMLConfiguration getConfigurationArray(int i);
    
    /**
     * Returns number of "configuration" element
     */
    int sizeOfConfigurationArray();
    
    /**
     * Sets array of all "configuration" element
     */
    void setConfigurationArray(org.bpelunit.framework.xml.config.XMLConfiguration[] configurationArray);
    
    /**
     * Sets ith "configuration" element
     */
    void setConfigurationArray(int i, org.bpelunit.framework.xml.config.XMLConfiguration configuration);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "configuration" element
     */
    org.bpelunit.framework.xml.config.XMLConfiguration insertNewConfiguration(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "configuration" element
     */
    org.bpelunit.framework.xml.config.XMLConfiguration addNewConfiguration();
    
    /**
     * Removes the ith "configuration" element
     */
    void removeConfiguration(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration newInstance() {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.config.XMLTestConfiguration parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.config.XMLTestConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
