package org.bpelunit.framework.coverage.result.statistic;

import java.util.List;
import java.util.Set;

import org.bpelunit.framework.coverage.receiver.MarkerState;


/**
 * Repräsentiert Statistik, die die Anzahl der getesteten und gesamten
 * Codestücke beinhalten. Eine Statistik kann sich aus mehreren Statistiken
 * zusammensetzten.
 * 
 * @author Alex Salnikow
 * 
 */

public interface IStatistic {

	public int getTotalNumber();

	public List<IStatistic> getSubStatistics();

	public void addSubStatistik(IStatistic statistic);

	public String getName();
	public int getTestedNumber(String testcases);
	public int getTestedNumber(Set<String> testCases);

	public int getTestedNumber();

	public void setStatusListe(List<MarkerState> statusListe);

	public Set<MarkerState> getTestedItems(String testCase);
}
