package net.bpelunit.utils.bpelstats;

public interface BpelStatsGatherer {

	public abstract BpelStatsFileResult gather(String bpelFileName) throws Exception;
	
}
