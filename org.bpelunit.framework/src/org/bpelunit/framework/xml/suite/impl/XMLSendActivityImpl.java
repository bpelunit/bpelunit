/*
 * XML Type:  SendActivity
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLSendActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML SendActivity(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLSendActivityImpl extends org.bpelunit.framework.xml.suite.impl.XMLActivityImpl implements org.bpelunit.framework.xml.suite.XMLSendActivity
{
    private static final long serialVersionUID = 1L;
    
    public XMLSendActivityImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DATA$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "data");
    private static final javax.xml.namespace.QName FAULT$2 = 
        new javax.xml.namespace.QName("", "fault");
    private static final javax.xml.namespace.QName DELAYSEQUENCE$4 = 
        new javax.xml.namespace.QName("", "delaySequence");
    
    
    /**
     * Gets the "data" element
     */
    public org.bpelunit.framework.xml.suite.XMLAnyElement getData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLAnyElement target = null;
            target = (org.bpelunit.framework.xml.suite.XMLAnyElement)get_store().find_element_user(DATA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "data" element
     */
    public void setData(org.bpelunit.framework.xml.suite.XMLAnyElement data)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLAnyElement target = null;
            target = (org.bpelunit.framework.xml.suite.XMLAnyElement)get_store().find_element_user(DATA$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLAnyElement)get_store().add_element_user(DATA$0);
            }
            target.set(data);
        }
    }
    
    /**
     * Appends and returns a new empty "data" element
     */
    public org.bpelunit.framework.xml.suite.XMLAnyElement addNewData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLAnyElement target = null;
            target = (org.bpelunit.framework.xml.suite.XMLAnyElement)get_store().add_element_user(DATA$0);
            return target;
        }
    }
    
    /**
     * Gets the "fault" attribute
     */
    public boolean getFault()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FAULT$2);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "fault" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetFault()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(FAULT$2);
            return target;
        }
    }
    
    /**
     * True if has "fault" attribute
     */
    public boolean isSetFault()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FAULT$2) != null;
        }
    }
    
    /**
     * Sets the "fault" attribute
     */
    public void setFault(boolean fault)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FAULT$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FAULT$2);
            }
            target.setBooleanValue(fault);
        }
    }
    
    /**
     * Sets (as xml) the "fault" attribute
     */
    public void xsetFault(org.apache.xmlbeans.XmlBoolean fault)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(FAULT$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(FAULT$2);
            }
            target.set(fault);
        }
    }
    
    /**
     * Unsets the "fault" attribute
     */
    public void unsetFault()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FAULT$2);
        }
    }
    
    /**
     * Gets the "delaySequence" attribute
     */
    public java.lang.String getDelaySequence()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DELAYSEQUENCE$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "delaySequence" attribute
     */
    public org.apache.xmlbeans.XmlString xgetDelaySequence()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DELAYSEQUENCE$4);
            return target;
        }
    }
    
    /**
     * True if has "delaySequence" attribute
     */
    public boolean isSetDelaySequence()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(DELAYSEQUENCE$4) != null;
        }
    }
    
    /**
     * Sets the "delaySequence" attribute
     */
    public void setDelaySequence(java.lang.String delaySequence)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DELAYSEQUENCE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DELAYSEQUENCE$4);
            }
            target.setStringValue(delaySequence);
        }
    }
    
    /**
     * Sets (as xml) the "delaySequence" attribute
     */
    public void xsetDelaySequence(org.apache.xmlbeans.XmlString delaySequence)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DELAYSEQUENCE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(DELAYSEQUENCE$4);
            }
            target.set(delaySequence);
        }
    }
    
    /**
     * Unsets the "delaySequence" attribute
     */
    public void unsetDelaySequence()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(DELAYSEQUENCE$4);
        }
    }
}
