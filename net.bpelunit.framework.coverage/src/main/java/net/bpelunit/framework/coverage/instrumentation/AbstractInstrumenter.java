package net.bpelunit.framework.coverage.instrumentation;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.framework.coverage.result.IMetricCoverage;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IVisitor;

public abstract class AbstractInstrumenter implements IVisitor  {

	public abstract String getMarkerPrefix();
	private MarkerFactory markerFactory;
	
	public void addCoverageMarkers(IProcess p) {
		markerFactory = new MarkerFactory(getMarkerPrefix() + "_" + p.getName() + "_");
		p.visit(this);
	}
	
	protected Marker addCoverageMarker(IActivity a) {
		Marker newMarker = markerFactory.createMarker();
		
		if(a.getDocumentation().size() == 0) {
			a.addDocumentation();
		}
		IDocumentation firstDocumentation = a.getDocumentation().get(0);
		List<Object> existingDoc = new ArrayList<Object>(firstDocumentation.getDocumentationElements()); 
		existingDoc.add(newMarker);
		firstDocumentation.setDocumentationElements(existingDoc);
		
		return newMarker;
	}

	public abstract void pushMarker(String markerName);

	public abstract IMetricCoverage getCoverageResult();

}
