/*
 * XML Type:  Configuration
 * Namespace: http://www.bpelunit.org/schema/testConfiguration
 * Java type: org.bpelunit.framework.xml.config.XMLConfiguration
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.config.impl;
/**
 * An XML Configuration(@http://www.bpelunit.org/schema/testConfiguration).
 *
 * This is a complex type.
 */
public class XMLConfigurationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.config.XMLConfiguration
{
    
    public XMLConfigurationImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROPERTY$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testConfiguration", "property");
    private static final javax.xml.namespace.QName DEPLOYER$2 = 
        new javax.xml.namespace.QName("", "deployer");
    
    
    /**
     * Gets array of all "property" elements
     */
    public org.bpelunit.framework.xml.config.XMLProperty[] getPropertyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PROPERTY$0, targetList);
            org.bpelunit.framework.xml.config.XMLProperty[] result = new org.bpelunit.framework.xml.config.XMLProperty[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "property" element
     */
    public org.bpelunit.framework.xml.config.XMLProperty getPropertyArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.config.XMLProperty)get_store().find_element_user(PROPERTY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "property" element
     */
    public int sizeOfPropertyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROPERTY$0);
        }
    }
    
    /**
     * Sets array of all "property" element
     */
    public void setPropertyArray(org.bpelunit.framework.xml.config.XMLProperty[] propertyArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(propertyArray, PROPERTY$0);
        }
    }
    
    /**
     * Sets ith "property" element
     */
    public void setPropertyArray(int i, org.bpelunit.framework.xml.config.XMLProperty property)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.config.XMLProperty)get_store().find_element_user(PROPERTY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(property);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "property" element
     */
    public org.bpelunit.framework.xml.config.XMLProperty insertNewProperty(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.config.XMLProperty)get_store().insert_element_user(PROPERTY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "property" element
     */
    public org.bpelunit.framework.xml.config.XMLProperty addNewProperty()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.config.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.config.XMLProperty)get_store().add_element_user(PROPERTY$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "property" element
     */
    public void removeProperty(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROPERTY$0, i);
        }
    }
    
    /**
     * Gets the "deployer" attribute
     */
    public java.lang.String getDeployer()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DEPLOYER$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "deployer" attribute
     */
    public org.apache.xmlbeans.XmlString xgetDeployer()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DEPLOYER$2);
            return target;
        }
    }
    
    /**
     * Sets the "deployer" attribute
     */
    public void setDeployer(java.lang.String deployer)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DEPLOYER$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DEPLOYER$2);
            }
            target.setStringValue(deployer);
        }
    }
    
    /**
     * Sets (as xml) the "deployer" attribute
     */
    public void xsetDeployer(org.apache.xmlbeans.XmlString deployer)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DEPLOYER$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(DEPLOYER$2);
            }
            target.set(deployer);
        }
    }
}
