package net.bpelunit.toolsupport.util.schema.nodes.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import org.junit.Before;
import org.junit.Test;

public class ComplexTypeImplTest extends SchemaNodeTestAbstract {

	private ComplexType complex;

	@Before
	public void setUp() throws Exception {
		this.complex = new ComplexTypeImpl("http://www.test.org", "PersonType");
	}

	@Test
	public void testAddElement() {
		assertTrue(this.complex.getElements().isEmpty());

		ElementImpl element = new ElementImpl("http://www.test.org", "Name");
		this.complex.addElement(element);
		assertEquals(1, this.complex.getElements().size());
		assertTrue(this.complex.getElements().contains(element));
	}

	@Test
	public void testAddAttribute() {
		assertTrue(this.complex.getAttributes().isEmpty());

		AttributeImpl attribute = new AttributeImpl("http://www.test.org", "id");
		this.complex.addAttribute(attribute);
		assertEquals(1, this.complex.getAttributes().size());
		assertTrue(this.complex.getAttributes().contains(attribute));
	}

	@Test
	public void testIsSimpleType() throws Exception {
		assertFalse(this.complex.isSimpleType());
	}

	@Test
	public void testIsComplexType() throws Exception {
		assertTrue(this.complex.isComplexType());
	}

	@Test
	public void testGetAsSimpleType() throws Exception {
		assertNull(this.complex.getAsSimpleType());
	}

	@Test
	public void testGetAsComplexType() throws Exception {
		assertSame(this.complex, this.complex.getAsComplexType());
	}

	@Override
	protected SchemaNode constructSchemaNode(QName name) {
		return new ComplexTypeImpl(name);
	}

	@Override
	protected SchemaNode constructSchemaNode(String targetNs, String locaPart) {
		return new ComplexTypeImpl(targetNs, locaPart);
	}

}
