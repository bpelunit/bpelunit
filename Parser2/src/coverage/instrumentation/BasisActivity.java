package coverage.instrumentation;

import java.util.Hashtable;

import org.jdom.Element;

public class BasisActivity {

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

	private static Hashtable<String, String> basis_activities;

	static {
		basis_activities = new Hashtable<String, String>();
		basis_activities.put(INVOKE_ACTIVITY, INVOKE_ACTIVITY);
		basis_activities.put(EXIT_ACTIVITY, EXIT_ACTIVITY);
		basis_activities.put(RECEIVE_ACTIVITY, RECEIVE_ACTIVITY);
		basis_activities.put(REPLY_ACTIVITY, REPLY_ACTIVITY);
		basis_activities.put(THROW_ACTIVITY, THROW_ACTIVITY);
		basis_activities.put(RETHROW_ACTIVITY, RETHROW_ACTIVITY);
		basis_activities.put(WAIT_ACTIVITY, WAIT_ACTIVITY);
		basis_activities.put(ASSIGN_ACTIVITY, ASSIGN_ACTIVITY);
		basis_activities.put(EMPTY_ACTIVITY, EMPTY_ACTIVITY);
		basis_activities.put(COMPENSATE_ACTIVITY, COMPENSATE_ACTIVITY);
		basis_activities
				.put(COMPENSATESCOPE_ACTIVITY, COMPENSATESCOPE_ACTIVITY);
	}

	public static boolean isBasisActivity(Element element) {
		boolean isBasis = false;
		if (basis_activities.containsKey(element.getName()))
			isBasis = true;
		return isBasis;
	}
	


}
