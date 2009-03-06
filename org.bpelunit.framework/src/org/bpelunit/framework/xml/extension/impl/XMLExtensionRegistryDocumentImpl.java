/*
 * An XML document type.
 * Localname: extensionRegistry
 * Namespace: http://www.bpelunit.org/schema/testExtension
 * Java type: org.bpelunit.framework.xml.extension.XMLExtensionRegistryDocument
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.extension.impl;
/**
 * A document containing one extensionRegistry(@http://www.bpelunit.org/schema/testExtension) element.
 *
 * This is a complex type.
 */
public class XMLExtensionRegistryDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.extension.XMLExtensionRegistryDocument
{
    private static final long serialVersionUID = 1L;
    
    public XMLExtensionRegistryDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXTENSIONREGISTRY$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testExtension", "extensionRegistry");
    
    
    /**
     * Gets the "extensionRegistry" element
     */
    public org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions getExtensionRegistry()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions target = null;
            target = (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions)get_store().find_element_user(EXTENSIONREGISTRY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "extensionRegistry" element
     */
    public void setExtensionRegistry(org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions extensionRegistry)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions target = null;
            target = (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions)get_store().find_element_user(EXTENSIONREGISTRY$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions)get_store().add_element_user(EXTENSIONREGISTRY$0);
            }
            target.set(extensionRegistry);
        }
    }
    
    /**
     * Appends and returns a new empty "extensionRegistry" element
     */
    public org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions addNewExtensionRegistry()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions target = null;
            target = (org.bpelunit.framework.xml.extension.XMLBPELUnitCoreExtensions)get_store().add_element_user(EXTENSIONREGISTRY$0);
            return target;
        }
    }
}
