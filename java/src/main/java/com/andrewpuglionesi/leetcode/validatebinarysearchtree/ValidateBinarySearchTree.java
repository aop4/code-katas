package com.andrewpuglionesi.leetcode.validatebinarysearchtree;

import com.andrewpuglionesi.datastructures.BinaryTree;
import lombok.NonNull;

/**
 * A service that determines whether a {@link BinaryTree} is a valid binary search tree.
 */
public class ValidateBinarySearchTree {

    private static final SubTreeStats INVALID_SUBTREE = new SubTreeStats(false);

    /**
     * Represents statistics describing a subtree of a {@link BinaryTree}.
     */
    private static class SubTreeStats {
        /**
         * The minimum value among the nodes in the subtree.
         */
        private int min;
        /**
         * The maximum value among the nodes in the subtree.
         */
        private int max;
        /**
         * Whether the subtree is a valid binary search tree (i.e., for every node, its left subtree contains values
         * strictly less than it, and its right subtree contains values strictly greater than it.)
         */
        private final boolean isValidSubtree;

        /**
         * Constructor.
         */
        public SubTreeStats(final boolean isValidSubtree) {
            this.isValidSubtree = isValidSubtree;
        }

        /**
         * Constructor.
         */
        public SubTreeStats(final int min, final int max, final boolean isValidSubtree) {
            this.min = min;
            this.max = max;
            this.isValidSubtree = isValidSubtree;
        }
    }

    /**
     * Checks whether {@code tree} is a valid binary search tree.
     * @param tree a {@link BinaryTree}.
     * @return true if the tree is empty or meets these criteria for a valid binary search tree: for every node,
     * its left subtree contains values strictly less than it, and its right subtree contains values strictly greater
     * than it. Returns false if this is not the case for any subtree of {@code tree} or if any node in the tree has a
     * null {@code value}.
     */
    public boolean isValidBst(@NonNull final BinaryTree<Integer> tree) {
        if (tree.getRoot() == null) {
            return true;
        }
        return this.collectSubTreeStats(tree.getRoot()).isValidSubtree;
    }

    /**
     * Recursively collects min and max data for the left and right subtrees of {@code curr}. If any subtree is found
     * to be invalid (it defies the restrictions on binary search trees), a flag indicating an invalid subtree
     * (a {@link SubTreeStats} with isValidSubtree == false) will be returned. The min and max values in the returned
     * object will not be reliable if isValidSubtree is false.
     */
    private SubTreeStats collectSubTreeStats(final BinaryTree.Node<Integer> curr) {
        if (curr == null) {
            return null;
        }
        // disqualify tree if any node has a null value attribute
        if (curr.getValue() == null) {
            return INVALID_SUBTREE;
        }

        final SubTreeStats leftSubTreeStats = this.collectSubTreeStats(curr.getLeft());
        final SubTreeStats rightSubTreeStats = this.collectSubTreeStats(curr.getRight());

        return this.buildSubTreeStats(curr, leftSubTreeStats, rightSubTreeStats);
    }

    private SubTreeStats buildSubTreeStats(final BinaryTree.Node<Integer> root, final SubTreeStats leftSubTreeStats, final SubTreeStats rightSubTreeStats) {
        if (leftSubTreeStats != null && (!leftSubTreeStats.isValidSubtree || leftSubTreeStats.max >= root.getValue())) {
            return INVALID_SUBTREE;
        }
        if (rightSubTreeStats != null && (!rightSubTreeStats.isValidSubtree || rightSubTreeStats.min <= root.getValue())) {
            return INVALID_SUBTREE;
        }
        return new SubTreeStats(
                leftSubTreeStats == null ? root.getValue() : leftSubTreeStats.min,
                rightSubTreeStats == null ? root.getValue() : rightSubTreeStats.max,
                true
        );
    }
}
