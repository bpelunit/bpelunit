package net.bpelunit.framework.control.deploymentchanger.waitmocking;

import static org.junit.Assert.*;

import java.util.List;

import net.bpelunit.framework.control.deploy.activevos9.IBPELProcess;
import net.bpelunit.framework.control.deploy.activevos9.IDeployment;
import net.bpelunit.framework.exception.DeploymentException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WaitMockingTest {

	public class DeploymentMock implements IDeployment {

		@Override
		public List<? extends IBPELProcess> getBPELProcesses()
				throws DeploymentException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private WaitMocking waitMocking;

	@Before
	public void setUp() {
		this.waitMocking = new WaitMocking();
	}
	
	@After
	public void tearDown() {
		this.waitMocking = null;
	}
	
	@Test(expected = DeploymentException.class)
	@Ignore("Not yet implemented")
	public void testWrongConfigMissingBpelNameWithMultipleProcesses() throws Exception {
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//some/xpath");
		
		waitMocking.changeDeployment(new DeploymentMock());
	}
	
	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingNewDuration() throws Exception {
		waitMocking.setBPELName("MyBPEL");
		waitMocking.setWaitToMock("//some/xpath");
		
		waitMocking.changeDeployment(new DeploymentMock());
	}
	
	@Test(expected = DeploymentException.class)
	@Ignore("Not yet implemented")
	public void testWrongConfigMissingWaitToMock() throws Exception {
		waitMocking.setBPELName("MyBPEL");
		waitMocking.setNewDuration("1");
		
		waitMocking.changeDeployment(new DeploymentMock());
	}

	
}
