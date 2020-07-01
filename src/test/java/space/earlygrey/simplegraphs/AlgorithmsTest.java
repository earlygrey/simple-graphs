package space.earlygrey.simplegraphs;

import org.junit.Test;
import space.earlygrey.simplegraphs.TestUtils.Vector2;
import space.earlygrey.simplegraphs.utils.Heuristic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlgorithmsTest {

    @Test
    public void shortestPathShouldBeCorrect() {
        int n = 5;
        Graph<Vector2> undirectedGraph = TestUtils.makeGridGraph(new UndirectedGraph<>(), n);
        Graph<Vector2> diGraph = TestUtils.makeGridGraph(new DirectedGraph<>(), n);

        Vector2 start = new Vector2(0, 0), end = new Vector2(n - 1, n - 1);

        List<Vector2> path = undirectedGraph.findShortestPath(start, end);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, undirectedGraph));

        path = diGraph.findShortestPath(start, end);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, diGraph));
        
        Heuristic<Vector2> h = Vector2::dst;

        path = undirectedGraph.findShortestPath(start, end, h);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, undirectedGraph));

        path = diGraph.findShortestPath(start, end, h);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, diGraph));
    }

    private static boolean pathIsConnected(List<Vector2> path, Graph<Vector2> graph) {
        for (int i = 0; i < path.size()-1; i++) {
            if (!graph.edgeExists(path.get(i), path.get(i+1))) return false;
        }
        return true;
    }


    @Test
    public void cyclesShouldBeDetected() {

        Graph<Integer> graph = new DirectedGraph<>();

        for (int i = 0; i < 6; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        assertTrue(!graph.detectCycle());

        graph.addEdge(0,2);
        assertTrue(!graph.detectCycle());

        graph.addEdge(2,0);
        assertTrue(graph.detectCycle());

        graph = new UndirectedGraph<>();

        for (int i = 0; i < 6; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        assertTrue(!graph.detectCycle());

        graph.addEdge(0,2);
        assertTrue(graph.detectCycle());

    }
    
    private Graph<Integer> createSearchGraph() {
        Graph<Integer> graph = new UndirectedGraph<>();

        for (int i = 0; i < 5; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(0, 1);
        graph.addEdge(1, 3);
        graph.addEdge(0, 2);
        graph.addEdge(2, 3);
        
        return graph;
    }

    @Test
    public void bfsShouldWork() {
        Graph<Integer> graph = createSearchGraph();

        Graph<Integer> tree = graph.breadthFirstSearch(0);
        assertEquals(4, tree.size());
        assertEquals(3, tree.getEdgeCount());

        int count = 0;
        for (Integer vertex : tree.getVertices()) {
            switch(count) {
                case 0:
                    assertEquals(Integer.valueOf(0), vertex);
                    break;
                case 3:
                    assertEquals(Integer.valueOf(3), vertex);
                    break;
            }
            count++;
        }

    }

    @Test
    public void dfsShouldWork() {
        Graph<Integer> graph = createSearchGraph();

        Graph<Integer> tree = graph.depthFirstSearch(0);

        assertEquals(4, tree.size());
        assertEquals(3, tree.getEdgeCount());

        int count = 0;
        for (Integer vertex : tree.getVertices()) {
            switch(count) {
                case 0:
                    assertEquals(Integer.valueOf(0), vertex);
                    break;
                case 2:
                    assertEquals(Integer.valueOf(3), vertex);
                    break;
            }
            count++;
        }
    }

    @Test
    public void topologicalSortShouldWork() {
        DirectedGraph<Integer> graph = new DirectedGraph<>();
        int n = 10;
        for (int i = 0; i < n; i++) graph.addVertex(i);
        for (int i = 0; i < n-1; i++) graph.addEdge(i+1, i);

        List<Integer> sorted = new ArrayList<>(n);
        boolean success = graph.topologicalSort(sorted);
        assertTrue(success);

        graph.topologicalSort();
        int i = n-1;
        for (Integer vertex : graph.getVertices()) {
            Integer expected = Integer.valueOf(i--);
            assertEquals(expected, vertex);
            assertEquals(expected, sorted.get(n-2-i));
        }


        graph.addEdge(0, n-1);
        success = graph.topologicalSort();
        assertTrue(!success);

    }

    @Test
    public void mwstShouldBeTree() {

        int n = 4;
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        for (int i = 0; i < n; i++) graph.addVertex(i);
        GraphBuilder.buildCompleteGraph(graph);

        Graph<Integer> mwst = graph.findMinimumWeightSpanningTree();

        assertEquals(n, mwst.size());
        assertEquals(n-1, mwst.getEdgeCount());

        assertTrue(!mwst.detectCycle());
    }
}
