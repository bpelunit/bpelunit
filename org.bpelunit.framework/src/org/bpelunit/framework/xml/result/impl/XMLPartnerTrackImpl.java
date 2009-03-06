/*
 * XML Type:  PartnerTrack
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLPartnerTrack
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * An XML PartnerTrack(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public class XMLPartnerTrackImpl extends org.bpelunit.framework.xml.result.impl.XMLArtefactImpl implements org.bpelunit.framework.xml.result.XMLPartnerTrack
{
    private static final long serialVersionUID = 1L;
    
    public XMLPartnerTrackImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ACTIVITY$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "activity");
    
    
    /**
     * Gets a List of "activity" elements
     */
    public java.util.List<org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity> getActivityList()
    {
        final class ActivityList extends java.util.AbstractList<org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity>
        {
            public org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity get(int i)
                { return XMLPartnerTrackImpl.this.getActivityArray(i); }
            
            public org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity set(int i, org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity o)
            {
                org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity old = XMLPartnerTrackImpl.this.getActivityArray(i);
                XMLPartnerTrackImpl.this.setActivityArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity o)
                { XMLPartnerTrackImpl.this.insertNewActivity(i).set(o); }
            
            public org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity remove(int i)
            {
                org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity old = XMLPartnerTrackImpl.this.getActivityArray(i);
                XMLPartnerTrackImpl.this.removeActivity(i);
                return old;
            }
            
            public int size()
                { return XMLPartnerTrackImpl.this.sizeOfActivityArray(); }
            
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
    public org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity[] getActivityArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity>();
            get_store().find_all_element_users(ACTIVITY$0, targetList);
            org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity[] result = new org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "activity" element
     */
    public org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity getActivityArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity)get_store().find_element_user(ACTIVITY$0, i);
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
    public void setActivityArray(org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity[] activityArray)
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
    public void setActivityArray(int i, org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity activity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity)get_store().find_element_user(ACTIVITY$0, i);
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
    public org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity insertNewActivity(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity)get_store().insert_element_user(ACTIVITY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "activity" element
     */
    public org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity addNewActivity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity)get_store().add_element_user(ACTIVITY$0);
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
     * An XML activity(@http://www.bpelunit.org/schema/testResult).
     *
     * This is a complex type.
     */
    public static class ActivityImpl extends org.bpelunit.framework.xml.result.impl.XMLActivityImpl implements org.bpelunit.framework.xml.result.XMLPartnerTrack.Activity
    {
        private static final long serialVersionUID = 1L;
        
        public ActivityImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        
    }
}
