package org.bpelunit.toolsupport.util.schema;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.bpelunit.toolsupport.util.schema.impl.SchemaElementManagerImpl;
import org.bpelunit.toolsupport.util.schema.nodes.Attribute;
import org.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import org.bpelunit.toolsupport.util.schema.nodes.Element;
import org.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import org.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import org.bpelunit.toolsupport.util.schema.nodes.impl.AttributeImpl;
import org.bpelunit.toolsupport.util.schema.nodes.impl.ComplexTypeImpl;
import org.bpelunit.toolsupport.util.schema.nodes.impl.ElementImpl;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.util.DomAnnotationParserFactory;

/**
 * Parses a XML schema and creates the containing Elements and Types
 * 
 * Following restrictions regarding XML schema
 * <ul>
 * <li>global attributes are not parsed</li>
 * <li>element-ref are not regarded</li>
 * <li>restrictions are not regarded</li>
 * <li>no mixed=true in Types</li>
 * <li>no Distinction between all, sequence, choice</li>
 * <li>no groups</li>
 * <li>ComplexTypes are only created if a Element references to it</li>
 * </ul>
 * 
 * @author cvolhard
 * 
 */
public class SchemaParser {

	private ErrorAdapter errorHandler = new ErrorAdapter();
	private SchemaElementManager elementManager = new SchemaElementManagerImpl();

	/**
	 * Sets the ErrorHandler, if <code>errorHandler</code> is not
	 * </code>null</code>.
	 * 
	 * @param errorHandler
	 */
	public void setErrorHandler(ErrorAdapter errorHandler) {
		if (errorHandler != null) {
			this.errorHandler = errorHandler;
		}
	}

	/**
	 * Returns a HashMap with all Elements of the parsed schemata. The QNames of
	 * the Elements are the keys.
	 * 
	 * @return
	 */
	public HashMap<QName, Element> getElements() {
		return this.elementManager.getElements();
	}

	/**
	 * Returns a HashMap with all ComplexTypes of the parsed schemata. The
	 * QNames of the ComplexTypes are the keys.
	 * 
	 * @return
	 */
	HashMap<QName, ComplexType> getComplexTypes() {
		return this.elementManager.getComplexTypes();
	}

	/**
	 * Returns a HashMap with all SimpleTypes of the parsed schemata. The QNames
	 * of the SimpleTypes are the keys.
	 * 
	 * @return
	 */
	HashMap<QName, SimpleType> getSimpleTypes() {
		return this.elementManager.getSimpleTypes();
	}

	/**
	 * Parses all Schemata contained in <code>file</code>.
	 * 
	 * @param file
	 * @throws SAXException
	 * @throws IOException
	 */
	public void parse(File file) throws SAXException, IOException {
		XSOMParser reader = new XSOMParser();
		reader.setErrorHandler(this.errorHandler);
		reader.setAnnotationParser(new DomAnnotationParserFactory());
		reader.parse(file);
		this.readSchemata(reader.getResult());
	}

	/**
	 * Parses all Schemata in <code>schemata</code>.
	 * 
	 * @param schemata
	 */
	public void readSchemata(XSSchemaSet schemata) {
		for (XSSchema schema : schemata.getSchemas()) {
			this.readSchema(schema);
		}
	}

	/**
	 * Parses <code>schema</code> if the TargetNamespace is not the XML Schema
	 * Namespace. The needed SimpleTypes from the XML Schema Namespace are
	 * generated automatically.
	 * 
	 * @param schema
	 */
	private void readSchema(XSSchema schema) {
		// ignore the built in datatypes
		if (SchemaNode.XML_SCHEMA_NAMESPACE.equals(schema.getTargetNamespace())) {
			return;
		}

		for (XSElementDecl elementDecl : schema.getElementDecls().values()) {
			this.readElement(elementDecl, false);
		}
	}

	/**
	 * Creates a Element from <code>decl</code>. If <code>isNested</code> is
	 * <code>true</code>, the created Element is not added to the
	 * Elements-HashMap. Only Elements which are not nested in an other Element
	 * or ComplexType should be added to the Elements-HashMap.
	 * 
	 * @param decl
	 * @param isNested
	 * @return
	 */
	private Element readElement(XSElementDecl decl, boolean isNested) {
		Element element;
		if (isNested) {
			element = new ElementImpl(decl.getTargetNamespace(), decl.getName());
			element.setNillable(decl.isNillable());
		} else {
			element = this.elementManager.getElement(decl.getTargetNamespace(),
					decl.getName());
		}
		XSType declType = decl.getType();
		if (declType instanceof XSSimpleType) {
			element.setType(this.readSimpleType((XSSimpleType) declType));
		}
		if (declType instanceof XSComplexType) {
			element.setType(this.readComplexType((XSComplexType) declType));
		}
		return element;
	}

	/**
	 * Creates a ComplexType from <code>xsComplex</code>.
	 * 
	 * @param xsComplex
	 * @return
	 */
	private ComplexType readComplexType(XSComplexType xsComplex) {
		String targetNamespace = xsComplex.getTargetNamespace();

		/*
		 * If name is null, the xsComplex is anonymous. Therefore it can only
		 * apear in this element. If name is not null, it can appear in other
		 * elements, therefore the manager has to create it.
		 */
		ComplexType complex;
		String name = xsComplex.getName();
		if (name == null) {
			complex = new ComplexTypeImpl(targetNamespace, name);
		} else {
			complex = this.elementManager.getComplexType(targetNamespace, name);
			if (!this.elementManager.wasLastComplexNewCreated()) {
				return complex;
			}
		}

		for (XSAttributeUse attributeUse : xsComplex.getAttributeUses()) {
			complex.addAttribute(this.readAttribute(targetNamespace,
					attributeUse.getDecl()));

		}
		// if null, complexType contains no elements
		if (xsComplex.getContentType().asParticle() != null) {
			for (XSParticle particle : xsComplex.getContentType().asParticle()
					.getTerm().asModelGroup().getChildren()) {
				Element element = this.readElement(particle.getTerm()
						.asElementDecl(), true);
				element.setMaxOccurs(particle.getMaxOccurs());
				element.setMinOccurs(particle.getMinOccurs());
				complex.addElement(element);
			}
		}
		return complex;
	}

	/**
	 * Creates Attribute in <code>targetNamespace</code> from <code>decl</code>.
	 * 
	 * @param targetNamespace
	 * @param decl
	 * @return
	 */
	private Attribute readAttribute(String targetNamespace, XSAttributeDecl decl) {
		String attributeName = decl.getName();
		// targetNamespace is empty, use the targetNamespace of the
		// surrounding complexType
		Attribute attribute = new AttributeImpl(targetNamespace, attributeName);
		attribute.setType(this.readSimpleType(decl.getType()));
		if (decl.getFixedValue() != null) {
			attribute.setFixedValue(decl.getFixedValue().value);
		}
		if (decl.getDefaultValue() != null) {
			attribute.setDefaultValue(decl.getDefaultValue().value);
		}
		return attribute;
	}

	/**
	 * Creates SimpleType from <code>decl</code>.
	 * 
	 * @param decl
	 * @return
	 */
	private SimpleType readSimpleType(XSSimpleType decl) {
		return this.elementManager.getSimpleType(decl.getTargetNamespace(),
				decl.getName());
	}

}
