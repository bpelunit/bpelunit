/*
 * XML Type:  Artefact
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLArtefact
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * An XML Artefact(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public class XMLArtefactImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.result.XMLArtefact
{
    private static final long serialVersionUID = 1L;
    
    public XMLArtefactImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName STATE$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "state");
    private static final javax.xml.namespace.QName NAME$2 = 
        new javax.xml.namespace.QName("", "name");
    private static final javax.xml.namespace.QName RESULT$4 = 
        new javax.xml.namespace.QName("", "result");
    private static final javax.xml.namespace.QName MESSAGE$6 = 
        new javax.xml.namespace.QName("", "message");
    private static final javax.xml.namespace.QName EXCEPTION$8 = 
        new javax.xml.namespace.QName("", "exception");
    
    
    /**
     * Gets a List of "state" elements
     */
    public java.util.List<org.bpelunit.framework.xml.result.XMLInfo> getStateList()
    {
        final class StateList extends java.util.AbstractList<org.bpelunit.framework.xml.result.XMLInfo>
        {
            public org.bpelunit.framework.xml.result.XMLInfo get(int i)
                { return XMLArtefactImpl.this.getStateArray(i); }
            
            public org.bpelunit.framework.xml.result.XMLInfo set(int i, org.bpelunit.framework.xml.result.XMLInfo o)
            {
                org.bpelunit.framework.xml.result.XMLInfo old = XMLArtefactImpl.this.getStateArray(i);
                XMLArtefactImpl.this.setStateArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.result.XMLInfo o)
                { XMLArtefactImpl.this.insertNewState(i).set(o); }
            
            public org.bpelunit.framework.xml.result.XMLInfo remove(int i)
            {
                org.bpelunit.framework.xml.result.XMLInfo old = XMLArtefactImpl.this.getStateArray(i);
                XMLArtefactImpl.this.removeState(i);
                return old;
            }
            
            public int size()
                { return XMLArtefactImpl.this.sizeOfStateArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new StateList();
        }
    }
    
    /**
     * Gets array of all "state" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.result.XMLInfo[] getStateArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.result.XMLInfo> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.result.XMLInfo>();
            get_store().find_all_element_users(STATE$0, targetList);
            org.bpelunit.framework.xml.result.XMLInfo[] result = new org.bpelunit.framework.xml.result.XMLInfo[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "state" element
     */
    public org.bpelunit.framework.xml.result.XMLInfo getStateArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLInfo target = null;
            target = (org.bpelunit.framework.xml.result.XMLInfo)get_store().find_element_user(STATE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "state" element
     */
    public int sizeOfStateArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(STATE$0);
        }
    }
    
    /**
     * Sets array of all "state" element
     */
    public void setStateArray(org.bpelunit.framework.xml.result.XMLInfo[] stateArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(stateArray, STATE$0);
        }
    }
    
    /**
     * Sets ith "state" element
     */
    public void setStateArray(int i, org.bpelunit.framework.xml.result.XMLInfo state)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLInfo target = null;
            target = (org.bpelunit.framework.xml.result.XMLInfo)get_store().find_element_user(STATE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(state);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "state" element
     */
    public org.bpelunit.framework.xml.result.XMLInfo insertNewState(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLInfo target = null;
            target = (org.bpelunit.framework.xml.result.XMLInfo)get_store().insert_element_user(STATE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "state" element
     */
    public org.bpelunit.framework.xml.result.XMLInfo addNewState()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLInfo target = null;
            target = (org.bpelunit.framework.xml.result.XMLInfo)get_store().add_element_user(STATE$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "state" element
     */
    public void removeState(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(STATE$0, i);
        }
    }
    
    /**
     * Gets the "name" attribute
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$2);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "result" attribute
     */
    public java.lang.String getResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RESULT$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "result" attribute
     */
    public org.apache.xmlbeans.XmlString xgetResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(RESULT$4);
            return target;
        }
    }
    
    /**
     * Sets the "result" attribute
     */
    public void setResult(java.lang.String result)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RESULT$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(RESULT$4);
            }
            target.setStringValue(result);
        }
    }
    
    /**
     * Sets (as xml) the "result" attribute
     */
    public void xsetResult(org.apache.xmlbeans.XmlString result)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(RESULT$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(RESULT$4);
            }
            target.set(result);
        }
    }
    
    /**
     * Gets the "message" attribute
     */
    public java.lang.String getMessage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MESSAGE$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "message" attribute
     */
    public org.apache.xmlbeans.XmlString xgetMessage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(MESSAGE$6);
            return target;
        }
    }
    
    /**
     * Sets the "message" attribute
     */
    public void setMessage(java.lang.String message)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MESSAGE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MESSAGE$6);
            }
            target.setStringValue(message);
        }
    }
    
    /**
     * Sets (as xml) the "message" attribute
     */
    public void xsetMessage(org.apache.xmlbeans.XmlString message)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(MESSAGE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(MESSAGE$6);
            }
            target.set(message);
        }
    }
    
    /**
     * Gets the "exception" attribute
     */
    public java.lang.String getException()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXCEPTION$8);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "exception" attribute
     */
    public org.apache.xmlbeans.XmlString xgetException()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(EXCEPTION$8);
            return target;
        }
    }
    
    /**
     * True if has "exception" attribute
     */
    public boolean isSetException()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(EXCEPTION$8) != null;
        }
    }
    
    /**
     * Sets the "exception" attribute
     */
    public void setException(java.lang.String exception)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXCEPTION$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXCEPTION$8);
            }
            target.setStringValue(exception);
        }
    }
    
    /**
     * Sets (as xml) the "exception" attribute
     */
    public void xsetException(org.apache.xmlbeans.XmlString exception)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(EXCEPTION$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(EXCEPTION$8);
            }
            target.set(exception);
        }
    }
    
    /**
     * Unsets the "exception" attribute
     */
    public void unsetException()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(EXCEPTION$8);
        }
    }
}
