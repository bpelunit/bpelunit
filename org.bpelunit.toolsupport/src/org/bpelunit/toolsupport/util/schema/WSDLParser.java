package org.bpelunit.toolsupport.util.schema;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Part;
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

import org.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import org.bpelunit.toolsupport.util.schema.nodes.Element;
import org.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.util.DomAnnotationParserFactory;

/**
 * Parses all schemata in the Definition of a WSDL and manages the Elements and
 * Types of the schemata. Connects the operations of the service with the input
 * and output messages.
 * 
 * @author cvolhard
 * 
 */
public class WSDLParser {
	private Definition definition;
	private HashMap<QName, ComplexType> complexTypes;
	private HashMap<QName, SimpleType> simpleTypes;
	private HashMap<QName, Element> elements;

	/**
	 * Creates the WSDL-Parser for <code>definition</code>. Parses all the
	 * Schemata using a {@link SchemaParser}.
	 * 
	 * @param definition
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public WSDLParser(Definition definition) throws SAXException,
			TransformerException {
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
		return this.complexTypes;
	}

	/**
	 * Returns a HashMap containing all SimpleTypes from all Schemata. The
	 * QNames are used as key.
	 * 
	 * @see SchemaElementManager#getSimpleTypes()
	 * @return
	 */
	HashMap<QName, SimpleType> getSimpleTypes() {
		return this.simpleTypes;
	}

	/**
	 * Returns a HashMap containing all root Elements from all Schemata. The
	 * QNames are used as key.
	 * 
	 * @see SchemaElementManager#getElements()
	 * @return
	 */
	HashMap<QName, Element> getElements() {
		return this.elements;
	}

	/**
	 * Reads all Schemata contained in the Definition.
	 * 
	 * @throws SAXException
	 * @throws TransformerException
	 */
	@SuppressWarnings("unchecked")
	private void readSchemata() throws SAXException, TransformerException {
		SchemaParser parser = new SchemaParser();
		this.complexTypes = parser.getComplexTypes();
		this.simpleTypes = parser.getSimpleTypes();
		this.elements = parser.getElements();

		XSOMParser reader = new XSOMParser();
		reader.setErrorHandler(new ErrorAdapter());
		reader.setAnnotationParser(new DomAnnotationParserFactory());

		Map<String, String> namespaces = this.definition.getNamespaces();
		for (Object tmp : this.definition.getTypes().getExtensibilityElements()) {
			if (tmp instanceof Schema) {
				Schema schema = (Schema) tmp;

				org.w3c.dom.Element schemaElement = schema.getElement();

				// inherit the namespaces from the definitions-tag
				this.addNamespaces(namespaces, schemaElement);

				StringReader stringReader = this
						.getStringReaderFromElement(schemaElement);
				reader.parse(stringReader);
				parser.readSchemas(reader.getResult());
			}
		}
	}

	private StringReader getStringReaderFromElement(
			org.w3c.dom.Element schemaElement)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		DOMSource source = new DOMSource(schemaElement);
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.transform(source, result);
		StringReader stringReader = new StringReader(stringWriter.getBuffer()
				.toString());
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
	@SuppressWarnings("unchecked")
	private void addNamespaces(Map<String, String> namespaces,
			org.w3c.dom.Element schemaElement) {
		for (Object o : namespaces.entrySet()) {
			if (o instanceof Entry) {
				Entry entry = (Entry) o;
				String attribute = "xmlns:" + (String) entry.getKey();
				if (schemaElement.getAttributeNode(attribute) == null) {
					schemaElement.setAttribute(attribute, (String) entry
							.getValue());
				}
			}
		}
	}

	/**
	 * Gets the Element of the first part of the input message of operation
	 * identified by <code>service</code>, <code>port</code> and
	 * <code>operationName</code> (first part because of the WS-I standard).
	 * 
	 * 
	 * @param service
	 *            QName of the service
	 * @param port
	 *            name of the port
	 * @param operationName
	 *            name of the operation
	 * @return
	 * @see #getOutputElementForOperation(QName, String, String)
	 */
	public Element getInputElementForOperation(QName service, String port,
			String operationName) {
		Operation operation = this.getOperation(service, port, operationName);
		if (operation == null) {
			return null;
		}
		Part part = (Part) operation.getInput().getMessage().getParts()
				.values().iterator().next();
		return this.elements.get(part.getElementName());
	}

	/**
	 * Returns the Operation identified by <code>service</code>,
	 * <code>port</code> and <code>operationName</code>
	 * 
	 * @param service
	 *            QName of the service
	 * @param port
	 *            name of the port
	 * @param operationName
	 *            name of the operation
	 * @return
	 */
	private Operation getOperation(QName service, String port,
			String operationName) {
		try {
			Operation operation = this.definition.getService(service).getPort(
					port).getBinding().getBindingOperation(operationName, null,
					null).getOperation();
			return operation;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Gets the Element of the first part of the output message of operation
	 * identified by <code>service</code>, <code>port</code> and
	 * <code>operationName</code> (first part because of the WS-I standard).
	 * 
	 * 
	 * @param service
	 *            QName of the service
	 * @param port
	 *            name of the port
	 * @param operationName
	 *            name of the operation
	 * @return
	 * @see #getInputElementForOperation(QName, String, String)
	 */
	public Element getOutputElementForOperation(QName service, String port,
			String operationName) {
		Operation operation = this.getOperation(service, port, operationName);
		if (operation == null) {
			return null;
		}
		Part part = (Part) operation.getOutput().getMessage().getParts()
				.values().iterator().next();
		return this.elements.get(part.getElementName());
	}
}
