package com.andrewpuglionesi.leetcode.sametree;

import com.andrewpuglionesi.datastructures.BinaryTree;
import com.andrewpuglionesi.datastructures.BinaryTree.Node;
import lombok.NonNull;

/**
 * What can I say, it's a classic.
 */
public class SameTree {
    /**
     * Determines whether two binary trees are equivalent. They'll be considered equivalent if all nodes are in
     * corresponding locations with corresponding values.
     * @param tree1 a binary tree.
     * @param tree2 another binary tree.
     * @return true if the trees are equivalent, false otherwise.
     * @param <T> data type of the nodes' values in each tree. Both trees must have exactly the same type.
     */
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public <T> boolean areTreesEqual(@NonNull final BinaryTree<T> tree1, @NonNull final BinaryTree<T> tree2) {
        if (tree1 == tree2) {
            return true;
        }
        return this.areSubTreesEqual(tree1.getRoot(), tree2.getRoot());
    }

    /**
     * Determines whether two subtrees, with roots {@code node1} and {@code node2}, are equivalent.
     * @param node1 the root of node1 subtree.
     * @param node2 the root of another subtree.
     * @return true if the subtrees are equivalent, false otherwise.
     * @param <T> data type of the nodes' values in each subtree.
     */
    private <T> boolean areSubTreesEqual(final Node<T> node1, final Node<T> node2) {
        if (node1 == null && node2 == null) {
            return true;
        }
        if (node1 == null || node2 == null) {
            return false;
        }
        return node1.getValue().equals(node2.getValue())
                && this.areSubTreesEqual(node1.getLeft(), node2.getLeft())
                && this.areSubTreesEqual(node1.getRight(), node2.getRight());
    }
}
