package net.bpelunit.framework.control.deploy.activevos9;

import java.util.ArrayList;
import java.util.List;

import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.IAeAxisActiveBpelAdmin;

import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.AdminAPIFault;
import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.IAeContributionManagement;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDeployBprType;
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

	}

	private final class AeContributionManagementMock implements
			IAeContributionManagement {
		private AesContributionListResult result = new AesContributionListResult();

		@Override
		public AesVoidType takePlanOffline(int input) throws AdminAPIFault {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AesVoidType takeContributionOffline(int input)
				throws AdminAPIFault {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
