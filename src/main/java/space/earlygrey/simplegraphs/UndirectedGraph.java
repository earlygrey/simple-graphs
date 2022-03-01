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

import space.earlygrey.simplegraphs.Connection.UndirectedConnection;
import space.earlygrey.simplegraphs.algorithms.UndirectedGraphAlgorithms;
import space.earlygrey.simplegraphs.utils.WeightFunction;

public class UndirectedGraph<V> extends Graph<V> {

    UndirectedGraphAlgorithms<V> algorithms;

    //================================================================================
    // Constructors
    //================================================================================

    public UndirectedGraph() {
        super();
        algorithms = new UndirectedGraphAlgorithms<>(this);
    }

    public UndirectedGraph(Collection<V> vertices) {
        super(vertices);
        algorithms = new UndirectedGraphAlgorithms<>(this);
    }

    public UndirectedGraph(Graph<V> graph) {
        super(graph);
        algorithms = new UndirectedGraphAlgorithms<>(this);
    }


    //================================================================================
    // Graph building
    //================================================================================

    @Override
    protected UndirectedConnection<V> obtainEdge() {
        return new UndirectedConnection<>();
    }

    @Override
    Connection<V> addConnection(Node<V> a, Node<V> b, WeightFunction<V> weight) {
        Connection<V> e = a.getEdge(b);
        if (e == null) {
            UndirectedConnection<V> e1 = obtainEdge(), e2 = obtainEdge();
            e1.link(e2);
            e2.link(e1);
            e1.set(a, b, weight);
            e2.set(b, a, weight);
            a.addEdge(e1);
            b.addEdge(e2);
            edgeMap.put(e1, e1);
            e = e1;
        } else {
            e.setWeight(weight);
        }
        return e;
    }

    @Override
    Connection<V> addConnection(Node<V> a, Node<V> b) {
        Connection<V> e = a.getEdge(b);
        return e != null ? edgeMap.get(e) : addConnection(a, b, getDefaultEdgeWeightFunction());
    }

    @Override
    boolean removeConnection(Node<V> a, Node<V> b) {
        Connection<V> e = a.removeEdge(b);
        if (e == null) return false;
        b.removeEdge(a);
        edgeMap.remove(e);
        return true;
    }

    @Override
    Connection<V> getEdge(Node<V> a, Node<V> b) {
        Connection<V> edge = a.getEdge(b);
        return edge == null ? null : edgeMap.get(edge); // get from map to ensure consistent instance is returned
    }


    //================================================================================
    // Superclass implementations
    //================================================================================

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public UndirectedGraph<V> createNew() {
        return new UndirectedGraph<>();
    }

    @Override
    public UndirectedGraphAlgorithms<V> algorithms() {
        return algorithms;
    }


    //================================================================================
    // Misc
    //================================================================================

    /**
     * @return the degree of this vertex, or -1 if it is not in the graph
     */
    public int getDegree(V v) {
        Node<V> node = getNode(v);
        return node == null ? -1 : node.getOutDegree();
    }
}
