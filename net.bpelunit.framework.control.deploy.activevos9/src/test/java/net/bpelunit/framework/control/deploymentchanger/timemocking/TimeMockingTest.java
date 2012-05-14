package net.bpelunit.framework.control.deploymentchanger.timemocking;

import static org.junit.Assert.assertEquals;

import javax.xml.xpath.XPathExpressionException;

import net.bpelunit.framework.control.deploy.DeploymentMock;
import net.bpelunit.framework.control.util.XPathTool;
import net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import net.bpelunit.framework.exception.DeploymentException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class TimeMockingTest {
	private static final String RESOURCE_BPEL = "waitprocess.bpel";

	private static final String BPEL_PROCESS_LOCALNAME = "waitprocess";
	private static final String BPEL_PROCESS_QNAME = "{http://test.bpelunit.net/waitprocess}waitprocess";

	private static final String NONEXISTING_BPEL_PROCESS_LOCALNAME = "Non-existent-process";
	private static final String NONEXISTING_BPEL_PROCESS_QNAME = "{http://test.bpelunit.net/waitprocess}Non-existent-process";

	private static final String NEW_DURATION_IN_SECONDS = "1";

	private static final String NEW_FOR_EXPRESSION = "'PT1S'";
	private static final String OLD_UNTIL_EXPRESSION = "'2012-01-01'";
	private static final String OLD_FOR_EXPRESSION = "'P1D'";

	private static final String XPATH_TO_WAIT_WITH_FOR = "//bpel:wait[@name='WaitToMock']";
	private static final String XPATH_TO_WAIT_WITH_UNTIL = "//bpel:wait[@name='WaitToMock2']";
	private static final String XPATH_ALL_WAITS_TO_MOCK = "//bpel:wait[@name != 'WaitToLeave']";
	private static final String XPATH_TO_FOR_IN_WAIT_TO_LEAVE_UNCHANGED = "//bpel:wait[@name='WaitToLeave']/bpel:for/text()";

	private static final String XPATH_TO_ONALARM_WITH_FOR_IN_PICK = "//bpel:pick/bpel:onAlarm[1]";
	private static final String XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK = "//bpel:pick/bpel:onAlarm[2]";

	private static final String XPATH_TO_FOR = "/bpel:for/text()";
	private static final String XPATH_TO_UNTIL = "/bpel:until/text()";

	private static final String XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER = "//bpel:eventHandlers/bpel:onAlarm[1]";
	private static final String XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER = "//bpel:eventHandlers/bpel:onAlarm[2]";

	private TimeMocking timeMocking;
	private XPathTool xpath;

	@Before
	public void setUp() {
		this.timeMocking = new TimeMocking();
		this.xpath = timeMocking
				.createBpelXPathTool(BpelXMLTools.NAMESPACE_BPEL_2_0.getURI());
	}

	@After
	public void tearDown() {
		this.timeMocking = null;
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingBpelNameWithMultipleProcesses()
			throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		timeMocking.changeDeployment(new DeploymentMock(RESOURCE_BPEL,
				RESOURCE_BPEL), null);
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigNonExistingBpelNameWithMultipleProcessesLocalName()
			throws Exception {
		timeMocking.setBPELName(NONEXISTING_BPEL_PROCESS_LOCALNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		timeMocking.changeDeployment(new DeploymentMock(RESOURCE_BPEL,
				RESOURCE_BPEL), null);
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigNonExistingBpelNameWithMultipleProcessesQName()
			throws Exception {
		timeMocking.setBPELName(NONEXISTING_BPEL_PROCESS_QNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		timeMocking.changeDeployment(new DeploymentMock(RESOURCE_BPEL,
				RESOURCE_BPEL), null);
	}

	@Test(expected = DeploymentException.class)
	public void testWrongDeploymentNoProcess() throws Exception {
		timeMocking.setBPELName(BPEL_PROCESS_QNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		timeMocking.changeDeployment(new DeploymentMock(), null);
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingNewDuration() throws Exception {
		timeMocking.setBPELName(BPEL_PROCESS_LOCALNAME);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		timeMocking.changeDeployment(new DeploymentMock(), null);
	}

	@Test(expected = DeploymentException.class)
	public void testWrongConfigMissingWaitToMock() throws Exception {
		timeMocking.setBPELName(BPEL_PROCESS_LOCALNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);

		timeMocking.changeDeployment(new DeploymentMock(), null);
	}

	@Test
	public void testSuccessfulWaitWithForMockingSingleProcess()
			throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertWaitWithForChanged(process);
		assertWaitWithUntilUnchanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesLocalName()
			throws Exception {
		timeMocking.setBPELName(BPEL_PROCESS_LOCALNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL, RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertWaitWithForChanged(process);
		assertWaitWithUntilUnchanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesQName()
			throws Exception {
		timeMocking.setBPELName(BPEL_PROCESS_QNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL, RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertWaitWithForChanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertWaitWithUntilUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	public void testSuccessfulWaitWithUntilMockingSingleProcess()
			throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_UNTIL);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertNotToChangeWaitUnchanged(process);
		assertWaitWithUntilChanged(process);
		assertWaitWithForUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	public void testSuccessfulMultipleWaitMockingSingleProcess()
			throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_ALL_WAITS_TO_MOCK);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertWaitWithForChanged(process);
		assertWaitWithUntilChanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	public void testSuccessfulOnAlarmWithForInPick() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_FOR_IN_PICK);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertPickWithForChanged(process);
		assertPickWithUntilUnchanged(process);
		assertWaitsUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	public void testSuccessfulOnAlarmWithUntilInPick() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertPickWithUntilChanged(process);
		assertPickWithForUnchanged(process);
		assertWaitsUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	public void testSuccessfulOnAlarmWithForHandler() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		Element process = d.getBPELProcesses().get(0).getBpelXml()
				.getDocumentElement();
		assertHandlerWithForChanged(process);
		assertHandlerWithUntilUnchanged(process);
		assertPicksUnchanged(process);
		assertWaitsUnchanged(process);
	}
	
	@Test
	public void testSuccessfulOnAlarmWithUntilHandler() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER);
		
		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);
		
		Element process = d.getBPELProcesses().get(0).getBpelXml()
		.getDocumentElement();
		assertHandlerWithUntilChanged(process);
		assertHandlerWithForUnchanged(process);
		assertPicksUnchanged(process);
		assertWaitsUnchanged(process);
	}
	
	private void assertHandlerWithUntilUnchanged(Element process) throws XPathExpressionException {
		assertEquals(
				OLD_UNTIL_EXPRESSION,
				xpath.evaluateAsString(XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER
						+ XPATH_TO_UNTIL, process));
	}

	private void assertHandlerWithForChanged(Element process) throws XPathExpressionException {
		String exp = XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER + XPATH_TO_FOR;
		assertEquals(NEW_FOR_EXPRESSION,
				xpath.evaluateAsString(exp, process));
	}
	
	private void assertHandlerWithUntilChanged(Element process) throws XPathExpressionException {
		String exp = XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER + XPATH_TO_FOR;
		assertEquals(NEW_FOR_EXPRESSION,
				xpath.evaluateAsString(exp, process));

		exp = "count(" + XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER + XPATH_TO_UNTIL
				+ ")";
		assertEquals("0", xpath.evaluateAsString(exp, process));
	}
	
	private void assertHandlerWithForUnchanged(Element process) throws XPathExpressionException {
		String exp = XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER + XPATH_TO_FOR;
		assertEquals(OLD_FOR_EXPRESSION,
				xpath.evaluateAsString(exp, process));
	}

	private void assertPickWithForUnchanged(Element process) throws XPathExpressionException {
		String exp = XPATH_TO_ONALARM_WITH_FOR_IN_PICK + XPATH_TO_FOR;
		assertEquals(OLD_FOR_EXPRESSION,
				xpath.evaluateAsString(exp, process));
	}

	private void assertPickWithUntilChanged(Element process)
			throws XPathExpressionException {
		String exp = XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK + XPATH_TO_FOR;
		assertEquals(NEW_FOR_EXPRESSION,
				xpath.evaluateAsString(exp, process));

		exp = "count(" + XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK + XPATH_TO_UNTIL
				+ ")";
		assertEquals("0", xpath.evaluateAsString(exp, process));
	}

	private void assertPickWithUntilUnchanged(Element process)
			throws XPathExpressionException {
		assertEquals(
				OLD_UNTIL_EXPRESSION,
				xpath.evaluateAsString(XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK
						+ XPATH_TO_UNTIL, process));
	}

	private void assertPickWithForChanged(Element process)
			throws XPathExpressionException {
		assertEquals(
				NEW_FOR_EXPRESSION,
				xpath.evaluateAsString(XPATH_TO_ONALARM_WITH_FOR_IN_PICK
						+ XPATH_TO_FOR, process));
	}

	private void assertWaitsUnchanged(Element process)
			throws XPathExpressionException {
		assertWaitWithForUnchanged(process);
		assertWaitWithUntilUnchanged(process);
		assertNotToChangeWaitUnchanged(process);
	}

	private void assertWaitWithUntilUnchanged(Element process)
			throws XPathExpressionException {
		assertEquals(OLD_UNTIL_EXPRESSION, xpath.evaluateAsString(
				XPATH_TO_WAIT_WITH_UNTIL + XPATH_TO_UNTIL, process));
	}

	private void assertNotToChangeWaitUnchanged(Element process)
			throws XPathExpressionException {
		assertEquals(OLD_FOR_EXPRESSION, xpath.evaluateAsString(
				XPATH_TO_FOR_IN_WAIT_TO_LEAVE_UNCHANGED, process));
	}

	private void assertWaitWithForChanged(Element process)
			throws XPathExpressionException {
		String exp = XPATH_TO_WAIT_WITH_FOR + XPATH_TO_FOR;
		assertEquals(NEW_FOR_EXPRESSION,
				xpath.evaluateAsString(exp, process));
	}

	private void assertWaitWithForUnchanged(Element process)
			throws XPathExpressionException {
		assertEquals(OLD_FOR_EXPRESSION, xpath.evaluateAsString(
				XPATH_TO_WAIT_WITH_FOR + XPATH_TO_FOR, process));
	}

	private void assertWaitWithUntilChanged(Element process)
			throws XPathExpressionException {
		String exp = XPATH_TO_WAIT_WITH_UNTIL + XPATH_TO_FOR;
		assertEquals(NEW_FOR_EXPRESSION,
				xpath.evaluateAsString(exp, process));
		exp = count(XPATH_TO_WAIT_WITH_UNTIL + XPATH_TO_UNTIL);
		assertEquals("0", xpath.evaluateAsString(exp, process));
	}

	private void assertPicksUnchanged(Element process) throws XPathExpressionException {
		assertPickWithForUnchanged(process);
		assertPickWithUntilUnchanged(process);
	}
	
	private void assertHandlerUnchanged(Element process) throws XPathExpressionException {
		assertHandlerWithForUnchanged(process);
		assertHandlerWithUntilUnchanged(process);
	}
	
	private String count(String xpath) {
		return String.format("count(%s)", xpath);
	}

}
