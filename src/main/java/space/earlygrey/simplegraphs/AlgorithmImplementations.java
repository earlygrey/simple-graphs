package space.earlygrey.simplegraphs;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import space.earlygrey.simplegraphs.utils.Heuristic;

class AlgorithmImplementations<V> {

    //================================================================================
    // Fields
    //================================================================================

    private final Graph<V> graph;
    private final PriorityQueue<Node<V>> priorityQueueWithEstimate, priorityQueue;
    private final ArrayDeque<Node<V>> queue;
    private int runID = 0;

    //================================================================================
    // Constructor
    //================================================================================

    AlgorithmImplementations(Graph<V> graph) {
        this.graph = graph;
        priorityQueueWithEstimate = new PriorityQueue<>(Comparator.comparing(e -> e.attribs.distance + e.attribs.estimate));
        priorityQueue = new PriorityQueue<>(Comparator.comparing(e -> e.attribs.distance));
        queue = new ArrayDeque<>();
    }

    //================================================================================
    // Util
    //================================================================================

    private void init() {
        runID++;
    }

    private boolean resetAttribs(Node<V> node) {
        return node.attribs.reset(runID);
    }

    //================================================================================
    // Connectivity
    //================================================================================

    boolean isReachable(Node<V> start, Node<V> target) {
        return findShortestPath(start, target, new ArrayList<>());
    }

    //================================================================================
    // Searches
    //================================================================================

    void breadthFirstSearch(Node<V> vertex, Graph<V> tree, int maxVertices, int maxDepth) {
        if (maxDepth <= 0 ) return;
        init();

        resetAttribs(vertex);
        vertex.attribs.visited = true;
        ArrayDeque<Node<V>> queue = this.queue;
        queue.clear();
        queue.addLast(vertex);

        while(!queue.isEmpty()) {
            Node<V> v = queue.poll();
            tree.addVertex(v.object);
            if (v.attribs.prev != null) tree.addEdge(v.object, v.attribs.prev.object);
            if (v.attribs.i == maxDepth) continue;
            if (tree.size() == maxVertices) break;
            int n = v.outEdges.size();
            for (int i = 0; i < n; i++) {
                Connection<V> e = v.outEdges.get(i);
                Node<V> w = e.b;
                resetAttribs(w);
                if (!w.attribs.visited) {
                    w.attribs.visited = true;
                    w.attribs.i = v.attribs.i + 1;
                    w.attribs.prev = v;
                    queue.addLast(w);
                }
            }
        }
    }

    void depthFirstSearch(Node<V> vertex, Graph<V> tree, int maxVertices, int maxDepth) {
        init();

        resetAttribs(vertex);
        ArrayDeque<Node<V>> queue = this.queue;
        queue.clear();
        queue.addLast(vertex);

        while(!queue.isEmpty()) {
            Node<V> v = queue.poll();
            if (!v.attribs.visited) {
                tree.addVertex(v.object);
                if (v.attribs.prev != null) tree.addEdge(v.object, v.attribs.prev.object);
                if (v.attribs.i == maxDepth) continue;
                if (tree.size() == maxVertices) break;
                v.attribs.visited = true;
                int n = v.outEdges.size();
                for (int i = 0; i < n; i++) {
                    Connection<V> e = v.outEdges.get(i);
                    Node<V> w = e.b;
                    resetAttribs(w);
                    w.attribs.i = v.attribs.i + 1;
                    w.attribs.prev = v;
                    queue.addFirst(w);
                }
            }
        }
    }

    //================================================================================
    // Shortest Paths
    //================================================================================

    float findMinimumDistance(Node<V> start, Node<V> target) {
        Node<V> end = aStarSearch(start, target, null);
        if (end==null) return Float.MAX_VALUE;
        else return end.attribs.distance;
    }

    List<V> findShortestPath(Node<V> start, Node<V> target) {
        ArrayList<V> path = new ArrayList<>();
        findShortestPath(start, target, path);
        return path;
    }

