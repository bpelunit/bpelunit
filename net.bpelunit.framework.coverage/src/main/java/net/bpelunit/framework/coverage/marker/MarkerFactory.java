package net.bpelunit.framework.coverage.marker;


public class MarkerFactory {

	private static final String DEFAULT_MARKER_PREFIX = "COVERAGE_";
	private String markerPrefix;
	private int markerId = 0;
	
	public MarkerFactory() {
		this(DEFAULT_MARKER_PREFIX);
	}

	public MarkerFactory(String newMarkerPrefix) {
		this.markerPrefix = newMarkerPrefix;
	}
	
	public synchronized Marker createMarker() {
		markerId++;
		Marker marker = new Marker(markerPrefix + markerId);
		
		return marker;
	}
	
}
