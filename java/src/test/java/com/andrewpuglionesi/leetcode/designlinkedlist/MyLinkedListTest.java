package com.andrewpuglionesi.leetcode.designlinkedlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyLinkedListTest {
    private MyLinkedList<Integer> linkedList;

    @BeforeEach
    void setup() {
        this.linkedList = new MyLinkedList<>();
    }

    @Test
    void testInitialState() {
        assertEquals(0, this.linkedList.size());
        assertEquals(Collections.emptyList(), this.linkedList.toList());
        assertThrows(NoSuchElementException.class, () -> this.linkedList.getHead());
        assertThrows(NoSuchElementException.class, () -> this.linkedList.getTail());
        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.deleteAtIndex(0));
    }

    @Test
    void testAddAtHeadOnce() {
        this.linkedList.addAtHead(3);

        assertEquals(1, this.linkedList.size());
        assertEquals(List.of(3), this.linkedList.toList());
        assertEquals(3, this.linkedList.getHead());
        assertEquals(3, this.linkedList.getTail());
        assertEquals(3, this.linkedList.get(0));
    }

    @Test
    void testAddAtHeadTwice() {
        this.linkedList.addAtHead(3);
        this.linkedList.addAtHead(4);

        assertEquals(2, this.linkedList.size());
        assertEquals(List.of(4, 3), this.linkedList.toList());
        assertEquals(4, this.linkedList.getHead());
        assertEquals(3, this.linkedList.getTail());
        assertEquals(4, this.linkedList.get(0));
        assertEquals(3, this.linkedList.get(1));
    }

    @Test
    void testAddAtTailOnce() {
        this.linkedList.addAtTail(3);

        assertEquals(1, this.linkedList.size());
        assertEquals(List.of(3), this.linkedList.toList());
        assertEquals(3, this.linkedList.getHead());
        assertEquals(3, this.linkedList.getTail());
        assertEquals(3, this.linkedList.get(0));
    }

    @Test
    void testAddAtTailTwice() {
        this.linkedList.addAtTail(3);
        this.linkedList.addAtTail(4);

        assertEquals(2, this.linkedList.size());
        assertEquals(List.of(3, 4), this.linkedList.toList());
        assertEquals(3, this.linkedList.getHead());
        assertEquals(4, this.linkedList.getTail());
        assertEquals(3, this.linkedList.get(0));
        assertEquals(4, this.linkedList.get(1));
    }

    @Test
    void addAtIndexZeroEmptyList() {
        this.linkedList.addAtIndex(0, 223);

        assertEquals(1, this.linkedList.size());
        assertEquals(List.of(223), this.linkedList.toList());
        assertEquals(223, this.linkedList.getHead());
        assertEquals(223, this.linkedList.getTail());
        assertEquals(223, this.linkedList.get(0));
    }

    @Test
    void addAtInvalidIndexEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.addAtIndex(-1, 999));
        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.addAtIndex(1, 999));
    }

    @Test
    void addAtIndexZeroPopulatedList() {
        this.linkedList.addAtTail(4);
        this.linkedList.addAtTail(5);
        this.linkedList.addAtIndex(0, 3);

        assertEquals(3, this.linkedList.size());
        assertEquals(List.of(3,4,5), this.linkedList.toList());
        assertEquals(3, this.linkedList.getHead());
        assertEquals(5, this.linkedList.getTail());
        assertEquals(3, this.linkedList.get(0));
    }

    @Test
    void addAtIndexInMiddleOfList() {
        this.linkedList.addAtTail(3);
        this.linkedList.addAtTail(5);
        this.linkedList.addAtTail(7);
        this.linkedList.addAtIndex(1, 4);
        this.linkedList.addAtIndex(3, 6);

        assertEquals(5, this.linkedList.size());
        assertEquals(List.of(3, 4, 5, 6, 7), this.linkedList.toList());
        assertEquals(3, this.linkedList.getHead());
        assertEquals(7, this.linkedList.getTail());
        assertEquals(4, this.linkedList.get(1));
        assertEquals(6, this.linkedList.get(3));
    }

    @Test
    void addAtIndexAtEndOfList() {
        this.linkedList.addAtTail(3);
        this.linkedList.addAtTail(4);

        this.linkedList.addAtIndex(2, 5);

        assertEquals(3, this.linkedList.size());
        assertEquals(List.of(3, 4, 5), this.linkedList.toList());
        assertEquals(3, this.linkedList.getHead());
        assertEquals(5, this.linkedList.getTail());
        assertEquals(5, this.linkedList.get(2));
    }

    @Test
    void addAtInvalidIndexPopulatedList() {
        this.linkedList.addAtTail(3);
        this.linkedList.addAtTail(4);

        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.addAtIndex(-1, 999));
        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.addAtIndex(3, 999));
    }

    @Test
    void deleteAtIndexSingletonList() {
        this.linkedList.addAtTail(1);
        this.linkedList.deleteAtIndex(0);

        assertEquals(0, this.linkedList.size());
        assertEquals(Collections.emptyList(), this.linkedList.toList());
    }

    @Test
    void deleteAtIndexTwoItemsDeleteHead() {
        this.linkedList.addAtTail(1);
        this.linkedList.addAtTail(2);
        this.linkedList.deleteAtIndex(0);

        assertEquals(1, this.linkedList.size());
        assertEquals(List.of(2), this.linkedList.toList());
        assertEquals(2, this.linkedList.getHead());
        assertEquals(2, this.linkedList.getTail());
    }

    @Test
    void deleteAtIndexTwoItemsDeleteTail() {
        this.linkedList.addAtTail(1);
        this.linkedList.addAtTail(2);
        this.linkedList.deleteAtIndex(1);

        assertEquals(1, this.linkedList.size());
        assertEquals(List.of(1), this.linkedList.toList());
        assertEquals(1, this.linkedList.getHead());
        assertEquals(1, this.linkedList.getTail());
    }

    @Test
    void deleteAtIndexThreeItemsDeleteHead() {
        this.linkedList.addAtTail(1);
        this.linkedList.addAtTail(2);
        this.linkedList.addAtTail(3);
        this.linkedList.deleteAtIndex(0);

        assertEquals(2, this.linkedList.size());
        assertEquals(List.of(2, 3), this.linkedList.toList());
        assertEquals(2, this.linkedList.getHead());
        assertEquals(3, this.linkedList.getTail());
    }

    @Test
    void deleteAtIndexThreeItemsDeleteTail() {
        this.linkedList.addAtTail(1);
        this.linkedList.addAtTail(2);
        this.linkedList.addAtTail(3);
        this.linkedList.deleteAtIndex(2);

        assertEquals(2, this.linkedList.size());
        assertEquals(List.of(1, 2), this.linkedList.toList());
        assertEquals(1, this.linkedList.getHead());
        assertEquals(2, this.linkedList.getTail());
    }

    @Test
    void deleteAtIndexThreeItemsDeleteMiddle() {
        this.linkedList.addAtTail(1);
        this.linkedList.addAtTail(2);
        this.linkedList.addAtTail(3);
        this.linkedList.deleteAtIndex(1);

        assertEquals(2, this.linkedList.size());
        assertEquals(List.of(1, 3), this.linkedList.toList());
        assertEquals(1, this.linkedList.getHead());
        assertEquals(3, this.linkedList.getTail());
    }

    @Test
    void deleteAtIndexInvalidIndex() {
        this.linkedList.addAtTail(1);
        this.linkedList.addAtTail(2);
        this.linkedList.addAtTail(3);

        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.deleteAtIndex(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> this.linkedList.deleteAtIndex(3));
    }
}
