package space.earlygrey.simplegraphs;

import java.util.Collection;

import space.earlygrey.simplegraphs.Connection.UndirectedConnection;

public class UndirectedGraph<V> extends Graph<V> {

    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(Collection<V> vertices) {
        super(vertices);
    }

    @Override
    Connection<V> createConnection(Node<V> u, Node<V> v, float weight) {
        return new UndirectedConnection<>(u, v, weight);
    }

    @Override
    public Edge<V> addEdge(V v, V w, float weight) {
        if (v == null || w == null) throw new IllegalArgumentException(NULL_VERTEX_MESSAGE);
        if ( v.equals(w)) throw new UnsupportedOperationException(SAME_VERTEX_MESSAGE);
        Node a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Edge<V> edge = addEdge(a, b, weight).edge;
        addEdge(b, a, weight);
        return edge;
    }

    @Override
    public Edge<V> removeEdge(V v, V w) {
        if (!contains(v) || !contains(w))  return null;
        if (!isConnected(v,w)) return null;
        Node a = getNode(v);
        Node b = getNode(w);
        Edge<V> edge = removeEdge(a, b).edge;
        removeEdge(b, a);
        return edge;
    }

    @Override
    Connection<V> removeEdge(Connection<V> connection) {
        Node a = connection.a;
        Node b = connection.b;
        Connection<V> e = removeEdge(a, b);
        removeEdge(b, a);
        return e;
    }

    @Override
    public Edge<V> getEdge(V v, V w) {
        Node<V> a = getNode(v), b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Connection<V> connection = a.getEdge(b);
        connection = edges.get(connection.edge); // get the instance of the Connection in the edge map
        if (connection == null) return null;
        return connection.edge;
    }

    @Override
    public boolean isDirected() {
        return false;
    }

}
