package com.andrewpuglionesi.leetcode.designlinkedlist;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An implementation of a singly linked list.
 * @param <T> data type of items in the list.
 */
public class MyLinkedList<T> {
    private ListNode<T> head;
    private ListNode<T> tail;
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private int size;

    /**
     * Represents a node within the linked list. Each node has a single {@code next} reference to another node.
     * @param <T> the data type of the node's value.
     */
    @Getter
    private static class ListNode<T> {
        private ListNode<T> next;
        private T value;

        /**
         * Constructor.
         * @param value the value of the node.
         */
        public ListNode(final T value) {
            this.value = value;
        }
    }

    /**
     * Constructor that creates an empty linked list.
     */
    public MyLinkedList() {
        this.size = 0;
    }

    /**
     * Retrieves a value by its index in the list.
     * @param index an integer index.
     * @return the value at index.
     * @throws IndexOutOfBoundsException if there is no item at the specified index.
     */
    public T get(final int index) {
        return this.getNodeAt(index).value;
    }

    /**
     * Retrieves the value stored at the head of the list.
     * @return T the head's value.
     * @throws NoSuchElementException if the list is empty.
     */
    public T getHead() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.head.value;
    }

    /**
     * Retrieves the value stored at the tail of the list.
     * @return T the tail's value.
     * @throws NoSuchElementException if the list is empty.
     */
    public T getTail() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.tail.value;
    }

    /**
     * Adds a value at the head of the list.
     * @param val the value to store at head.
     */
    public void addAtHead(final T val) {
        final ListNode<T> newHead = new ListNode<>(val);
        if (this.size == 0) {
            this.head = newHead;
            this.tail = newHead;
        } else {
            newHead.next = this.head;
            this.head = newHead;
        }
        this.size += 1;
    }

    /**
     * Adds a value at the tail of the list.
     * @param val the value to store at tail.
     */
    public void addAtTail(final T val) {
        final ListNode<T> newTail = new ListNode<>(val);
        if (this.size == 0) {
            this.head = newTail;
            this.tail = newTail;
        } else {
            this.tail.next = newTail;
            this.tail = newTail;
        }
        this.size += 1;
    }

    /**
     * Adds a value at the specified index. If there is already an item at the index, it will be pushed forward to the
     * next index.
     * @param index integer index.
     * @param val the value to add.
     * @throws IndexOutOfBoundsException if the index is invalid (less than 0 or strictly greater than the size of the
     * list).
     */
    public void addAtIndex(final int index, final T val) {
        if (index == 0) {
            this.addAtHead(val);
        } else if (index == this.size) {
            this.addAtTail(val);
        } else {
            final ListNode<T> newNode = new ListNode<>(val);
            final ListNode<T> prev = this.getNodeAt(index - 1);
            newNode.next = prev.next;
            prev.next = newNode;
            this.size += 1;
        }
    }

    /**
     * Delete the item at the specified index.
     * @param index integer index.
     * @throws IndexOutOfBoundsException if there is no item at the specified index.
     */
    @SuppressWarnings({"PMD.NullAssignment", "PMD.AvoidLiteralsInIfCondition"})
    public void deleteAtIndex(final int index) {
        this.validateIndex(index);
        if (index == 0) {
            // deleting head
            this.head = this.head.next;
            if (this.size == 1) {
                this.tail = null;
            }
        } else if (index == this.size - 1) {
            // deleting tail
            this.tail = this.getNodeAt(index - 1);
            this.tail.next = null;
        } else {
            // deleting a node in the middle of the list
            final ListNode<T> prev = this.getNodeAt(index - 1);
            prev.next = prev.next.next;
        }
        this.size--;
    }

    private ListNode<T> getNodeAt(final int index) {
        this.validateIndex(index);
        if (index == 0) {
            return this.head;
        }
        if (index == this.size - 1) {
            return this.tail;
        }
        ListNode<T> curr = this.head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr;
    }

    private void validateIndex(final int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    public int size() {
        return this.size;
    }

    /**
     * @return the contents of the list as an ArrayList.
     */
    public List<T> toList() {
        final ArrayList<T> list = new ArrayList<>(this.size);
        ListNode<T> curr = this.head;
        while (curr != null) {
            list.add(curr.value);
            curr = curr.next;
        }
        return list;
    }
}
