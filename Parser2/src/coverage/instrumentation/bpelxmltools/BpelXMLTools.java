package coverage.instrumentation.bpelxmltools;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import coverage.CoverageConstants;
import coverage.instrumentation.bpelxmltools.exprlang.impl.XpathLanguage;

/**
 * Die Klasse stellt zur Verfügung Methoden, mit denen man neue Elemente der
 * BPEL Sprache erzeugen und in den BPEL-Prozess einfügen.
 * 
 * @author Alex Salnikow
 */
public class BpelXMLTools {

	public static final Namespace NAMESPACE_BPEL_2 = Namespace
			.getNamespace("http://schemas.xmlsoap.org/ws/2003/03/business-process/");

	public static Element process_element;

	/* Elements from namespace of BPEL */

	public static final String PROCESS_ELEMENT = "process";

	public static final String VARIABLE_ELEMENT = "variable";

	private static final String VARIABLES_ELEMENT = "variables";

	public static final String ASSIGN_ELEMENT = "assign";

	public static final String COPY_ELEMENT = "copy";

	public static final String FROM_ELEMENT = "from";

	public static final String TO_ELEMENT = "to";

	public static final String LITERAL_ELEMENT = "literal";

	public static final String IF_ELEMENT = "if";

	public static final String CONDITION_ELEMENT = "condition";

	public static final String ELSE_ELEMENT = "else";

	public static final String TARGETS_ELEMENT = "targets";

	public static final String ELSE_IF_ELEMENT = "elseif";

	public static final String INT_VARIABLE_TYPE = "xsd:int";

	public static final String STRING_VARIABLE_TYPE = "xsd:string";

	/* Attributes of BPEL */

	public static final String VARIABLE_ATTRIBUTE = "variable";

	public static final String TYPE_ATTRIBUTE = "type";

	public static final String NAME_ATTRIBUTE = "name";

	public static final String MESSAGETYPE_ATTRIBUTE = "messageType";

	public static final String EXPRESSION_LANGUAGE_ATTRIBUTE = "expressionLanguage";

	private static final String PREFIX_FOR_NEW_VARIABLE = "_ZXYYXZ_";

	private static int count = 0;

	public static Namespace getBpelNamespace() {
		return process_element.getNamespace();
	}

	public static String createVariableName() {
		return PREFIX_FOR_NEW_VARIABLE + (count++);
	}

	public static Element createVariable(String name, String messageType,
			String type) {
		if (name == null) {
			name = createVariableName();
		}
		Element variable = createBPELElement(BpelXMLTools.VARIABLE_ELEMENT);
		if (type != null)
			variable.setAttribute(TYPE_ATTRIBUTE, type);
		if (messageType != null)
			variable.setAttribute(MESSAGETYPE_ATTRIBUTE, messageType);
		variable.setAttribute(BpelXMLTools.NAME_ATTRIBUTE, name);
		return variable;
	}

	public static Element insertNewStringVariable(String variableName,
			Element scope) {
		if (variableName == null) {
			variableName = createVariableName();
		}
		Element variable = new Element(VARIABLE_ELEMENT, getBpelNamespace());
		variable.setAttribute(NAME_ATTRIBUTE, variableName);
		variable.setAttribute(TYPE_ATTRIBUTE, STRING_VARIABLE_TYPE);
		insertVariable(variable, scope);
		return variable;
	}

	/**
	 * Erzeugt ein Variable-Element ohne es in BPEL einzufügen.
	 * 
	 * @param document
	 * @return
	 */
	public static Element insertNewIntVariable(Element scope, String name) {
		if (name == null) {
			name = createVariableName();
		}
		Element variable = createBPELElement(VARIABLE_ELEMENT);
		variable.setAttribute(NAME_ATTRIBUTE, name);
		variable.setAttribute(TYPE_ATTRIBUTE, INT_VARIABLE_TYPE);
		insertVariable(variable, scope);
		return variable;
	}

