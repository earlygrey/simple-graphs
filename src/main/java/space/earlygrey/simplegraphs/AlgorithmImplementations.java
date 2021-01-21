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


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import space.earlygrey.simplegraphs.AlgorithmStep.SearchStep;
import space.earlygrey.simplegraphs.AlgorithmStep.ShortestPathStep;
import space.earlygrey.simplegraphs.utils.Heuristic;

class AlgorithmImplementations<V> {

    //================================================================================
    // Fields
    //================================================================================

    private final Graph<V> graph;
    private final BinaryHeap heap;
    private final ArrayDeque<Node<V>> queue;
    private int runID = 0;

    //================================================================================
    // Constructor
    //================================================================================

    AlgorithmImplementations(Graph<V> graph) {
        this.graph = graph;
        heap = new BinaryHeap();
        queue = new ArrayDeque<>();
    }

    //================================================================================
    // Util
    //================================================================================

    private void init() {
        runID++;
    }

    //================================================================================
    // Connectivity
    //================================================================================

    boolean isReachable(Node<V> start, Node<V> target) {
        return !findShortestPath(start, target, null, null, null).isEmpty();
    }

    //================================================================================
    // Searches
    //================================================================================

    void breadthFirstSearch(Node<V> vertex, Consumer<SearchStep<V>> preprocessor) {
        init();

        vertex.resetAlgorithmAttribs(runID);
        ArrayDeque<Node<V>> queue = this.queue;
        queue.clear();
        queue.add(vertex);
        vertex.seen = true;

        final SearchStep<V> step = preprocessor != null ? new SearchStep<>() : null;

        while(!queue.isEmpty()) {
            Node<V> v = queue.poll();
            if (preprocessor != null) {
                step.prepare(v);
                preprocessor.accept(step);
                if (step.terminate) return;
                if (step.ignore) continue;
            }
            int n = v.outEdges.size();
            for (int i = 0; i < n; i++) {
                Connection<V> e = v.outEdges.get(i);
                Node<V> w = e.b;
                w.resetAlgorithmAttribs(runID);
                if (!w.seen) {
                    w.i = v.i + 1;
                    w.connection = e;
                    w.seen = true;
                    queue.addLast(w);
                }
            }
        }
    }

    void depthFirstSearch(Node<V> v, Consumer<SearchStep<V>> preprocessor) {
        init();
        v.resetAlgorithmAttribs(runID);
        recursiveDepthFirstSearch(v, preprocessor, 0, preprocessor != null ? new SearchStep<>() : null);
    }

    boolean recursiveDepthFirstSearch(Node<V> v, Consumer<SearchStep<V>> preprocessor, int depth, SearchStep<V> step) {
        if (preprocessor != null) {
            step.prepare(v);
            preprocessor.accept(step);
            if (step.terminate) return true;
            if (step.ignore) return false;
        }
        v.processed = true;
        int n = v.outEdges.size();
        for (int i = 0; i < n; i++) {
            Connection<V> e = v.outEdges.get(i);
            Node<V> w = e.b;
            w.resetAlgorithmAttribs(runID);
            if (!w.processed) {
                w.i = depth + 1;
                w.connection = e;
                if (recursiveDepthFirstSearch(w, preprocessor, depth + 1, step)) {
                    return true;
                }
            }
        }
        return false;
    }

    //================================================================================
    // Shortest Paths
    //================================================================================

    float findMinimumDistance(Node<V> start, Node<V> target) {
        return findMinimumDistance(start, target, null);
    }

    float findMinimumDistance(Node<V> start, Node<V> target, Heuristic<V> heuristic) {
        Node<V> end = aStarSearch(start, target, heuristic, null);
        if (end==null) return Float.MAX_VALUE;
        else return end.distance;
    }


