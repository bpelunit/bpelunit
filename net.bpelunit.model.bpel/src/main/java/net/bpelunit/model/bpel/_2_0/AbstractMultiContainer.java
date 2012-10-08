package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IMultiContainer;

import org.apache.xmlbeans.XmlCursor;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

abstract class AbstractMultiContainer<T extends TActivity> extends AbstractActivity<T> implements IMultiContainer {

	private List<AbstractActivity<?>> wrappedActivities = new ArrayList<AbstractActivity<?>>();
	private List<Object> activities = new ArrayList<Object>();
	
	AbstractMultiContainer(T wrappedActivity, BpelFactory f) {
		super(wrappedActivity, f);
		
		this.activities = new ArrayList<Object>();
		XmlCursor cursor = wrappedActivity.newCursor();
		while(cursor.hasNextToken()) {
			cursor.toNextToken();
			if(cursor.getObject() instanceof TActivity) {
				activities.add((TActivity)cursor.getObject());
			}
		}
		
		for(Object child : activities) {
			wrappedActivities.add(getFactory().createActivity(child));
		}
	}
	
	public List<AbstractActivity<?>> getActivities() {
		return Collections.unmodifiableList(wrappedActivities);
	}
	

	public void addActivity(IActivity a) {
		AbstractActivity<?> activity = checkForCorrectModel(a);

		wrappedActivities.add(activity);
		activities.add(activity.getNativeActivity());
	}

	public void removeActivity(IActivity a) {
		AbstractActivity<?> activity = checkForCorrectModel(a);

		int i = activities.indexOf(activity.getNativeActivity());
		
		wrappedActivities.remove(i);
		activities.remove(i);
	}
	
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
