/*
 * XML Type:  TwoWayActivity
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTwoWayActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML TwoWayActivity(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLTwoWayActivityImpl extends org.bpelunit.framework.xml.suite.impl.XMLActivityImpl implements org.bpelunit.framework.xml.suite.XMLTwoWayActivity
{
    private static final long serialVersionUID = 1L;
    
    public XMLTwoWayActivityImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RECEIVE$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "receive");
    private static final javax.xml.namespace.QName SEND$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "send");
    private static final javax.xml.namespace.QName MAPPING$4 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "mapping");
    private static final javax.xml.namespace.QName HEADERPROCESSOR$6 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "headerProcessor");
    
    
    /**
     * Gets the "receive" element
     */
    public org.bpelunit.framework.xml.suite.XMLReceiveActivity getReceive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLReceiveActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().find_element_user(RECEIVE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "receive" element
     */
    public void setReceive(org.bpelunit.framework.xml.suite.XMLReceiveActivity receive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLReceiveActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().find_element_user(RECEIVE$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().add_element_user(RECEIVE$0);
            }
            target.set(receive);
        }
    }
    
    /**
     * Appends and returns a new empty "receive" element
     */
    public org.bpelunit.framework.xml.suite.XMLReceiveActivity addNewReceive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLReceiveActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().add_element_user(RECEIVE$0);
            return target;
        }
    }
    
    /**
     * Gets the "send" element
     */
    public org.bpelunit.framework.xml.suite.XMLSendActivity getSend()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLSendActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().find_element_user(SEND$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "send" element
     */
    public void setSend(org.bpelunit.framework.xml.suite.XMLSendActivity send)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLSendActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().find_element_user(SEND$2, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().add_element_user(SEND$2);
            }
            target.set(send);
        }
    }
    
    /**
     * Appends and returns a new empty "send" element
     */
    public org.bpelunit.framework.xml.suite.XMLSendActivity addNewSend()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLSendActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().add_element_user(SEND$2);
            return target;
        }
    }
    
    /**
     * Gets the "mapping" element
     */
    public org.bpelunit.framework.xml.suite.XMLMapping getMapping()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLMapping target = null;
            target = (org.bpelunit.framework.xml.suite.XMLMapping)get_store().find_element_user(MAPPING$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "mapping" element
     */
    public boolean isSetMapping()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(MAPPING$4) != 0;
        }
    }
    
    /**
     * Sets the "mapping" element
     */
    public void setMapping(org.bpelunit.framework.xml.suite.XMLMapping mapping)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLMapping target = null;
            target = (org.bpelunit.framework.xml.suite.XMLMapping)get_store().find_element_user(MAPPING$4, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLMapping)get_store().add_element_user(MAPPING$4);
            }
            target.set(mapping);
        }
    }
    
    /**
     * Appends and returns a new empty "mapping" element
     */
    public org.bpelunit.framework.xml.suite.XMLMapping addNewMapping()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLMapping target = null;
            target = (org.bpelunit.framework.xml.suite.XMLMapping)get_store().add_element_user(MAPPING$4);
            return target;
        }
    }
    
    /**
     * Unsets the "mapping" element
     */
    public void unsetMapping()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(MAPPING$4, 0);
        }
    }
    
    /**
     * Gets the "headerProcessor" element
     */
    public org.bpelunit.framework.xml.suite.XMLHeaderProcessor getHeaderProcessor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLHeaderProcessor target = null;
            target = (org.bpelunit.framework.xml.suite.XMLHeaderProcessor)get_store().find_element_user(HEADERPROCESSOR$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "headerProcessor" element
     */
    public boolean isSetHeaderProcessor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(HEADERPROCESSOR$6) != 0;
        }
    }
    
    /**
     * Sets the "headerProcessor" element
     */
    public void setHeaderProcessor(org.bpelunit.framework.xml.suite.XMLHeaderProcessor headerProcessor)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLHeaderProcessor target = null;
            target = (org.bpelunit.framework.xml.suite.XMLHeaderProcessor)get_store().find_element_user(HEADERPROCESSOR$6, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLHeaderProcessor)get_store().add_element_user(HEADERPROCESSOR$6);
            }
            target.set(headerProcessor);
        }
    }
    
    /**
     * Appends and returns a new empty "headerProcessor" element
     */
    public org.bpelunit.framework.xml.suite.XMLHeaderProcessor addNewHeaderProcessor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLHeaderProcessor target = null;
            target = (org.bpelunit.framework.xml.suite.XMLHeaderProcessor)get_store().add_element_user(HEADERPROCESSOR$6);
            return target;
        }
    }
    
    /**
     * Unsets the "headerProcessor" element
     */
    public void unsetHeaderProcessor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(HEADERPROCESSOR$6, 0);
        }
    }
}
