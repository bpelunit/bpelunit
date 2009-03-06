/*
 * XML Type:  Track
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTrack
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite;


/**
 * An XML Track(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public interface XMLTrack extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMLTrack.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s21B6514B5535163199D3BCDDAA42EFA0").resolveHandle("track70edtype");
    
    /**
     * Gets a List of "sendOnly" elements
     */
    java.util.List<org.bpelunit.framework.xml.suite.XMLSendActivity> getSendOnlyList();
    
    /**
     * Gets array of all "sendOnly" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.suite.XMLSendActivity[] getSendOnlyArray();
    
    /**
     * Gets ith "sendOnly" element
     */
    org.bpelunit.framework.xml.suite.XMLSendActivity getSendOnlyArray(int i);
    
    /**
     * Returns number of "sendOnly" element
     */
    int sizeOfSendOnlyArray();
    
    /**
     * Sets array of all "sendOnly" element
     */
    void setSendOnlyArray(org.bpelunit.framework.xml.suite.XMLSendActivity[] sendOnlyArray);
    
    /**
     * Sets ith "sendOnly" element
     */
    void setSendOnlyArray(int i, org.bpelunit.framework.xml.suite.XMLSendActivity sendOnly);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "sendOnly" element
     */
    org.bpelunit.framework.xml.suite.XMLSendActivity insertNewSendOnly(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "sendOnly" element
     */
    org.bpelunit.framework.xml.suite.XMLSendActivity addNewSendOnly();
    
    /**
     * Removes the ith "sendOnly" element
     */
    void removeSendOnly(int i);
    
    /**
     * Gets a List of "receiveOnly" elements
     */
    java.util.List<org.bpelunit.framework.xml.suite.XMLReceiveActivity> getReceiveOnlyList();
    
    /**
     * Gets array of all "receiveOnly" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.suite.XMLReceiveActivity[] getReceiveOnlyArray();
    
    /**
     * Gets ith "receiveOnly" element
     */
    org.bpelunit.framework.xml.suite.XMLReceiveActivity getReceiveOnlyArray(int i);
    
    /**
     * Returns number of "receiveOnly" element
     */
    int sizeOfReceiveOnlyArray();
    
    /**
     * Sets array of all "receiveOnly" element
     */
    void setReceiveOnlyArray(org.bpelunit.framework.xml.suite.XMLReceiveActivity[] receiveOnlyArray);
    
    /**
     * Sets ith "receiveOnly" element
     */
    void setReceiveOnlyArray(int i, org.bpelunit.framework.xml.suite.XMLReceiveActivity receiveOnly);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveOnly" element
     */
    org.bpelunit.framework.xml.suite.XMLReceiveActivity insertNewReceiveOnly(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveOnly" element
     */
    org.bpelunit.framework.xml.suite.XMLReceiveActivity addNewReceiveOnly();
    
    /**
     * Removes the ith "receiveOnly" element
     */
    void removeReceiveOnly(int i);
    
    /**
     * Gets a List of "sendReceive" elements
     */
    java.util.List<org.bpelunit.framework.xml.suite.XMLTwoWayActivity> getSendReceiveList();
    
    /**
     * Gets array of all "sendReceive" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getSendReceiveArray();
    
    /**
     * Gets ith "sendReceive" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity getSendReceiveArray(int i);
    
    /**
     * Returns number of "sendReceive" element
     */
    int sizeOfSendReceiveArray();
    
    /**
     * Sets array of all "sendReceive" element
     */
    void setSendReceiveArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] sendReceiveArray);
    
    /**
     * Sets ith "sendReceive" element
     */
    void setSendReceiveArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity sendReceive);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "sendReceive" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewSendReceive(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "sendReceive" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewSendReceive();
    
    /**
     * Removes the ith "sendReceive" element
     */
    void removeSendReceive(int i);
    
    /**
     * Gets a List of "receiveSend" elements
     */
    java.util.List<org.bpelunit.framework.xml.suite.XMLTwoWayActivity> getReceiveSendList();
    
    /**
     * Gets array of all "receiveSend" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getReceiveSendArray();
    
    /**
     * Gets ith "receiveSend" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity getReceiveSendArray(int i);
    
    /**
     * Returns number of "receiveSend" element
     */
    int sizeOfReceiveSendArray();
    
    /**
     * Sets array of all "receiveSend" element
     */
    void setReceiveSendArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] receiveSendArray);
    
    /**
     * Sets ith "receiveSend" element
     */
    void setReceiveSendArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity receiveSend);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveSend" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewReceiveSend(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveSend" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewReceiveSend();
    
    /**
     * Removes the ith "receiveSend" element
     */
    void removeReceiveSend(int i);
    
    /**
     * Gets a List of "receiveSendAsynchronous" elements
     */
    java.util.List<org.bpelunit.framework.xml.suite.XMLTwoWayActivity> getReceiveSendAsynchronousList();
    
    /**
     * Gets array of all "receiveSendAsynchronous" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getReceiveSendAsynchronousArray();
    
    /**
     * Gets ith "receiveSendAsynchronous" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity getReceiveSendAsynchronousArray(int i);
    
    /**
     * Returns number of "receiveSendAsynchronous" element
     */
    int sizeOfReceiveSendAsynchronousArray();
    
    /**
     * Sets array of all "receiveSendAsynchronous" element
     */
    void setReceiveSendAsynchronousArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] receiveSendAsynchronousArray);
    
    /**
     * Sets ith "receiveSendAsynchronous" element
     */
    void setReceiveSendAsynchronousArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity receiveSendAsynchronous);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveSendAsynchronous" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewReceiveSendAsynchronous(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveSendAsynchronous" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewReceiveSendAsynchronous();
    
    /**
     * Removes the ith "receiveSendAsynchronous" element
     */
    void removeReceiveSendAsynchronous(int i);
    
    /**
     * Gets a List of "sendReceiveAsynchronous" elements
     */
    java.util.List<org.bpelunit.framework.xml.suite.XMLTwoWayActivity> getSendReceiveAsynchronousList();
    
    /**
     * Gets array of all "sendReceiveAsynchronous" elements
     * @deprecated
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getSendReceiveAsynchronousArray();
    
    /**
     * Gets ith "sendReceiveAsynchronous" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity getSendReceiveAsynchronousArray(int i);
    
    /**
     * Returns number of "sendReceiveAsynchronous" element
     */
    int sizeOfSendReceiveAsynchronousArray();
    
    /**
     * Sets array of all "sendReceiveAsynchronous" element
     */
    void setSendReceiveAsynchronousArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] sendReceiveAsynchronousArray);
    
    /**
     * Sets ith "sendReceiveAsynchronous" element
     */
    void setSendReceiveAsynchronousArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity sendReceiveAsynchronous);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "sendReceiveAsynchronous" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewSendReceiveAsynchronous(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "sendReceiveAsynchronous" element
     */
    org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewSendReceiveAsynchronous();
    
    /**
     * Removes the ith "sendReceiveAsynchronous" element
     */
    void removeSendReceiveAsynchronous(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.bpelunit.framework.xml.suite.XMLTrack newInstance() {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.bpelunit.framework.xml.suite.XMLTrack parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.bpelunit.framework.xml.suite.XMLTrack) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
