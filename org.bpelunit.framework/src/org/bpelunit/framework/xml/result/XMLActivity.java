/*
 * XML Type:  Activity
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result;


/**
 * An XML Activity(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public interface XMLActivity extends org.bpelunit.framework.xml.result.XMLArtefact
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLActivity.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s658D211C851517200AFEC2C2421DA420").resolveHandle("activity217etype");
    
    /**
     * Gets array of all "activity" elements
     */
    org.bpelunit.framework.xml.result.XMLActivity[] getActivityArray();
    
    /**
     * Gets ith "activity" element
     */
    org.bpelunit.framework.xml.result.XMLActivity getActivityArray(int i);
    
    /**
     * Returns number of "activity" element
     */
    int sizeOfActivityArray();
    
    /**
     * Sets array of all "activity" element
     */
    void setActivityArray(org.bpelunit.framework.xml.result.XMLActivity[] activityArray);
    
    /**
     * Sets ith "activity" element
     */
    void setActivityArray(int i, org.bpelunit.framework.xml.result.XMLActivity activity);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "activity" element
     */
    org.bpelunit.framework.xml.result.XMLActivity insertNewActivity(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "activity" element
     */
    org.bpelunit.framework.xml.result.XMLActivity addNewActivity();
    
    /**
     * Removes the ith "activity" element
     */
    void removeActivity(int i);
    
    /**
     * Gets array of all "dataPackage" elements
     */
    org.bpelunit.framework.xml.result.XMLData[] getDataPackageArray();
    
    /**
     * Gets ith "dataPackage" element
     */
    org.bpelunit.framework.xml.result.XMLData getDataPackageArray(int i);
    
    /**
     * Returns number of "dataPackage" element
     */
    int sizeOfDataPackageArray();
    
    /**
     * Sets array of all "dataPackage" element
     */
    void setDataPackageArray(org.bpelunit.framework.xml.result.XMLData[] dataPackageArray);
    
    /**
     * Sets ith "dataPackage" element
     */
    void setDataPackageArray(int i, org.bpelunit.framework.xml.result.XMLData dataPackage);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "dataPackage" element
     */
    org.bpelunit.framework.xml.result.XMLData insertNewDataPackage(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "dataPackage" element
     */
    org.bpelunit.framework.xml.result.XMLData addNewDataPackage();
    
    /**
     * Removes the ith "dataPackage" element
     */
    void removeDataPackage(int i);
    
    /**
     * Gets array of all "copyOperation" elements
     */
    org.bpelunit.framework.xml.result.XMLCopyOperation[] getCopyOperationArray();
    
    /**
     * Gets ith "copyOperation" element
     */
    org.bpelunit.framework.xml.result.XMLCopyOperation getCopyOperationArray(int i);
    
    /**
     * Returns number of "copyOperation" element
     */
    int sizeOfCopyOperationArray();
    
    /**
     * Sets array of all "copyOperation" element
     */
    void setCopyOperationArray(org.bpelunit.framework.xml.result.XMLCopyOperation[] copyOperationArray);
    
    /**
     * Sets ith "copyOperation" element
     */
    void setCopyOperationArray(int i, org.bpelunit.framework.xml.result.XMLCopyOperation copyOperation);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "copyOperation" element
     */
    org.bpelunit.framework.xml.result.XMLCopyOperation insertNewCopyOperation(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "copyOperation" element
     */
    org.bpelunit.framework.xml.result.XMLCopyOperation addNewCopyOperation();
    
    /**
     * Removes the ith "copyOperation" element
     */
    void removeCopyOperation(int i);
    
    /**
     * Gets the "type" attribute
     */
    java.lang.String getType();
    
    /**
     * Gets (as xml) the "type" attribute
     */
    org.apache.xmlbeans.XmlString xgetType();
    
    /**
     * Sets the "type" attribute
     */
    void setType(java.lang.String type);
    
    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.apache.xmlbeans.XmlString type);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.result.XMLActivity newInstance() {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLActivity parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLActivity parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLActivity parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLActivity) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
