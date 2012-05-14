package net.bpelunit.framework.control.deploymentchanger.endpointreplacement;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
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
		
	}
	
	@Test
	public void testReplacementNotAllPartnersAreMockedSingleProcess() throws Exception {
		TestSuite ts = createTestSuite();
		addPartnerTrack(ts, "Provider");
		DeploymentMock deployment = new DeploymentMock("tc1.bpel");
		
		endpointReplacer.changeDeployment(deployment, ts);
		
		BPELProcessMock process = deployment.getBPELProcesses().get(0);
		Map<String, String> changedEndpoints = process.getChangedEndpoints();
		assertEquals(1, changedEndpoints.size());
		assertEquals("http://localhost:7777/ws/Provider", changedEndpoints.get("Provider"));
	}

	private void addPartnerTrack(TestSuite ts, String name) throws SpecificationException {
		Partner p = new Partner(name, null, null, BASE_URL);
		ts.getProcessUnderTest().getPartners().put(name, p);
	}

	private TestSuite createTestSuite() throws SpecificationException,
			MalformedURLException {
		ProcessUnderTest suiteProcessUnderTest = new ProcessUnderTest("bpelName", ".", null, null, BASE_URL);
		TestSuite ts = new TestSuite("test.bpts", new URL(BASE_URL), suiteProcessUnderTest);
		return ts;
	}
	
	@Test
	public void testReplacementNotAllPartnerLinksAreMockedSingleProcess() throws Exception {
		
	}
	
	@Test
	public void testReplacementAllPartnersAreMockedTwoProcesses() throws Exception {
		
	}
	
}
