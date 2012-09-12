package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICompensate;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TCompensate;

class Compensate extends AbstractBasicActivity<TCompensate> implements ICompensate {

	Compensate(TCompensate wrappedCompensate, BpelFactory f) {
		super(wrappedCompensate, f, ICompensate.class);
	}
}
