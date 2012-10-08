package net.bpelunit.framework.coverage.output.html;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.bpelunit.framework.coverage.output.AbstractCoverageOutputter;
import net.bpelunit.framework.coverage.result.IBPELCoverage;
import net.bpelunit.framework.coverage.result.ICoverageResult;
import net.bpelunit.framework.coverage.result.IMetricCoverage;

import org.apache.commons.io.IOUtils;

public class HtmlCoverageOutputter extends AbstractCoverageOutputter {

	private String stylesheet;

	@Override
	protected void exportCoverageInformation(IBPELCoverage c)
			throws IOException {
		String htmlFileName = c.getProcessName().getLocalPart() + ".html";
		File htmlFile = new File(getOutputDirectory(), htmlFileName);

		HtmlWriter writer = new HtmlWriter(new FileWriter(htmlFile));

		try {
			writer.startHead()
					.title("Coverage Report for: "
							+ c.getProcessName().getLocalPart())
					.stylesheet(stylesheet)
				.endHead();
			writer.startBody();

			writer.h1("Coverage Report for: "
					+ c.getProcessName().getLocalPart());

			for (IMetricCoverage metricCoverage : c.getMetricCoverages()) {
				writer.h2(metricCoverage.getMetricName());

				writer.startTable();
				writer.startTr()
						.th("Element")
						.th("Min")
						.th("Max")
						.th("Total")
						.th("Avg")
						.th("Coverage")
					.endTr();
				for (ICoverageResult result : metricCoverage
						.getCoverageResult()) {
					writer.startTr()
							.td(result.getBPELElementReference())
							.td(result.min()).td(result.max())
							.td(result.getExecutionCount()).td(result.avg()).td(result.coverage())
						.endTr();
				}
				writer.endTable();
			}

			writer.endBody();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	@CoverageOutputOption
	public void setStylesheet(String stylesheetHref) {
		this.stylesheet = stylesheetHref;
	}

}
