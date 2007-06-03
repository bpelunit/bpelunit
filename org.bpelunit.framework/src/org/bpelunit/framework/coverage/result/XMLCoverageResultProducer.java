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

	private static final String COVERAGE_STATISTIC_ELEMENT = "coverageStatistics";

	private static final String TEST_CASE_ELEMENT = "testCase";

	private static final String COVERAGE_FILE_STATISTIC_ELEMENT = "FileStatistics";

	private static final String NAME_ATTRIBUT = "name";

	private static final String TOTAL_NUMBER_ATTRIBUT = "totalNumber";

	private static final String TESTED_NUMBER_ATTRIBUT = "testedNumber";

	private static final Namespace NAMESPACE = Namespace
			.getNamespace("http://www.bpelunit.org/schema/coverageResult");

	public static void writeResult(OutputStream out,
			List<IFileStatistic> statistics, List<String> testCases, List<String> info) throws IOException {
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
		logger.info("FILESTATISTIK WIRD EINGEFÜGT");
		for (Iterator<IFileStatistic> iter = statistics.iterator(); iter
				.hasNext();) {
			addFileStatisticElement(coverageStatisticElement, iter.next(),testCases);
			logger.info("EINE FILESTATISTIK WURD EINGEFÜGT");
		}
		logger.info("XMLFILE WIRD GESCHRIEBEN");
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
			IFileStatistic fileStatistic, List<String> testCases) {
		Logger logger = Logger.getLogger("XMLOutput");
		Element fileStatisticElement = new Element(
				COVERAGE_FILE_STATISTIC_ELEMENT, NAMESPACE);
		fileStatisticElement.setAttribute("filename", fileStatistic
				.getBPELFilename());
		parentElement.addContent(fileStatisticElement);

		logger.info("STATISTIKEN WERDEN EINGEFÜGT");
		for (Iterator<String> iter = testCases.iterator(); iter.hasNext();) {
			String testCase=iter.next();
			addTestCaseElement2(fileStatisticElement,testCase,fileStatistic);
		}
//		for (Iterator<IStatistic> iter = fileStatistic.getStatistics()
//				.iterator(); iter.hasNext();) {
//			IStatistic statistic = iter.next();
//
//			addTestCaseElements(fileStatisticElement, statistic, testCases);
//		}
	}

	private static void addTestCaseElement2(Element fileStatisticElement, String testCase, IFileStatistic fileStatistic) {

		Element testCaseElement = new Element(TEST_CASE_ELEMENT, NAMESPACE);
		testCaseElement.setAttribute(NAME_ATTRIBUT, testCase);
		fileStatisticElement.addContent(testCaseElement);
		for (Iterator<IStatistic> iter = fileStatistic.getStatistics()
				.iterator(); iter.hasNext();) {
			IStatistic statistic = iter.next();
			addStatisticElement2(testCase,testCaseElement,statistic);
		}
		
	}

	private static void addStatisticElement2(String testCase, Element testCaseElement, IStatistic statistic) {
		Element statisticElement;
		statisticElement = new Element(STATISTIC_ELEMENT, NAMESPACE);
		statisticElement.setAttribute(NAME_ATTRIBUT, statistic.getName());
		statisticElement.setAttribute(TOTAL_NUMBER_ATTRIBUT, Integer
				.toString(statistic.getTotalNumber()));
		statisticElement.setAttribute(TESTED_NUMBER_ATTRIBUT, Integer
				.toString(statistic.getTestedNumber(testCase)));

		testCaseElement.addContent(statisticElement);
		List<IStatistic> subStatistics = statistic.getSubStatistics();

		if (subStatistics != null && subStatistics.size() > 0) {
			for (Iterator<IStatistic> iter = subStatistics.iterator(); iter
					.hasNext();) {
				addStatisticElement(statisticElement, iter.next(), testCase);
			}
		}
		
	}

	private static void addTestCaseElements(Element fileStatisticElement,
			IStatistic statistic, List<String> testCases) {

		Logger logger = Logger.getLogger("XMLOutput");
		Element testCaseElement;
		String testCase;
		for (Iterator<String> iter = testCases.iterator(); iter.hasNext();) {
			logger.info("TESTCASES WERDEN EINGEFÜGT");
			testCase = iter.next();
			logger.info("TESTCASES WERDEN EINGEFÜGT "+testCase);
			testCaseElement = new Element(TEST_CASE_ELEMENT, NAMESPACE);
			testCaseElement.setAttribute(NAME_ATTRIBUT, testCase);
			fileStatisticElement.addContent(testCaseElement);
			addStatisticElement(testCaseElement, statistic, testCase);
		}
	}

	private static void addStatisticElement(Element element,
			IStatistic statistic, String testCase) {
		Logger logger = Logger.getLogger("XMLOutput");
		Element statisticElement;
		statisticElement = new Element(STATISTIC_ELEMENT, NAMESPACE);
		statisticElement.setAttribute(NAME_ATTRIBUT, statistic.getName());
		statisticElement.setAttribute(TOTAL_NUMBER_ATTRIBUT, Integer
				.toString(statistic.getTotalNumber()));
		statisticElement.setAttribute(TESTED_NUMBER_ATTRIBUT, Integer
				.toString(statistic.getTestedNumber(testCase)));

		element.addContent(statisticElement);
		List<IStatistic> subStatistics = statistic.getSubStatistics();

		logger.info("SUBSTATISTIKEN WIRD EINGEFÜGT");
		if (subStatistics != null && subStatistics.size() > 0) {
			for (Iterator<IStatistic> iter = subStatistics.iterator(); iter
					.hasNext();) {
				addStatisticElement(statisticElement, iter.next(), testCase);
			}
		}
	}
}
