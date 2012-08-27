package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IActivity;
import net.bpelunit.bpel.ISingleContainer;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TActivity;

abstract class AbstractSingleContainer<T extends TActivity> extends
		AbstractActivity<T> implements ISingleContainer {

	// private TActivity activity;
	private IActivity mainActivity;

	AbstractSingleContainer(T newActivity, IActivity newMainActivity) {
		super(newActivity);
		// this.activity = newActivity;
		this.mainActivity = newMainActivity;
	}

	@Override
	public void setMainActivity(IActivity a) {
		this.mainActivity = a;
		this.setMainActivityBpel(((AbstractActivity<?>) a));
	}

	protected abstract void setMainActivityBpel(
			AbstractActivity<?> nativeActivity);

	@Override
	public IActivity getMainActivity() {
		return this.mainActivity;
	}

	@Override
	public boolean isBasicActivity() {
		return false;
	}
}
