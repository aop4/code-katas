package com.andrewpuglionesi.leetcode.designhashset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashSetTest {
    private static final String SPAM = "spam";
    private static final String EGGS = "eggs";

    @Test
    void noArgsConstructor() {
        MyHashSet<String> set = new MyHashSet<>();
        assertEquals(0, set.getSize());
        assertEquals(10, set.getCapacity());
    }

    @Test
    void capacityConstructor() {
        MyHashSet<String> set = new MyHashSet<>(100);
        assertEquals(0, set.getSize());
        assertEquals(100, set.getCapacity());
    }

    @Test
    void capacityConstructorInvalidValue() {
        MyHashSet<String> set = new MyHashSet<>(0);
        assertEquals(0, set.getSize());
        assertEquals(10, set.getCapacity());
    }

    @Test
    void addChangesSize() {
        MyHashSet<String> set = new MyHashSet<>();
        set.add(SPAM);
        assertEquals(1, set.getSize());
        set.add(EGGS);
        assertEquals(2, set.getSize());
    }

    @Test
    void addSameKeyTwice() {
        MyHashSet<String> set = new MyHashSet<>();
        assertTrue(set.add(SPAM));
        assertEquals(1, set.getSize());
        assertFalse(set.add(SPAM));
        assertEquals(1, set.getSize());
    }

    @Test
    void addNullKey() {
        MyHashSet<String> set = new MyHashSet<>();
        assertTrue(set.add(null));
        assertEquals(1, set.getSize());
        assertTrue(set.contains(null));
    }

    @Test
    void addExpandsWhenThresholdReached() {
        MyHashSet<String> set = new MyHashSet<>(4);

        assertEquals(4, set.getCapacity());
        set.add("1");
        assertEquals(4, set.getCapacity());
        set.add("2");
        assertEquals(4, set.getCapacity());
        set.add("3");

        assertEquals(8, set.getCapacity()); // capacity should double when 75% full
        assertEquals(3, set.getSize());
        assertTrue(set.contains("1"));
        assertTrue(set.contains("2"));
        assertTrue(set.contains("3"));
    }

    @Test
    void containsEmptySet() {
        MyHashSet<String> set = new MyHashSet<>();
        assertFalse(set.contains(SPAM));
    }

    @Test
    void containsKeyNotPresent() {
        MyHashSet<String> set = new MyHashSet<>();
        set.add(EGGS);
        assertFalse(set.contains(SPAM));
    }

    @Test
    void containsKeyPresent() {
        MyHashSet<String> set = new MyHashSet<>();
        set.add(EGGS);
        assertTrue(set.contains(EGGS));
    }

    @Test
    void removeEmptySet() {
        MyHashSet<String> set = new MyHashSet<>();
        assertFalse(set.remove(SPAM));
        assertEquals(0, set.getSize());
    }

    @Test
    void removeKeyNotPresent() {
        MyHashSet<String> set = new MyHashSet<>();
        set.add(EGGS);
        assertFalse(set.remove(SPAM));
        assertEquals(1, set.getSize());
    }

    @Test
    void removeKeyIsPresent() {
        MyHashSet<String> set = new MyHashSet<>();
        set.add(EGGS);
        assertTrue(set.remove(EGGS));
        assertEquals(0, set.getSize());
    }

    @Test
    void bulkTest() {
        // This test takes advantage of the very predictable built-in Integer hash function, which just returns an
        // Integer's int value. If we set a large capacity as below, we can thus guarantee collisions by adding multiple
        // values where (value % capacity) is equivalent.
        MyHashSet<Integer> set = new MyHashSet<>(1000);
        for (int i = 0; i <= 100; i += 1) {
            assertTrue(set.add(i));
            assertTrue(set.contains(i));
        }
        // all of these will collide with the values added above
        for (int i = 1000; i <= 1100; i += 1) {
            assertTrue(set.add(i));
            assertTrue(set.contains(i));
        }
        // these values would collide if they were present
        for (int i = 2000; i <= 2100; i += 1) {
            assertFalse(set.contains(i));
            assertFalse(set.remove(i));
        }
        for (int i = 0; i <= 100; i += 1) {
            assertTrue(set.remove(i));
        }
        for (int i = 1000; i <= 1100; i += 1) {
            assertTrue(set.remove(i));
        }
        assertEquals(0, set.getSize());
    }

}
