package net.bpelunit.toolsupport.util.schema.nodes;

import javax.xml.namespace.QName;

public interface SchemaNode {

	public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";

	public abstract String getLocalPart();

	public abstract String getNamespace();

	public abstract QName getQName();

}