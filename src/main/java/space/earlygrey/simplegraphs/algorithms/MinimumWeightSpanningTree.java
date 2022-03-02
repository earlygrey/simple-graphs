package space.earlygrey.simplegraphs.algorithms;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;
import java.util.stream.Collectors;

import space.earlygrey.simplegraphs.Connection;
import space.earlygrey.simplegraphs.Edge;
import space.earlygrey.simplegraphs.Node;
import space.earlygrey.simplegraphs.UndirectedGraph;

public class MinimumWeightSpanningTree<V> extends Algorithm<V>{

    private UndirectedGraph<V> spanningTree;
    private Queue<Connection<V>> edgeQueue;
    private int finishAt;

    // adapted from https://www.baeldung.com/java-spanning-trees-kruskal

    protected MinimumWeightSpanningTree(int id, UndirectedGraph<V> graph, boolean minSpanningTree) {
        super(id);

        spanningTree = graph.createNew();

        spanningTree.addVertices(graph.getVertices());

        edgeQueue = graph.internals().getConnections().stream()
                .sorted(minSpanningTree ? Comparator.comparing(Edge<V>::getWeight) : Comparator.comparing(Edge<V>::getWeight).reversed())
                .collect(Collectors.toCollection(ArrayDeque::new));

        finishAt = graph.isConnected() ? graph.size() - 1 : -1;
    }

    @Override
    public boolean update() {
        if (isFinished()) return true;

        Connection<V> edge = edgeQueue.poll();

        if (doesEdgeCreateCycle(edge.getNodeA(), edge.getNodeB(), id)) {
            return false;
        }
        spanningTree.addEdge(edge.getA(), edge.getB(), edge.getWeightFunction());

        return isFinished();
    }

    private void unionByRank(Node<V> rootU, Node<V> rootV) {
        if (rootU.getIndex() < rootV.getIndex()) {
            rootU.setPrev(rootV);
        } else {
            rootV.setPrev(rootU);
            if (rootU.getIndex() == rootV.getIndex()) rootU.setIndex(rootU.getIndex() + 1);
        }
    }

    private Node<V> find(Node<V> node) {
        if (node.equals(node.getPrev())) {
            return node;
        } else {
            return find(node.getPrev());
        }
    }
    private Node<V> pathCompressionFind(Node<V> node) {
        if (node.equals(node.getPrev())) {
            return node;
        } else {
            Node<V> parentNode = find(node.getPrev());
            node.setPrev(parentNode);
            return parentNode;
        }
    }

    private boolean doesEdgeCreateCycle(Node<V> u, Node<V> v, int runID) {
        if (u.resetAlgorithmAttribs(runID)) u.setPrev(u);
        if (v.resetAlgorithmAttribs(runID)) v.setPrev(v);
        Node<V> rootU = pathCompressionFind(u);
        Node<V> rootV = pathCompressionFind(v);
        if (rootU.equals(rootV)) {
            return true;
        }
        unionByRank(rootU, rootV);
        return false;
    }

    @Override
    public boolean isFinished() {
        return finishAt < 0 ? edgeQueue.isEmpty() : spanningTree.getEdgeCount() == finishAt;
    }

    public UndirectedGraph<V> getSpanningTree() {
        return spanningTree;
    }
}
