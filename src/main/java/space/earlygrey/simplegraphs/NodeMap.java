package space.earlygrey.simplegraphs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

class NodeMap<V> {

    final Graph<V> graph;
    Node<V>[] table;

    Node<V> head, tail;

    int size = 0;
    int threshold = -1;
    static final int MIN_SIZE = 32;

    static final String MODIFY_EXCEPTION = "You cannot modify this list - use the Graph object.";

    public NodeMap(Graph<V> graph) {
        this.graph = graph;
        table = new Node[MIN_SIZE / 2];
        checkLength();
    }

    Node<V> get(V v) {
        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> head = table[i];
        if (head == null) return null;

        Node<V> currentNode = head;
        while (currentNode != null) {
            if (v.equals(currentNode.object)) return currentNode;
            currentNode = currentNode.nextInBucket;
        }

        return null;
    }

    boolean contains(V v) {
        return get(v) != null;
    }

    Node<V> put(V v) {
        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> head = table[i];
        if (head == null) {
            if (checkLength()) i = getIndex(hash);
            head = new Node<>(v, graph, objectHash);
            head.mapHash = hash;
            table[i] = head;
            size++;
            addToList(head);
            return head;
        }

        Node<V> currentNode = head, previousNode = null;
        while (currentNode != null) {
            if (v.equals(currentNode.object)) return null;
            previousNode = currentNode;
            currentNode = currentNode.nextInBucket;
        }

        checkLength();
        currentNode = new Node<>(v, graph, objectHash);
        currentNode.mapHash = hash;
        previousNode.nextInBucket = currentNode;
        size++;
        addToList(currentNode);
        return currentNode;
    }

    void addToList(Node<V> node) {
        if (head == null) {
            head = node;
            tail = node;
            return;
        }
        node.prevInOrder = tail;
        tail.nextInOrder = node;
        tail = node;
    }

    Node<V> remove(V v) {
        int objectHash = v.hashCode(), hash = hash(objectHash);
        int i = getIndex(hash);
        Node<V> currentNode = table[i];

        if (currentNode == null) {
            table[i] = null;
            return null;
        }

        Node<V> previousNode = null;
        while (currentNode != null) {
            if (v.equals(currentNode.object)) {
                if (previousNode != null) previousNode.nextInBucket = currentNode.nextInBucket;
                size--;
                return currentNode;
            }
            previousNode = currentNode;
            currentNode = currentNode.nextInBucket;
        }

        return null;
    }

    void removeFromList(Node<V> node) {
        if (head == node) {
            head = node.nextInOrder;
            if (head != null) head.prevInOrder = null;
        }
        if (tail == node) {
            tail = node.prevInOrder;
            if (tail != null) tail.nextInOrder = null;
        }
        node.prevInOrder.nextInOrder = node.nextInOrder;
        node.nextInOrder.prevInOrder = node.prevInOrder;
    }

    boolean checkLength() {
        if (size > threshold) {
            int newLength = 2 * table.length;
            Node<V>[] oldTable = table, newTable = new Node[newLength];
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] != null) {
                    Node<V> tail1 = null, tail2 = null, current = oldTable[i];
                    while (current != null) {
                        int newIndex = getIndex(current.mapHash);
                        if (newIndex == i) {
                            if (tail1 == null) newTable[newIndex] = current;
                            else tail1.nextInBucket = current;
                            tail1 = current;
                        } else {
                            if (tail2 == null) newTable[newIndex] = current;
                            else tail2.nextInBucket = current;
                            tail2 = current;
                        }
                        current.nextInBucket = null;
                    }
                }
            }
            threshold = (int) (0.7 * newLength);
            table = newTable;
            return true;
        }
        return false;
    }

    void clear() {
        Arrays.fill(table, null);
        size = 0;
    }


    int getIndex(int hash) {
        return getIndex(hash, table.length);
    }
    static int getIndex(int hash,  int length) {
        return hash & (length - 1);
    }

    static int hash(Object v) {
        int hashcode = v.hashCode();
        return hashcode ^ (hashcode >>> 16);
    }

    static class NodeIterator<V> implements Iterator<Node<V>> {

        final NodeMap<V> nodeMap;
        private Node<V> lastReturned;
        private Node<V> next;
        private int nextIndex;

        NodeIterator(NodeMap<V> nodeMap) {
            this.nodeMap = nodeMap;
        }

        @Override
        public boolean hasNext() {
            return this.nextIndex < nodeMap.size;
        }

        @Override
        public Node<V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                lastReturned = next;
                next = next.nextInOrder;
                nextIndex++;
                return lastReturned;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("You cannot modify this list - use the Graph object.");
        }

    }


}
