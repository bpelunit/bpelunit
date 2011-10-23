package net.bpelunit.toolsupport.util.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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

/**
 * Test cases for an RPC/literal operation. Make sure the elements serialize
 * all parts correctly and in the right order.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class RPCLiteralTest {

	private static final String WSDL_OPERATION_ONLYINPUT = "elemAttrInRpcLitOp";
	private static final String WSDL_IMPORT_NAMESPACE = "http://www.example.org/rpcLiteralTestImported/";
	private static final String WSDL_PATH = "testSchemata/rpcLiteralTest.wsdl";
	private static final String WSDL_TYPES_NAMESPACE = "http://www.example.org/rpcLiteralTest/Types";
	private static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	static final String WSDL_OPERATION = "doSomething";
	static final String WSDL_PORT = "rpcLiteralTestSOAP";
	static final String WSDL_SERVICE_NAME = "rpcLiteralTest";
	static final String WSDL_SERVICE_NAMESPACE = "http://www.example.org/rpcLiteralTest/";
	static final String WSDL_INPUT_NAMESPACE = WSDL_SERVICE_NAMESPACE + "Input";
	static final QName WSDL_SERVICE_QNAME = new QName(WSDL_SERVICE_NAMESPACE, WSDL_SERVICE_NAME);
	private WSDLParser fParser;

	@Before
	public void setUp() throws Exception {
		WSDLFactory factory = WSDLFactory.newInstance();
		WSDLReader reader = factory.newWSDLReader();
		Definition definition = reader.readWSDL(null, WSDL_PATH);
		this.fParser = new WSDLParser(definition);
	}

	@Test
	public void generatedInputElementIsCorrect() throws Exception {
		Element wrapperElement = fParser.getInputElementForOperation(WSDL_SERVICE_QNAME,
				WSDL_PORT, WSDL_OPERATION);

		// The WSDL has been designed to use different namespaces for the
		// service, its soap:body binding and some of its types, to handle
		// every case. This is to ensure compliance of the WS-I Basic Profile
		// 1.1, particularly its section 4.7.10. See:
		//
		// http://www.ws-i.org/Profiles/BasicProfile-1.1.html#Namespaces_for_soapbind_Elements

		// Check the name of the wrapper element
		assertEquals("main element should be the usual RPC wrapper", new QName(
				WSDL_INPUT_NAMESPACE, WSDL_OPERATION), wrapperElement.getQName());

		// Check the contents of the wrapper element
		ComplexType wrapperType = wrapperElement.getType().getAsComplexType();
		assertNotNull("main element should have complex content", wrapperType);

		// Check the elements which serialized each part, and make sure they're
		// in the right order (see WS-I Basic 1.1, section 4.5.1)
		String[] expectedLocalParts = new String[] { "description", "amount", "currentDate",
				"fingers" };
		QName[] expectedTypes = new QName[] { new QName(XSD_NAMESPACE, "string"),
				new QName(XSD_NAMESPACE, "float"), new QName(XSD_NAMESPACE, "dateTime"),
				new QName(WSDL_TYPES_NAMESPACE, "numberOfFingers") };
		List<Element> wrapperPartElements = wrapperType.getElements();
		assertEquals("there should be as many child elements as parts in the message",
				expectedTypes.length, wrapperPartElements.size());
		for (int iElement = 0; iElement < wrapperPartElements.size(); ++iElement) {
			Element partElement = wrapperPartElements.get(iElement);

			// Check the name of the part element
			assertEquals("elements should be in the soap:body binding's namespace",
					WSDL_INPUT_NAMESPACE, partElement.getNamespace());
			assertEquals("element names should match the parts in the WSDL",
					expectedLocalParts[iElement], partElement.getLocalPart());

			SimpleType partType = partElement.getType().getAsSimpleType();
			assertNotNull("part " + expectedLocalParts[iElement] + " should have simple content",
					partType);

			assertEquals("part " + expectedLocalParts[iElement] + " should have the right type",
					expectedTypes[iElement], partType.getQName());
		}
	}

	/**
	 * This test checks that the generator can cope with operations that do not
	 * fully follow the WS-I Basic Profile 1.1, section 4.7.10, restriction R2717.
	 * The soap:body element for the <output> doesn't have a namespace attribute
	 * in this case.
	 */
	@Test
	public void generatedOutputElementIsCorrect() throws Exception {
		Element wrapperElement = fParser.getOutputElementForOperation(
				WSDL_SERVICE_QNAME, WSDL_PORT, WSDL_OPERATION);
		assertEquals("wrapper element should have the right name",
				new QName(WSDL_SERVICE_NAMESPACE, WSDL_OPERATION),
				wrapperElement.getQName());

		ComplexType wrapperType = wrapperElement.getType().getAsComplexType();
		assertNotNull("wrapper element should have complex content", wrapperType);

		List<Element> elements = wrapperType.getElements();
		assertEquals("wrapper element should have as many children as parts in the message",
				1, elements.size());

		Element partElement = elements.get(0);
		assertEquals("part element should use the service NS and part name for its QName",
				new QName(WSDL_SERVICE_NAMESPACE, "result"), partElement.getQName());

		SimpleType partType = partElement.getType().getAsSimpleType();
		assertNotNull("part type should have simple content", partType);
		assertEquals("part type should be the right one",
				new QName(XSD_NAMESPACE, "string"),
				partType.getQName());
	}

	/**
	 * This test checks that the generator produces fault messages properly.
	 * In addition to the usual constraints, the generator should take into
	 * account that all fault messages will use the document style, as they
	 * cannot have parameters. This is according to WS-I Basic Profile 1.1,
	 * section 4.4.2.
	 *
	 * There's actually two tests like this: it's important to make sure that
	 * the code really uses the faultName parameter.
	 */
	@Test
	public void generatedProcessingErrorFaultElementIsCorrect() throws Exception {
		Element docElement = fParser.getFaultElementForOperation(
				WSDL_SERVICE_QNAME, WSDL_PORT, WSDL_OPERATION, "processingError");
		assertEquals("root element in the message should be the element in the WSDL",
				new QName(WSDL_TYPES_NAMESPACE, "errorCode"),
				docElement.getQName());

		SimpleType type = docElement.getType().getAsSimpleType();
		assertNotNull("root element should have simple content", type);
		assertEquals("root element should have the right content type",
				new QName(XSD_NAMESPACE, "int"),
				type.getQName());
	}

	@Test
	public void generatedInvalidQueryFaultElementIsCorrect() throws Exception {
		Element docElement = fParser.getFaultElementForOperation(
				WSDL_SERVICE_QNAME, WSDL_PORT, WSDL_OPERATION, "invalidQuery");
		assertEquals("root element in the message should be the element in the WSDL",
				new QName(WSDL_TYPES_NAMESPACE, "reason"),
				docElement.getQName());

		SimpleType type = docElement.getType().getAsSimpleType();
		assertNotNull("root element should have simple content", type);
		assertEquals("root element should have the right content type",
				new QName(XSD_NAMESPACE, "string"),
				type.getQName());
	}

	/**
	 * This test checks that faults which do not comply with the WS-I Basic
	 * Profile 1.1 restriction (section 4.4.2) that all faults should be defined
	 * on messages whose single part uses the element attribute are detected,
	 * throwing the right exception type. It's actually a more general issue:
	 * when using the doc/lit style, all parts should use the element attribute,
	 * and not the type attribute.
	 */
	@Test
	public void generatedFaultWithTypePartIsRejected() throws Exception {
		try {
			fParser.getFaultElementForOperation(
					WSDL_SERVICE_QNAME, WSDL_PORT, WSDL_OPERATION, "iambad");
			fail("Faults declared using the type attribute should be rejected");
		} catch (InvalidInputException ex) {}
	}

	/**
	 * This test checks that messages in the rpc/lit style which have parts
	 * declared using the element attribute (violating the WS-I Basic Profile
	 * 1.1, section 4.4.1, R2203) are detected, throwing the right exception
	 * type.
	 */
	@Test
	public void rpcLitMessagesWithElementPartAreRejected() throws Exception {
		try {
			fParser.getInputElementForOperation(
					WSDL_SERVICE_QNAME, WSDL_PORT, WSDL_OPERATION_ONLYINPUT);
			fail("rpc/lit messages with element parts should be rejected");
		} catch (InvalidInputException ex) {}
	}

	/**
	 * Check that messages imported from other WSDL files which use their own
	 * types are generated correctly. According to the WS-I Basic Profile 1.1,
	 * we can't access the elements of the schemata in an external WSDL by
	 * importing it, but we *can* use the WSDL stuff in there, which may
	 * reference its local types just fine.
	 */
	@Test
	public void generateImportedMsgWithInternalTypes() throws Exception {
		Element root = fParser.getFaultElementForOperation(
				WSDL_SERVICE_QNAME, WSDL_PORT, WSDL_OPERATION, "importTest");
		assertEquals("root element should have the right name",
				new QName(WSDL_IMPORT_NAMESPACE, "importTest"),
				root.getQName());

		SimpleType type = root.getType().getAsSimpleType();
		assertNotNull("root element should have simple content", type);
		assertEquals("root element should have the right content type",
				new QName(XSD_NAMESPACE, "int"), type.getQName());
	}

	/**
	 * Checks that we can ask for an element for a custom fault even in one-way
	 * operations. However, we'll get an exception saying there's no schema
	 * definition we can use to generate a sample XML fragment, as usual.
	 */
	@Test
	public void generateCustomFaultForOneWayOpProducesCorrectException() throws Exception {
		try {
			fParser.getFaultElementForOperation(WSDL_SERVICE_QNAME, WSDL_PORT,
					WSDL_OPERATION_ONLYINPUT, null);
			fail("Generating a custom fault should throw a 'no element definition' " +
					"exception even for operations without declared faults");
		} catch (NoElementDefinitionExistsException ex) {}
	}
}
