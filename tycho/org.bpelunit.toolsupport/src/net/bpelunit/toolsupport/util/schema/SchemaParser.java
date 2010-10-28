package net.bpelunit.toolsupport.util.schema;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.impl.SchemaElementManagerImpl;
import net.bpelunit.toolsupport.util.schema.nodes.Attribute;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import net.bpelunit.toolsupport.util.schema.nodes.SimpleType;
import net.bpelunit.toolsupport.util.schema.nodes.impl.AttributeImpl;
import net.bpelunit.toolsupport.util.schema.nodes.impl.ComplexTypeImpl;
import net.bpelunit.toolsupport.util.schema.nodes.impl.ElementImpl;
import net.bpelunit.toolsupport.util.schema.nodes.impl.SimpleTypeImpl;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
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
 * @author cvolhard, Antonio García-Domínguez
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
	 * Parses all Schemata contained in <code>file</code>. So far used for testing only. 
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
		// ignore the built-in datatypes returned by XSOM by default, we will create them as needed
		if (SchemaNode.XML_SCHEMA_NAMESPACE.equals(schema.getTargetNamespace())) {
			return;
		}

		for (XSElementDecl elementDecl : schema.getElementDecls().values()) {
			this.readElement(elementDecl, false);
		}
		for (XSSimpleType simpleType : schema.getSimpleTypes().values()) {
			this.readSimpleType(simpleType);
		}
		for (XSComplexType complexType : schema.getComplexTypes().values()) {
			this.readComplexType(complexType);
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
			// is anonymous element, doesn't need to be known by ElementManager
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
		 * appear in this element. If name is not null, it can appear in other
		 * elements, therefore the manager has to create it.
		 */
		ComplexType complex;
		String name = xsComplex.getName();
		if (name == null) {
			complex = new ComplexTypeImpl(targetNamespace, name);
		} else {
			complex = this.elementManager.getComplexType(targetNamespace, name);
			if (!this.elementManager.wasLastComplexNewCreated()) {
				// has been filled already, we can stop here -- otherwise, continue below
				return complex;
			}
		}

		// insert children elements and attributes of the complex type
		for (XSAttributeUse attributeUse : xsComplex.getAttributeUses()) {
			complex.addAttribute(this.readAttribute(targetNamespace,
					attributeUse.getDecl()));

		}
		// if null, complexType contains no elements
		if (xsComplex.getContentType().asParticle() != null) {
			XSModelGroup mainModelGroup
				= xsComplex.getContentType().asParticle().getTerm().asModelGroup();
			readModelGroup(complex, mainModelGroup);
		}
		return complex;
	}

	/**
	 * Collects recursively all elements in a model group. Currently, as <xsd:all>
	 * and <xsd:choice> aren't explicitly supported, they will be treated as if
	 * they were <xsd:sequence>s.
	 *
	 * TODO Handle wildcards (<xsd:any>)
	 */
	private void readModelGroup(ComplexType complex, XSModelGroup modelGroup) {
		for (XSParticle particle : modelGroup.getChildren()) {
			XSTerm term = particle.getTerm();

			XSElementDecl elemDecl = term.asElementDecl();
			if (elemDecl != null) {
				Element element = this.readElement(elemDecl, true);
				element.setMaxOccurs(particle.getMaxOccurs());
				element.setMinOccurs(particle.getMinOccurs());
				complex.addElement(element);
				continue;
			}

			XSModelGroup nestedModelGroup = term.asModelGroup();
			if (nestedModelGroup != null) {
				readModelGroup(complex, nestedModelGroup);
			}

			// otherwise, it's a wildcard: don't do anything in that case
		}
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
		String localPart = decl.getName();
		if (localPart == null) {
			return new SimpleTypeImpl(decl.getTargetNamespace(), localPart);
		} else {
			return this.elementManager.getSimpleType(decl.getTargetNamespace(),
				decl.getName());
		}
	}

	/**
	 * Returns the manager for the elements in this schema.
	 *
	 * @return Manager for the elements in this schema.
	 */
	public SchemaElementManager getSchemaElementManager() {
		return this.elementManager;
	}
}
