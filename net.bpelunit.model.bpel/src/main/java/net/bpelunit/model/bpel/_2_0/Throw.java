package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IThrow;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TThrow;

public class Throw extends AbstractBasicActivity<TThrow> implements IThrow {

	public Throw(TThrow t, IContainer parent) {
		super(t, parent);
	}
}
