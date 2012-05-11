package net.bpelunit.util;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import org.junit.Test;

public class QNameUtilTest {

	@Test
	public void testIsQName() {
		assertFalse(QNameUtil.isQName(null));
		assertFalse(QNameUtil.isQName(""));
		assertFalse(QNameUtil.isQName("a"));
		assertTrue(QNameUtil.isQName("{b}a"));
		assertTrue(QNameUtil.isQName("{}a"));
		
		assertFalse(QNameUtil.isQName("{a"));
		assertFalse(QNameUtil.isQName("{a}"));
		assertFalse(QNameUtil.isQName("a{a}"));
	}

	@Test
	public void testParseQName() throws Exception {
		assertEquals("a", QNameUtil.parseUtil("{a}b").getNamespaceURI());
		assertEquals("", QNameUtil.parseUtil("{}b").getNamespaceURI());
		
		assertEquals("b", QNameUtil.parseUtil("{a}b").getLocalPart());
		assertEquals("b", QNameUtil.parseUtil("{}b").getLocalPart());
	}		
	
}
