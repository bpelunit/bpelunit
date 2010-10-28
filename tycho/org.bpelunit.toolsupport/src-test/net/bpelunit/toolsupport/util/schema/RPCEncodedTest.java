package net.bpelunit.toolsupport.util.schema;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for a RPC/encoded operation. Basically, make sure we reject this
 * combination explicitly when generating sample XML fragments: for the time
 * being it won't be supported, as it's not WS-I compliant.
 *
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class RPCEncodedTest {

	private static final String WSDL_NAMESPACE = "http://www.example.org/rpcEncodedTest/";
	private static final String WSDL_OPERATION = "go";
	private static final String WSDL_PATH = "testSchemata/rpcEncodedTest.wsdl";
	private static final String WSDL_PORT = "doSomethingPort";
	private static final String WSDL_SERVICE = "doSomethingService";
	private WSDLParser fParser;

	@Before
	public void setUp() throws Exception {
		WSDLFactory factory = WSDLFactory.newInstance();
		WSDLReader reader = factory.newWSDLReader();
		Definition definition = reader.readWSDL(null, WSDL_PATH);
		fParser = new WSDLParser(definition);
	}

	@Test
	public void generateElementForRPCEncodedIsUnsupported() throws Exception {
		try {
			fParser.getOutputElementForOperation(
					new QName(WSDL_NAMESPACE,
					WSDL_SERVICE), WSDL_PORT, WSDL_OPERATION);
			fail("rpc/encoded combination should be rejected");
		} catch (InvalidInputException ex) {
			assertTrue("rpc/encoded combination should be rejected",
					ex.getMessage().startsWith("Unknown combination"));
			assertTrue("rpc/encoded should be mentioned in the error",
					ex.getMessage().contains("rpc/encoded"));
		}
	}
}
