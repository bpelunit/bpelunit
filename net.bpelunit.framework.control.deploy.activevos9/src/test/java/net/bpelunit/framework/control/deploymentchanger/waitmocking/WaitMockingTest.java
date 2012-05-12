package net.bpelunit.framework.control.deploymentchanger.waitmocking;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

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

	private static final String BPEL_PROCESS_LOCALNAME = "waitprocess";

	private static final String RESOURCE_BPEL = "waitprocess.bpel";

	private static final String BPEL_PROCESS_QNAME = "{http://test.bpelunit.net/waitprocess}waitprocess";
	private static final String NONEXISTING_BPEL_PROCESS_QNAME = "{http://test.bpelunit.net/waitprocess}Non-existent-process";

	private static final String NEW_DURATION_EXPRESSION = "'PT1S'";
	private static final String NEW_DURATION_IN_SECONDS = "1";

	private static final String XPATH_TO_WAIT1 = "//bpel:wait[@name='WaitToMock']";
	private static final String XPATH_ALL_WAITS_TO_MOCK = "//bpel:wait[@name != 'WaitToLeave']";

	static class BPELProcessMock implements IBPELProcess {

		private QName name;
		private Document bpel;

		public BPELProcessMock(Document bpelXml) {
			this.bpel = bpelXml;

			Element processElement = bpelXml.getDocumentElement();
			this.name = new QName(
					processElement.getAttribute("targetNamespace"),
					processElement.getAttribute("name"));
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
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock(XPATH_TO_WAIT1);

		waitMocking.changeDeployment(new DeploymentMock(RESOURCE_BPEL,
				RESOURCE_BPEL));
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigNonExistingBpelNameWithMultipleProcessesLocalName()
			throws Exception {
		waitMocking.setBPELName("Non-existent-process");
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock(XPATH_TO_WAIT1);

		waitMocking.changeDeployment(new DeploymentMock(RESOURCE_BPEL,
				RESOURCE_BPEL));
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigNonExistingBpelNameWithMultipleProcessesQName()
			throws Exception {
		waitMocking.setBPELName(NONEXISTING_BPEL_PROCESS_QNAME);
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock(XPATH_TO_WAIT1);

		waitMocking.changeDeployment(new DeploymentMock(RESOURCE_BPEL,
				RESOURCE_BPEL));
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingNewDuration() throws Exception {
		waitMocking.setBPELName("MyBPEL");
		waitMocking.setActivityToMock(XPATH_TO_WAIT1);

		waitMocking.changeDeployment(new DeploymentMock());
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingWaitToMock() throws Exception {
		waitMocking.setBPELName("MyBPEL");
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);

		waitMocking.changeDeployment(new DeploymentMock());
	}

	@Test
	public void testSuccessfulWaitWithForMockingSingleProcess()
			throws Exception {
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock(XPATH_TO_WAIT1);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		waitMocking.changeDeployment(d);

		IBPELProcess process = d.getBPELProcesses().get(0);
		assertWaitOneChanged(process);

		assertNotToChangeWaitUnchanged(process);
		assertWait2Unchanged(process);
	}

	@Test
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesLocalName()
			throws Exception {
		waitMocking.setBPELName(BPEL_PROCESS_LOCALNAME);
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock(XPATH_TO_WAIT1);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL, RESOURCE_BPEL);
		waitMocking.changeDeployment(d);

		IBPELProcess process = d.getBPELProcesses().get(0);
		assertWaitOneChanged(process);
		assertWait2Unchanged(process);
		assertNotToChangeWaitUnchanged(process);
	}

	@Test
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesQName()
			throws Exception {
		waitMocking.setBPELName(BPEL_PROCESS_QNAME);
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock(XPATH_TO_WAIT1);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL, RESOURCE_BPEL);
		waitMocking.changeDeployment(d);

		IBPELProcess process = d.getBPELProcesses().get(0);
		assertWaitOneChanged(process);

		assertNotToChangeWaitUnchanged(process);
		assertWait2Unchanged(process);
	}

	@Test
	public void testSuccessfulWaitWithUntilMockingSingleProcess()
			throws Exception {
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock("//bpel:wait[@name='WaitToMock2']");

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		waitMocking.changeDeployment(d);

		IBPELProcess process = d.getBPELProcesses().get(0);
		assertNotToChangeWaitUnchanged(process);
		assertWait2Changed(process);
		assertWait1Unchanged(process);
	}

	@Test
	public void testSuccessfulMultipleWaitMockingSingleProcess()
			throws Exception {
		waitMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		waitMocking.setActivityToMock(XPATH_ALL_WAITS_TO_MOCK);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		waitMocking.changeDeployment(d);

		IBPELProcess process = d.getBPELProcesses().get(0);
		assertWaitOneChanged(process);
		assertWait2Changed(process);
		assertNotToChangeWaitUnchanged(process);
	}

	private void assertWait2Unchanged(IBPELProcess process)
			throws XPathExpressionException {
		assertEquals("'2012-01-01T00:00:00Z'", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock2']/bpel:until/text()", process
						.getBpelXml().getDocumentElement()));
	}

	private void assertNotToChangeWaitUnchanged(IBPELProcess process)
			throws XPathExpressionException {
		assertEquals("\"P1D\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToLeave']/bpel:for/text()", process
						.getBpelXml().getDocumentElement()));
	}

	private void assertWaitOneChanged(IBPELProcess process)
			throws XPathExpressionException {
		assertEquals(NEW_DURATION_EXPRESSION, xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock']/bpel:for/text()", process
						.getBpelXml().getDocumentElement()));
	}

	private void assertWait1Unchanged(IBPELProcess process)
			throws XPathExpressionException {
		assertEquals("\"PT5S\"", xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock']/bpel:for/text()", process
						.getBpelXml().getDocumentElement()));
	}

	private void assertWait2Changed(IBPELProcess process)
			throws XPathExpressionException {
		assertEquals(NEW_DURATION_EXPRESSION, xpath.evaluateAsString(
				"//bpel:wait[@name='WaitToMock2']/bpel:for/text()", process
						.getBpelXml().getDocumentElement()));
		assertEquals("0", xpath.evaluateAsString(
				"count(//bpel:wait[@name='WaitToMock2']/bpel:until)", process
						.getBpelXml().getDocumentElement()));
	}

}
