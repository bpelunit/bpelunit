package net.bpelunit.model.bpel._2_0;

import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ISource;
import net.bpelunit.model.bpel.ITarget;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;

abstract class AbstractActivity<T extends TExtensibleElements> extends
		AbstractBpelObject implements IActivity {

	private static final String IMPL_POSTFIX = "Impl";
	
	T activity;

	AbstractActivity(T a) {
		super(a);

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
		if (activity instanceof TActivity) {
			return ((TActivity) activity).getName();
		} else {
			return null;
		}
	}

	public void setName(String value) {
		if (activity instanceof TActivity) {
			((TActivity) activity).setName(value);
		} else {
			throw new UnsupportedOperationException("Cannot set name for " + activity.getClass().getSimpleName());
		}
	}

	public void setSuppressJoinFailure(boolean value) {
		if (activity instanceof TActivity) {
			((TActivity) activity).setSuppressJoinFailure(TBooleanHelper.convert(value));
		} else {
			throw new UnsupportedOperationException("Cannot set suppressJoinFailure for " + activity.getClass().getSimpleName());
		}
	}
	
	public boolean getSuppressJoinFailure() {
		if (activity instanceof TActivity) {
			return ((TActivity) activity).getSuppressJoinFailure().equals(TBoolean.YES);
		} else {
			throw new UnsupportedOperationException("Cannot get suppressJoinFailure for " + activity.getClass().getSimpleName());
		}
	}

	T getNativeActivity() {
		return activity;
	}

	public String getActivityName() {
		String name = activity.getClass().getSimpleName().substring(1);
		if(name.endsWith(IMPL_POSTFIX)) {
			name = name.substring(0, name.length() - IMPL_POSTFIX.length());
		}
		
		return name;
	}

	@Override
	public String getXPathInDocument() {
		return "//" + getActivityName() + "['" + getName() + "']";
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if (nativeObject == activity) {
			return this;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected void setNativeActivity(XmlObject newNativeActivity) {
		this.activity = (T) newNativeActivity;
	}
}