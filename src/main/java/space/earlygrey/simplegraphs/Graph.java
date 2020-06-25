package space.earlygrey.simplegraphs;

import java.util.Collection;

import space.earlygrey.simplegraphs.Connection.UndirectedConnection;

public class Graph<V> extends AbstractGraph<V> {

    public Graph () {
        super();
    }

    public Graph (Collection<V> vertices) {
        super(vertices);
    }

    @Override
    Connection<V> createConnection(Node<V> u, Node<V> v, float weight) {
        return new UndirectedConnection<>(u, v, weight);
    }

    @Override
    public Edge<V> connect(V v, V w, float weight) {
        if (v == null || w == null) throw new IllegalArgumentException(NULL_VERTEX_MESSAGE);
        if ( v.equals(w)) throw new UnsupportedOperationException(SAME_VERTEX_MESSAGE);
        Node a = getNode(v);
        Node b = getNode(w);
        if (a == null  || b == null) throw new IllegalArgumentException(NOT_IN_GRAPH_MESSAGE);
        Edge<V> edge = justConnect(a, b, weight).edge;
        justConnect(b, a, weight);
        return edge;
    }

    @Override
    public Edge<V> disconnect(V v, V w) {
        if (!contains(v) || !contains(w))  return null;
        if (!isConnected(v,w)) return null;
        Node a = getNode(v);
        Node b = getNode(w);
        Edge<V> edge = justDisonnect(a, b).edge;
        justDisonnect(b, a);
        return edge;
    }

    @Override
    Connection<V> disconnect(Connection<V> connection) {
        Node a = connection.a;
        Node b = connection.b;
        Connection<V> e = justDisonnect(a, b);
        justDisonnect(b, a);
        return e;
    }

    @Override
    public boolean isDirected() {
        return false;
    }

}
