package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.IRepeatUntil;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TRepeatUntil;

public class RepeatUntil extends AbstractSingleContainer<TRepeatUntil>
		implements IRepeatUntil {

	private TRepeatUntil repeatUntil;

	public RepeatUntil(TRepeatUntil wrappedRepeatUntil) {
		super(wrappedRepeatUntil, BpelFactory.getInstance().createActivity(
				TComplexContainerHelper.getChildActivity(wrappedRepeatUntil)));
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
