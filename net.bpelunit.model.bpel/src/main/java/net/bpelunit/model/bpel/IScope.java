package net.bpelunit.model.bpel;

public interface IScope extends IActivity, IVariableContainer {

	void setMainActivity(IActivity sequence);
	IActivity getMainActivity();

}
