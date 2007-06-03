/*
 * XML Type:  ReceiveCondition
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLReceiveCondition
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result;


/**
 * An XML ReceiveCondition(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public interface XMLReceiveCondition extends org.bpelunit.framework.xml.result.XMLArtefact
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLReceiveCondition.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("receivecondition6ba7type");
    
    /**
     * Gets the "condition" element
     */
    org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition getCondition();
    
    /**
     * Sets the "condition" element
     */
    void setCondition(org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition condition);
    
    /**
     * Appends and returns a new empty "condition" element
     */
    org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition addNewCondition();
    
    /**
     * An XML condition(@http://www.bpelunit.org/schema/testResult).
     *
     * This is a complex type.
     */
    public interface Condition extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Condition.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("condition1980elemtype");
        
        /**
         * Gets the "expression" element
         */
        java.lang.String getExpression();
        
        /**
         * Gets (as xml) the "expression" element
         */
        org.apache.xmlbeans.XmlString xgetExpression();
        
        /**
         * Sets the "expression" element
         */
        void setExpression(java.lang.String expression);
        
        /**
         * Sets (as xml) the "expression" element
         */
        void xsetExpression(org.apache.xmlbeans.XmlString expression);
        
        /**
         * Gets the "expectedValue" element
         */
        java.lang.String getExpectedValue();
        
        /**
         * Gets (as xml) the "expectedValue" element
         */
        org.apache.xmlbeans.XmlString xgetExpectedValue();
        
        /**
         * Sets the "expectedValue" element
         */
        void setExpectedValue(java.lang.String expectedValue);
        
        /**
         * Sets (as xml) the "expectedValue" element
         */
        void xsetExpectedValue(org.apache.xmlbeans.XmlString expectedValue);
        
        /**
         * Gets the "actualValue" element
         */
        java.lang.String getActualValue();
        
        /**
         * Gets (as xml) the "actualValue" element
         */
        org.apache.xmlbeans.XmlString xgetActualValue();
        
        /**
         * Sets the "actualValue" element
         */
        void setActualValue(java.lang.String actualValue);
        
        /**
         * Sets (as xml) the "actualValue" element
         */
        void xsetActualValue(org.apache.xmlbeans.XmlString actualValue);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition newInstance() {
              return (org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition newInstance() {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.bpelunit.framework.xml.result.XMLReceiveCondition parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.result.XMLReceiveCondition) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
