/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 */
package net.bpelunit.framework.control.deploy.activevos9;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.active_endpoints.schemas.engineapi._2010._05.engineapitypes.AesContribution;


public class ActiveVOSAdministrativeFunctionsTest {

	@Test
	public void testExtractContributionIds() throws Exception {
		ActiveVOSAdministrativeFunctions activevos = new ActiveVOSAdministrativeFunctions(null, null, null);
		
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
	
}
