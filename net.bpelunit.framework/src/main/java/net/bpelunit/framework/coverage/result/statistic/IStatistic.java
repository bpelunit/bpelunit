package net.bpelunit.framework.coverage.result.statistic;

import java.util.List;
import java.util.Set;
import net.bpelunit.framework.coverage.receiver.MarkerState;

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

	/**
	 * Get total number of elements
	 * 
	 * @return number of elements
	 */
	int getTotalNumber();

	/**
	 * Get sub statistics.
	 * 
	 * <br />Returns null when no sub statistics are included at all
	 * 
	 * @return sub statistics or null
	 */
	List<IStatistic> getSubStatistics();

	/**
	 * Add a sub statistic
	 * @param statistic
	 */
	void addSubStatistic(IStatistic statistic);

	String getName();

	/**
	 * Gets number of elements tested with test case
	 * @param test case
	 * @return number of elements tested
	 */
	int getTestedNumber(String testcase);

	/**
	 * Gets number of elements tested with test cases
	 * @param set of test cases
	 * @return number of elements tested
	 */
	// TODO tested with all test cases or any of them?
	int getTestedNumber(Set<String> testcases);

	/**
	 * Gets total number of tested elements
	 * @return number
	 */
	int getTestedNumber();

	/**
	 * @param stateList
	 *            eine Menge von Marken, die mit einem Status behaftete sind
	 *            (getestet oder nicht) und eine Element des Kontrollflusses
	 *            repräsentieren.
	 */
	// TODO ... ???
	void setStateList(List<MarkerState> stateList);

	/**
	 * Gets markers tested with test case 
	 * @param test case
	 * @return tested markers
	 */
	Set<MarkerState> getTestedItems(String testcase);
}
