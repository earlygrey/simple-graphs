package space.earlygrey.simplegraphs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import space.earlygrey.simplegraphs.TestUtils.Vector2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlgorithmsTest {

    @Test
    public void shortestPathShouldBeCorrect() {
        int n = 5;
        Graph<Vector2> undirectedGraph = TestUtils.createGridGraph(n, false);
        Graph<Vector2> diGraph = TestUtils.createGridGraph(n, true);

        Vector2 start = new Vector2(0, 0), end = new Vector2(n - 1, n - 1);

        List<Vector2> path = undirectedGraph.findShortestPath(start, end);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, undirectedGraph));

        path = diGraph.findShortestPath(start, end);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, diGraph));

    }

    private static boolean pathIsConnected(List<Vector2> path, Graph<Vector2> graph) {
        for (int i = 0; i < path.size()-1; i++) {
            if (!graph.isConnected(path.get(i), path.get(i+1))) return false;
        }
        return true;
    }


    @Test
    public void graphShouldDetectCycles() {

        DirectedGraph<Integer> diGraph = new DirectedGraph<>();

        for (int i = 0; i < 6; i++) {
            diGraph.addVertex(i);
        }

        diGraph.addEdge(0, 1);
        diGraph.addEdge(1, 2);
        assertTrue(!diGraph.containsCycle());

        diGraph.addEdge(0,2);
        assertTrue(!diGraph.containsCycle());

        diGraph.addEdge(2,0);
        assertTrue(diGraph.containsCycle());
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

        List<Integer> results = graph.breadthFirstSearch(0);

        assertEquals(4, results.size());
        assertEquals(new Integer(0), results.get(0));
        assertEquals(new Integer(3), results.get(3));
    }

    @Test
    public void dfsShouldWork() {
        Graph<Integer> graph = createSearchGraph();

        List<Integer> results = graph.depthFirstSearch(0);

        assertEquals(4, results.size());
        assertEquals(new Integer(0), results.get(0));
        assertEquals(new Integer(3), results.get(2));
    }

    @Test
    public void topologicalSortShouldWork() {
        DirectedGraph<Integer> graph = new DirectedGraph<>();
        int n = 10;
        for (int i = 0; i < n; i++) graph.addVertex(i);
        for (int i = 0; i < n/2; i++) graph.addEdge(i, i+1);
        for (int i = n-1; i > n/2; i--) graph.addEdge(i, i-1);

        List<Integer> sort = graph.topologicalSort();
        assertEquals(graph.size(), sort.size());

        graph.removeAllEdges();

        for (int i = 0; i < n-1; i++) {
            graph.addEdge(i+1, i);
        }
        sort = graph.topologicalSort();
        List<Integer> correct = new ArrayList<>(n);
        for (int i = n-1; i >= 0 ; i--) correct.add(i);

        assertEquals(correct, sort);

        graph.addEdge(0, n-1);
        sort = graph.topologicalSort();
        assertEquals(0, sort.size());

    }

    @Test
    public void mwstShouldWork() {

        int n = 10;
        Graph<Object> graph = new UndirectedGraph<>();

        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                graph.addEdge(i, j);
            }
        }

        Graph<Object> mwst = graph.findMinimumWeightSpanningTree();
        assertEquals(n-1, mwst.getEdgeCount());
    }
}
