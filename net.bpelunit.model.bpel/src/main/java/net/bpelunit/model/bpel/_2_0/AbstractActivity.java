package net.bpelunit.model.bpel._2_0;

import java.util.List;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.ISource;
import net.bpelunit.model.bpel.ITarget;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;

abstract class AbstractActivity<T extends TExtensibleElements> extends
		AbstractBpelObject implements IActivity {

	private static final String IMPL_POSTFIX = "Impl";
	
	T activity;

	private IContainer parent;

	AbstractActivity(T a, IContainer parentContainer) {
		super(a);
		this.parent = parentContainer;
		this.activity = a;
	}

	@Override
	public IActivityContainer getParent() {
		return parent;
	}
	
	void reparent(IContainer newParent) {
		if(parent != null) {
			parent.unregister(this);
		}
		parent = newParent;
	}
	
	public List<ITarget> getTargets() {
		// TODO
		return null;
	}

	public List<ISource> getSources() {
		return null; // TODO
	}

	public String getName() {
			return ((TActivity) activity).getName();
	}

	public void setName(String value) {
			((TActivity) activity).setName(value);
	}

	public void setSuppressJoinFailure(boolean value) {
			((TActivity) activity).setSuppressJoinFailure(TBooleanHelper.convert(value));
	}
	
	public boolean getSuppressJoinFailure() {
			return ((TActivity) activity).getSuppressJoinFailure().equals(TBoolean.YES);
	}

	T getNativeActivity() {
		return activity;
	}

	public ActivityType getActivityType() {
		String name = activity.getClass().getSimpleName().substring(1);
		name = name.substring(0, name.length() - IMPL_POSTFIX.length());
		
		return ActivityType.valueOf(name);
	}

	@SuppressWarnings("unchecked")
	void setNativeObject(Object substitute) {
		super.setNativeObject(substitute);
		activity = (T)substitute;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getName();
	}
}