/*
 * XML Type:  TestSuite
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTestSuite
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML TestSuite(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLTestSuiteImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLTestSuite
{
    private static final long serialVersionUID = 1L;
    
    public XMLTestSuiteImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "name");
    private static final javax.xml.namespace.QName BASEURL$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "baseURL");
    private static final javax.xml.namespace.QName DEPLOYMENT$4 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "deployment");
    private static final javax.xml.namespace.QName TESTCASES$6 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "testCases");
    
    
    /**
     * Gets the "name" element
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "name" element
     */
    public org.apache.xmlbeans.XmlString xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "name" element
     */
    public void setName(java.lang.String name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NAME$0);
            }
            target.setStringValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "name" element
     */
    public void xsetName(org.apache.xmlbeans.XmlString name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAME$0);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "baseURL" element
     */
    public java.lang.String getBaseURL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BASEURL$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "baseURL" element
     */
    public org.apache.xmlbeans.XmlString xgetBaseURL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BASEURL$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "baseURL" element
     */
    public boolean isSetBaseURL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(BASEURL$2) != 0;
        }
    }
    
    /**
     * Sets the "baseURL" element
     */
    public void setBaseURL(java.lang.String baseURL)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BASEURL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BASEURL$2);
            }
            target.setStringValue(baseURL);
        }
    }
    
    /**
     * Sets (as xml) the "baseURL" element
     */
    public void xsetBaseURL(org.apache.xmlbeans.XmlString baseURL)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BASEURL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(BASEURL$2);
            }
            target.set(baseURL);
        }
    }
    
    /**
     * Unsets the "baseURL" element
     */
    public void unsetBaseURL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(BASEURL$2, 0);
        }
    }
    
    /**
     * Gets the "deployment" element
     */
    public org.bpelunit.framework.xml.suite.XMLDeploymentSection getDeployment()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLDeploymentSection target = null;
            target = (org.bpelunit.framework.xml.suite.XMLDeploymentSection)get_store().find_element_user(DEPLOYMENT$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "deployment" element
     */
    public void setDeployment(org.bpelunit.framework.xml.suite.XMLDeploymentSection deployment)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLDeploymentSection target = null;
            target = (org.bpelunit.framework.xml.suite.XMLDeploymentSection)get_store().find_element_user(DEPLOYMENT$4, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLDeploymentSection)get_store().add_element_user(DEPLOYMENT$4);
            }
            target.set(deployment);
        }
    }
    
    /**
     * Appends and returns a new empty "deployment" element
     */
    public org.bpelunit.framework.xml.suite.XMLDeploymentSection addNewDeployment()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLDeploymentSection target = null;
            target = (org.bpelunit.framework.xml.suite.XMLDeploymentSection)get_store().add_element_user(DEPLOYMENT$4);
            return target;
        }
    }
    
    /**
     * Gets the "testCases" element
     */
    public org.bpelunit.framework.xml.suite.XMLTestCasesSection getTestCases()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestCasesSection target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestCasesSection)get_store().find_element_user(TESTCASES$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "testCases" element
     */
    public void setTestCases(org.bpelunit.framework.xml.suite.XMLTestCasesSection testCases)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestCasesSection target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestCasesSection)get_store().find_element_user(TESTCASES$6, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLTestCasesSection)get_store().add_element_user(TESTCASES$6);
            }
            target.set(testCases);
        }
    }
    
    /**
     * Appends and returns a new empty "testCases" element
     */
    public org.bpelunit.framework.xml.suite.XMLTestCasesSection addNewTestCases()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestCasesSection target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestCasesSection)get_store().add_element_user(TESTCASES$6);
            return target;
        }
    }
}
