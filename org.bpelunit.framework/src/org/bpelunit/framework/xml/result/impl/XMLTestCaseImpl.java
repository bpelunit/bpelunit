/*
 * XML Type:  TestCase
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLTestCase
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * An XML TestCase(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public class XMLTestCaseImpl extends org.bpelunit.framework.xml.result.impl.XMLArtefactImpl implements org.bpelunit.framework.xml.result.XMLTestCase
{
    
    public XMLTestCaseImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PARTNERTRACK$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "partnerTrack");
    
    
    /**
     * Gets a List of "partnerTrack" elements
     */
    public java.util.List<org.bpelunit.framework.xml.result.XMLPartnerTrack> getPartnerTrackList()
    {
        final class PartnerTrackList extends java.util.AbstractList<org.bpelunit.framework.xml.result.XMLPartnerTrack>
        {
            public org.bpelunit.framework.xml.result.XMLPartnerTrack get(int i)
                { return XMLTestCaseImpl.this.getPartnerTrackArray(i); }
            
            public org.bpelunit.framework.xml.result.XMLPartnerTrack set(int i, org.bpelunit.framework.xml.result.XMLPartnerTrack o)
            {
                org.bpelunit.framework.xml.result.XMLPartnerTrack old = XMLTestCaseImpl.this.getPartnerTrackArray(i);
                XMLTestCaseImpl.this.setPartnerTrackArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.result.XMLPartnerTrack o)
                { XMLTestCaseImpl.this.insertNewPartnerTrack(i).set(o); }
            
            public org.bpelunit.framework.xml.result.XMLPartnerTrack remove(int i)
            {
                org.bpelunit.framework.xml.result.XMLPartnerTrack old = XMLTestCaseImpl.this.getPartnerTrackArray(i);
                XMLTestCaseImpl.this.removePartnerTrack(i);
                return old;
            }
            
            public int size()
                { return XMLTestCaseImpl.this.sizeOfPartnerTrackArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new PartnerTrackList();
        }
    }
    
    /**
     * Gets array of all "partnerTrack" elements
     */
    public org.bpelunit.framework.xml.result.XMLPartnerTrack[] getPartnerTrackArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PARTNERTRACK$0, targetList);
            org.bpelunit.framework.xml.result.XMLPartnerTrack[] result = new org.bpelunit.framework.xml.result.XMLPartnerTrack[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "partnerTrack" element
     */
    public org.bpelunit.framework.xml.result.XMLPartnerTrack getPartnerTrackArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack)get_store().find_element_user(PARTNERTRACK$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "partnerTrack" element
     */
    public int sizeOfPartnerTrackArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PARTNERTRACK$0);
        }
    }
    
    /**
     * Sets array of all "partnerTrack" element
     */
    public void setPartnerTrackArray(org.bpelunit.framework.xml.result.XMLPartnerTrack[] partnerTrackArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(partnerTrackArray, PARTNERTRACK$0);
        }
    }
    
    /**
     * Sets ith "partnerTrack" element
     */
    public void setPartnerTrackArray(int i, org.bpelunit.framework.xml.result.XMLPartnerTrack partnerTrack)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack)get_store().find_element_user(PARTNERTRACK$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(partnerTrack);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "partnerTrack" element
     */
    public org.bpelunit.framework.xml.result.XMLPartnerTrack insertNewPartnerTrack(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack)get_store().insert_element_user(PARTNERTRACK$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "partnerTrack" element
     */
    public org.bpelunit.framework.xml.result.XMLPartnerTrack addNewPartnerTrack()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.result.XMLPartnerTrack)get_store().add_element_user(PARTNERTRACK$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "partnerTrack" element
     */
    public void removePartnerTrack(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PARTNERTRACK$0, i);
        }
    }
}
