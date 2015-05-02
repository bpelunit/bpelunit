/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.activevos9;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.bpelunit.framework.control.deploy.activevos9.ActiveVOSAdministrativeFunctionsMock.MethodCall;
import net.bpelunit.framework.control.ext.ExtensionRegistry;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.util.FileUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.active_endpoints.schemas.activebpeladmin._2007._01.activebpeladmin.AesDeployBprType;

public class ActiveVOS9DeployerTest {


	private static final String BPR_FILENAME = "src/test/resources/net/bpelunit/framework/control/deploy/activevos9/bpelunit-tc1.bpr";


	private ActiveVOS9Deployer deployer;

	// can be null because it is not used by the current implementation
	private ProcessUnderTest processUnderTest = null;



	private ActiveVOSAdministrativeFunctionsMock administrativeFunctionMock;


	@Before
	public void setUp() {
		this.deployer = new ActiveVOS9Deployer();
		this.administrativeFunctionMock = new ActiveVOSAdministrativeFunctionsMock("", "", "");
		deployer.setAdministrativeFunctions(administrativeFunctionMock);
	}

	@After
	public void tearDown() {
		this.deployer = null;
		this.administrativeFunctionMock = null;
	}

	@Test
	public void testDefaultValues() throws Exception {
		assertEquals("bpelunit", deployer.getDeployerUserName());
		assertEquals(deployer.getDeployerUserName(), getDefaultValueFor("DeployerUserName"));
		
		assertEquals("", deployer.getDeployerPassword());
		assertEquals(deployer.getDeployerPassword(), getDefaultValueFor("DeployerPassword"));
		
		assertEquals("", deployer.getDeploymentLocation());
		assertEquals(deployer.getDeploymentLocation(), getDefaultValueFor("DeploymentLocation"));
		
		assertEquals("http://localhost:8080/active-bpel/services/ActiveBpelAdmin", deployer.getDeploymentServiceEndpoint());
		assertEquals(deployer.getDeploymentServiceEndpoint(), getDefaultValueFor("DeploymentServiceEndpoint"));
	}
	
	@Test(expected = DeploymentException.class)
	public void testDeploymentFailsIfNoArchiveIsSpecified() throws Exception {
		deployer.deploy(".", processUnderTest);

		assertEquals(0, administrativeFunctionMock.getMethodsCalls().size());
	}

	@Test
	public void testSimpleDeployWithoutAnyOptions() throws Exception {
		configureDeployer("DeploymentLocation", BPR_FILENAME);

		deployer.deploy(".", processUnderTest);

		assertEquals(1, administrativeFunctionMock.getMethodsCalls().size());
		MethodCall methodCall = administrativeFunctionMock.getMethodsCalls().get(0);
		assertDeployBprUnchangedArchive(methodCall);
	}

	@Test
	public void testDeployWithUndeploy() throws Exception {
		configureDeployer(
				"DeploymentLocation", BPR_FILENAME,
				"DoUndeploy", "true"
		);

		deployer.setDeploymentServiceEndpoint("http://localhost:8081/active-bpel/services/ActiveBpelDeployBPR");
		
		deployer.deploy(".", processUnderTest);

		assertEquals(1, administrativeFunctionMock.getMethodsCalls().size());
		MethodCall methodCall = administrativeFunctionMock.getMethodsCalls().get(0);
		assertDeployBprUnchangedArchive(methodCall);
	}

	private void configureDeployer(String... paramValues) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		for (int i = 0; i < paramValues.length; i += 2) {
			parameterMap.put(paramValues[i], paramValues[i + 1]);
		}
		ExtensionRegistry.configureDeployer(deployer, parameterMap);
	}

	private void assertDeployBprUnchangedArchive(MethodCall methodCall)
			throws IOException {
		assertEquals("deployBpr", methodCall.methodName);
		byte[] bprContents = FileUtil.readFile(new File(BPR_FILENAME));
		AesDeployBprType aesDeployBprType = (AesDeployBprType) methodCall.parameters[0];
		assertEquals(javax.xml.bind.DatatypeConverter.printBase64Binary(bprContents), aesDeployBprType.getBase64File());
		assertEquals("bpelunit-tc1.bpr", aesDeployBprType.getBprFilename());
	}


	private String getDefaultValueFor(String optionName) {
		return ExtensionRegistry.getDefaultValueFor(ActiveVOS9Deployer.class, optionName);
	}

}
