package net.bpelunit.bpel;

import java.util.List;

public interface IMultiContainer {

	List<? extends IActivity> getActivities();
	void addActivity(IActivity a);
	void removeActivity(IActivity a);

}
