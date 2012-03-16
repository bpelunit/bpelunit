package net.bpelunit.utils.testsuitesplitter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

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

}
