package space.earlygrey.simplegraphs;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTest {


    @Test
    public void verticesCanBeAddedAndRemoved() {
        Graph<Integer> graph = new Graph<>();
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

    }

    private static AbstractGraph<Vector2> createGridGraph(int n, boolean directed) {
        AbstractGraph<Vector2> graph = directed ? new DirectedGraph<>() : new Graph<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Vector2 v = new Vector2(i, j);
                graph.addVertex(v);
            }
        }

        int e = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i<n-1) {
                    Vector2 v1 = new Vector2(i, j), v2 = new Vector2(i+1,j);
                    graph.addEdge(v1, v2, v1.dst(v2));
                    if (directed) graph.addEdge(v2, v1, v1.dst(v2));
                    e++;
                }
                if (j<n-1) {
                    Vector2 v1 = new Vector2(i, j), v2 = new Vector2(i,j+1);
                    graph.addEdge(v1, v2, v1.dst(v2));
                    if (directed) graph.addEdge(v2, v1, v1.dst(v2));
                    e++;
                }
            }
        }
        return graph;
    }

    @Test
    public void edgesCanBeAddedAndRemoved() {
        int n = 5;
        AbstractGraph<Vector2> undirectedGraph = createGridGraph(n, false);
        AbstractGraph<Vector2> diGraph = createGridGraph(n, true);

        int expectedUndirected = 2*n*(n-1), expectedDirected = 2 * 2*n*(n-1);
        assertEquals(expectedUndirected, undirectedGraph.getEdgeCount());
        assertEquals(expectedDirected, diGraph.getEdgeCount());

        System.out.println(undirectedGraph.getEdgeCount());
        System.out.println(diGraph.getEdgeCount());

        undirectedGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));
        undirectedGraph.removeEdge(new Vector2(0,0), new Vector2(0,1));
        diGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));
        diGraph.removeEdge(new Vector2(0,0), new Vector2(0,1));

        assertEquals(expectedUndirected-2, undirectedGraph.getEdgeCount());
        assertEquals(expectedDirected-2, diGraph.getEdgeCount());

    }

    @Test
    public void shortestPathShouldBeCorrectLength() {
        int n = 5;
        AbstractGraph<Vector2> undirectedGraph = createGridGraph(n, false);
        AbstractGraph<Vector2> diGraph = createGridGraph(n, true);

        List<Vector2> path = undirectedGraph.findShortestPath(new Vector2(0, 0), new Vector2(n - 1, n - 1));
        assertEquals(2*(n-1) + 1, path.size());

        path = diGraph.findShortestPath(new Vector2(0, 0), new Vector2(n - 1, n - 1));
        assertEquals(2*(n-1) + 1, path.size());
    }

}
