package net.bpelunit.toolsupport.util.schema;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Types;
import javax.wsdl.extensions.schema.Schema;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import net.bpelunit.toolsupport.util.WSDLReadingException;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import net.bpelunit.toolsupport.util.schema.nodes.Type;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.util.DomAnnotationParserFactory;

/**
 * Parses all schemata in the Definition of a WSDL and manages the Elements and
 * Types of the schemata. Connects the operations of the service with the input,
 * fault and output messages.
 * 
 * Antonio: fix relative URI imports, generate messages for faults and rpc/lit
 * and improve error reporting.
 *
 * @author cvolhard, Antonio García-Domínguez
 * 
 */
public class WSDLParser {

	private Definition definition;
	private SchemaElementManager schemaManager;

	/**
	 * Creates the WSDL-Parser for <code>definition</code>. Parses all the
	 * Schemata using a {@link SchemaParser}.
	 * 
	 * @param definition
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws WSDLReadingException
	 */
	public WSDLParser(Definition definition) throws SAXException, TransformerException,
			WSDLReadingException {
		this.definition = definition;
		this.readSchemata();
	}

	/**
	 * Returns a HashMap containing all ComplexTypes from all Schemata. The
	 * QNames are used as key.
	 * 
	 * @see SchemaElementManager#getComplexTypes()
	 * @return
	 */
	HashMap<QName, ComplexType> getComplexTypes() {
		return this.schemaManager.getComplexTypes();
	}

	/**
	 * Returns a HashMap containing all SimpleTypes from all Schemata. The
	 * QNames are used as key.
	 * 
	 * @see SchemaElementManager#getSimpleTypes()
	 * @return
	 */
	HashMap<QName, SimpleType> getSimpleTypes() {
		return this.schemaManager.getSimpleTypes();
	}

	/**
	 * Returns a HashMap containing all root Elements from all Schemata. The
	 * QNames are used as key.
	 * 
	 * @see SchemaElementManager#getElements()
	 * @return
	 */
	HashMap<QName, Element> getElements() {
		return this.schemaManager.getElements();
	}

