package space.earlygrey.simplegraphs;

public class Internals<V> {

    final Graph<V> graph;

    Internals(Graph<V> graph) {
        this.graph = graph;
    }

    public Node<V> getNode(V v) {
        return graph.getNode(v);
    }


}
