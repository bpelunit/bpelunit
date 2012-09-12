package net.bpelunit.bpel;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IProcess;

import org.junit.Test;


public class BpelFactoryTest {

	@Test
	public void testLoadProcess() throws Exception {
		InputStream in = getClass().getResourceAsStream("/TEST.bpel");
		IProcess p = BpelFactory.loadProcess(in);
		assertEquals("TEST", p.getName());
		assertEquals("TESTNS", p.getTargetNamespace());
	}
}
