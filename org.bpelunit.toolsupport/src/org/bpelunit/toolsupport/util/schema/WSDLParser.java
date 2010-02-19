package org.bpelunit.toolsupport.util.schema;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
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

import org.bpelunit.toolsupport.util.WSDLReadingException;
import org.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import org.bpelunit.toolsupport.util.schema.nodes.Element;
import org.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.util.DomAnnotationParserFactory;

/**
 * Parses all schemata in the Definition of a WSDL and manages the Elements and
 * Types of the schemata. Connects the operations of the service with the input
 * and output messages.
 * 
 * Antonio: fix relative URI imports.
 *
 * @author cvolhard, Antonio García-Domínguez
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
	 * @throws WSDLReadingException
	 */
	@SuppressWarnings("unchecked")
	private void readSchemata() throws SAXException, TransformerException, WSDLReadingException {
		SchemaParser parser = new SchemaParser();
		this.complexTypes = parser.getComplexTypes();
		this.simpleTypes = parser.getSimpleTypes();
		this.elements = parser.getElements();

		XSOMParser reader = new XSOMParser();
		reader.setErrorHandler(new ErrorAdapter());
		reader.setAnnotationParser(new DomAnnotationParserFactory());

		Map<String, String> namespaces = this.definition.getNamespaces();
		// for all schemas in the WSDL ...
		for (Object tmp : this.definition.getTypes().getExtensibilityElements()) {
			if (tmp instanceof Schema) {
				Schema schema = (Schema) tmp;

				org.w3c.dom.Element schemaElement = schema.getElement();

				// inherit the namespaces from the definitions-tag
				this.addNamespaces(namespaces, schemaElement);

				/*
				 * We need to set a system ID or importing .xsd files with
				 * relative paths won't work. See this mailing list thread at
				 * the XSOM website:
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
	@SuppressWarnings("unchecked")
	private void addNamespaces(Map<String, String> namespaces, org.w3c.dom.Element schemaElement) {
		for (Object o : namespaces.entrySet()) {
			if (o instanceof Entry) {
				Entry entry = (Entry) o;
				String attribute = "xmlns:" + (String) entry.getKey();
				if (schemaElement.getAttributeNode(attribute) == null) {
					schemaElement.setAttribute(attribute, (String) entry.getValue());
				}
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
	 * @see #getOutputElementForOperation(QName, String, String)
	 */
	public Element getInputElementForOperation(QName service, String port, String operationName)
			throws InvalidInputException, NoSuchOperationException {
		Operation operation = this.getOperation(service, port, operationName);
		if (operation == null) {
			throw new NoSuchOperationException("Operation " + operationName + "for port " + port
					+ " in service " + service + " does not exists.");
		}
		Collection values = operation.getInput().getMessage().getParts().values();
		if (values.isEmpty()) {
			return null;
		}
		Part part = (Part) values.iterator().next();
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
	 * @throws InvalidInputException
	 */
	private Operation getOperation(QName service, String port, String operationName)
			throws InvalidInputException {
		if (service == null || service.getLocalPart().isEmpty()
				|| service.getNamespaceURI().isEmpty()) {
			throw new InvalidInputException("Service must be set.");
		}

		if (port == null || port.isEmpty()) {
			throw new InvalidInputException("Port must be set.");
		}

		if (operationName == null || operationName.isEmpty()) {
			throw new InvalidInputException("operationName must be set.");
		}

		try {
			Operation operation = this.definition.getService(service).getPort(port).getBinding()
					.getBindingOperation(operationName, null, null).getOperation();
			return operation;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Returns an XML Element for the output of the operation.
	 *
	 * For document/literal bindings, returns the Element of the first part of
	 * the output message of the operation identified by <code>service</code>,
	 * <code>port</code> and <code>operationName</code>.
	 * 
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
	 * @see #getInputElementForOperation(QName, String, String)
	 */
	public Element getOutputElementForOperation(QName service, String port, String operationName)
			throws InvalidInputException, NoSuchOperationException {
		Operation operation = this.getOperation(service, port, operationName);
		if (operation == null) {
			throw new NoSuchOperationException("Operation " + operationName + "for port " + port
					+ " in service " + service + " does not exists.");
		}
		Collection values = operation.getOutput().getMessage().getParts().values();
		if (values.isEmpty()) {
			return null;
		}
		Part part = (Part) values.iterator().next();
		return this.elements.get(part.getElementName());
	}
}

