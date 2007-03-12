package coverage.loggingservice;

import org.jdom.Element;
import org.jdom.Namespace;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;

public class LoggingServiceConfiguration {

	private static final Namespace LOG_SERVICE_NAMESPACE = Namespace
			.getNamespace("log", "http://www.bpelunit.org/coverage/logService");

	private static final String LOGGING_SERVICE_PORT = "_LogService_";

	private static final String LOGGING_SERVICE_PORTTYPE = "PLT_LogService_";

	private static final String LOGGING_SERVICE_OPERATION = "log";

	public LoggingServiceConfiguration(String file,Element process_element) {
		loadConfiguration(file);
		prepareBPELFile(process_element);
	}

	private void loadConfiguration(String file) {
		
	}

	private void prepareBPELFile(Element process_element) {
		process_element.addNamespaceDeclaration(LOG_SERVICE_NAMESPACE);
		int index = insertPartnerLink(process_element);
		insertVariable(process_element, index);
	}

	private void insertVariable(Element process_element, int index) {
		Element variable = new Element("variable", BpelXMLTools
				.NAMESPACE_BPEL_2);
		variable.setAttribute("messageType", "logRequest",LOG_SERVICE_NAMESPACE);
		variable.setAttribute("name", "logRequest");
		Element variables = process_element.getChild("variables",
				BpelXMLTools.NAMESPACE_BPEL_2);
		if (variables == null) {
			variables = new Element("variables", BpelXMLTools
					.NAMESPACE_BPEL_2);
			process_element.addContent(index + 1, variables);
		}
		variables.addContent(variable);
	}

	private int insertPartnerLink(Element process_element) {
		Element partnerLinks = process_element.getChild("partnerLinks",
				BpelXMLTools.NAMESPACE_BPEL_2);
		Element partnerLink = new Element("partnerLink", BpelXMLTools
				.NAMESPACE_BPEL_2);
		partnerLink.setAttribute("name", "PLT_LogService_");
		partnerLink.setAttribute("partnerLinkType", "PLT_LogService_",LOG_SERVICE_NAMESPACE);
		partnerLink.setAttribute("partnerRole", "Logger");
		partnerLinks.addContent(partnerLink);
		int index = process_element.indexOf(partnerLinks);
		return index;
	}

	public Element createInvokeElement() {

		Element invoke = new Element(BasisActivity.INVOKE_ACTIVITY,
				BpelXMLTools.NAMESPACE_BPEL_2);
		// Element variableElement =
		// BpelXMLTools.createVariable(element.getDocument());
		// BpelXMLTools.insertVariable(variable, element.getDocument()
		// .getRootElement());
		invoke.setAttribute("inputVariable", "logRequest");
		invoke.setAttribute("operation",
				LoggingServiceConfiguration.LOGGING_SERVICE_OPERATION);
		invoke.setAttribute("partnerLink",
				LoggingServiceConfiguration.LOGGING_SERVICE_PORTTYPE);
		invoke.setAttribute("portType",
				LoggingServiceConfiguration.LOGGING_SERVICE_PORT,
				LoggingServiceConfiguration.LOG_SERVICE_NAMESPACE);
		return invoke;
	}

	public Element createAssignElement(String content) {
		Element from = new Element("from", BpelXMLTools.NAMESPACE_BPEL_2);
		Element literal = new Element("literal", BpelXMLTools
				.NAMESPACE_BPEL_2);
		literal.setText(content);
		from.addContent(literal);
		Element to = new Element("to", BpelXMLTools.NAMESPACE_BPEL_2);
		to.setAttribute("part", "logEntry");
		to.setAttribute("variable", "logEntry");
		return BpelXMLTools.createAssign(from, to);
	}

}
