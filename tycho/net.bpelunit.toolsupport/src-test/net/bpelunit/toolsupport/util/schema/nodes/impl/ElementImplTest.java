package net.bpelunit.toolsupport.util.schema.nodes.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.HashMap;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.SchemaParser;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import net.bpelunit.toolsupport.util.schema.nodes.Type;
import org.junit.Before;
import org.junit.Test;

public class ElementImplTest extends SchemaNodeTestAbstract {

	private Element element;

	@Before
	public void setUp() throws Exception {
		this.element = new ElementImpl("http://www.Test.org", "vorname");
	}

	@Test
	public void testSetType() {
		Type simple = new SimpleTypeImpl("string");
		this.element.setType(simple);

		assertEquals(simple, this.element.getType());
	}

	@Test
	public void testSetMinOccurs() {
		// default value
		assertEquals(1, this.element.getMinOccurs());

		// minOccurs may be 0
		this.element.setMinOccurs(0);
		assertEquals(0, this.element.getMinOccurs());

		// can be any positive number
		this.element.setMinOccurs(10);
		assertEquals(10, this.element.getMinOccurs());

		// negative values are considered as 0
		this.element.setMinOccurs(-1);
		assertEquals(0, this.element.getMinOccurs());
	}

	@Test
	public void testSetMaxOccurs() {
		// default value for maxOccurs
		assertEquals(1, this.element.getMaxOccurs());

		// 0 is considered as unbound
		this.element.setMaxOccurs(0);
		assertEquals(0, this.element.getMaxOccurs());

		// can be any positive number
		this.element.setMaxOccurs(5);
		assertEquals(5, this.element.getMaxOccurs());

		// negative values are considered as 0
		this.element.setMaxOccurs(-5);
		assertEquals(0, this.element.getMaxOccurs());
	}

	@Test
	public void testSetDefaultValue() {
		// Either defaultValue or fixedValue have to be null
		this.element.setDefaultValue("1");
		assertEquals("1", this.element.getDefaultValue());
		assertNull("fixedValue has to be null", this.element.getFixedValue());

		// make sure the null check doesn't succeed because of the null
		// initialisation. Setting the defaultValue to null, must not change the
		// the fixedValue
		this.element.setFixedValue("-1");
		assertNull("defaultValue has to be null", this.element
				.getDefaultValue());
		this.element.setDefaultValue(null);
		assertEquals("-1", this.element.getFixedValue());

		// Setting the defaultValue with the Setter to null must work
		this.element.setDefaultValue("1");
		this.element.setDefaultValue(null);
		assertNull("defaultValue has to be null", this.element
				.getDefaultValue());
	}

	@Test
	public void testSetFixedValue() {
		// see testSetDefaultValue()
		this.element.setFixedValue("1000");
		assertEquals("1000", this.element.getFixedValue());
		assertNull("defaultValue has to be null", this.element
				.getDefaultValue());

		this.element.setDefaultValue("1");
		assertNull("fixedValue has to be null", this.element.getFixedValue());
		this.element.setFixedValue(null);
		assertEquals("1", this.element.getDefaultValue());

		this.element.setFixedValue("1");
		this.element.setFixedValue(null);
		assertNull("fixedValue has to be null", this.element.getFixedValue());
	}

	@Test
	public void testToXMLString() throws Exception {
		SchemaParser parser = new SchemaParser();
		parser.parse(new File("testSchemata/defineComplexElements3.xsd"));

		String namespace = "http://schematest.bpelunit.org";
		this.element = parser.getElements().get(
				new QName(namespace, "employee"));
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put(namespace, "tes");

		String actual = this.element.toXMLString(namespaces);
		String expected = "<tes:employee lang=\"DE\" personId=\"-1\">"
				+ "\n\t<tes:firstname></tes:firstname>"
				+ "\n\t<tes:lastname></tes:lastname>" + "\n\t<tes:location>"
				+ "\n\t\t<tes:city></tes:city>" + "\n\t\t<tes:zip></tes:zip>"
				+ "\n\t</tes:location>" + "\n\t<tes:location>"
				+ "\n\t\t<tes:city></tes:city>" + "\n\t\t<tes:zip></tes:zip>"
				+ "\n\t</tes:location>" + "\n</tes:employee>";
		assertEquals(expected, actual);
	}

	@Override
	protected SchemaNode constructSchemaNode(QName name) {
		return new ElementImpl(name);
	}

	@Override
	protected SchemaNode constructSchemaNode(String targetNs, String localPart) {
		return new ElementImpl(targetNs, localPart);
	}

}
