package com.andrewpuglionesi.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CounterTest {
    private Counter<String> counter;
    
    private static final String SPAM = "spam";
    private static final String EGGS = "eggs";

    @BeforeEach
    void setup() {
        this.counter = new Counter<>();
    }

    @Test
    void constructorEmptyList() {
        Counter<Integer> counter = new Counter<>(Collections.emptyList());
        assertEquals(0, counter.size());
    }

    @Test
    void constructorNullCollection() {
        Counter<Integer> counter = new Counter<>(null);
        assertEquals(0, counter.size());
    }

    @Test
    void constructorPopulatedList() {
        Counter<Integer> counter = new Counter<>(List.of(1, 2, 2, 3));
        assertEquals(3, counter.size());
        assertEquals(1, counter.get(1));
        assertEquals(2, counter.get(2));
        assertEquals(1, counter.get(3));
    }

    @Test
    void getNonexistentKey() {
        assertNull(this.counter.get(SPAM));
    }

    @Test
    void getOrDefaultKeyExists() {
        Counter<String> counter = new Counter<>(Collections.singletonList(SPAM));
        assertEquals(1, counter.getOrDefault(SPAM, -1));
    }

    @Test
    void getOrDefaultNonexistentKey() {
        Counter<String> counter = new Counter<>(Collections.singletonList(SPAM));
        assertEquals(-1, counter.getOrDefault(EGGS, -1));
    }

    @Test
    void incrementOneKeyOnce() {
        this.counter.increment(SPAM);
        assertEquals(1, this.counter.size());
        assertEquals(1, this.counter.get(SPAM));
    }

    @Test
    void incrementSameKeyTwice() {
        this.counter.increment(SPAM);
        this.counter.increment(SPAM);
        assertEquals(1, this.counter.size());
        assertEquals(2, this.counter.get(SPAM));
    }

    @Test
    void incrementDistinctKeys() {
        this.counter.increment(SPAM);
        this.counter.increment(SPAM);

        this.counter.increment(EGGS);
        this.counter.increment(EGGS);
        this.counter.increment(EGGS);

        assertEquals(2, this.counter.size());
        assertEquals(2, this.counter.get(SPAM));
        assertEquals(3, this.counter.get(EGGS));
    }

    @Test
    void decrementNonexistentKey() {
        this.counter.decrement(SPAM);

        assertNull(this.counter.get(SPAM));
        assertEquals(0, this.counter.size());
    }

    @Test
    void decrementRemovesKey() {
        this.counter.increment(SPAM);
        this.counter.decrement(SPAM);

        assertNull(this.counter.get(SPAM));
        assertEquals(0, this.counter.size());
    }

    @Test
    void decrementReducesCount() {
        this.counter.increment(SPAM);
        this.counter.increment(SPAM);

        this.counter.decrement(SPAM);

        assertEquals(1, this.counter.size());
        assertEquals(1, this.counter.get(SPAM));
    }

    @Test
    void decrementMultipleKeys() {
        this.counter.increment(SPAM);
        this.counter.increment(SPAM);
        this.counter.increment(SPAM);

        this.counter.decrement(SPAM);
        this.counter.decrement(SPAM);

        this.counter.increment(EGGS);
        this.counter.increment(EGGS);
        this.counter.increment(EGGS);

        this.counter.decrement(EGGS);

        assertEquals(2, this.counter.size());
        assertEquals(1, this.counter.get(SPAM));
        assertEquals(2, this.counter.get(EGGS));
    }

    @Test
    void nullKeyIsAllowed() {
        this.counter.increment(null);
        this.counter.increment(null);
        assertEquals(1, this.counter.size());
        assertEquals(2, this.counter.get(null));

        this.counter.decrement(null);
        assertEquals(1, this.counter.size());
        assertEquals(1, this.counter.get(null));

        this.counter.decrement(null);
        assertEquals(0, this.counter.size());
        assertNull(this.counter.get(null));
    }

    @Test
    void equalsWithNullArgument() {
        assertFalse(this.counter.equals(null));
    }

    @Test
    void equalsTypeMismatch() {
        assertFalse(this.counter.equals("hydrangea"));
    }

    @Test
    void equalsKeyTypeMismatch() {
        Counter<String> stringCounter = new Counter<>();
        Counter<Date> dateCounter = new Counter<>();

        stringCounter.increment(SPAM);
        dateCounter.increment(new Date());

        assertFalse(stringCounter.equals(dateCounter));
        assertFalse(dateCounter.equals(stringCounter));
    }

    @Test
    void equalsKeyMismatch() {
        Counter<String> counter1 = new Counter<>();
        Counter<String> counter2 = new Counter<>();

        counter1.increment(SPAM);
        counter2.increment(EGGS);

        assertNotEquals(counter1, counter2);
    }

    @Test
    void equalsValueMismatch() {
        Counter<String> counter1 = new Counter<>();
        Counter<String> counter2 = new Counter<>();

        counter1.increment(SPAM);

        counter2.increment(SPAM);
        counter2.increment(SPAM);

        assertNotEquals(counter1, counter2);
    }

    @Test
    void equalsItself() {
        assertEquals(this.counter, this.counter);
    }

    @Test
    void hashcodeIsBasedOnMapImplementation() {
        HashMap<String, Long> map = new HashMap<>();
        map.put(SPAM, 1L);

        Counter<String> counter = new Counter<>();
        counter.increment(SPAM);

        assertEquals(map.hashCode(), counter.hashCode());
    }
}
