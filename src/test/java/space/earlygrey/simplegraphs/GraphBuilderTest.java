package space.earlygrey.simplegraphs;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphBuilderTest {

    @Test
    public void completeGraphCanBeBuilt() {
        int n = 4;
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        for (int i = 0; i < n; i++) graph.addVertex(i);

        GraphBuilder.buildCompleteGraph(graph);

        assertEquals(n, graph.size());

        assertEquals(n*(n-1)/2, graph.getEdgeCount());

        DirectedGraph<Integer> digraph = new DirectedGraph<>();
        for (int i = 0; i < n; i++) digraph.addVertex(i);

        GraphBuilder.buildCompleteGraph(digraph);

        assertEquals(n, digraph.size());

        assertEquals(n*(n-1), digraph.getEdgeCount());
    }


}
