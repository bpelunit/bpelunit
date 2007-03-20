package coverage.result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Statistic implements IStatistic {

	private int totalNumber;

	private int testedNumber;

	private String name;

	private List<IStatistic> subStatistics = null;

	public Statistic(String string) {
		this.name = string;
	}

	public Statistic(int totalNumber, int testedNamber, String name) {
		this.totalNumber = totalNumber;
		this.testedNumber = testedNamber;
		this.name = name;
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
	 * Wenn die Statistik sich aus weiteren Statistiken zusammensetzt, dann setzt
	 * sich das Ergebnis aus den Daten der Unterstatistiken zusammen.
	 */
	public int getTestedNumber() {
		int testedNumber = this.testedNumber;
		if (subStatistics != null) {
			IStatistic statistic;
			for (Iterator<IStatistic> iter = subStatistics.iterator(); iter
					.hasNext();) {
				statistic = iter.next();
				testedNumber = testedNumber + statistic.getTestedNumber();
			}
		}
		return testedNumber;
	}

	/**
	 * Wenn die Statistik sich aus weiteren Statistiken zusammensetzt, dann setzt
	 * sich das Ergebnis aus den Daten der Unterstatistiken zusammen.
	 */
	public int getTotalNumber() {
		int totalNumber = this.totalNumber;
		if (subStatistics != null) {
			IStatistic statistic;
			for (Iterator<IStatistic> iter = subStatistics.iterator(); iter
					.hasNext();) {
				statistic = iter.next();
				totalNumber = totalNumber + statistic.getTotalNumber();
			}
		}
		return totalNumber;
	}

	@Override
	public String toString() {
		String result = "Statistic: " + name + "\n";
		result = result + "    Gesamtzahl: " + getTotalNumber() + "\n";
		result = result + "    Getestetzahl: " + getTestedNumber() + "\n";
		if (subStatistics != null) {
			Iterator<IStatistic> iter = subStatistics.iterator();
			while (iter.hasNext()) {
				result = result + "\n" + iter.next().toString();
			}
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
