package space.earlygrey.simplegraphs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

class Node<V extends Object> {

    final Graph graph;
    public final V object;
    Map<Edge<V>, Connection<V>> connections = new LinkedHashMap<>();
    Map<Node<V>, Connection<V>> neighbours = new LinkedHashMap<>();

    Node (V object, Graph<V> graph) {
        this.object = object;
        this.graph = graph;
    }

    Collection<Edge<V>> getEdges() {
        return connections.keySet();
    }


    Connection<V> getEdge(Node v) {
        return neighbours.get(v);
    }

    Connection<V> addEdge(Node v, float weight) {
        Connection<V> connection = neighbours.get(v);
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
    Connection<V> removeEdge(Node v) {
        Connection<V> connection = neighbours.remove(v);
        if (connection == null) return null;
        connections.remove(connection.edge);
        return connection;
    }

    void disconnect() {
        neighbours.clear();
        connections.clear();
    }

    //util fields for algorithms, don't store data in them
    boolean visited, seen;
    float distance;
    float estimate;
    Node<V> prev;
    int i;

    void resetAlgorithmAttribs() {
        visited = false;
        prev = null;
        distance = Float.MAX_VALUE;
        estimate = 0;
        i = 0;
        seen = false;
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

    @Override
    public String toString() {
        return String.valueOf(object);
    }
}
