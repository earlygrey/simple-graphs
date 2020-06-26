package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import space.earlygrey.simplegraphs.Connection.DirectedConnection;

public class DirectedGraph<V> extends Graph<V> {

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

    public boolean containsCycle() {
        return algorithms.containsCycle(this);
    }

    public List<V> topologicalSort() {
        List<V> vertices= new ArrayList<>();
        boolean success = algorithms.topologicalSort(vertices);
        if (!success) vertices.clear();
        return vertices;
    }

}
