package org.bpelunit.framework.coverage.result;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

/**
 * Die Klasse schreibt die Testabdeckungsmetriken im XML-Format in Outputstream.
 * 
 * @author Alex
 * 
 */
public class XMLCoverageResultProducer {

	private static final String STATISTIC_ELEMENT = "statistic";

	private static final String COVERAGE_STATISTIC_ELEMENT = "testingCoverage";

	private static final String COVERAGE_FILE_STATISTIC_ELEMENT = "FileStatistics";

	private static final String COVERAGE_ARCHIV_STATISTIC_ELEMENT = "ArchivStatistics";

	private static final String NAME_ATTRIBUT = "name";

	private static final String TOTAL_NUMBER_ATTRIBUT = "totalItems";

	private static final String TESTED_NUMBER_ATTRIBUT = "testedItems";

	private static final String PER_CENT_ATTRIBUT = "perCent";

	private static final Namespace NAMESPACE = Namespace
			.getNamespace("http://www.bpelunit.org/schema/coverageResult");

	/**
	 * 
	 * Schreibt Testabdeckungsmetriken im XML-Format in OutputStream.
	 * 
	 * @param out Outputstream in den die Statistiken geschrieben werden.
	 * @param statistics
	 * @param info
	 * @param detailed
	 *            entscheidet, ob eine detaillierte Augabe erzeugt wird
	 *            (Statistik für jede einzelen Datei), oder nicht (Statistik für
	 *            den kompletten Archive. )
	 * @throws IOException
	 */
	public static void writeResult(OutputStream out,
			List<IFileStatistic> statistics, List<String> info, boolean detailed)
			throws IOException {
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
		if (detailed) {
			for (Iterator<IFileStatistic> iter = statistics.iterator(); iter
					.hasNext();) {
				addFileStatisticElement(coverageStatisticElement, iter.next());
			}

		} else {
			addArchiveStatistic(coverageStatisticElement, statistics);
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

	private static void addArchiveStatistic(Element coverageStatisticElement,
			List<IFileStatistic> statistics) {
		if (statistics.size() > 0) {
			Element archiveStatisticElement = new Element(
					COVERAGE_ARCHIV_STATISTIC_ELEMENT, NAMESPACE);
			coverageStatisticElement.addContent(archiveStatisticElement);
			String entities[];
			ArrayList<IStatistic> totalStatistics;
			IFileStatistic fileStatistic = statistics.get(0);
			for (Iterator<IStatistic> iter = fileStatistic.getStatistics()
					.iterator(); iter.hasNext();) {
				totalStatistics = new ArrayList<IStatistic>();
				IStatistic statistic1 = iter.next();
				totalStatistics.add(statistic1);
				for (int i = 1; i < statistics.size(); i++) {
					IFileStatistic fileStatistic2 = statistics.get(i);
					IStatistic statistic2 = fileStatistic2
							.getStatistic(statistic1.getName());
					totalStatistics.add(statistic2);
				}
				Element statisticElement = new Element(STATISTIC_ELEMENT,
						NAMESPACE);
				entities = mergeMetric(totalStatistics, statistic1.getName());
				statisticElement.setAttribute(NAME_ATTRIBUT, entities[0]);
				statisticElement.setAttribute(TOTAL_NUMBER_ATTRIBUT,
						entities[1]);
				statisticElement.setAttribute(TESTED_NUMBER_ATTRIBUT,
						entities[2]);
				statisticElement.setAttribute(PER_CENT_ATTRIBUT, entities[3]);
				archiveStatisticElement.addContent(statisticElement);
			}
		}

	}

	private static String[] mergeMetric(java.util.List<IStatistic> statistics,
			String statisticName) {
		int tested = 0;
		int total = 0;
		String relativ = "-";
		for (Iterator<IStatistic> iter = statistics.iterator(); iter.hasNext();) {
			IStatistic statistic = iter.next();
			total = total + statistic.getTotalNumber();
			tested = tested + statistic.getTestedNumber();
		}
		if (total > 0)
			relativ = Float.toString((tested * 1000 / total) / (float) 10.0)
					+ "%";
		return new String[] { statisticName, Integer.toString(total),
				Integer.toString(tested), relativ };
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
