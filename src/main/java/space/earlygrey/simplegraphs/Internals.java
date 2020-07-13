package space.earlygrey.simplegraphs;

import java.util.Collection;

public class Internals<V> {

    final Graph<V> graph;

    Internals(Graph<V> graph) {
        this.graph = graph;
    }

    public Node<V> getNode(V v) {
        return graph.getNode(v);
    }

    public Collection<Node<V>> getNodes() {
        return graph.vertexMap.nodeCollection;
    }

    public Collection<Connection<V>> getConnections() {
        return graph.edgeMap.keySet();
    }


}
