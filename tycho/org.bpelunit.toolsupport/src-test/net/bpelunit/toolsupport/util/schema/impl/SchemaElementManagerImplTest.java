package net.bpelunit.toolsupport.util.schema.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.SchemaElementManager;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import org.junit.Before;
import org.junit.Test;

public class SchemaElementManagerImplTest {

	private SchemaElementManager manager;

	@Before
	public void setUp() throws Exception {

		this.manager = new SchemaElementManagerImpl();
	}

	@Test
	public void testGetSimpleTypeByQName() {
		String localPart = "string";
		SimpleType simple = this.manager.getSimpleType(new QName(SchemaNode.XML_SCHEMA_NAMESPACE,
				localPart));
		assertNotNull("If this SimpleType does not exists the manager has to create one", simple);
		assertEquals(new QName(SchemaNode.XML_SCHEMA_NAMESPACE, localPart), simple.getQName());

		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(new QName(SchemaNode.XML_SCHEMA_NAMESPACE, localPart)));
		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(SchemaNode.XML_SCHEMA_NAMESPACE, localPart));
		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(localPart));

	}

	@Test
	public void testGetSimpleTypeByLocalPart() throws Exception {
		String localPart = "integer";
		SimpleType simple = this.manager.getSimpleType(localPart);
		assertNotNull("If this SimpleType does not exists the manager has to create one", simple);
		assertEquals(SchemaNode.XML_SCHEMA_NAMESPACE, simple.getNamespace());
		assertEquals(localPart, simple.getLocalPart());

		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(localPart));
		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(SchemaNode.XML_SCHEMA_NAMESPACE, localPart));
		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(new QName(SchemaNode.XML_SCHEMA_NAMESPACE, localPart)));
	}

	@Test
	public void testGetSimpleTypeByTargetNamespaceAndLocalPart() throws Exception {
		String localPart = "bool";
		String targetNs = "http://my.own.namespace.org";
		SimpleType simple = this.manager.getSimpleType(targetNs, localPart);
		assertNotNull("If this SimpleType does not exists the manager has to create one", simple);
		assertEquals(targetNs, simple.getNamespace());
		assertEquals(localPart, simple.getLocalPart());

		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(targetNs, localPart));
		assertSame("a further call has to return the same SimpleType", simple, this.manager
				.getSimpleType(new QName(targetNs, localPart)));
	}

	@Test
	public void testGetComplexTypeByQName() {
		QName qName = new QName("http://my.own.namespace.org", "PersonType");
		ComplexType complex = this.manager.getComplexType(qName);
		assertNotNull("If this ComplexType does not exists the manager has to create one", complex);
		assertEquals(qName, complex.getQName());

		assertSame("a further call has to return the same ComplexType", complex, this.manager
				.getComplexType(qName));
		assertSame("a further call has to return the same ComplexType", complex, this.manager
				.getComplexType(qName.getNamespaceURI(), qName.getLocalPart()));
	}

	@Test
	public void testGetComplexTypeByTargetNamespaceAndLocalPart() {
		String targetNs = "http://my.own.namespace.org";
		String localPart = "PersonType";
		ComplexType complex = this.manager.getComplexType(targetNs, localPart);
		assertNotNull("If this ComplexType does not exists the manager has to create one", complex);
		assertEquals(targetNs, complex.getNamespace());
		assertEquals(localPart, complex.getLocalPart());

		assertSame("a further call has to return the same ComplexType", complex, this.manager
				.getComplexType(targetNs, localPart));
		assertSame("a further call has to return the same ComplexType", complex, this.manager
				.getComplexType(new QName(targetNs, localPart)));
	}

	@Test
	public void testGetElementByQName() {
		QName qName = new QName("http://my.own.namespace.org", "person");
		Element element = this.manager.getElement(qName);
		assertNotNull("If this Element does not exists the manager has to create one", element);
		assertEquals(qName, element.getQName());

		assertSame("a further call has to return the same Element", element, this.manager
				.getElement(qName));
		assertSame("a further call has to return the same Element", element, this.manager
				.getElement(qName.getNamespaceURI(), qName.getLocalPart()));
	}

	@Test
	public void testGetElementByTargetNamespaceAndLocalPart() {
		String targetNs = "http://my.own.namespace.org";
		String localPart = "person";
		Element element = this.manager.getElement(targetNs, localPart);
		assertNotNull("If this Element does not exists the manager has to create one", element);
		assertEquals(targetNs, element.getNamespace());
		assertEquals(localPart, element.getLocalPart());

		assertSame("a further call has to return the same Element", element, this.manager
				.getElement(targetNs, localPart));
		assertSame("a further call has to return the same Element", element, this.manager
				.getElement(new QName(targetNs, localPart)));
	}

	@Test
	public void testWasLastComplexCreated() throws Exception {
		this.manager.getComplexType("http://my.own.namespace.org", "person");
		assertTrue(this.manager.wasLastComplexNewCreated());

		this.manager.getComplexType("http://my.own.namespace.org", "person");
		assertFalse(this.manager.wasLastComplexNewCreated());

		this.manager.getComplexType("http://my.own.namespace.org", "person2");
		assertTrue(this.manager.wasLastComplexNewCreated());

		this.manager.getComplexType("http://my.own.namespace.org", "person2");
		assertFalse(this.manager.wasLastComplexNewCreated());

		this.manager.getComplexType("http://my.own.namespace.org", "person");
		assertFalse(this.manager.wasLastComplexNewCreated());
	}
}
