package net.bpelunit.framework.control.ext;

import static org.junit.Assert.*;

import org.junit.Test;


public class ExtensionRegistryTest {

	@Test
	public void testAnalyzeString() throws Exception {
		assertArrayEquals(new String[] { "a" }, ExtensionRegistry.analyzeString("a").toArray(new String[0]));
		assertArrayEquals(new String[] { "a", "b" }, ExtensionRegistry.analyzeString("a,b").toArray(new String[0]));
		assertArrayEquals(new String[] { "a", "b", "c" }, ExtensionRegistry.analyzeString("a,b,c").toArray(new String[0]));
		assertArrayEquals(new String[] { "a", "b", "c" }, ExtensionRegistry.analyzeString(" a , b , c ").toArray(new String[0]));
	}
	
}
