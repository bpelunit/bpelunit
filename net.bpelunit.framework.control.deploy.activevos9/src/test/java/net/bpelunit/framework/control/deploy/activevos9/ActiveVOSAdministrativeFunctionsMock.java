package net.bpelunit.framework.control.deploy.activevos9;

import java.util.ArrayList;
import java.util.List;

import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.AdminFaultMsg;
import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.IAeAxisActiveBpelAdmin;

import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.AdminAPIFault;
import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.IAeContributionManagement;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesAddAttachmentDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesAddAttachmentResponseType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesBreakpointRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesCompleteActivityType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesConfigurationType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDeployBprType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDigestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesEngineRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesGetVariableDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessContainerType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessCountType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessDetailType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessFilterType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessListType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessObjectType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesProcessType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesRemoveAttachmentDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesRemoveBreakpointRequestType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesRetryActivityType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesServerLogFilterType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesServerLogListType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesServerSettingsType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesSetCorrelationType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesSetPartnerLinkType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesSetVariableDataType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesStringResponseType;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionDetail;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionListResult;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionSearchFilter;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesVoidType;
import com.active_endpoints.schemas.engineapi._2010._05.enginemanagementtypes.AesDeleteContributionType;
import com.active_endpoints.schemas.engineapi._2010._05.enginemanagementtypes.AesSetOnlineType;

