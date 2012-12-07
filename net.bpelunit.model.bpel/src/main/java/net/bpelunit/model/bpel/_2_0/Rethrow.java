package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IRethrow;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TRethrow;

public class Rethrow extends AbstractBasicActivity<TRethrow> implements IRethrow {

	public Rethrow(TRethrow wrappedRethrow, IContainer parent) {
		super(wrappedRethrow, parent);
	}
}
