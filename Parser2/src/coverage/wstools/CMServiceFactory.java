package coverage.wstools;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.bpelxmltools.BpelXMLTools;

public class CMServiceFactory {

	private static final Namespace LOG_SERVICE_NAMESPACE = Namespace
			.getNamespace("log", "http://www.bpelunit.org/coverage/logService");

	private static final String LOGGING_SERVICE_PORT = "_LogService_";

	private static final String LOGGING_SERVICE_PORTTYPE = "PLT_LogService_";

	private static final String LOGGING_SERVICE_OPERATION = "log";

	private Element process_element;

	public CMServiceFactory(Element process_element) {
		this.process_element = process_element;
		prepareBPELFile(process_element);
	}

	private void prepareBPELFile(Element process_element) {
		process_element.addNamespaceDeclaration(LOG_SERVICE_NAMESPACE);
		int index = insertPartnerLink(process_element);

		Element variables = process_element.getChild("variables", BpelXMLTools
				.getBpelNamespace());
		if (variables == null) {
			variables = new Element("variables", BpelXMLTools
					.getBpelNamespace());
			process_element.addContent(index + 1, variables);
		}
		// insertVariable(process_element, index);
	}

	private void insertVariable(Element process_element, String variableName) {

		Element variables = process_element.getChild("variables", BpelXMLTools
				.getBpelNamespace());
		if (!containVariable(variables, variableName)) {
			Element variable = new Element("variable", BpelXMLTools
					.getBpelNamespace());
			variable.setAttribute("messageType", "log:logRequest");
			variable.setAttribute("name", variableName);
			variables.addContent(variable);
		}
	}

	private boolean containVariable(Element variables, String variableName) {
		boolean result = false;
		List<Element> allVariables = variables.getContent(new ContentFilter(
				ContentFilter.ELEMENT));
		Element element;
		for (Iterator<Element> iter = allVariables.iterator(); iter.hasNext();) {
			element = iter.next();
			String nameAttribute = element.getAttributeValue("name");
			if (nameAttribute != null && nameAttribute.equals(variableName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	private int insertPartnerLink(Element process_element) {
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
		int index = process_element.indexOf(partnerLinks);
		return index;
	}

	public Element createInvokeElement(String variable) {

		Element invoke = new Element(BasisActivity.INVOKE_ACTIVITY,
				BpelXMLTools.getBpelNamespace());
		// Element variableElement =
		// BpelXMLTools.createVariable(element.getDocument());
		// BpelXMLTools.insertVariable(variable, element.getDocument()
		// .getRootElement());
		invoke.setAttribute("inputVariable", variable);
		invoke.setAttribute("operation",
				CMServiceFactory.LOGGING_SERVICE_OPERATION);
		invoke.setAttribute("partnerLink",
				CMServiceFactory.LOGGING_SERVICE_PORTTYPE);
		invoke.setAttribute("portType", LOG_SERVICE_NAMESPACE.getPrefix() + ":"
				+ CMServiceFactory.LOGGING_SERVICE_PORT);
		return invoke;
	}

	public Element createAssignElement(String content, String variable) {
		insertVariable(process_element, variable);
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

}
