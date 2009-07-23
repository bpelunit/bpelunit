package org.bpelunit.toolsupport.util.schema;

import java.util.HashMap;

import javax.xml.namespace.QName;

import org.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import org.bpelunit.toolsupport.util.schema.nodes.Element;
import org.bpelunit.toolsupport.util.schema.nodes.SimpleType;

public interface SchemaElementManager {

	public abstract SimpleType getSimpleType(QName qName);

	public abstract SimpleType getSimpleType(String localPart);

	public abstract SimpleType getSimpleType(String targetNameSpace, String localPart);

	public abstract ComplexType getComplexType(QName qName);

	public abstract ComplexType getComplexType(String targetNameSpace, String localPart);

	public abstract Element getElement(QName qName);

	public abstract Element getElement(String targetNameSpace, String localPart);

	public abstract HashMap<QName, Element> getElements();

	public abstract HashMap<QName, ComplexType> getComplexTypes();

	public abstract HashMap<QName, SimpleType> getSimpleTypes();

	public abstract boolean wasLastComplexCreated();

}