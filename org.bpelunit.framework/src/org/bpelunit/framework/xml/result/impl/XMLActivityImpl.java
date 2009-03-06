/*
 * XML Type:  Activity
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLActivity
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * An XML Activity(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public class XMLActivityImpl extends org.bpelunit.framework.xml.result.impl.XMLArtefactImpl implements org.bpelunit.framework.xml.result.XMLActivity
{
    private static final long serialVersionUID = 1L;
    
    public XMLActivityImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ACTIVITY$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "activity");
    private static final javax.xml.namespace.QName DATAPACKAGE$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "dataPackage");
    private static final javax.xml.namespace.QName COPYOPERATION$4 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "copyOperation");
    private static final javax.xml.namespace.QName TYPE$6 = 
        new javax.xml.namespace.QName("", "type");
    
    
    /**
     * Gets a List of "activity" elements
     */
    public java.util.List<org.bpelunit.framework.xml.result.XMLActivity> getActivityList()
    {
        final class ActivityList extends java.util.AbstractList<org.bpelunit.framework.xml.result.XMLActivity>
        {
            public org.bpelunit.framework.xml.result.XMLActivity get(int i)
                { return XMLActivityImpl.this.getActivityArray(i); }
            
            public org.bpelunit.framework.xml.result.XMLActivity set(int i, org.bpelunit.framework.xml.result.XMLActivity o)
            {
                org.bpelunit.framework.xml.result.XMLActivity old = XMLActivityImpl.this.getActivityArray(i);
                XMLActivityImpl.this.setActivityArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.result.XMLActivity o)
                { XMLActivityImpl.this.insertNewActivity(i).set(o); }
            
            public org.bpelunit.framework.xml.result.XMLActivity remove(int i)
            {
                org.bpelunit.framework.xml.result.XMLActivity old = XMLActivityImpl.this.getActivityArray(i);
                XMLActivityImpl.this.removeActivity(i);
                return old;
            }
            
            public int size()
                { return XMLActivityImpl.this.sizeOfActivityArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new ActivityList();
        }
    }
    
    /**
     * Gets array of all "activity" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.result.XMLActivity[] getActivityArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.result.XMLActivity> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.result.XMLActivity>();
            get_store().find_all_element_users(ACTIVITY$0, targetList);
            org.bpelunit.framework.xml.result.XMLActivity[] result = new org.bpelunit.framework.xml.result.XMLActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "activity" element
     */
    public org.bpelunit.framework.xml.result.XMLActivity getActivityArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLActivity target = null;
            target = (org.bpelunit.framework.xml.result.XMLActivity)get_store().find_element_user(ACTIVITY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "activity" element
     */
    public int sizeOfActivityArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ACTIVITY$0);
        }
    }
    
    /**
     * Sets array of all "activity" element
     */
    public void setActivityArray(org.bpelunit.framework.xml.result.XMLActivity[] activityArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(activityArray, ACTIVITY$0);
        }
    }
    
    /**
     * Sets ith "activity" element
     */
    public void setActivityArray(int i, org.bpelunit.framework.xml.result.XMLActivity activity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLActivity target = null;
            target = (org.bpelunit.framework.xml.result.XMLActivity)get_store().find_element_user(ACTIVITY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(activity);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "activity" element
     */
    public org.bpelunit.framework.xml.result.XMLActivity insertNewActivity(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLActivity target = null;
            target = (org.bpelunit.framework.xml.result.XMLActivity)get_store().insert_element_user(ACTIVITY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "activity" element
     */
    public org.bpelunit.framework.xml.result.XMLActivity addNewActivity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLActivity target = null;
            target = (org.bpelunit.framework.xml.result.XMLActivity)get_store().add_element_user(ACTIVITY$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "activity" element
     */
    public void removeActivity(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ACTIVITY$0, i);
        }
    }
    
    /**
     * Gets a List of "dataPackage" elements
     */
    public java.util.List<org.bpelunit.framework.xml.result.XMLData> getDataPackageList()
    {
        final class DataPackageList extends java.util.AbstractList<org.bpelunit.framework.xml.result.XMLData>
        {
            public org.bpelunit.framework.xml.result.XMLData get(int i)
                { return XMLActivityImpl.this.getDataPackageArray(i); }
            
            public org.bpelunit.framework.xml.result.XMLData set(int i, org.bpelunit.framework.xml.result.XMLData o)
            {
                org.bpelunit.framework.xml.result.XMLData old = XMLActivityImpl.this.getDataPackageArray(i);
                XMLActivityImpl.this.setDataPackageArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.result.XMLData o)
                { XMLActivityImpl.this.insertNewDataPackage(i).set(o); }
            
            public org.bpelunit.framework.xml.result.XMLData remove(int i)
            {
                org.bpelunit.framework.xml.result.XMLData old = XMLActivityImpl.this.getDataPackageArray(i);
                XMLActivityImpl.this.removeDataPackage(i);
                return old;
            }
            
            public int size()
                { return XMLActivityImpl.this.sizeOfDataPackageArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new DataPackageList();
        }
    }
    
    /**
     * Gets array of all "dataPackage" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.result.XMLData[] getDataPackageArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.result.XMLData> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.result.XMLData>();
            get_store().find_all_element_users(DATAPACKAGE$2, targetList);
            org.bpelunit.framework.xml.result.XMLData[] result = new org.bpelunit.framework.xml.result.XMLData[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "dataPackage" element
     */
    public org.bpelunit.framework.xml.result.XMLData getDataPackageArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData)get_store().find_element_user(DATAPACKAGE$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "dataPackage" element
     */
    public int sizeOfDataPackageArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DATAPACKAGE$2);
        }
    }
    
    /**
     * Sets array of all "dataPackage" element
     */
    public void setDataPackageArray(org.bpelunit.framework.xml.result.XMLData[] dataPackageArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(dataPackageArray, DATAPACKAGE$2);
        }
    }
    
    /**
     * Sets ith "dataPackage" element
     */
    public void setDataPackageArray(int i, org.bpelunit.framework.xml.result.XMLData dataPackage)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData)get_store().find_element_user(DATAPACKAGE$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(dataPackage);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "dataPackage" element
     */
    public org.bpelunit.framework.xml.result.XMLData insertNewDataPackage(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData)get_store().insert_element_user(DATAPACKAGE$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "dataPackage" element
     */
    public org.bpelunit.framework.xml.result.XMLData addNewDataPackage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLData target = null;
            target = (org.bpelunit.framework.xml.result.XMLData)get_store().add_element_user(DATAPACKAGE$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "dataPackage" element
     */
    public void removeDataPackage(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DATAPACKAGE$2, i);
        }
    }
    
    /**
     * Gets a List of "copyOperation" elements
     */
    public java.util.List<org.bpelunit.framework.xml.result.XMLCopyOperation> getCopyOperationList()
    {
        final class CopyOperationList extends java.util.AbstractList<org.bpelunit.framework.xml.result.XMLCopyOperation>
        {
            public org.bpelunit.framework.xml.result.XMLCopyOperation get(int i)
                { return XMLActivityImpl.this.getCopyOperationArray(i); }
            
            public org.bpelunit.framework.xml.result.XMLCopyOperation set(int i, org.bpelunit.framework.xml.result.XMLCopyOperation o)
            {
                org.bpelunit.framework.xml.result.XMLCopyOperation old = XMLActivityImpl.this.getCopyOperationArray(i);
                XMLActivityImpl.this.setCopyOperationArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.result.XMLCopyOperation o)
                { XMLActivityImpl.this.insertNewCopyOperation(i).set(o); }
            
            public org.bpelunit.framework.xml.result.XMLCopyOperation remove(int i)
            {
                org.bpelunit.framework.xml.result.XMLCopyOperation old = XMLActivityImpl.this.getCopyOperationArray(i);
                XMLActivityImpl.this.removeCopyOperation(i);
                return old;
            }
            
            public int size()
                { return XMLActivityImpl.this.sizeOfCopyOperationArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new CopyOperationList();
        }
    }
    
    /**
     * Gets array of all "copyOperation" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.result.XMLCopyOperation[] getCopyOperationArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.result.XMLCopyOperation> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.result.XMLCopyOperation>();
            get_store().find_all_element_users(COPYOPERATION$4, targetList);
            org.bpelunit.framework.xml.result.XMLCopyOperation[] result = new org.bpelunit.framework.xml.result.XMLCopyOperation[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "copyOperation" element
     */
    public org.bpelunit.framework.xml.result.XMLCopyOperation getCopyOperationArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLCopyOperation target = null;
            target = (org.bpelunit.framework.xml.result.XMLCopyOperation)get_store().find_element_user(COPYOPERATION$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "copyOperation" element
     */
    public int sizeOfCopyOperationArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COPYOPERATION$4);
        }
    }
    
    /**
     * Sets array of all "copyOperation" element
     */
    public void setCopyOperationArray(org.bpelunit.framework.xml.result.XMLCopyOperation[] copyOperationArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(copyOperationArray, COPYOPERATION$4);
        }
    }
    
    /**
     * Sets ith "copyOperation" element
     */
    public void setCopyOperationArray(int i, org.bpelunit.framework.xml.result.XMLCopyOperation copyOperation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLCopyOperation target = null;
            target = (org.bpelunit.framework.xml.result.XMLCopyOperation)get_store().find_element_user(COPYOPERATION$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(copyOperation);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "copyOperation" element
     */
    public org.bpelunit.framework.xml.result.XMLCopyOperation insertNewCopyOperation(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLCopyOperation target = null;
            target = (org.bpelunit.framework.xml.result.XMLCopyOperation)get_store().insert_element_user(COPYOPERATION$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "copyOperation" element
     */
    public org.bpelunit.framework.xml.result.XMLCopyOperation addNewCopyOperation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLCopyOperation target = null;
            target = (org.bpelunit.framework.xml.result.XMLCopyOperation)get_store().add_element_user(COPYOPERATION$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "copyOperation" element
     */
    public void removeCopyOperation(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COPYOPERATION$4, i);
        }
    }
    
    /**
     * Gets the "type" attribute
     */
    public java.lang.String getType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "type" attribute
     */
    public org.apache.xmlbeans.XmlString xgetType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TYPE$6);
            return target;
        }
    }
    
    /**
     * Sets the "type" attribute
     */
    public void setType(java.lang.String type)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TYPE$6);
            }
            target.setStringValue(type);
        }
    }
    
    /**
     * Sets (as xml) the "type" attribute
     */
    public void xsetType(org.apache.xmlbeans.XmlString type)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TYPE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TYPE$6);
            }
            target.set(type);
        }
    }
}
