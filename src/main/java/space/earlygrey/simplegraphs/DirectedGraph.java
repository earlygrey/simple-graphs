package space.earlygrey.simplegraphs;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import space.earlygrey.simplegraphs.Connection.DirectedConnection;

public class DirectedGraph<V> extends Graph<V> {

    public DirectedGraph () {
        super();
    }

    public DirectedGraph (Collection<V> vertices) {
        super(vertices);
    }

    @Override
    protected Supplier<Connection<V>> getEdgeSupplier() {
        return () -> new DirectedConnection<>();
    }

    @Override
    Graph<V> createNew() {
        return new DirectedGraph<>();
    }

    /**
     * Sort the vertices of this graph in topological order. That is, for every edge from vertex u to vertex v, u comes before v in the ordering.
     * This is reflected in the iteration order of the collection returned by {@link #getVertices()}.
     * Note that the graph cannot contain any cycles for a topological order to exist. If a cycle exists, this method will do nothing.
     * @return true if the sort was successful, false if the graph contains a cycle
     */
    public boolean topologicalSort() {
        return algorithms.topologicalSort();
    }

    /**
     * Perform a topological sort on the graph, and puts the sorted vertices in the supplied list.
     * That is, for every edge from vertex u to vertex v, u will come before v in the supplied list.
     * Note that the graph cannot contain any cycles for a topological order to exist. If a cycle exists, the sorting procedure will
     * terminate and the supplied list will only contain the vertices up until the point of termination.
     * @return true if the sort was successful, false if the graph contains a cycle
     */
    public boolean topologicalSort(List<V> sortedVertices) {
        return algorithms.topologicalSort(sortedVertices);
    }

}
