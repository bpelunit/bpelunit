package net.bpelunit.framework.coverage.instrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.util.XMLUtil;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class MarkerToActivityConverterTest {

	private IProcess p;
	private IActivity activity;
	private final MarkerFactory mf = new MarkerFactory("TEST");
	private MarkerToActivityConverter markerToActivityConverter;
	private Marker marker1;
	private Marker marker2;
	private Marker marker3;
	private Element markerMsg;
	
	@Before
	public void setUp() {
		p = BpelFactory.createProcess();
		activity = p.setNewEmpty();
		activity.setName("ActivityToInstrument");
		
		markerToActivityConverter = new MarkerToActivityConverter();
		Document xmlDoc = XMLUtil.createDocument();
		markerMsg = xmlDoc.createElement("e");
		xmlDoc.appendChild(markerMsg);
		
		marker1 = mf.createMarker();
		marker2 = mf.createMarker();
		marker3 = mf.createMarker();
	}
	
	@Test
	public void testCreateRequestForMarkersNoMarkers() throws Exception {
		markerToActivityConverter.buildCoverageMarkerMessage(markerMsg, activity);
		assertEquals(0, markerMsg.getChildNodes().getLength());
	}
	
	@Test
	public void testCreateRequestForMarkersThreeMarkers() throws Exception {
		addThreeMarkersToActivity();
		
		assertEquals(3, activity.getDocumentation().get(0).getDocumentationElements().size());
		
		markerToActivityConverter.buildCoverageMarkerMessage(markerMsg, activity);
		NodeList children = markerMsg.getChildNodes();
		assertEquals(3, children.getLength());
		assertEquals(marker1.getName(), children.item(0).getTextContent());
		assertEquals(marker2.getName(), children.item(1).getTextContent());
		assertEquals(marker3.getName(), children.item(2).getTextContent());
	}

	private void addThreeMarkersToActivity() {
		AbstractInstrumenter.addCoverageMarker(activity, marker1);
		AbstractInstrumenter.addCoverageMarker(activity, marker2);
		AbstractInstrumenter.addCoverageMarker(activity, marker3);
	}
	
	@Test
	public void testCreateScopeForMarkers() throws Exception {
		addThreeMarkersToActivity();
		
		markerToActivityConverter.createScopeForMarkers(activity);
		
		IScope scope = (IScope)p.getMainActivity();
		assertEquals("__BPELUNIT_MARK_REQUEST__", scope.getVariables().get(0).getName());
		
		ISequence seq = (ISequence)scope.getMainActivity();
		assertTrue(seq.getActivities().get(0) instanceof IAssign);
		assertTrue(seq.getActivities().get(1) instanceof IInvoke);
		assertSame(activity, seq.getActivities().get(2));
		
//		ByteArrayOutputStream actualXMLAsStream = new ByteArrayOutputStream();
//		p.save(actualXMLAsStream);
//		System.out.println(new String(actualXMLAsStream.toByteArray()));
	}
}
