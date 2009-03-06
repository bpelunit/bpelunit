/*
 * An XML document type.
 * Localname: testSuite
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTestSuiteDocument
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * A document containing one testSuite(@http://www.bpelunit.org/schema/testSuite) element.
 *
 * This is a complex type.
 */
public class XMLTestSuiteDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLTestSuiteDocument
{
    private static final long serialVersionUID = 1L;
    
    public XMLTestSuiteDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TESTSUITE$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "testSuite");
    
    
    /**
     * Gets the "testSuite" element
     */
    public org.bpelunit.framework.xml.suite.XMLTestSuite getTestSuite()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestSuite target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestSuite)get_store().find_element_user(TESTSUITE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "testSuite" element
     */
    public void setTestSuite(org.bpelunit.framework.xml.suite.XMLTestSuite testSuite)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestSuite target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestSuite)get_store().find_element_user(TESTSUITE$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLTestSuite)get_store().add_element_user(TESTSUITE$0);
            }
            target.set(testSuite);
        }
    }
    
    /**
     * Appends and returns a new empty "testSuite" element
     */
    public org.bpelunit.framework.xml.suite.XMLTestSuite addNewTestSuite()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestSuite target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestSuite)get_store().add_element_user(TESTSUITE$0);
            return target;
        }
    }
}
