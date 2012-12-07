package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IElse;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivityContainer;

public class Else extends AbstractSingleContainer<TActivityContainer> implements IElse {

	public Else(TActivityContainer else1, IContainer parent) {
		super(else1, parent);
	}
}
