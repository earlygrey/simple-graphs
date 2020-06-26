package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import space.earlygrey.simplegraphs.Connection.UndirectedConnection;

public class UndirectedGraph<V> extends Graph<V> {

    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(Collection<V> vertices) {
        super(vertices);
    }

    @Override
    Connection<V> createConnection(Node<V> a, Node<V> b, float weight) {
        return new UndirectedConnection<>(a, b, weight);
    }

    @Override
    Connection<V> addEdge(Node<V> a, Node<V> b, float weight) {
        Connection<V> e = a.addEdge(b, weight);
        edges.put(e.edge, e);
        b.addEdge(a, weight);
        return e; //return connection instance in edges map
    }

    @Override
    Connection<V> removeEdge(Node<V> a, Node<V> b) {
        Connection<V> e = a.removeEdge(b);
        if (e == null) return null;
        b.removeEdge(a);
        return edges.remove(e.edge);
    }

    @Override
    Connection<V> getConnection(Node<V> a, Node<V> b) {
        Connection<V> connection = a.getEdge(b);
        if (connection == null) return null;
        connection = edges.get(connection.edge); // get the instance of the Connection in the edge map
        return connection;
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    public List<V> findComponent (V v) {
        List<V> list = new ArrayList<>();
        Node node = getNode(v);
        if (node==null) return list;
        algorithms.findComponent(node, list, Float.MAX_VALUE);
        return list;
    }

}
