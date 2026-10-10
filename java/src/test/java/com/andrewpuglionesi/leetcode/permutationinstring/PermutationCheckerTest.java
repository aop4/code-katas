package com.andrewpuglionesi.leetcode.permutationinstring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PermutationCheckerTest {

    private final PermutationChecker permutationChecker = new PermutationChecker();

    private static final String AMERICA = "america";

    @Test
    void nullTargetAndSource() {
        assertFalse(this.permutationChecker.containsPermutation(null, null));
    }

    @Test
    void nullTarget() {
        assertFalse(this.permutationChecker.containsPermutation(null, AMERICA));
    }

    @Test
    void nullSource() {
        assertFalse(this.permutationChecker.containsPermutation(AMERICA, null));
    }

    @Test
    void emptyTarget() {
        assertFalse(this.permutationChecker.containsPermutation("", AMERICA));
    }

    @Test
    void emptySource() {
        assertFalse(this.permutationChecker.containsPermutation(AMERICA, ""));
    }

    @Test
    void sourceShorterThanTarget() {
        assertFalse(this.permutationChecker.containsPermutation("cc", "c"));
    }

    @Test
    void singleCharacterTargetIsPresent() {
        assertTrue(this.permutationChecker.containsPermutation("a", "a"));
    }

    @Test
    void singleCharacterTargetIsAbsent() {
        assertFalse(this.permutationChecker.containsPermutation("a", "b"));
    }

    @Test
    void isCaseSensitive() {
        assertFalse(this.permutationChecker.containsPermutation("a", "A"));
    }

    @Test
    void permutationAtBeginningOfSource() {
        assertTrue(this.permutationChecker.containsPermutation("mea", AMERICA)); // m,e,a are the first 3 letters of america
    }

    @Test
    void permutationAtEndOfSource() {
        assertTrue(this.permutationChecker.containsPermutation("cai", AMERICA)); // c,a,i are the last 3 letters of america
    }

    @Test
    void permutationInMiddleOfSource() {
        assertTrue(this.permutationChecker.containsPermutation("eir", AMERICA)); // e,r,i occur consecutively in the middle of america
    }

    @Test
    void nonContiguousPermutation() {
        assertFalse(this.permutationChecker.containsPermutation("mei", AMERICA)); // m, e, and i all occur in America, but not consecutively
    }

    @Test
    void equivalentStrings() {
        assertTrue(this.permutationChecker.containsPermutation(AMERICA, AMERICA));
    }
}
