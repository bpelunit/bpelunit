package net.bpelunit.framework.control.ext;

import net.bpelunit.framework.exception.DeploymentException;

public interface IDeploymentChanger {

	public @interface DeploymentChangerOption {
		String description() default "";
	}

	public void changeDeployment(net.bpelunit.framework.control.deploy.activevos9.IDeployment d) throws DeploymentException;
	
}
