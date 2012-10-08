package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IOnMessage;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;

public class OnMessage extends AbstractBpelObject implements IOnMessage {

	private TOnMessage onMessage;
	private AbstractActivity<?> mainActivity;
	
	OnMessage(TOnMessage nativeOnMessage, BpelFactory f) {
		super(nativeOnMessage, f);
		this.onMessage = nativeOnMessage;
		
		TActivity childActivity = TComplexContainerHelper.getChildActivity(nativeOnMessage);
		this.mainActivity = getFactory().createActivity(childActivity);
	}
	
	public void setMainActivity(IActivity a) {
		AbstractActivity<?> abstractActivity = (AbstractActivity<?>)a;
		this.mainActivity = abstractActivity;
		TComplexContainerHelper.setActivity(onMessage, abstractActivity.getNativeActivity());
	}

	public AbstractActivity<?> getMainActivity() {
		return this.mainActivity;
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == onMessage) {
			return this;
		}
		
		if(getMainActivity() != null) {
			return getMainActivity().getObjectForNativeObject(nativeObject);
		}
		
		return null;
	}
	
	@Override
	void visit(IVisitor v) {
		v.visit(this);
		if(mainActivity != null) {
			mainActivity.visit(v);
		}
	}

	public TOnMessage getNativeActivity() {
		return onMessage;
	}
}
