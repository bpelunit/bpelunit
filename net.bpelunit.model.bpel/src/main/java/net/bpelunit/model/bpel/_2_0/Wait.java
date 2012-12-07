package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IWait;
import net.bpelunit.util.XMLUtil;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TDeadlineExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TDurationExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class Wait extends AbstractBasicActivity<TWait> implements IWait {

	public Wait(TWait wrappedWait, IContainer parent) {
		super(wrappedWait, parent);
	}

	public void setDuration(String durationExpression) {
		clear();

		TDurationExpr newFor = getNativeActivity().addNewFor();
		Node domNode = newFor.getDomNode();
		Text textNode = domNode.getOwnerDocument().createTextNode(
				durationExpression);

		domNode.appendChild(textNode);
	}

	public void setDeadline(String deadlineExpression) {
		clear();

		TDeadlineExpr newUntil = getNativeActivity().addNewUntil();
		Node domNode = newUntil.getDomNode();
		Text textNode = domNode.getOwnerDocument().createTextNode(
				deadlineExpression);

		domNode.appendChild(textNode);
	}

	public String getDeadline() {
		try {
			return XMLUtil.getContentsOfTextOnlyNode(getNativeActivity()
					.getUntil().getDomNode());
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getDuration() {
		try {
			return XMLUtil.getContentsOfTextOnlyNode(getNativeActivity()
					.getFor().getDomNode());
		} catch (NullPointerException e) {
			return null;
		}
	}

	private void clear() {
		if (getNativeActivity().getUntil() != null) {
			this.getNativeActivity().unsetUntil();
		}
		if (getNativeActivity().getFor() != null) {
			this.getNativeActivity().unsetFor();
		}
	}
}
