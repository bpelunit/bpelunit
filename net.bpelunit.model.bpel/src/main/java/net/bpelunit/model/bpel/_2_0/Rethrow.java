package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IRethrow;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TRethrow;

public class Rethrow extends AbstractBasicActivity<TRethrow> implements IRethrow {

	public Rethrow(TRethrow wrappedRethrow, BpelFactory f) {
		super(wrappedRethrow, f, IRethrow.class);
	}
}
