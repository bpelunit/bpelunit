package org.bpelunit.toolsupport.util.schema.nodes;

import java.util.Map;

public interface Element extends SchemaNode {

	public abstract void setType(Type type);

	public abstract Type getType();

	public abstract void setMinOccurs(int minOccurs);

	public abstract int getMinOccurs();

	public abstract void setMaxOccurs(int maxOccurs);

	public abstract int getMaxOccurs();

	public abstract void setDefaultValue(String defaultValue);

	public abstract String getDefaultValue();

	public abstract void setFixedValue(String fixedValue);

	public abstract String getFixedValue();

	public abstract void setNillable(boolean isNillable);

	public abstract boolean isNillable();

	public abstract String toXMLString(Map<String, String> namespaces);

	public abstract String toXMLString(Map<String, String> namespaces, String indent);

	public abstract Element clone();

}