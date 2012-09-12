package net.bpelunit.framework.coverage.result;

import java.io.File;
import java.util.List;

public interface ICoverageDocument {

	File getExecutedSuite();
	
	List<?extends IBPELCoverage> getCoverageInformationForProcesses();
	
}
