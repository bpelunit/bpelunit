package net.bpelunit.toolsupport.util.schema;

import java.util.HashMap;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;

/**
 * Manages the Elements, ComplexTypes and SimpleTypes of one or more schemata.
 * Takes care that for every QName exists only Element, SimpleType or
 * ComplexType.
 * 
 * @author cvolhard
 * @see Element
 * @see ComplexType
 * @see SimpleType
 */
public interface SchemaElementManager {

	/**
	 * Returns the SimpleType with the matching qName. If this SimpleType does
	 * not exist, a new one is created.
	 * 
	 * @param qName
	 * @return
	 */
	public abstract SimpleType getSimpleType(QName qName);

	/**
	 * Returns the SimpleType with the machting localPart and
	 * "http://www.w3.org/2001/XMLSchema" as Namespace. If this SimpleType does
	 * not exist, a new one is created.
	 * 
	 * @param localPart
	 * @return
	 */
	public abstract SimpleType getSimpleType(String localPart);

	/**
	 * Returns the SimpleType with the matching nameSpace and the matching
	 * localPart. If this SimpleType does not exist, a new one is created.
	 * 
	 * @param nameSpace
	 * @param localPart
	 * @return
	 */
	public abstract SimpleType getSimpleType(String nameSpace, String localPart);

	/**
	 * Returns the ComplexType with the matching qName. If this ComplexType does
	 * not exist, a new one is created.
	 * 
	 * @param qName
	 * @return
	 */
	public abstract ComplexType getComplexType(QName qName);

	/**
	 * Returns the ComplexType with the matching nameSpace and the matching
	 * localPart. If this ComplexType does not exist, a new one is created.
	 * 
	 * @param nameSpace
	 * @param localPart
	 * @return
	 */
	public abstract ComplexType getComplexType(String nameSpace,
			String localPart);

	/**
	 * Returns the Element with the matching qName. It this Element does not
	 * exist, a new one is created.
	 * 
	 * @param qName
	 * @return
	 */
	public abstract Element getElement(QName qName);

	/**
	 * Returns the Element with the matching nameSpace and the matching
	 * localPart. If this Element does not exist, a new one is created.
	 * 
	 * @param nameSpace
	 * @param localPart
	 * @return
	 */
	public abstract Element getElement(String nameSpace, String localPart);

	/**
	 * Returns all root Elements in a HashMap with the QNames as key. Root
	 * Elements are Elements which are not nested in an other Element or
	 * ComplexType.
	 * 
	 * @return
	 */
	public abstract HashMap<QName, Element> getElements();

	/**
	 * Returns all ComplexTypes in a HashMap with the QNames as key.
	 * 
	 * @return
	 */
	public abstract HashMap<QName, ComplexType> getComplexTypes();

	/**
	 * Returns all SimpleTypes in a HashMap with the QNames as key.
	 * 
	 * @return
	 */
	public abstract HashMap<QName, SimpleType> getSimpleTypes();

	/**
	 * If the last call of {@link #getComplexType(QName)} or
	 * {@link #getComplexType(String, String)} created a new ComplexType, this
	 * method returns true, else false.
	 * 
	 * @return
	 */
	public abstract boolean wasLastComplexNewCreated();

}