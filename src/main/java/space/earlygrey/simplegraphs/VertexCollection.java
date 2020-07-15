package space.earlygrey.simplegraphs;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import space.earlygrey.simplegraphs.NodeMap.NodeIterator;

class VertexCollection<V> implements Collection<V> {

    final NodeMap<V> nodeMap;

    VertexCollection(NodeMap<V> nodeMap) {
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
    public Iterator<V> iterator() {
        return new VertexIterator<>(nodeMap);
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
    public boolean add(V v) {
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
    public boolean addAll(Collection<? extends V> collection) {
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
        VertexCollection<?> that = (VertexCollection<?>) o;
        return nodeMap.equals(that.nodeMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeMap);
    }

    static class VertexIterator<V> implements Iterator<V> {

        private final NodeIterator<V> nodeIterator;

        VertexIterator(NodeMap<V> nodeMap) {
            nodeIterator = new NodeIterator<>(nodeMap);
        }

        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        @Override
        public V next() {
            return nodeIterator.next().object;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("You cannot modify this list - use the Graph object.");
        }
    }


    @Override
    public String toString() {
        Iterator<V> it = this.iterator();
        if (!it.hasNext()) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append('[');

            while(true) {
                V v = it.next();
                sb.append(v);
                if (!it.hasNext()) {
                    return sb.append(']').toString();
                }

                sb.append(',').append(' ');
            }
        }
    }
}
