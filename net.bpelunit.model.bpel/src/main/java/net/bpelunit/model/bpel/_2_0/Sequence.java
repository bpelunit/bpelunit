package net.bpelunit.model.bpel._2_0;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TSequence;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVisitor;

public class Sequence extends AbstractMultiContainer<TSequence> implements ISequence {

	public Sequence(TSequence wrappedSequence, BpelFactory f) {
		super(wrappedSequence, wrappedSequence.getActivity(), f);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(IActivity a : getActivities()) {
			a.visit(v);
		}
	}
}
