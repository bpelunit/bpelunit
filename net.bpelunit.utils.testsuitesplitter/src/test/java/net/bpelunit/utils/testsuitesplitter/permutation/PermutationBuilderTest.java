package net.bpelunit.utils.testsuitesplitter.permutation;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class PermutationBuilderTest {

	private PermutationBuilder builder = new PermutationBuilder();
	
	private static final Set<Integer> SET_0 = new HashSet<Integer>(); 
	private static final Set<Integer> SET_1 = new HashSet<Integer>(); 
	private static final Set<Integer> SET_2 = new HashSet<Integer>(); 
	private static final Set<Integer> SET_0_1 = new HashSet<Integer>(); 
	private static final Set<Integer> SET_0_2 = new HashSet<Integer>(); 
	private static final Set<Integer> SET_1_2 = new HashSet<Integer>(); 
	private static final Set<Integer> SET_0_1_2 = new HashSet<Integer>(); 
	
	@BeforeClass
	public static void initialize() {
		SET_0.add(0);
		
		SET_1.add(1);
		
		SET_2.add(2);
		
		SET_0_1.add(0);
		SET_0_1.add(1);
		
		SET_0_2.add(0);
		SET_0_2.add(2);
		
		SET_1_2.add(1);
		SET_1_2.add(2);
		
		SET_0_1_2.add(0);
		SET_0_1_2.add(1);
		SET_0_1_2.add(2);
	}
	
	@Test
	public void test0() {
		Set<Set<Integer>> ps = builder.getPermutationSet(0);
		
		assertEquals(1, ps.size());
		assertTrue(ps.contains(SET_0));
	}
	
	@Test
	public void test1() {
		Set<Set<Integer>> ps = builder.getPermutationSet(1);
		
		assertEquals(3, ps.size());
		assertTrue(ps.contains(SET_0));
		assertTrue(ps.contains(SET_1));
		assertTrue(ps.contains(SET_0_1));
	}
	
	@Test
	public void test2() {
		Set<Set<Integer>> ps = builder.getPermutationSet(2);
		
		assertEquals(7, ps.size());
		assertTrue(ps.contains(SET_0));
		assertTrue(ps.contains(SET_1));
		assertTrue(ps.contains(SET_2));
		assertTrue(ps.contains(SET_0_1));
		assertTrue(ps.contains(SET_0_2));
		assertTrue(ps.contains(SET_1_2));
		assertTrue(ps.contains(SET_0_1_2));
	}

}
