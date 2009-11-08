/*
 * An XML document type.
 * Localname: testResult
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLTestResultDocument
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * A document containing one testResult(@http://www.bpelunit.org/schema/testResult) element.
 *
 * This is a complex type.
 */
public class XMLTestResultDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.result.XMLTestResultDocument
{
    
    public XMLTestResultDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TESTRESULT$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "testResult");
    
    
    /**
     * Gets the "testResult" element
     */
    public org.bpelunit.framework.xml.result.XMLTestResult getTestResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLTestResult target = null;
            target = (org.bpelunit.framework.xml.result.XMLTestResult)get_store().find_element_user(TESTRESULT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "testResult" element
     */
    public void setTestResult(org.bpelunit.framework.xml.result.XMLTestResult testResult)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLTestResult target = null;
            target = (org.bpelunit.framework.xml.result.XMLTestResult)get_store().find_element_user(TESTRESULT$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.result.XMLTestResult)get_store().add_element_user(TESTRESULT$0);
            }
            target.set(testResult);
        }
    }
    
    /**
     * Appends and returns a new empty "testResult" element
     */
    public org.bpelunit.framework.xml.result.XMLTestResult addNewTestResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLTestResult target = null;
            target = (org.bpelunit.framework.xml.result.XMLTestResult)get_store().add_element_user(TESTRESULT$0);
            return target;
        }
    }
}
