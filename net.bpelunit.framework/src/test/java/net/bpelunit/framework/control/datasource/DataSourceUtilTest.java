package net.bpelunit.framework.control.datasource;

import static org.junit.Assert.*;

import org.junit.Test;


public class DataSourceUtilTest {

	@Test
	public void testToFirstUpper() throws Exception {
		assertEquals("", DataSourceUtil.toFirstUpper(null));
		assertEquals("", DataSourceUtil.toFirstUpper(""));
		assertEquals("A", DataSourceUtil.toFirstUpper("a"));
		assertEquals("Ab", DataSourceUtil.toFirstUpper("ab"));
	}
	
}
