package net.bpelunit.bpel;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TActivity;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TProcess;

public class BpelUtil {

	/** 
	 * Returns the flow or sequence in this process.
	 * Syntactically other activities would be supported but will not 
	 * be returned.
	 */
	public static TActivity getChildActivity(TProcess process) {
		if (process.getSequence() != null) {
			return process.getSequence();
		} else if (process.getFlow() != null) {
			return process.getFlow();
		} else {
			return null;
		}
	}

	public static String getActivityType(Object o) {
		return o.getClass().getSimpleName().substring(1);
	} 
}
