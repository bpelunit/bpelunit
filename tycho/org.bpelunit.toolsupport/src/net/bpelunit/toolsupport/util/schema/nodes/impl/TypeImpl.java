package net.bpelunit.toolsupport.util.schema.nodes.impl;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.Type;

public abstract class TypeImpl extends SchemaNodeImpl implements Type {

	public TypeImpl(QName qName) {
		super(qName);
	}

	public TypeImpl(String targetNamespace, String localPart) {
		super(targetNamespace, localPart);
	}

}