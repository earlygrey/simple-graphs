package space.earlygrey.simplegraphs;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Algorithms<V> {

    private final Graph<V> graph;
    boolean[] reset = new boolean[8];

    public Algorithms(Graph<V> graph) {
        this.graph = graph;
    }

    void ensureVertexCapacity(int capacity) {
        if (reset.length < capacity) reset = new boolean[(int) (1.5*capacity)];
    }

    private void clear() {
        Arrays.fill(reset, false);
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
        Node<V> end = aStarSearch(start, target, heuristic);
        if (end==null) {
            clear();
            return false;
        }
        Node<V> v = end;
        while(v.prev!=null) {
            path.add(v.object);
            v = v.prev;
        }
        path.add(start.object);
        Collections.reverse(path);
        clear();
        return true;
    }

    /*Map<Node<V>, Float> findAllShortestDistances(Node<V> start) {
        performShortestPathSearch(start, null, defaultHeuristic);
        priorityQueue.clear();
        Map<Node<V>, Float> distances = new HashMap<>();
        for (Node<V> v : graph.getNodes()) {
            if (v.visited) {
                distances.put(v, new Float(v.distance));
                //Gdx.app.log("GraphAlgorithms:findAllShortestDistances", ""+(v.distance));
            }
        }
        return distances;
    }*/

    private Node<V> aStarSearch(Node<V> start, Node<V> target, Heuristic<V> heuristic) {
        clear();



        FibonacciHeap<Node<V>> queue = new FibonacciHeap<>();

        resetAttribs(start);
        start.distance = 0;

        queue.enqueue(start, 0);

        while(!queue.isEmpty()) {
            Node<V> u = queue.dequeueMin().getValue();
            if (u == target) {
                clear();
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
        clear();
        return null;
    }

    void breadthFirstSearch(Node<V> vertex, Graph<V> tree, int maxVertices, int maxDepth) {
        if (maxDepth <= 0 ) return;
        clear();

        resetAttribs(vertex);
        vertex.visited = true;
        ArrayDeque<Node<V>> queue = new ArrayDeque<>();
        queue.add(vertex);

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
        clear();
    }

    void depthFirstSearch(Node<V> vertex, Graph<V> tree, int maxVertices, int maxDepth) {
        clear();

        resetAttribs(vertex);
        ArrayDeque<Node<V>> queue = new ArrayDeque<>();
        queue.add(vertex);

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
        clear();
    }

    boolean topologicalSort(List<V> sortedVertices) {
        sortedVertices.clear();
        clear();
        HashSet<Node<V>> set = new HashSet<>(graph.vertexMap.values());
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
        clear();
        HashSet<Node<V>> set = new HashSet<>(graph.vertexMap.values());
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
        clear();

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

        clear();
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
        clear();
        for (Node<V> v : graph.getNodes()) {
            resetAttribs(v);
            if (detectCycleDFS(v, null, new HashSet<>())) {
                clear();
                return true;
            }
        }
        clear();
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

