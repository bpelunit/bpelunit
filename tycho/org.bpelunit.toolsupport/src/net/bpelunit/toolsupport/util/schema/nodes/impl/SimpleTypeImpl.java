package net.bpelunit.toolsupport.util.schema.nodes.impl;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;

public class SimpleTypeImpl extends TypeImpl implements SimpleType {

	public SimpleTypeImpl(String localPart) {
		super(XML_SCHEMA_NAMESPACE, localPart);
	}

	public SimpleTypeImpl(String targetNamespace, String localPart) {
		super(targetNamespace, localPart);
	}

	public SimpleTypeImpl(QName qName) {
		super(qName);
	}

	@Override
	public ComplexType getAsComplexType() {
		return null;
	}

	@Override
	public SimpleType getAsSimpleType() {
		return this;
	}

	@Override
	public boolean isComplexType() {
		return false;
	}

	@Override
	public boolean isSimpleType() {
		return true;
	}
}