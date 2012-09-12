package net.bpelunit.framework.coverage.instrumentation;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.services.marker.Mark;

import org.junit.Before;
import org.junit.Test;


public class MarkerToActivityConverterTest {

	private IProcess p;
	private IActivity activity;
	private final MarkerFactory mf = new MarkerFactory("TEST");
	private MarkerToActivityConverter markerToActivityConverter;
	private Marker marker1;
	private Marker marker2;
	private Marker marker3;
	
	@Before
	public void setUp() {
		p = BpelFactory.createProcess();
		activity = p.getFactory().createEmpty();
		activity.setName("ActivityToInstrument");
		activity.addDocumentation();
		
		markerToActivityConverter = new MarkerToActivityConverter();
		
		marker1 = mf.createMarker();
		marker2 = mf.createMarker();
		marker3 = mf.createMarker();
	}
	
	@Test
	public void testCreateRequestForMarkersNoMarkers() throws Exception {
		Mark request = markerToActivityConverter.createRequestForMarkers(activity);
		assertEquals(0, request.getMarker().size());		
	}
	
	@Test
	public void testCreateRequestForMarkersThreeMarkers() throws Exception {
		addThreeMarkersToActivity();
		
		Mark request = markerToActivityConverter.createRequestForMarkers(activity);
		assertEquals(3, request.getMarker().size());
		assertEquals(marker1.getName(), request.getMarker().get(0));
		assertEquals(marker2.getName(), request.getMarker().get(1));
		assertEquals(marker3.getName(), request.getMarker().get(2));
	}

	private void addThreeMarkersToActivity() {
		List<Object> documentationElements = new ArrayList<Object>();
		documentationElements.add("Some String");
		documentationElements.add(marker1);
		documentationElements.add(marker2);
		documentationElements.add("Some other String");
		documentationElements.add(marker3);
		documentationElements.add("Final String");
		
		activity.getDocumentation().get(0).setDocumentationElements(documentationElements);
	}
	
	@Test
	public void testCreateScopeForMarkers() throws Exception {
		addThreeMarkersToActivity();
		
		IScope scope = markerToActivityConverter.createScopeForMarkers(activity);
		p.setMainActivity(scope);
		
		ByteArrayOutputStream actualXMLAsStream = new ByteArrayOutputStream();
		p.save(actualXMLAsStream);
		System.out.println(new String(actualXMLAsStream.toByteArray()));
	}
}
