package space.earlygrey.simplegraphs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


class Node<V> implements Suppliable {

    final Graph<V> graph;
    V object;
    int index;
    boolean isFree;
    Map<Edge<V>, Connection<V>> connections = new LinkedHashMap<>();
    Map<Node<V>, Connection<V>> neighbours = new LinkedHashMap<>();
    Array<Connection<V>> outEdges = new Array<>();

    Node (Graph<V> graph) {
        this.graph = graph;
    }

    public void free() {
        object = null;
        isFree = true;
    }

    Collection<Edge<V>> getEdges() {
        return connections.keySet();
    }


    Connection<V> getEdge(Node<V> v) {
        return neighbours.get(v);
    }

    Connection<V> addEdge(Node<V> v, float weight) {
        Connection<V> connection = neighbours.get(v);
        if (connection == null) {
            connection = graph.connections.getEdge(this, v, weight);
            connections.put(connection.edge, connection);
            neighbours.put(v, connection);
            outEdges.add(connection);
            return connection;
        } else {
            connection.setWeight(weight);
        }
        return connection;
    }
    Connection<V> removeEdge(Node<V> v) {
        Connection<V> connection = neighbours.remove(v);
        if (connection == null) return null;
        connections.remove(connection.edge);
        outEdges.remove(connection);
        return connection;
    }

    void disconnect() {
        neighbours.clear();
        connections.clear();
        outEdges.clear();
    }

    //util fields for algorithms, don't store data in them
    boolean visited, seen;
    float distance;
    float estimate;
    Node<V> prev;
    int i;
    //FibonacciHeap.Entry<Node<V>> entry;

    void resetAlgorithmAttribs() {
        visited = false;
        prev = null;
        distance = Float.MAX_VALUE;
        estimate = 0;
        i = 0;
        seen = false;
        //entry = null;
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public String toString() {
        return "["+object + " (" +index +")]";
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public boolean isFree() {
        return isFree;
    }

    @Override
    public void setIndex(int i) {
        this.index = i;
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }
}
