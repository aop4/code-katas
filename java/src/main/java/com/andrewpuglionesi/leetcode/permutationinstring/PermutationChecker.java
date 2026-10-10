package com.andrewpuglionesi.leetcode.permutationinstring;

import com.andrewpuglionesi.datastructures.Counter;

/**
 * A pattern-matching class that can identify permutations of one string within another.
 */
public class PermutationChecker {
    /**
     * Searches for a permutation of {@code source} within {@code target}.
     * @param target the string to look for permutations of.
     * @param source the string to search.
     * @return true if source contains a contiguous permutation of target.
     */
    public boolean containsPermutation(final String target, final String source) {
        if (source == null || target == null || source.length() == 0 || target.length() == 0) {
            return false;
        }
        if (source.length() < target.length()) {
            return false;
        }
        final Counter<Character> sourceCounter = this.buildInitialCounter(source, target.length());
        final Counter<Character> targetCounter = this.buildInitialCounter(target, target.length());

        if (sourceCounter.equals(targetCounter)) {
            return true;
        }

        // examines a sliding window of target.length() characters.
        for (int i = 0; i < source.length() - target.length(); i++) {
            sourceCounter.decrement(source.charAt(i));
            sourceCounter.increment(source.charAt(i + target.length()));
            if (sourceCounter.equals(targetCounter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a map of character frequencies for characters at index 0 to {@code end - 1} of a string.
     * @param source a String for which to count character frequencies.
     * @param end the end (non-inclusive) of the substring to count.
     * @return a map of character frequencies.
     */
    private Counter<Character> buildInitialCounter(final String source, final int end) {
        final Counter<Character> counter = new Counter<>();
        for (int i = 0; i < end; i++) {
            counter.increment(source.charAt(i));
        }
        return counter;
    }
}
