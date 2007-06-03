package org.bpelunit.framework.coverage.annotation.tools.bpelxmltools;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;

import java.util.Hashtable;

import org.jdom.Element;
import org.jdom.Namespace;

public class StructuredActivity {

	public static final String SEQUENCE_ACTIVITY = "sequence";

	public static final String IF_ACTIVITY = "if";

	public static final String WHILE_ACTIVITY = "while";

	public static final String REPEATUNTIL_ACTIVITY = "repeatUntil";

	public static final String FOREACH_ACTIVITY = "forEach";

	public static final String PICK_ACTIVITY = "pick";

	public static final String FLOW_ACTIVITY = "flow";

	public static final String SCOPE_ACTIVITY = "scope";

	public static final String SWITCH_ACTIVITY = "switch";

	private static Hashtable<String, String> structured_activities;

	/**
	 * Überprüft, ob das Element eine BPEL-StructuredActivität repräsentiert.
	 * 
	 * @param element
	 * @return
	 */
	static boolean isStructuredActivity(Element activity) {
		return structured_activities.containsKey(activity.getName()) ? true
				: false;
	}

	public static void initialize() {
		Namespace bpelNamespace = BpelXMLTools.getProcessNamespace();
		if (bpelNamespace.equals(NAMESPACE_BPEL_2_0)) {
			structured_activities = new Hashtable<String, String>();
			structured_activities.put(SEQUENCE_ACTIVITY, SEQUENCE_ACTIVITY);
			structured_activities.put(IF_ACTIVITY, IF_ACTIVITY);
			structured_activities.put(WHILE_ACTIVITY, WHILE_ACTIVITY);
			structured_activities.put(REPEATUNTIL_ACTIVITY,
					REPEATUNTIL_ACTIVITY);
			structured_activities.put(FOREACH_ACTIVITY, FOREACH_ACTIVITY);
			structured_activities.put(PICK_ACTIVITY, PICK_ACTIVITY);
			structured_activities.put(FLOW_ACTIVITY, FLOW_ACTIVITY);
			structured_activities.put(SCOPE_ACTIVITY,SCOPE_ACTIVITY);
		} else if (bpelNamespace.equals(NAMESPACE_BPEL_1_1)) {
			structured_activities = new Hashtable<String, String>();
			structured_activities.put(SEQUENCE_ACTIVITY, SEQUENCE_ACTIVITY);
			structured_activities.put(WHILE_ACTIVITY, WHILE_ACTIVITY);
			structured_activities.put(PICK_ACTIVITY, PICK_ACTIVITY);
			structured_activities.put(FLOW_ACTIVITY, FLOW_ACTIVITY);
			structured_activities.put(SWITCH_ACTIVITY, SWITCH_ACTIVITY);
			structured_activities.put(SCOPE_ACTIVITY,SCOPE_ACTIVITY);
		}

	}
}
