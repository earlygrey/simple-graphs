package space.earlygrey.simplegraphs.algorithms;

import java.util.HashSet;
import java.util.Set;

import space.earlygrey.simplegraphs.Array;
import space.earlygrey.simplegraphs.Connection;
import space.earlygrey.simplegraphs.Graph;
import space.earlygrey.simplegraphs.Node;

public class CycleDetector<V> extends Algorithm<V> {

    private boolean containsCycle;

    protected CycleDetector(int id, Graph<V> graph) {
        super(id);
        containsCycle = findCycle(graph);
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    boolean findCycle(Graph<V> graph) {
        if (graph.size() < 3 || graph.getEdgeCount() < 3) return false;
        int runID = graph.algorithms().requestRunID();
        for (Node<V> v : graph.internals().getNodes()) {
            v.resetAlgorithmAttribs(runID);
            if (detectCycleDFS(v, null, new HashSet<>(), runID, graph)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectCycleDFS(Node<V> v, Node<V> parent, Set<Node<V>> recursiveStack, int runID, Graph<V> graph) {
        v.setProcessed(true);
        recursiveStack.add(v);
        Array<Connection<V>> outEdges = v.getOutEdges();
        for (Connection<V> e : outEdges) {
            Node<V> u = e.getNodeB();
            if (!graph.isDirected() && u.equals(parent)) continue;
            u.resetAlgorithmAttribs(runID);
            if (recursiveStack.contains(u)) {
                return true;
            }
            if (!u.isProcessed()) {
                if (detectCycleDFS(u, v, recursiveStack, runID, graph)) return true;
            }
        }
        recursiveStack.remove(v);
        return false;
    }

    public boolean containsCycle() {
        return containsCycle;
    }
}
