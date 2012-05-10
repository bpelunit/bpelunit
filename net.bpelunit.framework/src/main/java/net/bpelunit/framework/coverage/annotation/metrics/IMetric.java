package net.bpelunit.framework.coverage.annotation.metrics;

import java.util.List;
import java.util.Map;

import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkerState;
import net.bpelunit.framework.coverage.result.statistic.IStatistic;

import org.jdom.Element;

/**
 * Interface representing a metric
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public interface IMetric {

	/**
	 * Get metric name
	 * 
	 * @return metric name
	 */
	String getName();

	/**
	 * Get characterizing prefix of a metric's markings
	 * 
	 * @return markings' prefix
	 */
	List<String> getMarkersId();

	/**
	 * Creates statistics after the test run.
	 * 
	 * @param allMarkers
	 *            list with all inserted markings (after test run)
	 * @return all created statistics
	 */
	IStatistic createStatistic(
			Map<String, Map<String, MarkerState>> allMarkers);

	/**
	 * Wants the (unmodified) description of the BPEL process as xml element.
	 * 
	 * <br />All process description elements will be saved (when needed for
	 * instrumentation)
	 * 
	 * @param process
	 *            unmodified BPEL process
	 */
	void setOriginalBPELProcess(Element process);

	/**
	 * Delegates instrumentation workload to own handler
	 * 
	 * @throws BpelException
	 */
	void insertMarkers() throws BpelException;
}
