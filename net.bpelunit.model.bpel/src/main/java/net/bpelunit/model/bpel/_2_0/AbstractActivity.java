package net.bpelunit.model.bpel._2_0;

import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ISource;
import net.bpelunit.model.bpel.ITarget;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

abstract class AbstractActivity<T extends TActivity> extends AbstractBpelObject implements IActivity {

	T activity;
	AbstractActivity(T a, BpelFactory factory) {
		super(a, factory);
		
		this.activity = a;
	}

	public List<ITarget> getTargets() {
		// TODO
		return null;
	}

	public List<ISource> getSources() {
		return null; // TODO
	}

	public String getName() {
		return activity.getName();
	}

	public void setName(String value) {
		activity.setName(value);
	}

	public void setSuppressJoinFailure(boolean value) {
		activity.setSuppressJoinFailure(TBooleanHelper.convert(value));
	}

	T getNativeActivity() {
		return activity;
	}
	
	public String getActivityName() {
		return activity.getClass().getSimpleName().substring(1);
	}
	
	@Override
	public String getXPathInDocument() {
		return "//" + getActivityName() + "['" + getName() + "']";
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == activity) {
			return this;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected void setNativeActivity(XmlObject newNativeActivity) {
		this.activity = (T)newNativeActivity;
	}
}