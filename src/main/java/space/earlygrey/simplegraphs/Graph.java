package space.earlygrey.simplegraphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Graph<V> {

    //================================================================================
    // Members
    //================================================================================

    final Map<V, Node<V>> vertexMap;
    final Map<Edge<V>, Connection<V>> edges;
    final List<Node<V>> nodeList;

    final ArrayDeque<Integer> freeIndexStack;
    int largestUsedIndex = 0;

    final Algorithms algorithms;

    static final String
            NULL_VERTEX_MESSAGE = "Vertices cannot be null",
            SAME_VERTEX_MESSAGE = "Self loops are not allowed",
            NOT_IN_GRAPH_MESSAGE = "At least one vertex is not in the graph";

    //================================================================================
    // Constructors
    //================================================================================

    protected Graph() {
        algorithms = new Algorithms(this);
        vertexMap = new LinkedHashMap<>();
        edges = new LinkedHashMap<>();
        nodeList = new ArrayList<>();
        freeIndexStack = new ArrayDeque<>();
    }

    protected Graph(Collection<V> vertices) {
        this();
        for (V v : vertices) {
            addVertex(v);
        }
    }

    //================================================================================
    // Graph Builders
    //================================================================================

    //--------------------
    //  Abstract Methods
    //--------------------

    abstract Connection<V> createConnection(Node<V> a, Node<V> b, float weight);
    abstract Graph<V> createNew();
    protected abstract <G extends Graph<V>, B extends GraphBuilder<V, G>> B builder();

    //--------------------
    //  Public Methods
    //--------------------

    /**
     * Adds a vertex to the graph.
     * @param v the vertex to be added
     * @return true if the vertex was not already in the graph, false otherwise
     */

    public boolean addVertex(V v) {
        Node node = getNode(v);
        if (node!=null) return false;
        if (freeIndexStack.size() > 0) {
            int index = freeIndexStack.pop();
            node = nodeList.get(index);
            node.object = v;
        } else {
            int index = nodeList.size();
            node = new Node(v, this, index);
            nodeList.add(node);
            largestUsedIndex = Math.max(index, largestUsedIndex);
            algorithms.ensureCapacity(largestUsedIndex+1);
        }
        vertexMap.put(v, node);
        return true;
    }

    /**
     * Adds all the vertices in the collection to the graph.
     * @param vertices a collection of vertices to be added
     */
    public void addVertices(Collection<V> vertices) {
        for (V v : vertices) {
            addVertex(v);
        }
    }

    /**
     * Removes a vertex from the graph, and any adjacent edges.
     * @param v the vertex to be removed
     * @return true if the vertex was in the graph, false otherwise
     */
    public boolean removeVertex(V v) {
        Node existing = getNode(v);
        if (existing==null) return false;
        removeVertex(existing);
        return true;
    }

    /**
     * Removes all the vertices in the collection from the graph, and any adjacent edges.
     * @param vertices vertices a collection of vertices to be removed
     */
    public void removeVertices(Collection<V> vertices) {
        for (V v : vertices) {
            removeVertex(v);
        }
    }

    /**
     * Add an edge to the graph, from v to w. The edge will have a default weight of 1.
     * If there is already an edge between v and w, its weight will be set to 1.
     * @param v the source vertex of the edge
     * @param w the destination vertex of the edge
     * @return the edge
     */
    public Edge<V> addEdge(V v, V w) {
        return addEdge(v, w, Connection.DEFAULT_WEIGHT);
    }

    /**
     * Add an edge to the graph, from v to w and with the specified weight.
     * If there is already an edge between v and w, its weight will be set to the specified weight.
     * @param v the source vertex of the edge
     * @param w the destination vertex of the edge
     * @param weight the weight of the edge
     * @return the edge
     */
    public Edge<V> addEdge(V v, V w, float weight) {
        if (v == null || w == null) throw new IllegalArgumentException(NULL_VERTEX_MESSAGE);
        if (v.equals(w)) throw new UnsupportedOperationException(SAME_VERTEX_MESSAGE);
        Node a = getNode(v);
        Node b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        return addEdge(a, b, weight).edge;
    }

    /**
     * Removes the edge from v to w from the graph.
     * @param v the source vertex of the edge
     * @param w the destination vertex of the edge
     * @return the edge if there exists an edge from v to w, or null if there is no edge
     */
    public Edge<V> removeEdge(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Connection<V> connection = removeEdge(a, b);
        return connection == null ? null : connection.edge;
    }

    /**
     * Removes all edges from the graph.
     */
    public void removeAllEdges() {
        for (Node v : getNodes()) {
            v.disconnect();
        }
        edges.clear();
    }

    /**
     * Removes all vertices and edges from the graph.
     */
    public void removeAllVertices() {
        edges.clear();
        vertexMap.clear();
        freeIndexStack.clear();
    }

    /**
     * Sort the vertices using the provided comparator. This affects the iteration order of the collection returned by {@link #getVertices()},
     * as well as algorithms which involve iterating over all vertices.
     * @param comparator a comparator for comparing vertices
     */
    public void sortVertices(Comparator<V> comparator) {
        List<Entry<V, Node<V>>> entryList = new ArrayList<>(vertexMap.entrySet());
        Collections.sort(entryList, Entry.comparingByKey(comparator));
        vertexMap.clear();
        for (Entry<V, Node<V>> entry : entryList) {
            vertexMap.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sort the edges using the provided comparator. This affects the iteration order of the collection returned by {@link #getEdges()},
     * as well as algorithms which involve iterating over all edges.
     * @param comparator a comparator for comparing edges
     */
    public void sortEdges(Comparator<Edge<V>> comparator) {
        List<Entry<Edge<V>, Connection<V>>> entryList = new ArrayList<>(edges.entrySet());
        Collections.sort(entryList, Entry.comparingByKey(comparator));
        edges.clear();
        for (Entry<Edge<V>, Connection<V>> entry : entryList) {
            edges.put(entry.getKey(), entry.getValue());
        }
    }

    //--------------------
    //  Internal Methods
    //--------------------

    void removeVertex (Node<V> node) {
        for (Node<V> neighbour : node.neighbours.keySet()) {
            neighbour.removeEdge(node);
        }
        node.disconnect();
        vertexMap.remove(node.object);
        freeIndexStack.push(node.index);
    }

    Connection<V> addEdge(Node<V> a, Node<V> b) {
        Connection<V> e = a.addEdge(b, Connection.DEFAULT_WEIGHT);
        edges.put(e.edge, e);
        return e;
    }

    Connection<V> addEdge(Node<V> a, Node<V> b, float weight) {
        Connection<V> e = a.addEdge(b, weight);
        edges.put(e.edge, e);
        return e;
    }

    Connection<V> removeEdge(Connection<V> connection) {
        return removeEdge(connection.a, connection.b);
    }

    Connection<V> removeEdge(Node<V> a, Node<V> b) {
        Connection<V> e = a.removeEdge(b);
        if (e == null) return null;
        return edges.remove(e.edge);
    }

    //================================================================================
    // Getters
    //================================================================================

    //--------------------
    //  Public Getters
    //--------------------

    /**
     * Check if the graph contains a vertex.
     * @param v the vertex with which to check
     * @return true if the graph contains the vertex, false otherwise
     */
    public boolean contains(V v) {
        return vertexMap.containsKey(v);
    }

    /**
     * Retrieve the edge which is from v to w.
     * @param v the source vertex of the edge
     * @param w the destination vertex of the edge
     * @return the edge if it is in the graph, otherwise null
     */
    public Edge<V> getEdge(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Connection<V> connection = getConnection(a, b);
        if (connection == null) return null;
        return connection.edge;
    }

    /**
     * Check if the graph contains an edge from v to w.
     * @param v the source vertex of the edge
     * @param w the destination vertex of the edge
     * @return true if the edge is in the graph, false otherwise
     */
    public boolean isConnected(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        return isConnected(a, b);
    }

    /**
     * Get a collection containing all the edges which have v as a source.
     * @param v the source vertex of all the edges
     * @return an unmodifiable collection of edges
     */
    public Collection<Edge<V>> getEdges(V v) {
        Node<V> node = getNode(v);
        if (node==null) return null;
        return Collections.unmodifiableCollection(node.getEdges());
    }

    /**
     * Get a collection containing all the edges in the graph.
     * @return an unmodifiable collection of all the edges in the graph
     */
    public Collection<Edge<V>> getEdges() {
        return Collections.unmodifiableCollection(edges.keySet());
    }

    /**
     * Get a collection containing all the vertices in the graph.
     * @return an unmodifiable collection of all the vertices in the graph
     */
    public Collection<V> getVertices() {
        return Collections.unmodifiableCollection(vertexMap.keySet());
    }

    /**
     * Check if the graph is directed, that is whether the edges form an ordered pair or a set.
     * @return whether the graph is directed
     */
    public boolean isDirected() {
        return true;
    }

    /**
     * Get the number of vertices in the graph.
     * @return the number of vertices
     */
    public int size() {
        return vertexMap.size();
    }

    /**
     * Get the number of edges in the graph.
     * @return the number of edges
     */
    public int getEdgeCount() {
        return edges.size();
    }



    //--------------------
    //  Internal Getters
    //--------------------

    Node getNode(V v) {
        return vertexMap.get(v);
    }

    Collection<Node<V>> getNodes() {
        return vertexMap.values();
    }

    boolean isConnected(Node<V> u, Node<V> v) {
        return u.getEdge(v) != null;
    }

    Connection<V> getConnection(Node<V> a, Node<V> b) {
        Connection<V> connection = a.getEdge(b);
        if (connection == null) return null;
        return connection;
    }

    /*Map<Edge<V>, Connection<V>> getEdgeMap() {
        return edges;
    }

    Collection<Connection<V>> getConnections(V v) {
        return getNode(v).connections.values();
    }*/


    //================================================================================
    // Algorithms API
    //================================================================================

    //--------------------
    //  Shortest Path
    //--------------------

    /**
     * Find the shortest path between the start and target vertices, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices
     */
    public List<V> findShortestPath(V start, V target) {
        return findShortestPath(start, target, null);
    }

    /**
     * Find the shortest path between the start and target vertices, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices
     */
    public List<V> findShortestPath(V start, V target, Heuristic<V> heuristic) {
        List<V> list = new ArrayList<>();
        findShortestPath(start, target, list, heuristic);
        return list;
    }

    /*public boolean findShortestPath(V start, V target, List<V> path) {
        return findShortestPath(start, target, path, null);
    }*/

    /**
     * Find the shortest path between the start and target vertices, using the A* search algorithm with the provided heuristic, and implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @param path the list of vertices to which the path vertices should be added
     * @return a list of vertices from start to target containing the ordered vertices of a shortest path, including both the start and target vertices
     */
    public boolean findShortestPath(V start, V target, List<V> path, Heuristic<V> heuristic) {
        path.clear();
        Node startNode = getNode(start);
        Node targetNode = getNode(target);
        if (startNode==null || targetNode==null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        algorithms.findShortestPath(startNode, targetNode, path, heuristic);
        return !path.isEmpty();
    }

    /**
     * Find the shortest path between the start and target vertices, using Dijkstra's algorithm implemented with a priority queue.
     * @param start the starting vertex
     * @param target the target vertex
     * @return the sum of the weights in a shortest path from the starting vertex to the target vertex
     */
    public float findMinimumDistance(V start, V target) {
        return algorithms.findMinimumDistance(getNode(start), getNode(target));
    }

    //--------------------
    // Graph Searching
    //--------------------

    /**
     * Perform a breadth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @param maxVertices the maximum number of vertices to process before terminating the search
     * @param maxDepth the maximum edge distance (the number of edges in a shortest path between vertices) a vertex should have to be
     *                 considered for processing. If a vertex has a distance larger than the maxDepth, it will not be added to the
     *                 returned graph
     * @return a Graph object containing all the processed vertices, and the edges from which each vertex was encountered.
     * The vertices and edges in the returned graph will be in the order they were encountered in the search, and this will be
     * reflected in the iteration order of the collections returned by {@link #getVertices()} and {@link #getEdges()}.
     */
    public Graph<V> breadthFirstSearch(V v, int maxVertices, int maxDepth) {
        Node node = getNode(v);
        if (node==null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Graph<V> tree = createNew();
        algorithms.breadthFirstSearch(node, tree, maxVertices, maxDepth);
        return tree;
    }

    /**
     * Perform a breadth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @return a Graph object containing all the processed vertices (all the vertices in this graph), and the edges from which each vertex was encountered.
     * The vertices and edges in the returned graph will be in the order they were encountered in the search, and this will be
     * reflected in the iteration order of the collections returned by {@link #getVertices()} and {@link #getEdges()}.
     */
    public Graph<V> breadthFirstSearch(V v) {
        return breadthFirstSearch(v, size(), size());
    }

    /**
     * Perform a depth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @param maxVertices the maximum number of vertices to process before terminating the search
     * @param maxDepth the maximum edge distance (the number of edges in a shortest path between vertices) a vertex should have to be
     *                 considered for processing. If a vertex has a distance larger than the maxDepth, it will not be added to the
     *                 returned graph
     * @return a Graph object containing all the processed vertices, and the edges from which each vertex was encountered.
     * The vertices and edges in the returned graph will be in the order they were encountered in the search, and this will be
     * reflected in the iteration order of the collections returned by {@link #getVertices()} and {@link #getEdges()}.
     */
    public Graph<V> depthFirstSearch(V v, int maxVertices, int maxDepth) {
        Node node = getNode(v);
        if (node==null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Graph<V> tree = createNew();
        algorithms.depthFirstSearch(node, tree, maxVertices, maxDepth);
        return tree;
    }

    /**
     * Perform a depth first search starting from the specified vertex.
     * @param v the vertex at which to start the search
     * @return a Graph object containing all the processed vertices (all the vertices in this graph), and the edges from which each vertex was encountered.
     * The vertices and edges in the returned graph will be in the order they were encountered in the search, and this will be
     * reflected in the iteration order of the collections returned by {@link #getVertices()} and {@link #getEdges()}.
     */
    public Graph<V> depthFirstSearch(V v) {
        return depthFirstSearch(v, size(), size());
    }


    //--------------------
    //  Structures
    //--------------------

    /**
     * Checks whether there are any cycles in the graph using depth first searches.
     * @return true if the graph contains a cycle, false otherwise
     */
    public boolean detectCycle() {
        return algorithms.containsCycle(this);
    }

}
