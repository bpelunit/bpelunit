package net.bpelunit.framework.execution;

import net.bpelunit.framework.exception.DeploymentException;

/**
 * Utility class that contains empty implementations for all methods in the
 * ITestLifeCycleElement interface
 * 
 * @author dluebke
 */
public class AbstractTestLifeCycleElement implements ITestLifeCycleElement {

	@Override
	public void doLoad(IBPELUnitContext context) {
	}

	@Override
	public void doPrepareProcesses(IBPELUnitContext context)
			throws DeploymentException {
	}

	@Override
	public void doRegisterMocks(IBPELUnitContext context) {
	}

	@Override
	public void doReport(IBPELUnitContext context) {
	}

	@Override
	public void doMarkProcesses(IBPELUnitContext context)
			throws DeploymentException {
	}

}
