package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IWait;

import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TWait;


public class ProcessTest {

	@Test
	public void testQueryByXPath() throws Exception {
		IProcess process = BpelFactory.loadProcess(getClass().getResourceAsStream("/waitprocess.bpel"));
		
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
		
		
		waitToLeave = process.getElementsByXPath("//*[name='WaitToLeave']");
		assertEquals(1, waitToLeave.size());
		assertTrue(waitToLeave.get(0) instanceof IWait);
		assertSame(w, waitToLeave.get(0));
	}
	
}
