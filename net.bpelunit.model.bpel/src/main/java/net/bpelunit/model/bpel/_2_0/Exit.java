package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IExit;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TExit;

public class Exit extends AbstractBasicActivity<TExit> implements IExit {

	public Exit(TExit wrappedExit, BpelFactory f) {
		super(wrappedExit, f, IExit.class);
	}
}
