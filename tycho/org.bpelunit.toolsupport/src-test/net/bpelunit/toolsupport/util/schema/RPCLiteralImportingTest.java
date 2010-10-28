package net.bpelunit.toolsupport.util.schema;

import static org.junit.Assert.*;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import net.bpelunit.toolsupport.util.schema.nodes.Element;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for WSDL definitions that pretty much only import a WSDL and add a
 * thing or two, like those produced by the Eclipse BPEL Designer. Those only
 * usually add a partner link or two.
 *
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class RPCLiteralImportingTest {

	private static final String WSDL_PATH = "testSchemata/rpcLiteralTestImporting.wsdl";
	private WSDLParser fParser;

	@Before
	public void setUp() throws Exception {
		WSDLFactory factory = WSDLFactory.newInstance();
		WSDLReader reader = factory.newWSDLReader();
		Definition definition = reader.readWSDL(null, WSDL_PATH);
		fParser = new WSDLParser(definition);
	}

	/**
	 * Checks that the namespace declarations which are brought over to the
	 * schemata in an imported WSDL definition are those from the imported
	 * WSDL definition itself, and not the main WSDL definition's.
	 */
	@Test
	public void importedDefinitionNamespacesAreInheritedCorrectly() throws Exception {
		Element elem = fParser.getInputElementForOperation(RPCLiteralTest.WSDL_SERVICE_QNAME,
				RPCLiteralTest.WSDL_PORT, RPCLiteralTest.WSDL_OPERATION);
		assertNotNull(elem);
	}
}
