package com.andrewpuglionesi.datastructures.graph;

import com.andrewpuglionesi.datastructures.Counter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DirectedGraphTest {

    @Test
    void addEdgeToGraphWhenNodesExist() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addNode("Bob");
            addNode("Alice");
            addEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertEquals(Collections.singletonList("Alice"), graph.getNeighbors("Bob"));
        assertTrue(graph.getNeighbors("Alice").isEmpty());
        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertFalse(graph.containsEdge("Alice", "Bob"));
    }

    @Test
    void addEdgeToGraphBeforeNodesExist() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertEquals(Collections.singletonList("Alice"), graph.getNeighbors("Bob"));
        assertTrue(graph.getNeighbors("Alice").isEmpty());
        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertFalse(graph.containsEdge("Alice", "Bob"));
    }

    @Test
    void addEdgeMultipleEdgesFromOneNode() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Bob", "Alice");
            addEdge("Bob", "Darren");
        }};
        assertEquals(3, graph.size());
        assertTrue(equalsIgnoreOrder(graph.getNeighbors("Bob"), List.of("Alice", "Darren")));
        assertTrue(graph.getNeighbors("Alice").isEmpty());
        assertTrue(graph.getNeighbors("Darren").isEmpty());

        assertTrue(graph.containsEdge("Bob", "Alice"));
        assertTrue(graph.containsEdge("Bob", "Darren"));
    }

    @Test
    void addEdgeDuplicateEdge() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Bob", "Alice");
            addEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertEquals(Collections.singletonList("Alice"), graph.getNeighbors("Bob"));
        assertTrue(graph.getNeighbors("Alice").isEmpty());
        assertTrue(graph.containsEdge("Bob", "Alice"));
    }

    @Test
    void addEdgeReflexiveEdge() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Bob", "Bob");
        }};
        assertEquals(1, graph.size());
        assertEquals(Collections.singletonList("Bob"), graph.getNeighbors("Bob"));
        assertTrue(graph.containsEdge("Bob", "Bob"));
    }

    @Test
    void addEdgeCycle() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
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
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            removeEdge("Bob", "Alice");
        }};
        assertEquals(0, graph.size());
    }

    @Test
    void removeEdgeDestinationNodeDoesNotExist() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addNode("Bob");
            removeEdge("Bob", "Alice");
        }};
        assertEquals(1, graph.size());
        assertTrue(graph.getNeighbors("Bob").isEmpty());
    }

    @Test
    void removeEdge() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Bob", "Alice");
            removeEdge("Bob", "Alice");
        }};
        assertEquals(2, graph.size());
        assertTrue(graph.getNeighbors("Bob").isEmpty());
        assertTrue(graph.getNeighbors("Alice").isEmpty());
        assertFalse(graph.containsEdge("Bob", "Alice"));
    }

    @Test
    void removeSameEdgeTwice() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
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
        DirectedGraph<String> graph = new DirectedGraph<>() {{
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
        DirectedGraph<String> graph = new DirectedGraph<>(List.of("a", "b"));
        List<String> nodes = new ArrayList<>();
        graph.iterator().forEachRemaining(nodes::add);
        assertTrue(equalsIgnoreOrder(List.of("a", "b"), nodes));
    }

    @Test
    void getNeighborsNodeDoesNotExist() {
        DirectedGraph<String> graph = new DirectedGraph<>(List.of("a", "b"));
        assertThrows(NoSuchElementException.class, () -> graph.getNeighbors("x"));
    }

    @Test
    void addNodeDuplicateAddition() {
        DirectedGraph<Integer> graph = new DirectedGraph<>();
        assertThrows(UnsupportedOperationException.class, () -> {
            graph.addNode(1);
            graph.addNode(1);
        });
    }

    @Test
    void equalsNUll() {
        DirectedGraph<Integer> graph = new DirectedGraph<>() {{
            addEdge(1, 2);
        }};
        assertFalse(graph.equals(null));
    }

    @Test
    void equalsObjectThatIsNotAGraph() {
        DirectedGraph<Integer> graph = new DirectedGraph<>() {{
            addEdge(1, 2);
        }};
        assertFalse(graph.equals("Daffy Duck"));
    }

    @Test
    void equalsGraphWithDifferentDataType() {
        DirectedGraph<Integer> graph1 = new DirectedGraph<>() {{
            addEdge(1, 2);
        }};
        DirectedGraph<String> graph2 = new DirectedGraph<>() {{
            addEdge("John", "Yoko");
        }};
        assertFalse(graph1.equals(graph2));
        assertNotEquals(graph1.hashCode(), graph2.hashCode());
    }

    @Test
    void equalsGraphWithDifferentEdges() {
        DirectedGraph<Integer> graph1 = new DirectedGraph<>() {{
            addEdge(1, 2);
        }};
        DirectedGraph<Integer> graph2 = new DirectedGraph<>() {{
            addEdge(2, 1);
        }};
        assertFalse(graph1.equals(graph2));
        assertNotEquals(graph1.hashCode(), graph2.hashCode());
    }

    @Test
    void equalsGraphWithSameEdges() {
        DirectedGraph<Integer> graph1 = new DirectedGraph<>() {{
            addEdge(1, 2);
            addEdge(1, 3);
            addEdge(2, 3);
        }};
        DirectedGraph<Integer> graph2 = new DirectedGraph<>() {{
            addEdge(1, 2);
            addEdge(1, 3);
            addEdge(2, 3);
        }};
        assertTrue(graph1.equals(graph2));
        assertEquals(graph1.hashCode(), graph2.hashCode());
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
        DirectedGraph<String> graph = new DirectedGraph<>();
        assertEquals(-1, graph.distanceBetween("Bob", "Alice"));
        graph.addEdge("John", "Yoko");
        assertEquals(-1, graph.distanceBetween("Bob", "Yoko"));
    }

    @Test
    void distanceBetweenDestinationDoesNotExist() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("John", "Yoko");
        }};
        assertEquals(-1, graph.distanceBetween("John", "Alice"));
    }

    @Test
    void distanceBetweenSelfDirectedEdgeExists() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Bob", "Bob");
        }};
        assertEquals(1, graph.distanceBetween("Bob", "Bob"));
    }

    @Test
    void distanceBetweenSelfDirectedEdgeDoesNotExist() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("George", "Ringo");
        }};
        assertEquals(-1, graph.distanceBetween("George", "George"));
    }

    @Test
    void distanceBetweenNextDoorNeighbor() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("John", "Yoko");
        }};
        assertEquals(1, graph.distanceBetween("John", "Yoko"));
    }

    @Test
    void distanceBetweenQueryIsInWrongDirection() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("John", "Yoko");
        }};
        assertEquals(-1, graph.distanceBetween("Yoko", "John"));
    }

    @Test
    void distanceBetweenCycle() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("George", "Ringo");
            addEdge("Ringo", "George");
        }};
        assertEquals(2, graph.distanceBetween("George", "George"));
        // the below tests that the graph does not cycle infinitely in search of the destination
        assertEquals(-1, graph.distanceBetween("George", "Yoko"));
    }

    @Test
    void distanceBetweenLongPath() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            // John -> Yoko -> Paul -> Ringo -> George
            addEdge("John", "Yoko");
            addEdge("Yoko", "Paul");
            addEdge("Paul", "Ringo");
            addEdge("Ringo", "George");
        }};
        assertEquals(4, graph.distanceBetween("John", "George"));
        assertEquals(3, graph.distanceBetween("Yoko", "George"));
    }

    @Test
    void distanceBetweenChoosesShortestPath() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
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
    }

    @Test
    void isConnectedGraphEmptyGraph() {
        DirectedGraph<String> graph = new DirectedGraph<>();
        assertFalse(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphSingleton() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addNode("Kevin bacon");
        }};
        assertTrue(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphLinearChain() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");
            addEdge("Zooey Deschanel", "Danny DeVito");
        }};
        assertFalse(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphLinearChainWithBackEdges() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Kevin bacon");

            addEdge("Tom Cruise", "Zooey Deschanel");
            addEdge("Zooey Deschanel", "Tom Cruise");

            addEdge("Zooey Deschanel", "Danny DeVito");
            addEdge("Danny DeVito", "Zooey Deschanel");
        }};
        assertTrue(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphCycle() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");
            addEdge("Zooey Deschanel", "Kevin bacon");
        }};
        assertTrue(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphLonerNode() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");
            addNode("Danny DeVito");
        }};
        assertFalse(graph.isConnectedGraph());
    }

    @Test
    void isConnectedGraphTwoIslands() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("Kevin bacon", "Tom Cruise");
            addEdge("Tom Cruise", "Zooey Deschanel");

            addEdge("Danny DeVito", "Will Smith");
            addEdge("Will Smith", "Sadie Sink");
        }};
        assertFalse(graph.isConnectedGraph());
    }

    @Test
    void getNodesConnectedToSourceNodeAbsent() {
        DirectedGraph<String> graph = new DirectedGraph<>();
        assertThrows(NoSuchElementException.class, () -> graph.getNodesConnectedTo("a"));
    }

    @Test
    void getNodesConnectedTo() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            // Kevin->Tom->Zooey<-Sadie
            addEdge("Kevin bacon", "Kevin bacon");

            addEdge("Kevin bacon", "Tom Cruise");

            addEdge("Tom Cruise", "Zooey Deschanel");

            addEdge("Sadie Sink", "Zooey Deschanel");
        }};
        assertEquals(Set.of("Tom Cruise", "Zooey Deschanel"), graph.getNodesConnectedTo("Kevin bacon"));
    }

    @Test
    void hasCycleEmptyGraph() {
        DirectedGraph<String> graph = new DirectedGraph<>();
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleSingletonGraph() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addNode("a");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleSingleEdge() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleReflexiveEdge() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "a");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleMinimalCycle() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b");
            addEdge("b", "a");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleTriangleCycle() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b");
            addEdge("b", "c");
            addEdge("c", "a");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleFourMemberCycle() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b");
            addEdge("b", "c");
            addEdge("c", "d");
            addEdge("d", "a");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleNonCyclicConvergence() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            // a->c<-b
            addEdge("a", "c");
            addEdge("b", "c");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleNonCyclicIslands() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b");

            addEdge("c", "d");
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleCyclicIsland() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b");

            addEdge("c", "d");
            addEdge("d", "c");
        }};
        assertTrue(graph.hasCycle());
    }

    @Test
    void hasCycleTree() {
        DirectedGraph<Integer> graph = new DirectedGraph<>() {{
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
    void hasCycleTreeWithCrossEdges() {
        DirectedGraph<Integer> graph = new DirectedGraph<>() {{
            /*
                    1
               2 ------> 3
            4 -> 5 --> 6 -> 7
             */
            // establishes a full binary tree
            addEdge(1, 2);
            addEdge(1, 3);

            addEdge(2, 4);
            addEdge(2, 5);

            addEdge(3, 6);
            addEdge(3, 7);

            // creates sideways edges linking the nodes of a given level
            addEdge(2, 3);
            addEdge(4, 5);
            addEdge(5, 6);
            addEdge(6, 7);
        }};
        assertFalse(graph.hasCycle());
    }

    @Test
    void hasCycleTreeWithBackEdges() {
        DirectedGraph<Integer> graph = new DirectedGraph<>() {{
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
        DirectedGraph<String> graph = new DirectedGraph<>();
        assertEquals(0, graph.totalWeight());
    }

    @Test
    void totalWeightUnspecifiedWeights() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b");
            addEdge("b", "a");
        }};
        assertEquals(0, graph.totalWeight());
    }

    @Test
    void totalWeightSpecifiedWeights() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b", -3);
            addEdge("b", "c", -6);
        }};
        assertEquals(-9, graph.totalWeight());
    }

    @Test
    void totalWeightUpdatedWeights() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b", 3);
            addEdge("b", "c", 6);
            addEdge("b", "c", 4); // updates b->c edge weight
        }};
        assertEquals(7, graph.totalWeight());
    }

    @Test
    void totalWeightAfterEdgeRemoval() {
        DirectedGraph<String> graph = new DirectedGraph<>() {{
            addEdge("a", "b", -3);
            addEdge("b", "c", -6);
            removeEdge("b", "c");
        }};
        assertEquals(-3, graph.totalWeight());
    }

    @Test
    void containsEdgeEdgeDoesNotExist() {
        DirectedGraph<String> graph = new DirectedGraph<>();
        assertFalse(graph.containsEdge("a", "b"));
    }
}
