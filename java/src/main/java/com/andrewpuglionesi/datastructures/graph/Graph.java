package com.andrewpuglionesi.datastructures.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * Foundation for a basic in-memory graph data structure. Nodes are stored and retrieved by indexing their
 * values in a Map. The nodes' values must be unique. The implementations in this abstract class do not dictate whether
 * the graph is directed or undirected, nor do they dictate whether the graph can contain cycles or reflexive edges.
 * The addEdge and removeEdge methods are not implemented so that concrete subclasses may control this behavior.
 * @param <T> the data type of nodes' values. This type should have a reliable hashCode() and equals() implementation
 *           as the graph uses a hash table to store and retrieve nodes.
 */
@SuppressWarnings({"PMD.ShortVariable", "PMD.ShortClassName", "PMD.TooManyMethods"})
public abstract class Graph<T> implements Iterable<T> {

    /**
     * The default weight of edges in the graph (used when no weight value is specified).
     */
    public static final double DEFAULT_EDGE_WEIGHT = 0;

    /**
     * Maps a node's value to the neighbors of the node.
     */
    private final Map<T, Map<T, Edge>> nodes;

    /**
     * Represents a weighted edge in the graph.
     */
    @Getter
    protected static class Edge {
        /**
         * Numeric weight of the edge.
         */
        private final double weight;

