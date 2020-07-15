package space.earlygrey.simplegraphs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class StructuresTest {

    static class BadHashInteger {

        int i;

        public BadHashInteger(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BadHashInteger that = (BadHashInteger) o;
            return i == that.i;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return i+"";
        }
    }

    @Test
    public void nodeMapShouldWork() {

        Graph<Integer> graph = new UndirectedGraph<>();
        NodeMap<Integer> nodeMap = graph.nodeMap;
        int n = 16;
        List<Integer> list = new ArrayList<>();


        for (int i = 0; i < n; i++) {
            assertTrue(nodeMap.put(i) != null);
            list.add(i);
        }

        assertEquals(n, nodeMap.size);

        Graph<BadHashInteger> badGraph = new UndirectedGraph<>();
        for (int i = 0; i < n; i++) {
            assertTrue(badGraph.nodeMap.put(new BadHashInteger(i)) != null);
        }

        badGraph.nodeMap.clear();

        for (int i = 0; i < n; i++) {
            assertTrue(badGraph.nodeMap.put(new BadHashInteger(i)) != null);
        }
    }
}
