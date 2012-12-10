package net.bpelunit.framework.execution;

import java.net.URL;
import java.util.List;

import org.mortbay.http.handler.AbstractHttpHandler;

import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.model.test.TestSuite;

public interface IBPELUnitContext {

	TestSuite getTestSuite();
	IDeployment getDeployment();
	
	/**
	 * TODO Make independent of Jetty
	 */
	URL addService(String name, AbstractHttpHandler handler);

	List<? extends ITestLifeCycleElement> getElementsInLoad();
	List<? extends ITestLifeCycleElement> getElementsInPrepareProcesses();
	List<? extends ITestLifeCycleElement> getElementsInStartMocks();
	List<? extends ITestLifeCycleElement> getElementsInDeploy();
	List<? extends ITestLifeCycleElement> getElementsInRunTests();
	List<? extends ITestLifeCycleElement> getElementsInStopMocks();
	List<? extends ITestLifeCycleElement> getElementsInReport();
	
}
