package space.earlygrey.simplegraphs;

import java.util.Collection;

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


}
