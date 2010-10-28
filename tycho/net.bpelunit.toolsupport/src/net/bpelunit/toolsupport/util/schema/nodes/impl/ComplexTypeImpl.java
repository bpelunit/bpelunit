package net.bpelunit.toolsupport.util.schema.nodes.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.Attribute;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;

public class ComplexTypeImpl extends TypeImpl implements ComplexType {

	private List<Element> elements = new ArrayList<Element>();

	private List<Attribute> attributes = new ArrayList<Attribute>();

	public ComplexTypeImpl(QName qName) {
		super(qName);
	}

	public ComplexTypeImpl(String targetNamespace, String localPart) {
		super(targetNamespace, localPart);
	}

	@Override
	public void addElement(Element element) {
		this.elements.add(element);
	}

	@Override
	public List<Element> getElements() {
		return this.elements;
	}

	@Override
	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	@Override
	public List<Attribute> getAttributes() {
		return this.attributes;
	}

	@Override
	public ComplexType getAsComplexType() {

		return this;
	}

	@Override
	public SimpleType getAsSimpleType() {
		return null;
	}

	@Override
	public boolean isComplexType() {
		return true;

	}

	@Override
	public boolean isSimpleType() {
		return false;
	}
}