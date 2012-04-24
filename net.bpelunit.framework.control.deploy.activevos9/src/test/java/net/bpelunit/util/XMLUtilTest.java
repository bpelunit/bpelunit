/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.Test;
import org.w3c.dom.Document;


public class XMLUtilTest {

	@Test
	public void testWriteXMLFromStream() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream("simple.xml"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		XMLUtil.writeXML(doc, out);
		
		// TODO Assertionks
	}
	
}
