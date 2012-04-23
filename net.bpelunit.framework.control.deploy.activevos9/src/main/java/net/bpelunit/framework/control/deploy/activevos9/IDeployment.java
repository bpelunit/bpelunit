package net.bpelunit.framework.control.deploy.activevos9;

import java.util.List;

import net.bpelunit.framework.exception.DeploymentException;

public interface IDeployment {

	List<?extends IBPELProcess> getBPELProcesses() throws DeploymentException;

}