	/**
	 * Fügt Variable in dem Scope ein. Wenn ein Varables-Element fehlt, dann
	 * wird ein hinzugefügt.
	 * 
	 * @param variable
	 * @param scope
	 */
	public static void insertVariable(Element variable, Element scope) {
		if (scope == null) {
			scope = process_element;
		}
		Element variables = scope.getChild(VARIABLES_ELEMENT,
				getBpelNamespace());
		if (variables == null) {
			variables = new Element(VARIABLES_ELEMENT, getBpelNamespace());
			scope.addContent(0, variables);
		}
		List allVariables = variables.getChildren(VARIABLE_ELEMENT,
				BpelXMLTools.getBpelNamespace());
		boolean exist = false;
		String variableName = variable.getAttributeValue(NAME_ATTRIBUTE);
		for (Iterator iter = allVariables.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			if (element.getAttributeValue(NAME_ATTRIBUTE).equals(variableName)) {
				exist = true;
				break;
			}
		}
		if (!exist)
			variables.addContent(variable);
	}

	/**
	 * Schließt das Element in eine Sequence ein.
	 * 
	 * @param activity:
	 *            Element, das in eine Sequence eingeschloßen werden soll.
	 * @return Umschließende Sequence-Element
	 */
	public static Element encloseInSequence(Element activity) {
		Element parent = activity.getParentElement();
		int index = parent.indexOf(activity);
		Element sequence = createSequence();
		sequence.addContent(activity.detach());
		parent.addContent(index, sequence);
		activity = sequence;
		return activity;
	}

	/**
	 * Überprüft, ob das Element in Sequence eingeschloßen oder selbst Sequence
	 * ist. Wenn nicht, dann wird es in ein neues Sequence-Element
	 * eingeschloßen.
	 * 
	 * @param activity
	 * @return Sequence-Element
	 */
	public static Element ensureElementIsInSequence(Element activity) {
		Element parent = activity.getParentElement();
		if (parent.getName().equals(StructuredActivity.SEQUENCE_ACTIVITY)) {
			activity = parent;
		} else {
			activity = encloseInSequence(activity);
		}
		return activity;
	}

	/**
	 * Überprüft, ob das Element in Sequence eingeschloßen oder selbst Sequence
	 * ist. Wenn nicht, dann wird es in ein neues Sequence-Element
	 * eingeschloßen.
	 * 
	 * @param activity
	 * @return Sequence-Element
	 */
	public static boolean isSequence(Element activity) {
		return activity.getName().equals(StructuredActivity.SEQUENCE_ACTIVITY);
	}

