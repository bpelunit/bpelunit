/*
 * XML Type:  ReceiveActivity
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLReceiveActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML ReceiveActivity(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLReceiveActivityImpl extends org.bpelunit.framework.xml.suite.impl.XMLActivityImpl implements org.bpelunit.framework.xml.suite.XMLReceiveActivity
{
    private static final long serialVersionUID = 1L;
    
    public XMLReceiveActivityImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONDITION$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "condition");
    private static final javax.xml.namespace.QName FAULT$2 = 
        new javax.xml.namespace.QName("", "fault");
    
    
    /**
     * Gets a List of "condition" elements
     */
    public java.util.List<org.bpelunit.framework.xml.suite.XMLCondition> getConditionList()
    {
        final class ConditionList extends java.util.AbstractList<org.bpelunit.framework.xml.suite.XMLCondition>
        {
            public org.bpelunit.framework.xml.suite.XMLCondition get(int i)
                { return XMLReceiveActivityImpl.this.getConditionArray(i); }
            
            public org.bpelunit.framework.xml.suite.XMLCondition set(int i, org.bpelunit.framework.xml.suite.XMLCondition o)
            {
                org.bpelunit.framework.xml.suite.XMLCondition old = XMLReceiveActivityImpl.this.getConditionArray(i);
                XMLReceiveActivityImpl.this.setConditionArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.suite.XMLCondition o)
                { XMLReceiveActivityImpl.this.insertNewCondition(i).set(o); }
            
            public org.bpelunit.framework.xml.suite.XMLCondition remove(int i)
            {
                org.bpelunit.framework.xml.suite.XMLCondition old = XMLReceiveActivityImpl.this.getConditionArray(i);
                XMLReceiveActivityImpl.this.removeCondition(i);
                return old;
            }
            
            public int size()
                { return XMLReceiveActivityImpl.this.sizeOfConditionArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new ConditionList();
        }
    }
    
    /**
     * Gets array of all "condition" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.suite.XMLCondition[] getConditionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.suite.XMLCondition> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.suite.XMLCondition>();
            get_store().find_all_element_users(CONDITION$0, targetList);
            org.bpelunit.framework.xml.suite.XMLCondition[] result = new org.bpelunit.framework.xml.suite.XMLCondition[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "condition" element
     */
    public org.bpelunit.framework.xml.suite.XMLCondition getConditionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCondition target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCondition)get_store().find_element_user(CONDITION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "condition" element
     */
    public int sizeOfConditionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONDITION$0);
        }
    }
    
    /**
     * Sets array of all "condition" element
     */
    public void setConditionArray(org.bpelunit.framework.xml.suite.XMLCondition[] conditionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(conditionArray, CONDITION$0);
        }
    }
    
    /**
     * Sets ith "condition" element
     */
    public void setConditionArray(int i, org.bpelunit.framework.xml.suite.XMLCondition condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCondition target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCondition)get_store().find_element_user(CONDITION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(condition);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "condition" element
     */
    public org.bpelunit.framework.xml.suite.XMLCondition insertNewCondition(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCondition target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCondition)get_store().insert_element_user(CONDITION$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "condition" element
     */
    public org.bpelunit.framework.xml.suite.XMLCondition addNewCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCondition target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCondition)get_store().add_element_user(CONDITION$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "condition" element
     */
    public void removeCondition(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONDITION$0, i);
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
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(FAULT$2);
            }
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
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(FAULT$2);
            }
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
}
