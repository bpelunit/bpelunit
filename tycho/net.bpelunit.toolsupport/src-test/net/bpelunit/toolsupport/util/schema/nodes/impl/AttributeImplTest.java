package net.bpelunit.toolsupport.util.schema.nodes.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.Attribute;
import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import org.junit.Before;
import org.junit.Test;

public class AttributeImplTest extends SchemaNodeTestAbstract {

	private Attribute attribute;

	@Before
	public void setUp() throws Exception {
		this.attribute = new AttributeImpl(new QName("http://www.test.de", "id"));
	}

	@Test
	public void testSetType() throws Exception {
		SimpleType simple = new SimpleTypeImpl("integer");
		this.attribute.setType(simple);

		assertEquals(simple, this.attribute.getType());
	}

	@Test
	public void testSetDefault() throws Exception {
		// Either defaultValue or fixedValue have to be null
		this.attribute.setDefaultValue("1");
		assertEquals("1", this.attribute.getDefaultValue());
		assertNull("fixedValue has to be null", this.attribute.getFixedValue());

		// make sure the null check doesn't succeed because of the null
		// initialisation. Setting the defaultValue to null, must not change the
		// the fixedValue
		this.attribute.setFixedValue("-1");
		assertNull("defaultValue has to be null", this.attribute.getDefaultValue());
		this.attribute.setDefaultValue(null);
		assertEquals("-1", this.attribute.getFixedValue());

		// Setting the defaultValue with the Setter to null must work
		this.attribute.setDefaultValue("1");
		this.attribute.setDefaultValue(null);
		assertNull("defaultValue has to be null", this.attribute.getDefaultValue());
	}

	@Test
	public void testSetFixedValue() throws Exception {
		// see testSetDefaultValue()
		this.attribute.setFixedValue("1000");
		assertEquals("1000", this.attribute.getFixedValue());
		assertNull("defaultValue has to be null", this.attribute.getDefaultValue());

		this.attribute.setDefaultValue("1");
		assertNull("fixedValue has to be null", this.attribute.getFixedValue());
		this.attribute.setFixedValue(null);
		assertEquals("1", this.attribute.getDefaultValue());

		this.attribute.setFixedValue("1");
		this.attribute.setFixedValue(null);
		assertNull("fixedValue has to be null", this.attribute.getFixedValue());
	}

	@Override
	protected SchemaNode constructSchemaNode(QName name) {
		return new AttributeImpl(name);
	}

	@Override
	protected SchemaNode constructSchemaNode(String targetNs, String localPart) {
		return new AttributeImpl(targetNs, localPart);
	}

}
