package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class Graph<V extends Object> {

    //================================================================================
    // Members
    //================================================================================

    private final boolean directed;
    private final Collection<V> objects;
    private final Collection<Node<V>> vertices;
    private final Map<V, Node> vertexMap;

    private final Collection<Connection<V>> connections;
    private final Collection<Edge> edges;

    final Algorithms algorithms;

    //================================================================================
    // Constructors
    //================================================================================

    public Graph() {
        this(false);
    }

    public Graph(boolean directed) {
        this.directed = directed;
        algorithms = new Algorithms(this);
        objects =  new LinkedHashSet<>();
        vertices = new LinkedHashSet<>();
        vertexMap = new LinkedHashMap<>();
        connections = new LinkedHashSet<>();
        edges = new LinkedHashSet<>();
    }

    public Graph(boolean directed, Collection<V> vertices) {
        this(directed);
        for (V v : vertices) {
            addVertex(v);
        }
    }

    //================================================================================
    // Graph Builders
    //================================================================================

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

    public void removeVertex(V v) {
        Node existing = getNode(v);
        if (existing==null) return;
        else removeVertex(existing);
    }

    public void removeVertices(Collection<V> vertices) {
        for (V v : vertices) {
            removeVertex(v);
        }
    }

    public Edge<V> connect(V v, V w) {
        return connect(v, w, Connection.DEFAULT_WEIGHT);
    }
    public Edge<V> connect(V v, V w, float weight) {
        if (v == null || w == null) throw new IllegalArgumentException("Vertices cannot be null");
        if ( v.equals(w)) throw new UnsupportedOperationException("Self loops are not allowed");
        Node a = getNode(v);
        Node b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException("At least one vertex is not in the graph");
        Edge<V> edge = justConnect(a, b, weight).edge;
        if (!directed) justConnect(b, a, weight);
        return edge;
    }

    public Connection<V> disconnect(V v, V w) {
        if (!contains(v) || !contains(w))  return null;
        if (!isConnected(v,w)) return null;
        Node a = getNode(v);
        Node b = getNode(w);
        Connection<V> e = justDisonnect(a, b);
        if (!directed) justDisonnect(b, a);
        return e;
    }

    public void disconnectAll() {
        connections.clear();
        for (Node v : getNodes()) {
            v.disconnectAll();
        }
    }


    //------------------
    //  Internal Methods
    //------------------

    Node addVertexAndGetNode(V v) {
        Node existing = getNode(v);
        if (existing!=null) return existing;
        Node n = new Node(v, this);
        vertices.add(n);
        vertexMap.put(v, n);
        objects.add(v);
        return n;
    }

    void removeVertex (Node<V> node) {
        for (Node<V> neighbour : node.neighbours.keySet()) {
            neighbour.disconnect(node);
        }
        node.disconnectAll();
        vertices.remove(node);
        objects.remove(node.object);
    }

    Connection<V> justConnect(Node v, Node w, float weight) {
        Connection<V> e = v.connect(w, weight);
        if (e!=null) {
            connections.add(e);
            edges.add(e.edge);
            return e;
        } else {
            return v.getEdge(w);
        }
    }

    Connection<V> disconnect(Connection<V> connection) {
        Node a = connection.a;
        Node b = connection.b;
        Connection<V> e = justDisonnect(a, b);
        if (!directed) justDisonnect(b, a);
        return e;
    }

    Connection<V> justDisonnect(Node v, Node w) {
        Connection<V> e = v.disconnect(w);
        connections.remove(e);
        edges.remove(e.edge);
        return e;
    }

    void clear() {
        vertices.clear();
        connections.clear();
        edges.clear();
        vertexMap.clear();
        objects.clear();
    }


    //================================================================================
    // Getters
    //================================================================================

    //------------------
    //  Public Methods
    //------------------

    public boolean contains(V v) {
        return vertexMap.containsKey(v);
    }

    public Edge<V> getEdge(V v, V w) {
        if (!contains(v) || !contains(w)) return null;
        Node<V> nodeV = getNode(v), nodeW = getNode(w);
        Connection<V> connection = nodeV.getEdge(nodeW);
        if (connection == null && isDirected()) {
            connection = nodeW.getEdge(nodeV);
        }
        return connection.edge;
    }

    public Collection<Edge<V>> getEdges(V v) {
        Node<V> node = getNode(v);
        if (node==null) return null;
        return Collections.unmodifiableCollection(node.getEdges());
    }

    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    public Collection<V> getVertices() {
        return Collections.unmodifiableCollection(objects);
    }

    public boolean isDirected() {
        return directed;
    }

    public int size() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return directed?connections.size():(connections.size()/2);
    }

    public boolean isConnected(V u, V v) {
        return getEdge(u, v) != null;
    }

    boolean isConnected(Node<V> u, Node<V> v) {
        return isConnected(u.object, v.object);
    }

    //------------------
    //  Internal Methods
    //------------------

    Node getNode(V v) {
        return vertexMap.get(v);
    }


    Collection<Node<V>> getNodes() {
        return vertices;
    }

    Collection<Connection<V>> getConnections() {
        return connections;
    }

    Collection<Connection<V>> getConnections(V v) {
        return getNode(v).connections.values();
    }


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
        List<Node> nodeList = algorithms.findShortestPath(startNode, targetNode, heuristic);
        for (int i = 0, n = nodeList.size(); i < n; i++) {
            path.add((V) nodeList.get(i).object);
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
