package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.PickDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;


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
		PickDocument pickDoc = PickDocument.Factory.newInstance();
		nativePick = pickDoc.addNewPick();
		pick = new Pick(nativePick, null);
		
		onMessage = pick.addOnMessage();
		nativeOnMessage = onMessage.getNativeActivity();
		
		mainActivityOnMessage = onMessage.setNewEmpty();
		nativeMainActivityOnMessage = mainActivityOnMessage.getNativeActivity();
		
		onAlarm = pick.addOnAlarm();
		nativeOnAlarm = onAlarm.getNativeActivity();
		mainActivityOnAlarm = onAlarm.setNewEmpty();
		nativeMainActivityOnAlarm = mainActivityOnAlarm.getNativeActivity();
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
	
	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Pick, pick.getActivityType());
	}
}
