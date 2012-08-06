package net.bpelunit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		assertEquals("a", QNameUtil.parseQName("{a}b").getNamespaceURI());
		assertEquals("", QNameUtil.parseQName("{}b").getNamespaceURI());
		
		assertEquals("b", QNameUtil.parseQName("{a}b").getLocalPart());
		assertEquals("b", QNameUtil.parseQName("{}b").getLocalPart());
	}		
	
}