        /**
         * Creates a new edge.
         * @param weight the new edge's weight.
         */
        public Edge(final double weight) {
            this.weight = weight;
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof Graph.Edge
                    && this.weight == ((Edge) other).weight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.weight);
        }
    }

    /**
     * Initializes an empty graph.
     */
    protected Graph() {
        this.nodes = new HashMap<>();
    }

    /**
     * Creates a graph containing nodes with the specified values. The graph will initially contain no edges.
     * @param values values to add to the graph.
     * @throws UnsupportedOperationException if there is a duplicate value.
     */
    protected Graph(final Collection<T> values) {
        this.nodes = new HashMap<>();
        values.forEach(this::addNode);
    }

    /**
     * Adds a node to the graph with the specified value.
     * @param value value to add to the graph.
     * @throws UnsupportedOperationException if the graph already contains a node with the specified value.
     */
    public void addNode(final T value) {
        if (this.containsNode(value)) {
            throw new UnsupportedOperationException("There is already a node in the Graph with value: " + value);
        }
        this.nodes.put(value, new HashMap<>());
    }

    /**
     * See {@link #addEdge(Object, Object, double)}. The newly added edge will have the default edge weight of
     * {@value DEFAULT_EDGE_WEIGHT}.
     */
    public abstract void addEdge(T from, T to);

    /**
     * Adds an edge to the graph between nodes with the specified values. If a node with either value does not exist,
     * the node will be created. If the edge already exists, its weight will be updated.
     * @param from value of the node that is the origin of the edge (assuming the graph is directed).
     * @param to value of the node that is the terminus of the edge (assuming the graph is directed).
     * @param edgeWeight the numeric weight of the new edge.
     */
    public abstract void addEdge(T from, T to, double edgeWeight);

    /**
     * See {@link #insertEdge(Object, Object, double)}. The newly added edge will have the default edge weight of
     * {@value DEFAULT_EDGE_WEIGHT}.
     */
    protected void insertEdge(final T from, final T to) {
        this.insertEdge(from, to, DEFAULT_EDGE_WEIGHT);
    }

    /**
     * Inserts a new edge between nodes with the values {@code from} and {@code to}.
     * The node whose value is equal to {@code from} will be the source of the edge. The node whose value is {@code to}
     * will be the terminus.
     * In other words, adds the node whose value is {@code to} into the neighbors of the node whose value is
     * {@code from}. The nodes will be created if no nodes with the supplied values exist.
     * @param from value of the node that is the origin of the edge.
     * @param to value of the node that is the terminus of the edge.
     * @param edgeWeight the numeric weight of the new edge.
     */
    protected void insertEdge(final T from, final T to, final double edgeWeight) {
        if (!this.containsNode(from)) {
            this.addNode(from);
        }
        if (!this.containsNode(to)) {
            this.addNode(to);
        }
        this.nodes.get(from).put(to, new Edge(edgeWeight));
    }

    /**
     * Removes an edge from the graph. If the edge does not exist, the graph will remain unchanged.
     * @param from value of the node that is the origin of the edge (assuming the graph is directed).
     * @param to value of the node that is the terminus of the edge (assuming the graph is directed).
     */
    public abstract void removeEdge(T from, T to);

    /**
     * Deletes an edge between nodes with the values {@code from} and {@code to}. The node whose value is
     * equal to {@code from} is the source of the edge. The node whose value is {@code to} is the terminus.
     * In other words, takes the node whose value is {@code to} out of the neighbors of the node whose value is
     * {@code from}. If the edge does not exist, the graph will remain unchanged.
     * @param from value of the node that is the origin of the edge.
     * @param to value of the node that is the terminus of the edge.
     */
    protected void deleteEdge(final T from, final T to) {
        final Map<T, Edge> edges = this.nodes.get(from);
        if (edges != null) {
            edges.remove(to);
        }
    }

    /**
     * Checks if an edge exists in the graph.
     * @param from value of the node that is the origin of the edge (assuming the graph is directed).
     * @param to value of the node that is the terminus of the edge (assuming the graph is directed).
     * @return true if the edge exists.
     */
    public boolean containsEdge(final T from, final T to) {
        final Map<T, Edge> edges = this.nodes.get(from);
        return edges != null && edges.containsKey(to);
    }

    /**
     * Returns all the neighbors of a node as a list. Modifying the returned list will not modify the graph.
     * @param from value of the node whose neighbors to retrieve.
     * @return a list of nodes that are connected to {@code from} in the graph.
     */
    public List<T> getNeighbors(final T from) {
        if (!this.containsNode(from)) {
            throw new NoSuchElementException("Cannot retrieve neighbors because node does not exist in graph: " + from);
        }
        final Map<T, Edge> edges = this.nodes.get(from);
        final List<T> neighbors = new ArrayList<>();
        if (edges != null) {
            neighbors.addAll(edges.keySet());
        }
        return neighbors;
    }

    /**
     * Provides a read-only view of a graph edge.
     */
    @AllArgsConstructor
    @Getter
    public class EdgeView {
        /**
         * The origin node of the edge.
         */
        private final T source;
        /**
         * The destination node of the edge.
         */
        private final T destination;
        /**
         * The weight of the edge.
         */
        private final double weight;
    }

    /**
     * Retrieves a list of edges emanating from a specific node.
     * Modifying the returned list will not modify the graph.
     * @param from value of the node whose neighbors to retrieve.
     * @return edges whose origin is {@code from}.
     */
    public List<EdgeView> getOutboundEdges(final T from) {
        if (!this.containsNode(from)) {
            throw new NoSuchElementException("Cannot retrieve neighbors because node does not exist in graph: " + from);
        }
        final List<EdgeView> output = new ArrayList<>();
        final Map<T, Edge> edges = this.nodes.get(from);
        for (final Map.Entry<T, Edge> entry : edges.entrySet()) {
            output.add(new EdgeView(from, entry.getKey(), entry.getValue().weight));
        }
        return output;
    }

    /**
     * @return true if the graph has no nodes.
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * @return the number of nodes in the graph.
     */
    public int size() {
        return this.nodes.size();
    }

    /**
     * @return an iterator over the values in the graph.
     */
    @Override
    public Iterator<T> iterator() {
        return this.nodes.keySet().iterator();
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Graph) {
            return Objects.equals(this.nodes, ((Graph) other).nodes);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.nodes.hashCode();
    }

    /**
     * Computes the minimum distance (i.e., shortest path length) between two nodes using a breadth-first traversal. The
     * path length is calculated as the number of edges, not as a sum of edge weights.
     * @param from value of the origin node.
     * @param to value of the destination node.
     * @return the minimum number of edges that must be crossed to travel between the nodes, or -1 if there is no path
     * between them.
     */
    public int distanceBetween(final T from, final T to) {
        /**
         * A small data class to track the depth of enqueued nodes in a breadth-first search.
         */
        @AllArgsConstructor
        class NodeData {
            private final T value;
            private final int depth;
        }

        if (!this.containsNode(from) || !this.containsNode(to)) {
            return -1;
        }

        final Set<T> visited = new HashSet<>();
        final Queue<NodeData> queue = new LinkedList<>();
        queue.add(new NodeData(from, 0));

        while (!queue.isEmpty()) {
            final NodeData curr = queue.poll();
            if (Objects.equals(curr.value, to) && curr.depth > 0) {
                return curr.depth;
            }
            if (!visited.contains(curr.value)) {
                visited.add(curr.value);
                this.getNeighbors(curr.value).forEach((neighbor) -> {
                    queue.offer(new NodeData(neighbor, curr.depth + 1));
                });
            }
        }
        return -1;
    }

    /**
     * @param node value of a node.
     * @return true if the node is present in the graph.
     */
    public boolean containsNode(final T node) {
        return this.nodes.containsKey(node);
    }

    /**
     * @return true if there exists a cycle in the graph. A cycle is a path beginning at a node, N, that ultimately
     * revisits N. In other words, it is a circular path. A self-directed edge constitutes a cycle.
     */
    public abstract boolean hasCycle();

    /**
     * Determines if the graph is connected. In a connected graph, every node is connected to every other node by
     * a path of one or more edges. For directed graphs, this tests for strong connectivity, meaning that an edge that
     * connects A to B directionally is not considered to connect B to A.
     * @return true if this is a connected graph. An empty graph is not considered to be connected.
     */
    public abstract boolean isConnectedGraph();

    /**
     * Retrieves all nodes that are connected to {@code source} by one or more edges. The set will not contain
     * {@code source} even if it is connected to itself.
     * @param source value of a node.
     * @return a set containing the values of all nodes connected to {@code source}, excluding {@code source} itself.
     */
    protected Set<T> getNodesConnectedTo(final T source) {
        if (!this.containsNode(source)) {
            throw new NoSuchElementException("No node exists with value " + source);
        }
        final Set<T> visited = new HashSet<>();
        this.collectConnectionsDepthFirst(source, visited);
        visited.remove(source);
        return visited;
    }

    private void collectConnectionsDepthFirst(final T source, final Set<T> visited) {
        if (!visited.contains(source)) {
            visited.add(source);
            for (final T neighbor : this.getNeighbors(source)) {
                this.collectConnectionsDepthFirst(neighbor, visited);
            }
        }
    }

    /**
     * @return the sum of every edge's weight, combined.
     */
    protected double totalWeight() {
        return this.nodes.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .mapToDouble(Edge::getWeight)
                .sum();
    }
}
