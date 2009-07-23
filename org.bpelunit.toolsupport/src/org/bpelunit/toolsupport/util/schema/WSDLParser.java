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

public class WSDLParser {
	private Definition definition;
	private HashMap<QName, ComplexType> complexTypes;
	private HashMap<QName, SimpleType> simpleTypes;
	private HashMap<QName, Element> elements;

	public WSDLParser(Definition definition) throws SAXException,
			TransformerException {
		this.definition = definition;
		this.readSchemata();
	}

	HashMap<QName, ComplexType> getComplexTypes() {
		return this.complexTypes;
	}

	HashMap<QName, SimpleType> getSimpleTypes() {
		return this.simpleTypes;
	}

	HashMap<QName, Element> getElements() {
		return this.elements;
	}

	@SuppressWarnings("unchecked")
	private void readSchemata() throws SAXException, TransformerException {
		SchemaParser parser = new SchemaParser();
		this.complexTypes = parser.getComplexTypes();
		this.simpleTypes = parser.getSimpleTypes();
		this.elements = parser.getElements();

		XSOMParser reader = new XSOMParser();
		reader.setErrorHandler(new ErrorReporter(System.out));
		reader.setAnnotationParser(new DomAnnotationParserFactory());

		Map<String, String> namespaces = this.definition.getNamespaces();
		for (Object tmp : this.definition.getTypes().getExtensibilityElements()) {
			if (tmp instanceof Schema) {
				Schema schema = (Schema) tmp;

				org.w3c.dom.Element schemaElement = schema.getElement();
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

	public Element getInputElementForOperation(QName service, String port,
			String operationName) {
		Operation operation = this.getOperation(service, port, operationName);
		Part part = (Part) operation.getInput().getMessage().getParts()
				.values().iterator().next();
		return this.elements.get(part.getElementName());
	}

	private Operation getOperation(QName service, String port,
			String operationName) {
		Operation operation = this.definition.getService(service).getPort(port)
				.getBinding().getBindingOperation(operationName, null, null)
				.getOperation();
		return operation;
	}

	public Element getOutputElementForOperation(QName service, String port,
			String operationName) {
		Operation operation = this.getOperation(service, port, operationName);
		Part part = (Part) operation.getOutput().getMessage().getParts()
				.values().iterator().next();
		return this.elements.get(part.getElementName());
	}
}
