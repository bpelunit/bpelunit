package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IThrow;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TThrow;

public class Throw extends AbstractBasicActivity<TThrow> implements IThrow {

	public Throw(TThrow t, BpelFactory f) {
		super(t, f, IThrow.class);
	}
}
