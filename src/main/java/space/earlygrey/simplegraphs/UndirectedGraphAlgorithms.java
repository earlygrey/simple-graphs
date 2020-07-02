package space.earlygrey.simplegraphs;

public class UndirectedGraphAlgorithms<V> extends Algorithms<V> {

    UndirectedGraphAlgorithms(UndirectedGraph<V> graph) {
        super(graph);
    }

    /**
     * Find a minimum weight spanning tree using Kruskal's algorithm.
     * @return a Graph object containing a minimum weight spanning tree (if this graph is connected -
     * in general a minimum weight spanning forest)
     */
    public Graph<V> findMinimumWeightSpanningTree() {
        return implementations.kruskalsMinimumWeightSpanningTree(true);
    }


}
