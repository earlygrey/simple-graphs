package space.earlygrey.simplegraphs;

import org.junit.Test;
import space.earlygrey.simplegraphs.TestUtils.Vector2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

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
            assertFalse(graph.contains(j));
        }

        assertEquals(n - n/2, graph.size());

        graph.removeAllVertices();
        assertEquals(0, graph.size());
    }

    @Test
    public void edgesCanBeAddedAndRemoved() {
        int n = 5;
        Graph<Vector2> undirectedGraph = TestUtils.makeGridGraph(new UndirectedGraph<>(), n);
        Graph<Vector2> diGraph = TestUtils.makeGridGraph(new DirectedGraph<>(), n);

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


    @Test
    public void verticesCanBeSorted() {
        Graph<Integer> graph = new UndirectedGraph<>();
        List<Integer> list = Arrays.asList(9,4,3,2,5,7,6,0,8,1);
        for (Integer i : list) {
            graph.addVertex(i);
        }
        graph.sortVertices(Comparator.comparing(v -> v));
        int i = 0;
        for (Integer vertex : graph.getVertices()) {
            assertEquals(Integer.valueOf(i++), vertex);
        }
    }

    @Test
    public void edgesCanBeSorted() {
        Graph<Integer> graph = new DirectedGraph<>();
        List<Integer> list = Arrays.asList(9,4,3,2,5,7,6,0,8,1);
        for (int j = 0; j < list.size(); j++) {
            graph.addVertex(j);
        }
        for (int j = 0; j < list.size(); j++) {
            graph.addEdge(list.get(j), list.get(list.size()-j-1));
        }
        graph.sortEdges(Comparator.comparing(Edge::getA));
        int i = 0;
        for (Edge<Integer> edge : graph.getEdges()) {
            assertEquals(Integer.valueOf(i++), edge.getA());
        }
    }
}
