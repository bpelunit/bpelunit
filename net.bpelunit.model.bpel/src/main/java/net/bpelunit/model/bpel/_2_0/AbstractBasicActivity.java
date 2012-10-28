package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivityContainer;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public abstract class AbstractBasicActivity<T extends TActivity> extends AbstractActivity<T> {

	AbstractBasicActivity(T a, IActivityContainer parent) {
		super(a, parent);
	}

	public final boolean isBasicActivity() {
		return true;
	}
}
