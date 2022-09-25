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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import space.earlygrey.simplegraphs.TestUtils.Vector2;
import space.earlygrey.simplegraphs.utils.BadHashInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTest {


    @Test
    public void verticesCanBeAddedAndRemoved() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        int n = 16;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
            list.add(i);
        }

        assertEquals(n, graph.size());

        Integer counter = 0;
        for (Integer v : graph.getVertices()) {
            assertEquals(counter++, v);
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

        Collections.shuffle(list, new Random(123));

        for (Integer i : list) {
            assertTrue(graph.addVertex(i));
        }

        assertEquals(n, graph.size());

        counter = 0;
        for (Integer v : graph.getVertices()) {
            assertEquals(list.get(counter++), v);
            assertTrue(graph.contains(v));
        }

        for (int j = 0; j < n/2; j++) {
            assertFalse(graph.addVertex(list.get(j)));
        }

        graph.removeAllVertices();

        for (int j = 0; j < n/2; j++) {
            assertTrue(graph.addVertex(list.get(j)));
        }

        assertEquals(n - n/2, graph.size());


        graph = new UndirectedGraph<>(IntStream.range(0, n).mapToObj(i -> i).collect(Collectors.toList()));
        graph.removeVertexIf(i -> i % 2 == 0);

        for (int i = 0; i < n; i+=2) {
            assertFalse("Vertex not removed", graph.contains(i));
            list.add(i);
        }
        for (int i = 1; i < n; i+=2) {
            assertTrue("Vertex incorrectly removed", graph.contains(i));
            list.add(i);
        }


        UndirectedGraph<BadHashInteger> badGraph = new UndirectedGraph<>(IntStream.range(0, n).mapToObj(i -> new BadHashInteger(i)).collect(Collectors.toList()));
        badGraph.removeVertexIf(i -> i.value() % 2 == 0);

        for (int i = 0; i < n; i+=2) {
            assertFalse("Vertex not removed", badGraph.contains(new BadHashInteger(i)));
            list.add(i);
        }
        for (int i = 1; i < n; i+=2) {
            assertTrue("Vertex incorrectly removed", badGraph.contains(new BadHashInteger(i)));
            list.add(i);
        }
    }

    @Test
    public void vertexanBeRemovedFromDirectedGraph() {
        DirectedGraph<Integer> graph = new DirectedGraph<>();

        int n = 10;

        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }

        for (int i = 0; i < n-1; i++) {
            graph.addEdge(i, i+1);
        }

        assertEquals(n, graph.size());
        assertEquals(n-1, graph.getEdgeCount());

        graph.removeVertex(n/2);

        assertEquals(n-1, graph.size());
        assertEquals(n-3, graph.getEdgeCount());
    }

    @Test
    public void vertexCanBeDisconnectedFromDirectedGraph() {
        DirectedGraph<Integer> graph = new DirectedGraph<>();

        int n = 10;

        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }

        for (int i = 0; i < n-1; i++) {
            graph.addEdge(i, i+1);
        }

        assertEquals(n, graph.size());
        assertEquals(n-1, graph.getEdgeCount());

        graph.disconnect(n/2);

        assertEquals(n, graph.size());
        assertEquals(n-3, graph.getEdgeCount());
    }

    @Test
    public void edgesCanBeAddedAndRemoved() {
        int n = 5;
        Graph<Vector2> undirectedGraph = TestUtils.makeGridGraph(new UndirectedGraph<>(), n);
        Graph<Vector2> diGraph = TestUtils.makeGridGraph(new DirectedGraph<>(), n);

        int expectedUndirected = 2*n*(n-1), expectedDirected = 2 * 2*n*(n-1);
        assertEquals("Expected edge count not correct", expectedUndirected, undirectedGraph.getEdgeCount());
        assertEquals("Expected edge count not correct", expectedDirected, diGraph.getEdgeCount());

        int edgeCount = 0;
        for (Vector2 v : undirectedGraph.getVertices()) {
            edgeCount += undirectedGraph.getEdges(v).size();
        }
        assertEquals("Expected edge count not correct", expectedUndirected, edgeCount / 2); // counted each edge twice

        edgeCount = 0;
        for (Vector2 v : diGraph.getVertices()) {
            edgeCount += diGraph.getEdges(v).size();
        }
        assertEquals("Expected edge count not correct", expectedDirected, edgeCount);

        undirectedGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));
        undirectedGraph.removeEdge(new Vector2(0,0), new Vector2(0,1));
        diGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));
        diGraph.removeEdge(new Vector2(0,0), new Vector2(0,1));

        assertEquals("Two edges were not removed", expectedUndirected-2, undirectedGraph.getEdgeCount());
        assertEquals("Two edges were not removed", expectedDirected-2, diGraph.getEdgeCount());


        undirectedGraph.removeAllEdges();
        assertEquals("Not all edges removed", 0, undirectedGraph.getEdgeCount());
        diGraph.removeAllEdges();
        assertEquals("Not all edges removed", 0, diGraph.getEdgeCount());


        // check removing single edge functions appropriatedly for directed and undirected graphs
        undirectedGraph = TestUtils.makeGridGraph(new UndirectedGraph<>(), n);
        diGraph = TestUtils.makeGridGraph(new DirectedGraph<>(), n);

        undirectedGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));
        diGraph.removeEdge(new Vector2(0,0), new Vector2(1,0));

        assertFalse("Edge not removed", undirectedGraph.edgeExists(new Vector2(0,0), new Vector2(1,0)));
        assertFalse("Edge not removed", undirectedGraph.edgeExists(new Vector2(1,0), new Vector2(0,0)));
        assertFalse("Edge not removed", diGraph.edgeExists(new Vector2(0,0), new Vector2(1,0)));
        assertTrue("Both directed edges removed", diGraph.edgeExists(new Vector2(1,0), new Vector2(0,0)));

        // check removeEdgeIf
        undirectedGraph = TestUtils.makeGridGraph(new UndirectedGraph<>(), n);
        diGraph = TestUtils.makeGridGraph(new DirectedGraph<>(), n);

        Vector2 v1 = new Vector2(1,1), v2 = new Vector2(2,1);
        undirectedGraph.removeEdgeIf(e -> e.hasEndpoint(v1) || e.hasEndpoint(v2));
        diGraph.removeEdgeIf(e -> e.getA().equals(v1) || e.getA().equals(v2));

        assertEquals("Undirected eges not removed via removeEdgeIf", expectedUndirected - 7, undirectedGraph.getEdgeCount() );
        assertEquals("Directed edges not removed via removeEdgeIf", expectedDirected - 8, diGraph.getEdgeCount());

        for (Edge<Vector2> e : undirectedGraph.getEdges()) {
            assertFalse("Edge not removed via removeEdgeIf", e.hasEndpoint(v1) || e.hasEndpoint(v2));
        }
        for (Edge<Vector2> e : diGraph.getEdges()) {
            assertFalse("Edge not removed via removeEdgeIf", e.getA().equals(v1) || e.getA().equals(v2));
        }

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
