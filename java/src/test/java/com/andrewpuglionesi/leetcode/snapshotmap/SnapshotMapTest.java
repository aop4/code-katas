package com.andrewpuglionesi.leetcode.snapshotmap;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SnapshotMapTest {
    @Test
    void getFromEmptyMap() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        assertNull(map.get("walrus"));
        assertNull(map.get("walrus", 0));

        assertThrows(IllegalArgumentException.class, () -> map.get("walrus", -1));
        assertThrows(IllegalArgumentException.class, () -> map.get("walrus", 1));
    }

    @Test
    void getWithOneSnapshot() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.put("animal", "narwhal");

        assertEquals("narwhal", map.get("animal"));
        assertEquals("narwhal", map.get("animal", 0));

        assertThrows(IllegalArgumentException.class, () -> map.get("animal", -1));
        assertThrows(IllegalArgumentException.class, () -> map.get("animal", 1));
    }

    @Test
    void getOverwriteCurrentSnapshot() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.put("animal", "narwhal");
        map.put("animal", "mackerel");

        assertEquals("mackerel", map.get("animal"));
        assertEquals("mackerel", map.get("animal", 0));
    }

    @Test
    void getWithTwoSnapshots() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.put("animal", "narwhal");
        map.snap();
        map.put("animal", "mackerel");

        assertEquals("mackerel", map.get("animal"));
        assertEquals("narwhal", map.get("animal", 0));
        assertEquals("mackerel", map.get("animal", 1));

        assertThrows(IllegalArgumentException.class, () -> map.get("animal", -1));
        assertThrows(IllegalArgumentException.class, () -> map.get("animal", 2));
    }

    @Test
    void getWithThreeSnapshots() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.put("animal", "narwhal");
        map.snap();
        map.put("animal", "mackerel");
        map.snap();
        map.put("animal", "stoat");

        assertEquals("stoat", map.get("animal"));
        assertEquals("narwhal", map.get("animal", 0));
        assertEquals("mackerel", map.get("animal", 1));
        assertEquals("stoat", map.get("animal", 2));
    }

    @Test
    void getWithFourSnapshots() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.put("animal", "narwhal");
        map.snap();
        map.put("animal", "mackerel");
        map.snap();
        map.put("animal", "stoat");
        map.snap();
        map.put("animal", "weasel");

        assertEquals("weasel", map.get("animal"));
        assertEquals("narwhal", map.get("animal", 0));
        assertEquals("mackerel", map.get("animal", 1));
        assertEquals("stoat", map.get("animal", 2));
        assertEquals("weasel", map.get("animal", 3));
    }

    @Test
    void getWithFiveSnapshots() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.put("animal", "narwhal");
        map.snap();
        map.put("animal", "mackerel");
        map.snap();
        map.put("animal", "stoat");
        map.snap();
        map.put("animal", "weasel");
        map.snap();
        map.put("animal", "ferret");

        assertEquals("ferret", map.get("animal"));
        assertEquals("narwhal", map.get("animal", 0));
        assertEquals("mackerel", map.get("animal", 1));
        assertEquals("stoat", map.get("animal", 2));
        assertEquals("weasel", map.get("animal", 3));
        assertEquals("ferret", map.get("animal", 4));
    }

    @Test
    void getRetrievesLatestSnapshotAvailable() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.put("animal", "narwhal");
        map.snap();
        map.put("animal", "mackerel");
        map.snap();
        map.snap();
        map.snap();

        assertEquals("mackerel", map.get("animal"));
        assertEquals("mackerel", map.get("animal", 3));
    }

    @Test
    void getSnapshotsWithNoAssociatedValue() {
        SnapshotMap<String, String> map = new SnapshotMap<>();

        map.snap();
        map.snap();
        map.put("animal", "narwhal");
        map.snap();
        map.put("animal", "mackerel");

        assertNull(map.get("animal", 0));
        assertNull(map.get("animal", 1));
        assertEquals("narwhal", map.get("animal", 2));
        assertEquals("mackerel", map.get("animal", 3));
    }
}
