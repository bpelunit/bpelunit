/*
 * XML Type:  Data
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLData
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result;


/**
 * An XML Data(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public interface XMLData extends org.bpelunit.framework.xml.result.XMLArtefact
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLData.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("datab8d9type");
    
    /**
     * Gets a List of "xmlData" elements
     */
    java.util.List<org.bpelunit.framework.xml.result.XMLData.XmlData> getXmlDataList();
    
    /**
     * Gets array of all "xmlData" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.result.XMLData.XmlData[] getXmlDataArray();
    
    /**
     * Gets ith "xmlData" element
     */
    org.bpelunit.framework.xml.result.XMLData.XmlData getXmlDataArray(int i);
    
    /**
     * Returns number of "xmlData" element
     */
    int sizeOfXmlDataArray();
    
    /**
     * Sets array of all "xmlData" element
     */
    void setXmlDataArray(org.bpelunit.framework.xml.result.XMLData.XmlData[] xmlDataArray);
    
    /**
     * Sets ith "xmlData" element
     */
    void setXmlDataArray(int i, org.bpelunit.framework.xml.result.XMLData.XmlData xmlData);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "xmlData" element
     */
    org.bpelunit.framework.xml.result.XMLData.XmlData insertNewXmlData(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "xmlData" element
     */
    org.bpelunit.framework.xml.result.XMLData.XmlData addNewXmlData();
    
    /**
     * Removes the ith "xmlData" element
     */
    void removeXmlData(int i);
    
    /**
     * Gets a List of "receiveCondition" elements
     */
    java.util.List<org.bpelunit.framework.xml.result.XMLReceiveCondition> getReceiveConditionList();
    
    /**
     * Gets array of all "receiveCondition" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.result.XMLReceiveCondition[] getReceiveConditionArray();
    
    /**
     * Gets ith "receiveCondition" element
     */
    org.bpelunit.framework.xml.result.XMLReceiveCondition getReceiveConditionArray(int i);
    
    /**
     * Returns number of "receiveCondition" element
     */
    int sizeOfReceiveConditionArray();
    
    /**
     * Sets array of all "receiveCondition" element
     */
    void setReceiveConditionArray(org.bpelunit.framework.xml.result.XMLReceiveCondition[] receiveConditionArray);
    
    /**
     * Sets ith "receiveCondition" element
     */
    void setReceiveConditionArray(int i, org.bpelunit.framework.xml.result.XMLReceiveCondition receiveCondition);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveCondition" element
     */
    org.bpelunit.framework.xml.result.XMLReceiveCondition insertNewReceiveCondition(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveCondition" element
     */
    org.bpelunit.framework.xml.result.XMLReceiveCondition addNewReceiveCondition();
    
    /**
     * Removes the ith "receiveCondition" element
     */
    void removeReceiveCondition(int i);
    
    /**
     * An XML xmlData(@http://www.bpelunit.org/schema/testResult).
     *
     * This is a complex type.
     */
    public interface XmlData extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XmlData.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("xmldata53acelemtype");
        
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
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.bpelunit.framework.xml.result.XMLData.XmlData newInstance() {
              return (org.bpelunit.framework.xml.result.XMLData.XmlData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.bpelunit.framework.xml.result.XMLData.XmlData newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.bpelunit.framework.xml.result.XMLData.XmlData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.result.XMLData newInstance() {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.result.XMLData parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.result.XMLData parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLData parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLData parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLData parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLData) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
