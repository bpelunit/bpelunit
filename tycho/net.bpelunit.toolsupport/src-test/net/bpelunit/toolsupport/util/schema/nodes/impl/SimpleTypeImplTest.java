package net.bpelunit.toolsupport.util.schema.nodes.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import org.junit.Before;
import org.junit.Test;

public class SimpleTypeImplTest extends SchemaNodeTestAbstract {

	private SimpleType simple;

	@Before
	public void setup() {
		this.simple = new SimpleTypeImpl("string");
	}

	@Test
	public void testSimpleTypeImplString() {

		assertEquals(SchemaNode.XML_SCHEMA_NAMESPACE, this.simple.getNamespace());
		assertEquals("string", this.simple.getLocalPart());
	}

	@Test
	public void testIsSimpleType() throws Exception {
		assertTrue(this.simple.isSimpleType());
	}

	@Test
	public void testIsComplexType() throws Exception {
		assertFalse(this.simple.isComplexType());
	}

	@Test
	public void testGetAsSimpleType() throws Exception {
		assertSame(this.simple, this.simple.getAsSimpleType());
	}

	@Test
	public void testGetAsComplexType() throws Exception {
		assertNull(this.simple.getAsComplexType());
	}

	@Override
	protected SchemaNode constructSchemaNode(QName name) {
		return new SimpleTypeImpl(name);
	}

	@Override
	protected SchemaNode constructSchemaNode(String targetNs, String localPart) {
		return new SimpleTypeImpl(targetNs, localPart);
	}

}
