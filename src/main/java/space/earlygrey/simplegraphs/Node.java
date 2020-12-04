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

public class Node<V> {

    //================================================================================
    // Graph structure related members
    //================================================================================

    final Graph<V> graph;
    final int idHash;
    final V object;

    Map<Node<V>, Connection<V>> neighbours = new LinkedHashMap<>();
    Array<Connection<V>> outEdges = new Array<>();
    Array<Connection<V>> inEdges;

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
        if (graph instanceof DirectedGraph) inEdges = new Array<>();
    }

    //================================================================================
    // Internal methods
    //================================================================================

    Connection<V> getEdge(Node<V> v) {
        return neighbours.get(v);
    }

    Connection<V> addEdge(Node<V> v, float weight) {
        Connection<V> edge = neighbours.get(v);
        if (edge == null) {
            edge = graph.obtainEdge();
            edge.set(this, v, weight);
            neighbours.put(v, edge);
            outEdges.add(edge);
            if (v.inEdges != null) v.inEdges.add(edge);
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
        if (v.inEdges != null) v.inEdges.remove(edge);
        return edge;
    }

    void disconnect() {
        neighbours.clear();
        outEdges.clear();
        if (inEdges != null) inEdges.clear();
    }

    //================================================================================
    // Public Methods
    //================================================================================

    public Collection<Connection<V>> getConnections() {
        return outEdges;
    }

    public V getObject() {
        return object;
    }

    public int getInDegree() {
        return inEdges == null ? getOutDegree() : inEdges.size();
    }

    public int getOutDegree() {
        return outEdges.size();
    }

    //================================================================================
    // Algorithm fields and methods
    //================================================================================

    // util fields for algorithms, don't store data in them
    boolean visited, seen;
    float distance;
    float estimate;
    Node<V> prev;
    int i, lastRunID;

    boolean resetAlgorithmAttribs(int runID) {
        if (runID == this.lastRunID) return false;
        visited = false;
        prev = null;
        distance = Float.MAX_VALUE;
        estimate = 0;
        i = 0;
        seen = false;
        this.lastRunID = runID;
        return true;
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
}
