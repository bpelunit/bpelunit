package net.bpelunit.framework.verify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLConditionGroup;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

public class NoCyclesInConditionGroupInheritanceValidator implements
		ITestSuiteValidator {

	@Override
	public void validate(XMLTestSuiteDocument suite) throws SpecificationException {
		Map<String, XMLConditionGroup> conditionGroups = extractConditionGroups(suite.getTestSuite());
		findCyclesInConditionGroups(conditionGroups);
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

	private void findCyclesInConditionGroups(
			Map<String, XMLConditionGroup> conditionGroups) throws SpecificationException {
		for (XMLConditionGroup cg : conditionGroups.values()) {
			findCycleInConditionGroup(cg, conditionGroups);
		}
	}

	private void findCycleInConditionGroup(XMLConditionGroup cgToCheck,
			Map<String, XMLConditionGroup> conditionGroups) throws SpecificationException {
		Set<XMLConditionGroup> parents = new HashSet<XMLConditionGroup>();

		XMLConditionGroup cg = cgToCheck;

		while (cg.getInheritFrom() != null) {
			parents.add(cg);
			String cgName = cg.getName();
			String cgParentName = cg.getInheritFrom();
			cg = conditionGroups.get(cgParentName);

			if (cg == null) {
				throw new SpecificationException(
						"Could not resolve condition group " + cgParentName
								+ " that is referenced from condition group "
								+ cgName);
			}

			if (parents.contains(cg)) {
				throw new SpecificationException("The Condition Group "
						+ cgToCheck.getName() + " has cyclic inheritance");
			}
		}
	}

}
