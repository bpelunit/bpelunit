package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TQuery;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class QueryTest {

	private Query query;

	@Before
	public void setUp() {
		this.query = new Query(TQuery.Factory.newInstance());
	}
	
	@Test
	public void testQueryLanguage() throws Exception {
		assertNull(query.getQueryLanguage());
		
		query.setQueryLanguage("abc");
		assertEquals("abc", query.getQueryLanguage());
		
		query.setQueryLanguage("def");
		assertEquals("def", query.getQueryLanguage());
	}

	@Test
	public void testQueryContents() throws Exception {
		assertNull(query.getQueryContents());
		
		query.setQueryContents("//a");
		assertEquals("//a", query.getQueryContents());
		assertEquals(1, query.getNodes().size());
		assertEquals("//a", query.getNodes().get(0).getNodeValue());
		
		query.setQueryContents("//b");
		assertEquals("//b", query.getQueryContents());
		assertEquals(1, query.getNodes().size());
		assertEquals("//b", query.getNodes().get(0).getNodeValue());
	}
	
	@Test
	public void testNodes() throws Exception {
		Element e1 = query.addNewElement("ns", "e1");
		Text t1 = query.addNewTextNode("t1");
		Element e2 = query.addNewElement("ns", "e2");
		Text t2 = query.addNewTextNode("t2");
		
		List<Node> children = query.getNodes();
		assertEquals(4, children.size());
		assertSame(e1, children.get(0));
		assertSame(t1, children.get(1));
		assertSame(e2, children.get(2));
		assertSame(t2, children.get(3));
	}
}
