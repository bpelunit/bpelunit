/*
 * XML Type:  TestCase
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTestCase
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML TestCase(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLTestCaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLTestCase
{
    
    public XMLTestCaseImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROPERTY$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "property");
    private static final javax.xml.namespace.QName CLIENTTRACK$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "clientTrack");
    private static final javax.xml.namespace.QName PARTNERTRACK$4 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "partnerTrack");
    private static final javax.xml.namespace.QName NAME$6 = 
        new javax.xml.namespace.QName("", "name");
    private static final javax.xml.namespace.QName VARY$8 = 
        new javax.xml.namespace.QName("", "vary");
    private static final javax.xml.namespace.QName BASEDON$10 = 
        new javax.xml.namespace.QName("", "basedOn");
    private static final javax.xml.namespace.QName ABSTRACT$12 = 
        new javax.xml.namespace.QName("", "abstract");
    
    
    /**
     * Gets a List of "property" elements
     */
    public java.util.List<org.bpelunit.framework.xml.suite.XMLProperty> getPropertyList()
    {
        final class PropertyList extends java.util.AbstractList<org.bpelunit.framework.xml.suite.XMLProperty>
        {
            public org.bpelunit.framework.xml.suite.XMLProperty get(int i)
                { return XMLTestCaseImpl.this.getPropertyArray(i); }
            
            public org.bpelunit.framework.xml.suite.XMLProperty set(int i, org.bpelunit.framework.xml.suite.XMLProperty o)
            {
                org.bpelunit.framework.xml.suite.XMLProperty old = XMLTestCaseImpl.this.getPropertyArray(i);
                XMLTestCaseImpl.this.setPropertyArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.suite.XMLProperty o)
                { XMLTestCaseImpl.this.insertNewProperty(i).set(o); }
            
            public org.bpelunit.framework.xml.suite.XMLProperty remove(int i)
            {
                org.bpelunit.framework.xml.suite.XMLProperty old = XMLTestCaseImpl.this.getPropertyArray(i);
                XMLTestCaseImpl.this.removeProperty(i);
                return old;
            }
            
            public int size()
                { return XMLTestCaseImpl.this.sizeOfPropertyArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new PropertyList();
        }
    }
    
    /**
     * Gets array of all "property" elements
     */
    public org.bpelunit.framework.xml.suite.XMLProperty[] getPropertyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PROPERTY$0, targetList);
            org.bpelunit.framework.xml.suite.XMLProperty[] result = new org.bpelunit.framework.xml.suite.XMLProperty[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "property" element
     */
    public org.bpelunit.framework.xml.suite.XMLProperty getPropertyArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.suite.XMLProperty)get_store().find_element_user(PROPERTY$0, i);
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
    public void setPropertyArray(org.bpelunit.framework.xml.suite.XMLProperty[] propertyArray)
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
    public void setPropertyArray(int i, org.bpelunit.framework.xml.suite.XMLProperty property)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.suite.XMLProperty)get_store().find_element_user(PROPERTY$0, i);
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
    public org.bpelunit.framework.xml.suite.XMLProperty insertNewProperty(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.suite.XMLProperty)get_store().insert_element_user(PROPERTY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "property" element
     */
    public org.bpelunit.framework.xml.suite.XMLProperty addNewProperty()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLProperty target = null;
            target = (org.bpelunit.framework.xml.suite.XMLProperty)get_store().add_element_user(PROPERTY$0);
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
     * Gets the "clientTrack" element
     */
    public org.bpelunit.framework.xml.suite.XMLTrack getClientTrack()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTrack target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTrack)get_store().find_element_user(CLIENTTRACK$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "clientTrack" element
     */
    public void setClientTrack(org.bpelunit.framework.xml.suite.XMLTrack clientTrack)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTrack target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTrack)get_store().find_element_user(CLIENTTRACK$2, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.suite.XMLTrack)get_store().add_element_user(CLIENTTRACK$2);
            }
            target.set(clientTrack);
        }
    }
    
    /**
     * Appends and returns a new empty "clientTrack" element
     */
    public org.bpelunit.framework.xml.suite.XMLTrack addNewClientTrack()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTrack target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTrack)get_store().add_element_user(CLIENTTRACK$2);
            return target;
        }
    }
    
    /**
     * Gets a List of "partnerTrack" elements
     */
    public java.util.List<org.bpelunit.framework.xml.suite.XMLPartnerTrack> getPartnerTrackList()
    {
        final class PartnerTrackList extends java.util.AbstractList<org.bpelunit.framework.xml.suite.XMLPartnerTrack>
        {
            public org.bpelunit.framework.xml.suite.XMLPartnerTrack get(int i)
                { return XMLTestCaseImpl.this.getPartnerTrackArray(i); }
            
            public org.bpelunit.framework.xml.suite.XMLPartnerTrack set(int i, org.bpelunit.framework.xml.suite.XMLPartnerTrack o)
            {
                org.bpelunit.framework.xml.suite.XMLPartnerTrack old = XMLTestCaseImpl.this.getPartnerTrackArray(i);
                XMLTestCaseImpl.this.setPartnerTrackArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.suite.XMLPartnerTrack o)
                { XMLTestCaseImpl.this.insertNewPartnerTrack(i).set(o); }
            
            public org.bpelunit.framework.xml.suite.XMLPartnerTrack remove(int i)
            {
                org.bpelunit.framework.xml.suite.XMLPartnerTrack old = XMLTestCaseImpl.this.getPartnerTrackArray(i);
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
    public org.bpelunit.framework.xml.suite.XMLPartnerTrack[] getPartnerTrackArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PARTNERTRACK$4, targetList);
            org.bpelunit.framework.xml.suite.XMLPartnerTrack[] result = new org.bpelunit.framework.xml.suite.XMLPartnerTrack[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "partnerTrack" element
     */
    public org.bpelunit.framework.xml.suite.XMLPartnerTrack getPartnerTrackArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerTrack)get_store().find_element_user(PARTNERTRACK$4, i);
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
            return get_store().count_elements(PARTNERTRACK$4);
        }
    }
    
    /**
     * Sets array of all "partnerTrack" element
     */
    public void setPartnerTrackArray(org.bpelunit.framework.xml.suite.XMLPartnerTrack[] partnerTrackArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(partnerTrackArray, PARTNERTRACK$4);
        }
    }
    
    /**
     * Sets ith "partnerTrack" element
     */
    public void setPartnerTrackArray(int i, org.bpelunit.framework.xml.suite.XMLPartnerTrack partnerTrack)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerTrack)get_store().find_element_user(PARTNERTRACK$4, i);
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
    public org.bpelunit.framework.xml.suite.XMLPartnerTrack insertNewPartnerTrack(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerTrack)get_store().insert_element_user(PARTNERTRACK$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "partnerTrack" element
     */
    public org.bpelunit.framework.xml.suite.XMLPartnerTrack addNewPartnerTrack()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLPartnerTrack target = null;
            target = (org.bpelunit.framework.xml.suite.XMLPartnerTrack)get_store().add_element_user(PARTNERTRACK$4);
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
            get_store().remove_element(PARTNERTRACK$4, i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$6);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$6);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "vary" attribute
     */
    public boolean getVary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VARY$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(VARY$8);
            }
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "vary" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetVary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(VARY$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(VARY$8);
            }
            return target;
        }
    }
    
    /**
     * True if has "vary" attribute
     */
    public boolean isSetVary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(VARY$8) != null;
        }
    }
    
    /**
     * Sets the "vary" attribute
     */
    public void setVary(boolean vary)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VARY$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VARY$8);
            }
            target.setBooleanValue(vary);
        }
    }
    
    /**
     * Sets (as xml) the "vary" attribute
     */
    public void xsetVary(org.apache.xmlbeans.XmlBoolean vary)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(VARY$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(VARY$8);
            }
            target.set(vary);
        }
    }
    
    /**
     * Unsets the "vary" attribute
     */
    public void unsetVary()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(VARY$8);
        }
    }
    
    /**
     * Gets the "basedOn" attribute
     */
    public java.lang.String getBasedOn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BASEDON$10);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "basedOn" attribute
     */
    public org.apache.xmlbeans.XmlString xgetBasedOn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(BASEDON$10);
            return target;
        }
    }
    
    /**
     * True if has "basedOn" attribute
     */
    public boolean isSetBasedOn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(BASEDON$10) != null;
        }
    }
    
    /**
     * Sets the "basedOn" attribute
     */
    public void setBasedOn(java.lang.String basedOn)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BASEDON$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(BASEDON$10);
            }
            target.setStringValue(basedOn);
        }
    }
    
    /**
     * Sets (as xml) the "basedOn" attribute
     */
    public void xsetBasedOn(org.apache.xmlbeans.XmlString basedOn)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(BASEDON$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(BASEDON$10);
            }
            target.set(basedOn);
        }
    }
    
    /**
     * Unsets the "basedOn" attribute
     */
    public void unsetBasedOn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(BASEDON$10);
        }
    }
    
    /**
     * Gets the "abstract" attribute
     */
    public boolean getAbstract()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ABSTRACT$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(ABSTRACT$12);
            }
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "abstract" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetAbstract()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(ABSTRACT$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(ABSTRACT$12);
            }
            return target;
        }
    }
    
    /**
     * True if has "abstract" attribute
     */
    public boolean isSetAbstract()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ABSTRACT$12) != null;
        }
    }
    
    /**
     * Sets the "abstract" attribute
     */
    public void setAbstract(boolean xabstract)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ABSTRACT$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ABSTRACT$12);
            }
            target.setBooleanValue(xabstract);
        }
    }
    
    /**
     * Sets (as xml) the "abstract" attribute
     */
    public void xsetAbstract(org.apache.xmlbeans.XmlBoolean xabstract)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(ABSTRACT$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(ABSTRACT$12);
            }
            target.set(xabstract);
        }
    }
    
    /**
     * Unsets the "abstract" attribute
     */
    public void unsetAbstract()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ABSTRACT$12);
        }
    }
}
