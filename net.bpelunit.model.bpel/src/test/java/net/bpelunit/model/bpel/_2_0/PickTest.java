package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TEmpty;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TOnAlarmPick;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TOnMessage;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TPick;


public class PickTest {
	private Pick pick;
	private TPick nativePick;
	private OnAlarm onAlarm;
	private TOnAlarmPick nativeOnAlarm;
	private Empty mainActivityOnMessage;
	private TEmpty nativeMainActivityOnMessage;
	private Empty mainActivityOnAlarm;
	private TEmpty nativeMainActivityOnAlarm;
	private OnMessage onMessage;
	private TOnMessage nativeOnMessage;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativePick = new TPick();
		pick = new Pick(nativePick, f);
		
		onMessage = (OnMessage)pick.addOnMessage();
		nativeOnMessage = onMessage.getNativeActivity();
		nativeMainActivityOnMessage = new TEmpty();
		mainActivityOnMessage = new Empty(nativeMainActivityOnMessage, f);
		onMessage.setMainActivity(mainActivityOnMessage);
		
		onAlarm = (OnAlarm)pick.addOnAlarm();
		nativeOnAlarm = onAlarm.getNativeActivity();
		nativeMainActivityOnAlarm = new TEmpty();
		mainActivityOnAlarm = new Empty(nativeMainActivityOnAlarm, f);
		onAlarm.setMainActivity(mainActivityOnAlarm);
	}
	
	@Test
	public void testAdds() throws Exception {
		assertNotNull(onMessage);
		assertNotNull(nativeOnMessage);
		assertEquals(1, pick.getOnMessages().size());
		
		assertNotNull(onAlarm);
		assertNotNull(nativeOnAlarm);
		assertEquals(1, pick.getOnAlarms().size());
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(pick, pick.getObjectForNativeObject(nativePick));
		assertSame(onAlarm, pick.getObjectForNativeObject(nativeOnAlarm));
		assertSame(onMessage, pick.getObjectForNativeObject(nativeOnMessage));
		
		assertSame(mainActivityOnMessage, onMessage.getObjectForNativeObject(nativeMainActivityOnMessage));
		assertSame(mainActivityOnMessage, pick.getObjectForNativeObject(nativeMainActivityOnMessage));
		
		assertSame(mainActivityOnAlarm, onAlarm.getObjectForNativeObject(nativeMainActivityOnAlarm));
		assertSame(mainActivityOnAlarm, pick.getObjectForNativeObject(nativeMainActivityOnAlarm));
	}
}
