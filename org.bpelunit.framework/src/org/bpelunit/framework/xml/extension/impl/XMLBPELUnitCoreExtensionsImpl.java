/*
 * XML Type:  BPELUnitCoreExtensions
 * Namespace: http://www.bpelunit.org/schema/testExtension
 * Java type: org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.extension.impl;
/**
 * An XML BPELUnitCoreExtensions(@http://www.bpelunit.org/schema/testExtension).
 *
 * This is a complex type.
 */
public class XMLBPELUnitCoreExtensionsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions
{
    private static final long serialVersionUID = 1L;
    
    public XMLBPELUnitCoreExtensionsImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DEPLOYER$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testExtension", "deployer");
    private static final javax.xml.namespace.QName ENCODER$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testExtension", "encoder");
    private static final javax.xml.namespace.QName HEADERPROCESSOR$4 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testExtension", "headerProcessor");
    
    
    /**
     * Gets a List of "deployer" elements
     */
    public java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> getDeployerList()
    {
        final class DeployerList extends java.util.AbstractList<org.bpelunit.framework.xml.extension.XMLExtension>
        {
            public org.bpelunit.framework.xml.extension.XMLExtension get(int i)
                { return XMLBPELUnitCoreExtensionsImpl.this.getDeployerArray(i); }
            
            public org.bpelunit.framework.xml.extension.XMLExtension set(int i, org.bpelunit.framework.xml.extension.XMLExtension o)
            {
                org.bpelunit.framework.xml.extension.XMLExtension old = XMLBPELUnitCoreExtensionsImpl.this.getDeployerArray(i);
                XMLBPELUnitCoreExtensionsImpl.this.setDeployerArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.extension.XMLExtension o)
                { XMLBPELUnitCoreExtensionsImpl.this.insertNewDeployer(i).set(o); }
            
            public org.bpelunit.framework.xml.extension.XMLExtension remove(int i)
            {
                org.bpelunit.framework.xml.extension.XMLExtension old = XMLBPELUnitCoreExtensionsImpl.this.getDeployerArray(i);
                XMLBPELUnitCoreExtensionsImpl.this.removeDeployer(i);
                return old;
            }
            
            public int size()
                { return XMLBPELUnitCoreExtensionsImpl.this.sizeOfDeployerArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new DeployerList();
        }
    }
    
    /**
     * Gets array of all "deployer" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.extension.XMLExtension[] getDeployerArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.extension.XMLExtension>();
            get_store().find_all_element_users(DEPLOYER$0, targetList);
            org.bpelunit.framework.xml.extension.XMLExtension[] result = new org.bpelunit.framework.xml.extension.XMLExtension[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "deployer" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension getDeployerArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().find_element_user(DEPLOYER$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "deployer" element
     */
    public int sizeOfDeployerArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DEPLOYER$0);
        }
    }
    
    /**
     * Sets array of all "deployer" element
     */
    public void setDeployerArray(org.bpelunit.framework.xml.extension.XMLExtension[] deployerArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(deployerArray, DEPLOYER$0);
        }
    }
    
    /**
     * Sets ith "deployer" element
     */
    public void setDeployerArray(int i, org.bpelunit.framework.xml.extension.XMLExtension deployer)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().find_element_user(DEPLOYER$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(deployer);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "deployer" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension insertNewDeployer(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().insert_element_user(DEPLOYER$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "deployer" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension addNewDeployer()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().add_element_user(DEPLOYER$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "deployer" element
     */
    public void removeDeployer(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DEPLOYER$0, i);
        }
    }
    
    /**
     * Gets a List of "encoder" elements
     */
    public java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> getEncoderList()
    {
        final class EncoderList extends java.util.AbstractList<org.bpelunit.framework.xml.extension.XMLExtension>
        {
            public org.bpelunit.framework.xml.extension.XMLExtension get(int i)
                { return XMLBPELUnitCoreExtensionsImpl.this.getEncoderArray(i); }
            
            public org.bpelunit.framework.xml.extension.XMLExtension set(int i, org.bpelunit.framework.xml.extension.XMLExtension o)
            {
                org.bpelunit.framework.xml.extension.XMLExtension old = XMLBPELUnitCoreExtensionsImpl.this.getEncoderArray(i);
                XMLBPELUnitCoreExtensionsImpl.this.setEncoderArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.extension.XMLExtension o)
                { XMLBPELUnitCoreExtensionsImpl.this.insertNewEncoder(i).set(o); }
            
            public org.bpelunit.framework.xml.extension.XMLExtension remove(int i)
            {
                org.bpelunit.framework.xml.extension.XMLExtension old = XMLBPELUnitCoreExtensionsImpl.this.getEncoderArray(i);
                XMLBPELUnitCoreExtensionsImpl.this.removeEncoder(i);
                return old;
            }
            
            public int size()
                { return XMLBPELUnitCoreExtensionsImpl.this.sizeOfEncoderArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new EncoderList();
        }
    }
    
    /**
     * Gets array of all "encoder" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.extension.XMLExtension[] getEncoderArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.extension.XMLExtension>();
            get_store().find_all_element_users(ENCODER$2, targetList);
            org.bpelunit.framework.xml.extension.XMLExtension[] result = new org.bpelunit.framework.xml.extension.XMLExtension[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "encoder" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension getEncoderArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().find_element_user(ENCODER$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "encoder" element
     */
    public int sizeOfEncoderArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ENCODER$2);
        }
    }
    
    /**
     * Sets array of all "encoder" element
     */
    public void setEncoderArray(org.bpelunit.framework.xml.extension.XMLExtension[] encoderArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(encoderArray, ENCODER$2);
        }
    }
    
    /**
     * Sets ith "encoder" element
     */
    public void setEncoderArray(int i, org.bpelunit.framework.xml.extension.XMLExtension encoder)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().find_element_user(ENCODER$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(encoder);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "encoder" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension insertNewEncoder(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().insert_element_user(ENCODER$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "encoder" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension addNewEncoder()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().add_element_user(ENCODER$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "encoder" element
     */
    public void removeEncoder(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ENCODER$2, i);
        }
    }
    
    /**
     * Gets a List of "headerProcessor" elements
     */
    public java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> getHeaderProcessorList()
    {
        final class HeaderProcessorList extends java.util.AbstractList<org.bpelunit.framework.xml.extension.XMLExtension>
        {
            public org.bpelunit.framework.xml.extension.XMLExtension get(int i)
                { return XMLBPELUnitCoreExtensionsImpl.this.getHeaderProcessorArray(i); }
            
            public org.bpelunit.framework.xml.extension.XMLExtension set(int i, org.bpelunit.framework.xml.extension.XMLExtension o)
            {
                org.bpelunit.framework.xml.extension.XMLExtension old = XMLBPELUnitCoreExtensionsImpl.this.getHeaderProcessorArray(i);
                XMLBPELUnitCoreExtensionsImpl.this.setHeaderProcessorArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.extension.XMLExtension o)
                { XMLBPELUnitCoreExtensionsImpl.this.insertNewHeaderProcessor(i).set(o); }
            
            public org.bpelunit.framework.xml.extension.XMLExtension remove(int i)
            {
                org.bpelunit.framework.xml.extension.XMLExtension old = XMLBPELUnitCoreExtensionsImpl.this.getHeaderProcessorArray(i);
                XMLBPELUnitCoreExtensionsImpl.this.removeHeaderProcessor(i);
                return old;
            }
            
            public int size()
                { return XMLBPELUnitCoreExtensionsImpl.this.sizeOfHeaderProcessorArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new HeaderProcessorList();
        }
    }
    
    /**
     * Gets array of all "headerProcessor" elements
     * @deprecated
     */
    public org.bpelunit.framework.xml.extension.XMLExtension[] getHeaderProcessorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List<org.bpelunit.framework.xml.extension.XMLExtension> targetList = new java.util.ArrayList<org.bpelunit.framework.xml.extension.XMLExtension>();
            get_store().find_all_element_users(HEADERPROCESSOR$4, targetList);
            org.bpelunit.framework.xml.extension.XMLExtension[] result = new org.bpelunit.framework.xml.extension.XMLExtension[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "headerProcessor" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension getHeaderProcessorArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().find_element_user(HEADERPROCESSOR$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "headerProcessor" element
     */
    public int sizeOfHeaderProcessorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(HEADERPROCESSOR$4);
        }
    }
    
    /**
     * Sets array of all "headerProcessor" element
     */
    public void setHeaderProcessorArray(org.bpelunit.framework.xml.extension.XMLExtension[] headerProcessorArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(headerProcessorArray, HEADERPROCESSOR$4);
        }
    }
    
    /**
     * Sets ith "headerProcessor" element
     */
    public void setHeaderProcessorArray(int i, org.bpelunit.framework.xml.extension.XMLExtension headerProcessor)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().find_element_user(HEADERPROCESSOR$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(headerProcessor);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "headerProcessor" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension insertNewHeaderProcessor(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().insert_element_user(HEADERPROCESSOR$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "headerProcessor" element
     */
    public org.bpelunit.framework.xml.extension.XMLExtension addNewHeaderProcessor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLExtension target = null;
            target = (org.bpelunit.framework.xml.extension.XMLExtension)get_store().add_element_user(HEADERPROCESSOR$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "headerProcessor" element
     */
    public void removeHeaderProcessor(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(HEADERPROCESSOR$4, i);
        }
    }
}
