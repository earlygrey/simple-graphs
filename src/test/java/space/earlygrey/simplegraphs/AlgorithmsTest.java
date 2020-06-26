package space.earlygrey.simplegraphs;

import org.junit.Test;

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

    @Test
    public void graphShouldFindComponent() {

        UndirectedGraph<Integer> graph = new UndirectedGraph<>();

        for (int i = 0; i < 6; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        graph.addEdge(4,3);

        List<Integer> component = graph.findComponent(0);
        assertEquals(3, component.size());

        component = graph.findComponent(3);
        assertEquals(2, component.size());

        component = graph.findComponent(5);
        assertEquals(1, component.size());
    }

}
