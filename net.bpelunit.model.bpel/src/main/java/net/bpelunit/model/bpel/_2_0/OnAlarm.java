package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IOnAlarm;
import net.bpelunit.model.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TActivity;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TOnAlarmPick;

public class OnAlarm extends AbstractBpelObject implements IOnAlarm {

	private TOnAlarmPick onAlarm;
	private AbstractActivity<?> mainActivity;
	
	OnAlarm(TOnAlarmPick nativeOnAlarm, BpelFactory f) {
		super(nativeOnAlarm, f);
		this.onAlarm = nativeOnAlarm;
		
		TActivity childActivity = TComplexContainerHelper.getChildActivity(nativeOnAlarm);
		this.mainActivity = getFactory().createActivity(childActivity);
	}
	
	@Override
	public void setMainActivity(IActivity a) {
		AbstractActivity<?> abstractActivity = (AbstractActivity<?>)a;
		this.mainActivity = abstractActivity;
		TComplexContainerHelper.setActivity(onAlarm, abstractActivity.getNativeActivity());
	}

	@Override
	public AbstractActivity<?> getMainActivity() {
		return this.mainActivity;
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == onAlarm) {
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

	public TOnAlarmPick getNativeActivity() {
		return onAlarm;
	}
}
