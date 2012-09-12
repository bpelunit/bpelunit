package net.bpelunit.framework.control.lifecycle;

public interface LifeCycleContributor {

	// TODO Add parameters
	// TODO Add exceptions
	void duringInitialization();
	void beforeDeploy();
	void beforeTest();
	void afterTest();
	
}