	/**
	 * Sucht bei dem übergebenen Element innerhalb seiner direkten
	 * Kind-Elementen nach der ersten Aktivität und gibt diese zurück.
	 * 
	 * @param element
	 * @return Das erste Kind-Element, das eine Aktivität ist. Falls nicht
	 *         vorhanden, dann null;
	 */
	public static Element getFirstEnclosedActivity(Element element) {
		Element activity = null;
		List children = element.getContent(new ContentFilter(
				ContentFilter.ELEMENT));
		Element child;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			if (isActivity(child)) {
				activity = child;
				break;
			} else if (isScope(child)) {
				activity = getFirstEnclosedActivity(child);
			}
		}
		return activity;
	}

	public static boolean isScope(Element child) {
		if (child.getName().equals(StructuredActivity.SCOPE_ACTIVITY)) {
			return true;
		}
		return false;
	}

	/**
	 * Schließt das Element in eine Flow ein.
	 * 
	 * @param activity:
	 *            Element, das in eine Flow eingeschloßen werden soll.
	 * @return Umschließende Flow-Element
	 */
	public static Element encloseElementInFlow(Element activity) {
		Element parent = activity.getParentElement();
		int index = parent.indexOf(activity);
		Element flow = createBPELElement(StructuredActivity.FLOW_ACTIVITY);
		activity.detach();
		flow.addContent(activity);
		parent.addContent(index, flow);
		return flow;
	}

	/**
	 * Überprüft, ob das Element selbst Flow ist. Wenn nicht, dann wird es in
	 * ein neues Flow-Element eingeschloßen.
	 * 
	 * @param activity
	 * @return Flow-Element
	 */
	public static Element ensureElementIsInFlow(Element activity) {

		Element parent = activity.getParentElement();
		if (!parent.getName().equals(StructuredActivity.FLOW_ACTIVITY)) {
			activity = encloseElementInFlow(activity);
		}
		return activity;
	}

	public static boolean isFlow(Element activity) {
		return activity.getName().equals(StructuredActivity.FLOW_ACTIVITY);
	}

	/**
	 * Erzeugt ein Sequence-Element
	 * 
	 * @return
	 */
	public static Element createSequence() {
		return new Element(StructuredActivity.SEQUENCE_ACTIVITY,
				getBpelNamespace());
	}

	public static boolean isStructuredActivity(Element activity) {
		return StructuredActivity.isStructuredActivity(activity);
	}

	public static boolean isBasicActivity(Element activity) {
		return BasisActivity.isBasisActivity(activity);
	}

	public static boolean isActivity(Element element) {
		return isBasicActivity(element) || isStructuredActivity(element);
	}

	/**
	 * Erzeugt ein Assign-Element für eine Count-Variable und setzt auf 0.
	 * 
	 * @param countVariable
	 * @return Assign-Element
	 */
	public static Element createInitializeAssign(Element countVariable) {
		Element assign = createBPELElement(ASSIGN_ELEMENT);
		Element copy = createBPELElement(COPY_ELEMENT);
		Element from = createBPELElement(FROM_ELEMENT);
		Element to = createBPELElement(TO_ELEMENT);
		Element literal = createBPELElement(LITERAL_ELEMENT);
		literal.setText("0");
		from.addContent(literal);
		to.setAttribute(VARIABLE_ATTRIBUTE, countVariable
				.getAttributeValue(NAME_ATTRIBUTE));
		copy.addContent(from);
		copy.addContent(to);
		assign.addContent(copy);
		return assign;
	}

	/**
	 * Erzeugt ein Assign-Element für die Erhöhung der Count-Variable um 1.
	 * 
	 * @param countVariable
	 * @return Assign-Element
	 */
	public static Element createIncreesAssign(Element countVariable) {
		Element assign = createBPELElement(ASSIGN_ELEMENT);
		Element copy = createBPELElement(COPY_ELEMENT);
		Element from = createBPELElement(FROM_ELEMENT);
		Element to = createBPELElement(TO_ELEMENT);
		from.setText(ExpressionLanguage.getInstance(
				CoverageConstants.EXPRESSION_LANGUAGE).valueOf(
				countVariable.getAttributeValue(NAME_ATTRIBUTE))
				+ " + 1");
		to.setAttribute(VARIABLE_ATTRIBUTE, countVariable
				.getAttributeValue(NAME_ATTRIBUTE));
		copy.addContent(from);
		copy.addContent(to);
		assign.addContent(copy);
		return assign;
	}

	/**
	 * Fügt in das If-Element Else-Zweig ein.
	 * 
	 * @param element -
	 *            If-Element
	 * @return Else-Element
	 */
	public static Element insertElseBranch(Element element) {
		Element elseElement = createBPELElement(ELSE_ELEMENT);
		elseElement.addContent(BpelXMLTools.createSequence());
		element.addContent(elseElement);
		return elseElement;
	}

	public static Element createIfActivity(String conditionContent) {
		Element if_element = createBPELElement(IF_ELEMENT);
		Element condition = createBPELElement(CONDITION_ELEMENT);
		condition.setAttribute("expressionLanguage",
				XpathLanguage.LANGUAGE_SPEZIFIKATION);
		condition.setText(conditionContent);
		if_element.addContent(condition);
		return if_element;
	}

	public static Element createAssign(Element from, Element to) {
		Element assign = createBPELElement(ASSIGN_ELEMENT);
		addCopyElement(assign, from, to);
		return assign;
	}

	public static void addCopyElement(Element assign, Element from, Element to) {
		Element copy = createBPELElement(COPY_ELEMENT);
		copy.addContent(from);
		copy.addContent(to);
		assign.addContent(copy);
	}

	public static void sysout(Element element) {
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutputter.output(element, System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sysout(Document doc) {
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutputter.output(doc, System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Element getSurroundScope(Content content) {
		Element scope = null;
		Element parent = content.getParentElement();
		String name;
		while (scope == null && parent != null) {
			name = parent.getName();
			if (name.equals(StructuredActivity.SCOPE_ACTIVITY)
					|| name.equals("process")) {
				scope = parent;
				break;
			} else {
				parent = parent.getParentElement();
			}
		}
		return scope;
	}

	public static Element createBPELElement(String name) {
		return new Element(name, getBpelNamespace());
	}

}
