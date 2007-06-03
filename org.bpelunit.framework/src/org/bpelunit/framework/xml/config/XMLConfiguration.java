/*
 * XML Type:  Configuration
 * Namespace: http://www.bpelunit.org/schema/testConfiguration
 * Java type: org.bpelunit.framework.xml.config.XMLConfiguration
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.config;


/**
 * An XML Configuration(@http://www.bpelunit.org/schema/testConfiguration).
 *
 * This is a complex type.
 */
public interface XMLConfiguration extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLConfiguration.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("configurationc4a0type");
    
    /**
     * Gets a List of "property" elements
     */
    java.util.List<org.bpelunit.framework.xml.config.XMLProperty> getPropertyList();
    
    /**
     * Gets array of all "property" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.config.XMLProperty[] getPropertyArray();
    
    /**
     * Gets ith "property" element
     */
    org.bpelunit.framework.xml.config.XMLProperty getPropertyArray(int i);
    
    /**
     * Returns number of "property" element
     */
    int sizeOfPropertyArray();
    
    /**
     * Sets array of all "property" element
     */
    void setPropertyArray(org.bpelunit.framework.xml.config.XMLProperty[] propertyArray);
    
    /**
     * Sets ith "property" element
     */
    void setPropertyArray(int i, org.bpelunit.framework.xml.config.XMLProperty property);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "property" element
     */
    org.bpelunit.framework.xml.config.XMLProperty insertNewProperty(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "property" element
     */
    org.bpelunit.framework.xml.config.XMLProperty addNewProperty();
    
    /**
     * Removes the ith "property" element
     */
    void removeProperty(int i);
    
    /**
     * Gets the "deployer" attribute
     */
    java.lang.String getDeployer();
    
    /**
     * Gets (as xml) the "deployer" attribute
     */
    org.apache.xmlbeans.XmlString xgetDeployer();
    
    /**
     * Sets the "deployer" attribute
     */
    void setDeployer(java.lang.String deployer);
    
    /**
     * Sets (as xml) the "deployer" attribute
     */
    void xsetDeployer(org.apache.xmlbeans.XmlString deployer);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.config.XMLConfiguration newInstance() {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.config.XMLConfiguration parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.config.XMLConfiguration) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
