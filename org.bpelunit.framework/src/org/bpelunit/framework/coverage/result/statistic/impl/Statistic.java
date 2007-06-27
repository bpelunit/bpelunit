package org.bpelunit.framework.coverage.result.statistic.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bpelunit.framework.coverage.receiver.MarkerStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;

/**
 * Repräsentiert Statistik, die die Anzahl der getesteten und gesamten
 * Codestücke beinhalten. Eine Statistik kann sich aus mehreren Statistiken
 * (Substatistiken) zusammensetzten.
 * 
 * @author Alex Salnikow
 * 
 */
public class Statistic implements IStatistic {

	private int totalNumber = 0;

	private int testedNumber = 0;

	private String name;

	private List<IStatistic> subStatistics = null;

	private List<MarkerStatus> statusListe = null;

	public Statistic(String string) {
		this.name = string;
	}

	public List<IStatistic> getSubStatistics() {
		return subStatistics;
	}

	public void addSubStatistik(IStatistic statistic) {
		if (subStatistics == null)
			subStatistics = new ArrayList<IStatistic>();
		subStatistics.add(statistic);
	}

	/**
	 * Wenn die Statistik sich aus weiteren Statistiken zusammensetzt, dann
	 * setzt sich das Ergebnis aus den Daten der Substatistiken zusammen.
	 */
	public int getTotalNumber() {
		int number = 0;
		if (subStatistics == null) {
			number = totalNumber;
		} else {
			for (Iterator<IStatistic> iter = subStatistics.iterator(); iter
					.hasNext();) {
				number = number + iter.next().getTotalNumber();
			}
		}
		return number;
	}

	public String getName() {
		return name;
	}

	public Set<MarkerStatus> getTestedItems(String testCase) {
		Set<MarkerStatus> set = new HashSet<MarkerStatus>();
		if (subStatistics == null) {
			for (Iterator<MarkerStatus> iter = statusListe.iterator(); iter
					.hasNext();) {
				MarkerStatus status = iter.next();
				if (status.isTestedWithTestcase(testCase))
					set.add(status);
			}
		} else {
			for (Iterator<IStatistic> iterator = subStatistics.iterator(); iterator
					.hasNext();) {
				set.addAll(iterator.next().getTestedItems(testCase));
			}
		}
		return set;
	}

	public int getTestedNumber(String testCase) {
		return getTestedItems(testCase).size();
	}

	public int getTestedNumber(Set<String> testCases) {
		Set<MarkerStatus> set = new HashSet<MarkerStatus>();
		for (Iterator<String> iter = testCases.iterator(); iter.hasNext();) {
			Set<MarkerStatus> items = getTestedItems(iter.next());
			set.addAll(items);
		}
		return set.size();
	}

	public int getTestedNumber() {
		int number = 0;
		if (subStatistics == null && statusListe != null) {
			for (Iterator<MarkerStatus> iter = statusListe.iterator(); iter
					.hasNext();) {
				if (iter.next().isTested())
					number++;		
			}
		} else {
			for (Iterator<IStatistic> iter = subStatistics.iterator(); iter
					.hasNext();) {
				number = number + iter.next().getTestedNumber();
			}
		}
		return number;
	}

	public void setStatusListe(List<MarkerStatus> statusListe) {
		totalNumber = statusListe.size();
		this.statusListe = statusListe;

	}

}
