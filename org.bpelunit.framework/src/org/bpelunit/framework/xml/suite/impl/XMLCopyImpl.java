/*
 * XML Type:  Copy
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLCopy
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML Copy(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLCopyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLCopy
{
    
    public XMLCopyImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FROM$0 = 
        new javax.xml.namespace.QName("", "from");
    private static final javax.xml.namespace.QName TO$2 = 
        new javax.xml.namespace.QName("", "to");
    
    
    /**
     * Gets the "from" attribute
     */
    public java.lang.String getFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROM$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "from" attribute
     */
    public org.apache.xmlbeans.XmlString xgetFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FROM$0);
            return target;
        }
    }
    
    /**
     * Sets the "from" attribute
     */
    public void setFrom(java.lang.String from)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROM$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FROM$0);
            }
            target.setStringValue(from);
        }
    }
    
    /**
     * Sets (as xml) the "from" attribute
     */
    public void xsetFrom(org.apache.xmlbeans.XmlString from)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FROM$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FROM$0);
            }
            target.set(from);
        }
    }
    
    /**
     * Gets the "to" attribute
     */
    public java.lang.String getTo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TO$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "to" attribute
     */
    public org.apache.xmlbeans.XmlString xgetTo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TO$2);
            return target;
        }
    }
    
    /**
     * Sets the "to" attribute
     */
    public void setTo(java.lang.String to)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TO$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TO$2);
            }
            target.setStringValue(to);
        }
    }
    
    /**
     * Sets (as xml) the "to" attribute
     */
    public void xsetTo(org.apache.xmlbeans.XmlString to)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TO$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TO$2);
            }
            target.set(to);
        }
    }
}
