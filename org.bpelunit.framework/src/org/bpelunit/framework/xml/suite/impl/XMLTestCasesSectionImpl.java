/*
 * XML Type:  TestCasesSection
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTestCasesSection
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML TestCasesSection(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLTestCasesSectionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLTestCasesSection
{
    
    public XMLTestCasesSectionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TESTCASE$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "testCase");
    
    
    /**
     * Gets a List of "testCase" elements
     */
    public java.util.List<org.bpelunit.framework.xml.suite.XMLTestCase> getTestCaseList()
    {
        final class TestCaseList extends java.util.AbstractList<org.bpelunit.framework.xml.suite.XMLTestCase>
        {
            public org.bpelunit.framework.xml.suite.XMLTestCase get(int i)
                { return XMLTestCasesSectionImpl.this.getTestCaseArray(i); }
            
            public org.bpelunit.framework.xml.suite.XMLTestCase set(int i, org.bpelunit.framework.xml.suite.XMLTestCase o)
            {
                org.bpelunit.framework.xml.suite.XMLTestCase old = XMLTestCasesSectionImpl.this.getTestCaseArray(i);
                XMLTestCasesSectionImpl.this.setTestCaseArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.suite.XMLTestCase o)
                { XMLTestCasesSectionImpl.this.insertNewTestCase(i).set(o); }
            
            public org.bpelunit.framework.xml.suite.XMLTestCase remove(int i)
            {
                org.bpelunit.framework.xml.suite.XMLTestCase old = XMLTestCasesSectionImpl.this.getTestCaseArray(i);
                XMLTestCasesSectionImpl.this.removeTestCase(i);
                return old;
            }
            
            public int size()
                { return XMLTestCasesSectionImpl.this.sizeOfTestCaseArray(); }
            
        }
        
        synchronized (monitor())
        {
            check_orphaned();
            return new TestCaseList();
        }
    }
    
    /**
     * Gets array of all "testCase" elements
     */
    public org.bpelunit.framework.xml.suite.XMLTestCase[] getTestCaseArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TESTCASE$0, targetList);
            org.bpelunit.framework.xml.suite.XMLTestCase[] result = new org.bpelunit.framework.xml.suite.XMLTestCase[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "testCase" element
     */
    public org.bpelunit.framework.xml.suite.XMLTestCase getTestCaseArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestCase)get_store().find_element_user(TESTCASE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "testCase" element
     */
    public int sizeOfTestCaseArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TESTCASE$0);
        }
    }
    
    /**
     * Sets array of all "testCase" element
     */
    public void setTestCaseArray(org.bpelunit.framework.xml.suite.XMLTestCase[] testCaseArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(testCaseArray, TESTCASE$0);
        }
    }
    
    /**
     * Sets ith "testCase" element
     */
    public void setTestCaseArray(int i, org.bpelunit.framework.xml.suite.XMLTestCase testCase)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestCase)get_store().find_element_user(TESTCASE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(testCase);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "testCase" element
     */
    public org.bpelunit.framework.xml.suite.XMLTestCase insertNewTestCase(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestCase)get_store().insert_element_user(TESTCASE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "testCase" element
     */
    public org.bpelunit.framework.xml.suite.XMLTestCase addNewTestCase()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTestCase)get_store().add_element_user(TESTCASE$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "testCase" element
     */
    public void removeTestCase(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TESTCASE$0, i);
        }
    }
}
