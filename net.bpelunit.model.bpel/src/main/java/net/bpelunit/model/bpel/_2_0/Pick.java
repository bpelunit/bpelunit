package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IOnAlarm;
import net.bpelunit.model.bpel.IOnMessage;
import net.bpelunit.model.bpel.IPick;
import net.bpelunit.model.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TBoolean;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TOnAlarmPick;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TOnMessage;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TPick;

public class Pick extends AbstractActivity<TPick> implements IPick {

	private List<OnMessage> onMessages = new ArrayList<OnMessage>();
	private List<OnAlarm> onAlarms = new ArrayList<OnAlarm>();
	private BpelFactory factory;

	public Pick(TPick wrappedPick, BpelFactory f) {
		super(wrappedPick, f);

		this.factory = f;
		
		for(TOnMessage m : wrappedPick.getOnMessage()) {
			onMessages.add(new OnMessage(m, f));
		}
		
		for(TOnAlarmPick a : wrappedPick.getOnAlarm()) {
			onAlarms.add(new OnAlarm(a, f));
		}
	}

	@Override
	public boolean isBasicActivity() {
		return false;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(IOnMessage m : onMessages ) {
			v.visit(m);
		}

		for(IOnAlarm a : onAlarms ) {
			v.visit(a);
		}
	}

	@Override
	public void setCreateInstance(boolean b) {
		if(b) {
			getNativeActivity().setCreateInstance(TBoolean.YES);
		} else {
			getNativeActivity().setCreateInstance(TBoolean.NO);
		}
	}

	@Override
	public boolean isCreateInstance() {
		return TBoolean.YES.equals(getNativeActivity().getCreateInstance());
	}

	@Override
	public List<? extends IOnMessage> getOnMessages() {
		return Collections.unmodifiableList(onMessages);
	}

	@Override
	public List<? extends IOnAlarm> getOnAlarms() {
		return Collections.unmodifiableList(onAlarms);
	}

	@Override
	public OnMessage addOnMessage() {
		TOnMessage nativeOnMessage = new TOnMessage();
		OnMessage onMessage = new OnMessage(nativeOnMessage, factory);
		onMessages.add(onMessage);
		getNativeActivity().getOnMessage().add(nativeOnMessage);
		
		return onMessage;
	}

	@Override
	public OnAlarm addOnAlarm() {
		TOnAlarmPick nativeOnAlarm = new TOnAlarmPick();
		OnAlarm onAlarm = new OnAlarm(nativeOnAlarm, factory);
		onAlarms.add(onAlarm);
		getNativeActivity().getOnAlarm().add(nativeOnAlarm);
		
		return onAlarm;
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == getNativeActivity()) {
			return this;
		}
		
		for(OnMessage m : onMessages) {
			IBpelObject o = m.getObjectForNativeObject(nativeObject);
			if(o != null) {
				return o; 
			}
		}
		
		for(OnAlarm a : onAlarms) {
			IBpelObject o = a.getObjectForNativeObject(nativeObject);
			if(o != null) {
				return o; 
			}
		}
		
		return null;
	}
}
