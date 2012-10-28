package net.bpelunit.model.bpel;

public interface IActivityContainer extends IVisitable {

	IScope encapsulateInNewScope(IActivity childActivity);
	
}