	/**
	 * Reads all Schemata contained in the Definition.
	 * 
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws WSDLReadingException
	 */
	@SuppressWarnings("unchecked")
	private void readSchemata() throws SAXException, TransformerException, WSDLReadingException {
		SchemaParser parser = new SchemaParser();
		this.schemaManager = parser.getSchemaElementManager();

		XSOMParser reader = new XSOMParser();
		reader.setErrorHandler(new ErrorAdapter());
		reader.setAnnotationParser(new DomAnnotationParserFactory());

		// We need to collect the schemata from all WSDL imports. It might be
		// that some of the imported WSDL elements refer to something in their
		// internal schemas. If we don't import it, we'll have missing types in
		// our internal schemata, and the tree editor won't work.
		List<Schema> schemata = new ArrayList<Schema>();
		WSDLParser.collectSchemata(schemata, this.definition);
		for (List<Import> importsByNamespace : ((Map<String, List<Import>>) this.definition
				.getImports()).values()) {
			for (Import importDef : importsByNamespace) {
				WSDLParser.collectSchemata(schemata, importDef.getDefinition());
			}
		}

		// loop through the collected schemata
		for (Schema schema : schemata) {
			org.w3c.dom.Element schemaElement = schema.getElement();

			/*
			 * We need to set a system ID or importing .xsd files with relative
			 * paths won't work. See this mailing list thread at the XSOM
			 * website:
			 *
			 * https://xsom.dev.java.net/servlets/ReadMsg?list=users&msgNo=159
			 *
			 * All relative URIs will use the WSDL URI as their base.
			 */
			StringReader sReader = this.getStringReaderFromElement(schemaElement);
			InputSource source = new InputSource(sReader);
			source.setSystemId(this.definition.getDocumentBaseURI());
			reader.parse(source);

			try {
				parser.readSchemata(reader.getResult());
			} catch (NullPointerException e) {
				throw new WSDLReadingException("Corrupt Schema in WSDL", e);
			} catch (Throwable e) {
				// error occured, needed schmeta could be read, continue
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void collectSchemata(List<Schema> schemata, Definition def) {
		Types typesSection = def.getTypes();
		if (typesSection == null)
			return;

		for (Object o : typesSection.getExtensibilityElements()) {
			if (!(o instanceof Schema))
				continue;

			// Import all spaces from the schema's WSDL definition into the
			// schema element, for later parsing by XSOM
			Schema schema = (Schema) o;
			org.w3c.dom.Element element = schema.getElement();
			WSDLParser.addNamespaces(def.getNamespaces(), element);

			schemata.add(schema);
		}
	}

	/**
	 * Creates a StringReader for the passed Element.
	 * 
	 * @param element
	 * @return
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	private StringReader getStringReaderFromElement(org.w3c.dom.Element element)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException,
			TransformerException {
		DOMSource source = new DOMSource(element);
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.transform(source, result);
		StringReader stringReader = new StringReader(stringWriter.getBuffer().toString());
		return stringReader;
	}

	/**
	 * Adds every namespace in <code>namespaces</code> to the
	 * <code>schemaElement</code>, if a namespace with this prefix not already
	 * exists.
	 * 
	 * @param namespaces
	 *            namespaces from the definitions-Tag of the WSDL.
	 * @param schemaElement
	 *            representing on schema element of the WSDL
	 */
	private static void addNamespaces(Map<String, String> namespaces,
			org.w3c.dom.Element schemaElement) {
		for (Entry<String, String> entry : namespaces.entrySet()) {
				String attribute = "xmlns:" + (String) entry.getKey();
				if (schemaElement.getAttributeNode(attribute) == null) {
					schemaElement.setAttribute(attribute, (String) entry.getValue());
				}
		}
	}

	/**
	 * Returns an XML Element for the input of the operation.
	 *
	 * For document/literal bindings, returns the Element of the first part of
	 * the output message of the operation identified by <code>service</code>,
	 * <code>port</code> and <code>operationName</code>.
	 * 
	 * For RPC/literal bindings, returns an Element. Its namespace URI is equal
	 * to the value of the soap:body element of the binding, or the target
	 * namespace for the service if it isn't specified (this last option is
	 * for WSDL files which do not comply to the WS-I Basic Profile). It has as
	 * many children as there are parts in the message. Their types are those
	 * indicated in the type attribute of their corresponding part.
	 *
	 * @param service
	 *            QName of the service
	 * @param port
	 *            name of the port
	 * @param operationName
	 *            name of the operation
	 * @return
	 * @throws InvalidInputException
	 * @throws NoSuchOperationException
	 * @throws NoElementDefinitionExistsException
	 * @see #getOutputElementForOperation(QName, String, String)
	 */
	public Element getInputElementForOperation(QName service, String port, String operationName)
			throws InvalidInputException, NoSuchOperationException, NoElementDefinitionExistsException {
		return this.getElementForOperation(service, port, operationName, null, SOAPOperationDirectionIdentifier.INPUT);
	}

	/**
	 * Returns an XML Element for the output of the operation.
	 *
	 * For document/literal bindings, returns the Element of the first part of
	 * the output message of the operation identified by <code>service</code>,
	 * <code>port</code> and <code>operationName</code>.
	 * 
	 * For RPC/literal bindings, returns an Element. Its namespace URI is equal
	 * to the value of the soap:body element of the binding, or the target
	 * namespace for the service if it isn't specified (this last option is
	 * for WSDL files which do not comply to the WS-I Basic Profile). It has as
	 * many children as there are parts in the message. Their types are those
	 * indicated in the type attribute of their corresponding part.
	 *
	 * @param service
	 *            QName of the service
	 * @param port
	 *            name of the port
	 * @param operationName
	 *            name of the operation
	 * @return
	 * @throws InvalidInputException
	 * @throws NoSuchOperationException
	 * @throws NoElementDefinitionExistsException
	 * @see #getInputElementForOperation(QName, String, String)
	 */
	public Element getOutputElementForOperation(QName service, String port, String operationName)
			throws InvalidInputException, NoSuchOperationException, NoElementDefinitionExistsException {
		return this.getElementForOperation(service, port, operationName, null, SOAPOperationDirectionIdentifier.OUTPUT);
	}

	/**
	 * Returns an XML Element for a specific fault of the operation.
	 *
	 * Results will be the same both for document/literal and RPC/literal
	 * bindings: fault messages always use the document/literal style, according
	 * to WS-I Basic Profile 1.1, section 4.4.2.
	 *
	 * @param service
	 *            QName of the service
	 * @param port
	 *            name of the port
	 * @param operationName
	 *            name of the operation
	 * @param faultName
	 *            name of the fault
	 * @return
	 * @throws InvalidInputException
	 * @throws NoSuchOperationException
	 * @throws NoElementDefinitionExistsException
	 */
	public Element getFaultElementForOperation(QName service, String port, String operationName, String faultName)
			throws InvalidInputException, NoSuchOperationException, NoElementDefinitionExistsException {
		return getElementForOperation(service, port, operationName, faultName, SOAPOperationDirectionIdentifier.FAULT);
	}

	private Element getElementForOperation(QName service, String port, String operationName, String faultName, SOAPOperationDirectionIdentifier direction)
			throws InvalidInputException, NoSuchOperationException, NoElementDefinitionExistsException {

		// Create the identifier, for querying additional required info
		SOAPOperationCallIdentifier opIdentifier;
		Operation operation;
		try {
			opIdentifier = new SOAPOperationCallIdentifier(this.definition,
					service, port, operationName, direction);
			operation = opIdentifier.getBindingOperation().getOperation();
		} catch (SpecificationException e) {
			throw new NoSuchOperationException(e);
		}

		// Query the style (rpc/lit, doc/lit ...)
		String opStyle;
		try {
			opStyle = opIdentifier.getEncodingStyle();
		} catch (SpecificationException e) {
			throw new InvalidInputException(e);
		}

		Message msg = getMessageFromOperation(faultName, direction, operation);

		if ("document/literal".equals(opStyle)) {
			return getElementForDocLitMessage(msg);
		}
		else if ("rpc/literal".equals(opStyle)) {
			String bodyNamespace = opIdentifier.getBodyNamespace();
			return getElementForRpcLitMessage(bodyNamespace, operation.getName(), msg);
		}
		else {
			throw new InvalidInputException(
					"Unknown combination of operation style and soap:body use: "
					+ opStyle);
		}
	}

	private Message getMessageFromOperation(String faultName,
			SOAPOperationDirectionIdentifier direction, Operation operation)
			throws InvalidInputException, NoElementDefinitionExistsException {
		switch (direction) {
		case OUTPUT:
			Output output = operation.getOutput();
			if (output == null)
				throw new InvalidInputException("Selected operation has no output");
			return output.getMessage();
		case INPUT:
			Input input = operation.getInput();
			if (input == null)
				throw new InvalidInputException("Selected operation has no input");
			return input.getMessage();
		case FAULT:
			// Empty fault name -> custom fault, which has no schema attached
			// to it. Warn the user that no schema is available for that case.
			if (faultName == null)
				throw new NoElementDefinitionExistsException(
						"Custom fault has no schema attached to it");

			Fault fault = operation.getFault(faultName);
			if (fault == null)
				throw new InvalidInputException(
						"Selected operation has no fault called " + faultName);
			return fault.getMessage();
		default:
			throw new InvalidInputException("Unknown direction type");
		}
	}

	private Element getElementForRpcLitMessage(
			String bodyNamespace, String operationName, Message msg)
			throws InvalidInputException {
		QName wrapperName = new QName(bodyNamespace, operationName);

		// Check if the element has been already created. If so, just return it.
		Map<QName, Element> elements = schemaManager.getElements();
		if (elements.containsKey(wrapperName)) {
			return elements.get(wrapperName);
		}

		// Create the complex type for the wrapper
		ComplexType wrapperType = schemaManager.getComplexType(
				bodyNamespace, operationName + "WrapperElementType");
		addRPCPartElements(bodyNamespace, msg, wrapperType);

		// Create the wrapper element itself
		Element wrapperElement = schemaManager.getElement(wrapperName);
		wrapperElement.setType(wrapperType);

		return wrapperElement;
	}

	@SuppressWarnings("unchecked")
	private void addRPCPartElements(String bodyNamespace, Message msg, ComplexType wrapperType)
			throws InvalidInputException {
		Map<QName, ComplexType> complexTypes = schemaManager.getComplexTypes();
		Map<QName, SimpleType> simpleTypes = schemaManager.getSimpleTypes();

		Collection<Part> parts = msg.getOrderedParts(null);
		for (Part part : parts) {
			QName typeName = part.getTypeName();
			Type type;
			if (typeName == null) {
				throw new InvalidInputException(
					"Style is rpc/lit, but the part "
						+ part.getName() + " uses the element attribute");
			}
			else if (complexTypes.containsKey(typeName)) {
				type = complexTypes.get(typeName);
			}
			else if (simpleTypes.containsKey(typeName)) {
				type = simpleTypes.get(typeName);
			}
			else if (SchemaNode.XML_SCHEMA_NAMESPACE.equals(typeName.getNamespaceURI())) {
				type = schemaManager.getSimpleType(typeName.getLocalPart());
			}
			else {
				throw new InvalidInputException("Could not find the type " + typeName);
			}

			Element partElement = schemaManager.getElement(bodyNamespace, part.getName());
			partElement.setType(type);
			wrapperType.addElement(partElement);
		}
	}

	@SuppressWarnings("unchecked")
	private Element getElementForDocLitMessage(Message msg)
		throws InvalidInputException, NoElementDefinitionExistsException {
		Collection<Part> parts = msg.getParts().values();
		if (parts.isEmpty()) {
			throw new NoElementDefinitionExistsException("Message has no parts");
		}
		Part part = (Part) parts.iterator().next();
		QName elementName = part.getElementName();

		// The message part was wrongly declared with the type attribute, so
		// there's no element name. Avoid throwing a NullPointerException by
		// just returning null here.
		if (elementName == null)
			throw new InvalidInputException(
				"Style is doc/lit, but the part "
					+ part.getName() + " uses the type attribute");

		return schemaManager.getElement(elementName);
	}

}

