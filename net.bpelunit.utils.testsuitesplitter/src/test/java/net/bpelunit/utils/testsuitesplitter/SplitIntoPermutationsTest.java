package net.bpelunit.utils.testsuitesplitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.Test;

public class SplitIntoPermutationsTest {

	@Test
	public void testGetSuiteFileName() {
		assertEquals("abc-1.bpts", SplitIntoPermutations.getSuiteFileName("abc", Arrays.asList(0)));
		assertEquals("abc-2.bpts", SplitIntoPermutations.getSuiteFileName("abc", Arrays.asList(1)));
		
		assertEquals("abc-1-2.bpts", SplitIntoPermutations.getSuiteFileName("abc", Arrays.asList(0, 1)));
		assertEquals("abc-3-5.bpts", SplitIntoPermutations.getSuiteFileName("abc", Arrays.asList(2, 4)));
		
		assertEquals("abc-1-2-3-4-5.bpts", SplitIntoPermutations.getSuiteFileName("abc", Arrays.asList(0, 1, 2, 3, 4)));
	}

	@Test
	public void testGeneratePermutedTestSuites1TC() throws Exception {
		XMLTestSuiteDocument tsd = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("testsuite-1testcase.bpts"));
		
		Map<Set<Integer>, XMLTestSuiteDocument> permutedTestSuites = SplitIntoPermutations.generatePermutedTestSuites(tsd);
		
		assertEquals(1, permutedTestSuites.size());
		
		assertCorrectContentsOfTestSuiteDocument(permutedTestSuites);
	}
	
	@Test
	public void testGeneratePermutedTestSuites2TCs() throws Exception {
		XMLTestSuiteDocument tsd = XMLTestSuiteDocument.Factory.parse(getClass().getResourceAsStream("testsuite-2testcases.bpts"));
		
		Map<Set<Integer>, XMLTestSuiteDocument> permutedTestSuites = SplitIntoPermutations.generatePermutedTestSuites(tsd);
		
		assertEquals(3, permutedTestSuites.size());
		
		assertCorrectContentsOfTestSuiteDocument(permutedTestSuites);
	}

	private void assertCorrectContentsOfTestSuiteDocument(
			Map<Set<Integer>, XMLTestSuiteDocument> permutedTestSuites) {
		for(Set<Integer> currentSet : permutedTestSuites.keySet()) {
			XMLTestSuiteDocument generatedTSD = permutedTestSuites.get(currentSet);
			Set<Integer> remainingInSet = new HashSet<Integer>(currentSet);
			
			for(XMLTestCase tc : generatedTSD.getTestSuite().getTestCases().getTestCaseList()) {
				Integer i = Integer.parseInt(tc.getName());
				
				assertTrue(currentSet.contains(i));
				remainingInSet.remove(i);
			}
			
			assertTrue(remainingInSet.isEmpty());
		}
	}
}
