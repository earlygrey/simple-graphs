/*
MIT License

Copyright (c) 2020 earlygrey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package space.earlygrey.simplegraphs;

import org.junit.Test;

import java.util.List;

import space.earlygrey.simplegraphs.TestUtils.Vector2;
import space.earlygrey.simplegraphs.utils.Heuristic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlgorithmsTest {

    @Test
    public void shortestPathShouldBeCorrect() {
        int n = 5;
        Graph<Vector2> undirectedGraph = TestUtils.makeGridGraph(new UndirectedGraph<Vector2>(), n);
        Graph<Vector2> diGraph = TestUtils.makeGridGraph(new DirectedGraph<Vector2>(), n);

        Vector2 start = new Vector2(0, 0), end = new Vector2(n - 1, n - 1);

        List<Vector2> path = undirectedGraph.algorithms().findShortestPath(start, end);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, undirectedGraph));

        path = diGraph.algorithms().findShortestPath(start, end);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, diGraph));
        
        Heuristic<Vector2> h = new Heuristic<Vector2>() {
            @Override
            public float getEstimate(Vector2 currentNode, Vector2 targetNode) {
                return currentNode.dst(targetNode);
            }
        };

        path = undirectedGraph.algorithms().findShortestPath(start, end, h);
        assertEquals(2*(n-1) + 1, path.size());
        assertEquals(start, path.get(0));
        assertTrue(pathIsConnected(path, undirectedGraph));

        path = diGraph.algorithms().findShortestPath(start, end, h);
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
        assertTrue(!graph.algorithms().detectCycle());

        graph.addEdge(0,2);
        assertTrue(!graph.algorithms().detectCycle());

        graph.addEdge(2,0);
        assertTrue(graph.algorithms().detectCycle());

        graph = new UndirectedGraph<>();

        for (int i = 0; i < 6; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        assertTrue(!graph.algorithms().detectCycle());

        graph.addEdge(0,2);
        assertTrue(graph.algorithms().detectCycle());

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

        Graph<Integer> tree = graph.algorithms().breadthFirstSearch(0);
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

        Graph<Integer> tree = graph.algorithms().depthFirstSearch(0);

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

        graph.addEdge(9,8);
        graph.addEdge(6,7);
        assertTrue(graph.algorithms().topologicalSort());

        graph.removeAllEdges();

        for (int i = 0; i < n-1; i++) graph.addEdge(i+1, i);

        assertTrue(graph.algorithms().topologicalSort());
        int i = n-1;
        for (Integer vertex : graph.getVertices()) {
            Integer expected = Integer.valueOf(i--);
            assertEquals(expected, vertex);
        }


        graph.addEdge(n/2, n/2 + 1);
        boolean success = graph.algorithms().topologicalSort();
        assertTrue(!success);

        graph = new DirectedGraph<>();
        graph.addVertices(0, 1, 2, 3, 4, 5);
        graph.addEdge(2,0);
        graph.addEdge(1,2);
        graph.addEdge(4,1);
        graph.addEdge(4,2);
        graph.addEdge(3,5);
        assertTrue(graph.algorithms().topologicalSort());

        graph = new DirectedGraph<>();
        graph.addVertices(0, 1, 2, 3, 4, 5);
        graph.addEdge(2,0);
        graph.addEdge(1,2);
        graph.addEdge(4,1);
        graph.addEdge(4,2);
        graph.addEdge(3,5);

        graph.addEdge(2,4);
        assertTrue(!graph.algorithms().topologicalSort());
    }

    @Test
    public void mwstShouldBeTree() {

        int n = 4;
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        for (int i = 0; i < n; i++) graph.addVertex(i);
        GraphBuilder.buildCompleteGraph(graph);

        Graph<Integer> mwst = graph.algorithms().findMinimumWeightSpanningTree();

        assertEquals(n, mwst.size());
        assertEquals(n-1, mwst.getEdgeCount());

        assertTrue(!mwst.algorithms().detectCycle());
    }
}
