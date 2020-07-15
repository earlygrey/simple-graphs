package space.earlygrey.simplegraphs;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import space.earlygrey.simplegraphs.NodeMap.NodeIterator;

class NodeCollection<V> implements Collection<Node<V>> {

    NodeMap<V> nodeMap;

    NodeCollection(NodeMap<V> nodeMap) {
        this.nodeMap = nodeMap;
    }

    @Override
    public int size() {
        return nodeMap.size;
    }

    @Override
    public boolean isEmpty() {
        return nodeMap.size == 0;
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
        throw new UnsupportedOperationException(NodeMap.MODIFY_EXCEPTION);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(NodeMap.MODIFY_EXCEPTION);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Node<V>> collection) {
        throw new UnsupportedOperationException(NodeMap.MODIFY_EXCEPTION);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException(NodeMap.MODIFY_EXCEPTION);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException(NodeMap.MODIFY_EXCEPTION);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(NodeMap.MODIFY_EXCEPTION);
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

    @Override
    public String toString() {
        Iterator<Node<V>> it = this.iterator();
        if (!it.hasNext()) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append('[');

            while(true) {
                Node<V> v = it.next();
                sb.append(v);
                if (!it.hasNext()) {
                    return sb.append(']').toString();
                }

                sb.append(',').append(' ');
            }
        }
    }
}
