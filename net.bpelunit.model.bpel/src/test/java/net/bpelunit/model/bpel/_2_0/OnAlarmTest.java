package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;


public class OnAlarmTest {

	private OnAlarm onAlarm;
	private TOnAlarmPick nativeOnAlarm;
	private Empty mainActivity;
	private TEmpty nativeMainActivity;
	
	@Before
	public void setUp() {
		nativeOnAlarm = TOnAlarmPick.Factory.newInstance();
		onAlarm = new OnAlarm(nativeOnAlarm, null);
		
		mainActivity = onAlarm.setNewEmpty();
		nativeMainActivity = mainActivity.getNativeActivity();
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(onAlarm, onAlarm.getObjectForNativeObject(nativeOnAlarm));
		assertSame(mainActivity, onAlarm.getObjectForNativeObject(nativeMainActivity));
	}
}
