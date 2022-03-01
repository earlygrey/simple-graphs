/*
MIT License

Copyright (c) 2020 earlygrey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import space.earlygrey.simplegraphs.algorithms.Algorithms;
import space.earlygrey.simplegraphs.utils.WeightFunction;


public abstract class Graph<V> {

    //================================================================================
    // Members
    //================================================================================

    final NodeMap<V> nodeMap;

    /**
     * This is a map so that for undirected graphs, a consistent edge instance can be obtained from
     * either (u, v) or (v, u)
     */
    final LinkedHashMap<Connection<V>, Connection<V>> edgeMap;

    final Internals<V> internals = new Internals<>(this);

    private WeightFunction<V> defaultEdgeWeight = (a, b) -> 1;


    //================================================================================
    // Constructors
    //================================================================================

    protected Graph() {
        nodeMap = new NodeMap<>(this);
        edgeMap = new LinkedHashMap<>();
    }

    protected Graph(Collection<V> vertices) {
        this();
        for (V v : vertices) {
            addVertex(v);
        }
    }

    Graph(Graph<V> graph) {
        this(graph.getVertices());
        graph.getEdges().forEach(this::addEdge);
    }

    //================================================================================
    // Graph Builders
    //================================================================================

    //--------------------
    //  Abstract Methods
    //--------------------

    abstract Connection<V> obtainEdge();

    public abstract Graph<V> createNew();

    public abstract Algorithms<V> algorithms();

    //--------------------
    //  Public Methods
    //--------------------

    /**
     * Adds a vertex to the graph.
     *
     * @param v the vertex to be added
     * @return true if the vertex was not already in the graph, false otherwise
     */

    public boolean addVertex(V v) {
        return nodeMap.put(v) != null;
    }

    /**
     * Adds all the vertices in the collection to the graph.
     *
     * @param vertices a collection of vertices to be added
     */
    public void addVertices(Collection<V> vertices) {
        for (V v : vertices) {
            addVertex(v);
        }
    }

    public void addVertices(V... vertices) {
        for (V v : vertices) {
            addVertex(v);
        }
    }

    /**
     * Removes a vertex from the graph, and any adjacent edges.
     *
     * @param v the vertex to be removed
     * @return true if the vertex was in the graph, false otherwise
     */
    public boolean removeVertex(V v) {
        Node<V> existing = nodeMap.remove(v);
        if (existing == null) return false;
        for (int i = existing.getOutEdges().size() - 1; i >= 0; i--) {
            removeConnection(existing.getOutEdges().get(i).b, existing);
        }
        existing.disconnect();
        return true;
    }

    public void disconnect(V v) {
        Node<V> existing = nodeMap.get(v);
        if (existing == null) Errors.throwVertexNotInGraphVertexException(false);
        for (int i = existing.getOutEdges().size() - 1; i >= 0; i--) {
            removeConnection(existing.getOutEdges().get(i).b, existing);
        }
        existing.disconnect();
    }

    /**
     * Removes all the vertices in the collection from the graph, and any adjacent edges.
     *
     * @param vertices vertices a collection of vertices to be removed
     */
    public void removeVertices(Collection<V> vertices) {
        for (V v : vertices) {
            removeVertex(v);
        }
    }

    public void removeVertexIf(Predicate<V> predicate) {
        removeVertices(getVertices().stream().filter(predicate).collect(Collectors.toList()));
    }

    /**
     * Add an edge to the graph, from v to w. The edge will have a default weight of 1.
     * If there is already an edge between v and w, its weight will be set to 1.
     *
     * @param v the tail vertex of the edge
     * @param w the head vertex of the edge
     * @return the edge
     */
    public Connection<V> addEdge(V v, V w) {
        return addEdge(v, w, getDefaultEdgeWeightFunction());
    }

    /**
     * Add an edge to the graph, with the same endpoints as the given edge. If the endpoints are not in the graph they
     * will be added.
     * If there is already an edge between v and w, its weight will be set to the weight of given edge.
     *
     * @param edge an edge (possibly from another graph)
     * @return the edge belonging to this graph
     */
    public Connection<V> addEdge(Edge<V> edge) {
        addVertex(edge.getA());
        addVertex(edge.getB());
        return addEdge(edge.getA(), edge.getB(), edge.getWeightFunction());
    }

    /**
     * Add an edge to the graph, from v to w and with the specified weight.
     * If there is already an edge between v and w, its weight will be set to the specified weight.
     *
     * @param v      the tail vertex of the edge
     * @param w      the head vertex of the edge
     * @param weight the weight of the edge
     * @return the edge
     */
    public Connection<V> addEdge(V v, V w, float weight) {
        return addEdge(v, w, (a, b) -> weight);
    }

    /**
     * Add an edge to the graph, from v to w and with the specified weight.
     * If there is already an edge between v and w, its weight will be set to the specified weight.
     *
     * @param v              the tail vertex of the edge
     * @param w              the head vertex of the edge
     * @param weightFunction a function which will return the weight of the edge
     * @return the edge
     */
    public Connection<V> addEdge(V v, V w, WeightFunction<V> weightFunction) {
        if (v == null || w == null) Errors.throwNullVertexException();
        if (v.equals(w)) Errors.throwSameVertexException();
        Node<V> a = getNode(v);
        Node<V> b = getNode(w);
        if (a == null || b == null) Errors.throwVertexNotInGraphVertexException(true);
        return addConnection(a, b, weightFunction);
    }

    /**
     * Removes the edge from v to w from the graph.
     *
     * @param v the tail vertex of the edge
     * @param w the head vertex of the edge
     * @return the edge if there exists an edge from v to w, or null if there is no edge
     */
    public boolean removeEdge(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null || b == null) Errors.throwVertexNotInGraphVertexException(true);
        return removeConnection(a, b);
    }

    public boolean removeEdge(Edge<V> edge) {
        return removeConnection(edge.getInternalNodeA(), edge.getInternalNodeB());
    }

    public void removeEdges(Collection<Edge<V>> edges) {
        edges.forEach(e -> removeConnection(e.getInternalNodeA(), e.getInternalNodeB()));
    }

    public void removeEdgeIf(Predicate<Edge<V>> predicate) {
        removeEdges(getEdges().stream().filter(predicate).collect(Collectors.toList()));
    }

    /**
     * Removes all edges from the graph.
     */
    public void removeAllEdges() {
        for (Node<V> v : getNodes()) {
            v.disconnect();
        }
        edgeMap.clear();
    }

    /**
     * Removes all vertices and edges from the graph.
     */
    public void removeAllVertices() {
        edgeMap.clear();
        nodeMap.clear();
    }

    /**
     * Sort the vertices using the provided comparator. This is reflected in the iteration order of the collection returned
     * by {@link #getVertices()}, as well as algorithms which involve iterating over all vertices.
     *
     * @param comparator a comparator for comparing vertices
     */
    public void sortVertices(Comparator<V> comparator) {
        nodeMap.sort(comparator);
    }

    /**
     * Sort the edges using the provided comparator. This is reflected in the iteration order of the collection returned
     * by {@link #getEdges()}, as well as algorithms which involve iterating over all edges.
     *
     * @param comparator a comparator for comparing edges
     */
    public void sortEdges(final Comparator<Connection<V>> comparator) {
        List<Entry<Connection<V>, Connection<V>>> entryList = new ArrayList<>(edgeMap.entrySet());
        Collections.sort(entryList, Entry.comparingByKey(comparator));
        edgeMap.clear();
        for (Entry<Connection<V>, Connection<V>> entry : entryList) {
            edgeMap.put(entry.getKey(), entry.getValue());
        }
    }

    //--------------------
    //  Internal Methods
    //--------------------

    Connection<V> addConnection(Node<V> a, Node<V> b) {
        Connection<V> e = a.getEdge(b);
        return e != null ? e : addConnection(a, b, getDefaultEdgeWeightFunction());
    }

    Connection<V> addConnection(Node<V> a, Node<V> b, WeightFunction<V> weight) {
        Connection<V> e = a.getEdge(b);
        if (e == null) {
            e = obtainEdge();
            e.set(a, b, weight);
            a.addEdge(e);
            edgeMap.put(e, e);
        } else {
            e.setWeight(weight);
        }
        return e;
    }

    boolean removeConnection(Node<V> a, Node<V> b) {
        return removeConnection(a, b, true);
    }

    boolean removeConnection(Node<V> a, Node<V> b, boolean removeFromMap) {
        Connection<V> e = a.removeEdge(b);
        if (e == null) return false;
        if (removeFromMap) edgeMap.remove(e);
        return true;
    }

    //================================================================================
    // Getters
    //================================================================================

    //--------------------
    //  Public Getters
    //--------------------

    /**
     * Check if the graph contains a vertex.
     *
     * @param v the vertex with which to check
     * @return true if the graph contains the vertex, false otherwise
     */
    public boolean contains(V v) {
        return nodeMap.contains(v);
    }

    /**
     * Retrieve the edge which is from v to w.
     *
     * @param v the tail vertex of the edge
     * @param w the head vertex of the edge
     * @return the edge if it is in the graph, otherwise null
     */
    public Edge<V> getEdge(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null || b == null) Errors.throwVertexNotInGraphVertexException(true);
        Connection<V> edge = getEdge(a, b);
        if (edge == null) return null;
        return edge;
    }

    /**
     * Check if the graph contains an edge from v to w.
     *
     * @param v the tail vertex of the edge
     * @param w the head vertex of the edge
     * @return true if the edge is in the graph, false otherwise
     */
    public boolean edgeExists(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null || b == null) Errors.throwVertexNotInGraphVertexException(true);
        return connectionExists(a, b);
    }

    /**
     * Get a collection containing all the edges which have v as a tail.
     * That is, for every edge e in the collection, e = (v, u) for some vertex u.
     *
     * @param v the vertex which all edges will have as a tail
     * @return an unmodifiable collection of edges
     */
    public Collection<Edge<V>> getEdges(V v) {
        Node<V> node = getNode(v);
        if (node == null) return null;
        return Collections.unmodifiableCollection(node.getOutEdges());
    }

    /**
     * <p>Get a collection containing all the edges in the graph.</p>
     *
     * <p>Note that for an undirected graph, there is no guarantee on the order of the vertices.
     * For example if there exists and edge between u and v, the returned collection will contain
     * exactly one edge for which either edge.getA().equals(u) and edge.getB().equals(v), or
     * edge.getA().equals(v) and edge.getB().equals(u). See {@link Edge#hasEndpoints(Object, Object)}.</p>
     *
     * @return an unmodifiable collection of all the edges in the graph
     */
    public Collection<Edge<V>> getEdges() {
        return Collections.unmodifiableCollection(edgeMap.values());
    }

    /**
     * Get a collection containing all the vertices in the graph.
     *
     * @return an unmodifiable collection of all the vertices in the graph
     */
    public Collection<V> getVertices() {
        return nodeMap.vertexCollection;
    }


    /**
     * Check if the graph is directed, that is whether the edges form an ordered pair or a set.
     *
     * @return whether the graph is directed
     */
    public boolean isDirected() {
        return true;
    }

    /**
     * Get the number of vertices in the graph.
     *
     * @return the number of vertices
     */
    public int size() {
        return nodeMap.size;
    }

    /**
     * Get the number of edges in the graph.
     *
     * @return the number of edges
     */
    public int getEdgeCount() {
        return edgeMap.size();
    }


    public Internals<V> internals() {
        return internals;
    }

    /**
     * Get the current default edge weight function. If none has been set, the default is a function returning the constant value 1f.
     *
     * @return the current default edge weight function
     */
    public WeightFunction<V> getDefaultEdgeWeightFunction() {
        return defaultEdgeWeight;
    }

    /**
     * Set the default edge weight function, which will be given to every edge for which the edge weight function is not specified.
     * See {@link WeightFunction}.
     *
     * @param defaultEdgeWeight the edge weight function
     */
    public void setDefaultEdgeWeight(WeightFunction<V> defaultEdgeWeight) {
        this.defaultEdgeWeight = defaultEdgeWeight;
    }

    /**
     * Sets the default edge weight, which will be given to every edge for which the edge weight is not specified.
     * Note that this actually sets the default edge weight function to a constant function returning the specified weight.
     *
     * @param weight the fixed value of the edge weight
     */
    public void setDefaultEdgeWeight(float weight) {
        this.defaultEdgeWeight = (a, b) -> weight;
    }

    /**
     * @return whether the graph is connected
     */
    public boolean isConnected() {
        return numberOfComponents() == 1;
    }

    public int numberOfComponents() {
        AtomicInteger visited = new AtomicInteger(1), components = new AtomicInteger();
        while (visited.get() < size()) {
            components.incrementAndGet();
            algorithms().depthFirstSearch(getVertices().iterator().next(), v -> visited.incrementAndGet());
        }
        return components.get();
    }

    //--------------------
    //  Internal Getters
    //--------------------

    Node<V> getNode(V v) {
        return nodeMap.get(v);
    }

    Collection<Node<V>> getNodes() {
        return nodeMap.nodeCollection;
    }

    boolean connectionExists(Node<V> u, Node<V> v) {
        return u.getEdge(v) != null;
    }

    Connection<V> getEdge(Node<V> a, Node<V> b) {
        return a.getEdge(b);
    }


    @Override
    public String toString() {
        return (isDirected() ? "Directed" : "Undirected") + " graph with " +
                size() + " vertices and " + getEdgeCount() + " edges";
    }
}
