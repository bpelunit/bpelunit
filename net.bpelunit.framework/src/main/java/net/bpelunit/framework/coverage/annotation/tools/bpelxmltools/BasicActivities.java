package net.bpelunit.framework.coverage.annotation.tools.bpelxmltools;

import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_1_1;
import static net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.NAMESPACE_BPEL_2_0;

import java.util.Hashtable;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Die Klasse repräsentiert die Basisaktivitäten.
 * 
 * @author Alex Salnikow
 *
 */
public final class BasicActivities {

	private BasicActivities() {
	}
	
	public static final String INVOKE_ACTIVITY = "invoke";

	public static final String RECEIVE_ACTIVITY = "receive";

	public static final String REPLY_ACTIVITY = "reply";

	public static final String THROW_ACTIVITY = "throw";

	public static final String RETHROW_ACTIVITY = "rethrow";

	public static final String WAIT_ACTIVITY = "wait";

	public static final String ASSIGN_ACTIVITY = "assign";

	public static final String EMPTY_ACTIVITY = "empty";

	public static final String COMPENSATE_ACTIVITY = "compensate";

	public static final String COMPENSATESCOPE_ACTIVITY = "compensateScope";

	public static final String EXIT_ACTIVITY = "exit";

	public static final String TERMINATE_ACTIVITY = "terminate";

	public static final String VALIDATE_ACTIVITY = "validate";

	private static Hashtable<String, String> basisActivities;


	/**
	 * Überprüft, ob das Element eine BPEL-BasicActivität repräsentiert.
	 * 
	 * @param element
	 * @return ist BasicActivität
	 */
	public static boolean isBasisActivity(Element element) {
		return isBasisActivity(element.getName());
	}

	public static boolean isBasisActivity(String name) {
		return basisActivities.containsKey(name);
	}

	/**
	 * Legt die Aktivitäten fest, abhängig von der BPEL-Version (1.1 oder 2.0)
	 *
	 */
	public static void initialize() {
		Namespace bpelNamespace = BpelXMLTools.getProcessNamespace();
		if (bpelNamespace.equals(NAMESPACE_BPEL_2_0)) {
			basisActivities = new Hashtable<String, String>();
			basisActivities.put(INVOKE_ACTIVITY, INVOKE_ACTIVITY);
			basisActivities.put(EXIT_ACTIVITY, EXIT_ACTIVITY);
			basisActivities.put(RECEIVE_ACTIVITY, RECEIVE_ACTIVITY);
			basisActivities.put(REPLY_ACTIVITY, REPLY_ACTIVITY);
			basisActivities.put(THROW_ACTIVITY, THROW_ACTIVITY);
			basisActivities.put(RETHROW_ACTIVITY, RETHROW_ACTIVITY);
			basisActivities.put(WAIT_ACTIVITY, WAIT_ACTIVITY);
			basisActivities.put(ASSIGN_ACTIVITY, ASSIGN_ACTIVITY);
			basisActivities.put(EMPTY_ACTIVITY, EMPTY_ACTIVITY);
			basisActivities.put(COMPENSATE_ACTIVITY, COMPENSATE_ACTIVITY);
			basisActivities.put(VALIDATE_ACTIVITY, VALIDATE_ACTIVITY);
			basisActivities.put(COMPENSATESCOPE_ACTIVITY,
					COMPENSATESCOPE_ACTIVITY);
		} else if (bpelNamespace.equals(NAMESPACE_BPEL_1_1)) {
			basisActivities = new Hashtable<String, String>();
			basisActivities.put(INVOKE_ACTIVITY, INVOKE_ACTIVITY);
			basisActivities.put(RECEIVE_ACTIVITY, RECEIVE_ACTIVITY);
			basisActivities.put(REPLY_ACTIVITY, REPLY_ACTIVITY);
			basisActivities.put(THROW_ACTIVITY, THROW_ACTIVITY);
			basisActivities.put(WAIT_ACTIVITY, WAIT_ACTIVITY);
			basisActivities.put(ASSIGN_ACTIVITY, ASSIGN_ACTIVITY);
			basisActivities.put(EMPTY_ACTIVITY, EMPTY_ACTIVITY);
			basisActivities.put(COMPENSATE_ACTIVITY, COMPENSATE_ACTIVITY);
			basisActivities.put(TERMINATE_ACTIVITY, TERMINATE_ACTIVITY);
		}

	}

}
