/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.unit;

import static org.junit.Assert.assertEquals;

import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.test.util.TestUtil;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * Tests data copying.
 * 
 * @version $Id: TestDataCopy.java,v 1.5 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestDataCopy extends SimpleTest {

	private static final String PATH_TO_FILES= "/dataCopy/";

	// ************ Helpers **************

	private DataCopyOperation getCopyFrom(String string) {
		return new DataCopyOperation(null, string, "");
	}

	private DataCopyOperation getCopyTo(String string) {
		return new DataCopyOperation(null, "", string);
	}

	// ********************* Test Cases ************************

	@Test
	public void testSimpleGet() throws Exception {

		Element parent= TestUtil.readLiteralData(PATH_TO_FILES + "dc.xmlfrag");

		NamespaceContextImpl c= new NamespaceContextImpl();
		c.setNamespace("emp", "http://packtpub.com/service/employee/");
		c.setNamespace("aln", "http://packtpub.com/service/airline/");

		DataCopyOperation copy= getCopyFrom("emp:employee/emp:FirstName[1]");
		copy.retrieveTextNodes(parent, c);
		String textNodeFrom= copy.getCopiedValue();

		assertEquals("Phil", textNodeFrom);

		DataCopyOperation copy2= getCopyFrom("emp:employee/emp:LastName/@myName");
		copy2.retrieveTextNodes(parent, c);
		String attribute= copy2.getCopiedValue();

		assertEquals("Huhu", attribute);
	}

	@Test
	public void testSimplePut() throws Exception {

		Element parent= TestUtil.readLiteralData(PATH_TO_FILES + "dc.xmlfrag");

		NamespaceContextImpl c= new NamespaceContextImpl();
		c.setNamespace("emp", "http://packtpub.com/service/employee/");
		c.setNamespace("aln", "http://packtpub.com/service/airline/");

		DataCopyOperation copy= getCopyTo("emp:employee/emp:FirstName");
		copy.setCopiedValue("Amen");
		copy.setTextNodes(parent, c);

		Node firstNameElement= TestUtil.getNode(parent, c, "emp:employee/emp:FirstName");

		assertEquals("Amen", firstNameElement.getTextContent());

		DataCopyOperation copy2= getCopyTo("emp:employee/emp:LastName/@myName");
		copy2.setCopiedValue("Foo");
		copy2.setTextNodes(parent, c);

		Element lastName= (Element) TestUtil.getNode(parent, c, "emp:employee/emp:LastName");

		assertEquals("Foo", lastName.getAttribute("myName"));
	}

}
