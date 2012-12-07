package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IExit;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TExit;

public class Exit extends AbstractBasicActivity<TExit> implements IExit {

	public Exit(TExit wrappedExit, IContainer parent) {
		super(wrappedExit, parent);
	}
}
