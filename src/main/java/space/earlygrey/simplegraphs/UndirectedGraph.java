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


    //================================================================================
    // Graph building
    //================================================================================

    @Override
    protected Connection<V> obtainEdge() {
        return new UndirectedConnection<>();
    }

    @Override
    Connection<V> addConnection(Node<V> a, Node<V> b, WeightFunction<V> weight) {
        Connection<V> e = a.addEdge(b, weight);
        edgeMap.put(e, e);
        b.addEdge(a, weight);
        return e;
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
        if (edge == null) return null;
        edge = edgeMap.get(edge);
        return edge;
    }


    //================================================================================
    // Superclass implementations
    //================================================================================

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    Graph<V> createNew() {
        return new UndirectedGraph<>();
    }

    @Override
    public UndirectedGraphAlgorithms<V> algorithms() {
        return algorithms;
    }


    //================================================================================
    // Misc
    //================================================================================

    public int getDegree(V v) {
        Node<V> node = getNode(v);
        return node == null ? -1 : node.getOutDegree();
    }
}
