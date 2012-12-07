package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivityContainer;

public abstract interface IContainer extends IActivityContainer {

	void unregister(AbstractActivity<?> a);
	
}
