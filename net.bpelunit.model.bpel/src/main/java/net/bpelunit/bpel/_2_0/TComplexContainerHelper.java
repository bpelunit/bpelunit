package net.bpelunit.bpel._2_0;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TActivity;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TProcess;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TRepeatUntil;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TScope;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TWhile;

final class TComplexContainerHelper {

	private static final String[] activityNames = { "Assign", "Compensate",
			"CompensateScope", "Empty", "Exit", "Flow", "ForEach", "If",
			"Invoke", "Pick", "Receive", "RepeatUntil", "Reply", "Rethrow",
			"Scope", "Sequence", "Throw", "Validate", "Wait", "While" };

	private TComplexContainerHelper() {
	}

	static TActivity getChildActivity(Object container) {
		List<TActivity> children = new ArrayList<TActivity>();

		for (String activity : activityNames) {
			Method m;
			try {
				m = container.getClass().getMethod("get" + activity);
				children.add((TActivity) m.invoke(container));
			} catch (Exception e) {
				throw new IllegalArgumentException(activity
						+ " is not supported by "
						+ container.getClass().getSimpleName());
			}
		}

		while (children.remove(null))
			;

		if (children.size() > 0) {
			return children.get(0);
		} else {
			return null;
		}
	}

	static void removeMainActivity(TProcess process) {
		process.setAssign(null);
		process.setCompensate(null);
		process.setCompensateScope(null);
		process.setEmpty(null);
		process.setExit(null);
		process.setExtensionActivity(null);
		process.setFlow(null);
		process.setForEach(null);
		process.setIf(null);
		process.setInvoke(null);
		process.setPick(null);
		process.setReceive(null);
		process.setRepeatUntil(null);
		process.setReply(null);
		process.setRethrow(null);
		process.setScope(null);
		process.setSequence(null);
		process.setThrow(null);
		process.setValidate(null);
		process.setWait(null);
		process.setWhile(null);
	}
	static void removeMainActivity(TWhile w) {
		w.setAssign(null);
		w.setCompensate(null);
		w.setCompensateScope(null);
		w.setEmpty(null);
		w.setExit(null);
		w.setExtensionActivity(null);
		w.setFlow(null);
		w.setForEach(null);
		w.setIf(null);
		w.setInvoke(null);
		w.setPick(null);
		w.setReceive(null);
		w.setRepeatUntil(null);
		w.setReply(null);
		w.setRethrow(null);
		w.setScope(null);
		w.setSequence(null);
		w.setThrow(null);
		w.setValidate(null);
		w.setWait(null);
		w.setWhile(null);
	}
	
	static void removeMainActivity(TRepeatUntil repeatUntil) {
		repeatUntil.setAssign(null);
		repeatUntil.setCompensate(null);
		repeatUntil.setCompensateScope(null);
		repeatUntil.setEmpty(null);
		repeatUntil.setExit(null);
		repeatUntil.setExtensionActivity(null);
		repeatUntil.setFlow(null);
		repeatUntil.setForEach(null);
		repeatUntil.setIf(null);
		repeatUntil.setInvoke(null);
		repeatUntil.setPick(null);
		repeatUntil.setReceive(null);
		repeatUntil.setRepeatUntil(null);
		repeatUntil.setReply(null);
		repeatUntil.setRethrow(null);
		repeatUntil.setScope(null);
		repeatUntil.setSequence(null);
		repeatUntil.setThrow(null);
		repeatUntil.setValidate(null);
		repeatUntil.setWait(null);
		repeatUntil.setWhile(null);
	}

	static void removeMainActivity(TScope scope) {
		scope.setAssign(null);
		scope.setCompensate(null);
		scope.setCompensateScope(null);
		scope.setEmpty(null);
		scope.setExit(null);
		scope.setExtensionActivity(null);
		scope.setFlow(null);
		scope.setForEach(null);
		scope.setIf(null);
		scope.setInvoke(null);
		scope.setPick(null);
		scope.setReceive(null);
		scope.setRepeatUntil(null);
		scope.setReply(null);
		scope.setRethrow(null);
		scope.setScope(null);
		scope.setSequence(null);
		scope.setThrow(null);
		scope.setValidate(null);
		scope.setWait(null);
		scope.setWhile(null);
	}

	static void setActivity(Object container, TActivity bpelActivity) {
		if (bpelActivity != null) {
			String setterName = "set"
					+ bpelActivity.getClass().getSimpleName().substring(1);

			Method method;
			try {
				method = container.getClass().getMethod(setterName,
						bpelActivity.getClass());
				method.invoke(container, bpelActivity);
			} catch (Exception e) {
				throw new IllegalArgumentException("Unsupported class "
						+ container.getClass(), e);
			}
		}
	}

}
