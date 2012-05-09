package net.bpelunit.framework.coverage.annotation.metrics;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.coverage.exceptions.BpelException;
import net.bpelunit.framework.coverage.receiver.MarkerState;
import net.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.jdom.Element;

/*
 * Die Schnittstelle repräsentiert die Metriken.
 * 
 * @author Alex Salnikow
 * 
 */
/**
 * Interface representing a metric
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public interface IMetric {

	/*
	 * 
	 * @return die Bezeichnung der Metrik
	 */
	/**
	 * Get metric name
	 * 
	 * @return metric name
	 */
	public String getName();

	/*
	 * Liefert Präfixe von allen Marken dieser Metrik. Sie ermöglichen die
	 * Zuordnung der empfangenen Marken einer Metrik
	 * 
	 * @return Präfixe von allen Marken dieser Metrik
	 */
	/**
	 * Get characterizing prefix of a metric's markings
	 * 
	 * @return markings' prefix
	 */
	public List<String> getMarkersId();

	/*
	 * Erzeugt Statistiken
	 * 
	 * @param allMarkers alle einegfügten Marken (von allen Metriken), nach dem
	 * Testen
	 * 
	 * @return Statistik
	 */
	/**
	 * Creates statistics after the test run.
	 * 
	 * @param allMarkers
	 *            list with all inserted markings (after test run)
	 * @return all created statistics
	 */
	public IStatistic createStatistic(
			Map<String, Map<String, MarkerState>> allMarkers);

	/*
	 * Erhält die noch nicht modifizierte Beschreibung des BPELProzesses als
	 * XML-Element. Alle für die Instrumentierung benötigten Elemente der
	 * Prozessbeschreibung werden gespeichert
	 * 
	 * @param process noch nicht modifiziertes BPEL-Prozess
	 */
	/**
	 * Wants the (unmodified) description of the BPEL process as xml element.
	 * 
	 * <br />All process description elements will be saved (when needed for
	 * instrumentation)
	 * 
	 * @param process
	 *            unmodified BPEL process
	 */
	public void setOriginalBPELProcess(Element process);

	/*
	 * delegiert die Instrumentierungsaufgabe an eigenen Handler
	 * 
	 * @throws BpelException
	 */
	/**
	 * Delegates instrumentation workload to own handler
	 * 
	 * @throws BpelException
	 */
	public void insertMarkers() throws BpelException;
}
