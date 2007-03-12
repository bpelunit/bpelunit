package coverage.instrumentation.bpelxmltools;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Die Klasse stellt zur Verfügung Methoden, mit denen man neue Elemente der
 * BPEL Sprache erzeugen und in den BPEL-Prozess einfügen.
 * 
 * @author Alex Salnikow
 */
public class BpelXMLTools {
	


	public static final Namespace NAMESPACE_BPEL_2 = Namespace
	.getNamespace("http://schemas.xmlsoap.org/ws/2003/03/business-process/");
	
	public static final String PROCESS_ELEMENT = "process";


	private static int count = 0;

	public static String namespacePrefix;

	public static Element process_element;

	private static final String VARIABLE_TAG = "variable";

	private static final String ATTRIBUTE_VARIABLE = "variable";

	private static final String VARIABLES_TAG = "variables";

	private static final String ASSIGN_TAG = "assign";

	private static final String COPY_TAG = "copy";

	private static final String FROM_TAG = "from";

	private static final String TO_TAG = "to";

	private static final String LITERAL_TAG = "literal";

	private static final String COUNT_VARIABLE_TYPE = "xsd:int";

	private static final String ATTRIBUTE_TYPE = "type";

	private static final String ATTRIBUTE_NAME = "name";

	private static final String VARIABLE_NAME = "_ZXYYXZ_";

	private static final String ELSE_ELEMENT = "else";
	
	private static final String IF_TAG = "if";

	private static final String CONDITION_TAG = "condition";
	
	public static Namespace getBpelNamespace(){
		return process_element.getNamespace();
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
	public static Element getFirstActivityChild(Element element) {
		Element activity = null;
		List children = element.getChildren();
		Element child;
		for (int i = 0; i < children.size(); i++) {
			child = (Element) children.get(i);
			if (isActivity(child)) {
				activity = child;
				break;
			}
		}
		return activity;
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
		Element flow = new Element(StructuredActivity.FLOW_ACTIVITY,getBpelNamespace());
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
	 * Erzeugt ein Variable-Element ohne es in BPEL einzufügen.
	 * 
	 * @param document
	 * @return
	 */
	public static Element createVariable(Document document) {
		Element variable = new Element(VARIABLE_TAG,
				getBpelNamespace());
		variable.setAttribute(ATTRIBUTE_NAME, createVariableName());
		variable.setAttribute(ATTRIBUTE_TYPE, COUNT_VARIABLE_TYPE);
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
		Element variables = scope.getChild(VARIABLES_TAG,
				getBpelNamespace());
		if (variables == null) {
			variables = new Element(VARIABLES_TAG,
					getBpelNamespace());
			scope.addContent(0, variables);
		}
		variables.addContent(variable);
	}

	/**
	 * Erzeugt ein Assign-Element für eine Count-Variable und setzt auf 0.
	 * 
	 * @param countVariable
	 * @return Assign-Element
	 */
	public static Element createInitializeAssign(Element countVariable) {
		Element assign = new Element(ASSIGN_TAG, getBpelNamespace());
		Element copy = new Element(COPY_TAG, getBpelNamespace());
		Element from = new Element(FROM_TAG, getBpelNamespace());
		Element to = new Element(TO_TAG, getBpelNamespace());
		Element literal = new Element(LITERAL_TAG,
				getBpelNamespace());
		literal.setText("0");
		from.addContent(literal);
		to.setAttribute(ATTRIBUTE_VARIABLE, countVariable.getAttributeValue(ATTRIBUTE_NAME));
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
		Element assign = new Element(ASSIGN_TAG, getBpelNamespace());
		Element copy = new Element(COPY_TAG,getBpelNamespace());
		Element from = new Element(FROM_TAG, getBpelNamespace());
		Element to = new Element(TO_TAG, getBpelNamespace());
		from.setText("$" + countVariable.getName() + " + 1");
		to.setAttribute(ATTRIBUTE_VARIABLE, countVariable.getAttributeValue(ATTRIBUTE_NAME));
		copy.addContent(from);
		copy.addContent(to);
		assign.addContent(copy);
		return assign;
	}

	private static String createVariableName() {
		return VARIABLE_NAME + (count++);
	}
	

	/**
	 * Fügt in das If-Element Else-Zweig ein.
	 * 
	 * @param element -
	 *            If-Element
	 * @return Else-Element
	 */
	public static Element insertElseBranch(Element element) {
		Element elseElement = new Element(ELSE_ELEMENT,
				getBpelNamespace());
		elseElement.addContent(BpelXMLTools.createSequence());
		element.addContent(elseElement);
		return elseElement;
	}
	public static Element createIfActivity(String conditionContent) {
		Element if_element= new Element(IF_TAG, getBpelNamespace());
		Element condition = new Element(CONDITION_TAG,
				getBpelNamespace());
		condition.setText(conditionContent);
		if_element.addContent(condition);
		return null;
	}
	
	public static Element createAssign(Element from,Element to){
		Element assign = new Element(ASSIGN_TAG,getBpelNamespace());
		Element copy = new Element(COPY_TAG, getBpelNamespace());
		copy.addContent(from);
		copy.addContent(to);
		assign.addContent(copy);
		return assign;
	}

}
