package com.andrewpuglionesi.datastructures.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An undirected graph, in which each edge can be traversed from either node that it connects. (This is accomplished by
 * creating two directional edges whenever a single edge is added).
 * @param <T> @param <T> the data type of nodes' values. This type should have a reliable hashCode() and equals()
 *           implementation, as the graph uses a hash table to store and retrieve nodes.
 */
@SuppressWarnings("PMD.ShortVariable")
public class UndirectedGraph<T> extends Graph<T> {

    /**
     * See {@link Graph#Graph()}
     */
    public UndirectedGraph() {
        super();
    }

    /**
     * See {@link Graph#Graph(Collection)}
     */
    public UndirectedGraph(final Collection<T> values) {
        super(values);
    }

    @Override
    public void addEdge(final T from, final T to, final double weight) {
        super.insertEdge(from, to, weight);
        super.insertEdge(to, from, weight);
    }

    @Override
    public void addEdge(final T from, final T to) {
        super.insertEdge(from, to);
        super.insertEdge(to, from);
    }

    @Override
    public void removeEdge(final T from, final T to) {
        super.deleteEdge(from, to);
        super.deleteEdge(to, from);
    }

    /**
     * Searches for a cycle in the graph. A self-directed edge between a node and itself constitutes a cycle. However,
     * an edge between two distinct nodes, although bidirectional, is not considered a cycle. Thus, in the context of an
     * undirected graph, cycles are either self-directed edges or circular paths containing three or more nodes.
     * @return true if there exists a cycle in the graph.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    @Override
    public boolean hasCycle() {
        final Set<T> visited = new HashSet<>();
        for (final T node : this) {
            if (this.searchForCycle(node, null, visited, new HashSet<>())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if a cycle is detected during a depth-first search.
     *
     * @param curr           the node currently being hit by the recursive search.
     * @param prev           the node hit by the recursive search immediately before {@code curr}; curr's direct
     *                       ancestor in the DFS tree.
     * @param visited        a set of all nodes that have been visited by the cycle-searching algorithm since its inception.
     * @param precedingNodes a set of nodes preceding this node in the current depth-first-search path. If a node in
     *                       this set is reached, then there is a cycle.
     * @return true if a cycle is detected during a depth-first search starting at {@code curr}.
     */
    private boolean searchForCycle(final T curr, final T prev, final Set<T> visited, final Set<T> precedingNodes) {
        if (precedingNodes.contains(curr)) {
            return true;
        }
        if (visited.contains(curr)) {
            return false;
        }
        precedingNodes.add(curr);
        visited.add(curr);
        for (final T neighbor : this.getNeighbors(curr)) {
            // ignore back-edges between the node and its direct predecessor, because every edge is bidirectional
            if (!Objects.equals(neighbor, prev) && this.searchForCycle(neighbor, curr, visited, precedingNodes)) {
                return true;
            }
        }
        precedingNodes.remove(curr);
        return false;
    }

    @Override
    public boolean isConnectedGraph() {
        if (this.isEmpty()) {
            return false;
        }
        // In an undirected graph, because the edges go in both directions, we can take an arbitrary node and test
        // whether it's connected to every other node. Because the edges are bidirectional, this would guarantee the
        // entire graph is connected.
        final T source = this.iterator().next();
        final Set<T> nodesConnectedToSource = this.getNodesConnectedTo(source);
        return nodesConnectedToSource.size() == this.size() - 1;
    }

    /**
     * @return the sum of every edge's weight, combined.
     */
    @Override
    public double totalWeight() {
        return super.totalWeight() / 2; // divide by 2 because every edge from A to B has a mirrored edge from B to A.
    }

}
