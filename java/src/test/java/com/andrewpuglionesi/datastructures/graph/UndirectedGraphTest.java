package com.andrewpuglionesi.datastructures.graph;

import com.andrewpuglionesi.datastructures.Counter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UndirectedGraphTest {

    @Test
    void addEdgeToGraphWhenNodesExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addNode("Bob");
            addNode("Alice");
            addEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertEquals(Collections.singletonList("Alice"), graph.getNeighbors("Bob"));
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Alice"));
        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertTrue(graph.containsEdge("Alice", "Bob"));
    }

    @Test
    void addEdgeToGraphBeforeNodesExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertEquals(Collections.singletonList("Alice"), graph.getNeighbors("Bob"));
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Alice"));
        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertTrue(graph.containsEdge("Alice", "Bob"));
    }

    @Test
    void addEdgeMultipleEdgesFromOneNode() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Alice");
            addEdge("Bob", "Darren");
        }};
        assertEquals(3, graph.size());
        assertTrue(equalsIgnoreOrder(graph.getNeighbors("Bob"), List.of("Alice", "Darren")));
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Alice"));
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Darren"));
        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertTrue(graph.containsEdge("Alice", "Bob"));
        assertTrue(graph.containsEdge("Bob", "Darren"));
        assertTrue(graph.containsEdge("Darren", "Bob"));
    }

    @Test
    void addEdgeDuplicateEdge() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Alice");
            addEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertEquals(Collections.singletonList("Alice"), graph.getNeighbors("Bob"));
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Alice"));
        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertTrue(graph.containsEdge("Alice", "Bob"));
    }

    @Test
    void addEdgeReflexiveEdge() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Bob");
        }};
        assertEquals(1, graph.size());
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Bob"));
        assertTrue(graph.containsEdge("Bob", "Bob"));
    }

    @Test
    void addEdgeCycle() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Alice");
            addEdge("Alice", "Bob");
        }};
        assertEquals(2, graph.size());
        assertEquals(Collections.singletonList("Alice"), graph.getNeighbors("Bob"));
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Alice"));
        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertTrue(graph.containsEdge("Alice", "Bob"));
    }

    @Test
    void removeEdgeBothNodesDoNotExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            removeEdge("Bob", "Alice");
        }};
        assertEquals(0, graph.size());
    }

    @Test
    void removeEdgeDestinationNodeDoesNotExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addNode("Bob");
            removeEdge("Bob", "Alice");
        }};
        assertEquals(1, graph.size());
        assertTrue(graph.getNeighbors("Bob").isEmpty());
    }

    @Test
    void removeEdge() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Alice");
            removeEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertTrue(graph.getNeighbors("Bob").isEmpty());
        assertTrue(graph.getNeighbors("Alice").isEmpty());
        assertFalse(graph.containsEdge("Bob", "Alice"));
        assertFalse(graph.containsEdge("Alice", "Bob"));
    }

    @Test
    void removeSameEdgeTwice() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Alice");
            removeEdge("Bob", "Alice");
            removeEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertTrue(graph.getNeighbors("Bob").isEmpty());
        assertTrue(graph.getNeighbors("Alice").isEmpty());
    }

    @Test
    void removeEdgeReflexiveEdge() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Bob");
            removeEdge("Bob", "Bob");
        }};
        assertEquals(1, graph.size());
        assertTrue(graph.getNeighbors("Bob").isEmpty());
        assertFalse(graph.containsEdge("Bob", "Bob"));
    }

    private <T> boolean equalsIgnoreOrder(Collection<T> c1, Collection<T> c2) {
        return (new Counter<>(c1)).equals(new Counter<>(c2));
    }

    @Test
    void collectionConstructor() {
        UndirectedGraph<String> graph = new UndirectedGraph<>(List.of("a", "b"));
        List<String> nodes = new ArrayList<>();
        graph.iterator().forEachRemaining(nodes::add);
        assertTrue(equalsIgnoreOrder(List.of("a", "b"), nodes));
    }

    @Test
    void getNeighborsNodeDoesNotExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>(List.of("a", "b"));
        assertThrows(NoSuchElementException.class, () -> graph.getNeighbors("x"));
    }

    @Test
    void addNodeDuplicateAddition() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        assertThrows(UnsupportedOperationException.class, () -> {
            graph.addNode(1);
            graph.addNode(1);
        });
    }

    @Test
    void equalsGraphWithIdenticalEdges() {
        UndirectedGraph<Integer> graph1 = new UndirectedGraph<>() {{
            addEdge(1, 2);
            addEdge(1, 3);
            addEdge(2, 3);
        }};
        UndirectedGraph<Integer> graph2 = new UndirectedGraph<>() {{
            addEdge(2, 1);
            addEdge(3, 1);
            addEdge(3, 2);
        }};
        assertTrue(graph1.equals(graph2));
    }

    @Test
    void equalsGraphWithSameWeights() {
        DirectedGraph<String> graph1 = new DirectedGraph<>() {{
            addEdge("a", "b", 3);
        }};
        DirectedGraph<String> graph2 = new DirectedGraph<>() {{
            addEdge("a", "b", 3);
        }};

        assertTrue(graph1.equals(graph2));
        assertEquals(graph1.hashCode(), graph2.hashCode());
    }

    @Test
    void equalsGraphWithDifferentWeights() {
        DirectedGraph<String> graph1 = new DirectedGraph<>() {{
            addEdge("a", "b", 3);
        }};
        DirectedGraph<String> graph2 = new DirectedGraph<>() {{
            addEdge("a", "b", 12);
        }};

        assertFalse(graph1.equals(graph2));
        assertNotEquals(graph1.hashCode(), graph2.hashCode());
    }

    @Test
    void distanceBetweenOriginDoesNotExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>();
        assertEquals(-1, graph.distanceBetween("Bob", "Alice"));
        graph.addEdge("John", "Yoko");
        assertEquals(-1, graph.distanceBetween("Bob", "Yoko"));
    }

    @Test
    void distanceBetweenDestinationDoesNotExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("John", "Yoko");
        }};
        assertEquals(-1, graph.distanceBetween("John", "Alice"));
    }

    @Test
    void distanceBetweenSelfDirectedEdgeExists() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Bob", "Bob");
        }};
        assertEquals(1, graph.distanceBetween("Bob", "Bob"));
    }

    @Test
    void distanceBetweenSelfDirectedEdgeDoesNotExist() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("George", "Ringo");
        }};
        assertEquals(2, graph.distanceBetween("George", "George"));
    }

    @Test
    void distanceBetweenNextDoorNeighbor() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("John", "Yoko");
        }};
        assertEquals(1, graph.distanceBetween("John", "Yoko"));
        assertEquals(1, graph.distanceBetween("Yoko", "John"));
        // the below tests that the graph does not cycle infinitely in search of the destination
        assertEquals(-1, graph.distanceBetween("John", "Kevin Bacon"));
    }

    @Test
    void distanceBetweenLongPath() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            // John -> Yoko -> Paul -> Ringo -> George
            addEdge("John", "Yoko");
            addEdge("Yoko", "Paul");
            addEdge("Paul", "Ringo");
            addEdge("Ringo", "George");
        }};
        assertEquals(4, graph.distanceBetween("John", "George"));
        assertEquals(4, graph.distanceBetween("George", "John"));
        assertEquals(3, graph.distanceBetween("Yoko", "George"));
        assertEquals(3, graph.distanceBetween("George", "Yoko"));
    }

    @Test
    void distanceBetweenChoosesShortestPath() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            // John -> John
            // John -> Yoko -> Jimi -> George
            // John -> Paul -> George
            addEdge("John", "John");

            addEdge("John", "Yoko");
            addEdge("Yoko", "Jimi");
            addEdge("Jimi", "George");

            addEdge("John", "Paul");
            addEdge("Paul", "George");
        }};
        assertEquals(2, graph.distanceBetween("John", "George"));
        assertEquals(2, graph.distanceBetween("George", "John"));
    }

    @Test
    void isConnectedGraphEmptyGraph() {
        UndirectedGraph<String> graph = new UndirectedGraph<>();
        assertFalse(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphSingleton() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addNode("Kevin bacon");
        }};
        assertTrue(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphLinearChain() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");
            addEdge("Zooey Deschanel", "Danny DeVito");
        }};
        assertTrue(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphCycle() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");
            addEdge("Zooey Deschanel", "Kevin bacon");
        }};
        assertTrue(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphLonerNode() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");
            addNode("Danny DeVito");
        }};
        assertFalse(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphTwoIslands() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");

            addEdge("Danny DeVito", "Will Smith");
            addEdge("Will Smith", "Sadie Sink");
        }};
        assertFalse(graph.isConnectedGraph());
    }

    @Test
    void getNodesConnectedTo() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            // Kevin->Tom->Zooey<-Sadie
            addEdge("Kevin bacon", "Kevin bacon");

            addEdge("Kevin bacon", "Tom Cruise");

            addEdge("Tom Cruise", "Zooey Deschanel");

            addEdge("Sadie Sink", "Zooey Deschanel");
        }};
        assertEquals(Set.of("Tom Cruise", "Zooey Deschanel", "Sadie Sink"), graph.getNodesConnectedTo("Kevin bacon"));
    }

    @Test
    void hasCycleEmptyGraph() {
        UndirectedGraph<String> graph = new UndirectedGraph<>();
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleSingletonGraph() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addNode("a");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleSingleEdge() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleReflexiveEdge() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "a");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleTriangleCycle() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b");
            addEdge("b", "c");
            addEdge("c", "a");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleFourMemberCycle() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b");
            addEdge("b", "c");
            addEdge("c", "d");
            addEdge("d", "a");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleNonCyclicConvergence() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            // a->c<-b
            addEdge("a", "c");
            addEdge("b", "c");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleNonCyclicIslands() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b");

            addEdge("c", "d");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleCyclicIsland() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b");

            addEdge("c", "d");
            addEdge("d", "e");
            addEdge("e", "c");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleTree() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>() {{
            /*
                    1
               2         3
            4    5     6   7
             */
            addEdge(1, 2);
            addEdge(1, 3);

            addEdge(2, 4);
            addEdge(2, 5);

            addEdge(3, 6);
            addEdge(3, 7);
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleTreeWithCrossEdge() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>() {{
            /*
                    1
               2         3
            4    5 -> 6    7
             */
            // establishes a full binary tree
            addEdge(1, 2);
            addEdge(1, 3);

            addEdge(2, 4);
            addEdge(2, 5);

            addEdge(3, 6);
            addEdge(3, 7);

            // creates a sideways edge that leads to a large cycle
            addEdge(5, 6);
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleTreeWithBackEdges() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>() {{
            /*
                    1
               2         3
            4    5     6   7
             */
            // establishes a full binary tree
            addEdge(1, 2);
            addEdge(1, 3);

            addEdge(2, 4);
            addEdge(2, 5);

            addEdge(3, 6);
            addEdge(3, 7);

            // creates back edges linking leaves to the root
            addEdge(5, 1);
            addEdge(6, 1);
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void totalWeightEmptyGraph() {
        UndirectedGraph<String> graph = new UndirectedGraph<>();
        assertEquals(0, graph.totalWeight());
    }

    @Test
    void totalWeightUnspecifiedWeights() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b");
            addEdge("b", "a");
        }};
        assertEquals(0, graph.totalWeight());
    }

    @Test
    void totalWeightSpecifiedWeights() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b", -3);
            addEdge("b", "c", -6);
        }};
        assertEquals(-9, graph.totalWeight());
    }

    @Test
    void totalWeightUpdatedWeights() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b", 3);
            addEdge("b", "c", 6);
            addEdge("b", "c", 4); // updates b->c edge weight
        }};
        assertEquals(7, graph.totalWeight());
    }

    @Test
    void totalWeightAfterEdgeRemoval() {
        UndirectedGraph<String> graph = new UndirectedGraph<>() {{
            addEdge("a", "b", -3);
            addEdge("b", "c", -6);
            removeEdge("b", "c");
        }};
        assertEquals(-3, graph.totalWeight());
    }
}
