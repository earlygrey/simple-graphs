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

import space.earlygrey.simplegraphs.utils.WeightFunction;

public abstract class Connection<V> extends Edge<V> {

    //================================================================================
    // Fields and constants
    //================================================================================

    Node<V> a, b;

    WeightFunction<V> weight;

    //================================================================================
    // Constructor
    //================================================================================

    Connection() {

    }

    //================================================================================
    // Internal methods
    //================================================================================

    @Override
    void set(Node<V> a, Node<V> b, WeightFunction<V> weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    @Override
    Node<V> getInternalNodeA() {
        return a;
    }

    @Override
    Node<V> getInternalNodeB() {
        return b;
    }

    //================================================================================
    // Public methods
    //================================================================================

    @Override
    public V getA() {
        return a.object;
    }

    @Override
    public V getB() {
        return b.object;
    }

    @Override
    public float getWeight() {
        return weight.getWeight(getA(), getB());
    }

    @Override
    public void setWeight(float weight) {
        this.weight = (a, b) -> weight;
    }

    @Override
    public void setWeight(WeightFunction<V> weight) {
        this.weight = weight;
    }

    @Override
    public WeightFunction<V> getWeightFunction() {
        return weight;
    }

    public Node<V> getNodeA() {
        return a;
    }

    public Node<V> getNodeB() {
        return b;
    }

    //================================================================================
    // Subclasses
    //================================================================================

    static class DirectedConnection<V> extends Connection<V> {

        @Override
        public boolean hasEndpoints(V u, V v) {
            return getA().equals(u) && getB().equals(v);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Connection edge = (Connection) o;
            // this assumes a and b are non-null when equals() is called.
            return a.equals(edge.a) && b.equals(edge.b);
        }

        @Override
        public int hashCode() {
            return (int) (a.hashCode() * 0xC13FA9A902A6328FL + b.hashCode() * 0x91E10DA5C79E7B1DL >>> 32);
        }

        @Override
        public String toString() {
            return "{" + a + " -> " + b +'}';
        }

    }

    static class UndirectedConnection<V> extends Connection<V> {

        @Override
        public boolean hasEndpoints(V u, V v) {
            return hasEndpoint(u) && hasEndpoint(v);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Connection edge = (Connection) o;
            // this assumes a and b are non-null when equals() is called.
            return (a.equals(edge.a) && b.equals(edge.b))
                    || (a.equals(edge.b) && b.equals(edge.a));
        }

        @Override
        public int hashCode() {
            return (int) ((a.hashCode() + b.hashCode()) * 0x9E3779B97F4A7C15L >>> 32);
        }

        @Override
        public String toString() {
            return "{" + a + " <> " + b +'}';
        }
    }

}
