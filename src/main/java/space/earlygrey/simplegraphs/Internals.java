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

import space.earlygrey.simplegraphs.utils.WeightFunction;

public class Internals<V> {

    final Graph<V> graph;

    Internals(Graph<V> graph) {
        this.graph = graph;
    }

    public Node<V> getNode(V v) {
        return graph.getNode(v);
    }

    public Collection<Node<V>> getNodes() {
        return graph.nodeMap.nodeCollection;
    }

    public Collection<Connection<V>> getConnections() {
        return graph.edgeMap.values();
    }

    public void addConnection(Node<V> a, Node<V> b, WeightFunction<V> weightFunction) {
        graph.addConnection(a, b, weightFunction);
    }
}
