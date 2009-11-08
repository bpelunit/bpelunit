/*
 * XML Type:  TestCase
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTestCase
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite;


/**
 * An XML TestCase(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public interface XMLTestCase extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLTestCase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s658D211C851517200AFEC2C2421DA420").resolveHandle("testcasecf04type");
    
    /**
     * Gets array of all "property" elements
     */
    org.bpelunit.framework.xml.suite.XMLProperty[] getPropertyArray();
    
    /**
     * Gets ith "property" element
     */
    org.bpelunit.framework.xml.suite.XMLProperty getPropertyArray(int i);
    
    /**
     * Returns number of "property" element
     */
    int sizeOfPropertyArray();
    
    /**
     * Sets array of all "property" element
     */
    void setPropertyArray(org.bpelunit.framework.xml.suite.XMLProperty[] propertyArray);
    
    /**
     * Sets ith "property" element
     */
    void setPropertyArray(int i, org.bpelunit.framework.xml.suite.XMLProperty property);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "property" element
     */
    org.bpelunit.framework.xml.suite.XMLProperty insertNewProperty(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "property" element
     */
    org.bpelunit.framework.xml.suite.XMLProperty addNewProperty();
    
    /**
     * Removes the ith "property" element
     */
    void removeProperty(int i);
    
    /**
     * Gets the "clientTrack" element
     */
    org.bpelunit.framework.xml.suite.XMLTrack getClientTrack();
    
    /**
     * Sets the "clientTrack" element
     */
    void setClientTrack(org.bpelunit.framework.xml.suite.XMLTrack clientTrack);
    
    /**
     * Appends and returns a new empty "clientTrack" element
     */
    org.bpelunit.framework.xml.suite.XMLTrack addNewClientTrack();
    
    /**
     * Gets array of all "partnerTrack" elements
     */
    org.bpelunit.framework.xml.suite.XMLPartnerTrack[] getPartnerTrackArray();
    
    /**
     * Gets ith "partnerTrack" element
     */
    org.bpelunit.framework.xml.suite.XMLPartnerTrack getPartnerTrackArray(int i);
    
    /**
     * Returns number of "partnerTrack" element
     */
    int sizeOfPartnerTrackArray();
    
    /**
     * Sets array of all "partnerTrack" element
     */
    void setPartnerTrackArray(org.bpelunit.framework.xml.suite.XMLPartnerTrack[] partnerTrackArray);
    
    /**
     * Sets ith "partnerTrack" element
     */
    void setPartnerTrackArray(int i, org.bpelunit.framework.xml.suite.XMLPartnerTrack partnerTrack);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "partnerTrack" element
     */
    org.bpelunit.framework.xml.suite.XMLPartnerTrack insertNewPartnerTrack(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "partnerTrack" element
     */
    org.bpelunit.framework.xml.suite.XMLPartnerTrack addNewPartnerTrack();
    
    /**
     * Removes the ith "partnerTrack" element
     */
    void removePartnerTrack(int i);
    
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
     * Gets the "vary" attribute
     */
    boolean getVary();
    
    /**
     * Gets (as xml) the "vary" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetVary();
    
    /**
     * True if has "vary" attribute
     */
    boolean isSetVary();
    
    /**
     * Sets the "vary" attribute
     */
    void setVary(boolean vary);
    
    /**
     * Sets (as xml) the "vary" attribute
     */
    void xsetVary(org.apache.xmlbeans.XmlBoolean vary);
    
    /**
     * Unsets the "vary" attribute
     */
    void unsetVary();
    
    /**
     * Gets the "basedOn" attribute
     */
    java.lang.String getBasedOn();
    
    /**
     * Gets (as xml) the "basedOn" attribute
     */
    org.apache.xmlbeans.XmlString xgetBasedOn();
    
    /**
     * True if has "basedOn" attribute
     */
    boolean isSetBasedOn();
    
    /**
     * Sets the "basedOn" attribute
     */
    void setBasedOn(java.lang.String basedOn);
    
    /**
     * Sets (as xml) the "basedOn" attribute
     */
    void xsetBasedOn(org.apache.xmlbeans.XmlString basedOn);
    
    /**
     * Unsets the "basedOn" attribute
     */
    void unsetBasedOn();
    
    /**
     * Gets the "abstract" attribute
     */
    boolean getAbstract();
    
    /**
     * Gets (as xml) the "abstract" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAbstract();
    
    /**
     * True if has "abstract" attribute
     */
    boolean isSetAbstract();
    
    /**
     * Sets the "abstract" attribute
     */
    void setAbstract(boolean xabstract);
    
    /**
     * Sets (as xml) the "abstract" attribute
     */
    void xsetAbstract(org.apache.xmlbeans.XmlBoolean xabstract);
    
    /**
     * Unsets the "abstract" attribute
     */
    void unsetAbstract();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.suite.XMLTestCase newInstance() {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLTestCase parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLTestCase) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
