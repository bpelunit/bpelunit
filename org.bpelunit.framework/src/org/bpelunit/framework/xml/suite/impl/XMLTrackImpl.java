/*
 * XML Type:  Track
 * Namespace: http://www.bpelunit.org/schema/testSuite
 * Java type: org.bpelunit.framework.xml.suite.XMLTrack
 *
 * Automatically generated - do not modify.
 */
package org.bpelunit.framework.xml.suite.impl;
/**
 * An XML Track(@http://www.bpelunit.org/schema/testSuite).
 *
 * This is a complex type.
 */
public class XMLTrackImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.bpelunit.framework.xml.suite.XMLTrack
{
    
    public XMLTrackImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDONLY$0 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "sendOnly");
    private static final javax.xml.namespace.QName RECEIVEONLY$2 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "receiveOnly");
    private static final javax.xml.namespace.QName SENDRECEIVE$4 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "sendReceive");
    private static final javax.xml.namespace.QName RECEIVESEND$6 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "receiveSend");
    private static final javax.xml.namespace.QName RECEIVESENDASYNCHRONOUS$8 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "receiveSendAsynchronous");
    private static final javax.xml.namespace.QName SENDRECEIVEASYNCHRONOUS$10 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "sendReceiveAsynchronous");
    private static final javax.xml.namespace.QName WAIT$12 = 
        new javax.xml.namespace.QName("http://www.bpelunit.org/schema/testSuite", "wait");
    
    
    /**
     * Gets array of all "sendOnly" elements
     */
    public org.bpelunit.framework.xml.suite.XMLSendActivity[] getSendOnlyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SENDONLY$0, targetList);
            org.bpelunit.framework.xml.suite.XMLSendActivity[] result = new org.bpelunit.framework.xml.suite.XMLSendActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "sendOnly" element
     */
    public org.bpelunit.framework.xml.suite.XMLSendActivity getSendOnlyArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLSendActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().find_element_user(SENDONLY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "sendOnly" element
     */
    public int sizeOfSendOnlyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENDONLY$0);
        }
    }
    
    /**
     * Sets array of all "sendOnly" element
     */
    public void setSendOnlyArray(org.bpelunit.framework.xml.suite.XMLSendActivity[] sendOnlyArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(sendOnlyArray, SENDONLY$0);
        }
    }
    
    /**
     * Sets ith "sendOnly" element
     */
    public void setSendOnlyArray(int i, org.bpelunit.framework.xml.suite.XMLSendActivity sendOnly)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLSendActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().find_element_user(SENDONLY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(sendOnly);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "sendOnly" element
     */
    public org.bpelunit.framework.xml.suite.XMLSendActivity insertNewSendOnly(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLSendActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().insert_element_user(SENDONLY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "sendOnly" element
     */
    public org.bpelunit.framework.xml.suite.XMLSendActivity addNewSendOnly()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLSendActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLSendActivity)get_store().add_element_user(SENDONLY$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "sendOnly" element
     */
    public void removeSendOnly(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENDONLY$0, i);
        }
    }
    
    /**
     * Gets array of all "receiveOnly" elements
     */
    public org.bpelunit.framework.xml.suite.XMLReceiveActivity[] getReceiveOnlyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RECEIVEONLY$2, targetList);
            org.bpelunit.framework.xml.suite.XMLReceiveActivity[] result = new org.bpelunit.framework.xml.suite.XMLReceiveActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "receiveOnly" element
     */
    public org.bpelunit.framework.xml.suite.XMLReceiveActivity getReceiveOnlyArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLReceiveActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().find_element_user(RECEIVEONLY$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "receiveOnly" element
     */
    public int sizeOfReceiveOnlyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RECEIVEONLY$2);
        }
    }
    
    /**
     * Sets array of all "receiveOnly" element
     */
    public void setReceiveOnlyArray(org.bpelunit.framework.xml.suite.XMLReceiveActivity[] receiveOnlyArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(receiveOnlyArray, RECEIVEONLY$2);
        }
    }
    
    /**
     * Sets ith "receiveOnly" element
     */
    public void setReceiveOnlyArray(int i, org.bpelunit.framework.xml.suite.XMLReceiveActivity receiveOnly)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLReceiveActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().find_element_user(RECEIVEONLY$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(receiveOnly);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveOnly" element
     */
    public org.bpelunit.framework.xml.suite.XMLReceiveActivity insertNewReceiveOnly(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLReceiveActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().insert_element_user(RECEIVEONLY$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveOnly" element
     */
    public org.bpelunit.framework.xml.suite.XMLReceiveActivity addNewReceiveOnly()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLReceiveActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLReceiveActivity)get_store().add_element_user(RECEIVEONLY$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "receiveOnly" element
     */
    public void removeReceiveOnly(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RECEIVEONLY$2, i);
        }
    }
    
    /**
     * Gets array of all "sendReceive" elements
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getSendReceiveArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SENDRECEIVE$4, targetList);
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] result = new org.bpelunit.framework.xml.suite.XMLTwoWayActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "sendReceive" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity getSendReceiveArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(SENDRECEIVE$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "sendReceive" element
     */
    public int sizeOfSendReceiveArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENDRECEIVE$4);
        }
    }
    
    /**
     * Sets array of all "sendReceive" element
     */
    public void setSendReceiveArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] sendReceiveArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(sendReceiveArray, SENDRECEIVE$4);
        }
    }
    
    /**
     * Sets ith "sendReceive" element
     */
    public void setSendReceiveArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity sendReceive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(SENDRECEIVE$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(sendReceive);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "sendReceive" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewSendReceive(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().insert_element_user(SENDRECEIVE$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "sendReceive" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewSendReceive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().add_element_user(SENDRECEIVE$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "sendReceive" element
     */
    public void removeSendReceive(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENDRECEIVE$4, i);
        }
    }
    
    /**
     * Gets array of all "receiveSend" elements
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getReceiveSendArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RECEIVESEND$6, targetList);
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] result = new org.bpelunit.framework.xml.suite.XMLTwoWayActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "receiveSend" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity getReceiveSendArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(RECEIVESEND$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "receiveSend" element
     */
    public int sizeOfReceiveSendArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RECEIVESEND$6);
        }
    }
    
    /**
     * Sets array of all "receiveSend" element
     */
    public void setReceiveSendArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] receiveSendArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(receiveSendArray, RECEIVESEND$6);
        }
    }
    
    /**
     * Sets ith "receiveSend" element
     */
    public void setReceiveSendArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity receiveSend)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(RECEIVESEND$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(receiveSend);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveSend" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewReceiveSend(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().insert_element_user(RECEIVESEND$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveSend" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewReceiveSend()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().add_element_user(RECEIVESEND$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "receiveSend" element
     */
    public void removeReceiveSend(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RECEIVESEND$6, i);
        }
    }
    
    /**
     * Gets array of all "receiveSendAsynchronous" elements
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getReceiveSendAsynchronousArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RECEIVESENDASYNCHRONOUS$8, targetList);
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] result = new org.bpelunit.framework.xml.suite.XMLTwoWayActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "receiveSendAsynchronous" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity getReceiveSendAsynchronousArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(RECEIVESENDASYNCHRONOUS$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "receiveSendAsynchronous" element
     */
    public int sizeOfReceiveSendAsynchronousArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RECEIVESENDASYNCHRONOUS$8);
        }
    }
    
    /**
     * Sets array of all "receiveSendAsynchronous" element
     */
    public void setReceiveSendAsynchronousArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] receiveSendAsynchronousArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(receiveSendAsynchronousArray, RECEIVESENDASYNCHRONOUS$8);
        }
    }
    
    /**
     * Sets ith "receiveSendAsynchronous" element
     */
    public void setReceiveSendAsynchronousArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity receiveSendAsynchronous)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(RECEIVESENDASYNCHRONOUS$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(receiveSendAsynchronous);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "receiveSendAsynchronous" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewReceiveSendAsynchronous(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().insert_element_user(RECEIVESENDASYNCHRONOUS$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "receiveSendAsynchronous" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewReceiveSendAsynchronous()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().add_element_user(RECEIVESENDASYNCHRONOUS$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "receiveSendAsynchronous" element
     */
    public void removeReceiveSendAsynchronous(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RECEIVESENDASYNCHRONOUS$8, i);
        }
    }
    
    /**
     * Gets array of all "sendReceiveAsynchronous" elements
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] getSendReceiveAsynchronousArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SENDRECEIVEASYNCHRONOUS$10, targetList);
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] result = new org.bpelunit.framework.xml.suite.XMLTwoWayActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "sendReceiveAsynchronous" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity getSendReceiveAsynchronousArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(SENDRECEIVEASYNCHRONOUS$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "sendReceiveAsynchronous" element
     */
    public int sizeOfSendReceiveAsynchronousArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENDRECEIVEASYNCHRONOUS$10);
        }
    }
    
    /**
     * Sets array of all "sendReceiveAsynchronous" element
     */
    public void setSendReceiveAsynchronousArray(org.bpelunit.framework.xml.suite.XMLTwoWayActivity[] sendReceiveAsynchronousArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(sendReceiveAsynchronousArray, SENDRECEIVEASYNCHRONOUS$10);
        }
    }
    
    /**
     * Sets ith "sendReceiveAsynchronous" element
     */
    public void setSendReceiveAsynchronousArray(int i, org.bpelunit.framework.xml.suite.XMLTwoWayActivity sendReceiveAsynchronous)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().find_element_user(SENDRECEIVEASYNCHRONOUS$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(sendReceiveAsynchronous);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "sendReceiveAsynchronous" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity insertNewSendReceiveAsynchronous(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().insert_element_user(SENDRECEIVEASYNCHRONOUS$10, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "sendReceiveAsynchronous" element
     */
    public org.bpelunit.framework.xml.suite.XMLTwoWayActivity addNewSendReceiveAsynchronous()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLTwoWayActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLTwoWayActivity)get_store().add_element_user(SENDRECEIVEASYNCHRONOUS$10);
            return target;
        }
    }
    
    /**
     * Removes the ith "sendReceiveAsynchronous" element
     */
    public void removeSendReceiveAsynchronous(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENDRECEIVEASYNCHRONOUS$10, i);
        }
    }
    
    /**
     * Gets array of all "wait" elements
     */
    public org.bpelunit.framework.xml.suite.XMLWaitActivity[] getWaitArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(WAIT$12, targetList);
            org.bpelunit.framework.xml.suite.XMLWaitActivity[] result = new org.bpelunit.framework.xml.suite.XMLWaitActivity[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "wait" element
     */
    public org.bpelunit.framework.xml.suite.XMLWaitActivity getWaitArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLWaitActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLWaitActivity)get_store().find_element_user(WAIT$12, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "wait" element
     */
    public int sizeOfWaitArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(WAIT$12);
        }
    }
    
    /**
     * Sets array of all "wait" element
     */
    public void setWaitArray(org.bpelunit.framework.xml.suite.XMLWaitActivity[] waitArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(waitArray, WAIT$12);
        }
    }
    
    /**
     * Sets ith "wait" element
     */
    public void setWaitArray(int i, org.bpelunit.framework.xml.suite.XMLWaitActivity wait)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLWaitActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLWaitActivity)get_store().find_element_user(WAIT$12, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(wait);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "wait" element
     */
    public org.bpelunit.framework.xml.suite.XMLWaitActivity insertNewWait(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLWaitActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLWaitActivity)get_store().insert_element_user(WAIT$12, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "wait" element
     */
    public org.bpelunit.framework.xml.suite.XMLWaitActivity addNewWait()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.bpelunit.framework.xml.suite.XMLWaitActivity target = null;
            target = (org.bpelunit.framework.xml.suite.XMLWaitActivity)get_store().add_element_user(WAIT$12);
            return target;
        }
    }
    
    /**
     * Removes the ith "wait" element
     */
    public void removeWait(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(WAIT$12, i);
        }
    }
}
