package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TDocumentation;
import org.w3c.dom.Element;


public class DocumentationTest {

	private Documentation documentation;
	
	@Before
	public void setUp() {
		documentation = new Documentation(TDocumentation.Factory.newInstance());
	}
	
	@Test
	public void testGetDocumentationElements() throws Exception {
		assertEquals(0, documentation.getDocumentationElements().size());
	}
	
	@Test
	public void testGetStringContent() throws Exception {
		assertEquals(null, documentation.getStringContent());
	}
	
	@Test
	public void testAddDocumentationElement() throws Exception {
		assertEquals(0, documentation.getDocumentationElements().size());
		
		Element e1 = documentation.addDocumentationElement("docNS", "d1");
		assertEquals(1, documentation.getDocumentationElements().size());
		
		Element e2 = documentation.addDocumentationElement("docNS", "d2");
		assertEquals(2, documentation.getDocumentationElements().size());
		
		assertSame(e1, documentation.getDocumentationElements().get(0));
		assertSame(e2, documentation.getDocumentationElements().get(1));
		assertNull(documentation.getStringContent());
	}
	
	@Test
	public void testSetStringContent() throws Exception {
		documentation.setStringContent("abc");
		assertEquals("abc", documentation.getStringContent());
		assertEquals(1, documentation.getDocumentationElements().size());
		
		documentation.setStringContent("def");
		assertEquals("def", documentation.getStringContent());
		assertEquals(1, documentation.getDocumentationElements().size());
	}
}
