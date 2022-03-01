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
package space.earlygrey.simplegraphs.algorithms;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import space.earlygrey.simplegraphs.Array;
import space.earlygrey.simplegraphs.Connection;
import space.earlygrey.simplegraphs.Graph;
import space.earlygrey.simplegraphs.Node;
import space.earlygrey.simplegraphs.Path;
import space.earlygrey.simplegraphs.utils.Heuristic;
import space.earlygrey.simplegraphs.utils.SearchProcessor;

class AlgorithmImplementations<V> {

    //================================================================================
    // Fields
    //================================================================================

    private final Graph<V> graph;

    //================================================================================
    // Constructor
    //================================================================================

    AlgorithmImplementations(Graph<V> graph) {
        this.graph = graph;
    }

    //================================================================================
    // Connectivity
    //================================================================================

    boolean isReachable(Node<V> start, Node<V> target) {
        return !findShortestPath(start, target, null, null).isEmpty();
    }

    //================================================================================
    // Searches
    //================================================================================

    void breadthFirstSearch(final Node<V> v, SearchProcessor<V> processor) {
        new BreadthFirstSearch<>(graph.algorithms().requestRunID(), v, processor).finish();
    }

    void depthFirstSearch(final Node<V> v, SearchProcessor<V> processor) {
        new DepthFirstSearch<>(graph.algorithms().requestRunID(), v, processor).finish();
    }

    //================================================================================
    // Shortest Paths
    //================================================================================

    float findMinimumDistance(Node<V> start, Node<V> target) {
        return findMinimumDistance(start, target, null);
    }

    float findMinimumDistance(Node<V> start, Node<V> target, Heuristic<V> heuristic) {
        AStarSearch<V> search = new AStarSearch<>(graph.algorithms().requestRunID(), start, target, heuristic, null);
        search.finish();
        if (search.getEnd() == null) return Float.MAX_VALUE;
        else return search.getEnd().getDistance();
    }


    Path<V> findShortestPath(Node<V> start, Node<V> target, final Heuristic<V> heuristic, SearchProcessor<V> processor) {
        AStarSearch<V> search = new AStarSearch<>(graph.algorithms().requestRunID(), start, target, heuristic, processor);
        search.finish();
        return search.getPath();
    }



    //================================================================================
    // Minimum spanning trees
    //================================================================================

    // adapted from https://www.baeldung.com/java-spanning-trees-kruskal

    Graph<V> kruskalsMinimumWeightSpanningTree(boolean minSpanningTree) {

        int runID = graph.algorithms().requestRunID();

        Graph<V> spanningTree = graph.createNew();

        spanningTree.addVertices(graph.getVertices());

        List<Connection<V>> edgeList = new ArrayList<>(graph.internals().getConnections());

        if (minSpanningTree) {
            Collections.sort(edgeList, (e1, e2) -> Float.floatToIntBits(e1.getWeight() - e2.getWeight()));
        } else {
            Collections.sort(edgeList, (e1, e2) -> Float.floatToIntBits(e2.getWeight() - e1.getWeight()));
        }

        int totalNodes = graph.size();
        int edgeCount = 0;

        for (Connection<V> edge : edgeList) {
            if (doesEdgeCreateCycle(edge.getNodeA(), edge.getNodeB(), runID)) {
                continue;

            }
            spanningTree.internals().addConnection(edge.getNodeA(), edge.getNodeB(), edge.getWeightFunction());
            edgeCount++;
            if (edgeCount == totalNodes - 1) {
                break;
            }
        }

        return spanningTree;
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

    //================================================================================
    // Cycle detection
    //================================================================================

    boolean containsCycle(Graph<V> graph) {
        if (graph.size() < 3 || graph.getEdgeCount() < 3) return false;
        int runID = graph.algorithms().requestRunID();
        for (Node<V> v : graph.internals().getNodes()) {
            v.resetAlgorithmAttribs(runID);
            if (detectCycleDFS(v, null, new HashSet<>(), runID)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectCycleDFS(Node<V> v, Node<V> parent, Set<Node<V>> recursiveStack, int runID) {
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
                if (detectCycleDFS(u, v, recursiveStack, runID)) return true;
            }
        }
        recursiveStack.remove(v);
        return false;
    }

}

