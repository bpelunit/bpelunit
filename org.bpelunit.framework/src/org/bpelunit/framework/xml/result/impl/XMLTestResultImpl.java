/*
 * XML Type:  TestResult
 * Namespace: http://www.bpelunit.org/schema/testResult
 * Java type: org.bpelunit.framework.xml.result.XMLTestResult
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.result.impl;
/**
 * An XML TestResult(@http://www.bpelunit.org/schema/testResult).
 *
 * This is a complex type.
 */
public class XMLTestResultImpl extends org.bpelunit.framework.xml.result.impl.XMLArtefactImpl implements org.bpelunit.framework.xml.result.XMLTestResult
{
    
    public XMLTestResultImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TESTCASE$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testResult", "testCase");
    
    
    /**
     * Gets a List of "testCase" elements
     */
    public java.util.List<org.bpelunit.framework.xml.result.XMLTestCase> getTestCaseList()
    {
        final class TestCaseList extends java.util.AbstractList<org.bpelunit.framework.xml.result.XMLTestCase>
        {
            public org.bpelunit.framework.xml.result.XMLTestCase get(int i)
                { return XMLTestResultImpl.this.getTestCaseArray(i); }
            
            public org.bpelunit.framework.xml.result.XMLTestCase set(int i, org.bpelunit.framework.xml.result.XMLTestCase o)
            {
                org.bpelunit.framework.xml.result.XMLTestCase old = XMLTestResultImpl.this.getTestCaseArray(i);
                XMLTestResultImpl.this.setTestCaseArray(i, o);
                return old;
            }
            
            public void add(int i, org.bpelunit.framework.xml.result.XMLTestCase o)
                { XMLTestResultImpl.this.insertNewTestCase(i).set(o); }
            
            public org.bpelunit.framework.xml.result.XMLTestCase remove(int i)
            {
                org.bpelunit.framework.xml.result.XMLTestCase old = XMLTestResultImpl.this.getTestCaseArray(i);
                XMLTestResultImpl.this.removeTestCase(i);
                return old;
            }
            
            public int size()
                { return XMLTestResultImpl.this.sizeOfTestCaseArray(); }
            
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
    public org.bpelunit.framework.xml.result.XMLTestCase[] getTestCaseArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TESTCASE$0, targetList);
            org.bpelunit.framework.xml.result.XMLTestCase[] result = new org.bpelunit.framework.xml.result.XMLTestCase[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "testCase" element
     */
    public org.bpelunit.framework.xml.result.XMLTestCase getTestCaseArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.result.XMLTestCase)get_store().find_element_user(TESTCASE$0, i);
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
    public void setTestCaseArray(org.bpelunit.framework.xml.result.XMLTestCase[] testCaseArray)
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
    public void setTestCaseArray(int i, org.bpelunit.framework.xml.result.XMLTestCase testCase)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.result.XMLTestCase)get_store().find_element_user(TESTCASE$0, i);
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
    public org.bpelunit.framework.xml.result.XMLTestCase insertNewTestCase(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.result.XMLTestCase)get_store().insert_element_user(TESTCASE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "testCase" element
     */
    public org.bpelunit.framework.xml.result.XMLTestCase addNewTestCase()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.result.XMLTestCase target = null;
            target = (org.bpelunit.framework.xml.result.XMLTestCase)get_store().add_element_user(TESTCASE$0);
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
