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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import space.earlygrey.simplegraphs.utils.WeightFunction;

public class Node<V> {

    //================================================================================
    // Graph structure related members
    //================================================================================

    final Graph<V> graph;
    final int idHash;
    final V object;

    Map<Node<V>, Connection<V>> neighbours = new LinkedHashMap<>();
    private Array<Connection<V>> outEdges = new Array<>();
    private Array<Connection<V>> inEdges;

    //================================================================================
    // Node map fields
    //================================================================================

    final int objectHash;
    int mapHash;
    Node<V> nextInOrder = null, prevInOrder = null;
    Node<V> nextInBucket = null;

    //================================================================================
    // Constructor
    //================================================================================

    Node(V v, Graph<V> graph, int objectHash) {
        this.object = v;
        this.graph = graph;
        this.objectHash = objectHash;
        idHash = System.identityHashCode(this);
        if (graph instanceof DirectedGraph) setInEdges(new Array<>());
    }

    //================================================================================
    // Internal methods
    //================================================================================

    Connection<V> getEdge(Node<V> v) {
        return neighbours.get(v);
    }

    Connection<V> addEdge(Node<V> v, WeightFunction<V> weight) {
        Connection<V> edge = neighbours.get(v);
        if (edge == null) {
            edge = graph.obtainEdge();
            edge.set(this, v, weight);
            neighbours.put(v, edge);
            getOutEdges().add(edge);
            if (v.getInEdges() != null) v.getInEdges().add(edge);
        } else {
            edge.setWeight(weight);
        }
        return edge;
    }

    Connection<V> removeEdge(Node<V> v) {
        Connection<V> edge = neighbours.remove(v);
        if (edge == null) return null;
        getOutEdges().remove(edge);
        if (v.getInEdges() != null) v.getInEdges().remove(edge);
        return edge;
    }

    void disconnect() {
        neighbours.clear();
        getOutEdges().clear();
        if (getInEdges() != null) getInEdges().clear();
    }

    //================================================================================
    // Public Methods
    //================================================================================

    public Collection<Connection<V>> getConnections() {
        return getOutEdges();
    }

    public V getObject() {
        return object;
    }

    public int getInDegree() {
        return getInEdges() == null ? getOutDegree() : getInEdges().size();
    }

    public int getOutDegree() {
        return getOutEdges().size();
    }

    //================================================================================
    // Algorithm fields and methods
    //================================================================================

    // util fields for algorithms, don't store data in them
    private boolean processed;
    private boolean seen;
    private float distance;
    private float estimate;
    private Node<V> prev;
    private Connection<V> connection;
    private int index;
    private int lastRunID = -1;

    public boolean resetAlgorithmAttribs(int runID) {
        if (runID == this.getLastRunID()) return false;
        setProcessed(false);
        setPrev(null);
        setConnection(null);
        setDistance(Float.MAX_VALUE);
        setEstimate(0);
        setIndex(0);
        setSeen(false);
        this.setLastRunID(runID);
        return true;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getEstimate() {
        return estimate;
    }

    public void setEstimate(float estimate) {
        this.estimate = estimate;
    }

    public Node<V> getPrev() {
        return prev;
    }

    public void setPrev(Node<V> prev) {
        this.prev = prev;
    }

    public Connection<V> getConnection() {
        return connection;
    }

    public void setConnection(Connection<V> connection) {
        this.connection = connection;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLastRunID() {
        return lastRunID;
    }

    public void setLastRunID(int lastRunID) {
        this.lastRunID = lastRunID;
    }


    //================================================================================
    // Heap fields
    //================================================================================

    int heapIndex;
    float heapValue;

    //================================================================================
    // Misc
    //================================================================================


    @Override
    public boolean equals(Object o) {
        return o == this;
    }


    @Override
    public int hashCode() {
        return idHash;
    }

    @Override
    public String toString() {
        return "["+object+"]";
    }

    public Array<Connection<V>> getOutEdges() {
        return outEdges;
    }

    public void setOutEdges(Array<Connection<V>> outEdges) {
        this.outEdges = outEdges;
    }

    public Array<Connection<V>> getInEdges() {
        return inEdges;
    }

    public void setInEdges(Array<Connection<V>> inEdges) {
        this.inEdges = inEdges;
    }
}
