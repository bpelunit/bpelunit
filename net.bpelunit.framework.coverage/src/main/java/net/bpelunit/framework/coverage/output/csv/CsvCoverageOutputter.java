package net.bpelunit.framework.coverage.output.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.bpelunit.framework.coverage.output.AbstractCoverageOutputter;
import net.bpelunit.framework.coverage.result.IBPELCoverage;
import net.bpelunit.framework.coverage.result.ICoverageResult;
import net.bpelunit.framework.coverage.result.IMetricCoverage;

import org.apache.commons.io.IOUtils;

public class CsvCoverageOutputter extends AbstractCoverageOutputter {

	private String separator = ",";
	
	@Override
	protected void exportCoverageInformation(IBPELCoverage c)
			throws IOException {

		for (IMetricCoverage metricCoverage : c.getMetricCoverages()) {
			String fileName = c.getProcessName().getLocalPart() + "." + metricCoverage.getMetricId() + ".csv";
			File csvFile = new File(getOutputDirectory(), fileName);

			Writer writer = new FileWriter(csvFile);

			try {
				writer.write(String.format("Element%sMin%sMax%sTotal%sAvg%sCoverage\n", separator, separator, separator, separator, separator));

				for (ICoverageResult result : metricCoverage
						.getCoverageResult()) {
					writer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s\n", 
							result.getBPELElementReference(),
							separator,
							result.min(),
							separator,
							result.max(),
							separator,
							result.getExecutionCount(),
							separator,
							result.avg(),
							separator,
							result.coverage()
						));
				}

			} finally {
				IOUtils.closeQuietly(writer);
			}
		}
	}
	
	@CoverageOutputOption(description="The separator used to delimit values")
	public void setSeperator(String newSeperator) {
		this.separator = newSeperator;
	}
}
