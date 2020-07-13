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

import java.util.Map.Entry;

public class GraphBuilder{

    private GraphBuilder() {
    }

    public static <V, G extends Graph<V>> void buildCompleteGraph(G graph) {
        for (Entry<V, Node<V>> entry1 : graph.vertexMap.entrySet()) {
            for (Entry<V, Node<V>> entry2 : graph.vertexMap.entrySet()) {
                Node<V> a = entry1.getValue(), b = entry2.getValue();
                if (!a.equals(b)) {
                    Connection<V> e = a.getEdge(b);
                    if (e == null) {
                        graph.addConnection(a, b);
                    }
                    if (graph.isDirected()) {
                        e = b.getEdge(a);
                        if (e == null) {
                            graph.addConnection(b, a);
                        }
                    }
                }
            }
        }
    }

}
