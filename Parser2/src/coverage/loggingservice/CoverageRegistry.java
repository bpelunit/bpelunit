package coverage.loggingservice;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;

public class CoverageRegistry {

	private static CoverageRegistry instance = null;

	private Hashtable allMetricsTable;

	private Logger logger;

	public static CoverageRegistry getInstance() {

		if (instance == null) {
			instance = new CoverageRegistry();
		}
		return instance;
	}

	public void initialize() {
		allMetricsTable = new Hashtable();
	}

	private CoverageRegistry() {
		logger = Logger.getLogger(getClass());
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

		allMetricsTable.put(BranchMetric.LINK_LABEL, new Hashtable());
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
		logger.info("!_!_!_!_!__-------!!!-11-1-1-!_!" + string + "testCase="
				+ testCase);
		String prefix = string.substring(0, string.indexOf('_'));
		MarkerStatus status = (MarkerStatus) ((Hashtable) allMetricsTable
				.get(prefix)).get(string);
		status.setStatus(true, testCase);
	}

	public IStatistic getStatistic(String metricName) {
		IStatistic statistic = null;
		if (metricName.equals(Statementmetric.METRIC_NAME)) {
			statistic = getStatementmetricResults();
		} else if (metricName.equals(BranchMetric.METRIC_NAME)) {
			statistic = getBranchmetricResults();
		}
		return statistic;
	}
	
	public List<IStatistic> getStatistics(){
		List<IStatistic> statistics=new ArrayList<IStatistic>();
		statistics.add(getStatementmetricResults());
		statistics.add(getBranchmetricResults());
		return statistics;
	}

	private IStatistic getBranchmetricResults() {
		IStatistic statistic = new Statistic(BranchMetric.BRANCH_LABEL);

		int[] numbers = getNumbers(BranchMetric.BRANCH_LABEL);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BranchMetric.BRANCH_LABEL));
		}
		numbers = getNumbers(BranchMetric.LINK_LABEL);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BranchMetric.LINK_LABEL));
		}
		return statistic;
	}

	private IStatistic getStatementmetricResults() {
		IStatistic statistic = new Statistic(Statementmetric.METRIC_NAME);
		int[] numbers = getNumbers(BasisActivity.EMPTY_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.EMPTY_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.ASSIGN_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.ASSIGN_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.COMPENSATE_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.COMPENSATE_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.COMPENSATESCOPE_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.COMPENSATESCOPE_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.EXIT_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.EXIT_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.INVOKE_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.INVOKE_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.RECEIVE_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.RECEIVE_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.REPLY_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.REPLY_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.RETHROW_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.RETHROW_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.THROW_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.THROW_ACTIVITY));
		}
		numbers = getNumbers(BasisActivity.WAIT_ACTIVITY);
		if (numbers != null) {
			statistic.addSubStatistik(new Statistic(numbers[0], numbers[1],
					BasisActivity.WAIT_ACTIVITY));
		}
		return statistic;
	}

	private int[] getNumbers(String activity) {
		int totalNumber;
		int testedNumber = 0;
		int[] numbers = null;
		if (allMetricsTable.get(activity) != null) {
			Hashtable activityTable = (Hashtable) allMetricsTable.get(activity);
			totalNumber = activityTable.size();
			Enumeration e = activityTable.elements();
			MarkerStatus status;
			while (e.hasMoreElements()) {
				status = (MarkerStatus) e.nextElement();
				if (status.isTested()) {
					testedNumber++;
				}
			}
			numbers = new int[] { totalNumber, testedNumber };
		}
		return numbers;
	}

}
