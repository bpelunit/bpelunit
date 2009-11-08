/*
 * XML Type:  SoapActivity
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLSoapActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML SoapActivity(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLSoapActivityImpl extends org.bpelunit.framework.xml.suite.impl.XMLActivityImpl implements org.bpelunit.framework.xml.suite.XMLSoapActivity
{
    
    public XMLSoapActivityImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SERVICE$0 = 
        new javax.xml.namespace.QName("", "service");
    private static final javax.xml.namespace.QName PORT$2 = 
        new javax.xml.namespace.QName("", "port");
    private static final javax.xml.namespace.QName OPERATION$4 = 
        new javax.xml.namespace.QName("", "operation");
    
    
    /**
     * Gets the "service" attribute
     */
    public javax.xml.namespace.QName getService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SERVICE$0);
            if (target == null)
            {
                return null;
            }
            return target.getQNameValue();
        }
    }
    
    /**
     * Gets (as xml) the "service" attribute
     */
    public org.apache.xmlbeans.XmlQName xgetService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlQName target = null;
            target = (org.apache.xmlbeans.XmlQName)get_store().find_attribute_user(SERVICE$0);
            return target;
        }
    }
    
    /**
     * True if has "service" attribute
     */
    public boolean isSetService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(SERVICE$0) != null;
        }
    }
    
    /**
     * Sets the "service" attribute
     */
    public void setService(javax.xml.namespace.QName service)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SERVICE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SERVICE$0);
            }
            target.setQNameValue(service);
        }
    }
    
    /**
     * Sets (as xml) the "service" attribute
     */
    public void xsetService(org.apache.xmlbeans.XmlQName service)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlQName target = null;
            target = (org.apache.xmlbeans.XmlQName)get_store().find_attribute_user(SERVICE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlQName)get_store().add_attribute_user(SERVICE$0);
            }
            target.set(service);
        }
    }
    
    /**
     * Unsets the "service" attribute
     */
    public void unsetService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(SERVICE$0);
        }
    }
    
    /**
     * Gets the "port" attribute
     */
    public java.lang.String getPort()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PORT$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "port" attribute
     */
    public org.apache.xmlbeans.XmlString xgetPort()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PORT$2);
            return target;
        }
    }
    
    /**
     * True if has "port" attribute
     */
    public boolean isSetPort()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(PORT$2) != null;
        }
    }
    
    /**
     * Sets the "port" attribute
     */
    public void setPort(java.lang.String port)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PORT$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PORT$2);
            }
            target.setStringValue(port);
        }
    }
    
    /**
     * Sets (as xml) the "port" attribute
     */
    public void xsetPort(org.apache.xmlbeans.XmlString port)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PORT$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PORT$2);
            }
            target.set(port);
        }
    }
    
    /**
     * Unsets the "port" attribute
     */
    public void unsetPort()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(PORT$2);
        }
    }
    
    /**
     * Gets the "operation" attribute
     */
    public java.lang.String getOperation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPERATION$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "operation" attribute
     */
    public org.apache.xmlbeans.XmlString xgetOperation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OPERATION$4);
            return target;
        }
    }
    
    /**
     * True if has "operation" attribute
     */
    public boolean isSetOperation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(OPERATION$4) != null;
        }
    }
    
    /**
     * Sets the "operation" attribute
     */
    public void setOperation(java.lang.String operation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPERATION$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OPERATION$4);
            }
            target.setStringValue(operation);
        }
    }
    
    /**
     * Sets (as xml) the "operation" attribute
     */
    public void xsetOperation(org.apache.xmlbeans.XmlString operation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(OPERATION$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(OPERATION$4);
            }
            target.set(operation);
        }
    }
    
    /**
     * Unsets the "operation" attribute
     */
    public void unsetOperation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(OPERATION$4);
        }
    }
}
