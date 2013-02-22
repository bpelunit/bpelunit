package net.bpelunit.framework.execution;

import net.bpelunit.framework.exception.DeploymentException;

/**
 * Interface for plug-ins that are executed during the test cycle, e.g.
 * deployers, runners, instrumenters, ...
 * 
 * When adding/removing a new life-cycle phase, also change getters in
 * IBPELUnitContext
 * 
 * @author dluebke
 */
public interface ITestLifeCycleElement {

	public @interface TestLifeCycleElementOption {
		String description() default "";
	}

	/**
	 * In this phase the test suite and corresponding artifacts are loaded.
	 */
	void doLoad(IBPELUnitContext context);

	/**
	 * In this phase the processes and the deployment are changed for testing.
	 * This includes endpoint replacements and coverage instrumentation and
	 * similar activities.
	 * @throws DeploymentException 
	 */
	void doPrepareProcesses(IBPELUnitContext context) throws DeploymentException;

	/**
	 * In this phase all mocks and other services are registered that will be offered
	 * by BPELUnit.
	 */
	void doRegisterMocks(IBPELUnitContext context);
	
	/**
	 * In this phase all reports and other test results are created.
	 */
	void doReport(IBPELUnitContext context);

	/**
	 * In this phase the process can be read and to a certain extent changed.
	 * However, structural modifications to a process are not allowed in this
	 * phase but should be done in the doPrepareProcesses phase.
	 * @throws DeploymentException 
	 */
	void doMarkProcesses(IBPELUnitContext context) throws DeploymentException;

}
