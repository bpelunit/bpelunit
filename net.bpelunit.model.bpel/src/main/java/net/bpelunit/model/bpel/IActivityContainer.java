package net.bpelunit.model.bpel;

public interface IActivityContainer extends IVisitable {

	IScope wrapActivityInNewScope(IActivity childActivity);
	
	ISequence wrapActivityInNewSequence(IActivity childActivity);
}
