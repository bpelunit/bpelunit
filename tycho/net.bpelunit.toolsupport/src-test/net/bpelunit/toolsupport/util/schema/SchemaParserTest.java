package net.bpelunit.toolsupport.util.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.Attribute;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import net.bpelunit.toolsupport.util.schema.nodes.Type;
import org.junit.Before;
import org.junit.Test;

public class SchemaParserTest {
	private final String schemaFileFolder = "testSchemata";
	private final static String targetNs = "http://schematest.bpelunit.org";
	private final static String schemaNs = "http://www.w3.org/2001/XMLSchema";
	private static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	private SchemaParser parser;
	private HashMap<QName, Element> elements;
	private HashMap<QName, ComplexType> complexTypes;
	private HashMap<QName, SimpleType> simpleTypes;

	@Before
	public void setUp() throws Exception {
		this.parser = new SchemaParser();
		this.elements = this.parser.getElements();
		this.complexTypes = this.parser.getComplexTypes();
		this.simpleTypes = this.parser.getSimpleTypes();
	}

	@Test
	public void testSimpleElement() throws Exception {
		this.parser.parse(this.getFile("simpleElement.xsd"));

		// there have to be 3 simpleTypes
		assertEquals(3, this.simpleTypes.size());
		this.checkSimpleTypesFor(new String[] { "string", "integer", "date" });

		// there have to be 4 Elements
		assertEquals(4, this.elements.size());

		// The schema does not contain any complexTypes
		assertTrue(this.complexTypes.isEmpty());

		// Element name in targetNs must exist, must be type string from
		// schemaNs
		Element element = this.elements.get(new QName(targetNs, "name"));
		assertNotNull(element);
		assertEquals("name", element.getLocalPart());
		assertEquals(targetNs, element.getNamespace());
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// Element lastname in targetNs must exist, must be type string from
		// schemaNs
		element = this.elements.get(new QName(targetNs, "lastname"));
		assertNotNull(element);
		assertEquals("lastname", element.getLocalPart());
		assertEquals(targetNs, element.getNamespace());
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// Element age in targetNs must exist, must be type integer from
		// schemaNs
		element = this.elements.get(new QName(targetNs, "age"));
		assertNotNull(element);
		assertEquals("age", element.getLocalPart());
		assertEquals(targetNs, element.getNamespace());
		assertEquals("integer", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// Element datebron in targetNs must exist, must be type date from
		// schemaNs
		element = this.elements.get(new QName(targetNs, "dateborn"));
		assertNotNull(element);
		assertEquals("dateborn", element.getLocalPart());
		assertEquals(targetNs, element.getNamespace());
		assertEquals("date", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());
	}

	@Test
	public void testComplexEmptyElements() throws Exception {
		this.parser.parse(this.getFile("complexEmptyElements.xsd"));

		// there have to be 1 simpleType
		assertEquals(1, this.simpleTypes.size());
		this.checkSimpleTypesFor(new String[] { "positiveInteger" });

		// there has to be 1 Elements
		assertEquals(1, this.elements.size());

		// There has to be 1 ComplexType
		assertEquals(1, this.complexTypes.size());

		// Element product in targetNs must exist
		Element element = this.elements.get(new QName(targetNs, "product"));
		assertNotNull(element);

		// Type must be ProductType in targetNs
		ComplexType complex = (ComplexType) element.getType();
		assertEquals("ProductType", complex.getLocalPart());
		assertEquals(targetNs, complex.getNamespace());
		assertSame(this.complexTypes.get(complex.getQName()), complex);

		// Type must have one attribute productId in targetNs, type of the
		// attribute must be positive Integer in schemaNs
		List<Attribute> attributes = complex.getAttributes();
		assertEquals(1, attributes.size());
		Attribute attribute = attributes.get(0);
		assertEquals("productId", attribute.getLocalPart());
		assertEquals(targetNs, attribute.getNamespace());
		assertEquals("positiveInteger", attribute.getType().getLocalPart());
		assertEquals(schemaNs, attribute.getType().getNamespace());

	}

	@Test
	public void testDefineComplexElements1() throws Exception {
		this.parser.parse(this.getFile("defineComplexElements1.xsd"));

		// there have to be 1 simpleType
		assertEquals(1, this.simpleTypes.size());
		this.checkSimpleTypesFor(new String[] { "string" });

		// there has to be 1 Elements
		assertEquals(1, this.elements.size());

		// There has to be 1 ComplexType
		assertEquals(1, this.complexTypes.size());

		// Element employee in targetNs must exist
		Element element = this.elements.get(new QName(targetNs, "employee"));
		assertNotNull(element);

		// Type has to be in targetNs. Because the complexType is nested, it
		// does not have any localPart
		ComplexType complex = (ComplexType) element.getType();
		assertNull(complex.getLocalPart());
		assertEquals(targetNs, complex.getNamespace());

		// complexType has to contain two element
		List<Element> nestedElements = complex.getElements();
		assertEquals(3, nestedElements.size());

		// Element firstname in targetNs must exist
		element = nestedElements.get(0);
		assertNotNull(element);
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// Element lastname in targetNs must exist
		element = nestedElements.get(1);
		assertNotNull(element);
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// Element address in targetNs must exist
		element = nestedElements.get(2);
		assertNotNull(element);
		assertEquals("AddressType", element.getType().getLocalPart());
		assertEquals(targetNs, element.getType().getNamespace());

		// complexType of address must be the same complexType like the
		// AddressType in the complexTypes
		complex = this.complexTypes.get(new QName(targetNs, "AddressType"));
		assertSame(complex, element.getType());

		// AddressType must be in targetNs, must contain two elements and no
		// attributes
		assertEquals(targetNs, complex.getNamespace());
		assertEquals("AddressType", complex.getLocalPart());
		assertEquals(0, complex.getAttributes().size());
		assertEquals(2, complex.getElements().size());

		// AddressType must contain a element named zip which type is string in
		// schemaNs
		element = complex.getElements().get(0);
		assertNotNull(element);
		assertEquals(targetNs, element.getNamespace());
		assertEquals("zip", element.getLocalPart());
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// AddressType must contain a element named city which type is string in
		// schemaNs
		element = complex.getElements().get(1);
		assertNotNull(element);
		assertEquals(targetNs, element.getNamespace());
		assertEquals("city", element.getLocalPart());
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

	}

	@Test
	public void testDefineComplexElements2() throws Exception {
		this.parser.parse(this.getFile("defineComplexElements2.xsd"));

		// there have to be 1 simpleType
		assertEquals(1, this.simpleTypes.size());
		this.checkSimpleTypesFor(new String[] { "string" });

		// there has to be 1 Elements
		assertEquals(1, this.elements.size());

		// There has to be no ComplexTypes
		assertTrue(this.complexTypes.isEmpty());

		// Element employee in targetNs must exist
		Element element = this.elements.get(new QName(targetNs, "employee"));
		assertNotNull(element);

		// Type has to be in targetNs. Because the complexType is nested, it
		// does not have any localPart
		ComplexType complex = (ComplexType) element.getType();
		assertNull(complex.getLocalPart());
		assertEquals(targetNs, complex.getNamespace());

		// complexType has to contain two element
		List<Element> nestedElements = complex.getElements();
		assertEquals(3, nestedElements.size());

		// Element firstname in targetNs must exist
		element = nestedElements.get(0);
		assertNotNull(element);
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// Element lastname in targetNs must exist
		element = nestedElements.get(1);
		assertNotNull(element);
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// Element address in targetNs must exist
		element = nestedElements.get(2);
		assertNotNull(element);
		complex = (ComplexType) element.getType();

		// complex must be in targetNs, must contain two elements and no
		// attributes
		assertEquals(targetNs, complex.getNamespace());
		assertEquals(0, complex.getAttributes().size());
		assertEquals(2, complex.getElements().size());

		// AddressType must contain a element named zip which type is string in
		// schemaNs
		element = complex.getElements().get(0);
		assertNotNull(element);
		assertEquals(targetNs, element.getNamespace());
		assertEquals("zip", element.getLocalPart());
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

		// AddressType must contain a element named city which type is string in
		// schemaNs
		element = complex.getElements().get(1);
		assertNotNull(element);
		assertEquals(targetNs, element.getNamespace());
		assertEquals("city", element.getLocalPart());
		assertEquals("string", element.getType().getLocalPart());
		assertEquals(schemaNs, element.getType().getNamespace());

	}

	@Test
	public void testDefineComplexElements3() throws Exception {
		this.parser.parse(this.getFile("defineComplexElements3.xsd"));

		// there have to be 1 simpleType
		assertEquals(1, this.simpleTypes.size());
		SimpleType stringType = this.simpleTypes.get(new QName(schemaNs, "string"));
		assertEquals("string", stringType.getLocalPart());
		assertEquals(schemaNs, stringType.getNamespace());

		// there have to be 2 complexTypes
		assertEquals(2, this.complexTypes.size());

		// check ComplexType LocationType
		ComplexType locationType = this.complexTypes.get(new QName(targetNs, "LocationType"));
		assertEquals("LocationType", locationType.getLocalPart());
		assertEquals(targetNs, locationType.getNamespace());
		assertEquals(0, locationType.getAttributes().size());
		assertEquals(2, locationType.getElements().size());

		// check nested element city of LocationType
		Element nested = locationType.getElements().get(0);
		assertSame(stringType, nested.getType());
		assertEquals(targetNs, nested.getNamespace());
		assertEquals("city", nested.getLocalPart());

		// check nested element zip of LocationType
		nested = locationType.getElements().get(1);
		assertSame(stringType, nested.getType());
		assertEquals(targetNs, nested.getNamespace());
		assertEquals("zip", nested.getLocalPart());

		// check ComplexType PersonType
		ComplexType personType = this.complexTypes.get(new QName(targetNs, "PersonType"));
		assertEquals("PersonType", personType.getLocalPart());
		assertEquals(targetNs, locationType.getNamespace());

		// check attributes of PersonType
		assertEquals(2, personType.getAttributes().size());
		Attribute attribute = this.findAttribute(personType.getAttributes(), targetNs, "personId");
		assertSame(stringType, attribute.getType());
		assertEquals("personId", attribute.getLocalPart());
		assertEquals(targetNs, attribute.getNamespace());
		assertNull(attribute.getFixedValue());
		assertEquals("-1", attribute.getDefaultValue());
		attribute = this.findAttribute(personType.getAttributes(), targetNs, "lang");
		assertSame(stringType, attribute.getType());
		assertEquals("lang", attribute.getLocalPart());
		assertEquals(targetNs, attribute.getNamespace());
		assertEquals("DE", attribute.getFixedValue());

		// check nested element firstname of PersonType
		nested = personType.getElements().get(0);
		assertSame(stringType, nested.getType());
		assertEquals(targetNs, nested.getNamespace());
		assertEquals("firstname", nested.getLocalPart());
		assertEquals(1, nested.getMinOccurs());
		assertEquals(2, nested.getMaxOccurs());
		assertFalse(nested.isNillable());

		// check nested element lastname of PersonType
		nested = personType.getElements().get(1);
		assertSame(stringType, nested.getType());
		assertEquals(targetNs, nested.getNamespace());
		assertEquals("lastname", nested.getLocalPart());
		assertEquals(1, nested.getMinOccurs());
		assertEquals(1, nested.getMaxOccurs());
		assertTrue(nested.isNillable());

		// check nested element location of PersonType
		nested = personType.getElements().get(2);
		assertSame(locationType, nested.getType());
		assertEquals(targetNs, nested.getNamespace());
		assertEquals("location", nested.getLocalPart());
		assertEquals(2, nested.getMinOccurs());
		assertEquals(0, nested.getMaxOccurs());
		assertFalse(nested.isNillable());

		// there have to be 3 elements
		assertEquals(3, this.elements.size());
		Element element = this.elements.get(new QName(targetNs, "employee"));
		assertSame(personType, element.getType());
		assertEquals(targetNs, element.getNamespace());
		assertEquals("employee", element.getLocalPart());
		element = this.elements.get(new QName(targetNs, "student"));
		assertSame(personType, element.getType());
		assertEquals(targetNs, element.getNamespace());
		assertEquals("student", element.getLocalPart());
		element = this.elements.get(new QName(targetNs, "member"));
		assertSame(personType, element.getType());
		assertEquals(targetNs, element.getNamespace());
		assertEquals("member", element.getLocalPart());

	}

	/**
	 * Check that anonymous inner simple types do not crash the parser and that
	 * they work correctly.
	 */
	@Test
	public void testDefineComplexElements4_NestedSimpleTypes() throws Exception {
		this.parser.parse(this.getFile("defineComplexElements4.xsd"));

		assertEquals(2, this.simpleTypes.size());
		assertEquals(1, this.complexTypes.size());
		assertEquals(2, this.elements.size());

		Element element = this.elements.get(new QName(targetNs, "itinerary"));
		ComplexType type = element.getType().getAsComplexType();
		assertNull(type.getLocalPart());

		List<Attribute> attrs = type.getAttributes();
		assertEquals(1, attrs.size());
		Attribute colorAttr = attrs.get(0);
		assertEquals("color", colorAttr.getLocalPart());

		SimpleType attrType = colorAttr.getType();
		assertNull(attrType.getLocalPart());
	}

	/**
	 * Check that elements in nested model groups are collected into a single
	 * list. We might not have explicit support for the <xsd:choice> and <xsd:all>
	 * model groups, but it's useful to consider them as <xsd:sequence> for the
	 * time being. This way, we'll be able to generate sample (invalid) messages,
	 * and the user will just need to remember the extra constraints to be met.
	 */
	@Test
	public void testDefineComplexElements4_NestedModelGroups() throws Exception {
		this.parser.parse(this.getFile("defineComplexElements4.xsd"));

		Element element = this.elements.get(new QName(targetNs, "results"));
		ComplexType type = element.getType().getAsComplexType();
		assertNull(type.getLocalPart());

		List<Element> elements = type.getElements();
		String[] localParts = new String[]{"result", "count", "empty"};
		QName[] nestedTypeName = new QName[]{new QName(XSD_NAMESPACE, "string"),
				new QName(XSD_NAMESPACE, "nonNegativeInteger"),
				new QName(XSD_NAMESPACE, "anyType")
		};
		assertEquals(localParts.length, elements.size());

		for (int i = 0; i < elements.size(); ++i) {
			Element nestedElement = elements.get(i);
			assertEquals(localParts[i], nestedElement.getLocalPart());

			Type nestedType = nestedElement.getType();
			assertEquals(nestedTypeName[i], nestedType.getQName());
		}
	}

	private Attribute findAttribute(List<Attribute> e, String namespace, String name) {
		for (Attribute attribute : e) {
			if (namespace.equals(attribute.getNamespace())
					&& name.equals(attribute.getLocalPart())) {
				return attribute;
			}
		}
		return null;
	}

	private File getFile(String filename) {
		return new File(this.schemaFileFolder + File.separator + filename);
	}

	private void checkSimpleTypesFor(String[] types) {
		for (String type : types) {
			assertNotNull(this.simpleTypes.get(new QName(schemaNs, type)));
		}

	}
}
