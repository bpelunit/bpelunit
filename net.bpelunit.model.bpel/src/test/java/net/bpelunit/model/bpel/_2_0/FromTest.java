package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IFrom;
import net.bpelunit.model.bpel.IFrom.Roles;
import net.bpelunit.model.bpel.IQuery;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.FromDocument;
import org.w3c.dom.Element;


public class FromTest {

	private IFrom from;

	@Before
	public void setUp() {
		FromDocument fromDoc = FromDocument.Factory.newInstance();
		
		fromDoc.addNewFrom();
		from = new From(fromDoc.getFrom());
	}
	
	@Test
	public void testExpressionLanguageFromModel() throws Exception {
		assertNull(from.getExpressionLanguage());
		
		from.setExpressionLanguage("abc");
		assertEquals("abc", from.getExpressionLanguage());
		
		from.setExpressionLanguage("def");
		assertEquals("def", from.getExpressionLanguage());
	}
	
	@Test
	public void testVariableFromModel() throws Exception {
		assertNull(from.getVariable());
		
		from.setVariable("abc");
		assertEquals("abc", from.getVariable());
		
		from.setVariable("def");
		assertEquals("def", from.getVariable());
	}
	
	@Test
	public void testPartFromModel() throws Exception {
		assertNull(from.getPart());
		
		from.setPart("abc");
		assertEquals("abc", from.getPart());
		
		from.setPart("def");
		assertEquals("def", from.getPart());
	}
	
	@Test
	public void testPropertyFromModel() throws Exception {
		assertNull(from.getProperty());
		
		QName p1 = new QName("ns", "abc");
		from.setProperty(p1);
		assertEquals(p1, from.getProperty());
		
		QName p2 = new QName("ns2", "def");
		from.setProperty(p2);
		assertEquals(p2, from.getProperty());
	}
	
	@Test
	public void testPartnerLinkFromModel() throws Exception {
		assertNull(from.getPartnerLink());
		
		from.setPartnerLink("abc");
		assertEquals("abc", from.getPartnerLink());
		
		from.setPartnerLink("def");
		assertEquals("def", from.getPartnerLink());
	}
	
	@Test
	public void testEndpointReferenceFromModel() throws Exception {
		assertNull(from.getEndpointReference());
		
		from.setEndpointReference(Roles.MY_ROLE);
		assertEquals(Roles.MY_ROLE, from.getEndpointReference());
		
		from.setEndpointReference(Roles.PARTNER_ROLE);
		assertEquals(Roles.PARTNER_ROLE, from.getEndpointReference());
	}
	
	@Test
	public void testSetLiteral() throws Exception {
		from.setNewQuery();
		Element e = from.setNewLiteral("nsUri", "element");
		assertEquals("nsUri", e.getNamespaceURI());
		assertEquals("element", e.getLocalName());
		
		assertSame(e, from.getLiteral());
		assertNull(from.getQuery());
	}
	
	@Test
	public void testSetQuery() throws Exception {
		from.setNewLiteral("a", "A");
		
		IQuery query = from.setNewQuery();
		assertNotNull(query);
		assertSame(query, from.getQuery());
		assertNull(from.getLiteral());
	}
	
	@Test
	public void testNamespacePrefixes() throws Exception {
		from.setNewQuery();
		
		assertNull(from.getNamespacePrefix("non-existing"));
		
		from.addLocalNamespace("ns", "existing");
		assertEquals("ns", from.getNamespacePrefix("existing"));
		
		from.addLocalNamespace("ns2", "existing2");
		assertEquals("ns2", from.getNamespacePrefix("existing2"));
	}
}
