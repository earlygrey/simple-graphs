package space.earlygrey.simplegraphs;

import java.util.Objects;

class TestUtils {

    static class Vector2 {

        float x, y;

        Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        float dst (Vector2 v) {
            final float x_d = v.x - x;
            final float y_d = v.y - y;
            return (float) Math.sqrt(x_d * x_d + y_d * y_d);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector2 vector2 = (Vector2) o;
            return Float.compare(vector2.x, x) == 0 &&
                    Float.compare(vector2.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static <G extends Graph<Integer>> G makeCompleteGraph(G graph, int n) {
        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!graph.isDirected() && j<i+1) continue;
                else if (i==j) continue;
                graph.addEdge(i, j);
            }
        }
        return graph;
    }

    static Graph<Vector2> makeGridGraph(Graph<Vector2> graph, int n) {

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
                    if (graph.isDirected()) graph.addEdge(v2, v1, v1.dst(v2));
                    e++;
                }
                if (j<n-1) {
                    Vector2 v1 = new Vector2(i, j), v2 = new Vector2(i,j+1);
                    graph.addEdge(v1, v2, v1.dst(v2));
                    if (graph.isDirected()) graph.addEdge(v2, v1, v1.dst(v2));
                    e++;
                }
            }
        }
        return graph;
    }
}
