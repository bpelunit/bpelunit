package net.bpelunit.framework.execution.endpointreplacement;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.bpelunit.framework.control.deploy.DeploymentMock;
import net.bpelunit.framework.control.deploy.DeploymentMock.BPELProcessMock;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.Partner;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.framework.model.test.TestSuite;

import org.junit.Test;


public class EndpointReplacerTest {

	private static final String BASE_URL = "http://localhost:7777/ws";
	private EndpointReplacer endpointReplacer = new EndpointReplacer();
	
	@Test
	public void testReplacementAllPartnersAreMockedSingleProcess() throws Exception {
		TestSuite ts = createTestSuite();
		addPartnerTrack(ts, "Provider");
		addPartnerTrack(ts, "Provider2");
		DeploymentMock deployment = new DeploymentMock("tc1.bpel");
		
		endpointReplacer.changeDeployment(deployment, ts);
		
		BPELProcessMock process = deployment.getBPELProcesses().get(0);
		Map<String, String> changedEndpoints = process.getChangedEndpoints();
		assertEquals(2, changedEndpoints.size());
		assertEquals("http://localhost:7777/ws/Provider", changedEndpoints.get("Provider"));
		assertEquals("http://localhost:7777/ws/Provider2", changedEndpoints.get("Provider2"));
	}
	
	@Test
	public void testReplacementNotAllPartnersAreMockedSingleProcess() throws Exception {
		TestSuite ts = createTestSuite();
		addPartnerTrack(ts, "Provider");
		addPartnerTrack(ts, "Provider2");
		addPartnerTrack(ts, "ProviderUnused");
		DeploymentMock deployment = new DeploymentMock("tc1.bpel");
		
		endpointReplacer.changeDeployment(deployment, ts);
		
		BPELProcessMock process = deployment.getBPELProcesses().get(0);
		Map<String, String> changedEndpoints = process.getChangedEndpoints();
		assertEquals(2, changedEndpoints.size());
		assertEquals("http://localhost:7777/ws/Provider", changedEndpoints.get("Provider"));
		assertEquals("http://localhost:7777/ws/Provider2", changedEndpoints.get("Provider2"));
	}

	private void addPartnerTrack(TestSuite ts, String name) throws SpecificationException {
		Partner p = new Partner(name, null, null, BASE_URL);
		ts.getProcessUnderTest().getPartners().put(name, p);
	}

	private TestSuite createTestSuite() throws SpecificationException,
			MalformedURLException {
		ProcessUnderTest put = new ProcessUnderTest("bpelName", ".", null, null, BASE_URL);
		TestSuite ts = new TestSuite("test.bpts", new URL(BASE_URL), put);
		
		put.setPartners(new HashMap<String, Partner>());
		
		return ts;
	}
	
	@Test
	public void testReplacementNotAllPartnerLinksAreMockedSingleProcess() throws Exception {
		TestSuite ts = createTestSuite();
		addPartnerTrack(ts, "Provider");
		DeploymentMock deployment = new DeploymentMock("tc1.bpel");
		
		endpointReplacer.changeDeployment(deployment, ts);
		
		BPELProcessMock process = deployment.getBPELProcesses().get(0);
		Map<String, String> changedEndpoints = process.getChangedEndpoints();
		assertEquals(1, changedEndpoints.size());
		assertEquals("http://localhost:7777/ws/Provider", changedEndpoints.get("Provider"));
	}
	
	@Test
	public void testReplacementAllPartnersAreMockedTwoProcesses() throws Exception {
		TestSuite ts = createTestSuite();
		addPartnerTrack(ts, "Provider");
		addPartnerTrack(ts, "Provider2");
		DeploymentMock deployment = new DeploymentMock("tc1.bpel", "tc1.bpel");
		
		endpointReplacer.changeDeployment(deployment, ts);
		
		BPELProcessMock process = deployment.getBPELProcesses().get(0);
		Map<String, String> changedEndpoints = process.getChangedEndpoints();
		assertEquals(2, changedEndpoints.size());
		assertEquals("http://localhost:7777/ws/Provider", changedEndpoints.get("Provider"));
		assertEquals("http://localhost:7777/ws/Provider2", changedEndpoints.get("Provider2"));		
		
		process = deployment.getBPELProcesses().get(1);
		changedEndpoints = process.getChangedEndpoints();
		assertEquals(2, changedEndpoints.size());
		assertEquals("http://localhost:7777/ws/Provider", changedEndpoints.get("Provider"));
		assertEquals("http://localhost:7777/ws/Provider2", changedEndpoints.get("Provider2"));
	}
	
}
