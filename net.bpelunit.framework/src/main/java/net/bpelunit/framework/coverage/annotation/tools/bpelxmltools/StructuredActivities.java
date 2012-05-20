package net.bpelunit.framework.coverage.annotation.tools.bpelxmltools;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;

import java.util.Hashtable;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Die Klasse repräsentiert die Strukturierten Aktivitäten.
 * 
 * @author Alex Salnikow
 *
 */
public final class StructuredActivities {

	private StructuredActivities() {
	}
	
	public static final String SEQUENCE_ACTIVITY = "sequence";

	public static final String IF_ACTIVITY = "if";

	public static final String WHILE_ACTIVITY = "while";

	public static final String REPEATUNTIL_ACTIVITY = "repeatUntil";

	public static final String FOREACH_ACTIVITY = "forEach";

	public static final String PICK_ACTIVITY = "pick";

	public static final String FLOW_ACTIVITY = "flow";

	public static final String SCOPE_ACTIVITY = "scope";

	public static final String SWITCH_ACTIVITY = "switch";

	private static Hashtable<String, String> structuredActivities;

	/**
	 * Überprüft, ob das Element eine BPEL-StructuredActivität repräsentiert.
	 * 
	 * @param element
	 * @return
	 */
	static boolean isStructuredActivity(Element activity) {
		return structuredActivities.containsKey(activity.getName());
	}

	/**
	 * Legt die Aktivitäten fest abhängig von der BPEL-Version (1.1 oder 2.0)
	 *
	 */
	public static void initialize() {
		Namespace bpelNamespace = BpelXMLTools.getProcessNamespace();
		if (bpelNamespace.equals(NAMESPACE_BPEL_2_0)) {
			structuredActivities = new Hashtable<String, String>();
			structuredActivities.put(SEQUENCE_ACTIVITY, SEQUENCE_ACTIVITY);
			structuredActivities.put(IF_ACTIVITY, IF_ACTIVITY);
			structuredActivities.put(WHILE_ACTIVITY, WHILE_ACTIVITY);
			structuredActivities.put(REPEATUNTIL_ACTIVITY,
					REPEATUNTIL_ACTIVITY);
			structuredActivities.put(FOREACH_ACTIVITY, FOREACH_ACTIVITY);
			structuredActivities.put(PICK_ACTIVITY, PICK_ACTIVITY);
			structuredActivities.put(FLOW_ACTIVITY, FLOW_ACTIVITY);
			structuredActivities.put(SCOPE_ACTIVITY,SCOPE_ACTIVITY);
		} else if (bpelNamespace.equals(NAMESPACE_BPEL_1_1)) {
			structuredActivities = new Hashtable<String, String>();
			structuredActivities.put(SEQUENCE_ACTIVITY, SEQUENCE_ACTIVITY);
			structuredActivities.put(WHILE_ACTIVITY, WHILE_ACTIVITY);
			structuredActivities.put(PICK_ACTIVITY, PICK_ACTIVITY);
			structuredActivities.put(FLOW_ACTIVITY, FLOW_ACTIVITY);
			structuredActivities.put(SWITCH_ACTIVITY, SWITCH_ACTIVITY);
			structuredActivities.put(SCOPE_ACTIVITY,SCOPE_ACTIVITY);
		}

	}
}
