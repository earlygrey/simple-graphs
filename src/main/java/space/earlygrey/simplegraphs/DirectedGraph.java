package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import space.earlygrey.simplegraphs.Connection.DirectedConnection;

public class DirectedGraph<V> extends Graph<V> {

    GraphBuilder<V, DirectedGraph<V>> builder = new GraphBuilder<>(this);

    public DirectedGraph () {
        super();
    }

    public DirectedGraph (Collection<V> vertices) {
        super(vertices);
    }

    @Override
    Connection<V> createConnection(Node<V> a, Node<V> b, float weight) {
        return new DirectedConnection<>(a, b, weight);
    }

    @Override
    Graph<V> createNew() {
        return new DirectedGraph<>();
    }

    public boolean topologicalSort() {
        return algorithms.topologicalSort();
    }

    public boolean topologicalSort(List<V> sortedVertices) {
        return algorithms.topologicalSort(sortedVertices);
    }

    @Override
    public GraphBuilder<V, DirectedGraph<V>> builder() {
        return builder;
    }

}
