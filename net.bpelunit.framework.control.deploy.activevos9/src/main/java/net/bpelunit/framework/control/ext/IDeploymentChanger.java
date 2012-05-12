package net.bpelunit.framework.control.ext;

import net.bpelunit.framework.exception.DeploymentException;

public interface IDeploymentChanger {

	public @interface DeploymentChangerOption {
		String description() default "";
	}

	void changeDeployment(net.bpelunit.framework.control.deploy.IDeployment d) throws DeploymentException;
	
}
