package coverage.result;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class XMLCoverageResultProducer {

	private static final String STATISTIC_ELEMENT = "statistic";

	private static final String COVERAGE_STATISTIC_ELEMENT = "coverageStatistics";

	private static final String NAME_ATTRIBUT = "name";

	private static final String TOTAL_NUMBER_ATTRIBUT = "totalNimber";

	private static final String TESTED_NUMBER_ATTRIBUT = "testedNumber";

	private static final Namespace NAMESPACE = Namespace.getNamespace("http://www.bpelunit.org/schema/coverageResult");

	public static void writeResult(OutputStream out,List<IStatistic> statistics) throws IOException  {
		Document doc = new Document();

		Element coverageStatisticElement = new Element(
				COVERAGE_STATISTIC_ELEMENT, NAMESPACE);
		doc.setRootElement(coverageStatisticElement);
		for (Iterator<IStatistic> iter = statistics.iterator(); iter.hasNext();) {
			addStatisticElement(coverageStatisticElement, iter.next());
		}
		XMLOutputter xmlOutputter = new XMLOutputter(Format
				.getPrettyFormat());

		try {
			xmlOutputter.output(doc, out);
		} catch (IOException e) {
			throw e;
		}
		//TODO out schlieﬂen oder nicht
	}



	private static void addStatisticElement(Element element,
			IStatistic statistic) {
		Element statisticElement;
		statisticElement = new Element(STATISTIC_ELEMENT,NAMESPACE);
		statisticElement.setAttribute(NAME_ATTRIBUT, statistic.getName());
		statisticElement.setAttribute(TOTAL_NUMBER_ATTRIBUT, Integer
				.toString(statistic.getTotalNumber()));
		statisticElement.setAttribute(TESTED_NUMBER_ATTRIBUT, Integer
				.toString(statistic.getTestedNumber()));

		element.addContent(statisticElement);
		List<IStatistic> subStatistics = statistic.getSubStatistics();
		if (subStatistics != null && subStatistics.size() > 0) {
			for (Iterator<IStatistic> iter = subStatistics.iterator(); iter
					.hasNext();) {
				addStatisticElement(statisticElement, iter.next());
			}
		}
	}
}
