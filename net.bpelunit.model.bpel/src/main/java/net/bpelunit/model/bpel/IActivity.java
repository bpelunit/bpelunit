package net.bpelunit.model.bpel;



public interface IActivity extends IBpelObject, IVisitable {

	boolean isBasicActivity();
	ActivityType getActivityType();
	String getName();
	void setName(String newName);
	boolean getSuppressJoinFailure();
	void setSuppressJoinFailure(boolean value);
	IActivityContainer getParent();
}
