package com.andrewpuglionesi.leetcode.networkdelaytime;

import com.andrewpuglionesi.datastructures.graph.Graph;

import java.util.*;

/**
 * Computes the minimum time required to traverse all the nodes in a network.
 */
public class NetworkDelayCalculator {

    /**
     * DTO representing a directed edge encountered during graph traversal.
     */
    private class NetworkEdge implements Comparable {
        /**
         * The node to which the edge leads.
         */
        private final String node;
        /**
         * The cumulative edge weight encountered en route to the destination node.
         */
        private final double cumulativeWeight;

        private NetworkEdge(final String node, final double cumulativeWeight) {
            this.node = node;
            this.cumulativeWeight = cumulativeWeight;
        }

        @Override
        public int compareTo(final Object other) {
            return Double.compare(this.cumulativeWeight, ((NetworkEdge) other).cumulativeWeight);
        }
    }

    /**
     * Computes the minimum time required to traverse all the nodes in a network starting from a specific origin node.
     * This simulates broadcasting a message to all nodes.
     * @param network a network represented by a weighted graph. Nodes in the graph are identified with strings. Delay
     *                time between nodes in the network are stored as edges in the graph.
     * @param origin string identifier for the origin node.
     * @return the minimum cumulative weight for a complete graph traversal starting from {@code origin}. Returns an
     * empty Optional if the traversal cannot be completed (i.e., the graph is not fully connected).
     * @throws NoSuchElementException if {@code origin} is not a node within the graph.
     */
    public Optional<Double> calculateDelayTime(final Graph<String> network, final String origin) {
        final Set<String> visited = new HashSet<>();
        final PriorityQueue<NetworkEdge> minHeap = new PriorityQueue<>();
        minHeap.add(new NetworkEdge(origin, 0));

        while (!minHeap.isEmpty()) {
            final NetworkEdge curr = minHeap.poll();
            if (visited.contains(curr.node)) {
                continue;
            }
            visited.add(curr.node);
            if (visited.size() == network.size()) {
                return Optional.of(curr.cumulativeWeight); // entire graph has been traversed
            }
            final List<Graph<String>.EdgeView> outboundEdges = network.getOutboundEdges(curr.node);
            outboundEdges.forEach(edge -> {
                minHeap.add(new NetworkEdge(edge.getDestination(), curr.cumulativeWeight + edge.getWeight()));
            });
        }
        return Optional.empty(); // this will occur if the graph is not fully connected
    }
}
