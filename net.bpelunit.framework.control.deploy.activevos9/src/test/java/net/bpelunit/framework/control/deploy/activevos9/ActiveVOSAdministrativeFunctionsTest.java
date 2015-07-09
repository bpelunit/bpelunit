/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.activevos9;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.junit.Test;

import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;


public class ActiveVOSAdministrativeFunctionsTest {

	@Test
	public void testExtractContributionIds() throws Exception {
		ActiveVOSAdministrativeFunctions activevos = new ActiveVOSAdministrativeFunctions("", null, null);
		
		List<AesContribution> contributions = new ArrayList<AesContribution>();
		contributions.add(new AesContribution());
		contributions.get(0).setId(2);
		contributions.add(new AesContribution());
		contributions.get(1).setId(5);
		
		List<Integer> ids = activevos.extractContributionIds(contributions);
		
		assertEquals(2, ids.size());
		assertEquals(2, (int)ids.get(0));
		assertEquals(5, (int)ids.get(1));
	}
	
	@Test
	public void testCorrectPortTypeInitialization() throws Exception {
		ActiveVOSAdministrativeFunctions activevos = new ActiveVOSAdministrativeFunctions("http://localhost:8081/active-bpel/services", "user", "pwd");
		
		BindingProvider ap = (BindingProvider)activevos.getActiveBpelAdminPort();
		BindingProvider cp = (BindingProvider)activevos.getContributionManagementPort();
		
		assertEquals("user", ap.getRequestContext().get(BindingProvider.USERNAME_PROPERTY));
		assertEquals("pwd", ap.getRequestContext().get(BindingProvider.PASSWORD_PROPERTY));
		assertEquals("http://localhost:8081/active-bpel/services/ActiveBpelAdmin", ap.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		
		assertEquals("user", cp.getRequestContext().get(BindingProvider.USERNAME_PROPERTY));
		assertEquals("pwd", cp.getRequestContext().get(BindingProvider.PASSWORD_PROPERTY));
		assertEquals("http://localhost:8081/active-bpel/services/AeContributionManagement", cp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));

		
		activevos = new ActiveVOSAdministrativeFunctions("http://localhost:8081/active-bpel/services/ActiveBpelDeployBPR", "user", "pwd");
		ap = (BindingProvider)activevos.getActiveBpelAdminPort();
		cp = (BindingProvider)activevos.getContributionManagementPort();
		
		assertEquals("user", ap.getRequestContext().get(BindingProvider.USERNAME_PROPERTY));
		assertEquals("pwd", ap.getRequestContext().get(BindingProvider.PASSWORD_PROPERTY));
		assertEquals("http://localhost:8081/active-bpel/services/ActiveBpelAdmin", ap.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		
		assertEquals("user", cp.getRequestContext().get(BindingProvider.USERNAME_PROPERTY));
		assertEquals("pwd", cp.getRequestContext().get(BindingProvider.PASSWORD_PROPERTY));
		assertEquals("http://localhost:8081/active-bpel/services/AeContributionManagement", cp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
	}
}
