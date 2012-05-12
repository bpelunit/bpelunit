package net.bpelunit.framework.control.deploymentchanger.waitmocking;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.util.XMLUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class WaitMockingTest {

	static class BPELProcessMock implements IBPELProcess {
		
		private QName name;
		private Document bpel;

		public BPELProcessMock(Document bpelXml) {
			this.bpel = bpelXml;
			
			Element processElement = bpelXml.getDocumentElement();
			this.name = new QName(processElement.getAttribute("targetNamespace"), processElement.getAttribute("name"));
		}
		
		@Override
		public void addPartnerlink(String name, QName partnerlinkType,
				String processRole, String partnerRole, QName service,
				String port, String endpointURL) {
		}

		@Override
		public void addWSDLImport(String wsdlFileName, InputStream contents) {

		}

		@Override
		public QName getName() {
			return this.name;
		}

		@Override
		public Document getBpelXml() {
			return this.bpel;
		}

		@Override
		public void changePartnerEndpoint(String partnerLinkName,
				String newEndpoint) throws DeploymentException {
		}
	}

	static class DeploymentMock implements IDeployment {

		private List<IBPELProcess> processes = new ArrayList<IBPELProcess>();

		public DeploymentMock(String... resourceNames) throws SAXException,
				IOException, ParserConfigurationException {
			if (resourceNames == null) {
				return;
			}

			for (String resourceName : resourceNames) {
				InputStream r = getClass().getResourceAsStream(resourceName);
				Document bpelXml = XMLUtil.parseXML(r);

				processes.add(new BPELProcessMock(bpelXml));
			}
		}

		@Override
		public List<? extends IBPELProcess> getBPELProcesses()
				throws DeploymentException {
			return processes;
		}

	}

	private WaitMocking waitMocking;
	private XPathTool xpath;

	@Before
	public void setUp() {
		this.waitMocking = new WaitMocking();
		this.xpath = waitMocking
				.createBpelXPathTool(BpelXMLTools.NAMESPACE_BPEL_2_0.getURI());
	}

	@After
	public void tearDown() {
		this.waitMocking = null;
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingBpelNameWithMultipleProcesses()
			throws Exception {
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//some/xpath");

		waitMocking.changeDeployment(new DeploymentMock("waitprocess.bpel", "waitprocess.bpel"));
	}
	
	@Test(expected = DeploymentException.class)
	public void testWrongConfigNonExistingBpelNameWithMultipleProcessesLocalName()
	throws Exception {
		waitMocking.setBPELName("Non-existent-process");
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//some/xpath");
		
		waitMocking.changeDeployment(new DeploymentMock("waitprocess.bpel", "waitprocess.bpel"));
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigNonExistingBpelNameWithMultipleProcessesQName()
	throws Exception {
		waitMocking.setBPELName("{http://waitprocess}Non-existent-process");
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//some/xpath");
		
		waitMocking.changeDeployment(new DeploymentMock("waitprocess.bpel", "waitprocess.bpel"));
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingNewDuration() throws Exception {
		waitMocking.setBPELName("MyBPEL");
		waitMocking.setWaitToMock("//some/xpath");

		waitMocking.changeDeployment(new DeploymentMock());
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingWaitToMock() throws Exception {
		waitMocking.setBPELName("MyBPEL");
		waitMocking.setNewDuration("1");

		waitMocking.changeDeployment(new DeploymentMock());
	}

	@Test
	public void testSuccessfulWaitWithForMockingSingleProcess()
			throws Exception {
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//bpel:wait[@name='WaitToMock']");

		DeploymentMock d = new DeploymentMock("waitprocess.bpel");
		waitMocking.changeDeployment(d);

		IBPELProcess process = d.getBPELProcesses().get(0);
		assertEquals("'PT1S'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock']/bpel:for/text()", process
						.getBpelXml().getDocumentElement()));

		assertEquals("\"P1D\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToLeave']/bpel:for/text()", process
						.getBpelXml().getDocumentElement()));
		assertEquals("'2012-01-01T00:00:00Z'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock2']/bpel:until/text()", process
						.getBpelXml().getDocumentElement()));
	}

	@Test
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesLocalName()
	throws Exception {
		waitMocking.setBPELName("waitprocess");
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//bpel:wait[@name='WaitToMock']");
		
		DeploymentMock d = new DeploymentMock("waitprocess.bpel", "waitprocess.bpel");
		waitMocking.changeDeployment(d);
		
		IBPELProcess process = d.getBPELProcesses().get(0);
		assertEquals("'PT1S'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		
		assertEquals("\"P1D\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToLeave']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		assertEquals("'2012-01-01T00:00:00Z'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock2']/bpel:until/text()", process
				.getBpelXml().getDocumentElement()));
	}

	@Test
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesQName()
	throws Exception {
		waitMocking.setBPELName("{http://waitprocess}waitprocess");
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//bpel:wait[@name='WaitToMock']");
		
		DeploymentMock d = new DeploymentMock("waitprocess.bpel", "waitprocess.bpel");
		waitMocking.changeDeployment(d);
		
		IBPELProcess process = d.getBPELProcesses().get(0);
		assertEquals("'PT1S'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		
		assertEquals("\"P1D\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToLeave']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		assertEquals("'2012-01-01T00:00:00Z'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock2']/bpel:until/text()", process
				.getBpelXml().getDocumentElement()));
	}
	
	@Test
	public void testSuccessfulWaitWithUntilMockingSingleProcess()
	throws Exception {
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//bpel:wait[@name='WaitToMock2']");
		
		DeploymentMock d = new DeploymentMock("waitprocess.bpel");
		waitMocking.changeDeployment(d);
		
		IBPELProcess process = d.getBPELProcesses().get(0);
		assertEquals("'PT1S'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock2']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		assertEquals("0", xpath.evaluateAsString(
				"count(//bpel:wait[@name='WaitToMock2']/bpel:until)", process
				.getBpelXml().getDocumentElement()));
		
		assertEquals("\"P1D\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToLeave']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		assertEquals("\"PT5S\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
	}
	
	@Test
	public void testSuccessfulMultipleWaitMockingSingleProcess()
	throws Exception {
		waitMocking.setNewDuration("1");
		waitMocking.setWaitToMock("//bpel:wait[@name != 'WaitToLeave']");
		
		DeploymentMock d = new DeploymentMock("waitprocess.bpel");
		waitMocking.changeDeployment(d);
		
		IBPELProcess process = d.getBPELProcesses().get(0);
		assertEquals("'PT1S'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		
		assertEquals("'PT1S'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock2']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
		assertEquals("0", xpath.evaluateAsString(
				"count(//bpel:wait[@name='WaitToMock2']/bpel:until)", process
				.getBpelXml().getDocumentElement()));
		
		assertEquals("\"P1D\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToLeave']/bpel:for/text()", process
				.getBpelXml().getDocumentElement()));
	}

}
