package coverage.loggingservice;

import java.util.Iterator;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;

public class CoverageRegestry {

	private static CoverageRegestry instance = null;

	private Hashtable allMetricsTable;

	private Logger logger;

	public static CoverageRegestry getInstance() {

		if (instance == null) {
			instance = new CoverageRegestry();
		}
		return instance;
	}

	private CoverageRegestry() {
		logger = Logger.getLogger(getClass());
		allMetricsTable = new Hashtable();
	}

	public void addMetric(IMetric metric) {
		if (metric instanceof Statementmetric) {
			createDataStructure((Statementmetric) metric);
		} else if (metric instanceof BranchMetric) {
			createDataStructure((BranchMetric) metric);
		}
	}

	private void createDataStructure(BranchMetric metric) {
		allMetricsTable.put(BranchMetric.BRANCH_LABEL, new Hashtable());
	}

	private void createDataStructure(Statementmetric statementmetric) {
		for (Iterator<String> iter = statementmetric.getBasisActivities()
				.iterator(); iter.hasNext();) {
			String name = iter.next();
			System.out.println("!!!!--" + name);
			allMetricsTable.put(name, new Hashtable());
		}

	}

	public void addMarker(String marker) {
		logger.info("---Es wird auf Marker " + marker + " registriert.");
		String prefix = marker.substring(0, marker.indexOf('_'));
		((Hashtable) allMetricsTable.get(prefix)).put(marker,
				new MarkerStatus());
	}

	public void setCoverageStatusForAllMarker(String marker, String testCase) {
		logger.info("---------!!!!!!--------" + marker);
		Scanner scanner = new Scanner(marker);
		scanner.useDelimiter(MetricHandler.MARKER_SEPARATOR);
		String marke;
		while (scanner.hasNext()) {
			marke = scanner.next().trim();
			if (marke.length() > 0)
				setCoverageStatusForMarker(marke, testCase);
		}

	}

	private void setCoverageStatusForMarker(String string, String testCase) {
		logger.info("!_!_!_!_!__-------!!!-11-1-1-!_!" + string+"testCase="+testCase);
		String prefix = string.substring(0, string.indexOf('_'));
		MarkerStatus status = (MarkerStatus) ((Hashtable) allMetricsTable
				.get(prefix)).get(string);
		status.setStatus(true, testCase);
	}

}
