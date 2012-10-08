package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TRepeatUntil;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWhile;

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
		if(process.getAssign() != null) {
			process.unsetAssign();
		}
		if(process.getCompensate() != null) {
			process.unsetCompensate();
		}
		if(process.getCompensateScope() != null) {
			process.unsetCompensateScope();
		}
		if(process.getEmpty() != null) {
			process.unsetEmpty();
		}
		if(process.getExit() != null) {
			process.unsetExit();
		}
		if(process.getExtensionActivity() != null) {
			process.unsetExtensionActivity();
		}
		if(process.getFlow() != null) {
			process.unsetFlow();
		}
		if(process.getForEach() != null) {
			process.unsetForEach();
		}
		if(process.getIf() != null) {
			process.unsetIf();
		}
		if(process.getInvoke() != null) {
			process.unsetInvoke();
		}
		if(process.getPick() != null) {
			process.unsetPick();
		}
		if(process.getReceive() != null) {
			process.unsetReceive();
		}
		if(process.getRepeatUntil() != null) {
			process.unsetRepeatUntil();
		}
		if(process.getReply() != null) {
			process.unsetReply();
		}
		if(process.getRethrow() != null) {
			process.unsetRethrow();
		}
		if(process.getScope() != null ) {
			process.unsetScope();
		}
		if(process.getSequence() != null) {
			process.unsetSequence();
		}
		if(process.getThrow() != null) {
			process.unsetThrow();
		}
		if(process.getValidate() != null) {
			process.unsetValidate();
		}
		if(process.getWait() != null) {
			process.unsetWait();
		}
		if(process.getWhile() != null) {
			process.unsetWhile();
		}
	}
	static void removeMainActivity(TWhile w) {
		if(w.getAssign() != null) {
			w.unsetAssign();
		}
		if(w.getCompensate() != null) {
			w.unsetCompensate();
		}
		if(w.getCompensateScope() != null) {
			w.unsetCompensateScope();
		}
		if(w.getEmpty() != null) {
			w.unsetEmpty();
		}
		if(w.getExit() != null) {
			w.unsetExit();
		}
		if(w.getExtensionActivity() != null) {
			w.unsetExtensionActivity();
		}
		if(w.getFlow() != null) {
			w.unsetFlow();
		}
		if(w.getForEach() != null) {
			w.unsetForEach();
		}
		if(w.getIf() != null) {
			w.unsetIf();
		}
		if(w.getInvoke() != null) {
			w.unsetInvoke();
		}
		if(w.getPick() != null) {
			w.unsetPick();
		}
		if(w.getReceive() != null) {
			w.unsetReceive();
		}
		if(w.getRepeatUntil() != null) {
			w.unsetRepeatUntil();
		}
		if(w.getReply() != null) {
			w.unsetReply();
		}
		if(w.getRethrow() != null) {
			w.unsetRethrow();
		}
		if(w.getScope() != null ) {
			w.unsetScope();
		}
		if(w.getSequence() != null) {
			w.unsetSequence();
		}
		if(w.getThrow() != null) {
			w.unsetThrow();
		}
		if(w.getValidate() != null) {
			w.unsetValidate();
		}
		if(w.getWait() != null) {
			w.unsetWait();
		}
		if(w.getWhile() != null) {
			w.unsetWhile();
		}
	}
	
	static void removeMainActivity(TRepeatUntil repeatUntil) {
		if(repeatUntil.getAssign() != null) {
			repeatUntil.unsetAssign();
		}
		if(repeatUntil.getCompensate() != null) {
			repeatUntil.unsetCompensate();
		}
		if(repeatUntil.getCompensateScope() != null) {
			repeatUntil.unsetCompensateScope();
		}
		if(repeatUntil.getEmpty() != null) {
			repeatUntil.unsetEmpty();
		}
		if(repeatUntil.getExit() != null) {
			repeatUntil.unsetExit();
		}
		if(repeatUntil.getExtensionActivity() != null) {
			repeatUntil.unsetExtensionActivity();
		}
		if(repeatUntil.getFlow() != null) {
			repeatUntil.unsetFlow();
		}
		if(repeatUntil.getForEach() != null) {
			repeatUntil.unsetForEach();
		}
		if(repeatUntil.getIf() != null) {
			repeatUntil.unsetIf();
		}
		if(repeatUntil.getInvoke() != null) {
			repeatUntil.unsetInvoke();
		}
		if(repeatUntil.getPick() != null) {
			repeatUntil.unsetPick();
		}
		if(repeatUntil.getReceive() != null) {
			repeatUntil.unsetReceive();
		}
		if(repeatUntil.getRepeatUntil() != null) {
			repeatUntil.unsetRepeatUntil();
		}
		if(repeatUntil.getReply() != null) {
			repeatUntil.unsetReply();
		}
		if(repeatUntil.getRethrow() != null) {
			repeatUntil.unsetRethrow();
		}
		if(repeatUntil.getScope() != null ) {
			repeatUntil.unsetScope();
		}
		if(repeatUntil.getSequence() != null) {
			repeatUntil.unsetSequence();
		}
		if(repeatUntil.getThrow() != null) {
			repeatUntil.unsetThrow();
		}
		if(repeatUntil.getValidate() != null) {
			repeatUntil.unsetValidate();
		}
		if(repeatUntil.getWait() != null) {
			repeatUntil.unsetWait();
		}
		if(repeatUntil.getWhile() != null) {
			repeatUntil.unsetWhile();
		}
	}

	static void removeMainActivity(TScope scope) {
		scope.unsetAssign();
		scope.unsetCompensate();
		scope.unsetCompensateScope();
		scope.unsetEmpty();
		scope.unsetExit();
		scope.unsetExtensionActivity();
		scope.unsetFlow();
		scope.unsetForEach();
		scope.unsetIf();
		scope.unsetInvoke();
		scope.unsetPick();
		scope.unsetReceive();
		scope.unsetRepeatUntil();
		scope.unsetReply();
		scope.unsetRethrow();
		scope.unsetScope();
		scope.unsetSequence();
		scope.unsetThrow();
		scope.unsetValidate();
		scope.unsetWait();
		scope.unsetWhile();
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
