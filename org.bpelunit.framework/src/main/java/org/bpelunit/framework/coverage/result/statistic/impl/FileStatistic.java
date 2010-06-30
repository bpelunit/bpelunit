package org.bpelunit.framework.coverage.result.statistic.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;

/**
 * Straight implementation of {@link IFileStatistic}
 * 
 * @author Alex Salnikow, Ronald Becher
 */

public class FileStatistic implements IFileStatistic {

	private String bpelFileName;

	private TreeMap<String, IStatistic> statistics;

	public FileStatistic(String bpelFileName) {
		this.bpelFileName = bpelFileName;
		statistics = new TreeMap<String, IStatistic>();
	}

	public String getBPELFilename() {
		return bpelFileName;
	}

	public Collection<IStatistic> getStatistics() {
		return statistics.values();
	}

	public void setStatistics(List<IStatistic> statistics) {
		for (Iterator<IStatistic> iter = statistics.iterator(); iter.hasNext();) {
			IStatistic statistic = iter.next();
			this.statistics.put(statistic.getName(), statistic);
			List<IStatistic> subStatistics = statistic.getSubStatistics();
			if(subStatistics!=null){
				for (Iterator<IStatistic> iterator = subStatistics.iterator(); iterator
						.hasNext();) {
					IStatistic subStatistic=iterator.next();
					this.statistics.put(subStatistic.getName(), subStatistic);
				}
			}
			
		}
	}

	public IStatistic getStatistic(String name) {
		return statistics.get(name);
	}

}
