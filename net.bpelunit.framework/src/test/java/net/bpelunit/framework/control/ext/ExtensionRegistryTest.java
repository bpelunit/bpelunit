package net.bpelunit.framework.control.ext;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;


public class ExtensionRegistryTest {

	static class TestBean {
		public void setSomething() {
		}
		
		public void setStringSometing(String s) {
		}
		
		public void something(String s) {
		}
		
		public String setNotStringSomething(String s) {
			return null;
		}
	}
	
	@Test
	public void testAnalyzeString() throws Exception {
		assertArrayEquals(new String[] { "a" }, ExtensionRegistry.analyzeString("a").toArray(new String[0]));
		assertArrayEquals(new String[] { "a", "b" }, ExtensionRegistry.analyzeString("a,b").toArray(new String[0]));
		assertArrayEquals(new String[] { "a", "b", "c" }, ExtensionRegistry.analyzeString("a,b,c").toArray(new String[0]));
		assertArrayEquals(new String[] { "a", "b", "c" }, ExtensionRegistry.analyzeString(" a , b , c ").toArray(new String[0]));
	}
	
	@Test
	public void testIsStringSetter() throws Exception {
		Method method = TestBean.class.getMethod("setSomething");
		assertFalse(ExtensionRegistry.isStringSetter(method));

		method = TestBean.class.getMethod("something", String.class);
		assertFalse(ExtensionRegistry.isStringSetter(method));
		
		method = TestBean.class.getMethod("setNotStringSomething", String.class);
		assertFalse(ExtensionRegistry.isStringSetter(method));
		
		method = TestBean.class.getMethod("setStringSometing", String.class);
		assertTrue(ExtensionRegistry.isStringSetter(method));
	}
}
