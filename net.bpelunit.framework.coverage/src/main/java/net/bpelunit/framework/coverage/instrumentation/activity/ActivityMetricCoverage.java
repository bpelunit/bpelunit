package net.bpelunit.framework.coverage.instrumentation.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.coverage.result.ICoverageResult;
import net.bpelunit.framework.coverage.result.IMetricCoverage;
import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.IActivity;

public class ActivityMetricCoverage implements IMetricCoverage {

	private List<ActivityCoverageResult> results = new ArrayList<ActivityCoverageResult>();
	private List<ActivityCoverageResult> basicActivityResults = new ArrayList<ActivityCoverageResult>();

	public ActivityMetricCoverage(List<String> markers,
			Map<String, ? extends IActivity> markerMapping,
			Map<String, Integer> markerCounter) {

		createActivityEntries(markers, markerMapping, markerCounter);
		createActivityTypeEntries();

		createOverallEntry();
	}

	private void createOverallEntry() {
		double min = Integer.MAX_VALUE;
		double max = 0;
		int total = 0;
		int executedCount = 0;
		for (ActivityCoverageResult a : basicActivityResults) {
			min = Math.min(min, a.min());
			max = Math.max(max, a.max());
			total += a.getExecutionCount();
			if (a.getExecutionCount() > 0) {
				executedCount++;
			}
		}
		int activityCount = basicActivityResults.size();
		double coverage = (double) executedCount / (double) activityCount;
		results.add(new ActivityCoverageResult("Overall", min, max,
				(double) total / (double) activityCount, total, coverage, null));
	}

	private void createActivityTypeEntries() {
		for (ActivityType activityType : ActivityType.values()) {
			createActivityTypeEntry(activityType);
		}
	}

	private void createActivityTypeEntry(ActivityType activityType) {
		double min = Integer.MAX_VALUE;
		double max = 0;
		int total = 0;
		int executedCount = 0;
		int activityCount = 0;
		for (ActivityCoverageResult a : basicActivityResults) {
			if (a.getActivity().getActivityType().equals(activityType)) {
				min = Math.min(min, a.min());
				max = Math.max(max, a.max());
				total += a.getExecutionCount();
				activityCount++;
				if (a.getExecutionCount() > 0) {
					executedCount++;
				}
			}
		}
		double coverage = (double) executedCount / (double) activityCount;
		if (activityCount != 0) {
			results.add(new ActivityCoverageResult("//"
					+ activityType.name().toLowerCase(), min, max,
					(double) total / (double) activityCount, total, coverage,
					null));
		}
	}

	private void createActivityEntries(List<String> markers,
			Map<String, ? extends IActivity> markerMapping,
			Map<String, Integer> markerCounter) {
		for (String marker : markers) {
			IActivity a = markerMapping.get(marker);
			int counter = markerCounter.get(marker);

			double coverage = Math.min(counter, 1.0);
			ActivityCoverageResult activityCoverageResult = new ActivityCoverageResult(
					a.getXPathInDocument(), counter, counter, counter, counter,
					coverage, a);
			results.add(activityCoverageResult);
			basicActivityResults.add(activityCoverageResult);
		}
	}

	public String getMetricName() {
		return "Activity Coverage";
	}

	public String getMetricId() {
		return "ACTIVITY";
	}

	public List<ICoverageResult> getCoverageResult() {
		return new ArrayList<ICoverageResult>(results);
	}

}
