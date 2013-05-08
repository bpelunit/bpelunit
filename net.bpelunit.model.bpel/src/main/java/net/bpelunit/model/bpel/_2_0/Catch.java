package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICatch;
import net.bpelunit.model.bpel.ISingleContainer;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TCatch;

public class Catch extends AbstractSingleContainer<TCatch> implements ICatch, ISingleContainer, IContainer {

	public Catch(TCatch tActivityContainer, IContainer parent) {
		super(tActivityContainer, parent);
	}
	
	@Override
	public String toString() {
		return "Catch";
	}
}
