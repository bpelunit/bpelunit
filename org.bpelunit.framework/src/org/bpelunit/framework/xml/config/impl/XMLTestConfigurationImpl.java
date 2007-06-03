/*
 * XML Type:  TestConfiguration
 * Namespace: http://www.bpelunit.org/schema/testConfiguration
 * Java type: org.bpelunit.framework.xml.config.XMLTestConfiguration
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.config.impl;
/**
 * An XML TestConfiguration(@http://www.bpelunit.org/schema/testConfiguration).
 *
 * This is a complex type.
 */
public class XMLTestConfigurationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.config.XMLTestConfiguration
{
    
    public XMLTestConfigurationImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONFIGURATION$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testConfiguration", "configuration");
    
    
    /**
     * Gets a List of "configuration" elements
     */
    public java.util.List<org.bpelunit.framework.xml.config.XMLConfiguration> getConfigurationList()
    {
        final class ConfigurationList extends java.util.AbstractList<org.bpelunit.framework.xml.config.XMLConfiguration>
        {
            public org.bpelunit.framework.xml.config.XMLConfiguration get(int i)
                { return XMLTestConfigurationImpl.this.getConfigurationArray(i); }
            
            public org.bpelunit.framework.xml.config.XMLConfiguration set(int i, org.bpelunit.framework.xml.config.XMLConfiguration o)
            {
                org.bpelunit.framework.xml.config.XMLConfiguration old = XMLTestConfigurationImpl.this.getConfigurationArray(i);
                XMLTestConfigurationImpl.this.setConfigurationArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.config.XMLConfiguration o)
                { XMLTestConfigurationImpl.this.insertNewConfiguration(i).set(o); }
            
            public org.bpelunit.framework.xml.config.XMLConfiguration remove(int i)
            {
                org.bpelunit.framework.xml.config.XMLConfiguration old = XMLTestConfigurationImpl.this.getConfigurationArray(i);
                XMLTestConfigurationImpl.this.removeConfiguration(i);
                return old;
            }
            
            public int size()
                { return XMLTestConfigurationImpl.this.sizeOfConfigurationArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new ConfigurationList();
        }
    }
    
    /**
     * Gets array of all "configuration" elements
     */
    public org.bpelunit.framework.xml.config.XMLConfiguration[] getConfigurationArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CONFIGURATION$0, targetList);
            org.bpelunit.framework.xml.config.XMLConfiguration[] result = new org.bpelunit.framework.xml.config.XMLConfiguration[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "configuration" element
     */
    public org.bpelunit.framework.xml.config.XMLConfiguration getConfigurationArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLConfiguration target = null;
            target = (org.bpelunit.framework.xml.config.XMLConfiguration)get_store().find_element_user(CONFIGURATION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "configuration" element
     */
    public int sizeOfConfigurationArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONFIGURATION$0);
        }
    }
    
    /**
     * Sets array of all "configuration" element
     */
    public void setConfigurationArray(org.bpelunit.framework.xml.config.XMLConfiguration[] configurationArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(configurationArray, CONFIGURATION$0);
        }
    }
    
    /**
     * Sets ith "configuration" element
     */
    public void setConfigurationArray(int i, org.bpelunit.framework.xml.config.XMLConfiguration configuration)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLConfiguration target = null;
            target = (org.bpelunit.framework.xml.config.XMLConfiguration)get_store().find_element_user(CONFIGURATION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(configuration);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "configuration" element
     */
    public org.bpelunit.framework.xml.config.XMLConfiguration insertNewConfiguration(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLConfiguration target = null;
            target = (org.bpelunit.framework.xml.config.XMLConfiguration)get_store().insert_element_user(CONFIGURATION$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "configuration" element
     */
    public org.bpelunit.framework.xml.config.XMLConfiguration addNewConfiguration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLConfiguration target = null;
            target = (org.bpelunit.framework.xml.config.XMLConfiguration)get_store().add_element_user(CONFIGURATION$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "configuration" element
     */
    public void removeConfiguration(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONFIGURATION$0, i);
        }
    }
}
