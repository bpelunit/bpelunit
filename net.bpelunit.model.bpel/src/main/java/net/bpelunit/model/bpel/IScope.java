package net.bpelunit.model.bpel;

public interface IScope extends IActivity, IVariableContainer, IEventHandlerHolder, ICompensationHandlerContainer, IActivityContainer {

	IActivity getMainActivity();
}
