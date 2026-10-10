package com.andrewpuglionesi.leetcode.designcircularqueue;

import java.util.Collections;
import java.util.Optional;

/**
 * An implementation of a circular queue with fixed capacity and memory allocation.
 */
public class CircularQueue<T> {

    private final T[] queue;
    /**
     * The number of items that the queue can accommodate.
     */
    private final int capacity;
    /**
     * The number of items currently in the queue.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private int size;
    /**
     * The index of the oldest item in the queue
     */
    private int start;
    /**
     * The index after the most recently added item in the queue.
     */
    private int end;

    /**
     * Constructor.
     * @param capacity the maximum number of items that may be stored in the queue.
     * @throws IllegalArgumentException if capacity is not a positive integer.
     */
    public CircularQueue(final int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("CircularQueue capacity must be a positive integer.");
        }
        this.queue = (T[]) Collections.nCopies(capacity, null).toArray();
        this.capacity = capacity;
        this.size = 0;
        this.start = 0;
        this.end = 0;
    }

    /**
     * Adds an item to the queue.
     * @param value the item to add.
     * @return true if the operation is successful, false otherwise (e.g., if the queue is already full).
     */
    public boolean enqueue(final T value) {
        if (this.isFull()) {
            return false;
        }
        this.queue[this.end] = value;
        this.end = this.getNextIndex(this.end);
        this.size++;
        return true;
    }

    /**
     * Removes the oldest item from the queue.
     * @return true if the operation is successful, false otherwise (e.g., if the queue is empty). Note that this method
     * does not return the value which was removed.
     */
    public boolean dequeue() {
        if (this.isEmpty()) {
            return false;
        }
        // Does not actually change the value at start index. Simply leaves it unreserved so it may later be overwritten
        this.start = this.getNextIndex(this.start);
        this.size--;
        return true;
    }

    /**
     * Returns the oldest item in the queue.
     * @return the oldest item if found, or an empty {@link Optional} if the queue is empty.
     */
    public Optional<T> head() {
        if (this.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.queue[start]);
    }

    /**
     * Returns the most recently added item in the queue.
     * @return the newest item if found, or an empty {@link Optional} if the queue is empty.
     */
    public Optional<T> tail() {
        if (this.isEmpty()) {
            return Optional.empty();
        }
        final int rearIndex = (this.queue.length + this.end - 1) % this.queue.length; // the tail is the item before "end"
        return Optional.of(this.queue[rearIndex]);
    }

    /**
     * @return the number of items in the queue.
     */
    public int size() {
        return this.size;
    }

    /**
     * @return the capacity (maximum number of items allowed) in the queue.
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * @return true if the queue has no items.
     */
    public boolean isEmpty() {
        return this.size <= 0;
    }

    /**
     * @return true if the queue is at capacity (has the maximum number of items).
     */
    public boolean isFull() {
        return this.size == this.capacity;
    }

    /**
     * @return the index after {@code index} in the q. If {@code index} is the last index in the queue, will return 0.
     */
    private int getNextIndex(final int index) {
        return (index + 1) % this.queue.length;
    }
}
