package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IWait;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TDeadlineExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TDurationExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;

public class Wait extends AbstractBasicActivity<TWait> implements IWait {

	public Wait(TWait wrappedWait) {
		super(wrappedWait);
	}

	public void setDuration(String durationExpression) {
		this.getNativeActivity().unsetUntil();
		this.getNativeActivity().unsetFor();

		TDurationExpr newFor = getNativeActivity().addNewFor();
		this.getNativeActivity().setFor(newFor);
		newFor.getDomNode().setTextContent(durationExpression);
	}

	public void setDeadline(String deadlineExpression) {
		this.getNativeActivity().unsetFor();
		this.getNativeActivity().unsetUntil();

		TDeadlineExpr newUntil = getNativeActivity().addNewUntil();
		newUntil.getDomNode().setTextContent(deadlineExpression);
	}

	public String getDeadline() {
		try {
			return getNativeActivity().getUntil().getDomNode().getTextContent();
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getDuration() {
		try {
			return getNativeActivity().getFor().getDomNode().getTextContent();
		} catch (NullPointerException e) {
			return null;
		}
	}
}
