package net.bpelunit.framework.coverage.result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoverageDocument implements ICoverageDocument {

	private File suite;
	private List<IBPELCoverage> coverageInformation = new ArrayList<IBPELCoverage>();

	public CoverageDocument(File suite) {
		super();
		this.suite = suite;
	}

	public File getExecutedSuite() {
		return this.suite;
	}

	public List<IBPELCoverage> getCoverageInformationForProcesses() {
		return this.coverageInformation;
	}

}
