package com.andrewpuglionesi.leetcode.sametree;

import com.andrewpuglionesi.datastructures.BinaryTree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SameTreeTest {
    private static final SameTree treeChecker = new SameTree();

    @Test
    void areTreesEqualSameInstance() {
        BinaryTree<Integer> tree = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(10));
        assertTrue(treeChecker.areTreesEqual(tree, tree));
    }

    @Test
    void areTreesEqualNullRoots() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(null));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(null));
        assertTrue(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualNullAndNonNullRoots() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(10));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(null));
        assertFalse(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualSingleNodeWithSameValue() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(10));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(10));
        assertTrue(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualSingleNodeWithDifferentValue() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(10));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Collections.singletonList(8));
        assertFalse(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualNullValueOnDifferentSides() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                10,
            15,    null
        ));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                10,
           null,    15
        ));
        assertFalse(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualMirroredTrees() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                10,
            15,     6
        ));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                10,
             6,    15
        ));
        assertFalse(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualLargeTrees() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                  10,
            15,         2,
         3,    6,    9,    4
        ));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                  10,
            15,         2,
         3,    6,    9,    4
        ));
        assertTrue(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualMissingLeaf() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                  10,
            15,         2,
         3,    6,    9,    4
        ));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                  10,
            15,         2,
         3,    6,    9,   null
        ));
        assertFalse(treeChecker.areTreesEqual(tree1, tree2));
    }

    @Test
    void areTreesEqualIncorrectValue() {
        BinaryTree<Integer> tree1 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                  10,
            15,         2,
         3,    6,    9,    4
        ));
        BinaryTree<Integer> tree2 = BinaryTree.buildFromLevelOrderTraversal(Arrays.asList(
                  10,
            15,         2,
         3,    6,    9,   999
        ));
        assertFalse(treeChecker.areTreesEqual(tree1, tree2));
    }
}
