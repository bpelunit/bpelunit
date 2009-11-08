/*
 * XML Type:  WaitActivity
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLWaitActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML WaitActivity(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLWaitActivityImpl extends org.bpelunit.framework.xml.suite.impl.XMLActivityImpl implements org.bpelunit.framework.xml.suite.XMLWaitActivity
{
    
    public XMLWaitActivityImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName WAITFORMILLISECONDS$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "waitForMilliseconds");
    
    
    /**
     * Gets the "waitForMilliseconds" element
     */
    public long getWaitForMilliseconds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WAITFORMILLISECONDS$0, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "waitForMilliseconds" element
     */
    public org.apache.xmlbeans.XmlUnsignedInt xgetWaitForMilliseconds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(WAITFORMILLISECONDS$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "waitForMilliseconds" element
     */
    public void setWaitForMilliseconds(long waitForMilliseconds)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WAITFORMILLISECONDS$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(WAITFORMILLISECONDS$0);
            }
            target.setLongValue(waitForMilliseconds);
        }
    }
    
    /**
     * Sets (as xml) the "waitForMilliseconds" element
     */
    public void xsetWaitForMilliseconds(org.apache.xmlbeans.XmlUnsignedInt waitForMilliseconds)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(WAITFORMILLISECONDS$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(WAITFORMILLISECONDS$0);
            }
            target.set(waitForMilliseconds);
        }
    }
}
