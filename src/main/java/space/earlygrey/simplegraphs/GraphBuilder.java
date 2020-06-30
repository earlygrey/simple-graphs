package space.earlygrey.simplegraphs;

import java.util.Map.Entry;

public class GraphBuilder{

    private GraphBuilder() {
    }

    public static <V, G extends Graph<V>> void buildCompleteGraph(G graph) {
        for (Entry<V, Node<V>> entry1 : graph.vertexMap.entrySet()) {
            for (Entry<V, Node<V>> entry2 : graph.vertexMap.entrySet()) {
                Node<V> a = entry1.getValue(), b = entry2.getValue();
                if (!a.equals(b)) {
                    Connection<V> e = a.getEdge(b);
                    if (e == null) {
                        graph.addConnection(a, b);
                    }
                    if (graph.isDirected()) {
                        e = b.getEdge(a);
                        if (e == null) {
                            graph.addConnection(b, a);
                        }
                    }
                }
            }
        }
    }


}
