package com.andrewpuglionesi.leetcode.equalmatrixpairs;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatrixPairsTest {
    private final MatrixPairs matrixPairs = new MatrixPairs();

    @Test
    void countEqualRowColumnPairsEmptyLists() {
        List<List<Integer>> input = Collections.emptyList();
        assertEquals(0, matrixPairs.countEqualRowColumnPairs(input));

        input = List.of(Collections.emptyList());
        assertEquals(0, matrixPairs.countEqualRowColumnPairs(input));

        input = List.of(Collections.emptyList(), Collections.emptyList());
        assertEquals(0, matrixPairs.countEqualRowColumnPairs(input));
    }

    @Test
    void countEqualRowColumnPairsVariedRowLengths() {
        List<List<Integer>> input = List.of(
                List.of(1, 2, 3),
                List.of(1, 2, 3, 4)
        );
        assertThrows(IllegalArgumentException.class, () -> matrixPairs.countEqualRowColumnPairs(input));
    }

    @Test
    void countEqualRowColumnPairsRowAndColumnLengthDontMatch() {
        List<List<Integer>> input = List.of(
                List.of(1, 2, 3),
                List.of(1, 2, 3)
        );
        assertEquals(0, matrixPairs.countEqualRowColumnPairs(input));

        input = List.of(
                List.of(1, 2),
                List.of(1, 2),
                List.of(1, 2)
        );
        assertEquals(0, matrixPairs.countEqualRowColumnPairs(input));
    }

    @Test
    void countEqualRowColumnPairsSingletonList() {
        List<List<Integer>> input = Collections.singletonList(Collections.singletonList(7));
        assertEquals(1, matrixPairs.countEqualRowColumnPairs(input));
    }

    @Test
    void countEqualRowColumnPairsNoMatches() {
        List<List<Integer>> input = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9)
        );
        assertEquals(0, matrixPairs.countEqualRowColumnPairs(input));
    }

    @Test
    void countEqualRowColumnPairsAllMatch() {
        List<List<Integer>> input = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1),
                List.of(1, 1, 1)
        );
        assertEquals(9, matrixPairs.countEqualRowColumnPairs(input));
    }

    @Test
    void countEqualRowColumnPairsOneRowMatchesMultipleColumns() {
        List<List<Integer>> input = List.of(
                List.of(2, 2, 1),
                List.of(2, 2, 5),
                List.of(1, 1, 5)
        );
        assertEquals(2, matrixPairs.countEqualRowColumnPairs(input));
    }

    @Test
    void countEqualRowColumnPairsOneColumnMatchesMultipleRows() {
        List<List<Integer>> input = List.of(
                List.of(2, 2, 1),
                List.of(2, 2, 1),
                List.of(1, 5, 5)
        );
        assertEquals(2, matrixPairs.countEqualRowColumnPairs(input));
    }
}
