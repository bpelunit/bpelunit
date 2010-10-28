package net.bpelunit.toolsupport.util.schema.nodes;

public interface Type extends SchemaNode {
	/**
	 * 
	 * @return <code>true</code> if <code>this</code> is instance of
	 *         {@link SimpleType}
	 */
	public abstract boolean isSimpleType();

	/**
	 * 
	 * @return <code>true</code> if <code>this</code> is instance of
	 *         {@link ComplexType}
	 */
	public abstract boolean isComplexType();

	/**
	 * 
	 * @return <code>this</code> as {@link ComplexType}, or <code>null</code> if
	 *         <code>this</code> is {@link SimpleType}
	 */
	public abstract ComplexType getAsComplexType();

	/**
	 * 
	 * @return <code>this</code> as {@link SimpleType}, or <code>null</code> if
	 *         <code>this</code> is {@link ComplexType}
	 */
	public abstract SimpleType getAsSimpleType();

}