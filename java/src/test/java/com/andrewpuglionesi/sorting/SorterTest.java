package com.andrewpuglionesi.sorting;

import com.andrewpuglionesi.datastructures.Counter;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SorterTest {
    private static final int NUM_PROPERTY_BASED_TESTS = 100;
    private static final Faker FAKER = new Faker();

    @Test
    void selectionSortPropertyBasedTest() {
        this.executePropertyBasedTest(Sorter::selectionSort);
    }

    @Test
    void insertionSortPropertyBasedTest() {
        this.executePropertyBasedTest(Sorter::insertionSort);
    }

    @Test
    void mergeSortPropertyBasedTest() {
        this.executePropertyBasedTest(Sorter::mergeSort);
    }

    private void executePropertyBasedTest(BiConsumer<List<Integer>, Comparator<Integer>> sortFunction) {
        for (int i = 0; i < NUM_PROPERTY_BASED_TESTS; i++) {
            // generate a list of random integers
            List<Integer> list = FAKER
                    .collection(() -> FAKER.random().nextInt(-50,50))
                    .len(0,100)
                    .generate();
            // count list values before sorting
            Counter<Integer> counter1 = new Counter<>(list);
            // sort the list with the provided sort function
            sortFunction.accept(list, Integer::compare);
            // count list values after sorting
            Counter<Integer> counter2 = new Counter<>(list);
            // ensure list has same count of each value after sorting
            assertEquals(counter1, counter2);
            // ensure that list is sorted in ascending order
            assertSortedAscending(list);
        }
    }

    private void assertSortedAscending(List<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            assertTrue(list.get(i - 1) <= list.get(i));
        }
    }

    // END property-based tests
    // BEGIN hard-coded parameterized tests (to check specific cases)

    private static Stream<Arguments> listsToSort() {
        return Stream.of(
                Arguments.of(Collections.emptyList(), Collections.emptyList()),
                Arguments.of(Arrays.asList(2), Arrays.asList(2)),
                Arguments.of(Arrays.asList(1, 2), Arrays.asList(1, 2)),
                Arguments.of(Arrays.asList(2, 1), Arrays.asList(1, 2)),
                Arguments.of(Arrays.asList(1, 2, 3, 4), Arrays.asList(1, 2, 3, 4)),
                Arguments.of(Arrays.asList(4, 3, 2, 1), Arrays.asList(1, 2, 3, 4)),
                Arguments.of(Arrays.asList(-4, 3, -2, 1), Arrays.asList(-4, -2, 1, 3)),
                Arguments.of(Arrays.asList(7, 7, 7, 7), Arrays.asList(7, 7, 7, 7)),
                Arguments.of(Arrays.asList(7, 7, 6, 6), Arrays.asList(6, 6, 7, 7)),
                Arguments.of(Arrays.asList(65, 100, 20, -10, 8, 50, 21, 103, -99), Arrays.asList(-99, -10, 8, 20, 21, 50, 65, 100, 103)),

                Arguments.of(
                        Arrays.asList(99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81, 80,
                                79, 78, 77, 76, 75, 74, 73, 72, 71, 70, 69, 68, 67, 66, 65, 64, 63, 62, 61, 60,
                                59, 58, 57, 56, 55, 54, 53, 52, 51, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40,
                                39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20,
                                19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0
                        ),
                        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                                21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                                41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                                61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
                                81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("listsToSort")
    void selectionSort(List<Integer> input, List<Integer> expected) {
        Sorter.selectionSort(input, Integer::compare);
        assertEquals(expected, input);
    }

    @ParameterizedTest
    @MethodSource("listsToSort")
    void insertionSort(List<Integer> input, List<Integer> expected) {
        Sorter.insertionSort(input, Integer::compare);
        assertEquals(expected, input);
    }

    @ParameterizedTest
    @MethodSource("listsToSort")
    void mergeSort(List<Integer> input, List<Integer> expected) {
        Sorter.mergeSort(input, Integer::compare);
        assertEquals(expected, input);
    }
}
