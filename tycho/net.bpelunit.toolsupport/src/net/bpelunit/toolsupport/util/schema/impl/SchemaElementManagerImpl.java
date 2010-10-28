package net.bpelunit.toolsupport.util.schema.impl;

import java.util.HashMap;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.SchemaElementManager;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import net.bpelunit.toolsupport.util.schema.nodes.impl.ComplexTypeImpl;
import net.bpelunit.toolsupport.util.schema.nodes.impl.ElementImpl;
import net.bpelunit.toolsupport.util.schema.nodes.impl.SimpleTypeImpl;

public class SchemaElementManagerImpl implements SchemaElementManager {
	private HashMap<QName, SimpleType> simpleTypes = new HashMap<QName, SimpleType>();
	private HashMap<QName, ComplexType> complexTypes = new HashMap<QName, ComplexType>();
	private HashMap<QName, Element> elements = new HashMap<QName, Element>();

	private boolean wasLastComplexCreated = false;

	@Override
	public SimpleType getSimpleType(QName qName) {
		SimpleType simpleType = this.simpleTypes.get(qName);
		if (simpleType == null) {
			simpleType = new SimpleTypeImpl(qName);
			this.simpleTypes.put(qName, simpleType);
		}
		return simpleType;
	}

	@Override
	public SimpleType getSimpleType(String localPart) {
		return this.getSimpleType(new QName(SchemaNode.XML_SCHEMA_NAMESPACE,
				localPart));
	}

	@Override
	public SimpleType getSimpleType(String targetNameSpace, String localPart) {
		return this.getSimpleType(new QName(targetNameSpace, localPart));
	}

	@Override
	public ComplexType getComplexType(QName qName) {
		ComplexType complexType = this.complexTypes.get(qName);
		if (complexType == null) {
			complexType = new ComplexTypeImpl(qName);
			this.complexTypes.put(qName, complexType);
			this.wasLastComplexCreated = true;
		} else {
			this.wasLastComplexCreated = false;
		}
		return complexType;
	}

	@Override
	public ComplexType getComplexType(String targetNameSpace, String localPart) {

		return this.getComplexType(new QName(targetNameSpace, localPart));
	}

	@Override
	public Element getElement(QName qName) {
		Element element = this.elements.get(qName);
		if (element == null) {
			element = new ElementImpl(qName);
			this.elements.put(qName, element);
		}
		return element;

	}

	@Override
	public Element getElement(String targetNameSpace, String localPart) {
		return this.getElement(new QName(targetNameSpace, localPart));
	}

	@Override
	public HashMap<QName, Element> getElements() {
		return this.elements;
	}

	@Override
	public HashMap<QName, ComplexType> getComplexTypes() {
		return this.complexTypes;
	}

	@Override
	public HashMap<QName, SimpleType> getSimpleTypes() {
		return this.simpleTypes;
	}

	@Override
	public boolean wasLastComplexNewCreated() {
		return this.wasLastComplexCreated;
	}

}
