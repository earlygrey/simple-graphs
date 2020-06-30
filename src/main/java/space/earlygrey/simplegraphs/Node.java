package space.earlygrey.simplegraphs;

import java.util.LinkedHashMap;
import java.util.Map;


class Node<V> {

    final Graph<V> graph;
    final int idHash;
    final V object;

    Map<Node<V>, Connection<V>> neighbours = new LinkedHashMap<>();
    Array<Connection<V>> outEdges = new Array<>();

    Node (V v, Graph<V> graph) {
        this.object = v;
        this.graph = graph;
        idHash = System.identityHashCode(this);
    }

    Connection<V> getEdge(Node<V> v) {
        return neighbours.get(v);
    }

    Connection<V> addEdge(Node<V> v, float weight) {
        Connection<V> edge = neighbours.get(v);
        if (edge == null) {
            edge = graph.edgeSupplier.get();
            edge.set(this, v, weight);
            neighbours.put(v, edge);
            outEdges.add(edge);
            return edge;
        } else {
            edge.setWeight(weight);
        }
        return edge;
    }
    Connection<V> removeEdge(Node<V> v) {
        Connection<V> edge = neighbours.remove(v);
        if (edge == null) return null;
        outEdges.remove(edge);
        return edge;
    }

    void disconnect() {
        neighbours.clear();
        outEdges.clear();
    }

    //util fields for algorithms, don't store data in them
    boolean visited, seen;
    float distance;
    float estimate;
    Node<V> prev;
    int i, runID;

    boolean resetAlgorithmAttribs(int runID) {
        if (runID == this.runID) return false;
        visited = false;
        prev = null;
        distance = Float.MAX_VALUE;
        estimate = 0;
        i = 0;
        seen = false;
        this.runID = runID;
        return true;
    }


    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public String toString() {
        return "["+object+"]";
    }


    @Override
    public int hashCode() {
        return idHash;
    }
}
