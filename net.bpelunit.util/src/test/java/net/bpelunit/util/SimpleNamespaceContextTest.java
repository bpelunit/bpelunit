package net.bpelunit.util;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;


public class SimpleNamespaceContextTest {

	private SimpleNamespaceContext context;

	@Before
	public void setUp() {
		context = new SimpleNamespaceContext();
		context.addNamespace("c", "B");
		context.addNamespace("a", "A");
		context.addNamespace("b", "B");
	}

	@Test
	public void testGetNamespaceURI() throws Exception {
		assertEquals("A", context.getNamespaceURI("a"));
		assertEquals("B", context.getNamespaceURI("b"));
		assertEquals("B", context.getNamespaceURI("c"));
		assertEquals(null, context.getNamespaceURI("d"));
	}
	
	@Test
	public void testGetPrefix() throws Exception {
		assertEquals("a", context.getPrefix("A"));
		assertEquals("b", context.getPrefix("B"));
		assertEquals(null, context.getPrefix("C"));
	}
	
	@Test
	public void testGetPrefixes() throws Exception {
		Iterator<String> prefixes = context.getPrefixes("A");
		assertTrue(prefixes.hasNext());
		assertEquals("a", prefixes.next());
		assertFalse(prefixes.hasNext());
		
		prefixes = context.getPrefixes("B");
		assertTrue(prefixes.hasNext());
		assertEquals("b", prefixes.next());
		assertTrue(prefixes.hasNext());
		assertEquals("c", prefixes.next());
		assertFalse(prefixes.hasNext());
	}
}