public class ActiveVOSAdministrativeFunctionsMock extends
		ActiveVOSAdministrativeFunctions {

	private String password;
	private String username;
	private String endpoint;
	private List<MethodCall> methodCalls = new ArrayList<MethodCall>();
	private AeAxisActiveBpelAdminMock adminMock = new AeAxisActiveBpelAdminMock();
	private AeContributionManagementMock contributionManagementMock = new AeContributionManagementMock();

	private class AeAxisActiveBpelAdminMock implements IAeAxisActiveBpelAdmin {

		@Override
		public AesStringResponseType deployBpr(AesDeployBprType deployBprInput) {
			methodCalls.add(new MethodCall("deployBpr", deployBprInput));

			AesStringResponseType response = new AesStringResponseType();
			response.setResponse("<deploymentSummary numErrors=\"0\" numWarnings=\"0\"><globalMessages>[bpelunit-tc1.bpr] [bpelunit-tc1.bpr] Skipping BPR archive deployment as the current version is already up to date.</globalMessages></deploymentSummary>");
			return response;
		}

		@Override
		public AesConfigurationType getConfiguration(
				com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesVoidType input)
				throws AdminFaultMsg {
			return null;
		}

		@Override
		public void setConfiguration(AesConfigurationType input)
				throws AdminFaultMsg {
		}

		@Override
		public AesServerSettingsType getServerSettings(
				com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesVoidType input)
				throws AdminFaultMsg {
			return null;
		}

		@Override
		public void setServerSettings(AesServerSettingsType input)
				throws AdminFaultMsg {
		}

		@Override
		public void suspendProcess(AesProcessType input) throws AdminFaultMsg {
		}

		@Override
		public void resumeProcess(AesProcessType input) throws AdminFaultMsg {
		}

		@Override
		public void gotoProcessObject(AesProcessObjectType input)
				throws AdminFaultMsg {
		}

		@Override
		public void resumeProcessObject(AesProcessObjectType input)
				throws AdminFaultMsg {
		}

		@Override
		public void resumeProcessContainer(AesProcessContainerType input)
				throws AdminFaultMsg {
		}

		@Override
		public void terminateProcess(AesProcessType input) throws AdminFaultMsg {
		}

		@Override
		public void addEngineListener(AesEngineRequestType input) {
		}

		@Override
		public void addBreakpointListener(AesBreakpointRequestType input) {
		}

		@Override
		public void updateBreakpointList(AesBreakpointRequestType input) {
		}

		@Override
		public void removeEngineListener(AesEngineRequestType input) {
		}

		@Override
		public void removeBreakpointListener(
				AesRemoveBreakpointRequestType input) {
		}

		@Override
		public void addProcessListener(AesProcessRequestType input) {
		}

		@Override
		public void removeProcessListener(AesProcessRequestType input) {
		}

		@Override
		public AesStringResponseType getVariable(AesGetVariableDataType input) {
			return null;
		}

		@Override
		public AesStringResponseType setVariable(AesSetVariableDataType input) {
			return null;
		}

		@Override
		public AesAddAttachmentResponseType addAttachment(
				AesAddAttachmentDataType input, byte[] attachment) {
			return null;
		}

		@Override
		public AesStringResponseType removeAttachments(
				AesRemoveAttachmentDataType input) {
			return null;
		}

		@Override
		public AesProcessListType getProcessList(AesProcessFilterType input) {
			return null;
		}

		@Override
		public AesProcessCountType getProcessCount(AesProcessFilterType input) {
			return null;
		}

		@Override
		public AesProcessDetailType getProcessDetail(AesProcessType input) {
			return null;
		}

		@Override
		public AesStringResponseType getProcessState(AesProcessType input) {
			return null;
		}

		@Override
		public AesDigestType getProcessDigest(AesProcessType input) {
			return null;
		}

		@Override
		public AesStringResponseType getProcessDef(AesProcessType input) {
			return null;
		}

		@Override
		public AesStringResponseType getProcessLog(AesProcessType input) {
			return null;
		}

		@Override
		public AesStringResponseType getAPIVersion(
				com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesVoidType input) {
			return null;
		}

		@Override
		public void setPartnerLinkData(AesSetPartnerLinkType input) {
			
		}

		@Override
		public void setCorrelationSetData(AesSetCorrelationType input) {
		}

		@Override
		public void retryActivity(AesRetryActivityType input) {
		}

		@Override
		public void completeActivity(AesCompleteActivityType input) {
		}

		@Override
		public AesServerLogListType getServerLogList(
				AesServerLogFilterType input) {
			return null;
		}

		@Override
		public void clearServerLog() throws AdminFaultMsg {
		}

	}

	private final class AeContributionManagementMock implements
			IAeContributionManagement {
		private AesContributionListResult result = new AesContributionListResult();

		@Override
		public AesVoidType takePlanOffline(int input) throws AdminAPIFault {
			return null;
		}

		@Override
		public AesVoidType takeContributionOffline(int input)
				throws AdminAPIFault {
			return null;
		}

		@Override
		public AesVoidType setPlanOnline(int input) throws AdminAPIFault {
			return null;
		}

		@Override
		public AesVoidType setContributionOnline(AesSetOnlineType input)
				throws AdminAPIFault {
			return null;
		}

		@Override
		public AesContributionListResult searchContributions(
				AesContributionSearchFilter input) {
			result.setCompleteRowCount(true);
			result.setTotalRowCount(1);

			AesContribution contribution = new AesContribution();
			contribution.setId(1000);
			result.getContributionItem().add(contribution);
			return result;
		}

		@Override
		public int purgeOffline(AesVoidType input) throws AdminAPIFault {
			return 0;
		}

		@Override
		public String getDeploymentLog(int input) throws AdminAPIFault {
			return null;
		}

		@Override
		public AesContributionDetail getContributionDetail(int input) {
			return null;
		}

		@Override
		public AesVoidType deletePlan(int input) throws AdminAPIFault {
			return null;
		}

		@Override
		public AesVoidType deleteContribution(AesDeleteContributionType input)
				throws AdminAPIFault {
			methodCalls.add(new MethodCall("deleteContribution", input));
			return new AesVoidType();
		}
	}

	public static class MethodCall {
		public MethodCall(String methodName, Object... parameters) {
			super();
			this.methodName = methodName;
			this.parameters = parameters;
		}

		public String methodName;
		public Object[] parameters;
	}

	public ActiveVOSAdministrativeFunctionsMock(String endpoint,
			String username, String password) {
		super(endpoint, username, password);

		this.endpoint = endpoint;
		this.username = username;
		this.password = password;
	}

	protected String getPassword() {
		return password;
	}

	protected String getUsername() {
		return username;
	}

	protected String getEndpoint() {
		return endpoint;
	}

	@Override
	protected IAeAxisActiveBpelAdmin getActiveBpelAdminPort() {
		return this.adminMock;
	}

	@Override
	protected IAeContributionManagement getContributionManagementPort() {
		return this.contributionManagementMock;
	}
	
	public List<MethodCall> getMethodsCalls() {
		return this.methodCalls;
	}

}
