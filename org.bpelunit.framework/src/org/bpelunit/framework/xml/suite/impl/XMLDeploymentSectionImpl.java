/*
 * XML Type:  DeploymentSection
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLDeploymentSection
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML DeploymentSection(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLDeploymentSectionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLDeploymentSection
{
    
    public XMLDeploymentSectionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PUT$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "put");
    private static final javax.xml.namespace.QName PARTNER$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "partner");
    
    
    /**
     * Gets the "put" element
     */
    public org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation getPut()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation)get_store().find_element_user(PUT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "put" element
     */
    public void setPut(org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation put)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation)get_store().find_element_user(PUT$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation)get_store().add_element_user(PUT$0);
            }
            target.set(put);
        }
    }
    
    /**
     * Appends and returns a new empty "put" element
     */
    public org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation addNewPut()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation)get_store().add_element_user(PUT$0);
            return target;
        }
    }
    
    /**
     * Gets a List of "partner" elements
     */
    public java.util.List<org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation> getPartnerList()
    {
        final class PartnerList extends java.util.AbstractList<org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation>
        {
            public org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation get(int i)
                { return XMLDeploymentSectionImpl.this.getPartnerArray(i); }
            
            public org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation set(int i, org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation o)
            {
                org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation old = XMLDeploymentSectionImpl.this.getPartnerArray(i);
                XMLDeploymentSectionImpl.this.setPartnerArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation o)
                { XMLDeploymentSectionImpl.this.insertNewPartner(i).set(o); }
            
            public org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation remove(int i)
            {
                org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation old = XMLDeploymentSectionImpl.this.getPartnerArray(i);
                XMLDeploymentSectionImpl.this.removePartner(i);
                return old;
            }
            
            public int size()
                { return XMLDeploymentSectionImpl.this.sizeOfPartnerArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new PartnerList();
        }
    }
    
    /**
     * Gets array of all "partner" elements
     */
    public org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation[] getPartnerArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PARTNER$2, targetList);
            org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation[] result = new org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "partner" element
     */
    public org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation getPartnerArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation)get_store().find_element_user(PARTNER$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "partner" element
     */
    public int sizeOfPartnerArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PARTNER$2);
        }
    }
    
    /**
     * Sets array of all "partner" element
     */
    public void setPartnerArray(org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation[] partnerArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(partnerArray, PARTNER$2);
        }
    }
    
    /**
     * Sets ith "partner" element
     */
    public void setPartnerArray(int i, org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation partner)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation)get_store().find_element_user(PARTNER$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(partner);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "partner" element
     */
    public org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation insertNewPartner(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation)get_store().insert_element_user(PARTNER$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "partner" element
     */
    public org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation addNewPartner()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation)get_store().add_element_user(PARTNER$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "partner" element
     */
    public void removePartner(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PARTNER$2, i);
        }
    }
}
