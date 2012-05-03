package net.bpelunit.utils.testsuitesplitter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.utils.testsuitesplitter.permutation.PermutationBuilder;

import org.apache.xmlbeans.XmlException;

/**
 * This is a small tool that splits up a test suite into all possible
 * combinations and writes them to separate files.
 * 
 * This tool is a quick hack. There are not enough tests nor is it guaranteed 
 * to work.
 * 
 * USE AT YOUR OWN RISK
 * 
 * @author Daniel Luebke
 */
public final class SplitIntoPermutations {

	private static final String BPTS_FILENAME_SUFFIX = ".bpts";

	private static final PermutationBuilder PERMUTATION_BUILDER = new PermutationBuilder();

	private static final PrintWriter CONSOLE =
		System.console() != null ? 
			System.console().writer() :
			new PrintWriter(new ByteArrayOutputStream());
	
	private SplitIntoPermutations() {
		// main only
	}
	
	public static void main(String[] args) throws IOException, XmlException {
	
		if(args.length == 0 || args[0].equals("--help")) {
			help();
		} 
		
		XMLTestSuiteDocument tsd = XMLTestSuiteDocument.Factory.parse(new File(
				args[0]));
		String prefix = getFileNameWithoutSuffix(args[0]);
		
		Map<Set<Integer>, XMLTestSuiteDocument> permutedTestSuites = generatePermutedTestSuites(tsd);
		
		for(Set<Integer> currentSet : permutedTestSuites.keySet()) {
			XMLTestSuiteDocument permutedTestSuite = permutedTestSuites.get(currentSet);
			
			String suiteFileName = getSuiteFileName(prefix, currentSet);
			permutedTestSuite.getTestSuite().setName(suiteFileName);
			CONSOLE.write("Writing " + suiteFileName + "...\n");
			permutedTestSuite.save(new File(suiteFileName));
		}
		
	}

	static Map<Set<Integer>, XMLTestSuiteDocument> generatePermutedTestSuites(XMLTestSuiteDocument tsd) throws IOException {
		
		Map<Set<Integer>, XMLTestSuiteDocument> result = new HashMap<Set<Integer>, XMLTestSuiteDocument>();
		
		Set<Set<Integer>> permutationSet = PERMUTATION_BUILDER.getPermutationSet(tsd.getTestSuite()
				.getTestCases().getTestCaseList().size() - 1);

		for (Set<Integer> currentSet : permutationSet) {
			XMLTestSuiteDocument permutedTestSuite = (XMLTestSuiteDocument) tsd.copy();

			XMLTestCasesSection testCases = permutedTestSuite.getTestSuite().getTestCases();
			for (int i = testCases.sizeOfTestCaseArray() - 1; i >= 0; i--) {

				if (!currentSet.contains(i)) {
					testCases.removeTestCase(i);
				}
			}

			result.put(currentSet, permutedTestSuite);
		}
		
		return result;
	}

	private static void help() {
		CONSOLE.write("SplitIntoPermutations\n");
		CONSOLE.write("\n");
		CONSOLE.write("SplitIntoPermutations file.bpts\n");
		CONSOLE.write("\n");
		CONSOLE.write("Splits a BPELUnit test suite into several files. Every combination of test cases is generated.\n");
		CONSOLE.write("\n");
		
		System.exit(1);
	}

	private static String getFileNameWithoutSuffix(String completeFileName) {
		if(completeFileName == null) {
			return "";
		}
		
		File f = new File(completeFileName);
		
		String fileName = f.getName();
		
		if(fileName.endsWith(BPTS_FILENAME_SUFFIX)) {
			fileName = fileName.substring(0, fileName.length() - BPTS_FILENAME_SUFFIX.length());
		}
		
		return fileName;
	}

	static String getSuiteFileName(String prefix, Collection<Integer> testcaseIndices) {
		List<Integer> sortedTestCaseIndices = sortSet(testcaseIndices);

		StringBuilder sb = new StringBuilder();
		sb.append(prefix);

		for (int i : sortedTestCaseIndices) {
			sb.append("-").append(i + 1);
		}

		sb.append(".bpts");

		return sb.toString();
	}

	private static List<Integer> sortSet(Collection<Integer> testcaseIndices) {
		List<Integer> sortedTestCaseIndices = new ArrayList<Integer>(
				testcaseIndices);
		Collections.sort(sortedTestCaseIndices);
		return sortedTestCaseIndices;
	}
}
