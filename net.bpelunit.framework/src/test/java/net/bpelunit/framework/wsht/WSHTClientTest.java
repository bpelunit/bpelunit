package net.bpelunit.framework.wsht;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class WSHTClientTest {

	private WSHTClient client;
	
	@Before
	public void setup() throws Exception {
		this.client = new WSHTClient(new URL("http://localhost:7777/ws/wsht"), "donald", "duck");
	}

	@After 
	public void tearDown() {
		this.client = null;
	}
	
	@Test
	public void testSetAuthorizationRealm() {
		assertEquals("ZG9uYWxkOmR1Y2s=", client.getAuthorizationRealm());
		
		client.setAuthorizationRealm("Mickey", "Mouse");
		assertEquals("TWlja2V5Ok1vdXNl", client.getAuthorizationRealm());

		// taken from http://en.wikipedia.org/wiki/Basic_access_authentication (2011-11-12)
		client.setAuthorizationRealm("Aladdin", "open sesame");
		assertEquals("QWxhZGRpbjpvcGVuIHNlc2FtZQ==", client.getAuthorizationRealm());
	}
	
	@Test
	public void testEmptyPasswordIsSameAsNullPassword() {
		client.setAuthorizationRealm("Donald", null);
		String ar1 = client.getAuthorizationRealm();
		
		client.setAuthorizationRealm("Donald", "");
		String ar2 = client.getAuthorizationRealm();
		
		assertEquals(ar1, ar2);
	}
}
