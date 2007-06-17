package org.bpelunit.framework.coverage.annotation.metrics.fhcoverage;

import static org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.metrics.IMetricHandler;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.jdom.Comment;
import org.jdom.Element;




public class FaultMetricHandler implements  IMetricHandler {

	public static final String METRIC_NAME = "FaultHandlerMetric";

	public static final String FAULT_HANDLER_LABEL = "catchBlock";

	private static int count=0;
	/**
	 * Generiert eine eindeutige Markierung.
	 * 
	 * @return eindeutige Markierung
	 */
	public static String getAndRegisterNextLabel() {
		String label = FAULT_HANDLER_LABEL + "_" + (count++);

//		LabelsRegistry.getInstance().addMarker(label);
		return  label;
	}

	private LabelsRegistry markersRegistry;
	
	public FaultMetricHandler(LabelsRegistry markersRegistry){

		this.markersRegistry=markersRegistry;
	}

	public String getName() {
		return METRIC_NAME;
	}

	public void insertMarkersForMetric(List<Element> activities)
			throws BpelException {

			Iterator<Element> iter = activities.iterator();
			while (iter.hasNext()) {
				insertCoverageLabelsForCatchBlock(iter.next());
			}
		

	}

	private void insertCoverageLabelsForCatchBlock(Element catchBlock) {
		Element child = getFirstEnclosedActivity(catchBlock);
		if (!isSequence(child)) {
			child = ensureElementIsInSequence(child);
		}
		String label=getAndRegisterNextLabel();
		markersRegistry.addMarker(label);
		Comment comment = new Comment(Instrumenter.COVERAGE_LABEL_IDENTIFIER +label);
		child.addContent(0, comment);
	}

//	public List<String> getPrefix4CovLabeles() {
//		List<String> list = new ArrayList<String>();
//		list.add(FaultMetricHandler.FAULT_HANDLER_LABEL);
//		return list;
//	}

}
