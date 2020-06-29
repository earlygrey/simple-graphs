package space.earlygrey.simplegraphs;

import java.util.Collection;
import java.util.function.Supplier;

import space.earlygrey.simplegraphs.Connection.UndirectedConnection;

public class UndirectedGraph<V> extends Graph<V> {

    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(Collection<V> vertices) {
        super(vertices);
    }

    @Override
    protected Supplier<Connection<V>> getConnectionSupplier() {
        return () -> new UndirectedConnection<>();
    }

    @Override
    Connection<V> addEdge(Node<V> a, Node<V> b, float weight) {
        Connection<V> e = a.addEdge(b, weight);
        edges.put(e.edge, e);
        b.addEdge(a, weight);
        return e; //return connection instance in edges map
    }

    @Override
    boolean removeEdge(Node<V> a, Node<V> b) {
        Connection<V> e = a.removeEdge(b);
        if (e == null) return false;
        b.removeEdge(a);
        edges.remove(e.edge);
        return true;
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

    /**
     * Find a minimum weight spanning tree using Kruskal's algorithm.
     * @return a Graph object containing a minimum weight spanning tree (if this graph is connected -
     * in general a minimum weight spanning forest)
     */
    public Graph<V> findMinimumWeightSpanningTree() {
        return algorithms.kruskalsMinimumWeightSpanningTree(true);
    }

    @Override
    Graph<V> createNew() {
        return new UndirectedGraph<>();
    }

}
