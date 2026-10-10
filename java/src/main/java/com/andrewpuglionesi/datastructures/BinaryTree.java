package com.andrewpuglionesi.datastructures;

import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A binary tree data structure. The nodes are mutable.
 * @param <V> the data type for a node's value.
 */
@RequiredArgsConstructor
public class BinaryTree<V> {
    private static final String NONEXISTENT_PARENT_MSG = "Cannot add child to nonexistent parent node.";
    private static final int MAX_NUM_CHILDREN = 2;
    private int size = 0;

    /**
     * The root of the tree.
     */
    @Getter
    private final Node<V> root;

    /**
     * Represents an individual node in the tree.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @SuppressWarnings("PMD.ShortClassName")
    public static class Node<V> {
        /**
         * The node's left child.
         */
        private Node<V> left;
        /**
         * The node's right child.
         */
        private Node<V> right;
        /**
         * The value stored in the node.
         */
        private V value;

        /**
         * Constructor.
         * @param value the node's value.
         */
        public Node(final V value) {
            this.value = value;
        }
    }

    /**
     * @return the number of items in the tree.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Builds a binary tree from a list representing the level-order traversal of the tree. Null list items are assumed
     * to indicate a nonexistent node.
     * @param <T> The data type for values in the tree's nodes.
     * @param levelOrderTraversal a list representing a valid level-order (i.e., level by level) traversal of a binary
     *                            tree. The list items are not actual {@link Node}s, but rather the nodes' {@code value}
     *                            fields. Null items may be used to indicate null children of a non-null parent. (To
     *                            represent a node with an empty value, you may wish to use an
     *                            {@link java.util.Optional} data type for {@code T}). Non-null nodes will be added to
     *                            the first available non-null parent in the preceding level.<br>
     *                            For example, the list<br>
     *                            <code>[1,2,3,null,null,4,5,null,6]</code><br>
     *                            will result in the following tree structure:<br>
     * <pre>
     *             1                  <br><br>
     *    2                 3         <br><br>
     *                 4         5    <br><br>
     *                   6            <br><br>
     * </pre>
     *                            (In the above rendering, 6 is the right child of 4.)
     * @return a binary tree.
     * @throws UnsupportedOperationException if the input would require adding a child to a nonexistent node.
     */
    public static <T> BinaryTree<T> buildFromLevelOrderTraversal(@NonNull final List<T> levelOrderTraversal) {
        if (levelOrderTraversal.isEmpty()) {
            return new BinaryTree<>(null);
        }

        // do not attempt to build a populated tree with a null root
        if (levelOrderTraversal.get(0) == null && levelOrderTraversal.size() > 1) {
            throw new UnsupportedOperationException(NONEXISTENT_PARENT_MSG);
        } else if (levelOrderTraversal.get(0) == null) {
            return new BinaryTree<>(null);
        }

        return buildFromPopulatedTraversal(levelOrderTraversal);
    }

    /**
     * @return all values present in the tree in the order they would be encountered during a pre-order traversal.
     * Nonexistent child nodes will not be represented in the output.
     */
    public List<V> preOrderTraversal() {
        List<V> values = new ArrayList<>();
        this.collectPreOrder(this.root, values);
        return values;
    }

    /**
     * @return all values present in the tree in the order they would be encountered during an in-order traversal.
     * Nonexistent child nodes will not be represented in the output.
     */
    public List<V> inOrderTraversal() {
        List<V> values = new ArrayList<>();
        this.collectInOrder(this.root, values);
        return values;
    }

    /**
     * @return all values present in the tree in the order they would be encountered during a post-order traversal.
     * Nonexistent child nodes will not be represented in the output.
     */
    public List<V> postOrderTraversal() {
        List<V> values = new ArrayList<>();
        this.collectPostOrder(this.root, values);
        return values;
    }

    /**
     * @return all values present in the tree in the order they would be encountered during a level-order traversal.
     * Nonexistent child nodes will not be represented in the output.
     */
    public List<V> levelOrderTraversal() {
        List<V> values = new ArrayList<>();
        Queue<Node<V>> toVisit = new LinkedList<>();
        toVisit.add(this.root);

        while (!toVisit.isEmpty()) {
            Node<V> current = toVisit.poll();
            if (current != null) {
                values.add(current.getValue());
                toVisit.add(current.getLeft());
                toVisit.add(current.getRight());
            }
        }
        return values;
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private static <T> BinaryTree<T> buildFromPopulatedTraversal(final List<T> levelOrderTraversal) {
        final Node<T> root = new Node<>(levelOrderTraversal.get(0));
        int treeSize = 1;

        final LinkedList<Node<T>> parentQueue = new LinkedList<>(List.of(root));
        int childReferencesRemaining = MAX_NUM_CHILDREN; // number of child slots remaining for the current parent
        for (int i = 1; i < levelOrderTraversal.size(); i++) {
            if (parentQueue.isEmpty()) {
                throw new UnsupportedOperationException(NONEXISTENT_PARENT_MSG);
            }
            final T currVal = levelOrderTraversal.get(i);
            if (currVal != null) {
                final Node<T> currNode = new Node<>(currVal);
                parentQueue.add(currNode);
                if (childReferencesRemaining == MAX_NUM_CHILDREN) {
                    parentQueue.peek().setLeft(currNode);
                } else {
                    parentQueue.peek().setRight(currNode);
                }
                treeSize += 1;
            }
            childReferencesRemaining--; // even null nodes exhaust one of the current parent's child references
            if (childReferencesRemaining <= 0) {
                parentQueue.pop();
                childReferencesRemaining = MAX_NUM_CHILDREN;
            }
        }
        final BinaryTree<T> tree = new BinaryTree<>(root);
        tree.size = treeSize;
        return tree;
    }

    private void collectPreOrder(final Node<V> current, final List<V> values) {
        if (current != null) {
            values.add(current.getValue());
            collectPreOrder(current.getLeft(), values);
            collectPreOrder(current.getRight(), values);
        }
    }

    private void collectInOrder(final Node<V> current, final List<V> values) {
        if (current != null) {
            collectInOrder(current.getLeft(), values);
            values.add(current.getValue());
            collectInOrder(current.getRight(), values);
        }
    }

    private void collectPostOrder(final Node<V> current, final List<V> values) {
        if (current != null) {
            collectPostOrder(current.getLeft(), values);
            collectPostOrder(current.getRight(), values);
            values.add(current.getValue());
        }
    }
}
