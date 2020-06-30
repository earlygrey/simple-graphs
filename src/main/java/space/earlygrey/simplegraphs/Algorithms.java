package space.earlygrey.simplegraphs;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class Algorithms<V> {

    private final Graph<V> graph;
    boolean[] reset = new boolean[8];
    private final FibonacciHeap<Node<V>> priorityQueue;
    private final ArrayDeque<Node<V>> queue;

    public Algorithms(Graph<V> graph) {
        this.graph = graph;
        priorityQueue = new FibonacciHeap<>();
        queue = new ArrayDeque<>();
    }

    void ensureVertexCapacity(int capacity) {
        if (reset.length < capacity) reset = new boolean[3 * capacity >>> 1]; // 3/2 of capacity
    }

    private void init() {
        Arrays.fill(reset, 0, graph.nodes.objects.size(), false);
    }

    boolean isReachable(Node<V> start, Node<V> target) {
        return findShortestPath(start, target, new ArrayList<>());
    }

    float findMinimumDistance(Node<V> start, Node<V> target) {
        Node<V> end = aStarSearch(start, target, null);
        if (end==null) return Float.MAX_VALUE;
        else return end.distance;
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
        Node<V> end = heuristic == null ? dijkstra(start, target) : aStarSearch(start, target, heuristic);
        if (end==null) {
            return false;
        }
        Node<V> v = end;
        while(v.prev!=null) {
            path.add(v.object);
            v = v.prev;
        }
        path.add(start.object);
        Collections.reverse(path);
        return true;
    }

    private Node<V> dijkstra(Node<V> start, Node<V> target) {
        init();
        
        FibonacciHeap<Node<V>> queue = priorityQueue;
        queue.clear();

        resetAttribs(start);
        start.distance = 0;

        queue.enqueue(start, 0);

        while(!queue.isEmpty()) {
            Node<V> u = queue.dequeueMin().getValue();
            if (u == target) {
                return u;
            }
            if (!u.visited) {
                u.visited = true;
                int n = u.outEdges.size();
                for (int i = 0; i < n; i++) {
                    Connection<V> e = u.outEdges.get(i);
                    Node<V> v = e.b;
                    resetAttribs(v);
                    if (!v.visited) {
                        float newDistance = u.distance + e.weight;
                        if (newDistance < v.distance) {
                            v.distance = newDistance;
                            v.prev = u;
                            if (v.entry == null) v.entry = queue.enqueue(v, v.distance);
                            else queue.decreaseKey(v.entry, v.distance);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Node<V> aStarSearch(Node<V> start, Node<V> target, Heuristic<V> heuristic) {
        init();

        FibonacciHeap<Node<V>> queue = priorityQueue;
        queue.clear();
        
        resetAttribs(start);
        start.distance = 0;

        queue.enqueue(start, 0);

        while(!queue.isEmpty()) {
            Node<V> u = queue.dequeueMin().object;
            if (u == target) {
                return u;
            }
            if (!u.visited) {
                u.visited = true;
                int n = u.outEdges.size();
                for (int i = 0; i < n; i++) {
                    Connection<V> e = u.outEdges.get(i);
                    Node<V> v = e.b;
                    resetAttribs(v);
                    if (!v.visited) {
                        float newDistance = u.distance + e.weight;
                        if (newDistance < v.distance) {
                            v.distance = newDistance;
                            v.prev = u;
                            if (!v.seen) {
                                v.estimate = heuristic.getEstimate(v.object, target.object);
                                v.seen = true;
                            }
                            if (v.entry == null) v.entry = queue.enqueue(v, v.distance + v.estimate);
                            else queue.decreaseKey(v.entry, v.distance + v.estimate);
                        }
                    }
                }
            }
        }
        return null;
    }

    void breadthFirstSearch(Node<V> vertex, Graph<V> tree, int maxVertices, int maxDepth) {
        if (maxDepth <= 0 ) return;
        init();

        resetAttribs(vertex);
        vertex.visited = true;
        ArrayDeque<Node<V>> queue = this.queue;
        queue.clear();
        queue.addLast(vertex);

        while(!queue.isEmpty()) {
            Node<V> v = queue.poll();
            tree.addVertex(v.object);
            if (v.prev != null) tree.addEdge(v.object, v.prev.object);
            if (v.i == maxDepth) continue;
            if (tree.size() == maxVertices) break;
            int n = v.outEdges.size();
            for (int i = 0; i < n; i++) {
                Connection<V> e = v.outEdges.get(i);
                Node<V> w = e.b;
                resetAttribs(w);
                if (!w.visited) {
                    w.visited = true;
                    w.i = v.i+1;
                    w.prev = v;
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
            if (!v.visited) {
                tree.addVertex(v.object);
                if (v.prev != null) tree.addEdge(v.object, v.prev.object);
                if (v.i == maxDepth) continue;
                if (tree.size() == maxVertices) break;
                v.visited = true;
                int n = v.outEdges.size();
                for (int i = 0; i < n; i++) {
                    Connection<V> e = v.outEdges.get(i);
                    Node<V> w = e.b;
                    resetAttribs(w);
                    w.i = v.i+1;
                    w.prev = v;
                    queue.addFirst(w);
                }
            }
        }
    }

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

        if (v.visited) return true;
        if (v.seen) {
            // not a DAG
            return false;
        }
        v.seen = true;
        int n = v.outEdges.size();
        for (int i = 0; i < n; i++) {
            Connection<V> e = v.outEdges.get(i);
            boolean success = recursiveTopologicalSort(sortedVertices, e.b, set);
            if (!success) return false;
        }
        v.seen = false;
        v.visited = true;
        sortedVertices.add(v.object);
        set.remove(v);
        return true;
    }

    // adapted from https://www.baeldung.com/java-spanning-trees-kruskal

    Graph<V> kruskalsMinimumWeightSpanningTree(boolean minSpanningTree) {
        init();

        Graph<V> spanningTree = graph.createNew();

        spanningTree.addVertices(graph.getVertices());

        Collection<Connection<V>> edges = graph.edges.values();
        List<Connection<V>> edgeList = new ArrayList<>(edges);

        if (minSpanningTree) {
           edgeList.sort(Comparator.comparing(e -> e.weight));
        } else {
           edgeList.sort(Collections.reverseOrder(Comparator.comparing(e -> e.weight)));
        }

        int totalNodes = graph.size();
        int edgeCount = 0;

        for (Connection<V> edge : edgeList) {
            if (detectCycle(edge.a, edge.b)) {
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

    private boolean detectCycle(Node<V> u, Node<V> v) {
        if (resetAttribs(u)) u.prev = u;
        if (resetAttribs(v)) v.prev = v;
        Node<V> rootU = pathCompressionFind(u);
        Node<V> rootV = pathCompressionFind(v);
        if (rootU.equals(rootV)) {
            return true;
        }
        unionByRank(rootU, rootV);
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
        v.visited = true;
        vertices.add(v.object);
        for (Connection e : v.connections.values()) {
            Node<V> w = e.b;
            if (!w.visited) {
                w.distance = v.distance + e.getWeight();
                if (w.distance <= maxDistance) dfsRecursive(w, vertices, maxDistance, maxDepth, depth + 1);
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
        v.visited = true;
        recursiveStack.add(v);
        int n = v.outEdges.size();
        for (int i = 0; i < n; i++) {
            Connection<V> e = v.outEdges.get(i);
            if (!graph.isDirected() && e.b.equals(parent)) continue;
            resetAttribs(e.b);
            if (recursiveStack.contains(e.b)) {
                return true;
            }
            if (!e.b.visited) {
                if (detectCycleDFS(e.b, v, recursiveStack)) return true;
            }
        }
        recursiveStack.remove(v);
        return false;
    }

    private boolean resetAttribs(Node<V> node) {
        if (reset[node.index]) return false;
        node.resetAlgorithmAttribs();
        reset[node.index] = true;
        return true;
    }

}

