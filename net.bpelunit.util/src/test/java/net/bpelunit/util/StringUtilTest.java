package net.bpelunit.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class StringUtilTest {

	@Test
	public void testToFirstUpper() throws Exception {
		assertEquals("", StringUtil.toFirstUpper(null));
		assertEquals("", StringUtil.toFirstUpper(""));

		assertEquals("A", StringUtil.toFirstUpper("a"));
		assertEquals("Ab", StringUtil.toFirstUpper("ab"));
	}
	
}
