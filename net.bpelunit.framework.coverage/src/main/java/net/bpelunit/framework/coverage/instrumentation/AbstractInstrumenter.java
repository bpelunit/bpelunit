package net.bpelunit.framework.coverage.instrumentation;

import javax.xml.namespace.QName;

import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.marker.MarkerFactory;
import net.bpelunit.framework.coverage.result.IMetricCoverage;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IVisitor;
import net.bpelunit.util.XMLUtil;

import org.w3c.dom.Element;

public abstract class AbstractInstrumenter implements IVisitor  {

	static final QName QNAME_MARKER = new QName("http://www.bpelunit.net/instrumentation", "marker");
	private MarkerFactory markerFactory;

	public abstract String getMarkerPrefix();
	
	public void addCoverageMarkers(IProcess p) {
		markerFactory = new MarkerFactory(getMarkerPrefix() + "_" + p.getName() + "_");
		p.visit(this);
	}
	
	protected Marker addCoverageMarker(IActivity a) {
		Marker newMarker = markerFactory.createMarker();
		
		addCoverageMarker(a, newMarker);
		
		return newMarker;
	}

	/**
	 * Extracted so that MarkerToActivityConverterTest can easily add
	 * markers to activities during test.
	 * 
	 * @param a Activity to which marker m should be added
	 * @param m Marker to be added
	 */
	static void addCoverageMarker(IActivity a, Marker m) {
		if(a.getDocumentation().size() == 0) {
			a.addDocumentation();
		}
		IDocumentation firstDocumentation = a.getDocumentation().get(0);
		Element e = firstDocumentation.addDocumentationElement(QNAME_MARKER);
		XMLUtil.appendTextNode(e, m.getName());
	}
	
	/**
	 * Called when the framework detects the execution of a certain marker.
	 * Might be called for markers that don't belong to this metric.
	 * 
	 * @param markerName
	 */
	public abstract void pushMarker(String markerName);

	public abstract IMetricCoverage getCoverageResult();

}
