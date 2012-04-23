package net.bpelunit.framework.control.deploy.activevos9;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.ActiveBpelAdmin;
import active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin.IAeAxisActiveBpelAdmin;

import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.AdminAPIFault;
import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.ContributionManagementService;
import com.active_endpoints.docs.wsdl.engineapi._2010._05.enginemanagement.IAeContributionManagement;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDeployBprType;
import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesStringResponseType;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionListResult;
import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContributionSearchFilter;
import com.active_endpoints.schemas.engineapi._2010._05.enginemanagementtypes.AesDeleteContributionType;

class ActiveVOSAdministrativeFunctions {

	public static final QName ACTIVE_BPEL_ADMIN_SERVICE_NAME = new QName(
			"http://docs.active-endpoints/wsdl/activebpeladmin/2007/01/activebpeladmin.wsdl",
			"ActiveBpelAdmin");
	public static final QName CONTRIBUTION_MANAGEMENT_SERVICE_NAME = new QName(
			"http://docs.active-endpoints.com/wsdl/engineapi/2010/05/EngineManagement.wsdl",
			"ContributionManagementService");

	public static final String ACTIVE_BPEL_ADMIN_WSDL_RESOURCE = "/ActiveBpelDeployBPR.wsdl";
	public static final String CONTRIBUTION_MANAGEMENT_SERVICE_WSDL_RESOURCE = "/AeContributionManagement.wsdl";
	
	public static final String ATTRIBUTE_ERROR_COUNT = "numErrors";
	
	private IAeAxisActiveBpelAdmin activeBpelAdminPort;
	private IAeContributionManagement contributionManagementPort;

	public ActiveVOSAdministrativeFunctions(String endpoint, String username,
			String password) {
		initializeActiveBpelAdminPort(username, password);
		initializeContributionManagementPort(username, password);
	}

	private void initializeContributionManagementPort(String username,
			String password) {
		ContributionManagementService contributionManagementService = new
		ContributionManagementService(ActiveVOSAdministrativeFunctions.class
				.getResource(CONTRIBUTION_MANAGEMENT_SERVICE_WSDL_RESOURCE), CONTRIBUTION_MANAGEMENT_SERVICE_NAME);
		
		contributionManagementPort = contributionManagementService.getContributionManagementPort();
		setBasicAuthenticationForBindingProvider(contributionManagementPort, username,
				password);
	}

	private void initializeActiveBpelAdminPort(String username, String password) {
		ActiveBpelAdmin activeBpelAdmin = new ActiveBpelAdmin(
				ActiveVOSAdministrativeFunctions.class
						.getResource(ACTIVE_BPEL_ADMIN_WSDL_RESOURCE),
				ACTIVE_BPEL_ADMIN_SERVICE_NAME);

		activeBpelAdminPort = activeBpelAdmin.getActiveBpelAdminPort();
		setBasicAuthenticationForBindingProvider(activeBpelAdminPort, username,
				password);
	}

	private void setBasicAuthenticationForBindingProvider(Object port,
			String username, String password) {
		Map<String, Object> requestContext = ((BindingProvider) port)
				.getRequestContext();
		requestContext.put(BindingProvider.USERNAME_PROPERTY, username);
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, password);
	}

	public List<AesContribution> getAllContributions() {
		AesContributionSearchFilter input = new AesContributionSearchFilter();
		AesContributionListResult searchContributions = contributionManagementPort.searchContributions(input);
				
		return searchContributions.getContributionItem();
	}
	
	public List<Integer> extractContributionIds(List<AesContribution> contributions) {
		List<Integer> result = new ArrayList<Integer>();
		for(AesContribution c : contributions) {
			result.add(c.getId());
		}
		
		return result;
	}

	public void deleteContribution(int contributionId, boolean deleteProcesses) throws AdminAPIFault {
		AesDeleteContributionType input = new AesDeleteContributionType();
		input.setContributionId(contributionId);
		input.setDeleteProcesses(deleteProcesses);
		contributionManagementPort.deleteContribution(input);
	}
	
	public void deployBpr(String bprFileName, InputStream contents) {
		try {
			deployBpr(bprFileName, IOUtils.toByteArray(contents));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deployBpr(String bprFileName, byte[] contents) {
		AesDeployBprType deployBprInput = new AesDeployBprType();
		AesStringResponseType deployBpr;
		try {
			deployBprInput.setBprFilename(bprFileName);
			deployBprInput.setBase64File(contents);
			deployBpr = activeBpelAdminPort.deployBpr(deployBprInput);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error while calling deployment service: " + e.getMessage(),
					e);
		}

		try {
			String responseMessage = deployBpr.getResponse();
			Document xml = parseXMLFromString(responseMessage);

			Node item = xml.getFirstChild().getAttributes()
					.getNamedItem(ATTRIBUTE_ERROR_COUNT);
			if (Integer.parseInt(item.getTextContent()) != 0) {
				throw new RuntimeException("Errors while deploying process: "
						+ responseMessage);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Document parseXMLFromString(String responseMessage)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(new ByteArrayInputStream(responseMessage
				.getBytes("UTF-8")));
	}

	public static void main(String[] args) throws FileNotFoundException {
		ActiveVOSAdministrativeFunctions t = new ActiveVOSAdministrativeFunctions(
				"http://localhost:8080/active-bpel/services/ActiveBpelDeployBPR",
				null, null);
		t.deployBpr(
				"Test",
				new FileInputStream(
						"C:\\Users\\dluebke\\workspaces\\egvt-mvn\\ch.terravis.egvt.process.egvt2\\deploy\\egvt-2.bpr"));
	}
}
