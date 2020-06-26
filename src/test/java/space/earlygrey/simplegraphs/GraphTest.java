package space.earlygrey.simplegraphs;

import org.junit.Test;

import space.earlygrey.simplegraphs.TestUtils.Vector2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTest {


    @Test
    public void verticesCanBeAddedAndRemoved() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        int n = 10;

        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }

        assertEquals(n, graph.size());

        Integer i = 0;
        for (Integer v : graph.getVertices()) {
            assertEquals(i++, v);
            assertTrue(graph.contains(v));
        }

        for (int j = 0; j < n/2; j++) {
            boolean wasInGraph = graph.removeVertex(j);
            assertTrue(wasInGraph);
            assertTrue(!graph.contains(j));
        }

        assertEquals(n - n/2, graph.size());

        graph.removeAllVertices();
        assertEquals(0, graph.size());
    }

    @Test
    public void edgesCanBeAddedAndRemoved() {
        int n = 5;
        Graph<Vector2> undirectedGraph = TestUtils.createGridGraph(n, false);
        Graph<Vector2> diGraph = TestUtils.createGridGraph(n, true);

        int expectedUndirected = 2*n*(n-1), expectedDirected = 2 * 2*n*(n-1);
        assertEquals(expectedUndirected, undirectedGraph.getEdgeCount());
        assertEquals(expectedDirected, diGraph.getEdgeCount());

        int edgeCount = 0;
        for (Vector2 v : undirectedGraph.getVertices()) {
            edgeCount += undirectedGraph.getEdges(v).size();
        }
        assertEquals(expectedUndirected, edgeCount / 2); // counted each edge twice

        edgeCount = 0;
        for (Vector2 v : diGraph.getVertices()) {
            edgeCount += diGraph.getEdges(v).size();
        }
        assertEquals(expectedDirected, edgeCount);

        undirectedGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));
        undirectedGraph.removeEdge(new Vector2(0,0), new Vector2(0,1));
        diGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));
        diGraph.removeEdge(new Vector2(0,0), new Vector2(0,1));

        assertEquals(expectedUndirected-2, undirectedGraph.getEdgeCount());
        assertEquals(expectedDirected-2, diGraph.getEdgeCount());


        undirectedGraph.removeAllEdges();
        assertEquals(0, undirectedGraph.getEdgeCount());

    }

}
