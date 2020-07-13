package space.earlygrey.simplegraphs;


public class GraphBuilder{

    private GraphBuilder() {
    }

    public static <V, G extends Graph<V>> void buildCompleteGraph(G graph) {
        for (Node<V> a : graph.vertexMap.nodeCollection) {
            for (Node<V> b : graph.vertexMap.nodeCollection) {
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
