/*
 * XML Type:  ReceiveCondition
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLReceiveCondition
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * An XML ReceiveCondition(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public class XMLReceiveConditionImpl extends org.bpelunit.framework.xml.result.impl.XMLArtefactImpl implements org.bpelunit.framework.xml.result.XMLReceiveCondition
{
    private static final long serialVersionUID = 1L;
    
    public XMLReceiveConditionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONDITION$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "condition");
    
    
    /**
     * Gets the "condition" element
     */
    public org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition getCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition target = null;
            target = (org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition)get_store().find_element_user(CONDITION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "condition" element
     */
    public void setCondition(org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition target = null;
            target = (org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition)get_store().find_element_user(CONDITION$0, 0);
            if (target == null)
            {
                target = (org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition)get_store().add_element_user(CONDITION$0);
            }
            target.set(condition);
        }
    }
    
    /**
     * Appends and returns a new empty "condition" element
     */
    public org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition addNewCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition target = null;
            target = (org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition)get_store().add_element_user(CONDITION$0);
            return target;
        }
    }
    /**
     * An XML condition(@http://www.bpelunit.org/schema/testResult).
     *
     * This is a complex type.
     */
    public static class ConditionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.result.XMLReceiveCondition.Condition
    {
        private static final long serialVersionUID = 1L;
        
        public ConditionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName EXPRESSION$0 = 
            new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "expression");
        private static final javax.xml.namespace.QName EXPECTEDVALUE$2 = 
            new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "expectedValue");
        private static final javax.xml.namespace.QName ACTUALVALUE$4 = 
            new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "actualValue");
        
        
        /**
         * Gets the "expression" element
         */
        public java.lang.String getExpression()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXPRESSION$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "expression" element
         */
        public org.apache.xmlbeans.XmlString xgetExpression()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EXPRESSION$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "expression" element
         */
        public void setExpression(java.lang.String expression)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXPRESSION$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EXPRESSION$0);
                }
                target.setStringValue(expression);
            }
        }
        
        /**
         * Sets (as xml) the "expression" element
         */
        public void xsetExpression(org.apache.xmlbeans.XmlString expression)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EXPRESSION$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EXPRESSION$0);
                }
                target.set(expression);
            }
        }
        
        /**
         * Gets the "expectedValue" element
         */
        public java.lang.String getExpectedValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXPECTEDVALUE$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "expectedValue" element
         */
        public org.apache.xmlbeans.XmlString xgetExpectedValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EXPECTEDVALUE$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "expectedValue" element
         */
        public void setExpectedValue(java.lang.String expectedValue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXPECTEDVALUE$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EXPECTEDVALUE$2);
                }
                target.setStringValue(expectedValue);
            }
        }
        
        /**
         * Sets (as xml) the "expectedValue" element
         */
        public void xsetExpectedValue(org.apache.xmlbeans.XmlString expectedValue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EXPECTEDVALUE$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EXPECTEDVALUE$2);
                }
                target.set(expectedValue);
            }
        }
        
        /**
         * Gets the "actualValue" element
         */
        public java.lang.String getActualValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACTUALVALUE$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "actualValue" element
         */
        public org.apache.xmlbeans.XmlString xgetActualValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ACTUALVALUE$4, 0);
                return target;
            }
        }
        
        /**
         * Sets the "actualValue" element
         */
        public void setActualValue(java.lang.String actualValue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACTUALVALUE$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ACTUALVALUE$4);
                }
                target.setStringValue(actualValue);
            }
        }
        
        /**
         * Sets (as xml) the "actualValue" element
         */
        public void xsetActualValue(org.apache.xmlbeans.XmlString actualValue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ACTUALVALUE$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ACTUALVALUE$4);
                }
                target.set(actualValue);
            }
        }
    }
}
