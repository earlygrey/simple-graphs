package space.earlygrey.simplegraphs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTest {


    @Test
    public void verticesCanBeAdded() {
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

    }

    @Test
    public void edgesCanBeAdded() {
        Graph<Vector2> graph = new Graph<>();
        int n = 5;

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
                    graph.connect(v1, v2, v1.dst(v2));
                    e++;
                }
                if (j<n-1) {
                    Vector2 v1 = new Vector2(i, j), v2 = new Vector2(i,j+1);
                    graph.connect(v1, v2, v1.dst(v2));
                    e++;
                }
            }
        }

        assertEquals(e, graph.getEdgeCount());

    }

}
