package net.bpelunit.framework.coverage.result.statistic;

import java.util.Collection;
import java.util.List;

/*
 * Repräsentiert Statistik einer BPEL-Datei.
 * 
 * @author Alex Salnikow
 * 
 */
/**
 * Represents BPEL file statistics
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public interface IFileStatistic {
	
	String getBPELFilename();

	Collection<IStatistic> getStatistics();

	void setStatistics(List<IStatistic> statistics);
	
	IStatistic getStatistic(String name);

}
