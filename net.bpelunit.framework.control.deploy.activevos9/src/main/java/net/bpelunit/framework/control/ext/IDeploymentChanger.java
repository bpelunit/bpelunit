package net.bpelunit.framework.control.ext;

import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.test.TestSuite;

public interface IDeploymentChanger {

	public @interface DeploymentChangerOption {
		String description() default "";
	}

	void changeDeployment(net.bpelunit.framework.control.deploy.IDeployment d, TestSuite testSuite) throws DeploymentException;
	
}
