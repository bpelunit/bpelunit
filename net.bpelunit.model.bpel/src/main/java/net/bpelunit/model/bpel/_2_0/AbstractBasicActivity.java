package net.bpelunit.model.bpel._2_0;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public abstract class AbstractBasicActivity<T extends TActivity> extends AbstractActivity<T> {

	AbstractBasicActivity(T a) {
		super(a);
	}

	public final boolean isBasicActivity() {
		return true;
	}
}
