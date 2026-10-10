package com.andrewpuglionesi.leetcode.treeiterator;

import com.andrewpuglionesi.datastructures.BinaryTree;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InOrderBinaryTreeIteratorTest {
    @Test
    void iterateEmptyTree() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Collections.emptyList());
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iterateOnlyTheRoot() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(
                7
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertTrue(iterator.hasNext());
        assertEquals(7, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void iterateNullLeftChildOfRoot() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                7,
          null,    6
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertEquals(List.of(7, 6), this.iteratorToList(iterator));
    }

    @Test
    void iterateNullRightChildOfRoot() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                7,
           6,      null
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertEquals(List.of(6, 7), this.iteratorToList(iterator));
    }

    @Test
    void iterateThreeNodesSymmetric() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                7,
            6,     5
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertEquals(List.of(6, 7, 5), this.iteratorToList(iterator));
    }

    @Test
    void iterateLinearLeftLeft() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                7,
            6,     null,
        5
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertEquals(List.of(5, 6, 7), this.iteratorToList(iterator));
    }

    @Test
    void iterateLinearRightRight() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                7,
         null,       6,
                null,   5
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertEquals(List.of(7, 6, 5), this.iteratorToList(iterator));
    }

    @Test
    void iterateCompleteThreeLevelTree() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                     1,
                2,       3,
             4,   5,   6,   7
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertEquals(List.of(4, 2, 5, 1, 6, 3, 7), this.iteratorToList(iterator));
    }

    @Test
    void iterateSparseFourLevelTree() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                            1,
                  2,                 3,
            5,       null,        6,    7,
         null,null,           null, 8
        ));
        InOrderBinaryTreeIterator<Integer> iterator = new InOrderBinaryTreeIterator<>(tree);
        assertEquals(List.of(5, 2, 1, 6, 8, 3, 7), this.iteratorToList(iterator));
    }

    private <T> List<T> iteratorToList(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
