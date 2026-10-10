package com.andrewpuglionesi.datastructures.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {
    @Test
    void edgeEqualsNull() {
        Graph.Edge edge = new Graph.Edge(1);
        assertFalse(edge.equals(null));
    }

    @Test
    void edgeEqualsOtherType() {
        Graph.Edge edge = new Graph.Edge(1);
        assertFalse(edge.equals("pandaBear"));
    }

    @Test
    void edgeEqualsDifferentWeights() {
        Graph.Edge edgeWeight1 = new Graph.Edge(1);
        Graph.Edge edgeWeight2 = new Graph.Edge(2);
        assertNotEquals(edgeWeight1, edgeWeight2);
        assertNotEquals(edgeWeight1.hashCode(), edgeWeight2.hashCode());
    }

    @Test
    void edgeEqualsSameWeights() {
        Graph.Edge edge = new Graph.Edge(1);
        Graph.Edge twin = new Graph.Edge(1);
        assertEquals(edge, twin);
        assertEquals(edge.hashCode(), twin.hashCode());
    }
}
