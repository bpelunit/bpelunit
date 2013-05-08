package net.bpelunit.model.bpel;

public interface IActivityContainer extends IVisitable, IBpelObject {

	IScope wrapActivityInNewScope(IActivity childActivity);
	
	ISequence wrapActivityInNewSequence(IActivity childActivity);
}
