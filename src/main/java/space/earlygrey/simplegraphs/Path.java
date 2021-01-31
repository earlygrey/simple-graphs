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
import java.util.function.Predicate;

public class Path<V> extends Array<V> {

    float length = 0;
    boolean fixed = false;

    public Path(int size) {
        super(size, true);
    }

    Path(Node<V> v) {
        super(v.i + 1, true);
        setByBacktracking(v);
    }

    /**
     * @return the length of this path, that is, the sum of the edge weights of all edges contained in the path.
     */
    public float getLength() {
        return length;
    }

    void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    void checkFixed() {
        if (fixed) throw new UnsupportedOperationException("You cannot modify this path.");
    }

    void setByBacktracking(Node<V> node) {
        int nodeCount = node.i + 1;
        setFixed(false);
        if (items.length < nodeCount) strictResize(nodeCount);

        Node<V> v = node;
        while(v != null) {
            set(v.i, v.object);
            v = v.prev;
        }

        length = node.distance;
    }


    @Override
    public boolean add(V item) {
        checkFixed();
        return super.add(item);
    }

    @Override
    public V set(int index, V item) {
        checkFixed();
        return super.set(index, item);
    }

    @Override
    public boolean remove(Object item) {
        checkFixed();
        return super.remove(item);
    }

    @Override
    public boolean addAll(Collection<? extends V> collection) {
        checkFixed();
        return super.addAll(collection);
    }

    @Override
    public V remove(int index) {
        checkFixed();
        return super.remove(index);
    }

    @Override
    public void clear() {
        checkFixed();
        super.clear();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        checkFixed();
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        checkFixed();
        return super.retainAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super V> filter) {
        checkFixed();
        return false;
    }

    public V getFirst() {
        if (isEmpty()) throw new IllegalStateException("Path has no vertices.");
        return get(0);
    }

    public V getLast() {
        if (isEmpty()) throw new IllegalStateException("Path has no vertices.");
        return get(size-1);
    }

}
