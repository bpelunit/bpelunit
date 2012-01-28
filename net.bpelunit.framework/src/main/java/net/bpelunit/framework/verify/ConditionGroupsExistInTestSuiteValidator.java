package net.bpelunit.framework.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLCompleteHumanTaskActivity;
import net.bpelunit.framework.xml.suite.XMLConditionGroup;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public class ConditionGroupsExistInTestSuiteValidator implements
		ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite)
			throws SpecificationException {
		
		Set<String> conditionGroupNames = extractConditionGroups(
				suite.getTestSuite()).keySet();
		
		Set<String> referencedConditionGroupNames = extractUsedConditionGroupNames(suite.getTestSuite());
		
		for(String cgName : referencedConditionGroupNames) {
			if(!conditionGroupNames.contains(cgName)) {
				throw new SpecificationException("Condition Group referenced but not defined: " + cgName);
			}
		}
	}

	private Map<String, XMLConditionGroup> extractConditionGroups(
			XMLTestSuite suite) {
		Map<String, XMLConditionGroup> cgs = new HashMap<String, XMLConditionGroup>();

		if (suite.getConditionGroups() != null
				&& suite.getConditionGroups().getConditionGroupList() != null) {
			for (XMLConditionGroup cg : suite.getConditionGroups()
					.getConditionGroupList()) {
				cgs.put(cg.getName(), cg);
			}
		}

		return cgs;
	}

	private Set<String> extractUsedConditionGroupNames(XMLTestSuite suite) {
		Set<String> retval = extractionConditionGroupNamesUsedInTestCases(suite);
		retval.addAll(extractConditionGroupNamesUsedInCondtionGroupInheritance(suite));

		return retval;
	}

	private Set<String> extractConditionGroupNamesUsedInCondtionGroupInheritance(XMLTestSuite suite) {
		Set<String> retval = new HashSet<String>();
		if (suite.getConditionGroups() != null) {
			for (XMLConditionGroup cg : suite.getConditionGroups().getConditionGroupList()) {
				if (cg.getInheritFrom() != null) {
					retval.add(cg.getInheritFrom());
				}
			}
		}
		return retval;
	}

	private Set<String> extractionConditionGroupNamesUsedInTestCases(
			XMLTestSuite suite) {
		Set<String> retval = new HashSet<String>();

		List<XMLHumanPartnerTrack> humanPartnerTracks = extractHumanTracks(suite);

		for (XMLHumanPartnerTrack pt : humanPartnerTracks) {
			for (XMLCompleteHumanTaskActivity a : pt.getCompleteHumanTaskList()) {
				retval.addAll(a.getConditionGroupList());
			}
		}

		return retval;
	}

	private List<XMLHumanPartnerTrack> extractHumanTracks(XMLTestSuite suite) {
		List<XMLHumanPartnerTrack> retval = new ArrayList<XMLHumanPartnerTrack>();

		for (XMLTestCase tc : suite.getTestCases().getTestCaseList()) {
			retval.addAll(tc.getHumanPartnerTrackList());
		}

		return retval;
	}
}
