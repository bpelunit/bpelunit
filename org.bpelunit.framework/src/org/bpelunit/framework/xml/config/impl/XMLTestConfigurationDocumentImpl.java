/*
 * An XML document type.
 * Localname: testConfiguration
 * Namespace: http://www.bpelunit.org/schema/testConfiguration
 * Java type: org.bpelunit.framework.xml.config.XMLTestConfigurationDocument
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.config.impl;
/**
 * A document containing one testConfiguration(@http://www.bpelunit.org/schema/testConfiguration) element.
 *
 * This is a complex type.
 */
public class XMLTestConfigurationDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.config.XMLTestConfigurationDocument
{
    
    public XMLTestConfigurationDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TESTCONFIGURATION$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testConfiguration", "testConfiguration");
    
    
    /**
     * Gets the "testConfiguration" element
     */
    public org.bpelunit.framework.xml.config.XMLTestConfiguration getTestConfiguration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLTestConfiguration target = null;
            target = (org.bpelunit.framework.xml.config.XMLTestConfiguration)get_store().find_element_user(TESTCONFIGURATION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "testConfiguration" element
     */
    public void setTestConfiguration(org.bpelunit.framework.xml.config.XMLTestConfiguration testConfiguration)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLTestConfiguration target = null;
            target = (org.bpelunit.framework.xml.config.XMLTestConfiguration)get_store().find_element_user(TESTCONFIGURATION$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.config.XMLTestConfiguration)get_store().add_element_user(TESTCONFIGURATION$0);
            }
            target.set(testConfiguration);
        }
    }
    
    /**
     * Appends and returns a new empty "testConfiguration" element
     */
    public org.bpelunit.framework.xml.config.XMLTestConfiguration addNewTestConfiguration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLTestConfiguration target = null;
            target = (org.bpelunit.framework.xml.config.XMLTestConfiguration)get_store().add_element_user(TESTCONFIGURATION$0);
            return target;
        }
    }
}
