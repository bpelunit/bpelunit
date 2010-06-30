package org.bpelunit.framework.coverage.result.statistic;

import java.util.List;
import java.util.Set;
import org.bpelunit.framework.coverage.receiver.MarkerState;

/*
 * Repräsentiert Statistik, die die Anzahl der getesteten und gesamten
 * Codestücke beinhalten. Eine Statistik kann sich aus mehreren Statistiken
 * (Substatistiken) zusammensetzten.
 * 
 * @author Alex Salnikow
 * 
 */
/**
 * Holds statistics containing the total number of code segments (tested and
 * untested)
 * 
 * <br />A statistic can include severea sub statistics
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public interface IStatistic {

	/*
	 * 
	 * @return Anzahl aller Elemente
	 */
	/**
	 * Get total number of elements
	 * 
	 * @return number of elements
	 */
	public int getTotalNumber();

	/*
	 * 
	 * @return Substatistiken oder null.
	 */
	/**
	 * Get sub statistics.
	 * 
	 * <br />Returns null when no sub statistics are included at all
	 * 
	 * @return sub statistics or null
	 */
	public List<IStatistic> getSubStatistics();

	/*
	 * 
	 * @param statistic
	 */
	/**
	 * Add a sub statistic
	 * @param statistic
	 */
	public void addSubStatistic(IStatistic statistic);

	public String getName();

	/*
	 * 
	 * @param testcases
	 * @return Anzahl der Elemente, die mit testcase gestestet wurden
	 */
	/**
	 * Gets number of elements tested with test case
	 * @param test case
	 * @return number of elements tested
	 */
	public int getTestedNumber(String testcase);

	/*
	 * 
	 * @param testCases
	 *            eine Menge von Testcasenamen
	 * @return Anzahl der Elemente, die mit testCases gestestet wurden
	 */
	/**
	 * Gets number of elements tested with test cases
	 * @param set of test cases
	 * @return number of elements tested
	 */
	// TODO tested with all test cases or any of them?
	public int getTestedNumber(Set<String> testcases);

	/*
	 * 
	 * @return alle getesteten Elemente
	 */
	/**
	 * Gets total number of tested elements
	 * @return number
	 */
	public int getTestedNumber();

	/*
	 * @param statusListe
	 *            eine Menge von Marken, die mit einem Status behaftete sind
	 *            (getestet oder nicht) und eine Element des Kontrollflusses
	 *            repräsentieren.
	 */
	/**
	 * @param stateList
	 *            eine Menge von Marken, die mit einem Status behaftete sind
	 *            (getestet oder nicht) und eine Element des Kontrollflusses
	 *            repräsentieren.
	 */
	// TODO ... ???
	public void setStateList(List<MarkerState> stateList);

	/*
	 * 
	 * @param testCase
	 * @return getestete Marken
	 */
	/**
	 * Gets markers tested with test case 
	 * @param test case
	 * @return tested markers
	 */
	public Set<MarkerState> getTestedItems(String testcase);
}
