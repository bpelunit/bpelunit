package net.bpelunit.toolsupport.util.schema.nodes.impl;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.Attribute;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;

public class AttributeImpl extends SchemaNodeImpl implements Attribute {

	private SimpleType type;

	private String defaultValue;

	private String fixedValue;

	public AttributeImpl(QName qName) {
		super(qName);
	}

	public AttributeImpl(String targetNamespace, String localPart) {
		super(targetNamespace, localPart);
	}

	@Override
	public void setType(SimpleType type) {
		this.type = type;
	}

	@Override
	public SimpleType getType() {
		return this.type;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		if (this.defaultValue != null) {
			this.fixedValue = null;
		}
	}

	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

	@Override
	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
		if (this.fixedValue != null) {
			this.defaultValue = null;
		}
	}

	@Override
	public String getFixedValue() {
		return this.fixedValue;
	}

	@Override
	public String getDefaultOrFixedValue() {
		if (this.defaultValue != null) {
			return this.defaultValue;
		}
		if (this.fixedValue != null) {
			return this.fixedValue;
		}
		return "";
	}
}