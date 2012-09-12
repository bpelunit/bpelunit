package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IEmpty;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TEmpty;

class Empty extends AbstractBasicActivity<TEmpty> implements IEmpty {

	Empty(TEmpty newEmpty, BpelFactory f) {
		super(newEmpty, f, IEmpty.class);
	}

}
