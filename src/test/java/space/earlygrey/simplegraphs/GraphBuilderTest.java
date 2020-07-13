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

import static org.junit.Assert.assertEquals;

public class GraphBuilderTest {

    /*static class GridPoint {
        final int i, j;
        GridPoint(int i, int j) {
            this.i = i;
            this.j = j;
        }
        float dst (GridPoint v) {
            int x_d = v.i - i, y_d = v.j - j;
            return (float) Math.sqrt(x_d * x_d + y_d * y_d);
        }
    }

    @Test
    public void testExample() {
        int n = 10;
        UndirectedGraph<GridPoint> graph = new UndirectedGraph<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph.addVertex(new GridPoint(i, j));
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i<n-1) {
                    GridPoint v1 = new GridPoint(i, j), v2 = new GridPoint(i+1,j);
                    graph.addEdge(v1, v2, v1.dst(v2));
                }
                if (j<n-1) {
                    GridPoint v1 = new GridPoint(i, j), v2 = new GridPoint(i,j+1);
                    graph.addEdge(v1, v2, v1.dst(v2));
                }
            }
        }
    }*/

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