    boolean findShortestPath(Node<V> start, Node<V> target, List<V> path) {
        return findShortestPath(start, target, path, null);
    }

    List<V> findShortestPath(Node<V> start, Node<V> target, Heuristic<V> heuristic) {
        ArrayList<V> path = new ArrayList<>();
        findShortestPath(start, target, path, heuristic);
        return path;
    }

    boolean findShortestPath(Node<V> start, Node<V> target, List<V> path, Heuristic<V> heuristic) {
        Node<V> end = aStarSearch(start, target, heuristic);
        if (end==null) {
            return false;
        }
        Node<V> v = end;
        while(v.attribs.prev!=null) {
            path.add(v.object);
            v = v.attribs.prev;
        }
        path.add(start.object);
        Collections.reverse(path);
        return true;
    }

    private Node<V> aStarSearch(Node<V> start, Node<V> target, Heuristic<V> heuristic) {
        init();

        boolean hasHeuristic = heuristic != null;

        PriorityQueue<Node<V>> queue = hasHeuristic ? priorityQueueWithEstimate : priorityQueue;
        queue.clear();

        resetAttribs(start);
        start.attribs.distance = 0;

        queue.add(start);

        while(queue.size() != 0) {
            Node<V> u = queue.poll();
            if (u == target) {
                return u;
            }
            if (!u.attribs.visited) {
                u.attribs.visited = true;
                int n = u.outEdges.size();
                for (int i = 0; i < n; i++) {
                    Connection<V> e = u.outEdges.get(i);
                    Node<V> v = e.b;
                    resetAttribs(v);
                    if (!v.attribs.visited) {
                        float newDistance = u.attribs.distance + e.weight;
                        if (newDistance < v.attribs.distance) {
                            v.attribs.distance = newDistance;
                            v.attribs.prev = u;
                            if (hasHeuristic && !v.attribs.seen) {
                                v.attribs.estimate = heuristic.getEstimate(v.object, target.object);
                                v.attribs.seen = true;
                            }
                            queue.add(v);
                        }
                    }
                }
            }
        }
        return null;
    }

    //================================================================================
    // Topological sorting
    //================================================================================

    boolean topologicalSort(List<V> sortedVertices) {
        sortedVertices.clear();
        init();
        LinkedHashSet<Node<V>> set = new LinkedHashSet<>(graph.vertexMap.values());
        boolean success = true;
        while (success && !set.isEmpty()) {
            success = recursiveTopologicalSort(sortedVertices, set.iterator().next(), set);
        }
        if (success) {
            Collections.reverse(sortedVertices);
        }

        return success;
    }

    boolean topologicalSort() {
        List<V> sortedVertices = new ArrayList<>();
        init();
        LinkedHashSet<Node<V>> set = new LinkedHashSet<>(graph.vertexMap.values());
        boolean success = true;
        while (success && !set.isEmpty()) {
            success = recursiveTopologicalSort(sortedVertices, set.iterator().next(), set);
        }
        if (success) {
            for (int i = sortedVertices.size()-1; i >= 0; i--) {
                V v = sortedVertices.get(i);
                Node<V> value = graph.vertexMap.remove(v);
                graph.vertexMap.put(v, value);
            }
        }
        return success;
    }

    private boolean recursiveTopologicalSort(List<V> sortedVertices, Node<V> v, Set<Node<V>> set) {
        resetAttribs(v);

        if (v.attribs.visited) return true;
        if (v.attribs.seen) {
            // not a DAG
            return false;
        }
        v.attribs.seen = true;
        int n = v.outEdges.size();
        for (int i = 0; i < n; i++) {
            Connection<V> e = v.outEdges.get(i);
            boolean success = recursiveTopologicalSort(sortedVertices, e.b, set);
            if (!success) return false;
        }
        v.attribs.seen = false;
        v.attribs.visited = true;
        sortedVertices.add(v.object);
        set.remove(v);
        return true;
    }

