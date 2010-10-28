package net.bpelunit.framework.coverage.annotation.tools.bpelxmltools;

import static net.bpelunit.framework.coverage.CoverageConstants.COVERAGETOOL_NAMESPACE;
import static net.bpelunit.framework.coverage.CoverageConstants.MESSAGETYPE_OF_REPORTING_MESSAGE;
import static net.bpelunit.framework.coverage.CoverageConstants.PARTNERLINK_NAME;
import static net.bpelunit.framework.coverage.CoverageConstants.PARTNER_ROLE;
import static net.bpelunit.framework.coverage.CoverageConstants.PART_OF_REPORTING_MESSAGE;
import static net.bpelunit.framework.coverage.CoverageConstants.REPORTING_SERVICE_PORT;
import static net.bpelunit.framework.coverage.CoverageConstants.REPORTING_SERVICE_PORTTYPE;
import static net.bpelunit.framework.coverage.CoverageConstants.REPORT_OPERATION;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.FROM_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.INPUTVARIABLE_ATTR;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.LITERAL_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAME_ATTR;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.OPERATION_ATTRIBUTE;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.PARTNERLINKS_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.PARTNERLINKTYPE_ATTRIBUTE;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.PARTNERLINK_ATTRIBUTE;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.PARTNERLINK_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.PARTNERROLE_ATTR_AND_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.PART_ATTR;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.PORTTYPE_ATTRIBUTE;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.TO_ELEMENT;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.VARIABLE_ATTR;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createAssign;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createBPELElement;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.createVariable;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.getProcessNamespace;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.insertVariable;

import org.jdom.Element;

/**
 * Factory for BPEL XML Elements
 * 
 * @author Alex
 */

public class CMServiceFactory {

	private static CMServiceFactory instance = null;

	public static CMServiceFactory getInstance() {
		if (instance == null) {
			instance = new CMServiceFactory();
		}
		return instance;
	}

	private CMServiceFactory() {
	}

	/**
	 * F�gt Namespace und Partner Link f�r Coverage Logging Service.
	 * 
	 * @param process_element
	 *            Wurzelelement des BPEL-Prozesses.
	 */
	public void prepareBPELFile(Element process_element) {
		process_element.addNamespaceDeclaration(COVERAGETOOL_NAMESPACE);
		insertPartnerLink(process_element);
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

	/**
	 * 
	 * @param variable
	 *            inputVariable
	 * @return Invoke-Element
	 */
	public Element createInvokeElementForLoggingService(String variable) {

		Element invoke = createBPELElement(BasicActivities.INVOKE_ACTIVITY);
		invoke.setAttribute(INPUTVARIABLE_ATTR, variable);
		invoke.setAttribute(OPERATION_ATTRIBUTE, REPORT_OPERATION);
		invoke.setAttribute(PARTNERLINK_ATTRIBUTE, PARTNERLINK_NAME);
		invoke.setAttribute(PORTTYPE_ATTRIBUTE, REPORTING_SERVICE_PORT);
		return invoke;
	}

	/**
	 * 
	 * @param content
	 *            Inhalt, der zugeordnet werden soll.
	 * @param variable
	 *            an die zugeordent wird.
	 * @return Assign-Element
	 */
	public Element createAssignElement(String content, String variable) {
		insertVariable(createVariable(variable,
				MESSAGETYPE_OF_REPORTING_MESSAGE, null), null);

		Element from = createBPELElement(FROM_ELEMENT);
		if (getProcessNamespace().equals(NAMESPACE_BPEL_1_1)) {
			from.setText(content);
		} else if (getProcessNamespace().equals(NAMESPACE_BPEL_2_0)) {
			Element literal = createBPELElement(LITERAL_ELEMENT);
			literal.setText(content);
			from.addContent(literal);
		}

		Element to = createBPELElement(TO_ELEMENT);
		to.setAttribute(PART_ATTR, PART_OF_REPORTING_MESSAGE);
		to.setAttribute(VARIABLE_ATTR, variable);
		return createAssign(from, to);
	}
}
