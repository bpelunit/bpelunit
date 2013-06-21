package net.bpelunit.framework.coverage.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.bpelunit.framework.coverage.CoverageConstants;
import net.bpelunit.framework.coverage.instrumentation.AbstractInstrumenter;
import net.bpelunit.framework.coverage.result.IMetricCoverage;
import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.ICatch;
import net.bpelunit.model.bpel.ICatchAll;
import net.bpelunit.model.bpel.ICompensate;
import net.bpelunit.model.bpel.ICompensateScope;
import net.bpelunit.model.bpel.ICompensationHandler;
import net.bpelunit.model.bpel.ICopy;
import net.bpelunit.model.bpel.IElse;
import net.bpelunit.model.bpel.IElseIf;
import net.bpelunit.model.bpel.IEmpty;
import net.bpelunit.model.bpel.IExit;
import net.bpelunit.model.bpel.IFlow;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IIf;
import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.ILink;
import net.bpelunit.model.bpel.IOnAlarm;
import net.bpelunit.model.bpel.IOnAlarmEventHandler;
import net.bpelunit.model.bpel.IOnMessage;
import net.bpelunit.model.bpel.IOnMessageHandler;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IPick;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IReceive;
import net.bpelunit.model.bpel.IRepeatUntil;
import net.bpelunit.model.bpel.IReply;
import net.bpelunit.model.bpel.IRethrow;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IThrow;
import net.bpelunit.model.bpel.IValidate;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IWait;
import net.bpelunit.model.bpel.IWhile;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MarkerServiceTest {

	private static final int TEST_PORT = 9999;
	private static final String CONTEXT = "ws";
	
	static class DummyInstrumenter extends AbstractInstrumenter {

		List<String> markers = new ArrayList<String>();

		public void visit(IAssign a) {
		}

		public void visit(ICompensate a) {
		}

		public void visit(ICompensateScope a) {
		}

		public void visit(ICopy c) {
		}

		public void visit(IEmpty a) {
		}

		public void visit(IExit a) {
		}

		public void visit(IFlow a) {
		}

		public void visit(IForEach a) {
		}

		public void visit(IIf a) {
		}

		public void visit(IImport i) {
		}

		public void visit(IInvoke a) {
		}

		public void visit(IOnAlarm a) {
		}

		public void visit(IOnMessage a) {
		}

		public void visit(IPartnerLink pl) {
		}

		public void visit(IPick a) {
		}

		public void visit(IProcess a) {
		}

		public void visit(IReceive a) {
		}

		public void visit(IRepeatUntil a) {
		}

		public void visit(IReply a) {
		}

		public void visit(IRethrow a) {
		}

		public void visit(IScope a) {
		}

		public void visit(ISequence a) {
		}

		public void visit(IThrow a) {
		}

		public void visit(IValidate a) {
		}

		public void visit(IVariable var) {
		}

		public void visit(IWait a) {
		}

		public void visit(IWhile a) {
		}

		public void visit(ICompensationHandler compensationHandler) {
		}

		@Override
		public String getMarkerPrefix() {
			return null;
		}

		@Override
		public void pushMarker(String markerName) {
			this.markers .add(markerName);
		}

		@Override
		public IMetricCoverage getCoverageResult() {
			return null;
		}

		public void visit(IOnMessageHandler onMessageHandler) {
		}

		public void visit(IElseIf elseIf) {
		}

		public void visit(IElse else1) {
		}

		public void visit(ILink link) {
		}

		public void visit(ICatch ccatch) {
		}

		public void visit(ICatchAll catchAll) {
		}

		public void visit(IOnAlarmEventHandler onAlarmEventHandler) {
		}

	}

	private static Server httpServer;
	private static DummyInstrumenter dummyInstrumenter = new DummyInstrumenter();

	@BeforeClass
	public static void setUpClass() throws Exception {
		
		httpServer = new Server(TEST_PORT);
		httpServer.setHandler(new MarkerService(Arrays.asList(dummyInstrumenter)));
		httpServer.start();
	}
	
	@Before
	public void setUp() {
		dummyInstrumenter.markers.clear();
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		httpServer.stop();
	}
	
	@Test
	public void testHandleWithCorrectConfiguration() throws Exception {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod();
		post.setURI(new URI("http://localhost:" + TEST_PORT + "/" + CONTEXT + "/" + CoverageConstants.COVERAGE_SERVICE_BPELUNIT_NAME, false));
		post.setRequestHeader("Content-Type", "text/xml");
		InputStream soapMsgStream = getClass().getResourceAsStream("mark.soap.xml");
		assertNotNull("mark.soap.xml exists in test resources", soapMsgStream);
		post.setRequestEntity(new InputStreamRequestEntity(soapMsgStream));
		client.executeMethod(post);
		
		assertEquals(3, dummyInstrumenter.markers.size());
		assertEquals("M1", dummyInstrumenter.markers.get(0));
		assertEquals("M2", dummyInstrumenter.markers.get(1));
		assertEquals("M3", dummyInstrumenter.markers.get(2));
	}
	
}
