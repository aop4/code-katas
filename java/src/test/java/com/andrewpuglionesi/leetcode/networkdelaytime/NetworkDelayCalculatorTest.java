package com.andrewpuglionesi.leetcode.networkdelaytime;

import com.andrewpuglionesi.datastructures.graph.DirectedGraph;
import com.andrewpuglionesi.datastructures.graph.UndirectedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NetworkDelayCalculatorTest {

    private NetworkDelayCalculator delayCalc;

    @BeforeEach
    public void setup() {
        this.delayCalc = new NetworkDelayCalculator();
    }

    @Test
    void calculateDelayTimeEmptyGraph() {
        DirectedGraph<String> network = new DirectedGraph<>();
        assertThrows(NoSuchElementException.class, () -> delayCalc.calculateDelayTime(network, "a"));
    }

    @Test
    void calculateDelayTimeSingleNode() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            addNode("a");
        }};
        assertEquals(0, delayCalc.calculateDelayTime(network, "a").get());
    }

    @Test
    void calculateDelayTimeSingleNodeSelfDirectedEdge() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            addEdge("a", "a", 55);
        }};
        assertEquals(0, delayCalc.calculateDelayTime(network, "a").get());
    }

    @Test
    void calculateDelayTimeTwoNodes() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            addEdge("a", "b", 55);
        }};
        assertEquals(55, delayCalc.calculateDelayTime(network, "a").get());
    }

    @Test
    void calculateDelayTimeZeroWeights() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            addEdge("a", "b", 0);
        }};
        assertEquals(0, delayCalc.calculateDelayTime(network, "a").get());
    }

    @Test
    void calculateDelayTimeOriginDoesNotExist() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            addEdge("a", "b", 55);
        }};
        assertThrows(NoSuchElementException.class, () -> delayCalc.calculateDelayTime(network, "c"));
    }

    @Test
    void calculateDelayTimeCyclicGraph() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            addEdge("A", "B", 5);
            addEdge("B", "C", 5);
            addEdge("C", "A", 5);
        }};
        assertEquals(10, delayCalc.calculateDelayTime(network, "A").get());
    }

    @Test
    void calculateDelayTimeTree() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            /*
            *                    A
            *               (1)    (3)
            *             B            E
            *          (1) (3)      (2)
            *        C        D    F
            */
            addEdge("A", "B", 1);
            addEdge("B", "C", 1);
            addEdge("B", "D", 3);
            addEdge("A", "E", 3);
            addEdge("E", "F", 2);
        }};
        assertEquals(5, delayCalc.calculateDelayTime(network, "A").get());
    }

    @Test
    void calculateDelayTimeBottlenecks() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            /*
             * Nodes form a diamond with a cross-beam between B and C.
             *
             *                    A
             *                (1)   (6)
             *              B --(1)--> C
             *                (9)  (2)
             *                   D
             */
            addEdge("A", "B", 1);
            addEdge("A", "C", 6);
            addEdge("B", "C", 1);
            addEdge("B", "D", 9);
            addEdge("C", "D", 2);
        }};
        // optimal path is A->B->C->D
        assertEquals(4, delayCalc.calculateDelayTime(network, "A").get());
    }

    @Test
    void calculateDelayTimeSnowflake() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            /*
             * Edges radiate from a central point
             *
             *                    C
             *               B         D
             *                    A
             *               G         E
             *                    F
             *
             */
            addEdge("A", "B", 2);
            addEdge("A", "C", 4);
            addEdge("A", "D", 6);
            addEdge("A", "E", 8);
            addEdge("A", "F", 10);
            addEdge("A", "G", 12);
        }};
        // constrained by longest path
        assertEquals(12, delayCalc.calculateDelayTime(network, "A").get());
    }

    @Test
    void calculateDelayTimeUndirectedGraph() {
        UndirectedGraph<String> network = new UndirectedGraph<>() {{
            /*
             *                    A
             *               (1)    (3)
             *             B            E
             *          (1) (3)      (2)
             *        C        D    F
             */
            addEdge("A", "B", 1);
            addEdge("B", "C", 1);
            addEdge("B", "D", 3);
            addEdge("A", "E", 3);
            addEdge("E", "F", 2);
        }};
        assertEquals(5, delayCalc.calculateDelayTime(network, "A").get());
    }

    @Test
    void calculateDelayTimeDisconnectedOrigin() {
        DirectedGraph<String> network = new DirectedGraph<>() {{
            addEdge("A", "B", 1);
            addEdge("B", "C", 1);
        }};
        // starting from B or C, we can't traverse the whole graph because of edge directions
        assertEquals(Optional.empty(), delayCalc.calculateDelayTime(network, "B"));
        assertEquals(Optional.empty(), delayCalc.calculateDelayTime(network, "C"));
    }
}
