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

import space.earlygrey.simplegraphs.Connection.DirectedConnection;

import java.util.Collection;
import java.util.Collections;

public class DirectedGraph<V> extends Graph<V> {

    final DirectedGraphAlgorithms<V> algorithms;

    //================================================================================
    // Constructors
    //================================================================================

    public DirectedGraph () {
        super();
        algorithms = new DirectedGraphAlgorithms<>(this);
    }

    public DirectedGraph (Collection<V> vertices) {
        super(vertices);
        algorithms = new DirectedGraphAlgorithms<>(this);
    }


    //================================================================================
    // Superclass implementations
    //================================================================================

    @Override
    protected Connection<V> obtainEdge() {
        return new DirectedConnection<>();
    }

    @Override
    Graph<V> createNew() {
        return new DirectedGraph<>();
    }

    @Override
    public DirectedGraphAlgorithms<V> algorithms() {
        return algorithms;
    }


    //================================================================================
    // Misc
    //================================================================================

    public int getOutDegree(V v) {
        Node<V> node = getNode(v);
        return node == null ? -1 : node.getOutDegree();
    }

    public int getInDegree(V v) {
        Node<V> node = getNode(v);
        return node == null ? -1 : node.getInDegree();
    }

    /**
     * Get a collection containing all the edges which have v as a head.
     * That is, for every edge e in the collection, e = (u, v) for some vertex u.
     * @param v the vertex which all edges will have as a head
     * @return an unmodifiable collection of edges
     */
    public Collection<Edge<V>> getInEdges(V v) {
        Node<V> node = getNode(v);
        if (node==null) return null;
        return Collections.unmodifiableCollection(node.outEdges);
    }


}