    Path<V> findShortestPath(Node<V> start, Node<V> target, final Heuristic<V> heuristic, Path<V> path, Consumer<ShortestPathStep<V>> preprocessor) {
        Node<V> end = aStarSearch(start, target, heuristic, preprocessor);
        if (end == null) {
            if (path != null) {
                path.setFixed(false);
                path.clear();
            } else {
                path = new Path<>(0);
            }
            return path;
        }
        int size = end.i + 1;
        if (path == null) {
            path = new Path<>(size);
        } else {
            path.setFixed(false);
            if (path.items.length < size) path.strictResize(size);
        }
        Node<V> v = end;
        while(v.prev != null) {
            path.set(v.i, v.object);
            v = v.prev;
        }
        path.set(0, start.object);
        path.length = end.distance;

        return path;
    }

    /**
     *
     * @param start
     * @param target
     * @param heuristic
     * @return the target Node if reachable, otherwise null
     */
    private Node<V> aStarSearch(final Node<V> start, final Node<V> target, final Heuristic<V> heuristic, final Consumer<ShortestPathStep<V>> preprocessor) {
        init();

        start.resetAlgorithmAttribs(runID);
        start.distance = 0;

        heap.add(start);

        final ShortestPathStep<V> step = preprocessor != null ? new ShortestPathStep<>() : null;

        while(heap.size != 0) {
            Node<V> u = heap.pop();
            if (u == target) {
                heap.clear();
                return u;
            }
            if (!u.processed) {
                if (preprocessor != null) {
                    step.prepare(u);
                    preprocessor.accept(step);
                    if (step.terminate) return null;
                    if (step.ignore) continue;
                }
                u.processed = true;
                int n = u.outEdges.size();
                for (int i = 0; i < n; i++) {
                    Connection<V> e = u.outEdges.get(i);
                    Node<V> v = e.b;
                    v.resetAlgorithmAttribs(runID);
                    if (!v.processed) {
                        float newDistance = u.distance + e.getWeight();
                        if (newDistance < v.distance) {
                            v.distance = newDistance;
                            v.prev = u;
                            v.connection = e;
                            if (heuristic != null && !v.seen) {
                                v.estimate = heuristic.getEstimate(v.object, target.object);
                            }
                            if (!v.seen) {
                                heap.add(v, v.distance + v.estimate);
                            } else {
                                heap.setValue(v, v.distance + v.estimate);
                            }
                            v.i = u.i+1;
                            v.seen = true;
                        }
                    }
                }
            }
        }
        heap.clear();
        return null;
    }

    //================================================================================
    // Topological sorting
    //================================================================================

   /* boolean topologicalSort(List<V> sortedVertices) {
        sortedVertices.clear();
        init();
        LinkedHashSet<Node<V>> set = new LinkedHashSet<>(graph.getNodes());
        boolean success = true;
        while (success && !set.isEmpty()) {
            success = recursiveTopologicalSort(sortedVertices, set.iterator().next(), set);
        }
        if (success) {
            Collections.reverse(sortedVertices);
        }
        return success;
    }*/

    // Keep track of the current position in the linked list,
    // We traverse the graph via DFS, and when we hit a terminal node we move that node
    // to the current cursor position, then move the cursor along one.
    Node<V> cursor;

    boolean topologicalSort() {
        if (graph.size() < 2 || graph.getEdgeCount() < 2) return true;

        init();

        // start the cursor at the tail and work towards the head,
        // so the list is sorted from head to tail
        cursor = graph.nodeMap.tail;

        boolean success = true;
        while (success && cursor != null) {
            success = recursiveTopologicalSort(cursor);
        }

        cursor = null;
        return success;
    }

    private boolean recursiveTopologicalSort(Node<V> v) {

        v.resetAlgorithmAttribs(runID);

        if (v.processed) return true;
        if (v.seen) return false; // not a DAG

        v.seen = true;

        int n = v.outEdges.size();
        for (int i = 0; i < n; i++) {
            Connection<V> edge = v.outEdges.get(i);
            if (!recursiveTopologicalSort(edge.b)) return false;

        }

        v.seen = false;
        v.processed = true;

        if (cursor != v) {
            // move v from its current position to just after the cursor
            graph.nodeMap.removeFromList(v);
            graph.nodeMap.insertIntoListAfter(v, cursor);
        } else {
            // v is already in the cursor position, just need to move the cursor along
            cursor = cursor.prevInOrder;
        }

        return true;
    }

    //================================================================================
    // Minimum spanning trees
    //================================================================================

