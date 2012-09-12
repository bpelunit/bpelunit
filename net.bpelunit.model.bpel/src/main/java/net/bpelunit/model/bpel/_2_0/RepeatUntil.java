package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IRepeatUntil;
import net.bpelunit.model.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TRepeatUntil;

public class RepeatUntil extends AbstractSingleContainer<TRepeatUntil>
		implements IRepeatUntil {

	private TRepeatUntil repeatUntil;

	public RepeatUntil(TRepeatUntil wrappedRepeatUntil, BpelFactory f) {
		super(wrappedRepeatUntil, f.createActivity(
				TComplexContainerHelper.getChildActivity(wrappedRepeatUntil)), f);
		this.repeatUntil = wrappedRepeatUntil;
	}

	@Override
	protected void setMainActivityBpel(AbstractActivity<?> nativeActivity) {
		TComplexContainerHelper.removeMainActivity(repeatUntil);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		getMainActivity().visit(v);
	}
}
