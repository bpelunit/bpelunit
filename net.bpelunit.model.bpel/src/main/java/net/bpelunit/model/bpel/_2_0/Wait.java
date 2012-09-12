package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IWait;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TDeadlineExpr;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TDurationExpr;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TWait;

public class Wait extends AbstractBasicActivity<TWait> implements IWait {

	public Wait(TWait wrappedWait, BpelFactory f) {
		super(wrappedWait, f, IWait.class);
	}

	@Override
	public void setDuration(String durationExpression) {
		this.getNativeActivity().setUntil(null);

		TDurationExpr newFor = new TDurationExpr();
		newFor.getContent().clear();
		newFor.getContent().add(durationExpression);
		this.getNativeActivity().setFor(newFor);
	}

	@Override
	public void setDeadline(String deadlineExpression) {
		this.getNativeActivity().setFor(null);

		TDeadlineExpr newUntil = new TDeadlineExpr();
		newUntil.getContent().clear();
		newUntil.getContent().add(deadlineExpression);
		this.getNativeActivity().setUntil(newUntil);
	}

	@Override
	public String getDeadline() {
		try {
			return getNativeActivity().getUntil().getContent().get(0)
					.toString();
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public String getDuration() {
		try {
			return getNativeActivity().getFor().getContent().get(0).toString();
		} catch (NullPointerException e) {
			return null;
		}
	}
}
