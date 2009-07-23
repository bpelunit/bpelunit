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
 * Liest ein Schema aus und erzeugt die zugehörigen Elemente. Dabei gelten
 * folgende Einschränkungen:
 * <ul>
 * <li>globale Attribute werden nicht ausgelesen</li>
 * <li>element-ref werden nicht berücksichtigt</li>
 * <li>restrictions werden nicht berücksichtigt</li>
 * <li>kein mixed=true in Types</li>
 * <li>keine Unterscheidung zwischen all, sequence, choice</li>
 * <li>keine groups (weder Attribute noch Element)</li>
 * <li>complexTypes werden nur erzeugt wenn sich auch ein Element darauf bezieht
 * </li>
 * </ul>
 * 
 * @author cvolhard
 * 
 */
public class SchemaParser {

	private ErrorReporter errorHandler = new ErrorReporter();
	private SchemaElementManager elementManager = new SchemaElementManagerImpl();

	public void setErrorHandler(ErrorReporter errorHandler) {
		if (errorHandler != null) {
			this.errorHandler = errorHandler;
		}
	}

	public HashMap<QName, Element> getElements() {
		return this.elementManager.getElements();
	}

	HashMap<QName, ComplexType> getComplexTypes() {
		return this.elementManager.getComplexTypes();
	}

	HashMap<QName, SimpleType> getSimpleTypes() {
		return this.elementManager.getSimpleTypes();
	}

	public void parse(File file) throws SAXException, IOException {
		XSOMParser reader = new XSOMParser();
		reader.setErrorHandler(this.errorHandler);
		reader.setAnnotationParser(new DomAnnotationParserFactory());
		reader.parse(file);
		this.readSchemas(reader.getResult());
	}

	public void readSchemas(XSSchemaSet schemas) {
		for (XSSchema schema : schemas.getSchemas()) {
			this.readSchema(schema);
		}
	}

	private void readSchema(XSSchema schema) {
		// ignore the built in datatypes
		if (SchemaNode.XML_SCHEMA_NAMESPACE.equals(schema.getTargetNamespace())) {
			return;
		}

		for (XSElementDecl elementDecl : schema.getElementDecls().values()) {
			this.readElement(elementDecl, false);
		}

	}

	private Element readElement(XSElementDecl decl, boolean isNested) {
		Element element;
		if (isNested) {
			element = new ElementImpl(decl.getTargetNamespace(), decl.getName());
			element.setNillable(decl.isNillable());
		} else {
			element = this.elementManager.getElement(decl.getTargetNamespace(), decl.getName());
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
			if (!this.elementManager.wasLastComplexCreated()) {
				return complex;
			}
		}

		for (XSAttributeUse attributeUse : xsComplex.getAttributeUses()) {
			complex.addAttribute(this.readAttribute(targetNamespace, attributeUse));

		}
		// if null, complexType contains no elements
		if (xsComplex.getContentType().asParticle() != null) {
			for (XSParticle particle : xsComplex.getContentType().asParticle().getTerm()
					.asModelGroup().getChildren()) {
				Element element = this.readElement(particle.getTerm().asElementDecl(), true);
				element.setMaxOccurs(particle.getMaxOccurs());
				element.setMinOccurs(particle.getMinOccurs());
				complex.addElement(element);
			}
		}
		return complex;
	}

	private Attribute readAttribute(String targetNamespace, XSAttributeUse attributeUse) {
		XSAttributeDecl attrDecl = attributeUse.getDecl();
		String attributeName = attrDecl.getName();
		// targetNamespace is empty, use the targetNamespace of the
		// surrounding complexType
		Attribute attribute = new AttributeImpl(targetNamespace, attributeName);
		attribute.setType(this.readSimpleType(attrDecl.getType()));
		if (attrDecl.getFixedValue() != null) {
			attribute.setFixedValue(attrDecl.getFixedValue().value);
		}
		if (attrDecl.getDefaultValue() != null) {
			attribute.setDefaultValue(attrDecl.getDefaultValue().value);
		}
		return attribute;
	}

	private SimpleType readSimpleType(XSSimpleType decl) {
		return this.elementManager.getSimpleType(decl.getTargetNamespace(), decl.getName());
	}

}
