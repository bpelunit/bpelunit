package org.bpelunit.framework.coverage.result.statistic;

import java.util.List;
import java.util.Set;

import org.bpelunit.framework.coverage.receiver.MarkerStatus;

/**
 * Repräsentiert Statistik, die die Anzahl der getesteten und gesamten
 * Codestücke beinhalten. Eine Statistik kann sich aus mehreren Statistiken
 * (Substatistiken) zusammensetzten.
 * 
 * @author Alex Salnikow
 * 
 */

public interface IStatistic {

	/**
	 * 
	 * @return Anzahl aller Elemente
	 */
	public int getTotalNumber();

	/**
	 * 
	 * @return Substatistiken oder null.
	 */
	public List<IStatistic> getSubStatistics();

	/**
	 * 
	 * @param statistic
	 */
	public void addSubStatistik(IStatistic statistic);

	public String getName();

	/**
	 * 
	 * @param testcases
	 * @return Anzahl der Elemente, die mit testcase gestestet wurden
	 */
	public int getTestedNumber(String testcases);

	/**
	 * 
	 * @param testCases
	 *            eine Menge von Testcasenamen
	 * @return Anzahl der Elemente, die mit testCases gestestet wurden
	 */
	public int getTestedNumber(Set<String> testCases);

	/**
	 * 
	 * @return alle getesteten Elemente
	 */
	public int getTestedNumber();

	/**
	 * @param statusListe
	 *            eine Menge von Marken, die mit einem Status behaftete sind
	 *            (getestet oder nicht) und eine Element des Kontrollflusses
	 *            repräsentieren.
	 */
	public void setStatusListe(List<MarkerStatus> statusListe);

	/**
	 * 
	 * @param testCase
	 * @return getestete Marken
	 */
	public Set<MarkerStatus> getTestedItems(String testCase);
}
