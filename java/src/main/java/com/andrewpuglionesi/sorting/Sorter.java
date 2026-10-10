package com.andrewpuglionesi.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A utility class with implementations of classic sorting algorithms.
 */
public final class Sorter {
    private Sorter() {}

    /**
     * Sorts {@code list} using the selection sort algorithm.
     * @param list the list to sort.
     * @param comparator a {@link Comparator} for determining the order of elements in the sorted list.
     * @param <T> the data type in the list.
     */
    public static <T> void selectionSort(final List<T> list, final Comparator<T> comparator) {
        for (int i = 0; i < list.size(); i++) {
            T min = list.get(i);
            int minIndex = i;
            for (int k = i + 1; k < list.size(); k++) {
                if (comparator.compare(list.get(k), min) < 0) {
                    min = list.get(k);
                    minIndex = k;
                }
            }
            swap(list, i, minIndex);
        }
    }

    /**
     * Sorts {@code list} using the insertion sort algorithm.
     * @param list the list to sort.
     * @param comparator a {@link Comparator} for determining the order of elements in the sorted list.
     * @param <T> the data type in the list.
     */
    public static <T> void insertionSort(final List<T> list, final Comparator<T> comparator) {
        for (int i = 1; i < list.size(); i++) {
            int swapIndex = i;
            while (swapIndex > 0 && comparator.compare(list.get(swapIndex), list.get(swapIndex - 1)) < 0) {
                swap(list, swapIndex, swapIndex - 1);
                swapIndex--;
            }
        }
    }

    private static <T> void swap(final List<T> list, final int index1, final int index2) {
        final T item1 = list.get(index1);
        final T item2 = list.get(index2);
        list.set(index1, item2);
        list.set(index2, item1);
    }

    /**
     * Sorts {@code list} using the merge sort algorithm.
     * @param list the list to sort.
     * @param comparator a {@link Comparator} for determining the order of elements in the sorted list.
     * @param <T> the data type in the list.
     */
    public static <T> void mergeSort(final List<T> list, final Comparator<T> comparator) {
        mergeSortIteration(list, 0, list.size(), comparator);
    }

    private static <T> void mergeSortIteration(final List<T> list, final int start, final int end, final Comparator<T> comparator) {
        if (start >= end - 1) {
            return;
        }
        final int middle = (start + end) / 2;
        mergeSortIteration(list, start, middle, comparator);
        mergeSortIteration(list, middle, end, comparator);
        mergeSortedSubLists(list, start, middle, end, comparator);
    }

    /**
     * Merges two sorted sub-lists contained within a larger list.
     * @param list a list containing two adjacent sorted sub-lists.
     * @param start the index where the first sorted sub-list begins.
     * @param middle the index where the second sorted sub-list begins.
     * @param end the index where the second sorted sub-list ends.
     * @param comparator a comparator.
     * @param <T> the data type in the list.
     */
    private static <T> void mergeSortedSubLists(final List<T> list, final int start, final int middle, final int end, final Comparator<T> comparator) {
        // could reduce total allocated heap space by having a single copy of the entire list that you use as a scratch
        // space for merging.
        int left = start;
        int right = middle;
        final List<T> sorted = new ArrayList<>();
        while (left < middle && right < end) {
            if (comparator.compare(list.get(left), list.get(right)) <= 0) {
                sorted.add(list.get(left));
                left++;
            } else {
                sorted.add(list.get(right));
                right++;
            }
        }
        for (int i = left; i < middle; i++) {
            sorted.add(list.get(i));
        }
        for (int i = 0; i < sorted.size(); i++) {
            list.set(i + start, sorted.get(i));
        }
    }
}
