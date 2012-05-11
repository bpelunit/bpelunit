/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.util;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;

public class XMLUtilTest {

	@Test
	public void testWriteXMLFromStream() throws Exception {
		Document doc = XMLUtil.parseXML(getClass().getResourceAsStream("simple.xml"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		XMLUtil.writeXML(doc, out);
		
		ByteArrayOutputStream reference = new ByteArrayOutputStream();
		IOUtils.copy(getClass().getResourceAsStream("simple.xml"), reference);
		
		String referenceString = normalize(reference.toString());
		String actualString = normalize(out.toString());
		
		assertEquals(referenceString, actualString);
	}

	private String normalize(String s) {
		return s.trim().replaceAll("\r", "");
	}

}
