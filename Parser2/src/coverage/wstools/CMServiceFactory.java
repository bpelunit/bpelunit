package coverage.wstools;

import static coverage.instrumentation.bpelxmltools.BpelXMLTools.*;

import org.jdom.Element;

import coverage.CoverageConstants;
import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;

public class CMServiceFactory {

	private static final String REPORTING_SERVICE_PORT = CoverageConstants.COVERAGETOOL_NAMESPACE
			.getPrefix()
			+ ":" + "_LogService_";

	private static final String REPORTING_SERVICE_PORTTYPE = CoverageConstants.COVERAGETOOL_NAMESPACE
			.getPrefix()
			+ ":" + "PLT_LogService_";

	private static final String PARTNERLINKNAME = "PLT_LogService_";

	private static final String REPORT_OPERATION = "log";

	private static final String REGISTER_COVERAGE_LABELS_OPERATION = "registerCoverageLabels";

	private static final String PARTNER_ROLE = "ReportListener";

	private static final String PART_OF_REPORTING_MESSAGE = "logEntry";

	private static final String PART_OF_REGISTER_MESSAGE = "registerEntries";

	private static final String MESSAGETYPE_OF_REPORTING_MESSAGE = CoverageConstants.COVERAGETOOL_NAMESPACE
			.getPrefix()
			+ ":" + "logRequest";

	private static final String MESSAGETYPE_OF_REGISTER_MESSAGE = CoverageConstants.COVERAGETOOL_NAMESPACE
			.getPrefix()
			+ ":" + "CoverageLabels";

	private static CMServiceFactory instance = null;

	public static CMServiceFactory getInstance() {
		if (instance == null) {
			instance = new CMServiceFactory();
		}
		return instance;
	}

	private CMServiceFactory() {
		prepareBPELFile(BpelXMLTools.process_element);
	}

	private void prepareBPELFile(Element process_element) {
		process_element
				.addNamespaceDeclaration(CoverageConstants.COVERAGETOOL_NAMESPACE);
		insertPartnerLink(process_element);
	}

	public void insertVariableForRegisterMarker(Element scope,
			String variableName) {
		insertVariable(createVariable(variableName,
				MESSAGETYPE_OF_REGISTER_MESSAGE, null), scope);

	}

	private void insertPartnerLink(Element process_element) {
		Element partnerLinks = process_element.getChild(PARTNERLINKS_ELEMENT,
				getBpelNamespace());
		Element partnerLink = createBPELElement(PARTNERLINK_ELEMENT);
		partnerLink.setAttribute(NAME_ATTRIBUTE, PARTNERLINKNAME);
		partnerLink.setAttribute(PARTNERLINKTYPE_ATTRIBUTE,
				REPORTING_SERVICE_PORTTYPE);
		partnerLink.setAttribute(PARTNERROLE_ATTRIBUTE, PARTNER_ROLE);
		partnerLinks.addContent(partnerLink);
	}

	public Element createInvokeElementForLog(String variable) {

		Element invoke = createBPELElement(BasisActivity.INVOKE_ACTIVITY);
		// Element variableElement =
		// BpelXMLTools.createVariable(element.getDocument());
		// BpelXMLTools.insertVariable(variable, element.getDocument()
		// .getRootElement());
		invoke.setAttribute(INPUTVARIABLE_ATTRIBUTE, variable);
		invoke.setAttribute(OPERATION_ATTRIBUTE, REPORT_OPERATION);
		invoke.setAttribute(PARTNERLINK_ATTRIBUTE, PARTNERLINKNAME);
		invoke.setAttribute(PORTTYPE_ATTRIBUTE, REPORTING_SERVICE_PORT);
		return invoke;
	}

	public Element createInvokeElementForRegisterMarker(String variable) {

		Element invoke = createBPELElement(BasisActivity.INVOKE_ACTIVITY);
		// Element variableElement =
		// BpelXMLTools.createVariable(element.getDocument());
		// BpelXMLTools.insertVariable(variable, element.getDocument()
		// .getRootElement());
		invoke.setAttribute(INPUTVARIABLE_ATTRIBUTE, variable);
		invoke.setAttribute(OPERATION_ATTRIBUTE,
				CMServiceFactory.REGISTER_COVERAGE_LABELS_OPERATION);
		invoke.setAttribute(PARTNERLINK_ATTRIBUTE, PARTNERLINKNAME);
		invoke.setAttribute(PORTTYPE_ATTRIBUTE,
				CMServiceFactory.REPORTING_SERVICE_PORT);
		return invoke;
	}

	public Element createAssignElement(String content, String variable) {

		insertVariable(createVariable(variable,
				MESSAGETYPE_OF_REPORTING_MESSAGE, null), null);

		Element from = createBPELElement(FROM_ELEMENT);
		Element literal = createBPELElement(LITERAL_ELEMENT);
		literal.setText(content);
		from.addContent(literal);
		Element to = createBPELElement(TO_ELEMENT);
		to.setAttribute(PART_ATTRIBUTE, PART_OF_REPORTING_MESSAGE);
		to.setAttribute(VARIABLE_ATTRIBUTE, variable);
		return createAssign(from, to);
	}

	public Element createAssignElementForRegisterMarker(Element scope,
			String content, String variable) {
		insertVariableForRegisterMarker(scope, variable);
		Element from = createBPELElement(FROM_ELEMENT);

		from.setText(content);
		Element to = createBPELElement(TO_ELEMENT);
		to.setAttribute(PART_ATTRIBUTE, PART_OF_REGISTER_MESSAGE);
		to.setAttribute(VARIABLE_ATTRIBUTE, variable);
		return createAssign(from, to);
	}

	public void insertVariableForRegisterMarker(String attributeValue) {
		insertVariableForRegisterMarker(null, attributeValue);
	}

}
