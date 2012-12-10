package net.bpelunit.framework.execution;

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

	/**
	 * In this phase the test suite and corresponding artifacts are loaded.
	 */
	void doLoad(IBPELUnitContext context);

	/**
	 * In this phase the processes and the deployment are changed for testing.
	 * This includes endpoint replacements and coverage instrumentation and
	 * similar activities.
	 */
	void doPrepareProcesses(IBPELUnitContext context);

	/**
	 * In this phase all mocks and other services are started that are offered
	 * by BPELUnit.
	 */
	void doStartMocks(IBPELUnitContext context);

	/**
	 * Deploy all necessary services and processes
	 * 
	 * @param context
	 */
	void doDeploy(IBPELUnitContext context);

	/**
	 * In this phase tests are run. This phase is usually only implemented in
	 * the runner.
	 */
	void doRunTests(IBPELUnitContext context);

	/**
	 * Mocks and other services offered by BPELUnit are stopped in this phase.
	 */
	void doStopMocks(IBPELUnitContext context);

	/**
	 * In this phase all reports and other test results are created.
	 */
	void doReport(IBPELUnitContext context);

}
