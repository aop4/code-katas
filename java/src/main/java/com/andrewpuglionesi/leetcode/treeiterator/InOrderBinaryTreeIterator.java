package com.andrewpuglionesi.leetcode.treeiterator;

import com.andrewpuglionesi.datastructures.BinaryTree;
import lombok.NonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Iterates through a binary tree in-order. (Which is to say, for each node, visits the left child, then outputs the
 * current value, and then visits the right child.)
 * @param <T> data type of the {@link BinaryTree}.
 */
public class InOrderBinaryTreeIterator<T> implements Iterator<T> {

    private final BinaryTree<T> tree;
    /**
     * The number of nodes that have been exhausted by the iterator so far, i.e. the number of times the user has called
     * next().
     */
    private int nodesVisited = 0;
    /**
     * The stack allows iteration to occur on-demand. Rather than iterating through all the nodes in the beginning
     * and collecting them into an ordered list, we save memory with a stack that holds only ancestors
     * visited in the path taken to reach the current node.
     */
    private final Stack<NodeData> stack;

    /**
     * Instantiates an in-order iterator for the tree provided.
     * @param tree a binary tree.
     */
    public InOrderBinaryTreeIterator(@NonNull final BinaryTree<T> tree) {
        this.tree = tree;
        this.stack = new Stack<>();
        this.stack.push(new NodeData(tree.getRoot()));
    }

    @Override
    public boolean hasNext() {
        return this.nodesVisited < this.tree.getSize();
    }

    @Override
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public T next() {
        // This is basically stop-motion recursion, with the context that would normally be in the call stack stored in
        // a stack of NodeData objects. Not my best work.
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        NodeData curr = this.stack.peek();
        // traverse to the leftmost descendant
        if (!curr.visitedLeft) {
            while (curr.node.getLeft() != null) {
                curr.visitedLeft = true;
                curr = new NodeData(curr.node.getLeft());
                this.stack.push(curr);
            }
        }
        // when we cannot go any further left, or we already explored the left subtree, output the current node's value
        if (!curr.visitedSelf) {
            curr.visitedSelf = true;
            this.nodesVisited++;
            return curr.node.getValue();
        }
        // after we've visited the left subtree and the node itself, visit the right subtree
        if (!curr.visitedRight && curr.node.getRight() != null) {
            curr.visitedRight = true;
            this.stack.push(new NodeData(curr.node.getRight()));
            return next();
        }
        // once we've visited the left subtree, the node itself, and the right subtree, it's time to revisit the parent
        this.stack.pop();
        return next();
    }

    /**
     * Stores iteration context for a node in the tree.
     */
    private class NodeData {
        private BinaryTree.Node<T> node;
        /**
         * Whether the iterator has visited {@code node}'s left child in the past.
         */
        private boolean visitedLeft;
        /**
         * Whether the iterator has visited {@code node}'s right child in the past.
         */
        private boolean visitedRight;
        /**
         * Whether the iterator has returned {@code node}'s value in the past.
         */
        private boolean visitedSelf;

        /**
         * Constructor. Creates a node with no visitation history.
         */
        public NodeData(final BinaryTree.Node<T> node) {
            this(node, false, false, false);
        }

        private NodeData(final BinaryTree.Node<T> node, final boolean visitedLeft, final boolean visitedSelf, final boolean visitedRight) {
            this.node = node;
            this.visitedLeft = visitedLeft;
            this.visitedSelf = visitedSelf;
            this.visitedRight = visitedRight;
        }
    }
}