    //================================================================================
    // Minimum spanning trees
    //================================================================================

    // adapted from https://www.baeldung.com/java-spanning-trees-kruskal

    Graph<V> kruskalsMinimumWeightSpanningTree(boolean minSpanningTree) {
        init();

        Graph<V> spanningTree = graph.createNew();

        spanningTree.addVertices(graph.getVertices());

        List<Connection<V>> edgeList = new ArrayList<>(graph.edgeMap.values());

        if (minSpanningTree) {
            edgeList.sort(Comparator.comparing(e -> e.weight));
        } else {
            edgeList.sort(Collections.reverseOrder(Comparator.comparing(e -> e.weight)));
        }

        int totalNodes = graph.size();
        int edgeCount = 0;

        for (Connection<V> edge : edgeList) {
            if (doesEdgeCreateCycle(edge.a, edge.b)) {
                continue;
            }
            spanningTree.addConnection(edge.a, edge.b, edge.weight);
            edgeCount++;
            if (edgeCount == totalNodes - 1) {
                break;
            }
        }

        return spanningTree;
    }

    private void unionByRank(Node<V> rootU, Node<V> rootV) {
        if (rootU.attribs.i < rootV.attribs.i) {
            rootU.attribs.prev = rootV;
        } else {
            rootV.attribs.prev = rootU;
            if (rootU.attribs.i == rootV.attribs.i) rootU.attribs.i++;
        }
    }

    private Node<V> find(Node<V> node) {
        if (node.attribs.prev.equals(node)) {
            return node;
        } else {
            return find(node.attribs.prev);
        }
    }
    private Node<V> pathCompressionFind(Node<V> node) {
        if (node.attribs.prev.equals(node)) {
            return node;
        } else {
            Node<V> parentNode = find(node.attribs.prev);
            node.attribs.prev = parentNode;
            return parentNode;
        }
    }

    private boolean doesEdgeCreateCycle(Node<V> u, Node<V> v) {
        if (resetAttribs(u)) u.attribs.prev = u;
        if (resetAttribs(v)) v.attribs.prev = v;
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
            resetAttribs(v);
            if (detectCycleDFS(v, null, new HashSet<>())) {
                init();
                return true;
            }
        }
        return false;
    }

    private boolean detectCycleDFS(Node<V> v, Node<V> parent, Set<Node<V>> recursiveStack) {
        v.attribs.visited = true;
        recursiveStack.add(v);
        int n = v.outEdges.size();
        for (int i = 0; i < n; i++) {
            Connection<V> e = v.outEdges.get(i);
            if (!graph.isDirected() && e.b.equals(parent)) continue;
            resetAttribs(e.b);
            if (recursiveStack.contains(e.b)) {
                return true;
            }
            if (!e.b.attribs.visited) {
                if (detectCycleDFS(e.b, v, recursiveStack)) return true;
            }
        }
        recursiveStack.remove(v);
        return false;
    }

    /*void dfs(Node<V> vertex, List<V> vertices, float maxDistance, int maxDepth) {
        vertices.clear();
        clear();
        dfsRecursive(vertex, vertices, maxDistance, maxDepth, 0);
        clear();
    }

    private void dfsRecursive(Node<V> v, List<V> vertices, float maxDistance, int maxDepth, int depth) {
        if (depth > maxDepth) return;
        resetAttribs(v);
        v.attribs.visited = true;
        vertices.add(v.object);
        for (Connection e : v.connections.values()) {
            Node<V> w = e.b;
            if (!w.attribs.visited) {
                w.attribs.distance = v.attribs.distance + e.getWeight();
                if (w.attribs.distance <= maxDistance) dfsRecursive(w, vertices, maxDistance, maxDepth, depth + 1);
            }
        }
    }*/


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