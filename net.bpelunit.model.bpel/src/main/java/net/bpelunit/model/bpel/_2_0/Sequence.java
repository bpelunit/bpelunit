package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;

public class Sequence extends AbstractMultiContainer<TSequence> implements ISequence {

	public Sequence(TSequence wrappedSequence, IContainer parent) {
		super(wrappedSequence, parent);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(IActivity a : getActivities()) {
			a.visit(v);
		}
	}
}
