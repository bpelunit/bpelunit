package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TEmpty;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TOnMessage;


public class OnMessageTest {

	private OnMessage onMessage;
	private TOnMessage nativeOnMessage;
	private Empty mainActivity;
	private TEmpty nativeMainActivity;
	
	@Before
	public void setUp() {
		BpelFactory f = new BpelFactory();
		nativeOnMessage = new TOnMessage();
		onMessage = new OnMessage(nativeOnMessage, f);
		
		nativeMainActivity = new TEmpty();
		mainActivity = new Empty(nativeMainActivity, f);
		
		onMessage.setMainActivity(mainActivity);
	}
	
	@Test
	public void testGetObjectForNativeObject() throws Exception {
		assertSame(onMessage, onMessage.getObjectForNativeObject(nativeOnMessage));
		assertSame(mainActivity, onMessage.getObjectForNativeObject(nativeMainActivity));
	}
	
}
