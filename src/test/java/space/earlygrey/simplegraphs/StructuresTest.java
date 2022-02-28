package space.earlygrey.simplegraphs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import space.earlygrey.simplegraphs.utils.BadHashInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class StructuresTest {

    @Test
    public void nodeMapShouldWork() {

        Graph<Integer> graph = new UndirectedGraph<>();
        NodeMap<Integer> nodeMap = graph.nodeMap;
        int n = 16;
        List<Integer> list = new ArrayList<>();

        int threshold = nodeMap.threshold;
        for (int i = 0; i < threshold; i++) {
            assertTrue(nodeMap.put(i) != null);
            list.add(i);
        }

        assertTrue("Put did not return a node", nodeMap.put(NodeMap.MIN_TABLE_LENGTH) != null);
        assertTrue("Object not contained in map", nodeMap.contains(NodeMap.MIN_TABLE_LENGTH));
        assertEquals("Map is not correct size", threshold+1, nodeMap.size);


        Node<Integer> removed = nodeMap.remove(2);
        assertTrue("Removal did not return node", removed != null);


        // test via graph object
        Graph<BadHashInteger> badGraph = new UndirectedGraph<>();
        for (int i = 0; i < n; i++) {
            assertTrue("Put did not return a node", badGraph.nodeMap.put(new BadHashInteger(i)) != null);
        }

        assertTrue("Graph is not correct size", badGraph.size() == n);

        badGraph.removeVertex(new BadHashInteger(2));

        assertTrue(badGraph.size() == n - 1);

        badGraph.nodeMap.clear();

        for (int i = 0; i < n; i++) {
            assertTrue(badGraph.nodeMap.put(new BadHashInteger(i)) != null);
        }
    }
}
