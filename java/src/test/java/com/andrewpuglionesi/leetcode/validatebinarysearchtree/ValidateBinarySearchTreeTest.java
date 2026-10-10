package com.andrewpuglionesi.leetcode.validatebinarysearchtree;

import com.andrewpuglionesi.datastructures.BinaryTree;
import com.andrewpuglionesi.datastructures.BinaryTree.Node;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateBinarySearchTreeTest {
    private static final ValidateBinarySearchTree validator = new ValidateBinarySearchTree();

    @Test
    void isValidBstNullTree() {
        NullPointerException e = assertThrows(NullPointerException.class, () -> validator.isValidBst(null));
        assertEquals("tree is marked non-null but is null", e.getMessage());
    }

    @Test
    void isValidBstNullRoot() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertTrue(validator.isValidBst(tree));
    }

    @Test
    void isValidBstRootWithNullValue() {
        Node<Integer> root = Node.<Integer>builder()
                .value(null)
                .build();
        BinaryTree<Integer> tree = new BinaryTree<>(root);
        assertFalse(validator.isValidBst(tree)); // null Node.value field is not allowed
    }

    @Test
    void isValidBstSingleNodeTree() {
        Node<Integer> root = Node.<Integer>builder()
                .value(4)
                .build();
        BinaryTree<Integer> tree = new BinaryTree<>(root);
        assertTrue(validator.isValidBst(tree));
    }

    @Test
    void isValidBstLeftChildIsEqual() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                5,
             5,   10
        ));
        assertFalse(validator.isValidBst(tree));
    }

    @Test
    void isValidBstLeftChildIsLarger() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                5,
             6,   10
        ));
        assertFalse(validator.isValidBst(tree));
    }

    @Test
    void isValidBstRightChildIsEqual() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                5,
             4,   5
        ));
        assertFalse(validator.isValidBst(tree));
    }

    @Test
    void isValidBstRightChildIsSmaller() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                5,
             4,   4
        ));
        assertFalse(validator.isValidBst(tree));
    }

    @Test
    void isValidBstLeftChildIsSmallerAndRightChildIsLarger() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                5,
             4,   6
        ));
        assertTrue(validator.isValidBst(tree));
    }

    @Test
    void isValidBstLargeValidTree() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                       6,
                3,             9,
             2,    4,       8,    10
        ));
        assertTrue(validator.isValidBst(tree));
    }

    @Test
    void isValidBstRighthandViolationSpansTwoLevels() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                       6,
                3,             9,
             2,    4,       1,    10   // 1 is invalid because it is less than 6
        ));
        assertFalse(validator.isValidBst(tree));
    }

    @Test
    void isValidBstLefthandViolationSpansTwoLevels() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(List.of(
                       6,
                3,             9,
             2,    7,       8,    10   // 7 is invalid because it is larger than 6
        ));
        assertFalse(validator.isValidBst(tree));
    }

    @Test
    void isValidBstImbalancedTree() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                           7,
                3,                 null,
         null,        6,
                   5
        ));
        assertTrue(validator.isValidBst(tree));
    }
}
