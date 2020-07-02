package space.earlygrey.simplegraphs;

import java.util.Collection;
import java.util.function.Supplier;

import space.earlygrey.simplegraphs.Connection.DirectedConnection;

public class DirectedGraph<V> extends Graph<V> {

    final DirectedGraphAlgorithms<V> algorithms;

    //================================================================================
    // Constructors
    //================================================================================

    public DirectedGraph () {
        super();
        algorithms = new DirectedGraphAlgorithms<>(this);
    }

    public DirectedGraph (Collection<V> vertices) {
        super(vertices);
        algorithms = new DirectedGraphAlgorithms<>(this);
    }


    //================================================================================
    // Superclass implementations
    //================================================================================

    @Override
    protected Supplier<Connection<V>> getEdgeSupplier() {
        return () -> new DirectedConnection<>();
    }

    @Override
    Graph<V> createNew() {
        return new DirectedGraph<>();
    }

    @Override
    public DirectedGraphAlgorithms<V> algorithms() {
        return algorithms;
    }

}
