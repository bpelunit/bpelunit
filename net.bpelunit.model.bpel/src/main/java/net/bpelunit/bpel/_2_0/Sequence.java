package net.bpelunit.bpel._2_0;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TSequence;

import net.bpelunit.bpel.IActivity;
import net.bpelunit.bpel.ISequence;
import net.bpelunit.bpel.IVisitor;

public class Sequence extends AbstractMultiContainer<TSequence> implements ISequence {

	public Sequence(TSequence wrappedSequence) {
		super(wrappedSequence, wrappedSequence.getActivity());
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(IActivity a : getActivities()) {
			a.visit(v);
		}
	}
}
