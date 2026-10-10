package com.andrewpuglionesi.leetcode.equalmatrixpairs;

import com.andrewpuglionesi.datastructures.Counter;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution for "Equal Row and Column Pairs." Uses a map to count column lists.
 * This solution has O(n^2) time complexity, where n is the length of the square matrix. Brute force, in my mind,
 * would be O(n^3) because each comparison compares n items in the row-column pair, and the number of row-to-column
 * comparisons would be n^2. With this map approach, building the map is O(n^2) simply because there are n^2 items
 * in the matrix. But the comparison is only O(n^2) because hashing each of the n rows will take O(n) time.
 */
public class MatrixPairs {
    private static final int MAX_UNIQUE_ROW_LENGTHS = 1;

    /**
     * Counts identical row-column pairs in a matrix. Each horizontal row equal to a vertical column increases the
     * count by one.
     * @param matrix a 2-D list of integers. All the matrix's sub-lists must have the same length. It is assumed that
     *               sub-lists and their Integer values are non-null.
     * @return the number of identical row-column pairs in {@code matrix}.
     * @throws IllegalArgumentException if the matrix's rows do not all have the same length.
     */
    @SuppressWarnings({"PMD.ForLoopCanBeForeach", "PMD.AvoidInstantiatingObjectsInLoops"})
    public int countEqualRowColumnPairs(final List<List<Integer>> matrix) {
        if (matrix.stream().map(row -> row.size()).distinct().count() > MAX_UNIQUE_ROW_LENGTHS) {
            throw new IllegalArgumentException("Matrix rows must all have same length.");
        }
        // ensure empty lists are not counted as identical pairs
        if (matrix.isEmpty() || matrix.get(0).isEmpty()) {
            return 0;
        }
        // rows and columns can't be equal if they have differing lengths
        if (matrix.get(0).size() != matrix.size()) {
            return 0;
        }
        int equalPairs = 0;
        final Counter<List<Integer>> columnCounts = new Counter<>(); // maps column contents to their frequency
        for (int col = 0; col < matrix.get(0).size(); col++) {
            final List<Integer> column = new ArrayList<>();
            for (int row = 0; row < matrix.size(); row++) {
                column.add(matrix.get(row).get(col));
            }
            columnCounts.increment(column);
        }
        for (final List<Integer> row : matrix) {
            equalPairs += columnCounts.getOrDefault(row, 0L);
        }
        return equalPairs;
    }
}
