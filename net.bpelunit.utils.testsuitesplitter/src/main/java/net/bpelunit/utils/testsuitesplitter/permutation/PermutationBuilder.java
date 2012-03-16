package net.bpelunit.utils.testsuitesplitter.permutation;

import java.util.HashSet;
import java.util.Set;

public class PermutationBuilder {

	public Set<Set<Integer>> getPermutationSet(int maxValue) {
		Set<Set<Integer>> permutationSet = new HashSet<Set<Integer>>();

		// add single values
		for (int i = 0; i <= maxValue; i++) {
			Set<Integer> singleNumberSet = new HashSet<Integer>();
			singleNumberSet.add(i);
			permutationSet.add(singleNumberSet);
		}

		// XXX There are better algos
		// Combine all values in set until there are no new combinations
		int startSize;
		do {
			startSize = permutationSet.size();
			Set<Set<Integer>> permutationSetCopy = new HashSet<Set<Integer>>(
					permutationSet);
			for (Set<Integer> set1 : permutationSetCopy) {
				for (Set<Integer> set2 : permutationSetCopy) {
					Set<Integer> newSet = new HashSet<Integer>(set1);
					newSet.addAll(set2);

					permutationSet.add(newSet);
				}
			}
		} while (startSize < permutationSet.size());

		return permutationSet;
	}
	
}
