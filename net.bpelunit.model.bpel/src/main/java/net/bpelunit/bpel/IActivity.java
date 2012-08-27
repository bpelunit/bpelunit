package net.bpelunit.bpel;



public interface IActivity extends IBpelObject, IVisitable {

	boolean isBasicActivity();
	String getActivityName();
	String getName();
	void setName(String newName);
}
