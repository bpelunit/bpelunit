package net.bpelunit.framework.execution.timemocking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.bpelunit.framework.control.deploy.DeploymentMock;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IWaitingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
	private static final String XPATH_TO_WAIT_TO_LEAVE_UNCHANGED = "//bpel:wait[@name='WaitToLeave']";

	private static final String XPATH_TO_ONALARM_WITH_FOR_IN_PICK = "//bpel:pick/bpel:onAlarm[1]";
	private static final String XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK = "//bpel:pick/bpel:onAlarm[2]";

	private static final String XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER = "//bpel:eventHandlers/bpel:onAlarm[1]";
	private static final String XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER = "//bpel:eventHandlers/bpel:onAlarm[2]";

	private TimeMocking timeMocking;

	@Before
	public void setUp() {
		this.timeMocking = new TimeMocking();
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
	@Ignore
	public void testSuccessfulWaitWithForMockingSingleProcess()
			throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertWaitWithForChanged(process);
		assertWaitWithUntilUnchanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesLocalName()
			throws Exception {
		timeMocking.setBPELName(BPEL_PROCESS_LOCALNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL, RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertWaitWithForChanged(process);
		assertWaitWithUntilUnchanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulWaitWithForMockingFromMultipleProcessesQName()
			throws Exception {
		timeMocking.setBPELName(BPEL_PROCESS_QNAME);
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_FOR);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL, RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertWaitWithForChanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertWaitWithUntilUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulWaitWithUntilMockingSingleProcess()
			throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_WAIT_WITH_UNTIL);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertNotToChangeWaitUnchanged(process);
		assertWaitWithUntilChanged(process);
		assertWaitWithForUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulMultipleWaitMockingSingleProcess()
			throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_ALL_WAITS_TO_MOCK);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertWaitWithForChanged(process);
		assertWaitWithUntilChanged(process);
		assertNotToChangeWaitUnchanged(process);
		assertPicksUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulOnAlarmWithForInPick() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_FOR_IN_PICK);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertPickWithForChanged(process);
		assertPickWithUntilUnchanged(process);
		assertWaitsUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulOnAlarmWithUntilInPick() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertPickWithUntilChanged(process);
		assertPickWithForUnchanged(process);
		assertWaitsUnchanged(process);
		assertHandlerUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulOnAlarmWithForHandler() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertHandlerWithForChanged(process);
		assertHandlerWithUntilUnchanged(process);
		assertPicksUnchanged(process);
		assertWaitsUnchanged(process);
	}

	@Test
	@Ignore
	public void testSuccessfulOnAlarmWithUntilHandler() throws Exception {
		timeMocking.setNewDuration(NEW_DURATION_IN_SECONDS);
		timeMocking.setActivityToMock(XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER);

		DeploymentMock d = new DeploymentMock(RESOURCE_BPEL);
		timeMocking.changeDeployment(d, null);

		IProcess process = d.getBPELProcesses().get(0).getProcessModel();
		assertHandlerWithUntilChanged(process);
		assertHandlerWithForUnchanged(process);
		assertPicksUnchanged(process);
		assertWaitsUnchanged(process);
	}

	private void assertHandlerWithUntilUnchanged(IProcess process) {
		assertEquals(
				OLD_UNTIL_EXPRESSION, getUntilFromTimedActivity(XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER, process));
	}

	private void assertHandlerWithForChanged(IProcess process) {
		assertEquals(NEW_FOR_EXPRESSION, getForFromTimedActivity(XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER, process));
	}

	private void assertHandlerWithUntilChanged(IProcess process) {
		assertEquals(NEW_FOR_EXPRESSION,getForFromTimedActivity(XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER, process));
		assertNull("0", getForFromTimedActivity(XPATH_TO_ONALARM_WITH_UNTIL_IN_HANDLER, process));
	}

	private void assertHandlerWithForUnchanged(IProcess process) {
		assertEquals(OLD_FOR_EXPRESSION, getForFromTimedActivity(XPATH_TO_ONALARM_WITH_FOR_IN_HANDLER, process));
	}

	private void assertPickWithForUnchanged(IProcess process) {
		assertEquals(OLD_FOR_EXPRESSION, getForFromTimedActivity(XPATH_TO_ONALARM_WITH_FOR_IN_PICK, process));
	}

	private void assertPickWithUntilChanged(IProcess process) {
		assertEquals(NEW_FOR_EXPRESSION, getForFromTimedActivity(XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK, process));
		assertNull(getForFromTimedActivity(XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK, process));
	}

	private void assertPickWithUntilUnchanged(IProcess process) {
		assertEquals(
				OLD_UNTIL_EXPRESSION,
				getUntilFromTimedActivity(XPATH_TO_ONALARM_WITH_UNTIL_IN_PICK
						, process));
	}

	private void assertPickWithForChanged(IProcess process) {
		assertEquals(
				NEW_FOR_EXPRESSION,
				getForFromTimedActivity(XPATH_TO_ONALARM_WITH_FOR_IN_PICK, process));
	}

	private void assertWaitsUnchanged(IProcess process) {
		assertWaitWithForUnchanged(process);
		assertWaitWithUntilUnchanged(process);
		assertNotToChangeWaitUnchanged(process);
	}

	private void assertWaitWithUntilUnchanged(IProcess process) {
		assertEquals(
				OLD_UNTIL_EXPRESSION,
				getUntilFromTimedActivity(XPATH_TO_WAIT_WITH_UNTIL, process));
	}

	private void assertNotToChangeWaitUnchanged(IProcess process) {
		assertEquals(OLD_FOR_EXPRESSION, getForFromTimedActivity(
				XPATH_TO_WAIT_TO_LEAVE_UNCHANGED, process));
	}

	private void assertWaitWithForChanged(IProcess process) {		
		assertEquals(NEW_FOR_EXPRESSION, getForFromTimedActivity(XPATH_TO_WAIT_WITH_FOR, process));
	}

	private void assertWaitWithForUnchanged(IProcess process) {
		assertEquals(OLD_FOR_EXPRESSION, getForFromTimedActivity(XPATH_TO_WAIT_WITH_FOR, process));
	}

	private void assertWaitWithUntilChanged(IProcess process) {
		assertEquals(NEW_FOR_EXPRESSION,
				getForFromTimedActivity(XPATH_TO_WAIT_WITH_UNTIL, process));
		assertEquals(null,
				getUntilFromTimedActivity(XPATH_TO_WAIT_WITH_UNTIL, process));
	}

	private void assertPicksUnchanged(IProcess process) {
		assertPickWithForUnchanged(process);
		assertPickWithUntilUnchanged(process);
	}

	private void assertHandlerUnchanged(IProcess process) {
		assertHandlerWithForUnchanged(process);
		assertHandlerWithUntilUnchanged(process);
	}

	private String getUntilFromTimedActivity(String xpathToActivity, IProcess p) {
		IWaitingActivity elementsByXPath = (IWaitingActivity) p
				.getElementsByXPath(xpathToActivity).get(0);

		return elementsByXPath.getDeadline();
	}

	private String getForFromTimedActivity(String xpathToActivity, IProcess p) {
		IWaitingActivity elementsByXPath = (IWaitingActivity) p
				.getElementsByXPath(xpathToActivity).get(0);

		return elementsByXPath.getDuration();
	}
}
