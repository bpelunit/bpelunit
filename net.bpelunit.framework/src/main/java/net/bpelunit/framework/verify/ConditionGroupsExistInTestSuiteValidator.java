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
import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;

public class ConditionGroupsExistInTestSuiteValidator implements
		ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite)
			throws SpecificationException {

		Set<String> conditionGroupNames = extractConditionGroups(
				suite.getTestSuite()).keySet();

		Set<String> referencedConditionGroupNames = extractUsedConditionGroupNames(suite
				.getTestSuite());

		for (String cgName : referencedConditionGroupNames) {
			if (!conditionGroupNames.contains(cgName)) {
				throw new SpecificationException(
						"Condition Group referenced but not defined: " + cgName);
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
		Set<String> retval = extractConditionGroupNamesUsedInTestCases(suite);
		retval.addAll(extractConditionGroupNamesUsedInCondtionGroupInheritance(suite));

		return retval;
	}

	private Set<String> extractConditionGroupNamesUsedInCondtionGroupInheritance(
			XMLTestSuite suite) {
		Set<String> retval = new HashSet<String>();
		if (suite.getConditionGroups() != null) {
			for (XMLConditionGroup cg : suite.getConditionGroups()
					.getConditionGroupList()) {
				if (cg.getInheritFrom() != null) {
					retval.add(cg.getInheritFrom());
				}
			}
		}
		return retval;
	}

	private Set<String> extractConditionGroupNamesUsedInTestCases(
			XMLTestSuite suite) {
		Set<String> retval = new HashSet<String>();

		retval.addAll(extractConditionGroupsUsedInPartnerTracks(suite));
		retval.addAll(extractConditionGroupsUsedInHumanPartnerTracks(suite));

		return retval;
	}

	private Set<String> extractConditionGroupsUsedInPartnerTracks(
			XMLTestSuite suite) {
		Set<String> retval = new HashSet<String>();

		List<XMLTrack> partnerTracks = extractTracks(suite);

		for (XMLTrack pt : partnerTracks) {
			if (pt.getReceiveOnlyList() != null) {
				for (XMLReceiveActivity a : pt.getReceiveOnlyList()) {
					retval.addAll(a.getConditionGroupList());
				}
			}
			if (pt.getReceiveSendList() != null) {
				for (XMLTwoWayActivity a : pt.getReceiveSendList()) {
					retval.addAll(a.getReceive().getConditionGroupList());
				}
			}
			if (pt.getSendReceiveList() != null) {
				for (XMLTwoWayActivity a : pt.getSendReceiveList()) {
					retval.addAll(a.getReceive().getConditionGroupList());
				}
			}
			if (pt.getReceiveSendAsynchronousList() != null) {
				for (XMLTwoWayActivity a : pt.getReceiveSendAsynchronousList()) {
					retval.addAll(a.getReceive().getConditionGroupList());
				}
			}
			if (pt.getSendReceiveAsynchronousList() != null) {
				for (XMLTwoWayActivity a : pt.getSendReceiveAsynchronousList()) {
					retval.addAll(a.getReceive().getConditionGroupList());
				}
			}
		}

		return retval;
	}

	private Set<String> extractConditionGroupsUsedInHumanPartnerTracks(
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

	private List<XMLTrack> extractTracks(XMLTestSuite suite) {
		List<XMLTrack> retval = new ArrayList<XMLTrack>();

		if (suite != null && suite.getTestCases() != null
				&& suite.getTestCases().getTestCaseList() != null) {
			for (XMLTestCase tc : suite.getTestCases().getTestCaseList()) {
				if(tc.getClientTrack() != null) {
					retval.add(tc.getClientTrack());
				}
				if(tc.getPartnerTrackList() != null) {
					retval.addAll(tc.getPartnerTrackList());
				}
			}
		}

		return retval;
	}

	private List<XMLHumanPartnerTrack> extractHumanTracks(XMLTestSuite suite) {
		List<XMLHumanPartnerTrack> retval = new ArrayList<XMLHumanPartnerTrack>();

		if (suite != null && suite.getTestCases() != null
				&& suite.getTestCases().getTestCaseList() != null) {
			for (XMLTestCase tc : suite.getTestCases().getTestCaseList()) {
				retval.addAll(tc.getHumanPartnerTrackList());
			}
		}

		return retval;
	}
}
