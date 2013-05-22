package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICatchAll;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivityContainer;

public class CatchAll extends AbstractSingleContainer<TActivityContainer> implements ICatchAll, IContainer {

	public CatchAll(TActivityContainer tActivityContainer, IContainer parent) {
		super(tActivityContainer, parent);
	}
	
	@Override
	public String toString() {
		return "CatchAll";
	}
}
