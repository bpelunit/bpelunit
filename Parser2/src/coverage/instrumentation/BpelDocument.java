package coverage.instrumentation;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

public class BpelDocument {

	private static int count = 0;

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

	public static Element createVariable(Document document) {
		Element process = document.getRootElement();
		Element variable = new Element(VARIABLE_TAG, process.getNamespace());
		variable.setAttribute(ATTRIBUTE_NAME, createVariableName());
		variable.setAttribute(ATTRIBUTE_TYPE, COUNT_VARIABLE_TYPE);
		return variable;
	}
	
	
	public static void insertVariable(Element variable,Element process){
		Element variables=process.getChild(VARIABLES_TAG,ActivityTools.NAMESPACE_BPEL_2);
		if(variables==null){
			variables=new Element(VARIABLES_TAG,ActivityTools.NAMESPACE_BPEL_2);
			process.addContent(0,variables);
		}
		variables.addContent(variable);
	}

	public static Element createInitializeAssign(Element countVariable) {
		Element assign = new Element(ASSIGN_TAG,ActivityTools.NAMESPACE_BPEL_2);
		Element copy = new Element(COPY_TAG,ActivityTools.NAMESPACE_BPEL_2);
		Element from = new Element(FROM_TAG,ActivityTools.NAMESPACE_BPEL_2);
		Element to = new Element(TO_TAG,ActivityTools.NAMESPACE_BPEL_2);
		Element literal = new Element(LITERAL_TAG,ActivityTools.NAMESPACE_BPEL_2);
		literal.setText("0");
		from.addContent(literal);
		to.setAttribute(ATTRIBUTE_VARIABLE, countVariable.getName());
		copy.addContent(from);
		copy.addContent(to);
		assign.addContent(copy);
		return assign;
	}
	
	public static Element createIncreesAssign(Element countVariable) {
		Element assign = new Element(ASSIGN_TAG,ActivityTools.NAMESPACE_BPEL_2);
		Element copy = new Element(COPY_TAG,ActivityTools.NAMESPACE_BPEL_2);
		Element from = new Element(FROM_TAG,ActivityTools.NAMESPACE_BPEL_2);
		Element to = new Element(TO_TAG,ActivityTools.NAMESPACE_BPEL_2);
		from.setText("$"+countVariable.getName()+" + 1");
		to.setAttribute(ATTRIBUTE_VARIABLE, countVariable.getName());
		copy.addContent(from);
		copy.addContent(to);
		assign.addContent(copy);
		return assign;
	}
	
	private static String createVariableName(){
		return VARIABLE_NAME+(count++);
	}
}
