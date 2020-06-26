package space.earlygrey.simplegraphs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

class Node<T extends Object> {

    final Graph graph;
    public final T object;
    Map<Edge<T>, Connection<T>> connections = new LinkedHashMap<>();
    Map<Node<T>, Connection<T>> neighbours = new LinkedHashMap<>();

    Node (T object, Graph<T> graph) {
        this.object = object;
        this.graph = graph;
    }

    Collection<Edge<T>> getEdges() {
        return connections.keySet();
    }


    Connection<T> getEdge(Node v) {
        return neighbours.get(v);
    }

    Connection<T> addEdge(Node v, float weight) {
        Connection<T> connection = neighbours.get(v);
        if (connection == null) {
            connection = graph.createConnection(this, v, weight);
            connections.put(connection.edge, connection);
            neighbours.put(v, connection);
            return connection;
        } else {
            connection.setWeight(weight);
        }
        return connection;
    }
    Connection<T> removeEdge(Node v) {
        Connection<T> connection = neighbours.remove(v);
        if (connection == null) return null;
        connections.remove(connection.edge);
        return connection;
    }

    void disconnectAll() {
        neighbours.clear();
        connections.clear();
    }

    //util fields for algorithms, don't store data in them
    boolean visited;
    float distance;
    float estimate;
    Node prev;

    void resetAlgorithmAttribs() {
        visited = false;
        prev = null;
        distance = Float.MAX_VALUE;
        estimate = 0;
    }

    @Override
    public boolean equals(Object o) {
        /*if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return object.equals(node.object);*/
        return o == this;
    }

    @Override
    public int hashCode() {
        //return Objects.hashCode(object);
        return System.identityHashCode(this);
    }
}
