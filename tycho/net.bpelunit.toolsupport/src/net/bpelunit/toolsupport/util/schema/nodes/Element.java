package net.bpelunit.toolsupport.util.schema.nodes;

import java.util.Map;

public interface Element extends SchemaNode {

	public abstract void setType(Type type);

	public abstract Type getType();

	public abstract void setMinOccurs(int minOccurs);

	public abstract int getMinOccurs();

	public abstract void setMaxOccurs(int maxOccurs);

	public abstract int getMaxOccurs();

	/**
	 * 
	 * @param defaultValue
	 * @see Attribute#setDefaultValue(String)
	 */
	public abstract void setDefaultValue(String defaultValue);

	public abstract String getDefaultValue();

	/**
	 * @see Attribute#setFixedValue(String)
	 * @param fixedValue
	 */
	public abstract void setFixedValue(String fixedValue);

	public abstract String getFixedValue();

	public abstract void setNillable(boolean isNillable);

	public abstract boolean isNillable();

	public abstract String toXMLString(Map<String, String> namespaces);

	public abstract String toXMLString(Map<String, String> namespaces,
			String indent);

}