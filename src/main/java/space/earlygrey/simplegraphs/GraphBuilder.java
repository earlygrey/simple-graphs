package space.earlygrey.simplegraphs;

import java.util.Map.Entry;

public class GraphBuilder<V, G extends Graph<V>> {

    final G graph;

    GraphBuilder(G graph) {
        this.graph = graph;
    }

    public GraphBuilder<V, G> buildCompleteGraph() {

        for (Entry<V, Node<V>> entry1 : graph.vertexMap.entrySet()) {
            for (Entry<V, Node<V>> entry2 : graph.vertexMap.entrySet()) {
                Node<V> a = entry1.getValue(), b = entry2.getValue();
                if (!a.equals(b)) {
                    Connection<V> e = a.getEdge(b);
                    if (e == null) {
                        graph.addEdge(a, b);
                    }
                    if (graph.isDirected()) {
                        e = b.getEdge(a);
                        if (e == null) {
                            graph.addEdge(b, a);
                        }
                    }
                }
            }
        }
        return this;
    }


}
