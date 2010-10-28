package net.bpelunit.toolsupport.util.schema.nodes;

/**
 * Represents a xml attribute in a schema.
 * 
 * @author cvolhard
 * 
 */
public interface Attribute extends SchemaNode {

	public abstract void setType(SimpleType type);

	public abstract SimpleType getType();

	/**
	 * Sets the default vaule.
	 * 
	 * The default value is used if no other value is set. Either default value
	 * or fixed value can be set.
	 * 
	 * @param defaultValue
	 * @see #setFixedValue(String)
	 */
	public abstract void setDefaultValue(String defaultValue);

	/**
	 * @see #setDefaultValue(String)
	 * @return
	 */
	public abstract String getDefaultValue();

	/**
	 * Sets the fixed value.
	 * 
	 * If a fixed value is set, the attribute can has no other value. Either
	 * default value or fixed value can be set.
	 * 
	 * @see #setDefaultValue(String)
	 * @param fixedValue
	 */
	public abstract void setFixedValue(String fixedValue);

	/**
	 * @see #setFixedValue(String)
	 */
	public abstract String getFixedValue();

	/**
	 * Returns first non <code>null</code> value of default value, fixed value
	 * or empty String.
	 * 
	 * @return
	 */
	public abstract String getDefaultOrFixedValue();

}