package net.bpelunit.toolsupport.util.schema.nodes.impl;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;

public abstract class SchemaNodeImpl implements SchemaNode {

	private String localPart;
	private String targetNamespace;

	public SchemaNodeImpl(QName qName) {
		this.setQName(qName);
	}

	public SchemaNodeImpl(String targetNamespace, String localPart) {
		this.setTargetNamespace(targetNamespace);
		this.setLocalPart(localPart);
	}

	private void setLocalPart(String localPart) {
		this.localPart = localPart;
	}

	@Override
	public String getLocalPart() {
		return this.localPart;
	}

	private void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	@Override
	public String getNamespace() {
		return this.targetNamespace;
	}

	@Override
	public QName getQName() {
		String namespace = (this.targetNamespace == null) ? "" : this.targetNamespace;
		return new QName(namespace, this.localPart);
	}

	private void setQName(QName qName) {
		this.localPart = qName.getLocalPart();
		this.targetNamespace = qName.getNamespaceURI();
	}
}