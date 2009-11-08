/*
 * XML Type:  Data
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLData
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * An XML Data(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public class XMLDataImpl extends org.bpelunit.framework.xml.result.impl.XMLArtefactImpl implements org.bpelunit.framework.xml.result.XMLData
{
    
    public XMLDataImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName XMLDATA$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "xmlData");
    private static final javax.xml.namespace.QName RECEIVECONDITION$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "receiveCondition");
    
    
    /**
     * Gets array of all "xmlData" elements
     */
    public org.bpelunit.framework.xml.result.XMLData.XmlData[] getXmlDataArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(XMLDATA$0, targetList);
            org.bpelunit.framework.xml.result.XMLData.XmlData[] result = new org.bpelunit.framework.xml.result.XMLData.XmlData[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "xmlData" element
     */
    public org.bpelunit.framework.xml.result.XMLData.XmlData getXmlDataArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData.XmlData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData.XmlData)get_store().find_element_user(XMLDATA$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "xmlData" element
     */
    public int sizeOfXmlDataArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(XMLDATA$0);
        }
    }
    
    /**
     * Sets array of all "xmlData" element
     */
    public void setXmlDataArray(org.bpelunit.framework.xml.result.XMLData.XmlData[] xmlDataArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(xmlDataArray, XMLDATA$0);
        }
    }
    
    /**
     * Sets ith "xmlData" element
     */
    public void setXmlDataArray(int i, org.bpelunit.framework.xml.result.XMLData.XmlData xmlData)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData.XmlData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData.XmlData)get_store().find_element_user(XMLDATA$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(xmlData);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "xmlData" element
     */
    public org.bpelunit.framework.xml.result.XMLData.XmlData insertNewXmlData(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData.XmlData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData.XmlData)get_store().insert_element_user(XMLDATA$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "xmlData" element
     */
    public org.bpelunit.framework.xml.result.XMLData.XmlData addNewXmlData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData.XmlData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData.XmlData)get_store().add_element_user(XMLDATA$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "xmlData" element
     */
    public void removeXmlData(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(XMLDATA$0, i);
        }
    }
    
    /**
     * Gets array of all "receiveCondition" elements
     */
    public org.bpelunit.framework.xml.result.XMLReceiveCondition[] getReceiveConditionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RECEIVECONDITION$2, targetList);
            org.bpelunit.framework.xml.result.XMLReceiveCondition[] result = new org.bpelunit.framework.xml.result.XMLReceiveCondition[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "receiveCondition" element
     */
    public org.bpelunit.framework.xml.result.XMLReceiveCondition getReceiveConditionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLReceiveCondition target = null;
            target = (org.bpelunit.framework.xml.result.XMLReceiveCondition)get_store().find_element_user(RECEIVECONDITION$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "receiveCondition" element
     */
    public int sizeOfReceiveConditionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RECEIVECONDITION$2);
        }
    }
    
    /**
     * Sets array of all "receiveCondition" element
     */
    public void setReceiveConditionArray(org.bpelunit.framework.xml.result.XMLReceiveCondition[] receiveConditionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(receiveConditionArray, RECEIVECONDITION$2);
        }
    }
    
    /**
     * Sets ith "receiveCondition" element
     */
    public void setReceiveConditionArray(int i, org.bpelunit.framework.xml.result.XMLReceiveCondition receiveCondition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLReceiveCondition target = null;
            target = (org.bpelunit.framework.xml.result.XMLReceiveCondition)get_store().find_element_user(RECEIVECONDITION$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(receiveCondition);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveCondition" element
     */
    public org.bpelunit.framework.xml.result.XMLReceiveCondition insertNewReceiveCondition(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLReceiveCondition target = null;
            target = (org.bpelunit.framework.xml.result.XMLReceiveCondition)get_store().insert_element_user(RECEIVECONDITION$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveCondition" element
     */
    public org.bpelunit.framework.xml.result.XMLReceiveCondition addNewReceiveCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLReceiveCondition target = null;
            target = (org.bpelunit.framework.xml.result.XMLReceiveCondition)get_store().add_element_user(RECEIVECONDITION$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "receiveCondition" element
     */
    public void removeReceiveCondition(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RECEIVECONDITION$2, i);
        }
    }
    /**
     * An XML xmlData(@http://www.bpelunit.org/schema/testResult).
     *
     * This is a complex type.
     */
    public static class XmlDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.result.XMLData.XmlData
    {
        
        public XmlDataImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName NAME$0 = 
            new javax.xml.namespace.QName("", "name");
        
        
        /**
         * Gets the "name" attribute
         */
        public java.lang.String getName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "name" attribute
         */
        public org.apache.xmlbeans.XmlString xgetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$0);
                return target;
            }
        }
        
        /**
         * Sets the "name" attribute
         */
        public void setName(java.lang.String name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$0);
                }
                target.setStringValue(name);
            }
        }
        
        /**
         * Sets (as xml) the "name" attribute
         */
        public void xsetName(org.apache.xmlbeans.XmlString name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$0);
                }
                target.set(name);
            }
        }
    }
}
