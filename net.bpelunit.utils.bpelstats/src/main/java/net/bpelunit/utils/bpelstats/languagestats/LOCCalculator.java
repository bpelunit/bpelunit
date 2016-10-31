package net.bpelunit.utils.bpelstats.languagestats;


public class LOCCalculator {

	public static int calculateLOC(String source) {
		if(source == null) return 0;
		
		String[] lines = source.split("\n");
		int locCount = 0;
		for(String line : lines) {
			line = line.replaceAll("\r", "");
			if(!"".equals(line.trim())) {
				locCount++;
			}
		}
		return locCount;
	}
	
}
