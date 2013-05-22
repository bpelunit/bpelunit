package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IElse;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivityContainer;

public class Else extends AbstractSingleContainer<TActivityContainer> implements IElse {

	public Else(TActivityContainer else1, IContainer parent) {
		super(else1, parent);
	}
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		if(this.getMainActivity() != null) {
			this.getMainActivity().visit(v);
		}
	}
	
	@Override
	public String toString() {
		return "Else";
	}
}
