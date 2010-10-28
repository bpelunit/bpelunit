package net.bpelunit.toolsupport.util.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import org.junit.Before;
import org.junit.Test;

public class WSDLParserTest {
	private final String behemothNs = "http://behemoth.service.suppliersguild.org";
	private final String commonNs = "http://common.types.suppliersguild.org/xsd";
	private final String uebung9Ns = "http://soa08.se.uni-hannover.de/uebung09";
	private WSDLParser parser;
	private HashMap<QName, ComplexType> complexTypes;
	private HashMap<QName, SimpleType> simpleTypes;
	private HashMap<QName, Element> elements;

	@Before
	public void setUp() throws Exception {
		WSDLFactory factory = WSDLFactory.newInstance();
		WSDLReader reader = factory.newWSDLReader();
		Definition definition = reader.readWSDL(null, "testSchemata/OrderProcess.wsdl");
		this.parser = new WSDLParser(definition);
		this.complexTypes = this.parser.getComplexTypes();
		this.simpleTypes = this.parser.getSimpleTypes();
		this.elements = this.parser.getElements();
	}

	@Test
	public void testParseDefinition() throws Exception {
		assertEquals(5, this.elements.size());
		assertEquals(5, this.complexTypes.size());
		assertEquals(4, this.simpleTypes.size());

		// Because the complexTypes do not refer to other schemata, check only
		// if they exist
		ComplexType product = this.complexTypes.get(new QName(this.commonNs, "Product"));
		assertNotNull(product);
		ComplexType amount = this.complexTypes.get(new QName(this.commonNs, "Amount"));
		assertNotNull(amount);
		ComplexType unit = this.complexTypes.get(new QName(this.commonNs, "Unit"));
		assertNotNull(unit);
		ComplexType money = this.complexTypes.get(new QName(this.commonNs, "Money"));
		assertNotNull(money);
		ComplexType currency = this.complexTypes.get(new QName(this.commonNs, "Currency"));
		assertNotNull(currency);

		// The elements orderProductResponse do not refer to other schemata,
		// check only if they exist
		Element orderProductRes = this.elements.get(new QName(this.behemothNs,
				"orderProductResponse"));
		assertNotNull(orderProductRes);
		orderProductRes = this.elements.get(new QName(this.uebung9Ns, "orderProductResponse"));
		assertNotNull(orderProductRes);

		// The other elements contains complexTypes from a different schema,
		// check if the linking is correct
		Element orderProduct = this.elements.get(new QName(this.behemothNs, "orderProduct"));
		assertNotNull(orderProduct);
		List<Element> nested = ((ComplexType) orderProduct.getType()).getElements();
		assertSame(product, nested.get(0).getType());
		assertSame(amount, nested.get(1).getType());

		Element getPriceForProduct = this.elements.get(new QName(this.behemothNs,
				"getPriceForProduct"));
		assertNotNull(getPriceForProduct);
		nested = ((ComplexType) getPriceForProduct.getType()).getElements();
		assertSame(product, nested.get(0).getType());

		Element getPriceForProductRes = this.elements.get(new QName(this.behemothNs,
				"getPriceForProductResponse"));
		assertNotNull(getPriceForProductRes);
		nested = ((ComplexType) getPriceForProductRes.getType()).getElements();
		assertSame(money, nested.get(0).getType());

		// Check if the correct input element for a given operation is returned
		Element input = this.parser.getInputElementForOperation(new QName(this.uebung9Ns,
				"OrderProcess"), "OrderProcessSOAP11port_http", "orderProduct");
		assertSame(orderProduct, input);
		input = this.parser.getInputElementForOperation(new QName(this.uebung9Ns, "OrderProcess"),
				"OrderProcessSOAP11port_http", "getPriceForProduct");
		assertSame(getPriceForProduct, input);

		Element output = this.parser.getOutputElementForOperation(new QName(this.uebung9Ns,
				"OrderProcess"), "OrderProcessSOAP11port_http", "orderProduct");
		assertSame(orderProductRes, output);
		output = this.parser.getOutputElementForOperation(
				new QName(this.uebung9Ns, "OrderProcess"), "OrderProcessSOAP11port_http",
				"getPriceForProduct");
		assertSame(getPriceForProductRes, output);
	}
}
