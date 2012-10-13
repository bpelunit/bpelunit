package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IIf;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;

public class If extends AbstractActivity<TIf> implements IIf {

	public If(TIf i) {
		super(i);
	}

	public boolean isBasicActivity() {
		return false;
	}
}