    final Comparator<Connection<V>> weightComparator = new Comparator<Connection<V>>() {
        @Override
        public int compare(Connection<V> o1, Connection<V> o2) {
            return Float.floatToIntBits(o1.getWeight() - o2.getWeight());
        }
    };

    final Comparator<Connection<V>> reverseWeightComparator = new Comparator<Connection<V>>() {
        @Override
        public int compare(Connection<V> o1, Connection<V> o2) {
            return Float.floatToIntBits(o2.getWeight() - o1.getWeight());
        }
    };

    // adapted from https://www.baeldung.com/java-spanning-trees-kruskal

    Graph<V> kruskalsMinimumWeightSpanningTree(boolean minSpanningTree) {
        init();

        Graph<V> spanningTree = graph.createNew();

        spanningTree.addVertices(graph.getVertices());

        List<Connection<V>> edgeList = new ArrayList<>(graph.edgeMap.values());

        if (minSpanningTree) {
            Collections.sort(edgeList, weightComparator);
        } else {
            Collections.sort(edgeList, reverseWeightComparator);
        }

        int totalNodes = graph.size();
        int edgeCount = 0;

        for (Connection<V> edge : edgeList) {
            if (doesEdgeCreateCycle(edge.a, edge.b)) {
                continue;
            }
            spanningTree.addConnection(edge.a, edge.b, edge.getWeight());
            edgeCount++;
            if (edgeCount == totalNodes - 1) {
                break;
            }
        }

        return spanningTree;
    }

    private void unionByRank(Node<V> rootU, Node<V> rootV) {
        if (rootU.i < rootV.i) {
            rootU.prev = rootV;
        } else {
            rootV.prev = rootU;
            if (rootU.i == rootV.i) rootU.i++;
        }
    }

    private Node<V> find(Node<V> node) {
        if (node.prev.equals(node)) {
            return node;
        } else {
            return find(node.prev);
        }
    }
    private Node<V> pathCompressionFind(Node<V> node) {
        if (node.prev.equals(node)) {
            return node;
        } else {
            Node<V> parentNode = find(node.prev);
            node.prev = parentNode;
            return parentNode;
        }
    }

    private boolean doesEdgeCreateCycle(Node<V> u, Node<V> v) {
        if (u.resetAlgorithmAttribs(runID)) u.prev = u;
        if (v.resetAlgorithmAttribs(runID)) v.prev = v;
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
        init();
        for (Node<V> v : graph.getNodes()) {
            v.resetAlgorithmAttribs(runID);
            if (detectCycleDFS(v, null, new HashSet<>())) {
                init();
                return true;
            }
        }
        return false;
    }

    private boolean detectCycleDFS(Node<V> v, Node<V> parent, Set<Node<V>> recursiveStack) {
        v.processed = true;
        recursiveStack.add(v);
        int n = v.outEdges.size();
        for (int i = 0; i < n; i++) {
            Connection<V> e = v.outEdges.get(i);
            Node<V> u = e.b;
            if (!graph.isDirected() && u.equals(parent)) continue;
            u.resetAlgorithmAttribs(runID);
            if (recursiveStack.contains(u)) {
                return true;
            }
            if (!u.processed) {
                if (detectCycleDFS(u, v, recursiveStack)) return true;
            }
        }
        recursiveStack.remove(v);
        return false;
    }

    /*List<List<V>> getComponents(Graph<V> graph) {
        Set<Node<V>> nodeSet = new HashSet();
        nodeSet.addAll(graph.getNodes());
        List<List<Node<V>>> components = new ArrayList<>();
        while(!nodeSet.isEmpty()) {
            List<Node<V>> nodeList = findComponent(nodeSet.iterator().next());
            components.add(nodeList);
            nodeSet.removeAll(nodeList);
        }
        List<List<V>> objectComponents = new ArrayList<>(components.size());
        for (List<Node<V>> component : components) {
            List<V> objectNodes = new ArrayList<>();
            for (Node<V> node : component) {
                objectNodes.add(node.object);
            }
            objectComponents.add(objectNodes);
        }
        return objectComponents;
    }*/


}

