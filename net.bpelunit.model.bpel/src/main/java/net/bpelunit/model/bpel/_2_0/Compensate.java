package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICompensate;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensate;

class Compensate extends AbstractBasicActivity<TCompensate> implements ICompensate {

	public Compensate(TCompensate wrappedCompensate, IContainer parent) {
		super(wrappedCompensate, parent);
	}
}
