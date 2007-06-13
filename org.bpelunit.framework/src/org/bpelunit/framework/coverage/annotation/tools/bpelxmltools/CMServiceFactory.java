package org.bpelunit.framework.coverage.annotation.tools.bpelxmltools;

import static org.bpelunit.framework.coverage.CoverageConstants.*;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import org.apache.log4j.Logger;
import org.jdom.Element;


public class CMServiceFactory {

	private static CMServiceFactory instance = null;
	private static Logger logger;

	public static CMServiceFactory getInstance() {
		if (instance == null) {
			instance = new CMServiceFactory();
		}
		return instance;
	}

	private CMServiceFactory() {
		logger = Logger.getLogger(getClass());
	}

	public void prepareBPELFile(Element process_element) {
		process_element
				.addNamespaceDeclaration(COVERAGETOOL_NAMESPACE);
		insertPartnerLink(process_element);
	}

	public void insertVariableForRegisterMarker(Element scope,
			String variableName) {
		insertVariable(createVariable(variableName,
				MESSAGETYPE_OF_REGISTER_MESSAGE, null), scope);

	}

	private void insertPartnerLink(Element process_element) {
		Element partnerLinks = process_element.getChild(PARTNERLINKS_ELEMENT,
				getProcessNamespace());
		Element partnerLink = createBPELElement(PARTNERLINK_ELEMENT);
		partnerLink.setAttribute(NAME_ATTR, PARTNERLINK_NAME);
		partnerLink.setAttribute(PARTNERLINKTYPE_ATTRIBUTE,
				REPORTING_SERVICE_PORTTYPE);
		partnerLink.setAttribute(PARTNERROLE_ATTR_AND_ELEMENT, PARTNER_ROLE);
		partnerLinks.addContent(partnerLink);
	}

	public Element createInvokeElementForLog(String variable) {

		Element invoke = createBPELElement(BasicActivities.INVOKE_ACTIVITY);
		invoke.setAttribute(INPUTVARIABLE_ATTR, variable);
		invoke.setAttribute(OPERATION_ATTRIBUTE, REPORT_OPERATION);
		invoke.setAttribute(PARTNERLINK_ATTRIBUTE, PARTNERLINK_NAME);
		invoke.setAttribute(PORTTYPE_ATTRIBUTE, REPORTING_SERVICE_PORT);
		return invoke;
	}

	public Element createInvokeElementForRegisterMarker(String variable) {

		Element invoke = createBPELElement(BasicActivities.INVOKE_ACTIVITY);
		invoke.setAttribute(INPUTVARIABLE_ATTR, variable);
		invoke.setAttribute(OPERATION_ATTRIBUTE,
				REGISTER_COVERAGE_LABELS_OPERATION);
		invoke.setAttribute(PARTNERLINK_ATTRIBUTE, PARTNERLINK_NAME);
		invoke.setAttribute(PORTTYPE_ATTRIBUTE,
				REPORTING_SERVICE_PORT);

		return invoke;
	}

	public Element createAssignElement(String content, String variable) {
		insertVariable(createVariable(variable,
				MESSAGETYPE_OF_REPORTING_MESSAGE, null), null);

		Element from = createBPELElement(FROM_ELEMENT);
		if(getProcessNamespace().equals(NAMESPACE_BPEL_1_1)){
			from.setText(content);
		}else if(getProcessNamespace().equals(NAMESPACE_BPEL_2_0)){
			Element literal = createBPELElement(LITERAL_ELEMENT);
			literal.setText(content);
			from.addContent(literal);
		}
		
		Element to = createBPELElement(TO_ELEMENT);
		to.setAttribute(PART_ATTR, PART_OF_REPORTING_MESSAGE);
		to.setAttribute(VARIABLE_ATTR, variable);
		return createAssign(from, to);
	}

//	public Element createAssignElementForRegisterMarker(Element scope,
//			String content, String variable) {
//		insertVariableForRegisterMarker(scope, variable);
//		Element from = createBPELElement(FROM_ELEMENT);
//
//		from.setText(content);
//		Element to = createBPELElement(TO_ELEMENT);
//		to.setAttribute(PART_ATTR, PART_OF_REGISTER_MESSAGE);
//		to.setAttribute(VARIABLE_ATTR, variable);
//		return createAssign(from, to);
//	}

//	public void insertVariableForRegisterMarker(String attributeValue) {
//		insertVariableForRegisterMarker(null, attributeValue);
//	}

}
