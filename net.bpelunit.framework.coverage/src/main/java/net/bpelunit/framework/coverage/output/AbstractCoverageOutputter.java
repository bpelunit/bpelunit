package net.bpelunit.framework.coverage.output;

import java.io.File;
import java.io.IOException;

import net.bpelunit.framework.coverage.result.IBPELCoverage;
import net.bpelunit.framework.coverage.result.ICoverageDocument;

public abstract class AbstractCoverageOutputter implements ICoverageOutputter {


	private File outputDirectory = null;

	protected final File getOutputDirectory() {
		return outputDirectory;
	}

	@CoverageOutputOption
	public final void setOutputDirectory(String directory) {
		outputDirectory = new File(directory);
	}
	
	public final void exportCoverageInformation(ICoverageDocument doc) throws IOException {
		if(getOutputDirectory() == null) {
			outputDirectory = doc.getExecutedSuite().getParentFile();
		}
		
		startExportCoverageInformation(doc);
		
		for(IBPELCoverage c : doc.getCoverageInformationForProcesses()) {
			exportCoverageInformation(c);
		}
		
		finishExportCoverageInformation(doc);
	}

	protected void startExportCoverageInformation(ICoverageDocument doc) {
	}

	protected void finishExportCoverageInformation(ICoverageDocument doc) {
	}
	
	protected abstract void exportCoverageInformation(IBPELCoverage c) throws IOException;

}
