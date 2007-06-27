package org.bpelunit.framework.coverage.result.statistic;

import java.util.Collection;
import java.util.List;

/**
 * Repräsentiert Statistik einer BPEL-Datei.
 * 
 * @author Alex Salnikow
 * 
 */
public interface IFileStatistic {
	
	public String getBPELFilename();

	public Collection<IStatistic> getStatistics();

	public void setStatistics(List<IStatistic> statistics);
	
	public IStatistic getStatistic(String name);

}
