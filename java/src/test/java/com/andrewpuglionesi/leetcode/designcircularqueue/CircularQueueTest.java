package com.andrewpuglionesi.leetcode.designcircularqueue;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CircularQueueTest {
    @Test
    void validateInitialState() {
        CircularQueue<Integer> queue = new CircularQueue<>(1);
        assertEquals(1, queue.getCapacity());
        assertEquals(0, queue.size());
    }

    @Test
    void constructorWithInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue<Character>(0));
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue<Character>(-1));
    }

    @Test
    void enqueueOneItem() {
        CircularQueue<Integer> queue = new CircularQueue<>(1);

        assertTrue(queue.enqueue(7));

        assertEquals(1, queue.size());
    }

    @Test
    void enqueueMultipleItems() {
        CircularQueue<Integer> queue = new CircularQueue<>(2);

        assertTrue(queue.enqueue(7));
        assertTrue(queue.enqueue(8));

        assertEquals(2, queue.size());
    }

    @Test
    void enqueueAfterCapacityReached() {
        CircularQueue<Integer> queue = new CircularQueue<>(1);

        assertTrue(queue.enqueue(7));
        assertFalse(queue.enqueue(8));

        assertEquals(1, queue.size());
        assertEquals(7, queue.head().get());
        assertEquals(7, queue.tail().get());
    }

    @Test
    void dequeueWhenEmpty() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);

        assertFalse(queue.dequeue());

        assertEquals(0, queue.size());
    }

    @Test
    void dequeueOneItem() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);

        queue.enqueue(1);
        queue.enqueue(2);
        assertTrue(queue.dequeue());

        assertEquals(1, queue.size());
        assertEquals(2, queue.head().get());
        assertEquals(2, queue.tail().get());
    }

    @Test
    void dequeueAllItems() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);

        queue.enqueue(1);
        queue.enqueue(2);
        assertTrue(queue.dequeue());
        assertTrue(queue.dequeue());

        assertEquals(0, queue.size());
        assertEquals(Optional.empty(), queue.head());
        assertEquals(Optional.empty(), queue.tail());
    }

    @Test
    void dequeueAllItemsPlusOne() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);

        queue.enqueue(1);
        queue.enqueue(2);
        assertTrue(queue.dequeue());
        assertTrue(queue.dequeue());
        assertFalse(queue.dequeue());

        assertEquals(0, queue.size());
    }

    @Test
    void headOfEmptyQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);
        assertEquals(Optional.empty(), queue.head());
    }

    @Test
    void headIsEarliestAddedItem() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.head().get());
    }

    @Test
    void tailOfEmptyQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);
        assertEquals(Optional.empty(), queue.tail());
    }

    @Test
    void tailIsMostRecentlyAddedItem() {
        CircularQueue<Integer> queue = new CircularQueue<>(10);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.tail().get());
    }

    @Test
    void tailOfFullQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.tail().get());
    }

    @Test
    void isEmptyInitialState() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        assertTrue(queue.isEmpty());
    }

    @Test
    void isEmptyNonEmptyQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        queue.enqueue(4);
        assertFalse(queue.isEmpty());
    }

    @Test
    void isEmptyAfterRemovingAllItems() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        queue.enqueue(4);
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    void isFullInitialState() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        assertFalse(queue.isFull());
    }

    @Test
    void isFullPartiallyFullQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        queue.enqueue(4);
        assertFalse(queue.isFull());
    }

    @Test
    void isFullCompletelyFullQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);
        queue.enqueue(2);
        queue.enqueue(4);
        queue.enqueue(6);
        assertTrue(queue.isFull());
    }

    @Test
    void fullRotationOfCircularQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(3);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        // queue state: [1,2,3]
        assertEquals(1, queue.head().get());
        assertEquals(3, queue.tail().get());

        queue.dequeue();
        queue.enqueue(4);
        // queue state: [4,2,3]
        assertEquals(2, queue.head().get());
        assertEquals(4, queue.tail().get());

        queue.dequeue();
        queue.enqueue(5);
        // queue state: [4,5,3]
        assertEquals(3, queue.head().get());
        assertEquals(5, queue.tail().get());

        queue.dequeue();
        queue.enqueue(6);
        // queue state: [4,5,6]
        assertEquals(4, queue.head().get());
        assertEquals(6, queue.tail().get());

        queue.dequeue();
        queue.enqueue(7);
        // queue state: [7,5,6]
        assertEquals(5, queue.head().get());
        assertEquals(7, queue.tail().get());
    }
}
