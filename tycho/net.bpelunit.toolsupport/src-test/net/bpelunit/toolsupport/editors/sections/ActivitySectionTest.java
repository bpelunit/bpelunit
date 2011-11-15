package net.bpelunit.toolsupport.editors.sections;

import static org.junit.Assert.assertEquals;
import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.toolsupport.editors.sections.ActivitySection.ActivityLabelProvider;

import org.junit.Test;


public class ActivitySectionTest {
	
	private ActivityLabelProvider lp = new ActivityLabelProvider();;

	private XMLTrack createTrack() {
		return XMLTrack.Factory.newInstance();
	}
	
	@Test
	public void testLabelProviderSendActivityEmptyOperation() throws Exception {
		XMLSendActivity xml = createTrack().addNewSendOnly();
		xml.setOperation(null);
		
		assertEquals("n/a (Send Asynchronous)", lp.getText(xml));
	}

	@Test
	public void testLabelProviderSendActivity() throws Exception {
		XMLSendActivity xml = createTrack().addNewSendOnly();
		xml.setOperation("myOperation");
		
		assertEquals("myOperation (Send Asynchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderReceiveActivityEmptyOperation() throws Exception {
		XMLReceiveActivity xml = createTrack().addNewReceiveOnly();
		xml.setOperation(null);
		
		assertEquals("n/a (Receive Asynchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderReceiveActivity() throws Exception {
		XMLReceiveActivity xml = createTrack().addNewReceiveOnly();
		xml.setOperation("myOperation");
		
		assertEquals("myOperation (Receive Asynchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderSendReceiveActivityEmptyOperation() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewSendReceive();
		xml.setOperation(null);
		
		assertEquals("n/a (Send/Receive Synchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderSendReceiveActivity() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewSendReceive();
		xml.setOperation("myOperation");
		
		assertEquals("myOperation (Send/Receive Synchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderSendReceiveAsyncActivityEmptyOperation() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewSendReceiveAsynchronous();
		xml.setOperation(null);
		
		assertEquals("n/a (Send/Receive Asynchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderSendReceiveAsyncActivity() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewSendReceiveAsynchronous();
		xml.setOperation("myOperation");
		
		assertEquals("myOperation (Send/Receive Asynchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderReceiveSendActivityEmptyOperation() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewReceiveSend();
		xml.setOperation(null);
		
		assertEquals("n/a (Receive/Send Synchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderReceiveSendActivity() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewReceiveSend();
		xml.setOperation("myOperation");
		
		assertEquals("myOperation (Receive/Send Synchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderReceiveSendAsyncActivityEmptyOperation() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewReceiveSendAsynchronous();
		xml.setOperation(null);
		
		assertEquals("n/a (Receive/Send Asynchronous)", lp.getText(xml));
	}
	
	@Test
	public void testLabelProviderReceiveSendAsyncActivity() throws Exception {
		XMLTwoWayActivity xml = createTrack().addNewReceiveSendAsynchronous();
		xml.setOperation("myOperation");
		
		assertEquals("myOperation (Receive/Send Asynchronous)", lp.getText(xml));
	}
}
