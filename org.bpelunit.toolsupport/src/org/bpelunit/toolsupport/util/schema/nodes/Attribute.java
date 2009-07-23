package org.bpelunit.toolsupport.util.schema.nodes;

public interface Attribute extends SchemaNode {

	public abstract void setType(SimpleType type);

	public abstract SimpleType getType();

	public abstract void setDefaultValue(String defaultValue);

	public abstract String getDefaultValue();

	public abstract void setFixedValue(String fixedValue);

	public abstract String getFixedValue();

	public abstract Attribute clone();

	public abstract String getValue();

}