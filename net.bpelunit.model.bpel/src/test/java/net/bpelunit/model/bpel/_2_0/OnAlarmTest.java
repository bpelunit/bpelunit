package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TEmpty;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TOnAlarmPick;


public class OnAlarmTest {

	private OnAlarm onAlarm;
	private TOnAlarmPick nativeOnAlarm;
	private Empty mainActivity;
	private TEmpty nativeMainActivity;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativeOnAlarm = new TOnAlarmPick();
		onAlarm = new OnAlarm(nativeOnAlarm, f);
		
		nativeMainActivity = new TEmpty();
		mainActivity = new Empty(nativeMainActivity, f);
		
		onAlarm.setMainActivity(mainActivity);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(onAlarm, onAlarm.getObjectForNativeObject(nativeOnAlarm));
		assertSame(mainActivity, onAlarm.getObjectForNativeObject(nativeMainActivity));
	}
}
