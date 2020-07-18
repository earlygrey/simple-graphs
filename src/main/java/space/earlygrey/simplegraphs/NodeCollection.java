package space.earlygrey.simplegraphs;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

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
        return new NodeIterator<>(nodeMap, true);
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
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), nodeMap.size);
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
