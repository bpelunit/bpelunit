package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.InputStream;
import java.util.List;

import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IProcess;

import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;


public class ProcessTest {

	@Test
	public void testQueryByXPath() throws Exception {
		InputStream bpelResource = getClass().getResourceAsStream("waitprocess.bpel");
		assertNotNull(bpelResource);
		Process process = (Process)BpelFactory.loadProcess(bpelResource);
		
		List<IBpelObject> waitToLeave;
		
		Sequence s = (Sequence)process.getMainActivity();
		Pick p = (Pick)s.getActivities().get(1);

		OnAlarm a = (OnAlarm)p.getOnAlarms().get(1);
		Scope scope = (Scope)a.getMainActivity();
		Sequence s2 = (Sequence)scope.getMainActivity();
		Wait w = (Wait)s2.getActivities().get(0);
		
		TWait nativeWait = w.getNativeActivity();
		assertSame(nativeWait, w.getNativeActivity());
		assertSame(w, s2.getObjectForNativeObject(nativeWait));
		assertSame(w, scope.getObjectForNativeObject(nativeWait));
		assertSame(w, a.getObjectForNativeObject(nativeWait));
		assertSame(w, p.getObjectForNativeObject(nativeWait));
		assertSame(w, s.getObjectForNativeObject(nativeWait));
		assertSame(w, process.getObjectForNativeObject(nativeWait));
		
//		waitToLeave = process.getElementsByXPath("//*[@name='WaitToLeave']");
//		assertEquals(1, waitToLeave.size());
//		assertSame(w, waitToLeave.get(0));
	}
	
}
