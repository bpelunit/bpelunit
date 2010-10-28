package net.bpelunit.toolsupport.util.schema.nodes.impl;

import java.util.Map;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.Attribute;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.Type;

public class ElementImpl extends SchemaNodeImpl implements Element {

	private Type type;

	private int minOccurs = 1;

	private int maxOccurs = 1;

	private String defaultValue;

	private String fixedValue;

	private boolean isNillable = false;

	public ElementImpl(QName qName) {
		super(qName);
	}

	public ElementImpl(String targetNamespace, String localPart) {
		super(targetNamespace, localPart);
	}

	@Override
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public void setMinOccurs(int minOccurs) {
		this.minOccurs = Math.max(0, minOccurs);
	}

	@Override
	public int getMinOccurs() {
		return this.minOccurs;
	}

	@Override
	public void setMaxOccurs(int maxOccurs) {
		this.maxOccurs = Math.max(0, maxOccurs);
	}

	@Override
	public int getMaxOccurs() {
		return this.maxOccurs;
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
	public void setNillable(boolean isNillable) {
		this.isNillable = isNillable;
	}

	@Override
	public boolean isNillable() {
		return this.isNillable;
	}

	@Override
	public String toXMLString(Map<String, String> namespaces) {
		return this.toXMLString(namespaces, "\n");
	}

	@Override
	public String toXMLString(Map<String, String> namespaces, String indent) {
		String namespace = namespaces.get(this.getNamespace());
		if (namespace == null) {
			namespace = this.getNamespace();
		}
		String start = "<" + namespace + ":" + this.getLocalPart();

		if (this.type instanceof ComplexType) {
			ComplexType complex = (ComplexType) this.type;

			// add the attributes to the string
			for (Attribute attribute : complex.getAttributes()) {
				start += " " + attribute.getLocalPart() + "=\""
						+ attribute.getDefaultOrFixedValue() + "\"";
			}
			start += ">";

			String innerIndent = indent + "\t";
			for (Element element : complex.getElements()) {
				for (int i = 0; i < Math.max(1, element.getMinOccurs()); i++) {
					start += innerIndent
							+ element.toXMLString(namespaces, innerIndent);
				}
			}
			start += indent + "</" + namespace + ":" + this.getLocalPart()
					+ ">";
		} else {
			start += "></" + namespace + ":" + this.getLocalPart() + ">";
		}
		return start;
	}
}