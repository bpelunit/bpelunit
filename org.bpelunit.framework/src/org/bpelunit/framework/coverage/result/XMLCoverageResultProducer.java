package org.bpelunit.framework.coverage.result;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class XMLCoverageResultProducer {

	private static final String STATISTIC_ELEMENT = "statistic";

	private static final String COVERAGE_STATISTIC_ELEMENT = "testingCoverage";

	private static final String COVERAGE_FILE_STATISTIC_ELEMENT = "FileStatistics";

	private static final String NAME_ATTRIBUT = "name";

	private static final String TOTAL_NUMBER_ATTRIBUT = "totalItems";

	private static final String TESTED_NUMBER_ATTRIBUT = "testedItems";

	private static final Namespace NAMESPACE = Namespace
			.getNamespace("http://www.bpelunit.org/schema/coverageResult");

	public static void writeResult(OutputStream out,
			List<IFileStatistic> statistics, List<String> info) throws IOException {
		Logger logger = Logger.getLogger("XMLOutput");
		Document doc = new Document();
		Element coverageStatisticElement = new Element(
				COVERAGE_STATISTIC_ELEMENT, NAMESPACE);
		doc.setRootElement(coverageStatisticElement);
		logger.info("INFO WIRD EINGEFÜGT");
		if (info != null) {
			Element infoElement;
			for (Iterator<String> iter = info.iterator(); iter.hasNext();) {
				infoElement = new Element("info", NAMESPACE);
				infoElement.setText(iter.next());
				coverageStatisticElement.addContent(infoElement);
			}
		}
		for (Iterator<IFileStatistic> iter = statistics.iterator(); iter
				.hasNext();) {
			addFileStatisticElement(coverageStatisticElement, iter.next());
		}
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutputter.output(doc, out);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	private static void addFileStatisticElement(Element parentElement,
			IFileStatistic fileStatistic) {
		Element fileStatisticElement = new Element(
				COVERAGE_FILE_STATISTIC_ELEMENT, NAMESPACE);
		fileStatisticElement.setAttribute("filename", fileStatistic
				.getBPELFilename());
		parentElement.addContent(fileStatisticElement);
		for (Iterator<IStatistic> iter = fileStatistic.getStatistics()
				.iterator(); iter.hasNext();) {
			IStatistic statistic = iter.next();
			Element statisticElement;
			statisticElement = new Element(STATISTIC_ELEMENT, NAMESPACE);
			statisticElement.setAttribute(NAME_ATTRIBUT, statistic.getName());
			statisticElement.setAttribute(TOTAL_NUMBER_ATTRIBUT, Integer
					.toString(statistic.getTotalNumber()));
			statisticElement.setAttribute(TESTED_NUMBER_ATTRIBUT, Integer
					.toString(statistic.getTestedNumber()));
			fileStatisticElement.addContent(statisticElement);
		}
	}



}
