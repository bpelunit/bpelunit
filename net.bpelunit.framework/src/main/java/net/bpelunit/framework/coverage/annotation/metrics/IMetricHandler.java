package net.bpelunit.framework.coverage.annotation.metrics;

import java.util.List;

import net.bpelunit.framework.coverage.exceptions.BpelException;
import org.jdom.Element;

/**
 * This interface is for handlers instrumenting the metrics
 * 
 * @author Alex, Ronald Becher
 */
public interface IMetricHandler {

	/**
	 * Inserts the markers at the correct place in a bpel process element.
	 * 
	 * <br />According to those markers the associated invoke calls will be
	 * generated, logging the processing of certain activities
	 * 
	 * @param processElements
	 * @throws BpelException
	 */
	void insertMarkersForMetric(List<Element> processElements)
			throws BpelException;

}
