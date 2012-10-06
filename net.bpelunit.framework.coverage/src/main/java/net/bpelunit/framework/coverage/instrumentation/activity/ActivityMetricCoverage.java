package net.bpelunit.framework.coverage.instrumentation.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.coverage.result.ICoverageResult;
import net.bpelunit.framework.coverage.result.IMetricCoverage;
import net.bpelunit.model.bpel.IActivity;

public class ActivityMetricCoverage implements IMetricCoverage {

	private List<ActivityCoverageResult> results = new ArrayList<ActivityCoverageResult>();
	private Map<String, Double> cummulatedCoveragePerActivityType = new HashMap<String, Double>();
	private Map<String, Integer> counterPerActivityType = new HashMap<String, Integer>();
	private Map<String, Integer> minPerActivityType = new HashMap<String, Integer>();
	private Map<String, Integer> maxPerActivityType = new HashMap<String, Integer>();
	private Map<String, Integer> totalActivitiesPerType = new HashMap<String, Integer>();
	
	public ActivityMetricCoverage(List<String> markers, Map<String, ?extends IActivity> markerMapping,
			Map<String, Integer> markerCounter) {

		createActivityEntries(markers, markerMapping, markerCounter);
		createActivityTypeEntries();
		
		createOverallEntry();
	}

	private void createOverallEntry() {
		int min = Integer.MAX_VALUE;
		for(Integer i : minPerActivityType.values()) {
			min = Math.min(min, i);
		}
		int max = 0;
		for(Integer i : maxPerActivityType.values()) {
			max = Math.max(max, i);
		}
		int total = 0;
		for(Integer i : counterPerActivityType.values()) {
			total += i;
		}
		int activityCount = 0;
		for(Integer i : totalActivitiesPerType.values()) {
			activityCount += i;
		}
		double coverage = 0.0;
		for(Double d : cummulatedCoveragePerActivityType.values()) {
			coverage += d;
		}
		coverage /= activityCount;
		results.add(new ActivityCoverageResult("Overall", min, max, (double)total / (double)activityCount, total, coverage));
	}

	private void createActivityTypeEntries() {
		List<String> activityTypes = new ArrayList<String>();
		activityTypes.addAll(totalActivitiesPerType.keySet());
		Collections.sort(activityTypes);
		for(String activityType : activityTypes) {
			results.add(
				new ActivityCoverageResult(
					"//" + activityType, 
					minPerActivityType.get(activityType), 
					maxPerActivityType.get(activityType), 
					(double)counterPerActivityType.get(activityType) / (double)totalActivitiesPerType.get(activityType), 
					counterPerActivityType.get(activityType),
					cummulatedCoveragePerActivityType.get(activityType) / (double)totalActivitiesPerType.get(activityType)
				)
			);
		}
	}

	private void createActivityEntries(List<String> markers,
			Map<String, ? extends IActivity> markerMapping,
			Map<String, Integer> markerCounter) {
		for(String marker : markers) {
			IActivity a = markerMapping.get(marker);
			int counter = markerCounter.get(marker);
			
			double coverage = Math.min(counter, 1.0);
			results.add(new ActivityCoverageResult(a.getXPathInDocument(), counter, counter, counter, counter, coverage));
			
			String activityName = a.getActivityName();
			if(!counterPerActivityType.containsKey(activityName)) {
				counterPerActivityType.put(activityName, counter);
				minPerActivityType.put(activityName, counter);
				maxPerActivityType.put(activityName, counter);
				totalActivitiesPerType.put(activityName, 1);
				cummulatedCoveragePerActivityType.put(activityName, coverage);
			} else {
				counterPerActivityType.put(activityName, counterPerActivityType.get(activityName) + counter);
				minPerActivityType.put(activityName, Math.min(minPerActivityType.get(activityName), counter));
				maxPerActivityType.put(activityName, Math.max(maxPerActivityType.get(activityName), counter));
				totalActivitiesPerType.put(activityName, totalActivitiesPerType.get(activityName) + 1);
				cummulatedCoveragePerActivityType.put(activityName, cummulatedCoveragePerActivityType.get(activityName) + coverage);
			}
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
