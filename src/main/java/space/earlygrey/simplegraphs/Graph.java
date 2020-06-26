package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Graph<V> {

    //================================================================================
    // Members
    //================================================================================

    final Map<V, Node<V>> vertexMap;
    final Map<Edge<V>, Connection<V>> edges;

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

    //------------------
    //  Abstract Methods
    //------------------

    abstract Connection<V> createConnection(Node<V> a, Node<V> b, float weight);


    //------------------
    //  Public Methods
    //------------------

    public boolean addVertex(V v) {
        int n = size();
        addVertexAndGetNode(v);
        return size() > n;
    }

    public void addVertices(Collection<V> vertices) {
        for (V v : vertices) {
            addVertex(v);
        }
    }

    public boolean removeVertex(V v) {
        Node existing = getNode(v);
        if (existing==null) return false;
        removeVertex(existing);
        return true;
    }

    public void removeVertices(Collection<V> vertices) {
        for (V v : vertices) {
            removeVertex(v);
        }
    }

    public Edge<V> addEdge(V v, V w) {
        return addEdge(v, w, Connection.DEFAULT_WEIGHT);
    }

    public Edge<V> addEdge(V v, V w, float weight) {
        if (v == null || w == null) throw new IllegalArgumentException(NULL_VERTEX_MESSAGE);
        if ( v.equals(w)) throw new UnsupportedOperationException(SAME_VERTEX_MESSAGE);
        Node a = getNode(v);
        Node b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        return addEdge(a, b, weight).edge;
    }

    public Edge<V> removeEdge(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Connection<V> connection = removeEdge(a, b);
        return connection == null ? null : connection.edge;
    }

    public void disconnectAll() {
        for (Node v : getNodes()) {
            v.disconnectAll();
        }
        edges.clear();
    }

    //------------------
    //  Internal Methods
    //------------------

    Node addVertexAndGetNode(V v) {
        Node existing = getNode(v);
        if (existing!=null) return existing;
        Node n = new Node(v, this);
        //vertices.add(n);
        vertexMap.put(v, n);
        return n;
    }

    void removeVertex (Node<V> node) {
        for (Node<V> neighbour : node.neighbours.keySet()) {
            neighbour.removeEdge(node);
        }
        node.disconnectAll();
        vertexMap.remove(node.object);
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

    void clear() {
        edges.clear();
        vertexMap.clear();
    }

    //================================================================================
    // Getters
    //================================================================================

    //------------------
    //  Public Getters
    //------------------

    public boolean contains(V v) {
        return vertexMap.containsKey(v);
    }

    public Edge<V> getEdge(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Connection<V> connection = getConnection(a, b);
        if (connection == null) return null;
        return connection.edge;
    }

    Connection<V> getConnection(Node<V> a, Node<V> b) {
        Connection<V> connection = a.getEdge(b);
        if (connection == null) return null;
        return connection;
    }

    public Collection<Edge<V>> getEdges(V v) {
        Node<V> node = getNode(v);
        if (node==null) return null;
        return Collections.unmodifiableCollection(node.getEdges());
    }

    public Collection<Edge<V>> getEdges() {
        return Collections.unmodifiableCollection(edges.keySet());
    }

    public Collection<V> getVertices() {
        return Collections.unmodifiableCollection(vertexMap.keySet());
    }

    public boolean isDirected() {
        return true;
    }

    public int size() {
        return vertexMap.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public boolean isConnected(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        return isConnected(a, b);
    }

    //------------------
    //  Internal Getters
    //------------------

    Node getNode(V v) {
        return vertexMap.get(v);
    }

    Collection<Node<V>> getNodes() {
        return vertexMap.values();
    }

    boolean isConnected(Node<V> u, Node<V> v) {
        return u.getEdge(v) != null;
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

    public List<V> findShortestPath(V start, V target) {
        return findShortestPath(start, target, algorithms.defaultHeuristic);
    }

    public List<V> findShortestPath(V start, V target, Heuristic<V> heuristic) {
        List<V> list = new ArrayList<>();
        findShortestPath(start, target, list, heuristic);
        return list;
    }

    public boolean findShortestPath(V start, V target, List<V> path) {
        return findShortestPath(start, target, path, algorithms.defaultHeuristic);
    }

    public boolean findShortestPath(V start, V target, List<V> path, Heuristic<V> heuristic) {
        path.clear();
        Node startNode = getNode(start);
        Node targetNode = getNode(target);
        if (startNode==null || targetNode==null) return !path.isEmpty();
        List<Node<V>> nodeList = algorithms.findShortestPath(startNode, targetNode, heuristic);
        for (Node<V> v : nodeList) {
            path.add(v.object);
        }
        return !path.isEmpty();
    }

    public float findMinimumDistance(V start, V target) {
        return algorithms.findMinimumDistance(getNode(start), getNode(target));
    }

    /*public List<V> getComponent (V object, int max) {
        List<V> list = new ArrayList<>();
        Node node = getNode(object);
        if (node==null) return list;
        List<Node> nodeList = algorithms.getComponent(node, max);
        for (int i = 0, n = nodeList.size(); i < n; i++) {
            list.add((V) nodeList.get(i).object);
        }
        return list;
    }

    public boolean hasCycle() {
        return algorithms.containsCycle(this);
    }*/

}
