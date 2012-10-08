package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;


public class OnMessageTest {

	private OnMessage onMessage;
	private TOnMessage nativeOnMessage;
	private Empty mainActivity;
	private TEmpty nativeMainActivity;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativeOnMessage = TOnMessage.Factory.newInstance();
		onMessage = new OnMessage(nativeOnMessage, f);
		
		nativeMainActivity = TEmpty.Factory.newInstance();
		mainActivity = new Empty(nativeMainActivity, f);
		
		onMessage.setMainActivity(mainActivity);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(onMessage, onMessage.getObjectForNativeObject(nativeOnMessage));
		assertSame(mainActivity, onMessage.getObjectForNativeObject(nativeMainActivity));
	}
	
}
