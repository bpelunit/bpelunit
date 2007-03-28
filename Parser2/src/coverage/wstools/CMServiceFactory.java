package coverage.wstools;

import org.jdom.Element;
import org.jdom.Namespace;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;

public class CMServiceFactory {

	private static final Namespace LOG_SERVICE_NAMESPACE = Namespace
			.getNamespace("log", "http://www.bpelunit.org/coverage/logService");

	private static final String LOGGING_SERVICE_PORT = "_LogService_";

	private static final String LOGGING_SERVICE_PORTTYPE = "PLT_LogService_";

	private static final String LOG_OPERATION = "log";

	private static final String REGISTER_COVERAGE_LABELS_OPERATION = "registerCoverageLabels";

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
		process_element.addNamespaceDeclaration(LOG_SERVICE_NAMESPACE);
		insertPartnerLink(process_element);
	}

	public void insertVariableForRegisterMarker(Element scope,
			String variableName) {
		BpelXMLTools.insertVariable(BpelXMLTools.createVariable(variableName,
				LOG_SERVICE_NAMESPACE.getPrefix() + ":CoverageLabels",null), scope);

	}

	private void insertPartnerLink(Element process_element) {
		Element partnerLinks = process_element.getChild("partnerLinks",
				BpelXMLTools.getBpelNamespace());
		Element partnerLink = new Element("partnerLink", BpelXMLTools
				.getBpelNamespace());
		partnerLink.setAttribute("name", "PLT_LogService_");
		partnerLink.setAttribute("partnerLinkType", LOG_SERVICE_NAMESPACE
				.getPrefix()
				+ ":PLT_LogService_");
		partnerLink.setAttribute("partnerRole", "Logger");
		partnerLinks.addContent(partnerLink);
	}

	public Element createInvokeElementForLog(String variable) {

		Element invoke = new Element(BasisActivity.INVOKE_ACTIVITY,
				BpelXMLTools.getBpelNamespace());
		// Element variableElement =
		// BpelXMLTools.createVariable(element.getDocument());
		// BpelXMLTools.insertVariable(variable, element.getDocument()
		// .getRootElement());
		invoke.setAttribute("inputVariable", variable);
		invoke.setAttribute("operation", CMServiceFactory.LOG_OPERATION);
		invoke.setAttribute("partnerLink",
				CMServiceFactory.LOGGING_SERVICE_PORTTYPE);
		invoke.setAttribute("portType", LOG_SERVICE_NAMESPACE.getPrefix() + ":"
				+ CMServiceFactory.LOGGING_SERVICE_PORT);
		return invoke;
	}

	public Element createInvokeElementForRegisterMarker(String variable) {

		Element invoke = new Element(BasisActivity.INVOKE_ACTIVITY,
				BpelXMLTools.getBpelNamespace());
		// Element variableElement =
		// BpelXMLTools.createVariable(element.getDocument());
		// BpelXMLTools.insertVariable(variable, element.getDocument()
		// .getRootElement());
		invoke.setAttribute("inputVariable", variable);
		invoke.setAttribute("operation",
				CMServiceFactory.REGISTER_COVERAGE_LABELS_OPERATION);
		invoke.setAttribute("partnerLink",
				CMServiceFactory.LOGGING_SERVICE_PORTTYPE);
		invoke.setAttribute("portType", LOG_SERVICE_NAMESPACE.getPrefix() + ":"
				+ CMServiceFactory.LOGGING_SERVICE_PORT);
		return invoke;
	}

	public Element createAssignElement(String content, String variable) {

		BpelXMLTools.insertVariable(BpelXMLTools.createVariable(variable,
				LOG_SERVICE_NAMESPACE.getPrefix() + ":logRequest",null), null);

		Element from = new Element("from", BpelXMLTools.getBpelNamespace());
		Element literal = new Element("literal", BpelXMLTools
				.getBpelNamespace());
		literal.setText(content);
		from.addContent(literal);
		Element to = new Element("to", BpelXMLTools.getBpelNamespace());
		to.setAttribute("part", "logEntry");
		to.setAttribute("variable", variable);
		return BpelXMLTools.createAssign(from, to);
	}

	public Element createAssignElementForRegisterMarker(Element scope,
			String content, String variable) {
		insertVariableForRegisterMarker(scope, variable);
		Element from = new Element("from", BpelXMLTools.getBpelNamespace());

		from.setText(content);
		// from.addContent(literal);
		Element to = new Element("to", BpelXMLTools.getBpelNamespace());
		to.setAttribute("part", "registerEntries");
		to.setAttribute("variable", variable);
		return BpelXMLTools.createAssign(from, to);
	}

	public void insertVariableForRegisterMarker(String attributeValue) {
		insertVariableForRegisterMarker(null, attributeValue);
	}

}
