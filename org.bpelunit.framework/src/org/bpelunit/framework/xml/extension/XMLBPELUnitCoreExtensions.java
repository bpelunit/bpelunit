/*
 * XML Type:  BPELUnitCoreExtensions
 * Namespace: http://www.bpelunit.org/schema/testExtension
 * Java type: org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.extension;


/**
 * An XML BPELUnitCoreExtensions(@http://www.bpelunit.org/schema/testExtension).
 *
 * This is a complex type.
 */
public interface XMLBPELUnitCoreExtensions extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLBPELUnitCoreExtensions.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2DE349CDB64FC5F159C18554BFE3264F").resolveHandle("bpelunitcoreextensionsdf41type");
    
    /**
     * Gets a List of "deployer" elements
     */
    java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> getDeployerList();
    
    /**
     * Gets array of all "deployer" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.extension.XMLExtension[] getDeployerArray();
    
    /**
     * Gets ith "deployer" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension getDeployerArray(int i);
    
    /**
     * Returns number of "deployer" element
     */
    int sizeOfDeployerArray();
    
    /**
     * Sets array of all "deployer" element
     */
    void setDeployerArray(org.bpelunit.framework.xml.extension.XMLExtension[] deployerArray);
    
    /**
     * Sets ith "deployer" element
     */
    void setDeployerArray(int i, org.bpelunit.framework.xml.extension.XMLExtension deployer);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "deployer" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension insertNewDeployer(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "deployer" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension addNewDeployer();
    
    /**
     * Removes the ith "deployer" element
     */
    void removeDeployer(int i);
    
    /**
     * Gets a List of "encoder" elements
     */
    java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> getEncoderList();
    
    /**
     * Gets array of all "encoder" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.extension.XMLExtension[] getEncoderArray();
    
    /**
     * Gets ith "encoder" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension getEncoderArray(int i);
    
    /**
     * Returns number of "encoder" element
     */
    int sizeOfEncoderArray();
    
    /**
     * Sets array of all "encoder" element
     */
    void setEncoderArray(org.bpelunit.framework.xml.extension.XMLExtension[] encoderArray);
    
    /**
     * Sets ith "encoder" element
     */
    void setEncoderArray(int i, org.bpelunit.framework.xml.extension.XMLExtension encoder);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "encoder" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension insertNewEncoder(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "encoder" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension addNewEncoder();
    
    /**
     * Removes the ith "encoder" element
     */
    void removeEncoder(int i);
    
    /**
     * Gets a List of "headerProcessor" elements
     */
    java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> getHeaderProcessorList();
    
    /**
     * Gets array of all "headerProcessor" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.extension.XMLExtension[] getHeaderProcessorArray();
    
    /**
     * Gets ith "headerProcessor" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension getHeaderProcessorArray(int i);
    
    /**
     * Returns number of "headerProcessor" element
     */
    int sizeOfHeaderProcessorArray();
    
    /**
     * Sets array of all "headerProcessor" element
     */
    void setHeaderProcessorArray(org.bpelunit.framework.xml.extension.XMLExtension[] headerProcessorArray);
    
    /**
     * Sets ith "headerProcessor" element
     */
    void setHeaderProcessorArray(int i, org.bpelunit.framework.xml.extension.XMLExtension headerProcessor);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "headerProcessor" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension insertNewHeaderProcessor(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "headerProcessor" element
     */
    org.bpelunit.framework.xml.extension.XMLExtension addNewHeaderProcessor();
    
    /**
     * Removes the ith "headerProcessor" element
     */
    void removeHeaderProcessor(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions newInstance() {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
