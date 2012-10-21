package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.*;

import javax.xml.namespace.NamespaceContext;

import org.junit.Test;

public class BpelFactoryTest {

	@Test
	public void testCreateNamespaceContext() throws Exception {
		NamespaceContext ctx = BpelFactory.INSTANCE.createNamespaceContext();
		
		String bpelNamespace = BpelFactory.INSTANCE.getNamespace();
		assertNotNull(bpelNamespace);
		assertEquals(bpelNamespace, ctx.getNamespaceURI("bpel"));
		assertEquals("bpel", ctx.getPrefix(bpelNamespace));
	}
	
}
