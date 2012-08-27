package net.bpelunit.bpel;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import net.bpelunit.bpel.BpelFactory;

import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TProcess;


public class BpelFactoryTest {

	@Test
	public void testLoadProcess() throws Exception {
		InputStream in = getClass().getResourceAsStream("/TEST.bpel");
		IProcess p = BpelFactory.loadProcess(in);
		assertEquals("TEST", p.getName());
		assertEquals("TESTNS", p.getTargetNamespace());
	}
}
