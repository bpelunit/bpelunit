package net.bpelunit.framework.execution;

import java.net.URL;
import java.util.List;

import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.model.test.TestSuite;

import org.eclipse.jetty.server.handler.AbstractHandler;

public interface IBPELUnitContext {

	TestSuite getTestSuite();
	IDeployment getDeployment();
	
	/**
	 * TODO Make independent of Jetty
	 */
	URL addService(String name, AbstractHandler handler);

	List<? extends ITestLifeCycleElement> getElementsInLoad();
	List<? extends ITestLifeCycleElement> getElementsInPrepareProcesses();
	List<? extends ITestLifeCycleElement> getElementsInStartMocks();
	List<? extends ITestLifeCycleElement> getElementsInDeploy();
	List<? extends ITestLifeCycleElement> getElementsInRunTests();
	List<? extends ITestLifeCycleElement> getElementsInStopMocks();
	List<? extends ITestLifeCycleElement> getElementsInReport();
	URL getURLForService(String serviceName);
	
}
