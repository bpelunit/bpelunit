package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.ISingleContainer;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

abstract class AbstractSingleContainer<T extends TActivity> extends
		AbstractActivity<T> implements ISingleContainer {

	private AbstractActivity<?> mainActivity;

	AbstractSingleContainer(T newActivity, AbstractActivity<?> newMainActivity, BpelFactory f) {
		super(newActivity, f);
		this.mainActivity = newMainActivity;
	}

	public void setMainActivity(IActivity a) {
		if(a instanceof AbstractActivity<?>) {
			this.mainActivity = (AbstractActivity<?>)a;
			this.setMainActivityBpel(((AbstractActivity<?>) a));
		} else {
			throw new RuntimeException(a.getClass() + " is not compatible with this model");
		}
	}

	protected abstract void setMainActivityBpel(
			AbstractActivity<?> nativeActivity);

	public AbstractActivity<?> getMainActivity() {
		return this.mainActivity;
	}

	public boolean isBasicActivity() {
		return false;
	}
}
