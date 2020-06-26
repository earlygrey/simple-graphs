package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import space.earlygrey.simplegraphs.Heuristic.DefaultHeuristic;

class Algorithms<V> {

    private final Graph<V> graph;
    private final PriorityQueue<Node> priorityQueue;
    private final HashSet<Node<V>> isReset;
    final Heuristic<V> defaultHeuristic = new DefaultHeuristic<>();

    public Algorithms(Graph<V> graph) {
        this.graph = graph;
        priorityQueue = new PriorityQueue<>((o1, o2) -> (int) Math.signum(o1.distance+o1.estimate - (o2.distance+o2.estimate)));
        isReset = new HashSet<>();
    }

    private void clear() {
        isReset.clear();
        priorityQueue.clear();
    }

    boolean isReachable(Node<V> start, Node<V> target) {
        return findShortestPath(start, target, new ArrayList<>());
    }

    float findMinimumDistance(Node<V> start, Node<V> target) {
        Node<V> end = performShortestPathSearch(start, target, defaultHeuristic);
        if (end==null) return Float.MAX_VALUE;
        else return end.distance;
    }

    List<Node<V>> findShortestPath(Node<V> start, Node<V> target) {
        ArrayList<Node<V>> nodeList = new ArrayList<>();
        findShortestPath(start, target, nodeList);
        return nodeList;
    }

    boolean findShortestPath(Node<V> start, Node<V> target, List<Node<V>> path) {
        return findShortestPath(start, target, path, defaultHeuristic);
    }

    List<Node<V>> findShortestPath(Node<V> start, Node<V> target, Heuristic<V> heuristic) {
        ArrayList<Node<V>> nodeList = new ArrayList<>();
        findShortestPath(start, target, nodeList, heuristic);
        return nodeList;
    }

    boolean findShortestPath(Node<V> start, Node<V> target, List<Node<V>> path, Heuristic<V> heuristic) {
        Node<V> end = performShortestPathSearch(start, target, heuristic);
        path.clear();
        if (end==null) {
            priorityQueue.clear();
            return false;
        }

        Node<V> v = end;
        while(v.prev!=null) {
            path.add(v);
            v = v.prev;
        }
        path.add(start);
        Collections.reverse(path);
        priorityQueue.clear();
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

    private Node<V> performShortestPathSearch(Node<V> start, Node<V> target, Heuristic<V> heuristic) {

        clear();

        resetAttribs(start);
        start.distance = 0;

        priorityQueue.add(start);
        isReset.add(start);

        while(!priorityQueue.isEmpty()) {
            Node<V> u = priorityQueue.poll();
            if (u == target) {
                clear();
                return u;
            }
            if (u.visited) continue;
            u.visited = true;
            for (Connection e : u.connections.values()) {
                Node<V> v = e.b;
                resetAttribs(v);
                if (!v.visited) {
                    float newDistance = u.distance + e.getWeight();
                    if (newDistance < v.distance) {
                        v.distance = newDistance;
                        v.prev = u;
                        v.estimate = heuristic.getEstimate(v.object, target.object);
                        priorityQueue.add(v);
                    }
                }
            }
        }
        clear();
        return null;
    }

    void findComponent(Node<V> vertex, List<V> vertices, float maxDistance) {
        vertices.clear();
        clear();

        resetAttribs(vertex);
        vertex.visited = true;
        vertex.distance = 0;

        priorityQueue.add(vertex);
        isReset.add(vertex);

        while(!priorityQueue.isEmpty()) {
            Node<V> v = priorityQueue.poll();
            vertices.add(v.object);
            for (Connection e : v.connections.values()) {
                Node<V> w = e.b;
                resetAttribs(w);
                if (!w.visited) {
                    w.visited = true;
                    w.distance = v.distance + e.getWeight();
                    if (w.distance <= maxDistance) priorityQueue.add(w);
                }
            }

        }

        clear();
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

    boolean containsCycle(Graph<V> graph) {
        if (graph.size() < 3 || graph.getEdgeCount() < 3) return false;
        clear();
        for (Node<V> v : graph.getNodes()) {
            resetAttribs(v);
            if (detectCycleDFS(v, new HashSet<>())) {
                clear();
                return true;
            }
        }
        clear();
        return false;
    }

    private boolean detectCycleDFS(Node<V> v, Set<Node<V>> recursiveStack) {
        v.visited = true;
        recursiveStack.add(v);
        for (Connection<V> e : v.connections.values()) {
            resetAttribs(e.b);
            if (recursiveStack.contains(e.b)) {
                return true;
            }
            if (!e.b.visited) {
                if (detectCycleDFS(e.b, recursiveStack)) return true;
            }
        }
        recursiveStack.remove(v);
        return false;
    }

    private void resetAttribs(Node<V> v) {
        boolean needsReset = isReset.add(v);
        if (needsReset) {
            v.resetAlgorithmAttribs();
            isReset.add(v);
        }
    }

}

