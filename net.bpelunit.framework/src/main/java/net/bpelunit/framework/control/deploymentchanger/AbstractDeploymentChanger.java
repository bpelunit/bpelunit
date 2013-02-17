package net.bpelunit.framework.control.deploymentchanger;

import org.apache.commons.beanutils.BeanUtils;

import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.execution.IBPELUnitContext;
import net.bpelunit.framework.execution.ITestLifeCycleElement;

public abstract class AbstractDeploymentChanger implements ITestLifeCycleElement {

	@Override
	public final void doLoad(IBPELUnitContext context) {
	}

	@Override
	public abstract void doPrepareProcesses(IBPELUnitContext context)
			throws DeploymentException;

	@Override
	public final void doRegisterMocks(IBPELUnitContext context) {
	}

	@Override
	public final void doReport(IBPELUnitContext context) {
	}

	@Override
	public void doMarkProcesses(IBPELUnitContext context)
			throws DeploymentException {
	}

}
