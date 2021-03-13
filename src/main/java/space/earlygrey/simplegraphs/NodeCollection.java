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

import java.lang.reflect.Array;
import java.util.*;

import space.earlygrey.simplegraphs.NodeMap.NodeIterator;

class NodeCollection<V> extends AbstractCollection<Node<V>> {

    NodeMap<V> nodeMap;

    NodeCollection(NodeMap<V> nodeMap) {
        this.nodeMap = nodeMap;
    }

    @Override
    public int size() {
        return nodeMap.size;
    }

    @Override
    public boolean contains(Object o) {
        return nodeMap.contains((V) o);
    }

    @Override
    public Iterator<Node<V>> iterator() {
        return new NodeIterator<>(nodeMap);
    }


    @Override
    public Object[] toArray() {
        Object[] array = new Object[nodeMap.size];
        int index = 0;
        for (int i = 0; i < nodeMap.table.length; i++) {
            Node<V> node = nodeMap.table[i];
            while (node != null) {
                array[index++] = node.object;
                node = node.nextInBucket;
            }
        }
        return array;
    }


    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < nodeMap.size) {
            array = Arrays.copyOf(array, nodeMap.size);
        }
        int index = 0;
        for (int i = 0; i < nodeMap.table.length; i++) {
            Node<V> node = nodeMap.table[i];
            while (node != null) {
                array[index++] = (T) node.object;
                node = node.nextInBucket;
            }
        }
        return array;
    }


    @Override
    public boolean add(Node<V> v) {
        Errors.throwModificationException();
        return false;
    }

    @Override
    public boolean remove(Object o) {
        Errors.throwModificationException();
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object e : collection) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Node<V>> collection) {
        Errors.throwModificationException();
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        Errors.throwModificationException();
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Errors.throwModificationException();
        return false;
    }

    @Override
    public void clear() {
        Errors.throwModificationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeCollection<?> that = (NodeCollection<?>) o;
        return nodeMap.equals(that.nodeMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeMap);
    }

}
