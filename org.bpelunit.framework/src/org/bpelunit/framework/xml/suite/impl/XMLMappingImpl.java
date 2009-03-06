/*
 * XML Type:  Mapping
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLMapping
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML Mapping(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLMappingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLMapping
{
    private static final long serialVersionUID = 1L;
    
    public XMLMappingImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COPY$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "copy");
    
    
    /**
     * Gets a List of "copy" elements
     */
    public java.util.List<org.bpelunit.framework.xml.suite.XMLCopy> getCopyList()
    {
        final class CopyList extends java.util.AbstractList<org.bpelunit.framework.xml.suite.XMLCopy>
        {
            public org.bpelunit.framework.xml.suite.XMLCopy get(int i)
                { return XMLMappingImpl.this.getCopyArray(i); }
            
            public org.bpelunit.framework.xml.suite.XMLCopy set(int i, org.bpelunit.framework.xml.suite.XMLCopy o)
            {
                org.bpelunit.framework.xml.suite.XMLCopy old = XMLMappingImpl.this.getCopyArray(i);
                XMLMappingImpl.this.setCopyArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.suite.XMLCopy o)
                { XMLMappingImpl.this.insertNewCopy(i).set(o); }
            
            public org.bpelunit.framework.xml.suite.XMLCopy remove(int i)
            {
                org.bpelunit.framework.xml.suite.XMLCopy old = XMLMappingImpl.this.getCopyArray(i);
                XMLMappingImpl.this.removeCopy(i);
                return old;
            }
            
            public int size()
                { return XMLMappingImpl.this.sizeOfCopyArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new CopyList();
        }
    }
    
    /**
     * Gets array of all "copy" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.suite.XMLCopy[] getCopyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.suite.XMLCopy> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.suite.XMLCopy>();
            get_store().find_all_element_users(COPY$0, targetList);
            org.bpelunit.framework.xml.suite.XMLCopy[] result = new org.bpelunit.framework.xml.suite.XMLCopy[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "copy" element
     */
    public org.bpelunit.framework.xml.suite.XMLCopy getCopyArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCopy target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCopy)get_store().find_element_user(COPY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "copy" element
     */
    public int sizeOfCopyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COPY$0);
        }
    }
    
    /**
     * Sets array of all "copy" element
     */
    public void setCopyArray(org.bpelunit.framework.xml.suite.XMLCopy[] copyArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(copyArray, COPY$0);
        }
    }
    
    /**
     * Sets ith "copy" element
     */
    public void setCopyArray(int i, org.bpelunit.framework.xml.suite.XMLCopy copy)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCopy target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCopy)get_store().find_element_user(COPY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(copy);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "copy" element
     */
    public org.bpelunit.framework.xml.suite.XMLCopy insertNewCopy(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCopy target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCopy)get_store().insert_element_user(COPY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "copy" element
     */
    public org.bpelunit.framework.xml.suite.XMLCopy addNewCopy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLCopy target = null;
            target = (org.bpelunit.framework.xml.suite.XMLCopy)get_store().add_element_user(COPY$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "copy" element
     */
    public void removeCopy(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COPY$0, i);
        }
    }
}
