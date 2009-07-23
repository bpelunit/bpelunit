package org.bpelunit.toolsupport.util.schema.nodes;

public interface Type extends SchemaNode {
	public abstract boolean isSimpleType();

	public abstract boolean isComplexType();

	public abstract ComplexType getAsComplexType();

	public abstract SimpleType getAsSimpleType();

}