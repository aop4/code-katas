package com.andrewpuglionesi.leetcode.designhashset;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * A hash set implementation, for kicks and giggles.
 * @param <K> data type of keys in the set. For best results, the class should implement a pseudo-unique,
 *           well-distributed hash function. The hash function MUST have the same output for any given input.
 */
public class MyHashSet<K> {
    /**
     * Default initial capacity to use when none is specified.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    /**
     * The size:capacity ratio at which the hash set's capacity is doubled.
     */
    private static final double EXPANSION_THRESHOLD = 0.75;

    /**
     * Items are stored in linked lists to accommodate separate chaining collision resolution.
     */
    private LinkedList<K>[] keyLists;

    /**
     * The number of keys present.
     */
    private int size;

    /**
     * Creates a hash set with the default initial capacity ({@value DEFAULT_INITIAL_CAPACITY}).
     */
    public MyHashSet() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Creates a hash set with the specified initial capacity.
     * @param capacity a positive integer specifying the hash set's initial capacity.
     */
    public MyHashSet(final int capacity) {
        final int initialCapacity = capacity > 0 ? capacity : DEFAULT_INITIAL_CAPACITY;
        this.keyLists = new LinkedList[initialCapacity];
        this.size = 0;
    }

    /**
     * @param key object to search for.
     * @return true if {@code key} exists in the hash set.
     */
    public boolean contains(final K key) {
        final int index = this.getIndex(key);
        if (this.keyLists[index] == null) {
            return false;
        }
        return this.keyLists[index].contains(key);
    }

    /**
     * Adds the key to the hash set, if the key is not already present.
     * @param key value to add to the set.
     * @return true if {@code key} was not already present in the set.
     */
    public boolean add(final K key) {
        if (this.contains(key)) {
            return false;
        }
        final int index = this.getIndex(key);
        if (this.keyLists[index] == null) {
            this.keyLists[index] = new LinkedList<>();
        }
        this.keyLists[index].add(key);
        this.size++;
        if (this.hasReachedExpansionThreshold()) {
            this.expand();
        }
        return true;
    }

    /**
     * Removes the specified key from the hash set if the key is present.
     * @param key value to remove from the set.
     * @return true if the item was removed.
     */
    public boolean remove(final K key) {
        final int index = this.getIndex(key);
        if (this.keyLists[index] == null) {
            return false;
        }
        final boolean removed = this.keyLists[index].remove(key);
        if (removed) {
            this.size--;
        }
        return removed;
    }

    /**
     * Returns the index in keyLists where the specified key's list would be located if the key were present. Collisions
     * are resolved by separate chaining, so the returned index locates the linked list where the key may be located.
     * @param key an item that may or may not be in the set.
     * @return the index where the key's chain would be located in the keyLists array, were the key present in the set.
     */
    private int getIndex(final K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() % this.keyLists.length;
    }

    /**
     * @return true if the set has reached the size:capacity ratio at which it should be expanded (where capacity is
     * defined as the size of the {@code keyLists} array and thus the maximum number of collision chains (linked lists)
     * that can exist).
     */
    private boolean hasReachedExpansionThreshold() {
        return ((double) this.size / (double) this.keyLists.length) >= EXPANSION_THRESHOLD;
    }

    /**
     * Expands the set by doubling the size of the array holding collision chains. The hash set is re-indexed, i.e.,
     * all items are redistributed based on the new array length, making this an O(n) operation.
     */
    private void expand() {
        final LinkedList<K>[] originalKeyLists = this.keyLists;
        this.keyLists = new LinkedList[this.keyLists.length * 2];
        this.size = 0; // reset the size to compensate for elements being re-added
        Arrays.stream(originalKeyLists)
                .filter(Objects::nonNull)
                .flatMap(LinkedList::stream)
                .forEach(this::add);
    }

    /**
     * @return the number of items in the set.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @return the number of separate chains that could theoretically be stored in the hash set.
     */
    public int getCapacity() {
        return this.keyLists.length;
    }
}
