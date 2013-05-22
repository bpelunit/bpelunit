package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICompensationHandler;
import net.bpelunit.model.bpel.ISingleContainer;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivityContainer;

public class CompensationHandler extends AbstractSingleContainer<TActivityContainer> implements ICompensationHandler, ISingleContainer, IContainer {

	public CompensationHandler(TActivityContainer tActivityContainer, IContainer parent) {
		super(tActivityContainer, parent);
	}
	
	@Override
	public String toString() {
		return "CompensationHandler";
	}
}
