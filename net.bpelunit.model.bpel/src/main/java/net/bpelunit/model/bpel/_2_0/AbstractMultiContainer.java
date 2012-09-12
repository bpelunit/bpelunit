package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IMultiContainer;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TActivity;

abstract class AbstractMultiContainer<T extends TActivity> extends AbstractActivity<T> implements IMultiContainer {

	private List<AbstractActivity<?>> wrappedActivities = new ArrayList<AbstractActivity<?>>();
	private List<Object> activities = new ArrayList<Object>();
	
	AbstractMultiContainer(T wrappedActivity, List<Object> children, BpelFactory f) {
		super(wrappedActivity, f);
		this.activities = children;
		
		for(Object child : children) {
			wrappedActivities.add(getFactory().createActivity(child));
		}
	}
	
	@Override
	public List<AbstractActivity<?>> getActivities() {
		return Collections.unmodifiableList(wrappedActivities);
	}
	

	@Override
	public void addActivity(IActivity a) {
		AbstractActivity<?> activity = checkForCorrectModel(a);

		wrappedActivities.add(activity);
		activities.add(activity.getNativeActivity());
	}

	@Override
	public void removeActivity(IActivity a) {
		AbstractActivity<?> activity = checkForCorrectModel(a);

		int i = activities.indexOf(activity.getNativeActivity());
		
		wrappedActivities.remove(i);
		activities.remove(i);
	}
	
	@Override
	public boolean isBasicActivity() {
		return false;
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		IBpelObject o = super.getObjectForNativeObject(nativeObject);
		if(o != null) {
			return o;
		} else {
			for(AbstractActivity<?> child : wrappedActivities) {
				o = child.getObjectForNativeObject(nativeObject);
				if(o != null) {
					return o;
				}
			}
		}
		
		return null;
	}
}
