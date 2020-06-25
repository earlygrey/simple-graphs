package space.earlygrey.simplegraphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import space.earlygrey.simplegraphs.Heuristic.DefaultHeuristic;

class Algorithms<V> {

    private final AbstractGraph<V> graph;
    private final PriorityQueue<Node> priorityQueue;
    private final HashSet<Node<V>> isReset;
    final Heuristic<V> defaultHeuristic = new DefaultHeuristic<>();

    public Algorithms(AbstractGraph<V> graph) {
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

    /*Map<Node<T>, Float> findAllShortestDistances(Node<T> start) {
        performShortestPathSearch(start, null, defaultHeuristic);
        priorityQueue.clear();
        Map<Node<T>, Float> distances = new HashMap<>();
        for (Node<T> v : graph.getNodes()) {
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

/*
    List<Node<T>> getComponent(Node<T> vertex) {
        return getComponent(vertex, Float.MAX_VALUE);
    }
    List<Node<T>> getComponent(Node<T> vertex, float max) {
        ArrayList<Node<T>> nodeList = new ArrayList<>();

        clear();

        resetAttribs(vertex);
        vertex.visited = true;
        vertex.distance = 0;

        priorityQueue.add(vertex);
        isReset.add(vertex);

        while(!priorityQueue.isEmpty()) {
            Node<T> v = priorityQueue.poll();
            nodeList.add(v);
            for (Connection e : v.connections.values()) {
                Node<T> w = e.b;
                resetAttribs(w);
                if (!w.visited) {
                    w.visited = true;
                    w.distance = v.distance + e.getWeight();
                    if (w.distance <= max) priorityQueue.add(w);
                }
            }

        }
        priorityQueue.clear();
        return nodeList;
    }

    List<List<T>> getComponents(Graph<T> graph) {
        Set<Node<T>> nodeSet = new HashSet();
        nodeSet.addAll(graph.getNodes());
        List<List<Node<T>>> components = new ArrayList<>();
        while(!nodeSet.isEmpty()) {
            List<Node<T>> nodeList = getComponent(nodeSet.iterator().next());
            components.add(nodeList);
            nodeSet.removeAll(nodeList);
        }
        List<List<T>> objectComponents = new ArrayList<>(components.size());
        for (List<Node<T>> component : components) {
            List<T> objectNodes = new ArrayList<>();
            for (Node<T> node : component) {
                objectNodes.add(node.object);
            }
            objectComponents.add(objectNodes);
        }
        return objectComponents;
    }

    boolean containsCycle(Graph<T> graph) {
        if (graph.size() < 3 || graph.getEdgeCount() < 3) return false;
        clear();
        for (Node<T> v : graph.getNodes()) {
            if (cycleDFS(v, new HashSet<>())) {
                clear();
                return true;
            }
        }
        clear();
        return false;
    }

    private boolean cycleDFS(Node<T> v, Set<Node<T>> recursiveStack) {
        resetAttribs(v);
        v.visited = true;
        recursiveStack.add(v);
        for (Connection<T> e : v.connections.values()) {
            if (recursiveStack.contains(e.b)) return true;
            if (!e.b.visited) {
                if (cycleDFS(e.b, recursiveStack)) return true;
            }
        }
        recursiveStack.remove(v);
        return false;
    }*/

    private void resetAttribs(Node<V> v) {
        boolean needsReset = isReset.add(v);
        if (needsReset) {
            v.resetAlgorithmAttribs();
            isReset.add(v);
        }
    }

}

