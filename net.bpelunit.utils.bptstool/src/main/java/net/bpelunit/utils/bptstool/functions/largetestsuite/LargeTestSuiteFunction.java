package net.bpelunit.utils.bptstool.functions.largetestsuite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.utils.bptstool.functions.IFunction;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LargeTestSuiteFunction implements IFunction {

	@Override
	public String getName() {
		return "largetestsuite";
	}

	@Override
	public String getDescription() {
		return "Creates a large test suite by duplicating all test cases n times";
	}

	@Override
	public String getHelp() {
		return "Creates a large test suite by duplicating all test cases n times. Useful for enlarging test suites for performance measuring etc.";
	}

	@Override
	public void execute(String[] params) {
		if (params.length != 2) {
			throw new IllegalArgumentException("Bad parameters for "
					+ getName());
		}

		String bptsFilename = params[0];
		int repetititionTimes = 0;
		try {
			repetititionTimes = Integer.parseInt(params[1]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad parameters for "
					+ getName() + "; non-numeric repition count");
		}

		try {
			File testSuiteFile = new File(bptsFilename);
			XMLTestSuiteDocument testDoc = XMLTestSuiteDocument.Factory
					.parse(testSuiteFile);

			XMLTestCasesSection testCases = testDoc.getTestSuite()
					.getTestCases();
			List<XMLTestCase> testCasesToDuplicate = new ArrayList<XMLTestCase>(
					testCases.getTestCaseList());

			for (int i = 1; i < repetititionTimes; i++) {
				for (int t = 0; t < testCasesToDuplicate.size(); t++) {
					XMLTestCase newTestCase = testCases.addNewTestCase();
					newTestCase.set(testCasesToDuplicate.get(t));
					addPrefix(newTestCase, i + 1);

				}
			}

			// Add Numbering Prefix for existing Test Cases
			for (XMLTestCase tc : testCasesToDuplicate) {
				addPrefix(tc, 1);
			}

			testDoc.save(testSuiteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addPrefix(XMLTestCase tc, int i) {
		String prefix = "__" + (i + 1) + "__";

		tc.setName(prefix + tc.getName());

		addPrefixToIdAndDependsOn(tc.getDomNode(), prefix);
	}

	private void addPrefixToIdAndDependsOn(Node node, String prefix) {
		if(node.hasAttributes()) {
			Node idAttributeValue = node.getAttributes().getNamedItem("id");
			if(idAttributeValue != null) {
				idAttributeValue.setNodeValue(prefix + idAttributeValue.getNodeValue());
			}

			Node dependsOnValue = node.getAttributes().getNamedItem("dependsOn");
			if (dependsOnValue != null) {
				StringBuilder newDependsOn = new StringBuilder();
				String[] oldDependsOns = dependsOnValue.getNodeValue().split(",");
				for (String oldDependsOn : oldDependsOns) {
					newDependsOn.append(prefix).append(oldDependsOn).append(",");
				}
				newDependsOn.deleteCharAt(newDependsOn.length() - 1);
				dependsOnValue.setNodeValue(newDependsOn.toString());
			}
		}

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node childNode = list.item(i);
			addPrefixToIdAndDependsOn(childNode, prefix);
		}
	}

}
