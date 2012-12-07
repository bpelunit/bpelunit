package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.util.StringUtil;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

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
				// ignore
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

	static void removeMainActivity(Object o) {
		for (String activityName : activityNames) {
			String methodName = "unset" + activityName;
			Method m;
			try {
				m = o.getClass().getMethod(methodName);
			} catch (Exception e) {
				throw new RuntimeException(
						"Passed invalid object that does not support "
								+ methodName, e);
			}
			try {
				m.invoke(o);
			} catch (Exception e) {
				// ignore
			}
		}
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

	static AbstractActivity<?> setNewActivityOfType(Object container,
			String activityType, IActivityContainer parent) {
		String methodName = "addNew" + StringUtil.toFirstUpper(activityType);
		TActivity sequence = null;
		try {
//			removeMainActivity(container);

			Method m = container.getClass().getMethod(methodName);
			sequence = (TActivity) m.invoke(container);
		} catch (Exception e) {
			throw new RuntimeException(
					"Invalid configuration. Cannot find method name "
							+ container.getClass().getName() + "." + methodName,
					e);
		}
		AbstractActivity<?> wrapper = BpelFactory.INSTANCE.createWrapper(
				sequence, parent);
		return wrapper;
	}
	
}
