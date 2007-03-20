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

	private static final String LOGGING_SERVICE_OPERATION = "log";

	public CMServiceFactory(Element process_element) {
		prepareBPELFile(process_element);
	}


	private void prepareBPELFile(Element process_element) {
		process_element.addNamespaceDeclaration(LOG_SERVICE_NAMESPACE);
		int index = insertPartnerLink(process_element);
		insertVariable(process_element, index);
	}

	private void insertVariable(Element process_element, int index) {
		Element variable = new Element("variable", BpelXMLTools
				.getBpelNamespace());
		variable.setAttribute("messageType", "log:logRequest");
		variable.setAttribute("name", "logRequest");
		Element variables = process_element.getChild("variables",
				BpelXMLTools.getBpelNamespace());
		if (variables == null) {
			variables = new Element("variables", BpelXMLTools
					.getBpelNamespace());
			process_element.addContent(index + 1, variables);
		}
		variables.addContent(variable);
	}

	private int insertPartnerLink(Element process_element) {
		Element partnerLinks = process_element.getChild("partnerLinks",
				BpelXMLTools.getBpelNamespace());
		Element partnerLink = new Element("partnerLink", BpelXMLTools
				.getBpelNamespace());
		partnerLink.setAttribute("name", "PLT_LogService_");
		partnerLink.setAttribute("partnerLinkType",LOG_SERVICE_NAMESPACE.getPrefix()+ ":PLT_LogService_");
		partnerLink.setAttribute("partnerRole", "Logger");
		partnerLinks.addContent(partnerLink);
		int index = process_element.indexOf(partnerLinks);
		return index;
	}

	public Element createInvokeElement() {

		Element invoke = new Element(BasisActivity.INVOKE_ACTIVITY,
				BpelXMLTools.getBpelNamespace());
		// Element variableElement =
		// BpelXMLTools.createVariable(element.getDocument());
		// BpelXMLTools.insertVariable(variable, element.getDocument()
		// .getRootElement());
		invoke.setAttribute("inputVariable", "logRequest");
		invoke.setAttribute("operation",
				CMServiceFactory.LOGGING_SERVICE_OPERATION);
		invoke.setAttribute("partnerLink",
				CMServiceFactory.LOGGING_SERVICE_PORTTYPE);
		invoke.setAttribute("portType",
				LOG_SERVICE_NAMESPACE.getPrefix()+":"+CMServiceFactory.LOGGING_SERVICE_PORT);
		return invoke;
	}

	public Element createAssignElement(String content) {
		Element from = new Element("from", BpelXMLTools.getBpelNamespace());
		Element literal = new Element("literal", BpelXMLTools
				.getBpelNamespace());
		literal.setText(content);
		from.addContent(literal);
		Element to = new Element("to", BpelXMLTools.getBpelNamespace());
		to.setAttribute("part", "logEntry");
		to.setAttribute("variable", "logRequest");
		return BpelXMLTools.createAssign(from, to);
	}

}
